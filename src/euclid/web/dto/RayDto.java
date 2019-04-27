package euclid.web.dto;

@SuppressWarnings("serial")
public class RayDto extends ElementDto {
	
	public RayDto() {
		super("ray");
	}

	public RayDto(final String ex, final String ey, final String dx, final String dy) {
		this();
		put("ex", ex);
		put("ey", ey);
		put("dx", dx);
		put("dy", dy);
	}

}
