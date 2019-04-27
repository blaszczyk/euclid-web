package euclid.web.dto;

@SuppressWarnings("serial")
public class SegmentDto extends ElementDto {
	
	public SegmentDto() {
		super("segment");
	}

	public SegmentDto(final String x1, final String y1, final String x2, final String y2) {
		this();
		put("x1", x1);
		put("y1", y1);
		put("x2", x2);
		put("y2", y2);
	}

}
