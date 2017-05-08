package br.ufal.ic.operations;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import br.ufal.ic.json.BugInfo;
import br.ufal.ic.objects.Metric;
import br.ufal.ic.utils.IO;
import br.ufal.ic.utils.Paths;

public class Collector {

	private String projectName;

	public Collector(String projectName) {
		this.projectName = projectName;
	}

	// private HashSet<String> map;

	public void collectMetricsInProjects(String projectName) {

		BugsOperations bugsOperations = new BugsOperations();
		List<BugInfo> bugs = bugsOperations.assignBugList(projectName);

		// List<Integer> missingElements = getElementsNotCollected(projectName);

		int count = 1121;
		for (int j = 1121; j < bugs.size(); j++) {

			BugInfo bug = bugs.get(j);
			if (!checkValited(bug)) {
				count++;
				continue;
			}

			System.out.println(bug.getElement() + " has some missing elements.");

			HashMap<String, List<Metric>> output = new HashMap<>();

			System.out.println(count + "/" + bugs.size());
			System.out.println("Element: " + count);
			System.out.println("Start " + bug.getOrder_init() + " End " + bug.getOrder_reported());
			System.out.println("Real Size " + (bug.getOrder_reported() - bug.getOrder_init()));

			List<String> hashs = IO.readAnyFile(projectName + "/hashs_by_element/" + bug.getElement() + ".txt");
			System.out.println("Hashs Size: " + hashs.size());

			for (int i = 0; i < hashs.size(); i++) {

				String c = hashs.get(i);

				if (c == null) {
					System.out.println("No commit at " + i);
					continue;
				}

				ReaderUtils r = new ReaderUtils();

				if (c.equals("null")) {
					continue;
				}
				String path = Paths.PATH_DATA + projectName + "/metrics/commit_" + c + "/";

				HashMap<String, Metric> metrics = r.getHashMetricsCSV(path);

				String e = bug.getElement();
				if (metrics != null) {
					if (metrics.containsKey(e)) {
						try {
							Metric m = metrics.get(e);
							m.setCommit(c);

							if (!output.containsKey(e)) {
								output.put(e, new LinkedList<Metric>());
							}
							output.get(e).add(m);
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				}

			}

			System.out.println("Writing started.");
			for (String s : output.keySet()) {
				if (output.get(s) != null) {
					WriterUtils.writeMetrics("timeline/" + s, output.get(s));
				}
			}
			System.out.println("Writing ended.");

			long end = System.currentTimeMillis();

			Date date = new Date(end);
			DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
			String dateFormatted = formatter.format(date);

			System.out.println("Time: " + dateFormatted);
			count++;
		}

	}

	private boolean checkValited(BugInfo bug) {

		List<String> bugFile = IO.readAnyFile(projectName + "/validation/" + bug.getElement() + ".txt");

		for (String line : bugFile) {
			if (line.contains("false")) {
				return true;
			}
		}

		return false;

	}

	public void collectMetricsInProjects3(String projectName) {
		List<String> listHashs = IO.readAnyFile("hashs_" + projectName + ".txt");
		BugsOperations bugsOperations = new BugsOperations();
		List<BugInfo> bugs = bugsOperations.assignBugList(projectName);
		double max = bugsOperations.getMaxCommit();

		HashMap<String, List<Metric>> output = new HashMap<>();

		for (int i = 0; i < max; i++) {

			String c = listHashs.get(i);

			if (c == null) {
				System.out.println("No commit at " + i);
				continue;
			}

			System.out.println(i + "/" + max);

			ReaderUtils r = new ReaderUtils();

			String path = Paths.PATH_DATA + projectName + "/metrics/commit_" + c + "/";

			HashMap<String, Metric> metrics = r.getHashMetricsCSV(path);

			checkBugs(bugs, metrics, output, i, c);

			long end = System.currentTimeMillis();

		}

		int aux = 0;
		for (String s : output.keySet()) {
			if (output.get(s) != null) {
				System.out.println(aux + "/" + output.keySet().size());
				WriterUtils.writeMetrics("timeline/" + s, output.get(s));
				aux++;
			}
		}

	}

	private void checkBugs(List<BugInfo> bugs, HashMap<String, Metric> metrics, HashMap<String, List<Metric>> output,
			int i, String c) {

		if (metrics == null) {
			return;
		}

		for (int j = 1000; j < 1100; j++) {

			BugInfo b = bugs.get(j);

			if (i > b.getOrder_reported()) {
				continue;
			}

			if (i < b.getOrder_init()) {
				continue;
			}

			String e = b.getElement();
			if (metrics != null) {
				if (metrics.containsKey(e)) {
					try {
						Metric m = metrics.get(e);
						m.setCommit(c);

						if (!output.containsKey(e)) {
							output.put(e, new LinkedList<Metric>());
						}
						output.get(e).add(m);
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		}
	}

	public void checkMissingMetrics() {
	}

	public List<Integer> getElementsNotCollected(String projectName) {

		BugsOperations bo = new BugsOperations();
		List<BugInfo> bugs = bo.assignBugList(projectName);

		List<String> files = Filter.filesOnFolder("timeline/");

		List<Integer> elementsMissing = new ArrayList<Integer>();

		for (int i = 0; i < bugs.size(); i++) {
			BugInfo b = bugs.get(i);
			boolean flag = true;
			for (String f : files) {
				if (b.getElement().contains(f)) {
					flag = false;
				}
			}
			if (flag) {
				// System.out.println(b);
				elementsMissing.add(i);
			}
		}

		return elementsMissing;

	}

}
