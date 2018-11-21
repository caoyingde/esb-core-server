package org.example;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(value = "/simple/")
@Produces(value = { MediaType.APPLICATION_JSON })
public interface SimpleService {

	@GET
	@Path(value = "/say/{name}")
	@Produces(value = { MediaType.TEXT_PLAIN })
	public String sayHello(@PathParam("name") String name);

	@GET
	@Path(value = "/json/{name}")
	@Produces(value = { MediaType.APPLICATION_JSON })
	public TestPojo getPojo(@PathParam("name") String name);
	
	@GET
	@Path(value = "/xml/{name}")
	@Produces(value = { MediaType.APPLICATION_XML })
	public TestPojo[] getPojo2(@PathParam("name") String name);
}
