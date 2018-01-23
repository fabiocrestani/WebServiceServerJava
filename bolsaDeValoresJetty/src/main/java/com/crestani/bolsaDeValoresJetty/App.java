package com.crestani.bolsaDeValoresJetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public class App {
	public static void main(String[] args) {
		System.out.println("Inicializando...");
		try {
			StockMarketManagerHelper.loadStocksFromFile();
		} catch (Exception e1) {
			System.out.println("Erro ao carregar lista de ações de arquivo");
			e1.printStackTrace();
		}

		test();

		ResourceConfig config = new ResourceConfig();
		config.packages("com.crestani.bolsaDeValoresJetty");
		ServletHolder servlet = new ServletHolder(new ServletContainer(config));

		Server server = new Server(2222);
		ServletContextHandler context = new ServletContextHandler(server, "/*");
		context.addServlet(servlet, "/*");

		System.out.println("Inicializando servlet...");

		try {
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			server.destroy();
		}
	}

	private static void test() {
		Stock stock = new Stock("ABC", 1, 10);
		String json = GsonHelper.toJson(stock);
		System.out.println("json produzido: " + json);
	}
}
