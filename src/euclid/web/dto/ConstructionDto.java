package euclid.web.dto;

import java.util.List;
import java.util.Map;

public class ConstructionDto {
	
	private List<BoardDto> construction;
	
	private Map<String, Number> kpi;
	
	private boolean finished;

	public ConstructionDto(final List<BoardDto> construction, final Map<String, Number> kpi, final boolean finished) {
		this.construction = construction;
		this.kpi = kpi;
		this.finished = finished;
	}

	public List<BoardDto> getConstruction() {
		return construction;
	}

	public void setConstruction(List<BoardDto> construction) {
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
