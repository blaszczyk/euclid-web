package euclid.web;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class AppConfig extends Application {
	
	@Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> classes = new HashSet<>();
        classes.add(HelloEuclid.class);
        return classes;
    }

}
