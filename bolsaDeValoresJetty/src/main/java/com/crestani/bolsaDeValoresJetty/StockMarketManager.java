package com.crestani.bolsaDeValoresJetty;

import java.util.List;

import javax.servlet.annotation.WebListener;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.crestani.bolsaDeValoresJetty.Bid.BidStatus;

@WebListener
@Path("/servidorBolsaDeValores")
public class StockMarketManager {

	@GET
	@Path("/")
	@Produces(MediaType.TEXT_PLAIN)
	public String helloWorld() {
		return "Opcoes disponiveis: StockMarket/Sell StockMarket/Buy StockMarket/Poll StockMarket/ListAll";
	}

	/**
	 * Registra um ordem de compra ou venda no servidor.
	 * 
	 * @return Mensagem de controle (ACK ou NACK)
	 */
	@POST
	@Path("/Post")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String Post(String jsonInput) {
		Bid bid = (Bid) JsonHelper.fromJson(jsonInput, Bid.class);
		if (bid == null) {
			return StockMarketManagerHelper.nack();
		}
		bid.setStatus(BidStatus.PENDING);

		System.out.println("Cliente " + bid.getClientId() + " registrando ordem de compra da ação " + bid.getStockName()
				+ " por R$ " + bid.getNegotiatedPrice() + " e quantidade " + bid.getQuantity() + " (" + bid.getStatus()
				+ ")");
		Bid presentBid = null;
		List<Bid> list;
		if (bid.getType() == Bid.BidType.BUY) {
			list = StockMarketManagerHelper.listOfBuyers;
		} else if (bid.getType() == Bid.BidType.SELL) {
			list = StockMarketManagerHelper.listOfSellers;
		} else {
			return StockMarketManagerHelper.nack();
		}

		for (Bid b : list) {
			if (b.getStockName().equals(bid.getStockName())) {
				presentBid = b;
				presentBid.setQuantity(bid.getQuantity());
				presentBid.setNegotiatedPrice(bid.getNegotiatedPrice());
				presentBid.setStatus(bid.getStatus());
				StockMarketManagerHelper.computeBids();
				return StockMarketManagerHelper.ack();
			}
		}
		if (presentBid == null) {
			presentBid = new Bid(bid.getStockName(), bid.getNegotiatedPrice(), bid.getQuantity(), bid.getType(),
					bid.getClientId());
			presentBid.setStatus(bid.getStatus());
			list.add(presentBid);
		}
		StockMarketManagerHelper.computeBids();
		return StockMarketManagerHelper.ack();

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
	public String Poll(@QueryParam("stockName") String stockName, @QueryParam("clientId") int clientId) {
		StockMarketManagerHelper.computeBids();
		StockMarketManagerHelper.removeAlreadyNotifiedBids();
		for (Bid b : StockMarketManagerHelper.listOfSellers) {
			if (b.getStockName().equals(stockName) && (b.getClientId() == clientId)
					&& (b.getStatus() == Bid.BidStatus.DONE)) {
				b.setStatus(Bid.BidStatus.SENT_TO_CLIENT);
				return JsonHelper.toJson(b);
			}
		}
		for (Bid b : StockMarketManagerHelper.listOfBuyers) {
			if (b.getStockName().equals(stockName) && (b.getClientId() == clientId)
					&& (b.getStatus() == Bid.BidStatus.DONE)) {
				b.setStatus(Bid.BidStatus.SENT_TO_CLIENT);
				return JsonHelper.toJson(b);
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
	@Produces(MediaType.APPLICATION_JSON)
	public String listAll() {
		return JsonHelper.toJson(StockMarketManagerHelper.listOfStocks);
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
	@Produces(MediaType.APPLICATION_JSON)
	public String getPrice(@QueryParam("stockName") String stockName) {
		System.out.println("GetPrice: stock name: " + stockName);
		if (stockName.equals("")) {
			return StockMarketManagerHelper.nack();
		}
		for (Stock s : StockMarketManagerHelper.listOfStocks) {
			if (s.getName().equals(stockName)) {
				return JsonHelper.toJson(s);
			}
		}
		return JsonHelper.toJson(new Stock());
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