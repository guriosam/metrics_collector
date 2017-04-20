package br.ufal.ic.operations;

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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import br.ufal.ic.json.BugInfo;
import br.ufal.ic.model.Metric;
import br.ufal.ic.objects.Commit;

public class ReaderUtils {

	public static List<Commit> readCSV(String filename) {

		List<Commit> commits = new ArrayList<Commit>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filename));

			// read file line by line
			String line = reader.readLine();

			if (line.contains("project")) {
				line = reader.readLine();
			}

			while (line != null) {
				String[] info = line.split(",");

				Commit commit = new Commit();
				commit.setProjectName(info[0]);
				commit.setCommitOrder(Double.parseDouble(info[1]));
				commit.setCommitHash(info[2]);
				commits.add(commit);

				line = reader.readLine();
			}

			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

		return commits;
	}

	public static List<BugInfo> readJSON(String path, String projectName) {

		List<BugInfo> bugs = new ArrayList<BugInfo>();

		try {
			// Get Gson object
			Gson gson = new GsonBuilder().setPrettyPrinting().create();

			// read JSON file data as String
			String fileData = new String(Files.readAllBytes(Paths.get(path)));

			// parse json string to object
			List<LinkedTreeMap> treeBugs = gson.fromJson(fileData, List.class);

			for (LinkedTreeMap b : treeBugs) {
				if (b.get("project").equals(projectName)) {
					BugInfo bug = new BugInfo();
					bug.setBug_id((String) b.get("bug_id"));
					bug.setElements((List) b.get("elements"));
					bug.setProject((String) b.get("project"));
					bug.setOrder_fixed((Double) b.get("order_fixed"));
					bug.setOrder_reported((Double) b.get("order_reported"));
					bugs.add(bug);

				}
				/*
				 * BugInfo bug = new BugInfo(); bug.setBug_id((String)
				 * b.get("bug_id")); bug.setElements((List) b.get("elements"));
				 * bug.setProject((String) b.get("project"));
				 * bug.setOrder_fixed((Double) b.get("order_fixed"));
				 * bug.setOrder_reported((Double) b.get("order_reported"));
				 * bugs.add(bug);
				 */
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return bugs;
	}

	public static List<Metric> readMetricsCSV(String path) {
		List<Metric> metrics = new ArrayList<Metric>();

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(path + "metrics.csv"));

			// read file line by line
			String line = reader.readLine();

			if (line.contains("AvgCyclomatic")) {
				line = reader.readLine();
			}

			while (line != null) {

				String[] info = line.split(",");

				Metric m = new Metric();
				m.setKind(info[0]);
				m.setFile(info[2]);
				m.setName(info[1].replaceAll("\"", ""));

				String aux = "";
				for (int i = 3; i < info.length - 1; i++) {
					aux += info[i] + ",";
				}

				aux += info[info.length - 1];

				// m.setAllValues(aux);

				metrics.add(m);

				line = reader.readLine();
			}

			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

		return metrics;

	}

	public static HashMap<String, Metric> getHashMetricsCSV(String path) {
		HashMap<String, Metric> metrics = new HashMap<String, Metric>();

		try {

			File f = new File(path + "metrics.csv");

			if (!f.exists()) {
				return null;
			}

			CSVReader csvReader = new CSVReader(new FileReader(f));
			List<String[]> myEntries = csvReader.readAll();

			CSVWriter csvWriter = new CSVWriter(new FileWriter(f), ';');

			for (String[] info : myEntries) {
				if (info[3].contains("AvgCyclomatic")) {
					continue;
				}

				if (info[0].contains("Method") || info[0].contains("Constructor")) {
					Metric m = new Metric();
					m.setKind(info[0]);
					m.setName(info[1]);
					m.setFile(info[2]);
					m.setAvgCyclomatic(info[3]);
					m.setAvgCyclomaticModified(info[4]);
					m.setAvgCyclomaticStrict(info[5]);
					m.setAvgEssential(info[6]);
					m.setAvgLine(info[7]);
					m.setAvgLineBlank(info[8]);
					m.setAvgLineCode(info[9]);
					m.setAvgLineComment(info[10]);
					m.setCountClassBase(info[11]);
					m.setCountClassCoupled(info[12]);
					m.setCountClassDerived(info[13]);
					m.setCountDeclClass(info[14]);
					m.setCountDeclClassMethod(info[15]);
					m.setCountDeclClassVariable(info[16]);
					m.setCountDeclFile(info[17]);
					m.setCountDeclFunction(info[18]);
					m.setCountDeclInstanceMethod(info[19]);
					m.setCountDeclInstanceVariable(info[20]);
					m.setCountDeclMethod(info[21]);
					m.setCountDeclMethodAll(info[22]);
					m.setCountDeclMethodDefault(info[23]);
					m.setCountDeclMethodPrivate(info[24]);
					m.setCountDeclMethodProtected(info[25]);
					m.setCountDeclMethodPublic(info[26]);
					m.setCountInput(info[27]);
					m.setCountLine(info[28]);
					m.setCountLineBlank(info[29]);
					m.setCountLineCode(info[30]);
					m.setCountLineCodeDecl(info[31]);
					m.setCountLineCodeExe(info[32]);
					m.setCountLineComment(info[33]);
					m.setCountOutput(info[34]);
					m.setCountPath(info[35]);
					m.setCountSemicolon(info[36]);
					m.setCountStmt(info[37]);
					m.setCountStmtDecl(info[38]);
					m.setCountStmtExe(info[39]);
					m.setCyclomatic(info[40]);
					m.setCyclomaticModified(info[41]);
					m.setCyclomaticStrict(info[42]);
					m.setEssential(info[43]);
					m.setMaxCyclomatic(info[44]);
					m.setMaxCyclomaticModified(info[45]);
					m.setMaxCyclomaticStrict(info[46]);
					m.setMaxEssential(info[47]);
					m.setMaxInheritanceTree(info[48]);
					m.setMaxNesting(info[49]);
					m.setPercentLackOfCohesion(info[50]);
					m.setRatioCommentToCode(info[51]);
					m.setSumCyclomatic(info[52]);
					m.setSumCyclomaticModified(info[53]);
					m.setSumCyclomaticStrict(info[54]);
					m.setSumEssential(info[55]);

					// metrics.add(m);
					metrics.put(m.getName(), m);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return metrics;

	}

	public static ArrayList<Metric> getListMetricsCSV(String path, String commit) {
		ArrayList<Metric> metrics = new ArrayList<Metric>();

		try {

			File f = new File(path + "metrics.csv");

			if (!f.exists()) {
				return null;
			}

			CSVReader csvReader = new CSVReader(new FileReader(f));
			List<String[]> myEntries = csvReader.readAll();

			for (String[] info : myEntries) {
				if (info[3].contains("AvgCyclomatic")) {
					continue;
				}

				if (info[0].contains("Method") || info[0].contains("Constructor")) {
					Metric m = new Metric();
					m.setKind(info[0]);
					m.setName(info[1]);
					m.setFile(info[2]);
					m.setAvgCyclomatic(info[3]);
					m.setAvgCyclomaticModified(info[4]);
					m.setAvgCyclomaticStrict(info[5]);
					m.setAvgEssential(info[6]);
					m.setAvgLine(info[7]);
					m.setAvgLineBlank(info[8]);
					m.setAvgLineCode(info[9]);
					m.setAvgLineComment(info[10]);
					m.setCountClassBase(info[11]);
					m.setCountClassCoupled(info[12]);
					m.setCountClassDerived(info[13]);
					m.setCountDeclClass(info[14]);
					m.setCountDeclClassMethod(info[15]);
					m.setCountDeclClassVariable(info[16]);
					m.setCountDeclFile(info[17]);
					m.setCountDeclFunction(info[18]);
					m.setCountDeclInstanceMethod(info[19]);
					m.setCountDeclInstanceVariable(info[20]);
					m.setCountDeclMethod(info[21]);
					m.setCountDeclMethodAll(info[22]);
					m.setCountDeclMethodDefault(info[23]);
					m.setCountDeclMethodPrivate(info[24]);
					m.setCountDeclMethodProtected(info[25]);
					m.setCountDeclMethodPublic(info[26]);
					m.setCountInput(info[27]);
					m.setCountLine(info[28]);
					m.setCountLineBlank(info[29]);
					m.setCountLineCode(info[30]);
					m.setCountLineCodeDecl(info[31]);
					m.setCountLineCodeExe(info[32]);
					m.setCountLineComment(info[33]);
					m.setCountOutput(info[34]);
					m.setCountPath(info[35]);
					m.setCountSemicolon(info[36]);
					m.setCountStmt(info[37]);
					m.setCountStmtDecl(info[38]);
					m.setCountStmtExe(info[39]);
					m.setCyclomatic(info[40]);
					m.setCyclomaticModified(info[41]);
					m.setCyclomaticStrict(info[42]);
					m.setEssential(info[43]);
					m.setMaxCyclomatic(info[44]);
					m.setMaxCyclomaticModified(info[45]);
					m.setMaxCyclomaticStrict(info[46]);
					m.setMaxEssential(info[47]);
					m.setMaxInheritanceTree(info[48]);
					m.setMaxNesting(info[49]);
					m.setPercentLackOfCohesion(info[50]);
					m.setRatioCommentToCode(info[51]);
					m.setSumCyclomatic(info[52]);
					m.setSumCyclomaticModified(info[53]);
					m.setSumCyclomaticStrict(info[54]);
					m.setSumEssential(info[55]);
					m.setCommit(commit);

					metrics.add(m);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return metrics;

	}

	public static ArrayList<Commit> readFile(String filename) {

		ArrayList<Commit> commits = new ArrayList<Commit>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filename));

			// read file line by line
			String line = reader.readLine();

			if (line.contains("project")) {
				line = reader.readLine();
			}

			while (line != null) {
				String[] info = line.split(",");

				Commit commit = new Commit();
				commit.setProjectName(info[0]);
				commit.setCommitOrder(Double.parseDouble(info[1]));
				commit.setCommitHash(info[2]);
				commits.add(commit);

				line = reader.readLine();
			}

			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

		return commits;
	}

	public static ArrayList<String> readSecundaryFile(String filename) {

		ArrayList<String> commits = new ArrayList<String>();
		BufferedReader reader = null;
		try {

			File f = new File(filename);

			if (!f.exists()) {
				System.out.println("File " + filename + " does not exists.");
			}

			reader = new BufferedReader(new FileReader(filename));

			// read file line by line
			String line = reader.readLine();

			if (line.contains("project")) {
				line = reader.readLine();
			}

			while (line != null) {

				commits.add(line);
				line = reader.readLine();
			}

			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

		return commits;
	}

	public static void copyMetricsFile(String path, String path2, String filename) {

		/*
		 * BufferedReader reader = null; try { reader = new BufferedReader(new
		 * FileReader(path + filename));
		 * 
		 * //List<String> lines = new ArrayList<String>(); String text = "";
		 * String line = reader.readLine();
		 * 
		 * //System.out.println("Reading file...");
		 * 
		 * while (line != null) {
		 * 
		 * //System.out.println(line); //lines.add(line); text += line + "\n";
		 * 
		 * line = reader.readLine(); }
		 * 
		 * reader.close();
		 * 
		 * //System.out.println("Reading closed");
		 * 
		 * File outputDirectory = new File(path2);
		 * 
		 * System.out.println(path2);
		 * 
		 * if (!outputDirectory.exists()) { if (outputDirectory.mkdir()) { } }
		 * 
		 * 
		 * Writer wr = new FileWriter(path2 + "/" + filename); wr.write(text);
		 * wr.close();
		 * 
		 * 
		 * } catch (Exception e) { e.printStackTrace(); if (reader != null) {
		 * try { reader.close(); } catch (IOException e1) { // TODO
		 * Auto-generated catch block e1.printStackTrace(); } } }
		 */

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

	public static void getOtherHash() {

		ArrayList<Commit> commit = new ArrayList<Commit>();
		commit = readFile("commits.csv");

		ArrayList<String> commitSecundary1 = new ArrayList<String>();
		commitSecundary1 = readSecundaryFile("commit1.txt");

		ArrayList<String> commitSecundary2 = new ArrayList<String>();
		commitSecundary2 = readSecundaryFile("commit2.txt");

		ArrayList<String> commitSecundary3 = new ArrayList<String>();
		commitSecundary3 = readSecundaryFile("commit3.txt");

		ArrayList<String> commitSecundary4 = new ArrayList<String>();
		commitSecundary4 = readSecundaryFile("commit4.txt");

		// ArrayList<String> commitSecundary5 = new ArrayList<String>();
		// commitSecundary5 = readSecundaryFile("commit5.txt");

		ArrayList<String> commitSecundary6 = new ArrayList<String>();
		commitSecundary6 = readSecundaryFile("commit6.txt");

		for (int i = 0; i < commit.size(); i++) {
			for (int j = 0; j < commitSecundary1.size(); j++) {
				String[] hashs = commitSecundary1.get(j).split(";");
				if (hashs[1].equals(commit.get(i).getCommitHash())) {
					commit.get(i).setCommitOld(hashs[0]);
				}
			}
		}

		for (int i = 0; i < commit.size(); i++) {
			for (int j = 0; j < commitSecundary2.size(); j++) {
				String[] hashs = commitSecundary2.get(j).split(";");
				if (hashs[1].contains(commit.get(i).getCommitHash())) {
					commit.get(i).setCommitOld(hashs[0]);
				}
			}
		}

		for (int i = 0; i < commit.size(); i++) {
			for (int j = 0; j < commitSecundary3.size(); j++) {
				String[] hashs = commitSecundary3.get(j).split(";");
				if (hashs[1].contains(commit.get(i).getCommitHash())) {
					commit.get(i).setCommitOld(hashs[0]);
				}
			}
		}

		for (int i = 0; i < commit.size(); i++) {
			for (int j = 0; j < commitSecundary4.size(); j++) {
				String[] hashs = commitSecundary4.get(j).split(";");
				if (hashs[1].contains(commit.get(i).getCommitHash())) {
					commit.get(i).setCommitOld(hashs[0]);
				}
			}
		}
		/*
		 * for (int i = 0; i < commit.size(); i++) { for (int j = 0; j <
		 * commitSecundary5.size(); j++) { String[] hashs =
		 * commitSecundary5.get(j).split(";"); if
		 * (hashs[1].contains(commit.get(i).getCommitHash())) {
		 * commit.get(i).setCommitOld(hashs[0]); } } }
		 */

		for (int i = 0; i < commit.size(); i++) {
			for (int j = 0; j < commitSecundary6.size(); j++) {
				String[] hashs = commitSecundary6.get(j).split(";");
				if (hashs[1].contains(commit.get(i).getCommitHash())) {
					commit.get(i).setCommitOld(hashs[0]);
				}
			}
		}

		WriterUtils.writeFileCommits("commits_repo_table", commit);
	}

	public static void changeCSVSeparator(String projectName) {

		List<String> listHashs = ReaderUtils.readSecundaryFile("hashs_" + projectName + ".txt");

		for (String h : listHashs) {

			File f = new File("C:/Users/gurio/Desktop/Pesquisa/Puc/Dados/" + projectName + "/metrics/commit_" + h + "/"
					+ "metrics.csv");

			if (!f.exists()) {
				return;
			}

			try {

				CSVReader csvReader = new CSVReader(new FileReader(f));
				List<String[]> myEntries = csvReader.readAll();

				File f2 = new File("C:/Users/gurio/Desktop/Pesquisa/Puc/Dados/" + projectName + "/metrics/commit_" + h
						+ "/" + "metrics2.csv");

				CSVWriter csvWriter = new CSVWriter(new FileWriter(f2), ';');
				csvWriter.writeAll(myEntries);
				
				System.out.println(h);
				break;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
