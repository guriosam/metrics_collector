package br.ufal.br.operations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import br.ufal.br.json.BugInfo;
import br.ufal.ic.objects.Metric;

public class Collector {

	// private HashSet<String> map;

	public void collectMetricsInProjects(String projectName) {
		Reader r = new Reader();
		List<String> listHashs = r.readSecundaryFile("hashs.txt");
		BugsOperations bugsOperations = new BugsOperations();
		List<BugInfo> bugs = bugsOperations.assignBugList(projectName);
		double max = bugsOperations.getMaxCommit();

		HashMap<String, List<Metric>> output = new HashMap<String, List<Metric>>();

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
		if (max > 11000) {
			startAnalysis(listHashs, 11000, (int) max, bugs, output);
		}
	}

	private void startAnalysis(List<String> listHashs, int start, int end, List<BugInfo> bugs,
			HashMap<String, List<Metric>> output) {

		for (int i = start; i < end; i++) {

			System.out.println(i + "/" + end);
			String c = listHashs.get(i);
			HashMap<String, Metric> metrics = Reader
					.getMetricsCSV("C:/Users/gurio/Desktop/Pesquisa/Puc/Dados/tomcat/metrics/commit_" + c + "/");

			if (metrics == null) {
				continue;
			}

			checkBugs(bugs, metrics, output, i, c);

		}

		int aux = 0;
		for (String s : output.keySet()) {
			if (output.get(s) != null) {
				System.out.println(aux + "/" + output.keySet().size() + "_" + s);
				Reader.writeMetrics("timeline/" + s, output.get(s));
				aux++;
			}
		}
	}

	private void checkBugs(List<BugInfo> bugs, HashMap<String, Metric> metrics, HashMap<String, List<Metric>> output,
			int i, String c) {

		List<BugInfo> bugsToRemove = new LinkedList<BugInfo>();
		for (BugInfo b : bugs) {
			// if (b.getOrder_reported() <= i && i <= (b.getOrder_fixed() + 1))
			// {
			if (i <= b.getOrder_reported()) {
				String e = b.getElement();

				if (metrics != null) {
					if (metrics.containsKey(e)) {
						Metric m = metrics.get(e);
						if (output.containsKey(b.getElement())) {
							m.setCommit(c);
							output.get(e).add(m);
						} else {
							List<Metric> aux = new ArrayList<Metric>();
							m.setCommit(c);
							aux.add(m);
							// System.out.println(b.getElement());
							output.put(e, aux);
						}
					}
				}
			} else {
				bugsToRemove.add(b);
			}

		}

		for (BugInfo b : bugsToRemove) {
			bugs.remove(b);
		}
	}

	public void checkMissingMetrics() {
	}

	public void checkAllWasCollected(String projectName) {
		//List<String> filePaths = Filter.filesOnFolder("timeline/");
		List<String> filePaths = Filter.filesOnFolder("C:/Users/gurio/Desktop/Pesquisa/Puc/Dados/derby/timeline_init_reported/");


		BugsOperations bugsOperations = new BugsOperations();
		List<BugInfo> bugs = bugsOperations.assignBugList(projectName);
		HashMap<String, String> countElements = new HashMap<String, String>();

		for (String path : filePaths) {
			//System.out.println(path);

			for (BugInfo b : bugs) {
				if (path.contains(b.getElement())) {
					List<String> file = Reader.readSecundaryFile("C:/Users/gurio/Desktop/Pesquisa/Puc/Dados/derby/timeline_init_reported/" + path + ".txt");
					int fileLines = file.size();
					double fullCount = b.getOrder_reported() - b.getOrder_init();
					countElements.put(path, fileLines + "," + fullCount);
				}
			}

		}

		for (String s : countElements.keySet()) {
			System.out.println(s + "," + countElements.get(s));
		}

	}
}
