package euclid.web;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import euclid.model.*;
import euclid.problem.Problem;
import euclid.problem.ProblemParser;
import euclid.web.dto.*;

public class Mapper {
	
	@Inject
	private AlgebraHold algebra;

	public SolutionDto map(Problem problem) {
		return map(problem, Board.EMPTY);
	}

	public SolutionDto map(Problem problem, Board solution) {
		return new SolutionDto(map(problem.initial()), map(problem.required().iterator().next()), map(solution));
	}

	public BoardDto map(Board board) {
		final List<PointDto> points = new ArrayList<>();
		final List<CurveDto> curves = new ArrayList<>();
		for(final Point point : board.points()) {
			points.add(map(point));
		}
		for(final Curve curve : board.curves()) {
			if(curve.isLine()) {
				curves.add(map(curve.asLine()));
			}
			else {
				curves.add(map(curve.asCircle()));
			}
		}
		return new BoardDto(points, curves);
	}

	public PointDto map(final Point point) {
		return new PointDto(map(point.x()), map(point.y()));
	}

	public LineDto map(final Line line) {
		return new LineDto(map(line.normal()), map(line.offset()));
	}

	public CircleDto map(final Circle circle) {
		return new CircleDto(map(circle.center()), map(circle.radiusSquare().root()));
	}
	
	public String map(final Constructable number) {
		return new DecimalFormat("#.######").format(number.doubleValue());
	}

	public Problem map(final ProblemDto problemDto) {
		final List<String> lines = new ArrayList<>();
		lines.addAll(Arrays.asList(problemDto.getVariables().split("\\r?\\n")));
		lines.add("initial=" + problemDto.getInitial());
		lines.add("required=" + problemDto.getRequired());
		lines.add("maxdepth=" + problemDto.getDepth());
		lines.add("findall=false");
		lines.add("algorithm=curve_based");
		return new ProblemParser(algebra.get(), lines).parse();
	}
}
