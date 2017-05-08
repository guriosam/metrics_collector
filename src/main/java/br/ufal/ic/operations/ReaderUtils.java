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
import br.ufal.ic.objects.Commit;
import br.ufal.ic.objects.Metric;

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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<BugInfo> writeBugJsonIntoBugList(String path, String projectName) {

		List<BugInfo> bugs = new ArrayList<BugInfo>();

		try {
			// Get Gson object
			Gson gson = new GsonBuilder().setPrettyPrinting().create();

			// read JSON file data as String
			String fileData = new String(Files.readAllBytes(Paths.get(path)));

			// parse json string to object
			@SuppressWarnings("rawtypes")
			List<LinkedTreeMap> treeBugs = gson.fromJson(fileData, List.class);

			for (LinkedTreeMap<?, ?> b : treeBugs) {
				if (b.get("project").equals(projectName)) {
					BugInfo bug = new BugInfo();
					bug.setBug_id((String) b.get("bug_id"));
					bug.setElements((List) b.get("elements"));
					bug.setProject((String) b.get("project"));
					bug.setOrder_fixed((Double) b.get("order_fixed"));
					bug.setOrder_reported((Double) b.get("order_reported"));
					bugs.add(bug);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return bugs;
	}

	public HashMap<String, Metric> getHashMetricsCSV(String path) {
		HashMap<String, Metric> metrics = new HashMap<String, Metric>();

		try {

			File f = new File(path + "metrics2.csv");

			if (!f.exists()) {
				System.out.println("Not exists " + path);
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

					// metrics.add(m);
					metrics.put(m.getName(), m);
				}

			}

			csvReader.close();

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

	public static List<String> getElementsWithBug(String path, String projectName) {

		List<String> bugs = new ArrayList<String>();

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
					bug.setElements((List) b.get("elements"));

					for (String s : bug.getElements()) {

						String e = s;
						if (e.contains("org.apache")) {
							e = e.substring(e.indexOf("org.apache"));
						}
						if (e.contains("javax.")) {
							e = e.substring(e.indexOf("javax"));
						}
						bugs.add(e);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return bugs;
	}
}
