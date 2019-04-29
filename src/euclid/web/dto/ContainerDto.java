package euclid.web.dto;

import java.util.List;
import java.util.Map;

public class ContainerDto {
	
	private BoardDto initial;
	
	private BoardDto required;
	
	private List<ConstructionDto> construction;
	
	private Map<String, Number> kpi;
	
	private boolean finished;

	public ContainerDto(final BoardDto initial, final BoardDto required, final List<ConstructionDto> construction,
			final Map<String, Number> kpi, final boolean finished) {
		this.initial = initial;
		this.required = required;
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
