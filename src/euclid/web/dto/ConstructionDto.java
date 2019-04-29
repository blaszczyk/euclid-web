package euclid.web.dto;

public class ConstructionDto {
	
	private BoardDto constituents;
	
	private ElementDto curve;

	public ConstructionDto(final BoardDto constituents, final ElementDto curve) {
		this.constituents = constituents;
		this.curve = curve;
	}

	public BoardDto getConstituents() {
		return constituents;
	}

	public void setConstituents(BoardDto constituents) {
		this.constituents = constituents;
	}

	public ElementDto getCurve() {
		return curve;
	}

	public void setCurve(ElementDto curve) {
		this.curve = curve;
	}

}
