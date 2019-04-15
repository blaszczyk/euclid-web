package euclid.web;

import java.util.List;

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
	public Response solve(final ProblemDto problemDto) {
		final List<String> lines = ProblemMapper.map(problemDto);
		final Problem problem =  new ProblemParser(lines).parse();
		final String jobId = jobManager.createAndStartJob(problem);
		return ok(new JobIdDto(jobId));
	}

	@GET
	@Path("/{jobId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response pollSolution(@PathParam("jobId") final String jobId) {
		final Job job = jobManager.job(jobId);
		final KpiReport kpiReport = job.kpiReport();

		final ConstructionDto constructionDto;
		if(job.finished()) {
			final List<? extends Board> solutions = job.solutions();
			final Board solution = solutions.isEmpty() ? null : solutions.get(0);
			final Problem problem = job.problem();
			jobManager.removeJob(jobId);
			constructionDto = new BoardMapper(problem).mapConstruction(solution, kpiReport);
		}
		else
		{
			constructionDto = new BoardMapper().mapKpi(kpiReport);
		}
		return ok(constructionDto);
	}

	@DELETE
	@Path("/{jobId}")
	public Response halt(@PathParam("jobId") final String jobId) {
		jobManager.halt(jobId);
		return ok();
	}

}
