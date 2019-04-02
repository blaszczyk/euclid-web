package euclid.web.job;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import euclid.alg.Algorithm;
import euclid.alg.CurveBasedSearch;
import euclid.alg.engine.ThreadedSearchEngine;
import euclid.model.Algebra;
import euclid.model.Board;
import euclid.problem.Problem;

public class JobManager {
	
	@Inject
	private Algebra algebra;
	
	private final Map<String, Job> jobs = new ConcurrentHashMap<>();

	public String createAndStartJob(final Problem problem) {
		final Algorithm<Board> algorithm = new CurveBasedSearch(problem.initial(), problem.required(), algebra);
		final ThreadedSearchEngine<Board> engine = new ThreadedSearchEngine<>(algorithm, problem.maxDepth(), 
				false, Runtime.getRuntime().availableProcessors());
		engine.start(true);

		final Job job = new Job(problem, engine);
		final String jobId = String.format("%08X", job.hashCode());
		jobs.put(jobId, job);
		return jobId;
	}
	
	public boolean isFinished(final String jobId) {
		return jobs.get(jobId).getEngine().finished();
	}
	
	public void halt(final String jobId) {
		jobs.get(jobId).getEngine().halt();
	}
	
	public Optional<Board> getSolution(final String jobId) {
		return jobs.get(jobId).getEngine().firstSolution();
	}
	
	public Problem getProblem(final String jobId) {
		return jobs.get(jobId).getProblem();
	}
	
	public void removeJob(final String jobId) {
		jobs.remove(jobId);
	}

}
