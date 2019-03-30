package euclid.web;

import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import euclid.model.*;
import euclid.problem.Problem;
import euclid.web.dto.*;
import euclid.web.job.JobManager;

@Path("/solve")
public class SolverEndpoint {

	@Inject
	private JobManager jobManager;

	@Inject
	private Mapper mapper;	

	@POST
	@Consumes("application/json")
	public Response solve(final ProblemDto problemDto) {
		final Problem problem = mapper.map(problemDto);
		final String jobId = jobManager.createJob(problem);
		return Response.ok(jobId).build();
	}

	@GET
	@Path("/{jobId}")
	@Produces("application/json")
	public Response pollSolution(@PathParam("jobId") final String jobId) {
	
		final Optional<Board> solution = jobManager.getSolution(jobId);
		if(solution.isPresent()) {
			final SolutionDto solutionDto = mapper.map(jobManager.getProblem(jobId), solution.get());
			return Response.ok(solutionDto).build();
		}
		return Response.ok().build();
	}

	@DELETE
	@Path("/{jobId}")
	public Response halt(@PathParam("jobId") final String jobId) {
		jobManager.halt(jobId);
		return Response.ok().build();
	}

}
