package euclid.web.dto;

import java.util.List;
import java.util.Map;

public class ContainerDto {
	
	private BoardDto initial;
	
	private BoardDto required;
	
	private BoardDto assist;
	
	private List<ConstructionDto> construction;

	private Map<String, Number> kpi;
	
	private boolean finished;

	public ContainerDto(final BoardDto initial, final BoardDto required, final BoardDto assist, 
			final List<ConstructionDto> construction, final Map<String, Number> kpi, 
			final boolean finished) {
		this.initial = initial;
		this.required = required;
		this.assist = assist;
		this.construction = construction;
		this.kpi = kpi;
		this.finished = finished;
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

	public BoardDto getAssist() {
		return assist;
	}

	public void setAssist(BoardDto assist) {
		this.assist = assist;
	}

	public List<ConstructionDto> getConstruction() {
		return construction;
	}

	public void setConstruction(List<ConstructionDto> construction) {
		this.construction = construction;
	}

	public Map<String, Number> getKpi() {
		return kpi;
	}

	public void setKpi(Map<String, Number> kpi) {
		this.kpi = kpi;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

}
