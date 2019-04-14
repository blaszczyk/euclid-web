package euclid.web;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import euclid.geometry.Circle;
import euclid.geometry.Curve;
import euclid.geometry.Line;
import euclid.geometry.Number;
import euclid.geometry.Point;
import euclid.geometry.Segment;
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

	public List<BoardDto> map() {
		final PointSet points = problem.initial().points().adjoin(problem.required().points());
		final CurveSet curves = problem.initial().curves().adjoin(problem.required().curves());
		return map(new Board(points, curves));
	}

	public List<BoardDto> map(final Board construction) {
		final List<BoardDto> list = new ArrayList<>();
		Board board = construction;
		while(board != null) {
			list.add(mapBoard(board));
			board = board.parent();
		}
		Collections.reverse(list);
		return list;
	}

	private BoardDto mapBoard(final Board board) {
		final List<PointDto> points = new ArrayList<>();
		final List<CurveDto> curves = new ArrayList<>();
		for(final Point point : board.points()) {
			points.add(mapPoint(point));
		}
		for(final Curve curve : board.curves()) {
			curves.add(mapCurve(curve));
		}
		return new BoardDto(points, curves);
	}

	private PointDto mapPoint(final Point point) {
		final PointDto dto = new PointDto(mapNumber(point.x()), mapNumber(point.y()));
		final String role = pointRoleRetriever.apply(point);
		dto.setRole(role);
		return dto;
	}
	
	private CurveDto mapCurve(final Curve curve) {
		final CurveDto dto;
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

	private CurveDto mapLine(final Line line) {
		return new LineDto(mapPoint(line.normal()), mapNumber(line.offset()));
	}

	private CurveDto mapSegment(final Segment segment) {
		final Point basePoint = segment.normal().mul(segment.offset());
		final Point tangent = segment.normal().orth();
		final Point from = basePoint.add(tangent.mul(segment.from()));
		final Point to = basePoint.add(tangent.mul(segment.to()));
		return new SegmentDto(mapPoint(from), mapPoint(to));
	}

	private CurveDto mapCircle(final Circle circle) {
		return new CircleDto(mapPoint(circle.center()), mapNumber(circle.radiusSquare().root()));
	}
	
	private String mapNumber(final Number number) {
		return new DecimalFormat("#.######").format(number.doubleValue());
	}
}
