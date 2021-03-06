package euclid.web.job;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.inject.Named;

import euclid.algorithm.Algorithm;
import euclid.algorithm.AlgorithmFactory;
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

	public void removeJob(final String jobId) {
		job(jobId).halt();
		jobs.remove(jobId);
	}

	private Job createJob(final Problem problem, final String jobId) {
		final KpiMonitor monitor = new KpiMonitor(config.getInt("kpi.interval"));

		final Algorithm<Board> algorithm = AlgorithmFactory.create(problem);
		final EngineParameters parameters = new EngineParameters(jobId, 1, problem.depthFirst(), problem.shuffle(),
				threadCount(), config.getInt("engine.bunchsize"), config.getInt("engine.maxqueuesize"));
		final SearchEngine<Board> engine = new SearchEngine<>(algorithm, parameters, monitor);
		final Job job = new Job(problem, engine);

		monitor.addReporter(new SystemKpi());

		if(config.getBoolean("kpi.csv")) {
			monitor.addConsumer(new KpiCsvWriter(new File(rootDir, "log")));
		}
		if(config.getBoolean("kpi.out")) {
			monitor.addConsumer(new KpiStdoutLogger());
		}
		monitor.addConsumer(job::consumeKpi);
		return job;
	}

	private int threadCount() {
		int threadCount = config.getInt("engine.threads");
		if(threadCount <= 0) {
			threadCount += Runtime.getRuntime().availableProcessors();
		}
		return threadCount;
	}

}
