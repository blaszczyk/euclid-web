package euclid.web.config;

import java.io.File;

import javax.inject.Singleton;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import euclid.web.job.JobManager;

public class Binder extends AbstractBinder {

	@Override
	protected void configure() {
		bind(JobManager.class).to(JobManager.class).in(Singleton.class);
		bind(new File(System.getenv("LOCALAPPDATA"), "euclid")).to(File.class).named("root-dir");
		bind(new Config(Binder.class.getResourceAsStream("config.properties"))).to(Config.class);
	}

}
