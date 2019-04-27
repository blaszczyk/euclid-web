package euclid.web.dto;

@SuppressWarnings("serial")
public class CircleDto extends ElementDto {
	
	public CircleDto() {
		super("circle");
	}
	
	public CircleDto(final String cx, final String cy, final String radius) {
		this();
		put("cx", cx);
		put("cy", cy);
		put("radius", radius);
	}

}
