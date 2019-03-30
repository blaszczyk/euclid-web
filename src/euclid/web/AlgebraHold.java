package euclid.web;

import euclid.model.Algebra;
import euclid.model.CachedCurveLifeCycle;
import euclid.model.CurveLifeCycle;

public class AlgebraHold {

	private final Algebra algebra;
	
	public AlgebraHold() {
		final CurveLifeCycle lifeCycle = new CachedCurveLifeCycle();
		algebra = new Algebra(lifeCycle);
	}
	
	public Algebra get() {
		return algebra;
	}
}
