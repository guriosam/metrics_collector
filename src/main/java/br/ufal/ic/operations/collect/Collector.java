package br.ufal.ic.operations.collect;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import br.ufal.ic.objects.Bug;
import br.ufal.ic.objects.Metric;
import br.ufal.ic.utils.BugUtils;
import br.ufal.ic.utils.IOUtils;
import br.ufal.ic.utils.Paths;
import br.ufal.ic.utils.ReaderUtils;
import br.ufal.ic.utils.WriterUtils;
import br.ufal.ic.utils.FileUtils;

public class Collector {

	private String projectName;

	public Collector(String projectName) {
		this.projectName = projectName;
	}

	// private HashSet<String> map;

	// lowMemoryUsage - slower
	public void collectMetricsInProjects(String projectName) {

		List<Bug> bugs = BugUtils.assignBugList(projectName);

		int bugsSize = bugs.size();
		for (int j = 500; j < 1176; j++) { // for each BugInfo

			Bug bug = bugs.get(j);

			// if (!checkValited(bug)) { // if is "false" returns true
			// System.out.println(bug.getElement() + " has some missing
			// elements.");
			// continue;
			// }

			System.out.println(j + "/" + bugs.size());

			List<String> hashs = IOUtils.readAnyFile(projectName + "/hashs_by_element/" + bug.getElement() + ".txt");
			System.out.println("Hashs Size: " + hashs.size());
			String e = bug.getElement();

			if (e.contains("?")) {
				e = e.replace("?", "");
			}
			if (e.contains("<")) {
				e = e.replace("<", "");
			}
			if (e.contains(">")) {
				e = e.replace(">", "");
			}

			HashMap<String, List<Metric>> output = new HashMap<String, List<Metric>>();

			for (int i = 0; i < hashs.size(); i++) {

				String c = hashs.get(i);

				if (c == null) {
					System.out.println("No commit at " + i);
					continue;
				}

				if (c.contains("null")) {
					continue;
				}

				String path = Paths.PATH_DATA + projectName + "/metrics2/commit_" + c + "/";

				HashMap<String, Metric> metrics = IOUtils.getBruteMetricsMap(path + "metrics2.csv");

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

			if (output.get(e) != null) {
				System.out.println("Writing started.");
				WriterUtils.writeMetrics(projectName + "/timeline/" + e, output.get(e));
				System.out.println("Writing ended.");

			}
			long end = System.currentTimeMillis();

			Date date = new Date(end);
			DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
			String dateFormatted = formatter.format(date);
			System.out.println("Time: " + dateFormatted);
		}

	}

	public boolean checkValited(Bug bug) {

		List<String> bugFile = IOUtils.readAnyFile(projectName + "/validation/" + bug.getElement() + ".txt");

		for (String line : bugFile) {
			if (line.contains("false") && !line.contains("null")) {
				return true;
			}
		}

		return false;

	}

	// highMemoryUsage - faster
	public void collectMetricsInProjects2(String projectName) {
		List<Bug> bugs = BugUtils.assignBugList(projectName);

		List<String> hashs = IOUtils.readAnyFile(projectName + "/hashs.txt");

		HashMap<String, List<Metric>> output = new HashMap<String, List<Metric>>();

		for (int i = 0; i < hashs.size(); i++) {

			String c = hashs.get(i);

			if (c == null) {
				System.out.println("No commit at " + i);
				continue;
			}

			if (c.contains("null")) {
				continue;
			}

			System.out.println(i + "/" + hashs.size());

			String path = Paths.PATH_DATA + projectName + "/metrics2/commit_" + c + "/";

			HashMap<String, Metric> metrics = IOUtils.getBruteMetricsMap(path + "metrics2.csv");

			//1176
			//1100
			//1000
			//900
			//800
			//700
			//600
			//500
			//400
			//300
			//200
			//100 ok
			for (int j = 0; j < 2; j++) { // for each BugInfo

				Bug bug = bugs.get(j);

				String e = bug.getElement();

				if (e.contains("?")) {
					e = e.replace("?", "");
				}
				if (e.contains("<")) {
					e = e.replace("<", "");
				}
				if (e.contains(">")) {
					e = e.replace(">", "");
				}

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

		System.out.println("Starting writing");
		for (String k : output.keySet()) {
			if (output.get(k) != null) {
				WriterUtils.writeMetrics(projectName + "/timeline/" + k, output.get(k));
			}
		}
		System.out.println("Writing finished");
	}

	public List<Integer> getElementsNotCollected(String projectName) {

		BugUtils bo = new BugUtils();
		List<Bug> bugs = bo.assignBugList(projectName);

		List<String> files = FileUtils.filesOnFolder("timeline/");

		List<Integer> elementsMissing = new ArrayList<Integer>();

		for (int i = 0; i < bugs.size(); i++) {
			Bug b = bugs.get(i);
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

	public void methodNotValidated(String projectName) {

		String elementsPath = projectName + "/elementsToGetMetrics_apache_tomcat.txt";

		try {

			File f = new File(projectName + "/elementsNotValidated.txt");
			List<String> notValid = new ArrayList<String>();
			List<String> elements = IOUtils.readAnyFile(elementsPath);

			int elementsSize = elements.size();
			int elementCont = 0;

			for (String method : elements) {
				elementCont++;
				System.out.println("Elements to check:" + elementCont + "/" + elementsSize);

				String[] elementSplitted = method.split("%");
				String validationPath = projectName + "/validation/" + elementSplitted[0] + ".txt";
				List<String> validation = IOUtils.readAnyFile(validationPath);
				// looking for commits in method validation
				for (String commits : validation) {

					String[] commit = commits.split(",");// hash, boolean
					if (commit[1].contains("false")) {

						notValid.add(elementSplitted[0]);
						System.out.println("---" + elementSplitted[0]);
						break;
					}
				}

			}
			Writer writer = new FileWriter(f);

			String out = "";
			for (String m : notValid) {
				out += m + "\n";
			}
			writer.write(out);
			writer.close();
			System.out.println("END!!!");

			// OLHAR VALIDATION NO METODO ONDE TEM FALSE, CHECAR COMMIT, SE NAO
			// TIVER MESMO. IGNORAR NO METODO checkMinning no class MineData
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
