package euclid.web.dto;

public class SolutionDto {
	
	private BoardDto initial;
	private BoardDto required;
	private BoardDto solution;
	
	public SolutionDto() {
	}
	
	public SolutionDto(BoardDto initial, BoardDto required, BoardDto solution) {
		this.initial = initial;
		this.required = required;
		this.solution = solution;
	}

	public BoardDto getInitial() {
		return initial;
	}

	public void setInitial(BoardDto initial) {
		this.initial = initial;
	}

	public BoardDto getRequired() {
		return required;
	}

	public void setRequired(BoardDto required) {
		this.required = required;
	}

	public BoardDto getSolution() {
		return solution;
	}

	public void setSolution(BoardDto solution) {
		this.solution = solution;
	}
	
}
