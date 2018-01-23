package com.crestani.bolsaDeValoresJetty;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class StockMarketManagerHelper {
	public static List<Stock> listOfStocks = new ArrayList<Stock>();
	public static List<Bid> listOfBuyers = new LinkedList<Bid>();
	public static List<Bid> listOfSellers = new LinkedList<Bid>();

	/**
	 * Carrega uma lista de ações de um arquivo de texto.
	 * 
	 * @throws Exception
	 */
	static void loadStocksFromFile() throws Exception {
		System.out.println("Carregando lista de ações...");
		File file = new File("listaDeAcoes.txt");
		Scanner sc;
		try {
			sc = new Scanner(file);
			while (sc.hasNextLine()) {
				String name = sc.nextLine();
				double quantity = MyRandom.randDouble(0, 100);
				listOfStocks.add(new Stock(name, 0, quantity));
			}
		} catch (FileNotFoundException e) {
			throw e;
		}
		sc.close();
	}

	/**
	 * Percorre as lista de compra e venda de ações e trata as negociações.
	 */
	public static void computeBids() {
		for (Bid buyer : listOfBuyers) {
			for (Bid seller : listOfSellers) {
				if ((buyer.getStatus() == Bid.BidStatus.PENDING) && (seller.getStatus() == Bid.BidStatus.PENDING)
						&& (buyer.getStockName().equals(seller.getStockName()))
						&& (buyer.getClientId() != seller.getClientId()) && (seller.getQuantity() > 0)
						&& (buyer.getQuantity() > 0)) {
					doTransaction(buyer, seller);
				}
			}
		}
	}

	/**
	 * Processa a transação em si
	 * 
	 * @param buyer
	 * @param seller
	 */
	private static void doTransaction(Bid buyer, Bid seller) {
		double newPrice = (seller.getNegotiatedPrice() + buyer.getNegotiatedPrice()) / 2;
		double transactionedQuantity = 0;
		if (buyer.getQuantity() > seller.getQuantity()) {
			transactionedQuantity = seller.getQuantity();
		} else {
			transactionedQuantity = buyer.getQuantity();
		}

		buyer.setQuantity(transactionedQuantity);
		seller.setQuantity(transactionedQuantity);

		buyer.setNegotiatedPrice(newPrice);
		seller.setNegotiatedPrice(newPrice);

		updatePriceOfStock(buyer.getStockName(), newPrice);

		System.out.println("");
		System.out.println("Nova transação efetuada:");
		System.out.println("Cliente " + seller.getClientId() + " vendeu  " + transactionedQuantity + " ações "
				+ seller.getStockName() + " por R$" + newPrice);
		System.out.println("Cliente " + buyer.getClientId() + " comprou " + transactionedQuantity + " ações "
				+ buyer.getStockName() + " por R$" + newPrice);
		System.out.println("");

		buyer.setStatus(Bid.BidStatus.DONE);
		seller.setStatus(Bid.BidStatus.DONE);
	}

	/**
	 * Atualiza o preço de uma ação no servidor.
	 * 
	 * @param stockName
	 *            Nome da ação.
	 * @param newPrice
	 *            Novo preço da ação.
	 */
	private static void updatePriceOfStock(String stockName, double newPrice) {
		for (Stock s : listOfStocks) {
			if (s.getName().equals(stockName)) {
				s.setPrice(newPrice);
			}
		}
	}

	/**
	 * Remove da lista de compradores ou vendedores os lances que já foram
	 * devidamente concluídos e cujos clientes foram notificados.
	 */
	public static void removeAlreadyNotifiedBids() {
		{
			Iterator<Bid> it = listOfBuyers.iterator();
			while (it.hasNext()) {
				Bid b = it.next();
				if (b.getStatus() == Bid.BidStatus.SENT_TO_CLIENT) {
					it.remove();
				}
			}
		}
		{
			Iterator<Bid> it = listOfSellers.iterator();
			while (it.hasNext()) {
				Bid b = it.next();
				if (b.getStatus() == Bid.BidStatus.SENT_TO_CLIENT) {
					it.remove();
				}
			}
		}
	}

}