package com.crestani.bolsaDeValoresJetty;

import java.util.List;

import javax.servlet.annotation.WebListener;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@WebListener
@Path("/StockMarket")
public class StockMarketManager {

	public StockMarketManager() {

	}

	@GET
	@Path("/")
	@Produces(MediaType.TEXT_PLAIN)
	public String helloWorld() {
		return "Opções disponíveis: StockMarket/Sell StockMarket/Buy StockMarket/Poll StockMarket/ListAll";
	}

	/**
	 * Usado para fazer polling no servidor perguntando o estado de um lance.
	 * 
	 * @param stockName
	 *            Ação que se deseja saber o estado.
	 * @param clientId
	 *            Id do cliente perguntando.
	 * @return Um objeto Bid com o estado atual do lance no servidor. Ou null, se
	 *         não encontrou o lance.
	 */
	@GET
	@Path("/Poll")
	@Produces(MediaType.APPLICATION_JSON)
	public String Poll(@QueryParam("stockName") String stockName, @QueryParam("id") int clientId) {
		StockMarketManagerHelper.computeBids();
		StockMarketManagerHelper.removeAlreadyNotifiedBids();
		for (Bid b : StockMarketManagerHelper.listOfSellers) {
			if (b.getStockName().equals(stockName) && (b.getClientId() == clientId)
					&& (b.getStatus() == Bid.BidStatus.DONE)) {
				b.setStatus(Bid.BidStatus.SENT_TO_CLIENT);
				return b.toJson();
			}
		}
		for (Bid b : StockMarketManagerHelper.listOfBuyers) {
			if (b.getStockName().equals(stockName) && (b.getClientId() == clientId)
					&& (b.getStatus() == Bid.BidStatus.DONE)) {
				b.setStatus(Bid.BidStatus.SENT_TO_CLIENT);
				return b.toJson();
			}
		}
		return null;
	}

	/**
	 * Lista todas as ações presentes no servidor.
	 * 
	 * @return Lista de todas as ações presentes no servidor
	 */
	@GET
	@Path("ListAll")
	public List<Stock> listAll() {
		return StockMarketManagerHelper.listOfStocks;
	}

	/**
	 * Consulta o preço de uma ação.
	 * 
	 * @param stockName
	 *            O preço atual da ação stockName
	 * @return
	 */
	@GET
	@Path("GetPrice")
	public Stock getPrice(@QueryParam("stockName") String stockName) {
		System.out.println("GetPrice: stock name = " + stockName);
		for (Stock s : StockMarketManagerHelper.listOfStocks) {
			if (s.getName().equals(stockName)) {
				return s;
			}
		}
		return new Stock();
	}

	/**
	 * Para fins de debug
	 * 
	 * @return
	 */
	@GET
	@Path("debug")
	@Produces(MediaType.TEXT_HTML)
	public String debug() {
		String s = "<html><head><meta charset=\"UTF-8\"></head>";
		s += "<body><h1>Debug</h1><h2>Lista de ações do servidor:</h2>";
		if (StockMarketManagerHelper.listOfStocks.size() == 0) {
			s += "<p>(vazio)</p>";
		} else {
			for (Stock stock : StockMarketManagerHelper.listOfStocks) {
				s += "<p>" + stock.toString() + "</p>";
			}
		}
		s += "<h2>Lista de lances de compra:</h2>";
		if (StockMarketManagerHelper.listOfBuyers.size() == 0) {
			s += "<p>(vazio)</p>";
		} else {
			for (Bid bid : StockMarketManagerHelper.listOfBuyers) {
				s += "<p>" + bid.toString() + "</p>";
			}
		}
		s += "<h2>Lista de lances de venda:</h2>";
		if (StockMarketManagerHelper.listOfSellers.size() == 0) {
			s += "<p>(vazio)</p>";
		} else {
			for (Bid bid : StockMarketManagerHelper.listOfSellers) {
				s += "<p>" + bid.toString() + "</p>";
			}
		}
		s += "</body></html>";
		return s;
	}

}