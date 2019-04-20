package euclid.web;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
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
import euclid.problem.ProblemParser;
import euclid.web.dto.*;

@Path("/problem")
public class ProblemEndpoint extends AbstractEndpoint {
	
	@Inject
	@Named("root-dir")
	private File rootDir;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response preview(final Map<String,String> problemDto) {
		final List<String> lines = ProblemMapper.map(problemDto);
		final Problem problem =  new ProblemParser(lines).parse();
		final List<ElementDto> preview = new BoardMapper(problem).mapPreview();
		return ok(preview);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response list(@QueryParam("search") final String searchTerm) {
		final List<String> files = Arrays.stream(problemsDir().list())
				.filter(f -> f.endsWith(".euclid"))
				.map(f -> f.substring(0, f.lastIndexOf('.')))
				.filter(f -> searchTerm == null || f.toLowerCase().contains(searchTerm.toLowerCase()))
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
			final Map<String,String> problemDto = ProblemMapper.map(lines);
			return ok(problemDto);
		}
		catch (IOException e) {
			return badRequest(e.getMessage());
		}
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{name}")
	public Response save(@PathParam("name") final String name, final Map<String,String> problemDto) {
		try {
			final List<String> lines = ProblemMapper.map(problemDto);
			new ProblemParser(lines).parse();
			final File file = file(name);
			Files.write(file.toPath(), lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
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
	
	@PostConstruct
	public void mkProblemsDir() {
		problemsDir().mkdirs();
	}

	private File problemsDir() {
		return new File(rootDir, "problems");
	}

	private File file(final String name) {
		return new File(problemsDir(), name + ".euclid");
	}

}
