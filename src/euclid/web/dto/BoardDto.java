package euclid.web.dto;

import java.util.List;

public class BoardDto {
	
	private List<PointDto> points;
	
	private List<CurveDto> curves;
	
	public BoardDto() {
	}

	public BoardDto(List<PointDto> points, List<CurveDto> curves) {
		this.points = points;
		this.curves = curves;
	}


	public List<PointDto> getPoints() {
		return points;
	}

	public void setPoints(List<PointDto> points) {
		this.points = points;
	}

	public List<CurveDto> getCurves() {
		return curves;
	}

	public void setCurves(List<CurveDto> curves) {
		this.curves = curves;
	}

}
