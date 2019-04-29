package euclid.web;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import euclid.algorithm.Reconstruction;
import euclid.geometry.Circle;
import euclid.geometry.Curve;
import euclid.geometry.Line;
import euclid.geometry.Number;
import euclid.geometry.Point;
import euclid.geometry.Ray;
import euclid.geometry.Segment;
import euclid.kpi.KpiReport;
import euclid.sets.*;
import euclid.problem.Problem;
import euclid.web.dto.*;

public class BoardMapper {
	
	private final Problem problem;

	public BoardMapper(final Problem problem) {
		this.problem = problem;
	}
	
	public BoardMapper() {
		this(null);
	}

	public ContainerDto mapPreview() {
		return map(null, null, true);
	}

	public ContainerDto mapConstruction(final Board construction, final KpiReport report) {
		return map(construction, report, true);
	}

	public ContainerDto mapKpiReport(final KpiReport report) {
		return map(null, report, false);
	}

	private ContainerDto map(final Board solution, final KpiReport report, final boolean finished) {
		final BoardDto initial = finished ? mapBoard(problem.initial()) : null;
		final BoardDto required = finished ? mapBoard(problem.required()) : null;
		final List<ConstructionDto> construction = mapConstruction(solution);
		final Map<String, java.lang.Number> kpi = mapKpi(report);
		return new ContainerDto(initial, required, construction, kpi, finished);
	}
	
	private Map<String, java.lang.Number> mapKpi(final KpiReport report) {
		if(report == null) {
			return null;
		}
		final Map<String, java.lang.Number> keyValues = new LinkedHashMap<>();
		if(report != null) {
			report.items().forEach(i -> keyValues.put(i.name(), i.value()));
		}
		return keyValues;
	}
	
	private List<ConstructionDto> mapConstruction(final Board solution) {
		if(solution == null) {
			return null;
		}
		Reconstruction construction = Reconstruction.from(solution, problem.constructor().create());
		final List<ConstructionDto> dtos = new ArrayList<>(solution.depth());
		while(construction != null) {
			final BoardDto constituents = mapBoard(construction.constituents());
			final ElementDto curve = mapCurve(construction.curve());
			final ConstructionDto dto = new ConstructionDto(constituents, curve);
			dtos.add(dto);
			construction = construction.next();
		}
		return dtos;
	}

	private BoardDto mapBoard(final Board board) {
		final BoardDto boardDto = new BoardDto();
		for(final Point point : board.points()) {
			boardDto.add(mapPoint(point));
		}
		for(final Curve curve : board.curves()) {
			boardDto.add(mapCurve(curve));
		}
		return boardDto;
	}

	private ElementDto mapPoint(final Point point) {
		final String x = mapNumber(point.x());
		final String y = mapNumber(point.y());
		return ElementDto.point(x, y);
	}
	
	private ElementDto mapCurve(final Curve curve) {
		final ElementDto dto;
		if(curve.isLine()) {
			final Line line = curve.asLine();
			if(line.isRay()) {
				dto = mapRay(line.asRay());
			}
			else if(line.isSegment()) {
				dto = mapSegment(line.asSegment());
			}
			else {
				dto = mapLine(curve.asLine());
			}
		}
		else {
			dto = mapCircle(curve.asCircle());
		}
		return dto;
	}

	private ElementDto mapLine(final Line line) {
		final String nx = mapNumber(line.normal().x());
		final String ny = mapNumber(line.normal().y());
		final String offset = mapNumber(line.offset());
		return ElementDto.line(nx, ny, offset);
	}

	private ElementDto mapRay(final Ray ray) {
		final Point tangent = ray.normal().orth();
		final Point end = ray.normal().mul(ray.offset()).add(tangent.mul(ray.end()));
		final Point direction = ray.orientation() ? tangent : tangent.negate();
		final String ex = mapNumber(end.x());
		final String ey = mapNumber(end.y());
		final String dx = mapNumber(direction.x());
		final String dy = mapNumber(direction.y());
		return ElementDto.ray(ex, ey, dx, dy);
	}

	private ElementDto mapSegment(final Segment segment) {
		final Point basePoint = segment.normal().mul(segment.offset());
		final Point tangent = segment.normal().orth();
		final Point from = basePoint.add(tangent.mul(segment.from()));
		final Point to = basePoint.add(tangent.mul(segment.to()));
		final String x1 = mapNumber(from.x());
		final String y1 = mapNumber(from.y());
		final String x2 = mapNumber(to.x());
		final String y2 = mapNumber(to.y());
		return ElementDto.segment(x1, y1, x2, y2);
	}

	private ElementDto mapCircle(final Circle circle) {
		final String cx = mapNumber(circle.center().x());
		final String cy = mapNumber(circle.center().y());
		final String radius = mapNumber(circle.radiusSquare().root());
		return ElementDto.circle(cx, cy, radius);
	}
	
	private String mapNumber(final Number number) {
		return new DecimalFormat("#.######").format(number.doubleValue());
	}
}
