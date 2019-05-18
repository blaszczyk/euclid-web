package euclid.web.config;

import java.io.File;

import javax.inject.Singleton;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import euclid.web.job.JobManager;

public class Binder extends AbstractBinder {

	private static final String CONFIG_PROPERTIES_FILE_NAME = "config.properties";

	@Override
	protected void configure() {
		bind(JobManager.class).to(JobManager.class).in(Singleton.class);

		final File rootDir = new File(System.getenv("LOCALAPPDATA"), "euclid");
		bind(rootDir).to(File.class).named("root-dir");

		final Config config;
		final File userConfig = new File(rootDir, CONFIG_PROPERTIES_FILE_NAME);
		if(userConfig.exists()) {
			config = new Config(userConfig);
		}
		else {
			config = new Config(Binder.class.getResourceAsStream(CONFIG_PROPERTIES_FILE_NAME));
		}
		bind(config).to(Config.class);
	}

}
