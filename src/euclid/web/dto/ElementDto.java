package euclid.web.dto;

public abstract class ElementDto {

	private String type;
	
	private String role = null;
	
	ElementDto(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
