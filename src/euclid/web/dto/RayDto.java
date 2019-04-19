package euclid.web.dto;

public class RayDto extends ElementDto {

	private PointDto end;
	private PointDto direction;
	
	public RayDto() {
		super("ray");
	}

	public RayDto(PointDto end, PointDto direction) {
		this();
		this.end = end;
		this.direction = direction;
	}

	public PointDto getEnd() {
		return end;
	}

	public void setEnd(PointDto end) {
		this.end = end;
	}

	public PointDto getDirection() {
		return direction;
	}

	public void setDirection(PointDto direction) {
		this.direction = direction;
	}

}
