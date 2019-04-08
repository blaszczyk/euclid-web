package euclid.web.job;

import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.inject.Named;

import euclid.algorithm.Algorithm;
import euclid.algorithm.CurveBasedSearch;
import euclid.engine.EngineParameters;
import euclid.engine.SearchEngine;
import euclid.kpi.KpiCsvWriter;
import euclid.kpi.KpiMonitor;
import euclid.model.Algebra;
import euclid.model.Board;
import euclid.problem.Problem;

public class JobManager {
	
	@Inject
	@Named("root-dir")
	private File rootDir;
	
	@Inject
	private Algebra algebra;
	
	private final Map<String, Job> jobs = new ConcurrentHashMap<>();

	public String createAndStartJob(final Problem problem) {
		final Algorithm<Board> algorithm = new CurveBasedSearch(problem, algebra);
		final int threadCount = Runtime.getRuntime().availableProcessors();
		final String jobId = String.format("%08X", algorithm.hashCode());
		final EngineParameters parameters = new EngineParameters(jobId, false, threadCount, false);
		final SearchEngine<Board> engine = new SearchEngine<>(algorithm, parameters);
		
		final KpiMonitor monitor = new KpiMonitor(5000);
		engine.kpiReporters().forEach(monitor::addReporter);
		monitor.addConsumer(new KpiCsvWriter(new File(rootDir, "log")));

		final Job job = new Job(problem, engine, monitor);
		jobs.put(jobId, job);
		monitor.start();
		engine.start(true);
		return jobId;
	}
	
	public boolean isFinished(final String jobId) {
		return job(jobId).getEngine().finished();
	}
	
	public void halt(final String jobId) {
		job(jobId).getEngine().halt();
		job(jobId).getKpiMonitor().halt();
	}
	
	public Optional<Board> getSolution(final String jobId) {
		return job(jobId).getEngine().firstSolution();
	}
	
	public Problem getProblem(final String jobId) {
		return job(jobId).getProblem();
	}
	
	public void removeJob(final String jobId) {
		job(jobId).getKpiMonitor().halt();
		jobs.remove(jobId);
	}
	
	private Job job(final String jobId) {
		return jobs.get(jobId);
	}

}
