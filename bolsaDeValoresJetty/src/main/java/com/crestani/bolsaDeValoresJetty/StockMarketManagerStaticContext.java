package com.crestani.bolsaDeValoresJetty;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class StockMarketManagerStaticContext {
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

}
