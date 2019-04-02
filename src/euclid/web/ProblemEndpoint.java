package euclid.web;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import euclid.problem.Problem;
import euclid.web.dto.*;

@Path("/problem")
public class ProblemEndpoint extends AbstractEndpoint {

	private final File problemsRootDir = new File(System.getenv("LOCALAPPDATA"), "euclid/problems");

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response preview(final ProblemDto problemDto) {
		final List<String> lines = mapper.map(problemDto);
		final Problem problem = parseProblem(lines);
		final SolutionDto solution = mapper.map(problem);
		return ok(solution);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response list(@QueryParam("search") final String searchTerm) {
		final List<String> files = Arrays.stream(problemsRootDir.list())
				.filter(f -> f.endsWith(".euclid"))
				.map(f -> f.substring(0, f.lastIndexOf('.')))
				.filter(f -> searchTerm == null || f.contains(searchTerm))
				.collect(Collectors.toList());
		return ok(files);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{name}")
	public Response load(@PathParam("name") final String name) {
		try {
			final File file = file(name);
			final List<String> lines = Files.readAllLines(file.toPath());
			final ProblemDto problemDto = mapper.map(lines);
			return ok(problemDto);
		}
		catch (IOException e) {
			return badRequest(e.getMessage());
		}
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{name}")
	public Response save(@PathParam("name") final String name, final ProblemDto problemDto) {
		try {
			final List<String> lines = mapper.map(problemDto);
			final File file = file(name);
			Files.write(file.toPath(), lines, StandardOpenOption.CREATE);
			return ok();
		}
		catch (IOException e) {
			return badRequest(e.getMessage());
		}
	}

	@DELETE
	@Path("/{name}")
	public Response delete(@PathParam("name") final String name) {
		final File file = file(name);
		file.delete();
		return ok();
	}

	private File file(final String name) {
		problemsRootDir.mkdirs();
		return new File(problemsRootDir, name + ".euclid");
	}

}
