package euclid.web.dto;

import java.util.HashMap;

@SuppressWarnings("serial")
public abstract class ElementDto extends HashMap<String, String> {
	
	ElementDto(String type) {
		put("type", type);
	}

}
