package br.ufal.ic.start;

import java.util.List;

import br.ufal.ic.operations.Collector;
import br.ufal.ic.operations.Filter;
import br.ufal.ic.operations.MineData;
import br.ufal.ic.utils.Paths;

public class Main {

	public static void main(String[] args) { // TODO Auto-generated method

		String projectName = "apache_tomcat";
		//String projectName = "apache_derby";
		
		/*List<String> commitFiles = Filter.filesOnFolder(Paths.PATH_WORKSPACE + projectName + "/repositoryCommits/");
		for(String s : commitFiles){
			System.out.println(s);
		}*/
		
		/*
		 * Steps
		 * 1. Collect the pair of hashs of commits, from the real database to the ufal database.
		 * 2. Collect all metrics.csv from all commits.
		 * 3. Create the filtered CSV file to help the minning.
		 * 4. Collect the historic of metrics in the CSV files for each element of bug.
		 */
		
		
		// r.getOtherHash();
		
		MineData m = new MineData(projectName);
		//m.checkMinning();
		//m.mineData();
		
		// m.checkoutProject();

		// Filter.getReportedCommitOfMissingFiles();
		// Filter.filterMinedData("apache_derby");
		// Filter.filterMinedData("apache_tomcat");

		Collector c = new Collector(projectName);
		c.collectMetricsInProjects(projectName);
		//c.getElementsNotCollected(projectName);
		
		

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