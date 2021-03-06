package euclid.web.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	
	private final Properties props = new Properties();
	
	Config(final File configFile) {
		this(fileInputStream(configFile));
	}

	Config(final InputStream is) {
		try {
			props.load(is);
		} catch (IOException e) {
			throw new RuntimeException("error loading configs", e);
		}
	}

	public String getString(final String key) {
		return props.getProperty(key);
	}

	public boolean getBoolean(final String key) {
		return Boolean.parseBoolean(props.getProperty(key));
	}
	
	public int getInt(final String key) {
		return Integer.parseInt(props.getProperty(key));
	}

	private static FileInputStream fileInputStream(final File file) {
		try {
			return new FileInputStream(file);
		}
		catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
