package euclid.web.job;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import euclid.alg.Algorithm;
import euclid.alg.CurveBasedSearch;
import euclid.alg.engine.ThreadedSearchEngine;
import euclid.model.Board;
import euclid.problem.Problem;
import euclid.web.AlgebraHold;

public class JobManager {
	
	@Inject
	private AlgebraHold algebra;
	
	private final Map<String, Job> jobs = new ConcurrentHashMap<>();

	public String createJob(final Problem problem) {
		final Algorithm<Board> algorithm = new CurveBasedSearch(problem.initial(), problem.required(), algebra.get());
		final ThreadedSearchEngine<Board> engine = new ThreadedSearchEngine<>(algorithm, problem.maxDepth(), 
				false, Runtime.getRuntime().availableProcessors());
		engine.start(true);
		
		final String jobId = UUID.randomUUID().toString();
		final Job job = new Job(problem, engine);
		jobs.put(jobId, job);
		return jobId;
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

}
