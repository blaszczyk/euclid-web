package euclid.web.dto;

@SuppressWarnings("serial")
public class PointDto extends ElementDto {
	
	public PointDto() {
		super("point");
	}
	
	public PointDto(final String x, final String y) {
		this();
		put("x", x);
		put("y", y);
	}
	
}
