package euclid.web;

import javax.ws.rs.core.Response;

abstract class AbstractEndpoint {
	
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
