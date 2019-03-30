package euclid.web.dto;

public class LineDto extends CurveDto {
	private PointDto normal;
	private String offset;
	
	public LineDto() {
		super("line");
	}
	
	public LineDto(PointDto normal, String offset) {
		this();
		this.normal = normal;
		this.offset = offset;
	}
	public PointDto getNormal() {
		return normal;
	}
	public void setNormal(PointDto normal) {
		this.normal = normal;
	}
	public String getOffset() {
		return offset;
	}
	public void setOffset(String offset) {
		this.offset = offset;
	}

	
}
