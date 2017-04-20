package br.ufal.ic.operations;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;

import br.ufal.ic.connection.ConnectionURL;
import br.ufal.ic.connection.HttpResponse;
import br.ufal.ic.json.BugInfo;
import br.ufal.ic.model.Element;
import br.ufal.ic.model.Metric;
import br.ufal.ic.utils.Constants;
import br.ufal.ic.utils.Parameters;
import br.ufal.ic.utils.Parse;

public class MineData {

	private String projectName;
	private ConnectionURL conn;
	private Parse parse;
	private Gson gson;

	public MineData(String projectName) {
		this.projectName = projectName;
		gson = new Gson();
		parse = new Parse();
		conn = new ConnectionURL();
	}

	public void mineData() {

		/*
		 * Retorna os dados do JSON Ex: { "project": "apache_derby", "bug_id":
		 * "5734", "order_reported": 6531, "order_fixed": 6545, "elements": [
		 * "java.testing.org.apache.derbyTesting.junit.CleanDatabaseTestSetup.setUp()"]
		 * }
		 */
		List<BugInfo> jsonBugs = ReaderUtils.readJSON("all_bugs.json", projectName);

		List<String> listHashs = ReaderUtils.readSecundaryFile("hashs_" + projectName + ".txt");

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
		
		System.out.println(info.keySet().size());
		
		System.out.println("Start reading Metrics..");
		
		for (int i = 1455; i < listHashs.size(); i++) {
			String h = listHashs.get(i);
			System.out.println(i + "/" + listHashs.size());
			String cd = "cd C:/Users/gurio/Desktop/Pesquisa/Puc/Dados/apache_tomcat/metrics/commit_" + h;
			// HashMap<String, Metric> metrics = ReaderUtils.getHashMetricsCSV(
			// "C:/Users/gurio/Desktop/Pesquisa/Puc/Dados/" + projectName +
			// "/metrics/commit_" + c + "/");

			// for (String e : info.keySet()) {
			// if (metrics.containsKey(e)) {
			// if (info.get(e) == null) {
			// info.put(e, i);
			// System.out.println(e + "%" + i);
			// }
			// }
			//
			String output = cd + "\n";
	
			for (String e : info.keySet()) {
				
				String path = e;
				
				if(e.contains("<")){
					path = e.substring(0, e.indexOf("<"));
				} else if(e.length() > 100){
					path = e.substring(0, 100);
				} else if (e.contains(">")){
					path = e.substring(0, e.indexOf(">"));
				}
				
				String command = "csvcut -c Kind,Name,File,AvgCyclomatic,AvgCyclomaticModified,AvgCyclomaticStrict,AvgEssential,AvgLine,AvgLineBlank,AvgLineCode,AvgLineComment,CountClassBase,CountClassCoupled,CountClassDerived,CountDeclClass,CountDeclClassMethod,CountDeclClassVariable,CountDeclFile,CountDeclFunction,CountDeclInstanceMethod,CountDeclInstanceVariable,CountDeclMethod,CountDeclMethodAll,CountDeclMethodDefault,CountDeclMethodPrivate,CountDeclMethodProtected,CountDeclMethodPublic,CountInput,CountLine,CountLineBlank,CountLineCode,CountLineCodeDecl,CountLineCodeExe,CountLineComment,CountOutput,CountPath,CountSemicolon,CountStmt,CountStmtDecl,CountStmtExe,Cyclomatic,CyclomaticModified,CyclomaticStrict,Essential,MaxCyclomatic,MaxCyclomaticModified,MaxCyclomaticStrict,MaxEssential,MaxInheritanceTree,MaxNesting,PercentLackOfCohesion,RatioCommentToCode,SumCyclomatic,SumCyclomaticModified,SumCyclomaticStrict,SumEssential"
						+ " metrics.csv " + "| csvgrep -c Name -m " + e
						+ " | csvlook > " + "\"" + path + ".txt\"";
				
				output += command + "\n";
			}
			
			WriterUtils.writeMiningOutput("commands/mined_data_" + projectName + "_" + h + ".bat", output);

		}

		System.out.println("Finished reading Metrics..");

	}

	public void checkoutProject() {

		ReaderUtils jr = new ReaderUtils();

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
		List<String> commits = ReaderUtils.readSecundaryFile("C:/Users/gurio/workspace/metrics_collector/commit6.txt");
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

				ReaderUtils.copyMetricsFile(
						"C:/Users/gurio/Desktop/Pesquisa/Puc/Projetos/" + projectName + "/ufal5_results_repo/",
						outputDirectory + "/commit_" + com[0] + "/", "metrics.csv");
				System.out.println(count + "/" + commits.size());
			} catch (Exception e) {
				e.printStackTrace();
			}

			count++;

		}
	}

	public void saveMetricsOnDatabase() {

		List<String> listHashs = ReaderUtils.readSecundaryFile("hashs_" + projectName + ".txt");
		int count = 0;

		for (int i = 114; i < listHashs.size(); i++) {

			String h = listHashs.get(i);

			System.out.println(i + "/" + listHashs.size());
			try {
				ArrayList<Metric> metrics = ReaderUtils.getListMetricsCSV(
						"C:/Users/gurio/Desktop/Pesquisa/Puc/Dados/" + projectName + "/metrics/commit_" + h + "/", h);

				final Element e = new Element();

				e.setName(nextSessionId());

				e.setMetric(metrics);

				final int actual = i;

				Thread t = new Thread() {
					@Override
					public void run() {
						try {
							// System.out.println("Sending data to database");

							HttpResponse postResponse = conn
									.postMethod(Constants.IP + Parameters.ELEMENT_POST.getText(), gson.toJson(e));

							System.out.println("Ended " + actual);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};

				t.start();

				// System.out.println(free);

				if (getFreeMemory() < 500000) {
					System.out.println("waiting...");
					while (getFreeMemory() < 500000) {

					}
					System.out.println("going..");
				}

				// System.out.println(postResponse.getMesage());

				// break;
			} catch (Exception e) {
				e.printStackTrace();
			}
			count++;
			// break;
		}
	}

	private static SecureRandom random = new SecureRandom();

	public static String nextSessionId() {
		return new BigInteger(130, random).toString(32);
	}

	public static double getFreeMemory() {

		Runtime runtime = Runtime.getRuntime();

		long freeMemory = runtime.freeMemory();

		// if(freeMemory < )

		NumberFormat format = NumberFormat.getInstance();

		long maxMemory = runtime.maxMemory();
		long allocatedMemory = runtime.totalMemory();
		// long freeMemory = runtime.freeMemory();

		String f = format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024);
		Double free = Double.parseDouble(f.replace(".", ""));

		return free;
	}

}
