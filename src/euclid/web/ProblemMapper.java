package euclid.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import euclid.algorithm.AlgorithmType;
import euclid.algorithm.AlgorithmType.PriorityType;
import euclid.problem.ProblemParser;
import euclid.web.dto.*;

public class ProblemMapper {

	public static List<String> map(final ProblemDto problemDto) {
		final List<String> lines = new ArrayList<>();
		lines.addAll(Arrays.asList(problemDto.getVariables().split("\\r?\\n")));
		addKeyValueLine(ProblemParser.KEY_INITIAL, problemDto.getInitial(), lines);
		addKeyValueLine(ProblemParser.KEY_REQUIRED, problemDto.getRequired(), lines);
		addKeyValueLine(ProblemParser.KEY_MAX_DEPTH, problemDto.getDepth(), lines);
		addKeyValueLine(ProblemParser.KEY_DEPTH_FIRST, problemDto.getDepthFirst(), lines);
		addKeyValueLine(ProblemParser.KEY_MAX_SOLUTIONS, 1, lines);
		addKeyValueLine(ProblemParser.KEY_ALGORITHM, problemDto.getAlgorithm(), lines);
		addKeyValueLine(ProblemParser.KEY_PRIORITY, problemDto.getPriority(), lines);
		return lines;
	}
	
	private static void addKeyValueLine(final String key, final Object value, final List<String> lines) {
		lines.add(key + "=" + value);
	}

	public static ProblemDto map(final List<String> lines) {
		final StringBuilder variables = new StringBuilder();
		String initial = "";
		String required = "";
		int depth = 0;
		String depthFirst = "false";
		AlgorithmType algorithm = null;
		PriorityType priority = null;
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
				else if(key.equals(ProblemParser.KEY_DEPTH_FIRST)) {
					depthFirst = value.trim();
				}
				else if(key.equals(ProblemParser.KEY_ALGORITHM)) {
					algorithm = AlgorithmType.valueOf(value.toUpperCase());
				}
				else if(key.equals(ProblemParser.KEY_PRIORITY)) {
					priority = PriorityType.valueOf(value.toUpperCase());
				}
				else if(!key.equals(ProblemParser.KEY_MAX_SOLUTIONS) ){
					isReservedKeyword = false;
				}
			}
			if(!isReservedKeyword) {
				variables.append(line).append("\r\n");
			}
		}
		return new ProblemDto(variables.toString(), initial, required, depth, depthFirst, algorithm, priority);
	}
}
