package euclid.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import euclid.problem.ProblemParser;

import static euclid.problem.ProblemParser.*;

public class ProblemMapper {

	private static final String KEY_VARIABLES = "variables";
	
	private static final Set<String> PARSER_KEYS = new HashSet<>(Arrays.asList(KEY_INITIAL, KEY_REQUIRED, KEY_MAX_DEPTH, KEY_DEPTH_FIRST,
			KEY_SHUFFLE, KEY_ALGORITHM, KEY_PRIORITY));

	public static List<String> map(final Map<String,String> problemDto) {
		final List<String> lines = new ArrayList<>();
		lines.addAll(Arrays.asList(problemDto.get(KEY_VARIABLES).split("\\r?\\n")));
		problemDto.entrySet().stream()
			.filter(e -> PARSER_KEYS.contains(e.getKey()))
			.forEach(e -> lines.add(e.getKey() + "=" + e.getValue()));
		lines.add(KEY_MAX_SOLUTIONS + "=1");
		return lines;
	}

	public static Map<String, String> map(final List<String> lines) {
		final Map<String,String> problemDto = new LinkedHashMap<>();
		final StringBuilder variables = new StringBuilder();
		for(final String line : lines) {
			if(line.contains("=")) {
				final String[] split = line.split("\\=", 2);
				final String key = split[0].replaceAll("\\s", "").toLowerCase();
				final String value = split[1];
				if(PARSER_KEYS.contains(key)) {
					problemDto.put(key, value);
					continue;
				}
				else if(key.equals(ProblemParser.KEY_MAX_SOLUTIONS) ){
					continue;
				}
			}
			variables.append(line).append("\r\n");
		}
		problemDto.put(KEY_VARIABLES, variables.toString());
		return problemDto;
	}
}
