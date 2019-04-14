package euclid.web.job;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.inject.Named;

import euclid.algorithm.Algorithm;
import euclid.engine.EngineParameters;
import euclid.engine.SearchEngine;
import euclid.kpi.KpiCsvWriter;
import euclid.kpi.KpiMonitor;
import euclid.kpi.KpiStdoutLogger;
import euclid.kpi.SystemKpi;
import euclid.sets.Board;
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

		final Algorithm<? extends Board> algorithm = problem.algorithm().create(problem);
		final EngineParameters parameters = new EngineParameters(jobId, 1, problem.depthFirst(), threadCount(), config.getInt("engine.dedupedepth"));
		final SearchEngine<? extends Board> engine = new SearchEngine<>(algorithm, parameters);

		engine.kpiReporters().forEach(monitor::addReporter);
		monitor.addReporter(new SystemKpi());

		if(config.getBoolean("kpi.csv")) {
			monitor.addConsumer(new KpiCsvWriter(new File(rootDir, "log")));
		}
		if(config.getBoolean("kpi.out")) {
			monitor.addConsumer(new KpiStdoutLogger());
		}
		
		return new Job(problem, engine, monitor);
	}
	
	private int threadCount() {
		if(config.getString("engine.threads").equalsIgnoreCase("max")) {
			return Runtime.getRuntime().availableProcessors();
		}
		return config.getInt("engine.threads");
	}

}
