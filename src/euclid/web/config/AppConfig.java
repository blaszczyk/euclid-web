package euclid.web.config;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import euclid.web.HelloEuclid;
import euclid.web.ProblemEndpoint;
import euclid.web.SolverEndpoint;

@ApplicationPath("/rest")
public class AppConfig extends ResourceConfig {
	
	public AppConfig() {
      register(HelloEuclid.class);
      register(SolverEndpoint.class);
      register(ProblemEndpoint.class);
      register(JacksonFeature.class);
      register(new Binder());
	}

}
