package com.crestani.bolsaDeValoresJetty;

import javax.servlet.annotation.WebListener;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@WebListener
@Path("/")
public class Resource  {


	public Resource() {

	}


	// @GET
	// @Path("hello")
	// @Produces(MediaType.TEXT_PLAIN)
	// public String helloWorld() {
	// return "Hello, world!";
	// }

	@GET
	@Path("debug")
	@Produces(MediaType.TEXT_HTML)
	public String debug() {
		String s = "<html><head><meta charset=\"UTF-8\"></head>";
		s += "<body><h1>Debug</h1><h2>Lista de ações do servidor:</h2>";
		if (StockMarketManagerStaticContext.listOfStocks.size() == 0) {
			s += "<p>(vazio)</p>";
		} else {
			for (Stock stock : StockMarketManagerStaticContext.listOfStocks) {
				s += "<p>" + stock.toString() + "</p>";
			}
		}
		s += "<h2>Lista de lances de compra:</h2>";
		if (StockMarketManagerStaticContext.listOfBuyers.size() == 0) {
			s += "<p>(vazio)</p>";
		} else {
			for (Bid bid : StockMarketManagerStaticContext.listOfBuyers) {
				s += "<p>" + bid.toString() + "</p>";
			}
		}
		s += "<h2>Lista de lances de venda:</h2>";
		if (StockMarketManagerStaticContext.listOfSellers.size() == 0) {
			s += "<p>(vazio)</p>";
		} else {
			for (Bid bid : StockMarketManagerStaticContext.listOfSellers) {
				s += "<p>" + bid.toString() + "</p>";
			}
		}
		s += "</body></html>";
		return s;
	}


}