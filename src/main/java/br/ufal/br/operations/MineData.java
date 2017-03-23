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

		/*
		 * Read commits file
		 */

		/*
		 * Retorna Lista de Commmits private String projectName; private double
		 * commitOrder; private String commitHash;
		 */
		// List<Commit> commits = jr.readCSV("commits.csv");

		List<String> listHashs = jr.readSecundaryFile("hashs.txt");

		// for (Commit c : commits) {
		// System.out.println("cd dengue_results_repo");
		// System.out.println("git checkout " + c.getCommitHash());

		// File outputDirectory = new
		// File("/media/caio/B82C2E7C2C2E3632/Users/Gurio/metrics/");

		// if (!outputDirectory.exists()) {
		// if (outputDirectory.mkdir()) {
		// }
		// }
		int count = 0;
		HashMap<String, BugInfo> info = new HashMap<String, BugInfo>();

		/*int a = 0;
		for (BugInfo b : jsonBugs) {
			a += b.getElements().size();
		}

		System.out.println(a);
		// for (String c : listHashs) {

		if (a > 0) {
			return;
		}*/

		for (int i = 0; i < listHashs.size(); i++) {
			String c = listHashs.get(i);
			List<Metric> metrics = jr.readMetricsCSV("C:/Users/Gurio/metrics/commit_" + c + "/");

			// String cmd = "git checkout -f " + c;
			try {

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
				 * String s = null; while ((s = stdInput.readLine()) != null) {
				 * }
				 * 
				 * // read any errors from the attempted command while ((s =
				 * stdError.readLine()) != null) { } System.out.println(count +
				 * "/" + listHashs.size());
				 */

				// List<Metric> metrics = jr
				// .readMetricsCSV("/media/caio/B82C2E7C2C2E3632/Users/Gurio/metrics/commit_"
				// + c + "/");

				System.out.println(count + "_" + jsonBugs.size());
				boolean flag = false;
				for (Metric m : metrics) {

					ArrayList<BugInfo> bugsToRemove = new ArrayList<BugInfo>();
					for (BugInfo bug : jsonBugs) {

						ArrayList<String> elementsToRemove = new ArrayList<String>();
						for (String element : bug.getElements()) {

							String auxElement = element;

							String e = element.substring(0, element.lastIndexOf("."));
							e = element.substring(e.lastIndexOf(".") + 1);

							element = e;

							if (m.getName().contains(element)) {
								// System.out.println(c);
								// System.out.print(m.getName());
								// System.out.println(" " + element);

								if (Double.isNaN(bug.getOrder_init()) || bug.getOrder_init() == 0.0) {
									bug.setOrder_init(count);
									// System.out.println(count + " " + element
									// + " " + c);
								}

								if (!info.containsKey(auxElement)) {
									info.put(auxElement, bug);
									//System.out.println(count + " " + element + " " + c);
									elementsToRemove.add(auxElement);
									flag = true;
								}

							}
						}

						if (flag) {

							for (String e : elementsToRemove) {
								if (bug.getElements().contains(e)) {
									// System.out.println("Bugs before " +
									// bug.getElements().size());
									bug.getElements().remove(e);
									// System.out.println("Bugs after " +
									// bug.getElements().size());
								}
							}

							if (bug.getElements().size() == 0) {
								bugsToRemove.add(bug);
							}
						}
					}

					for (BugInfo b : bugsToRemove) {
						jsonBugs.remove(b);
					}

					if (jsonBugs.size() <= 0) {
						break;
					}
					// System.out.println(m.getName());

				}

				count++;

				// jr.writeMetrics("", metrics);
				// jr.copyMetricsFile("",
				// "/media/caio/B82C2E7C2C2E3632/Users/Gurio/metrics/commit_"
				// +
				// c + "/", "metrics.csv");
				// jr.getOtherHash();
				// System.out.println(element);
				// System.out.print(c);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		//System.out.println(info);
		for(String s : info.keySet()){
			System.out.println(s + "=" + info.get(s));
		}
	}

}
