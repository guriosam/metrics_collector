package br.ufal.br.operations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.ufal.br.json.BugInfo;
import br.ufal.ic.objects.Metric;

public class MineData {

	public void mineData() {
		Reader jr = new Reader();

		/*
		 * Retorna os dados do JSON Ex: { "project": "apache_derby", "bug_id":
		 * "5734", "order_reported": 6531, "order_fixed": 6545, "elements": [
		 * "java.testing.org.apache.derbyTesting.junit.CleanDatabaseTestSetup.setUp()"]
		 * }
		 */
		List<BugInfo> jsonBugs = jr.readJSON("all_bugs.json");

		List<String> listHashs = jr.readSecundaryFile("hashs.txt");

		HashMap<String, Integer> info = new HashMap<String, Integer>();

		for (BugInfo b : jsonBugs) {
			for (String element : b.getElements()) {
				String e = element;
				if (e.contains("org.apache")) {
					e = e.substring(e.indexOf("org.apache"));
				}

				if (info.containsKey(e)) {
					System.out.println("Repetida: " + e);
				} else {
					info.put(e, null);
				}
			}

		}

		List<HashMap<String, Metric>> listMetrics = new ArrayList();

		System.out.println("Start reading Metrics..");
		for (int i = 0; i < listHashs.size(); i++) {
			String c = listHashs.get(i);
			System.out.println(i + "/" + listHashs.size());
			HashMap<String, Metric> metrics = jr.getMetricsCSV("C:/Users/Gurio/metrics/commit_" + c + "/");

			// listMetrics.add(metrics);

			// HashMap<String, Metric> metrics = listMetrics.get(i);

			boolean flag = false;

			for (String e : info.keySet()) {

				if (metrics.containsKey(e)) {

					if (info.get(e) == null) {
						info.put(e, i);
						System.out.println(e + "%" + i);
					}
				}
			}

		}

		System.out.println("Finished reading Metrics..");

		for (String s : info.keySet()) {
			System.out.println(s + "=" + info.get(s));
		}
	}

	public void checkoutProject() {

		/*
		 * Read commits file
		 */

		/*
		 * Retorna Lista de Commmits private String projectName; private double
		 * commitOrder; private String commitHash;
		 */
		// List<Commit> commits = jr.readCSV("commits.csv");

		// for (Commit c : commits) {
		// System.out.println("cd dengue_results_repo");
		// System.out.println("git checkout " + c.getCommitHash());

		// File outputDirectory = new
		// File("/media/caio/B82C2E7C2C2E3632/Users/Gurio/metrics/");

		// if (!outputDirectory.exists()) {
		// if (outputDirectory.mkdir()) {
		// }
		// }

		// String cmd = "git checkout -f " + c;

		/*
		 * Runtime run = Runtime.getRuntime();
		 * 
		 * Process pr = run.exec(cmd);
		 * 
		 * BufferedReader stdInput = new BufferedReader(new
		 * InputStreamReader(pr.getInputStream()));
		 * 
		 * BufferedReader stdError = new BufferedReader(new
		 * InputStreamReader(pr.getErrorStream()));
		 * 
		 * // read the output from the command
		 * 
		 * String s = null; while ((s = stdInput.readLine()) != null) { }
		 * 
		 * // read any errors from the attempted command while ((s =
		 * stdError.readLine()) != null) { } System.out.println(count + "/" +
		 * listHashs.size());
		 */

		// jr.writeMetrics("", metrics);
		// jr.copyMetricsFile("",
		// "/media/caio/B82C2E7C2C2E3632/Users/Gurio/metrics/commit_"
		// +
		// c + "/", "metrics.csv");
		// jr.getOtherHash();
		// System.out.println(element);
		// System.out.print(c);
	}
}
