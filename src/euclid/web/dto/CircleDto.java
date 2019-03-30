package euclid.web.dto;

public class CircleDto {
	
	private PointDto center;
	private String radius;
	
	public CircleDto() {
	}
	
	public CircleDto(PointDto center, String radius) {
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
