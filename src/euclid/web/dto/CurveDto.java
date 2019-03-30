package euclid.web.dto;

public abstract class CurveDto {
	
	private String type;
	
	CurveDto(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
