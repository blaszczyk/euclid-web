package euclid.web;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import euclid.model.*;
import euclid.problem.Problem;
import euclid.web.dto.*;

@Path("/problem")
public class ProblemEndpoint extends AbstractEndpoint {

	
	public ProblemEndpoint() {
		super(new Algebra(new BasicCurveLifeCycle()));
	}

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response problem(final ProblemDto problemDto) {
		final Problem problem = map(problemDto);
		final SolutionDto solution = map(problem, Board.withPoints().andCurves());
		return Response.ok(solution).build();
	}
	
}
