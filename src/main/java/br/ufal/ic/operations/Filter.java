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
			if(files != null){
				for (File file : files) {
					fileNames.add(file.getName().replace(".txt", ""));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return fileNames;
	}
	
	public static boolean isFolderEmpty(String path) {

		if (path == null) {
			System.out.println("Folder name is null");
		}

		try {
			
			File f = new File(path);
			
			if(f.list() == null){
				System.out.println("IS EMPTY!");
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

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

				if (csvLine[1].contains("?")) {
					csvLine[1] = csvLine[1].replaceAll("?", "");
				}

				if (csvLine[0].contains("Method") || csvLine[0].contains("Constructor")) {
					for (String j : elements) {
						if (j.contains(csvLine[1])) {
							metrics.add(line);
						}
					}

				}
			}

			System.out.println("WRITING FILTERED DATA!!!");
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
	
	
	public static void checkReducedCSVFile(String projectName, List<String> elements){
		

			try {
				int control = 0;
				// get the commit's hash.
				List<String> listHashs = IO.readAnyFile(projectName + "/hashs_" + projectName + ".txt");
				int listSize = listHashs.size();
				//run each hash
				for (int i = 0; i < listSize ; i++) {
					System.out.println(i + "/" + listSize);
					String h = listHashs.get(i);

					String pathToCheck = Paths.PATH_DATA + projectName + "/metrics/commit_" + h + "/";
					//check if dir is empty
					if (isFolderEmpty(pathToCheck)) {
						System.out.println("the hash" + h + "is empty.");
						
						String pathOutput2 = Paths.PATH_DATA + projectName + "/metrics2/commit_" + h + "/";
						//filter metrics.csv at this hash
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

		List<String> listHashs = IO.readAnyFile(projectName + "/hashs_" + projectName + ".txt");
		
		for (int i = 0; i < listHashs.size(); i++) {
			//System.out.println(i + "/" + listHashs.size());
			if(i == listHashs.size() - 1){

				System.out.println("DONE!");
			}
			String h = listHashs.get(i);

			String path = Paths.PATH_DATA + projectName + "/metrics/commit_" + h + "/";
			File outputDirectory = new File(path);

			if (!outputDirectory.exists()) {
				if (outputDirectory.mkdir()) {
				}
			}

			String pathOutput = Paths.PATH_DATA + projectName + "/metrics2/";
			File outputDirectory2 = new File(pathOutput);

			if (!outputDirectory2.exists()) {
				System.out.println("CREATING DIR METRICS2");
				if (outputDirectory2.mkdir()) {
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

		List<String> elementsToGetHash = IO.readAnyFile(projectName + "/elementsToGetMetrics_" + projectName + ".txt");
		List<String> commitsList = IO.readAnyFile(projectName + "/commits_repo_table.txt");
		HashMap<String, String> commitsHash = new HashMap<String, String>();
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
