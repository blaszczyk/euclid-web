package euclid.web;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/hello")
public class HelloEuclid {

	@GET
	public Response sayHello() {
		return Response.ok("Hello Euclid").build();		
	}
	
	
}
