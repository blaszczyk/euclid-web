package euclid.web;

import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import euclid.alg.Algorithm;
import euclid.alg.CurveBasedSearch;
import euclid.alg.engine.ThreadedSearchEngine;
import euclid.model.*;
import euclid.problem.Problem;
import euclid.web.dto.*;

@Path("/solve")
public class SolverEndpoint extends AbstractEndpoint{
	
	public SolverEndpoint() {
		super(new Algebra(new CachedCurveLifeCycle()));
	}

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response solve(final ProblemDto problemDto) {
		final Problem problem = map(problemDto);
		final Optional<Board> optSolution = findSolution(problem);
		if(!optSolution.isPresent()) {
			return Response.status(404).build();
		}
		final SolutionDto solution = map(problem, optSolution.get());
		return Response.ok(solution).build();
	}

	private Optional<Board> findSolution(final Problem problem) {
		final Algorithm<Board> algorithm = new CurveBasedSearch(problem.initial(), problem.required(), algebra);
		final ThreadedSearchEngine<Board> engine = new ThreadedSearchEngine<>(algorithm, problem.maxDepth(), 
				false, Runtime.getRuntime().availableProcessors());
		
		final Optional<Board> optSolution = engine.findFirst();
		return optSolution;
	}
}
