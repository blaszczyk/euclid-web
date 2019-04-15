package euclid.web.dto;

public class CircleDto extends ElementDto {
	
	private PointDto center;
	private String radius;
	
	public CircleDto() {
		super("circle");
	}
	
	public CircleDto(PointDto center, String radius) {
		this();
		this.center = center;
		this.radius = radius;
	}
	
	public PointDto getCenter() {
		return center;
	}
	public void setCenter(PointDto center) {
		this.center = center;
	}
	public String getRadius() {
		return radius;
	}
	public void setRadius(String radius) {
		this.radius = radius;
	}
	
	

}
