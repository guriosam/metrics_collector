package br.ufal.ic.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.ufal.ic.objects.Commit;
import br.ufal.ic.objects.Metric;

public class IOUtils {

	private String projectName;

	public IOUtils(String projectName) {
		this.projectName = projectName;
	}

	/*
	 * 1. Read commits.csv and compare with commit<n>.txt to make
	 * commits_repo_table.txt 2. Make hashs_project.txt from
	 * commits_repo_table.txt 3. Read hashs_project.txt 3. Read all_bugs.json
	 * and make it a list. 4. Read metrics.csv and reduce it to metrics2.csv 5.
	 * Read metrics2.csv and make it a list and a hashmap. 6. Write metrics2.csv
	 * minning output in element_of_bug.csv 7.
	 * 
	 * 
	 * Utils
	 * 
	 * 1. Read file and put lines in string list. 2. Check if fileExists 3. Copy
	 * the metrics.csv from the repository folder that i'm on checkout to
	 * another folder, to export the data from the checkout.
	 */

	public static List<String> readAnyFile(String filename) {

		List<String> lines = new ArrayList<String>();
		BufferedReader reader = null;
		try {

			File f = new File(filename);

			if (!FileUtils.fileExists(f, filename)) {
				return lines;
			}

			reader = new BufferedReader(new FileReader(filename));

			String line = reader.readLine();

			while (line != null) {
				lines.add(line);
				line = reader.readLine();
			}

			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in file: " + filename);
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();

				}
			}
		}

		return lines;
	}

	public static void copyMetricsFile(String path, String path2, String filename) {

		InputStream is = null;
		OutputStream os = null;
		try {
			File outputDirectory = new File(path2);

			if (!outputDirectory.exists()) {
				if (outputDirectory.mkdir()) {
				}
			}

			Writer wr = new FileWriter(path2 + filename);
			wr.close();

			is = new FileInputStream(path + filename);
			os = new FileOutputStream(path2 + filename);

			byte[] buffer = new byte[1024];
			int length;

			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
				os.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public static List<Commit> readCommitList(String filename) {

		List<Commit> commits = new ArrayList<>();

		List<String> commitFile = readAnyFile(filename);

		for (String commitLine : commitFile) {
			String[] c = commitLine.split(",");
			Commit commit = new Commit();
			commit.setProjectName(c[0]);
			commit.setCommitOrder(Double.parseDouble(c[1]));
			commit.setCommitHash(c[2]);
			commits.add(commit);
		}

		return commits;

	}

	public static HashMap<String, Metric> getBruteMetricsMap(String path) {

		List<Metric> metrics = readBruteMetricsFile(path);

		HashMap<String, Metric> metricsMap = new HashMap<>();

		for (Metric m : metrics) {
			String name = m.getName();
			name = name.replace("\"", "");
			metricsMap.put(name, m);
		}

		return metricsMap;

	}

	public static HashMap<String, Metric> getFilteredMetricsMap(String path) {

		List<Metric> metrics = getMetricsOnCSV(path);

		HashMap<String, Metric> metricsMap = new HashMap<>();

		for (Metric m : metrics) {
			metricsMap.put(m.getName(), m);
		}

		return metricsMap;

	}

	public static List<Metric> readBruteMetricsFile(String path) {
		List<Metric> metrics = new ArrayList<Metric>();

		try {

			File f = new File(path);

			if (!f.exists()) {
				System.out.println("Not exists " + path);
				return new ArrayList<>();
			}
			BufferedReader reader = new BufferedReader(new FileReader(path));

			String line = reader.readLine();
			line = reader.readLine();

			List<String[]> myEntries = new ArrayList<String[]>();

			while (line != null) {

				if (line.contains("\"")) {
					String firstString = line.substring(0, line.indexOf("\"") - 1);
					String middleString = line.substring(line.indexOf("\""), line.indexOf(")") + 2);
					String finalString = line.substring(line.indexOf(")") + 3);

					String[] end = finalString.split(",", -1);

					int size = end.length + 2;

					String[] metric = new String[size];

					metric[0] = firstString;

					metric[1] = middleString;

					for (int i = 2; i < end.length + 2; i++) {
						metric[i] = end[i - 2];
					}

					myEntries.add(metric);

				}

				line = reader.readLine();
			}

			for (String[] info : myEntries) {
				if (info[3].contains("AvgCyclomatic")) {
					continue;
				}

				if (info[0].contains("Method") || info[0].contains("Constructor")) {
					Metric m = new Metric();

					int i = 0;

					m.setKind(info[i++]);

					if (info[i].contains("?")) {
						info[i] = info[i].replace("?", "");
					}

					if (info[i].contains("<")) {
						info[i] = info[i].replace("<", "");
					}

					if (info[i].contains(">")) {
						info[i] = info[i].replace(">", "");
					}

					m.setName(info[i++]);
					m.setFile(info[i++]);
					m.setAvgCyclomatic(info[i++]);
					m.setAvgCyclomaticModified(info[i++]);
					m.setAvgCyclomaticStrict(info[i++]);
					m.setAvgEssential(info[i++]);
					m.setAvgLine(info[i++]);
					m.setAvgLineBlank(info[i++]);
					m.setAvgLineCode(info[i++]);
					m.setAvgLineComment(info[i++]);
					m.setCountClassBase(info[i++]);
					m.setCountClassCoupled(info[i++]);
					m.setCountClassDerived(info[i++]);
					m.setCountDeclClass(info[i++]);
					m.setCountDeclClassMethod(info[i++]);
					m.setCountDeclClassVariable(info[i++]);
					m.setCountDeclFile(info[i++]);
					m.setCountDeclFunction(info[i++]);
					m.setCountDeclInstanceMethod(info[i++]);
					m.setCountDeclInstanceVariable(info[i++]);
					m.setCountDeclMethod(info[i++]);
					m.setCountDeclMethodAll(info[i++]);
					m.setCountDeclMethodDefault(info[i++]);
					m.setCountDeclMethodPrivate(info[i++]);
					m.setCountDeclMethodProtected(info[i++]);
					m.setCountDeclMethodPublic(info[i++]);
					m.setCountInput(info[i++]);
					m.setCountLine(info[i++]);
					m.setCountLineBlank(info[i++]);
					m.setCountLineCode(info[i++]);
					m.setCountLineCodeDecl(info[i++]);
					m.setCountLineCodeExe(info[i++]);
					m.setCountLineComment(info[i++]);
					m.setCountOutput(info[i++]);
					m.setCountPath(info[i++]);
					m.setCountSemicolon(info[i++]);
					m.setCountStmt(info[i++]);
					m.setCountStmtDecl(info[i++]);
					m.setCountStmtExe(info[i++]);
					m.setCyclomatic(info[i++]);
					m.setCyclomaticModified(info[i++]);
					m.setCyclomaticStrict(info[i++]);
					m.setEssential(info[i++]);
					m.setMaxCyclomatic(info[i++]);
					m.setMaxCyclomaticModified(info[i++]);
					m.setMaxCyclomaticStrict(info[i++]);
					m.setMaxEssential(info[i++]);
					m.setMaxInheritanceTree(info[i++]);
					m.setMaxNesting(info[i++]);
					m.setPercentLackOfCohesion(info[i++]);
					m.setRatioCommentToCode(info[i++]);
					m.setSumCyclomatic(info[i++]);
					m.setSumCyclomaticModified(info[i++]);
					m.setSumCyclomaticStrict(info[i++]);
					m.setSumEssential(info[i++]);

					metrics.add(m);
				}

			}

			reader.close();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return metrics;

	}

	public static List<Metric> getMetricsOnCSV(String path) {
		List<Metric> metrics = new ArrayList<Metric>();

		try {

			File f = new File(path);

			if (!f.exists()) {
				System.out.println("Not exists " + path);
				return new ArrayList<>();
			}
			BufferedReader reader = new BufferedReader(new FileReader(path));

			String line = reader.readLine();
			line = reader.readLine();

			List<String[]> myEntries = new ArrayList<String[]>();

			while (line != null) {

				if (line.contains("\"")) {
					System.out.println(line);
					String firstString = line.substring(0, line.indexOf("\"") - 1);
					String middleString = line.substring(line.indexOf("\""), line.indexOf(")") + 2);
					String finalString = line.substring(line.indexOf(")") + 3);

					String[] first = firstString.split(",");
					String[] end = finalString.split(",", -1);

					int size = first.length + end.length + 1;

					String[] metric = new String[size];

					metric[0] = first[0];
					metric[1] = first[1];
					metric[2] = middleString;

					for (int i = 3; i < end.length + 3; i++) {
						metric[i] = end[i - 3];
					}

					myEntries.add(metric);

				}

				line = reader.readLine();
			}

			for (String[] info : myEntries) {
				if (info[4].contains("AvgCyclomatic")) {
					continue;
				}

				if (info[1].contains("Method") || info[1].contains("Constructor")) {
					Metric m = new Metric();

					int i = 0;

					m.setCommit(info[i++]);
					m.setKind(info[i++]);

					if (info[i].contains("?")) {
						info[i] = info[i].replace("?", "");
					}

					if (info[i].contains("<")) {
						info[i] = info[i].replace("<", "");
					}

					if (info[i].contains(">")) {
						info[i] = info[i].replace(">", "");
					}

					m.setName(info[i++]);
					m.setFile(info[i++]);
					m.setAvgCyclomatic(info[i++]);
					m.setAvgCyclomaticModified(info[i++]);
					m.setAvgCyclomaticStrict(info[i++]);
					m.setAvgEssential(info[i++]);
					m.setAvgLine(info[i++]);
					m.setAvgLineBlank(info[i++]);
					m.setAvgLineCode(info[i++]);
					m.setAvgLineComment(info[i++]);
					m.setCountClassBase(info[i++]);
					m.setCountClassCoupled(info[i++]);
					m.setCountClassDerived(info[i++]);
					m.setCountDeclClass(info[i++]);
					m.setCountDeclClassMethod(info[i++]);
					m.setCountDeclClassVariable(info[i++]);
					m.setCountDeclFile(info[i++]);
					m.setCountDeclFunction(info[i++]);
					m.setCountDeclInstanceMethod(info[i++]);
					m.setCountDeclInstanceVariable(info[i++]);
					m.setCountDeclMethod(info[i++]);
					m.setCountDeclMethodAll(info[i++]);
					m.setCountDeclMethodDefault(info[i++]);
					m.setCountDeclMethodPrivate(info[i++]);
					m.setCountDeclMethodProtected(info[i++]);
					m.setCountDeclMethodPublic(info[i++]);
					m.setCountInput(info[i++]);
					m.setCountLine(info[i++]);
					m.setCountLineBlank(info[i++]);
					m.setCountLineCode(info[i++]);
					m.setCountLineCodeDecl(info[i++]);
					m.setCountLineCodeExe(info[i++]);
					m.setCountLineComment(info[i++]);
					m.setCountOutput(info[i++]);
					m.setCountPath(info[i++]);
					m.setCountSemicolon(info[i++]);
					m.setCountStmt(info[i++]);
					m.setCountStmtDecl(info[i++]);
					m.setCountStmtExe(info[i++]);
					m.setCyclomatic(info[i++]);
					m.setCyclomaticModified(info[i++]);
					m.setCyclomaticStrict(info[i++]);
					m.setEssential(info[i++]);
					m.setMaxCyclomatic(info[i++]);
					m.setMaxCyclomaticModified(info[i++]);
					m.setMaxCyclomaticStrict(info[i++]);
					m.setMaxEssential(info[i++]);
					m.setMaxInheritanceTree(info[i++]);
					m.setMaxNesting(info[i++]);
					m.setPercentLackOfCohesion(info[i++]);
					m.setRatioCommentToCode(info[i++]);
					m.setSumCyclomatic(info[i++]);
					m.setSumCyclomaticModified(info[i++]);
					m.setSumCyclomaticStrict(info[i++]);
					m.setSumEssential(info[i++]);

					metrics.add(m);
				}

			}

			reader.close();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return metrics;

	}

	public static void writeText(String path, String text) {
		// TODO Auto-generated method stub

		Writer wr = null;

		try {
			wr = new FileWriter(path);
			wr.write(text);
			wr.close();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (wr != null) {
					wr.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

	}

	public static void writeAnyFile(String path, List<String> lines) {
		// TODO Auto-generated method stub

		String text = "";
		for (String line : lines) {
			text += line + "\n";
		}

		Writer wr = null;

		try {
			wr = new FileWriter(path);
			wr.write(text);
			wr.close();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (wr != null) {
					wr.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

	}

}
