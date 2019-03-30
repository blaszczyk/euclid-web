package euclid.web;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import euclid.problem.Problem;
import euclid.web.dto.*;

@Path("/problem")
public class ProblemEndpoint {

	@Inject
	private Mapper mapper;

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response problem(final ProblemDto problemDto) {
		final Problem problem = mapper.map(problemDto);
		final SolutionDto solution = mapper.map(problem);
		return Response.ok(solution).build();
	}
	
}
