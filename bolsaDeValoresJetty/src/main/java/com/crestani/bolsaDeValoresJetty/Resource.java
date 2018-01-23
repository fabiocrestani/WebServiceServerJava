package com.crestani.bolsaDeValoresJetty;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class Resource {

	private List<Stock> listOfStocks = new ArrayList<Stock>();

	public Resource() {
		listOfStocks.add(new Stock("ABC", 1.0, 1));
		listOfStocks.add(new Stock("XYZ", 32.2, 2));
	}

	@GET
	@Path("hello")
	@Produces(MediaType.TEXT_PLAIN)
	public String helloWorld() {
		return "Hello, world!";
	}

	@GET
	@Path("debug")
	@Produces(MediaType.TEXT_HTML)
	public String debug() {
		String s = "";
		for (Stock stock : listOfStocks) {
			s += "<p>" + stock.getName() + " R$" + stock.getPrice() + "</p>";
		}
		return s;
	}

}