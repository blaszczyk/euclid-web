package euclid.web.dto;

import java.util.HashMap;

@SuppressWarnings("serial")
public class ElementDto extends HashMap<String, String> {
	
	public static ElementDto point(final String x, final String y) {
		return new ElementDto("point").attr("x", x).attr("y", y);
	}
	
	public static ElementDto line(final String nx, final String ny, final String offset) {
		return new ElementDto("line").attr("nx", nx).attr("ny", ny).attr("offset", offset);
	}
	
	public static ElementDto ray(final String ex, final String ey, final String dx, final String dy) {
		return new ElementDto("ray").attr("ex", ex).attr("ey", ey).attr("dx", dx).attr("dy", dy);
	}
	
	public static ElementDto segment(final String x1, final String y1, final String x2, final String y2) {
		return new ElementDto("segment").attr("x1", x1).attr("y1", y1).attr("x2", x2).attr("y2", y2);
	}
	
	public static ElementDto circle(final String cx, final String cy, final String radius) {
		return new ElementDto("circle").attr("cx", cx).attr("cy", cy).attr("radius", radius);
	}
	
	private ElementDto(final String type) {
		put("type", type);
	}
	
	private ElementDto attr(final String key, final String value) {
		put(key, value);
		return this;
	}
}
