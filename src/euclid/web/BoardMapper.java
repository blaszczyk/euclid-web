package euclid.web;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import euclid.geometry.Circle;
import euclid.geometry.Curve;
import euclid.geometry.Line;
import euclid.geometry.Number;
import euclid.geometry.Point;
import euclid.geometry.Segment;
import euclid.kpi.KpiReport;
import euclid.sets.*;
import euclid.problem.Problem;
import euclid.web.dto.*;

public class BoardMapper {
	
	private final Function<Point, String> pointRoleRetriever;
	
	private final Function<Curve, String> curveRoleRetriever;
	
	private final Problem problem;

	public BoardMapper(final Problem problem) {
		this.problem = problem;
		pointRoleRetriever = point -> {
			if(problem.initial().points().contains(point)) {
				return "initial";
			}
			if(problem.required().points().contains(point)) {
				return "required";
			}
			return "";
		};
		curveRoleRetriever = curve -> {
			if(problem.initial().curves().contains(curve)) {
				return "initial";
			}
			if(problem.required().curves().contains(curve)) {
				return "required";
			}
			return "";
		};
	}
	
	public BoardMapper() {
		this(null);
	}

	public List<ElementDto> mapPreview() {
		final PointSet points = problem.initial().points().adjoin(problem.required().points());
		final CurveSet curves = problem.initial().curves().adjoin(problem.required().curves());
		return mapBoard(new Board(points, curves));
	}

	public ConstructionDto mapConstruction(final Board construction, final KpiReport report) {
		return map(construction, report, true);
	}

	public ConstructionDto mapKpi(final KpiReport report) {
		return map(null, report, false);
	}

	private ConstructionDto map(final Board construction, final KpiReport report, final boolean finished) {
		final List<List<ElementDto>> list = new ArrayList<>();
		Board board = construction;
		while(board != null) {
			list.add(mapBoard(board));
			board = board.parent();
		}
		Collections.reverse(list);

		final Map<String, java.lang.Number> keyValues = new LinkedHashMap<>();
		if(report != null) {
			report.items().forEach(i -> keyValues.put(i.name(), i.value()));
		}
		
		return new ConstructionDto(list.isEmpty() ? null : list, 
				keyValues.isEmpty() ? null : keyValues , finished);
	}

	private List<ElementDto> mapBoard(final Board board) {
		final List<ElementDto> boardDto = new ArrayList<>();
		for(final Point point : board.points()) {
			boardDto.add(mapPoint(point));
		}
		for(final Curve curve : board.curves()) {
			boardDto.add(mapCurve(curve));
		}
		return boardDto;
	}

	private PointDto mapPoint(final Point point) {
		final PointDto dto = new PointDto(mapNumber(point.x()), mapNumber(point.y()));
		final String role = pointRoleRetriever.apply(point);
		dto.setRole(role);
		return dto;
	}
	
	private ElementDto mapCurve(final Curve curve) {
		final ElementDto dto;
		if(curve.isLine()) {
			final Line line = curve.asLine();
			if(line.isSegment()) {
				dto = mapSegment(line.asSegment());
			}
			else {
				dto = mapLine(curve.asLine());
			}
		}
		else {
			dto = mapCircle(curve.asCircle());
		}
		final String role = curveRoleRetriever.apply(curve);
		dto.setRole(role);
		return dto;
	}

	private LineDto mapLine(final Line line) {
		return new LineDto(mapPoint(line.normal()), mapNumber(line.offset()));
	}

	private SegmentDto mapSegment(final Segment segment) {
		final Point basePoint = segment.normal().mul(segment.offset());
		final Point tangent = segment.normal().orth();
		final Point from = basePoint.add(tangent.mul(segment.from()));
		final Point to = basePoint.add(tangent.mul(segment.to()));
		return new SegmentDto(mapPoint(from), mapPoint(to));
	}

	private CircleDto mapCircle(final Circle circle) {
		return new CircleDto(mapPoint(circle.center()), mapNumber(circle.radiusSquare().root()));
	}
	
	private String mapNumber(final Number number) {
		return new DecimalFormat("#.######").format(number.doubleValue());
	}
}
