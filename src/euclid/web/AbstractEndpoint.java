package euclid.web;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;

import euclid.model.Algebra;
import euclid.problem.Problem;
import euclid.problem.ProblemParser;
import euclid.problem.ProblemParserException;

abstract class AbstractEndpoint {
	
	@Inject
	Algebra algebra;
	
	Problem parseProblem(final List<String> lines) {
		try {
			return new ProblemParser(algebra, lines).parse();
		}
		catch(ProblemParserException e) {
			e.printStackTrace();
			throw new BadRequestException(e); // TODO: handle. now exception gets lost.
		}
	}
	
	static Response ok(final Object body) {
		return Response.ok(body).build();
	}
	
	static Response ok() {
		return Response.ok().build();
	}
	
	static Response badRequest(final Object body) {
		return Response.status(404).entity(body).build();
	}
}
