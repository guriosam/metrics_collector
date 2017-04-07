package br.ufal.br.operations;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

import br.ufal.br.json.BugInfo;
import br.ufal.ic.objects.Commit;
import br.ufal.ic.objects.Metric;

public class MineData {
	
	private String projectName;
	
	public MineData(String projectName){
		this.projectName = projectName;
	}

	public void mineData() {

		/*
		 * Retorna os dados do JSON Ex: { "project": "apache_derby", "bug_id":
		 * "5734", "order_reported": 6531, "order_fixed": 6545, "elements": [
		 * "java.testing.org.apache.derbyTesting.junit.CleanDatabaseTestSetup.setUp()"]
		 * }
		 */
		List<BugInfo> jsonBugs = Reader.readJSON("all_bugs.json", projectName);

		List<String> listHashs = Reader.readSecundaryFile("hashs_" + projectName + ".txt");

		HashMap<String, Integer> info = new HashMap<String, Integer>();

		for (BugInfo b : jsonBugs) {
			for (String element : b.getElements()) {
				String e = element;
				if (e.contains("org.apache")) {
					e = e.substring(e.indexOf("org.apache"));
				}
				if (e.contains("javax.")) {
					e = e.substring(e.indexOf("javax"));
				}

				if (info.containsKey(e)) {
					// System.out.println("Repetida: " + e);
				} else {
					info.put(e, null);
				}
			}

		}			
		
		System.out.println("Start reading Metrics..");
		for (int i = 0; i < listHashs.size(); i++) {
			String c = listHashs.get(i);
			System.out.println(i + "/" + listHashs.size());
			HashMap<String, Metric> metrics = Reader.getMetricsCSV("C:/Users/gurio/Desktop/Pesquisa/Puc/Dados/" + projectName + "/metrics/commit_" + c + "/");

			for (String e : info.keySet()) {
				if (metrics.containsKey(e)) {
					if (info.get(e) == null) {
						info.put(e, i);
						// System.out.println(e + "%" + i);
					}
				}
			}
		}

		System.out.println("Finished reading Metrics..");

		String txt = "";
		for (String s : info.keySet()) {

			txt += s + "=" + info.get(s) + "\n";
			//System.out.println(info.get(s));
		}

		Reader.writeMiningOutput("mined_data_" +  projectName, txt);
	}

	public void checkoutProject() {

		Reader jr = new Reader();

		// List<BugInfo> jsonBugs =
		// jr.readJSON("C:/Users/gurio/workspace/metrics_collector/all_bugs.json");

		// List<String> listHashs =
		// jr.readSecundaryFile("C:/Users/gurio/workspace/metrics_collector/hashs.txt");

		/*
		 * Read commits file
		 */

		/*
		 * Retorna Lista de Commmits private String projectName; private double
		 * commitOrder; private String commitHash;
		 */
		// List<String> commits =
		// jr.readSecundaryFile("C:/Users/gurio/workspace/metrics_collector/commits_repo_table.txt");
		List<String> commits = Reader.readSecundaryFile("C:/Users/gurio/workspace/metrics_collector/commit6.txt");
		int count = 0;
		for (String c : commits) {
			// System.out.println("cd
			// C:/Users/gurio/Desktop/Pesquisa/Puc/Projetos/apache_tomcat/dengue_results_repo");
			String[] com = c.split(";");
			// System.out.println("git checkout -f " + com[3]);

			File outputDirectory = new File("C:/Users/gurio/Desktop/Pesquisa/Puc/Dados/" + projectName + "/metrics/");

			if (!outputDirectory.exists()) {
				if (outputDirectory.mkdir()) {
				}
			}

			try {
				String cmd = "git checkout -f " + com[0];

				Runtime run = Runtime.getRuntime();

				Process pr = run.exec(cmd);

				BufferedReader stdInput = new BufferedReader(new InputStreamReader(pr.getInputStream()));

				BufferedReader stdError = new BufferedReader(new InputStreamReader(pr.getErrorStream()));

				String s = null;
				while ((s = stdInput.readLine()) != null) {
					System.out.println(s);
				}

				while ((s = stdError.readLine()) != null) {
					System.out.println(s);
				}

				Reader.copyMetricsFile("C:/Users/gurio/Desktop/Pesquisa/Puc/Projetos/" + projectName +"/ufal5_results_repo/",
						outputDirectory + "/commit_" + com[0] + "/", "metrics.csv");
				System.out.println(count + "/" + commits.size());
			} catch (Exception e) {
				e.printStackTrace();
			}

			count++;

		}
	}

}
