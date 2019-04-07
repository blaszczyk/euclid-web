package euclid.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import euclid.problem.ProblemParser;
import euclid.web.dto.*;

public class ProblemMapper {

	public static List<String> map(final ProblemDto problemDto) {
		final List<String> lines = new ArrayList<>();
		lines.addAll(Arrays.asList(problemDto.getVariables().split("\\r?\\n")));
		lines.add("initial=" + problemDto.getInitial());
		lines.add("required=" + problemDto.getRequired());
		lines.add("maxdepth=" + problemDto.getDepth());
		lines.add("findall=false");
		lines.add("algorithm=curve_based");
		return lines;
	}

	public static ProblemDto map(final List<String> lines) {
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
