package euclid.web;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import euclid.kpi.KpiReport;
import euclid.problem.Problem;
import euclid.problem.ProblemParser;
import euclid.sets.Board;
import euclid.web.dto.*;
import euclid.web.job.Job;
import euclid.web.job.JobManager;

@Path("/solve")
public class SolverEndpoint extends AbstractEndpoint {
	
	@Inject
	private JobManager jobManager;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response solve(final Map<String,String> problemDto) {
		final List<String> lines = ProblemMapper.map(problemDto);
		final Problem problem =  new ProblemParser(lines).parse();
		final String jobId = jobManager.createAndStartJob(problem);
		return ok(Collections.singletonMap("jobId", jobId));
	}

	@GET
	@Path("/{jobId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response pollSolution(@PathParam("jobId") final String jobId) {
		final Job job = jobManager.job(jobId);
		final ContainerDto containerDto;
		if(job.finished()) {
			jobManager.removeJob(jobId);
			final List<? extends Board> solutions = job.solutions();
			final Board solution = solutions.isEmpty() ? null : solutions.get(0);
			final Problem problem = job.problem();
			final KpiReport kpiReport = job.kpiReport();
			containerDto = new BoardMapper(problem).mapConstruction(solution, kpiReport);
		}
		else
		{
			final KpiReport kpiReport = job.kpiReport();
			containerDto = new BoardMapper().mapKpiReport(kpiReport);
		}
		return ok(containerDto);
	}

	@DELETE
	@Path("/{jobId}")
	public Response halt(@PathParam("jobId") final String jobId) {
		jobManager.job(jobId).halt();
		return ok();
	}

}
