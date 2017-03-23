package br.ufal.br.operations;

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
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

import br.ufal.br.json.BugInfo;
import br.ufal.ic.objects.Commit;
import br.ufal.ic.objects.Metric;

public class Reader {

	public List<Commit> readCSV(String filename) {

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

	public List<BugInfo> readJSON(String path) {

		List<BugInfo> bugs = new ArrayList<BugInfo>();

		try {
			// Get Gson object
			Gson gson = new GsonBuilder().setPrettyPrinting().create();

			// read JSON file data as String
			String fileData = new String(Files.readAllBytes(Paths.get(path)));

			// parse json string to object
			List<LinkedTreeMap> treeBugs = gson.fromJson(fileData, List.class);

			for (LinkedTreeMap b : treeBugs) {
				BugInfo bug = new BugInfo();
				bug.setBug_id((String) b.get("bug_id"));
				bug.setElements((List) b.get("elements"));
				bug.setProject((String) b.get("project"));
				bug.setOrder_fixed((Double) b.get("order_fixed"));
				bug.setOrder_reported((Double) b.get("order_reported"));
				bugs.add(bug);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return bugs;
	}

	public List<Metric> readMetricsCSV(String path) {
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

	public ArrayList<Commit> readFile(String filename) {

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

	public ArrayList<String> readSecundaryFile(String filename) {

		ArrayList<String> commits = new ArrayList<String>();
		BufferedReader reader = null;
		try {
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

	public void writeFileCommits(String path, List<Commit> commits) {

		String text = "project,commit_order,commit_hash,commit_old\n";
		for (Commit c : commits) {
			text += c.getProjectName() + "," + c.getCommitOrder() + "," + c.getCommitHash() + "," + c.getCommitOld()
					+ "\n";
		}

		Writer wr = null;

		try {
			wr = new FileWriter(path + ".txt");
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

	public void writeMetrics(String path, List<Metric> metrics) {

		String text = "Kind, Name, File, AvgCyclomatic, AvgCyclomaticModified,"
				+ " AvgCyclomaticStrict, AvgEssential, AvgLine, AvgLineBlank, "
				+ "AvgLineCode, AvgLineComment, CountClassBase, CountClassCoupled, "
				+ "CountClassDerived, CountDeclClass, CountDeclClassMethod, "
				+ "CountDeclClassVariable, CountDeclFile, CountDeclFunction, "
				+ "CountDeclInstanceMethod, CountDeclInstanceVariable, "
				+ "CountDeclMethod, CountDeclMethodAll, CountDeclMethodDefault, "
				+ "CountDeclMethodPrivate, CountDeclMethodProtected, "
				+ "CountDeclMethodPublic, CountInput, CountLine, "
				+ "CountLineBlank, CountLineCode, CountLineCodeDecl, "
				+ "CountLineCodeExe, CountLineComment, CountOutput, CountPath, "
				+ "CountSemicolon, CountStmt, CountStmtDecl, CountStmtExe, Cyclomatic,"
				+ " CyclomaticModified, CyclomaticStrict, Essential, MaxCyclomatic, "
				+ "MaxCyclomaticModified, MaxCyclomaticStrict, MaxEssential, MaxInheritanceTree, "
				+ "MaxNesting, PercentLackOfCohesion, RatioCommentToCode, SumCyclomatic, "
				+ "SumCyclomaticModified, SumCyclomaticStrict, SumEssential\n";
		for (Metric m : metrics) {

		}

		Writer wr = null;

		try {
			wr = new FileWriter(path + ".txt");
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

	public void copyMetricsFile(String path, String path2, String filename) {

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

	public void getOtherHash() {

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

		ArrayList<String> commitSecundary5 = new ArrayList<String>();
		commitSecundary5 = readSecundaryFile("commit5.txt");

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
		for (int i = 0; i < commit.size(); i++) {
			for (int j = 0; j < commitSecundary5.size(); j++) {
				String[] hashs = commitSecundary5.get(j).split(";");
				if (hashs[1].contains(commit.get(i).getCommitHash())) {
					commit.get(i).setCommitOld(hashs[0]);
				}
			}
		}

		for (int i = 0; i < commit.size(); i++) {
			for (int j = 0; j < commitSecundary6.size(); j++) {
				String[] hashs = commitSecundary6.get(j).split(";");
				if (hashs[1].contains(commit.get(i).getCommitHash())) {
					commit.get(i).setCommitOld(hashs[0]);
				}
			}
		}

		writeFileCommits("commitsNew", commit);
	}
}
