package euclid.web.job;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.inject.Named;

import euclid.algorithm.Algorithm;
import euclid.algorithm.CurveBasedSearch;
import euclid.algorithm.PointBasedSearch;
import euclid.engine.EngineParameters;
import euclid.engine.SearchEngine;
import euclid.kpi.KpiCsvWriter;
import euclid.kpi.KpiMonitor;
import euclid.kpi.KpiReporter;
import euclid.kpi.KpiStdoutLogger;
import euclid.model.Algebra;
import euclid.model.BasicCurveLifeCycle;
import euclid.model.Board;
import euclid.model.CachedCurveLifeCycle;
import euclid.model.CachedIntersectionAlgebra;
import euclid.model.CurveLifeCycle;
import euclid.problem.Problem;
import euclid.web.config.Config;

public class JobManager {
	
	@Inject
	@Named("root-dir")
	private File rootDir;
	
	@Inject
	private Config config;
	
	private final Map<String, Job> jobs = new ConcurrentHashMap<>();

	public String createAndStartJob(final Problem problem) {

		final String jobId = String.format("%08X", problem.hashCode());
		final Job job = createJob(problem, jobId);
		jobs.put(jobId, job);
		job.start();
		return jobId;
	}
	
	public Job job(final String jobId) {
		return jobs.get(jobId);
	}
	
	public void halt(final String jobId) {
		job(jobId).halt();
	}
	
	public void removeJob(final String jobId) {
		job(jobId).halt();
		jobs.remove(jobId);
	}
	
	private Job createJob(final Problem problem, final String jobId) {
		final KpiMonitor monitor = new KpiMonitor(config.getInt("kpi.interval"));
		
		final CurveLifeCycle lifeCycle = config.getBoolean("cache.curves") ? new CachedCurveLifeCycle() : new BasicCurveLifeCycle();
		monitor.addReporter(lifeCycle);
		final Algebra algebra = config.getBoolean("cache.intersections") ? new CachedIntersectionAlgebra(lifeCycle) : new Algebra(lifeCycle);
		if(algebra instanceof KpiReporter) {
			monitor.addReporter((KpiReporter) algebra);
		}
		
		final Algorithm<Board> algorithm = createAlgorithm(problem, algebra);

		final EngineParameters parameters = new EngineParameters(jobId, false, threadCount(), config.getBoolean("cache.candidates"));
		final SearchEngine<Board> engine = new SearchEngine<>(algorithm, parameters);

		engine.kpiReporters().forEach(monitor::addReporter);

		if(config.getBoolean("kpi.csv")) {
			monitor.addConsumer(new KpiCsvWriter(new File(rootDir, "log")));
		}
		if(config.getBoolean("kpi.out")) {
			monitor.addConsumer(new KpiStdoutLogger());
		}
		
		return new Job(problem, engine, monitor);
	}

	private Algorithm<Board> createAlgorithm(final Problem problem, final Algebra algebra) {
		switch (problem.algorithmType()) {
		case CURVE_BASED:
			return new CurveBasedSearch(problem, algebra);
		case POINT_BASED:
			return new PointBasedSearch(problem, algebra);
		}
		return null;
	}
	
	private int threadCount() {
		if(config.getString("engine.threads").equalsIgnoreCase("max")) {
			return Runtime.getRuntime().availableProcessors();
		}
		return config.getInt("engine.threads");
	}

}
