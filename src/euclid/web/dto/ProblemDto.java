package euclid.web.dto;

public class ProblemDto {
	
	private String variables;
	private String initial;
	private String required;
	private int depth;
	private String depthFirst;
	
	public ProblemDto() {
	}
	
	public ProblemDto(final String variables, final String initial, final String required, final int depth, final String depthFirst) {
		this.variables = variables;
		this.initial = initial;
		this.required = required;
		this.depth = depth;
		this.depthFirst = depthFirst;
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
	public String getDepthFirst() {
		return depthFirst;
	}
	public void setDepthFirst(String depthFirst) {
		this.depthFirst = depthFirst;
	}

}
