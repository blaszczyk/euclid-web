package euclid.web.config;

import javax.inject.Singleton;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import euclid.web.AlgebraHold;
import euclid.web.Mapper;
import euclid.web.job.JobManager;

public class Binder extends AbstractBinder {

	@Override
	protected void configure() {
		bind(Mapper.class).to(Mapper.class);
		bind(JobManager.class).to(JobManager.class).in(Singleton.class);
		bind(AlgebraHold.class).to(AlgebraHold.class).in(Singleton.class);
	}

}
