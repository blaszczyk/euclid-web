package euclid.web;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.jackson.JacksonFeature;

@ApplicationPath("/rest")
public class AppConfig extends Application {
	
	@Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> classes = new HashSet<>();
        classes.add(HelloEuclid.class);
        classes.add(SolverEndpoint.class);
        classes.add(ProblemEndpoint.class);
        classes.add(JacksonFeature.class);
        return classes;
    }

}
