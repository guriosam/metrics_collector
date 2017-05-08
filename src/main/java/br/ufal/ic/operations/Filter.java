package br.ufal.ic.operations;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import br.ufal.ic.json.BugInfo;
import br.ufal.ic.objects.Metric;
import br.ufal.ic.utils.IO;
import br.ufal.ic.utils.Paths;

public class Filter {

	public void filterMinedData(String projectName) {

		List<String> mined = IO.readAnyFile("mined_data_" + projectName + ".txt");

		List<BugInfo> jsonBugs = ReaderUtils.writeBugJsonIntoBugList("all_bugs.json", projectName);

		Set<String> set = new HashSet<String>();

		for (BugInfo bug : jsonBugs) {

			for (String element : bug.getElements()) {

				// String e = element.substring(0, element.lastIndexOf("."));
				// e = element.substring(e.lastIndexOf(".") + 1);

				// element = e;

				if (set.contains(element)) {
					// System.out.println(element);
				} else {
					set.add(element);
				}

			}

		}

		HashSet<String> setElemeents = new HashSet<String>();
		HashSet<String> nullElements = new HashSet<String>();

		for (BugInfo b : jsonBugs) {

			for (String s : b.getElements()) {
				if (set.contains(s)) {
					for (String m : mined) {

						String[] aux = m.split("=");

						String e = s;

						if (e.contains("org.apache")) {
							e = e.substring(e.indexOf("org.apache"));
						} else if (e.contains("javax.")) {
							e = e.substring(e.indexOf("javax"));
						}

						if (aux[0].contains(e)) {
							// System.out.println(e);

							if (aux[1].equals("null")) {
								nullElements.add(e + "%" + aux[1] + "%" + (int) b.getOrder_reported() + "%"
										+ (int) b.getOrder_fixed());
								continue;
							}

							if (Double.valueOf(aux[1]) > (int) b.getOrder_reported()) {
								break;
							}

							setElemeents.add(e + "%" + aux[1] + "%" + (int) b.getOrder_reported() + "%"
									+ (int) b.getOrder_fixed());

							System.out.println(s + "%" + aux[1] + "%" + (int) b.getOrder_reported());
						}

					}

				}
			}
		}

		String text = "";
		for (String s : setElemeents) {
			text += s + "\n";
			System.out.println(s);
		}
		WriterUtils.writeMiningOutput("elementsToGetMetrics_" + projectName, text);

		String nullText = "";
		for (String s : nullElements) {
			nullText += s + "\n";
			System.out.println(s);
		}
		WriterUtils.writeMiningOutput("elementsNotFound_" + projectName, nullText);

		System.out.println(setElemeents.size());
	}

	public void getReportedCommitOfMissingFiles(String projectName) {
		List<String> missingFiles = IO.readAnyFile("elementsMissingMetrics.txt");
		List<BugInfo> bugs = ReaderUtils.writeBugJsonIntoBugList("all_bugs.json", projectName);

		for (String mf : missingFiles) {
			String[] mfs = mf.split("%");
			for (BugInfo b : bugs) {
				for (String e : b.getElements()) {
					if (e.contains(mfs[0])) {
						System.out.println(mf + "%" + b.getOrder_reported());
						break;
					}
				}
			}
		}

	}

	public static List<String> filesOnFolder(String path) {

		List<String> fileNames = new ArrayList<String>();

		if (path == null) {
			System.out.println("Folder name is null");
		}

		try {
			// filesNamesPath = path;
			File f = new File(path);

			File[] files = f.listFiles();

			for (File file : files) {
				fileNames.add(file.getName().replace(".txt", ""));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return fileNames;
	}

	public static void filterCSVFile(String path, String path2, List<String> elements) {
		// HashMap<String, Metric> metrics = new HashMap<String, Metric>();
		List<String[]> metrics = new ArrayList<String[]>();

		try {

			File f = new File(path + "metrics.csv");
			File f2 = new File(path2 + "metrics2.csv");

			if (!f.exists()) {
				return;
			}

			CSVReader csvReader = new CSVReader(new FileReader(f));
			List<String[]> myEntries = csvReader.readAll();

			for (String[] info : myEntries) {
				if (info[3].contains("AvgCyclomatic")) {
					continue;
				}
				
				if(info[1].contains("?")){
					info[1] = info[1].replaceAll("?", "");
				}

				if (info[0].contains("Method") || info[0].contains("Constructor")) {
					if (elements.contains(info[1])) {
						metrics.add(info);
					}
				}
			}

			CSVWriter csvWriter = new CSVWriter(new FileWriter(f2), ',');

			csvWriter.writeAll(metrics);

			csvWriter.close();
			csvReader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void createReducedCSVFile(String projectName) {

		List<String> listHashs = IO.readAnyFile("hashs_" + projectName + ".txt");

		for (int i = 0; i < listHashs.size(); i++) {
			System.out.println(i + "/" + listHashs.size());
			String h = listHashs.get(i);
			filterCSVFile(Paths.PATH_DATA + projectName + "/metrics/commit_" + h + "/",
					Paths.PATH_DATA + projectName + "/metrics2/commit_" + h + "/",
					ReaderUtils.getElementsWithBug("all_bugs.json", projectName));
		}

	}

	public static void makeHashFileByElement(String projectName) {

		List<String> elementsToGetHash = IO.readAnyFile(projectName + "/elementsToGetMetrics_" + projectName + ".txt");
		List<String> commitsList = IO.readAnyFile(projectName + "/commits_repo_table.txt");
		HashMap<String, String> commitsHash = new HashMap<>();
		for (String commit : commitsList) {
			String[] commitCollumns = commit.split(",");
			String c1 = commitCollumns[1].replaceAll(" ", "");
			c1 = c1.replaceAll("\n", "");
			String c3 = commitCollumns[3].replace(" ", "");
			c3 = c3.replaceAll("\n", "");
			// System.out.println(c1);
			commitsHash.put(c1, c3);
		}

		int count = 0;

		for (String element : elementsToGetHash) {

			System.out.println(count + "/" + elementsToGetHash.size());

			String[] elementCollumns = element.split("%");

			Double startCommit = Double.parseDouble(elementCollumns[1]);
			Double endCommit = Double.parseDouble(elementCollumns[2]);

			String outputFile = "";

			for (double i = startCommit + 1; i <= endCommit; i++) {
				outputFile += commitsHash.get(i + "") + "\n";
			}

			String outputPath = projectName + "/hashs_by_element/";
			File outputDirectory = new File(outputPath);

			if (!outputDirectory.exists()) {
				if (outputDirectory.mkdir()) {
				}
			}

			WriterUtils.writeMiningOutput(outputPath + elementCollumns[0] + ".txt", outputFile);
			count++;
		}

	}
}
