package euclid.web;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import euclid.model.*;
import euclid.problem.Problem;
import euclid.problem.ProblemParser;
import euclid.web.dto.*;

public class Mapper {

	public SolutionDto map(final Problem problem) {
		return map(problem, Board.EMPTY);
	}

	public SolutionDto map(final Problem problem, final Board construction) {
		return new SolutionDto(map(problem.initial()), map(problem.required().iterator().next()), mapConstruction(construction));
	}

	private List<BoardDto> mapConstruction(final Board construction) {
		final List<BoardDto> list = new ArrayList<>();
		Board board = construction;
		while(board != null) {
			list.add(map(board));
			board = board.parent();
		}
		Collections.reverse(list);
		return list;
	}

	public BoardDto map(final Board board) {
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

	public List<String> map(final ProblemDto problemDto) {
		final List<String> lines = new ArrayList<>();
		lines.addAll(Arrays.asList(problemDto.getVariables().split("\\r?\\n")));
		lines.add("initial=" + problemDto.getInitial());
		lines.add("required=" + problemDto.getRequired());
		lines.add("maxdepth=" + problemDto.getDepth());
		lines.add("findall=false");
		lines.add("algorithm=curve_based");
		return lines;
	}

	public ProblemDto map(final List<String> lines) {
		final StringBuilder variables = new StringBuilder();
		String initial = "";
		String required = "";
		int depth = 0;
		for(final String line : lines) {
			boolean isReservedKeyword = false;
			if(line.contains("=")) {
				isReservedKeyword = true;
				final String[] split = line.split("\\=", 2);
				final String key = split[0].replaceAll("\\s", "").toLowerCase();
				final String value = split[1];
				if(key.equals(ProblemParser.KEY_INITIAL)) {
					initial = value;
				}
				else if(key.equals(ProblemParser.KEY_REQUIRED)) {
					required = value;
				}
				else if(key.equals(ProblemParser.KEY_MAX_DEPTH)) {
					depth = Integer.parseInt(value.trim());
				}
				else if(!key.equals(ProblemParser.KEY_ALGORITHM) && !key.equals(ProblemParser.KEY_FIND_ALL) ){
					isReservedKeyword = false;
				}
			}
			if(!isReservedKeyword) {
				variables.append(line).append("\r\n");
			}
		}
		return new ProblemDto(variables.toString(), initial, required, depth);
	}
}
