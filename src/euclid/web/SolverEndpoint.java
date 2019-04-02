package euclid.web;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import euclid.model.*;
import euclid.problem.Problem;
import euclid.web.dto.*;
import euclid.web.job.JobManager;

@Path("/solve")
public class SolverEndpoint extends AbstractEndpoint {

	@Inject
	private JobManager jobManager;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response solve(final ProblemDto problemDto) {
		final List<String> lines = mapper.map(problemDto);
		final Problem problem = parseProblem(lines);
		final String jobId = jobManager.createAndStartJob(problem);
		return ok(jobId);
	}

	@GET
	@Path("/{jobId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response pollSolution(@PathParam("jobId") final String jobId) {
	
		if(jobManager.isFinished(jobId)) {
			final Optional<Board> solution = jobManager.getSolution(jobId);
			final Problem problem = jobManager.getProblem(jobId);
			jobManager.removeJob(jobId);
			final SolutionDto solutionDto;
			if(solution.isPresent()) {
				solutionDto = mapper.map(problem, solution.get());
			}
			else {
				solutionDto = mapper.map(problem);
			}
			return ok(solutionDto);
		}
		return ok();
	}

	@DELETE
	@Path("/{jobId}")
	public Response halt(@PathParam("jobId") final String jobId) {
		jobManager.halt(jobId);
		return ok();
	}

}
