package euclid.web.job;

import euclid.alg.engine.ThreadedSearchEngine;
import euclid.model.Board;
import euclid.problem.Problem;

public class Job {
	
	private final Problem problem;
	
	private final ThreadedSearchEngine<Board> engine;

	Job(Problem problem, ThreadedSearchEngine<Board> engine) {
		this.problem = problem;
		this.engine = engine;
	}

	public Problem getProblem() {
		return problem;
	}

	public ThreadedSearchEngine<Board> getEngine() {
		return engine;
	}
	
	

}
