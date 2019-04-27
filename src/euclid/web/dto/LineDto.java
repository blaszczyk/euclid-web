package euclid.web.dto;


@SuppressWarnings("serial")
public class LineDto extends ElementDto {
	
	public LineDto() {
		super("line");
	}
	
	public LineDto(final String nx, final String ny, final String offset) {
		this();
		put("nx", nx);
		put("ny", ny);
		put("offset", offset);
	}

}
