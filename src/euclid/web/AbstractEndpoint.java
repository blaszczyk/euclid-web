package euclid.web;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import euclid.model.*;
import euclid.problem.Problem;
import euclid.problem.ProblemParser;
import euclid.web.dto.*;

abstract class AbstractEndpoint {
	
	final Algebra algebra;

	AbstractEndpoint(Algebra algebra) {
		this.algebra = algebra;
	}

	SolutionDto map(Problem problem, Board solution) {
		return new SolutionDto(map(problem.initial()), map(problem.required().iterator().next()), map(solution));
	}

	BoardDto map(Board board) {
		final List<PointDto> points = new ArrayList<>();
		final List<LineDto> lines = new ArrayList<>();
		final List<CircleDto> circles = new ArrayList<>();
		for(final Point point : board.points()) {
			points.add(map(point));
		}
		for(final Curve curve : board.curves()) {
			if(curve.isLine()) {
				lines.add(map(curve.asLine()));
			}
			else {
				circles.add(map(curve.asCircle()));
			}
		}
		return new BoardDto(points, lines, circles);
	}

	PointDto map(final Point point) {
		return new PointDto(map(point.x()), map(point.y()));
	}

	LineDto map(final Line line) {
		return new LineDto(map(line.normal()), map(line.offset()));
	}

	CircleDto map(final Circle circle) {
		return new CircleDto(map(circle.center()), map(circle.radiusSquare().root()));
	}
	
	String map(final Constructable number) {
		return new DecimalFormat("#.######").format(number.doubleValue());
	}

	Problem map(final ProblemDto problemDto) {
		final List<String> lines = new ArrayList<>();
		lines.addAll(Arrays.asList(problemDto.getVariables().split("\\r?\\n")));
		lines.add("initial=" + problemDto.getInitial());
		lines.add("required=" + problemDto.getRequired());
		lines.add("maxdepth=" + problemDto.getDepth());
		lines.add("findall=false");
		lines.add("algorithm=curve_based");
		return new ProblemParser(algebra, lines).parse();
	}
	
	
}
