package euclid.web.dto;

public class SegmentDto extends CurveDto {
	private PointDto from;
	private PointDto to;
	
	public SegmentDto() {
		super("segment");
	}

	public SegmentDto(PointDto from, PointDto to) {
		this();
		this.from = from;
		this.to = to;
	}

	public PointDto getFrom() {
		return from;
	}

	public void setFrom(PointDto from) {
		this.from = from;
	}

	public PointDto getTo() {
		return to;
	}

	public void setTo(PointDto to) {
		this.to = to;
	}
	
}
