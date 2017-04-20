package br.ufal.ic.operations;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
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

public class Collector {

	private String projectName;

	private ConnectionURL conn;
	private Parse parse;
	private Gson gson;

	public Collector(String projectName) {
		this.projectName = projectName;
		gson = new Gson();
		parse = new Parse();
		conn = new ConnectionURL();
	}

	// private HashSet<String> map;

	public void collectMetricsInProjects(String projectName) {
		ReaderUtils r = new ReaderUtils();
		List<String> listHashs = ReaderUtils.readSecundaryFile("hashs_" + projectName + ".txt");
		BugsOperations bugsOperations = new BugsOperations();
		List<BugInfo> bugs = bugsOperations.assignBugList(projectName);
		double max = bugsOperations.getMaxCommit();

		// HashMap<String, List<Metric>> output = new HashMap<String,
		// List<Metric>>();

		// startAnalysis(listHashs, 0, 1000, bugs, output);

		// startAnalysis(listHashs, 1000, 2000, bugs, output);
		// startAnalysis(listHashs, 2000, 3000, bugs, output);
		// startAnalysis(listHashs, 3000, 4000, bugs, output);
		// startAnalysis(listHashs, 4000, 5000, bugs, output);
		// startAnalysis(listHashs, 5000, 6000, bugs, output);
		// startAnalysis(listHashs, 6000, 7000, bugs, output);
		// startAnalysis(listHashs, 7000, 8000, bugs, output);
		// startAnalysis(listHashs, 8000, 9000, bugs, output);
		// startAnalysis(listHashs, 9000, 10000, bugs, output);
		// startAnalysis(listHashs, 10000, 11000, bugs, output);
		// if (max > 11000) {
		// startAnalysis(listHashs, 11000, (int) max, bugs, output);
		// }

		startAnalysis(listHashs, 0, (int) max, bugs);

	}

	private void startAnalysis(List<String> listHashs, int start, int end, List<BugInfo> bugs) {

		HashMap<String, Metric> metrics = null;
		for (int i = start; i < end; i++) {

			System.out.println(i + "/" + end);
			String c = listHashs.get(i);
			metrics = ReaderUtils.getHashMetricsCSV(
					"C:/Users/gurio/Desktop/Pesquisa/Puc/Dados/" + projectName + "/metrics/commit_" + c + "/");

			if (metrics == null) {
				continue;
			}

			checkBugs(bugs, metrics, i, c);

			metrics.clear();
			metrics = null;
			System.gc();
		}

		int aux = 0;
		/*
		 * for (String s : output.keySet()) { if (output.get(s) != null) {
		 * System.out.println(aux + "/" + output.keySet().size() + "_" + s);
		 * Reader.writeMetrics("timeline/" + s, output.get(s)); aux++; } }
		 */
	}

	private void checkBugs(List<BugInfo> bugs, HashMap<String, Metric> metrics,
			// HashMap<String, List<Metric>> output,
			int i, String c) {

		List<BugInfo> bugsToRemove = new LinkedList<BugInfo>();
		for (BugInfo b : bugs) {
			// if (b.getOrder_reported() <= i && i <= (b.getOrder_fixed() + 1))
			// {
			if (i <= b.getOrder_reported()) {
				String e = b.getElement();

				if (metrics != null) {
					if (metrics.containsKey(e)) {
						try {
							HttpResponse getResponse = conn
									.getMethod(Constants.IP + Parameters.METRIC_GET_BY_NAME.getText() + "name/" + "valuename"); 
							// columnName/valuename
							ArrayList<Metric> ms = (ArrayList<Metric>) parse.parseJsonToClass(getResponse.getMesage(), new Metric());
							System.out.println("\nMetricas encontradas cujo valor da diretiva MaxCyclomatic seja valuename:");
							for (Metric m2 : ms) {
								System.out.println(m2.getCommit());
							}
							
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				}
			}

		}

		for (BugInfo b : bugsToRemove) {
			bugs.remove(b);
		}
	}

	public void checkMissingMetrics() {
	}

	public void checkAllWasCollected(String projectName) {
		// List<String> filePaths = Filter.filesOnFolder("timeline/");
		List<String> filePaths = Filter
				.filesOnFolder("C:/Users/gurio/Desktop/Pesquisa/Puc/Dados/" + projectName + "/timeline_init_reported/");

		BugsOperations bugsOperations = new BugsOperations();
		List<BugInfo> bugs = bugsOperations.assignBugList(projectName);
		HashMap<String, String> countElements = new HashMap<String, String>();

		int count = 0;
		int countTotal = 0;
		for (String path : filePaths) {
			// System.out.println(path);

			for (BugInfo b : bugs) {
				if (path.contains(b.getElement())) {
					List<String> file = ReaderUtils.readSecundaryFile("C:/Users/gurio/Desktop/Pesquisa/Puc/Dados/"
							+ projectName + "/timeline_init_reported/" + path + ".txt");
					int fileLines = file.size();
					double fullCount = b.getOrder_reported() - b.getOrder_init();
					countElements.put(path,
							fileLines + "," + fullCount + "," + b.getOrder_reported() + " - " + b.getOrder_init());

					if (fileLines > fullCount) {
						count += fileLines;
						countTotal += fileLines;
					} else {
						count += fileLines;
						countTotal += fullCount;
					}
				}
			}

		}
		// System.out.println(count);
		// System.out.println(countTotal);

		BigDecimal c = new BigDecimal(count);
		BigDecimal ct = new BigDecimal(countTotal);

		double c1 = count / 100;
		double c2 = countTotal / 100;

		// System.out.println(c1);
		// System.out.println(c2);
		// BigDecimal percentage = c.divide(ct);

		// System.out.println(c1/c2);

		for (String s : countElements.keySet()) {
			System.out.println(s + "," + countElements.get(s));
		}

	}
	
	private static SecureRandom random = new SecureRandom();

    public static String nextSessionId() {
        return new BigInteger(130, random).toString(32);
    }
}
