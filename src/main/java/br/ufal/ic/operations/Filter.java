package br.ufal.ic.operations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.ufal.ic.objects.Bug;
import br.ufal.ic.utils.IOUtils;
import br.ufal.ic.utils.JSONUtils;
import br.ufal.ic.utils.Paths;
import br.ufal.ic.utils.ReaderUtils;
import br.ufal.ic.utils.WriterUtils;
import br.ufal.ic.utils.FileUtils;

public class Filter {

	public void filterMinedData(String projectName) {

		List<String> mined = IOUtils.readAnyFile("mined_data_" + projectName + ".txt");

		List<Bug> jsonBugs = JSONUtils.writeBugJsonIntoBugList("all_bugs.json", projectName);

		Set<String> set = new HashSet<String>();

		for (Bug bug : jsonBugs) {

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

		for (Bug b : jsonBugs) {

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
		List<String> missingFiles = IOUtils.readAnyFile("elementsMissingMetrics.txt");
		List<Bug> bugs = JSONUtils.writeBugJsonIntoBugList("all_bugs.json", projectName);

		for (String mf : missingFiles) {
			String[] mfs = mf.split("%");
			for (Bug b : bugs) {
				for (String e : b.getElements()) {
					if (e.contains(mfs[0])) {
						System.out.println(mf + "%" + b.getOrder_reported());
						break;
					}
				}
			}
		}

	}

	public static void filterCSVFile(String path, String path2, List<String> elements) {

		List<String> metrics = new ArrayList<String>();

		try {

			File f = new File(path + "metrics.csv");
			File f2 = new File(path2 + "metrics2.csv");
			BufferedReader reader = null;
			String csvSplitBy = "\"";

			if (!f.exists()) {
				return;
			}

			reader = new BufferedReader(new FileReader(f));
			String line;

			String[] csvLine = null;

			while ((line = reader.readLine()) != null) {

				if (line.contains("AvgCyclomatic")) {
					metrics.add(line);
					continue;
				}

				csvLine = line.split(csvSplitBy); // use Quotes as separator

				// if (csvLine[1].contains("?")) {
				// csvLine[1] = csvLine[1].replaceAll("?", "");
				// }

				if (csvLine[0].contains("Method") || csvLine[0].contains("Constructor")) {
					for (String j : elements) {
						if (j.contains(csvLine[1])) {
							if (!metrics.contains(line)) {
								metrics.add(line);
							}
						}
					}

				}
			}

			Writer writer = new FileWriter(f2);

			String out = "";
			for (String m : metrics) {
				out += m + "\n";
			}
			writer.write(out);
			writer.close();
			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void checkReducedCSVFile(String projectName, List<String> elements) {

		try {
			int control = 0;
			// get the commit's hash.
			List<String> listHashs = IOUtils.readAnyFile(projectName + "/hashs_" + projectName + ".txt");
			int listSize = listHashs.size();
			// run each hash
			for (int i = 0; i < listSize; i++) {
				System.out.println(i + "/" + listSize);
				String h = listHashs.get(i);

				String pathToCheck = Paths.PATH_DATA + projectName + "/metrics/commit_" + h + "/";

				if (FileUtils.isFolderEmpty(pathToCheck)) {
					System.out.println("the hash" + h + "is empty.");

					String pathOutput2 = Paths.PATH_DATA + projectName + "/metrics2/commit_" + h + "/";
					// filter metrics.csv at this hash
					System.out.println("Filtering!!!");
					filterCSVFile(Paths.PATH_DATA + projectName + "/metrics/commit_" + h + "/", pathOutput2, elements);
					control++;
				}

			}
			System.out.println("Filtered : " + control);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void createReducedCSVFile(String projectName) {

		List<String> listHashs = IOUtils.readAnyFile(projectName + "/hashs.txt");

		for (int i = 0; i < listHashs.size(); i++) {
			System.out.println(i + "/" + listHashs.size());
			String h = listHashs.get(i);

			if (h.contains("null")) {
				continue;
			}

			String path = Paths.PATH_DATA + projectName + "/metrics/commit_" + h + "/";
			File inputDirectory = new File(path);

			if (!inputDirectory.exists()) {
				System.out.println("Input Path not exists: " + path);
				continue;
			}

			String pathOutput = Paths.PATH_DATA + projectName + "/metrics2/";
			File outputDirectory = new File(pathOutput);

			if (!outputDirectory.exists()) {
				if (outputDirectory.mkdir()) {
				}
			}

			String pathOutput2 = Paths.PATH_DATA + projectName + "/metrics2/commit_" + h + "/";
			File outputDirectory3 = new File(pathOutput2);

			if (!outputDirectory3.exists()) {
				if (outputDirectory3.mkdir()) {
				}
			}

			filterCSVFile(Paths.PATH_DATA + projectName + "/metrics/commit_" + h + "/", pathOutput2,
					ReaderUtils.getElementsWithBug("all_bugs.json", projectName));
		}

	}

	public static void makeHashFileByElement(String projectName) {

		List<String> elementsToGetHash = IOUtils.readAnyFile(projectName + "/elementsToGetMetrics.txt");
		List<String> commitsList = IOUtils.readAnyFile(projectName + "/commits_repo_table.txt");

		String outputPath = projectName + "/hashs_by_element/";
		File outputDirectory = new File(outputPath);

		if (!outputDirectory.exists()) {
			if (outputDirectory.mkdir()) {
			}
		}

		HashMap<String, String> commitsHash = new HashMap<String, String>();
		for (String commit : commitsList) {
			String[] commitCollumns = commit.split(",");
			String c1 = commitCollumns[1].replace(" ", "");
			c1 = c1.replace("\n", "");
			String c3 = commitCollumns[3].replace(" ", "");
			c3 = c3.replace("\n", "");
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

			for (double i = startCommit + 1; i <= endCommit + 1; i++) {
				outputFile += commitsHash.get(i + "") + "\n";
			}

			WriterUtils.writeMiningOutput(outputPath + elementCollumns[0] + ".txt", outputFile);
			count++;
		}

	}
}
