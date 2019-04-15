package euclid.web.dto;

public class PointDto extends ElementDto {
	private String x;
	private String y;
	
	public PointDto() {
		super("point");
	}
	
	public PointDto(String x, String y) {
		this();
		this.x = x;
		this.y = y;
	}
	public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	public String getY() {
		return y;
	}
	public void setY(String y) {
		this.y = y;
	}
	
	
}
