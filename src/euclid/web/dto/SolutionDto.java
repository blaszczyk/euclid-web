package euclid.web.dto;

import java.util.List;

public class SolutionDto {
	
	private BoardDto initial;
	private BoardDto required;
	private List<BoardDto> construction;
	
	public SolutionDto() {
	}
	
	public SolutionDto(BoardDto initial, BoardDto required, List<BoardDto> construction) {
		this.initial = initial;
		this.required = required;
		this.construction = construction;
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

	public List<BoardDto> getConstruction() {
		return construction;
	}

	public void setConstruction(List<BoardDto> solution) {
		this.construction = solution;
	}
	
}
