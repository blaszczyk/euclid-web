package euclid.web.job;

import java.util.List;

import euclid.engine.SearchEngine;
import euclid.kpi.KpiMonitor;
import euclid.kpi.KpiReport;
import euclid.sets.Board;
import euclid.problem.Problem;

public class Job {
	
	private final Problem problem;
	
	private final SearchEngine<? extends Board> engine;
	
	private final KpiMonitor monitor;
	
	private KpiReport lastKpiReport;

	Job(Problem problem, SearchEngine<? extends Board> engine, KpiMonitor monitor) {
		this.problem = problem;
		this.engine = engine;
		this.monitor = monitor;
		monitor.addConsumer(this::consumeKpi);
	}

	public Problem problem() {
		return problem;
	}

	public List<? extends Board> solutions() {
		return engine.solutions();
	}

	public boolean finished() {
		return engine.finished();
	}
	
	public KpiReport kpiReport() {
		return lastKpiReport;
	}

	void start() {
		engine.start(true);
		monitor.start();
	}

	void halt() {
		engine.halt();
		monitor.halt();
	}
	
	private void consumeKpi(final KpiReport report) {
		lastKpiReport = report;
	}
	
}
