package euclid.web.dto;

import euclid.algorithm.AlgorithmType;
import euclid.algorithm.AlgorithmType.PriorityType;

public class ProblemDto {
	
	private String variables;
	private String initial;
	private String required;
	private int depth;
	private String depthFirst;
	private String shuffle;
	private AlgorithmType algorithm;
	private PriorityType priority;
	
	public ProblemDto() {
	}
	
	public ProblemDto(final String variables, final String initial, final String required, final int depth, 
			final String depthFirst, final String shuffle, final AlgorithmType algorithm, final PriorityType priority) {
		this.variables = variables;
		this.initial = initial;
		this.required = required;
		this.depth = depth;
		this.depthFirst = depthFirst;
		this.shuffle = shuffle;
		this.algorithm = algorithm;
		this.priority = priority;
	}


	public String getVariables() {
		return variables;
	}
	public void setVariables(String variables) {
		this.variables = variables;
	}
	public String getInitial() {
		return initial;
	}
	public void setInitial(String initial) {
		this.initial = initial;
	}
	public String getRequired() {
		return required;
	}
	public void setRequired(String required) {
		this.required = required;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public String getDepthFirst() {
		return depthFirst;
	}
	public void setDepthFirst(String depthFirst) {
		this.depthFirst = depthFirst;
	}

	public String getShuffle() {
		return shuffle;
	}

	public void setShuffle(String shuffle) {
		this.shuffle = shuffle;
	}

	public AlgorithmType getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(AlgorithmType algorithm) {
		this.algorithm = algorithm;
	}

	public PriorityType getPriority() {
		return priority;
	}

	public void setPriority(PriorityType priority) {
		this.priority = priority;
	}

}
