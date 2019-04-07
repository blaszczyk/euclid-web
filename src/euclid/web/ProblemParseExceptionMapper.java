package euclid.web;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import euclid.problem.ProblemParserException;

@Provider
public class ProblemParseExceptionMapper implements ExceptionMapper<ProblemParserException> {

	@Override
	public Response toResponse(final ProblemParserException exception) {
		return Response.status(400).entity(exception.getMessage()).build();
	}

}
