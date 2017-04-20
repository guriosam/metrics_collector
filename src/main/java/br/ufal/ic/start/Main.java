package br.ufal.ic.start;

import java.util.List;

import br.ufal.ic.operations.Collector;
import br.ufal.ic.operations.Filter;
import br.ufal.ic.operations.MineData;
import br.ufal.ic.operations.ReaderUtils;

public class Main {

	public static void main(String[] args) { // TODO Auto-generated method

		String projectName = "apache_tomcat";
		//String projectName = "apache_derby";

		// Reader r = new Reader();

		// r.getOtherHash();
		

		MineData m = new MineData(projectName);
		m.mineData();
		//m.saveMetricsOnDatabase();
		
		// m.checkoutProject();

		Filter f = new Filter();

		// f.getReportedCommitOfMissingFiles();
		// f.filterMinedData("apache_derby");
		// f.filterMinedData("apache_tomcat");

		Collector c = new Collector(projectName); // c.setMap(map);
		//c.collectMetricsInProjects(projectName);
		// c.checkAllWasCollected("apache_tomcat");
		// c.checkAllWasCollected(projectName);

	}

	/*
	 * private static ConnectionURL conn = new ConnectionURL(); private static
	 * Parse parse = new Parse(); private static Gson gson = new Gson();
	 * 
	 * public static void main(String[] args) throws ConnectionException {
	 * 
	 * Metric m = new Metric(); m.setCommit(nextSessionId());
	 * 
	 * // Adiciona o elemeto --- Params(IP+Parametro, json do elemento)
	 * HttpResponse postResponse = conn.postMethod(Constants.IP +
	 * Parameters.METRIC_POST.getText(), gson.toJson(m));
	 * System.out.println("Elemento adicionado com sucesso");
	 * 
	 * HttpResponse getResponse = conn .getMethod(Constants.IP +
	 * Parameters.METRIC_GET_BY_NAME.getText() + "name/" + "valuename"); //
	 * columnName/valuename ArrayList<Metric> metrics = (ArrayList<Metric>)
	 * parse.parseJsonToClass(getResponse.getMesage(), new Metric());
	 * System.out.
	 * println("\nMetricas encontradas cujo valor da diretiva MaxCyclomatic seja valuename:"
	 * ); for (Metric m2 : metrics) { System.out.println(m2.getCommit()); }
	 * 
	 * }
	 * 
	 * private static SecureRandom random = new SecureRandom();
	 * 
	 * public static String nextSessionId() { return new BigInteger(130,
	 * random).toString(32); }
	 */
}
