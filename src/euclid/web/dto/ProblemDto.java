package euclid.web.dto;

public class ProblemDto {
	
	private String variables;
	private String initial;
	private String required;
	private int depth;
	
	public ProblemDto() {
	}
	
	
	public ProblemDto(final String variables, final String initial, final String required, final int depth) {
		this.variables = variables;
		this.initial = initial;
		this.required = required;
		this.depth = depth;
	}


	public String getVariables() {
		return variables;
	}
	public void setVariables(String variables) {
		this.variables = variables;
	}
	public String getInitial() {
		return initial;
	}
	public void setInitial(String initial) {
		this.initial = initial;
	}
	public String getRequired() {
		return required;
	}
	public void setRequired(String required) {
		this.required = required;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	

}
