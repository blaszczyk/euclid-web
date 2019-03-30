package euclid.web.dto;

import java.util.List;

public class BoardDto {
	
	private List<PointDto> points;
	
	private List<LineDto> lines;
	
	private List<CircleDto> circles;
	
	public BoardDto() {
	}

	public BoardDto(List<PointDto> points, List<LineDto> lines, List<CircleDto> circles) {
		this.points = points;
		this.lines = lines;
		this.circles = circles;
	}

	public List<PointDto> getPoints() {
		return points;
	}

	public void setPoints(List<PointDto> points) {
		this.points = points;
	}

	public List<LineDto> getLines() {
		return lines;
	}

	public void setLines(List<LineDto> lines) {
		this.lines = lines;
	}

	public List<CircleDto> getCircles() {
		return circles;
	}

	public void setCircles(List<CircleDto> circles) {
		this.circles = circles;
	}

}
