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
import java.util.List;

import com.opencsv.CSVReader;

import br.ufal.ic.objects.Commit;
import br.ufal.ic.operations.Filter;
import br.ufal.ic.operations.WriterUtils;

public class IO {

	private String projectName;

	public IO(String projectName) {
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

			if (!fileExists(f, filename)) {
				return lines;
			}

			reader = new BufferedReader(new FileReader(filename));

			// read file line by line
			String line = reader.readLine();

			if (line.contains("project")) {
				line = reader.readLine();
			}

			while (line != null) {
				lines.add(line);
				line = reader.readLine();
			}

			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(filename);
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

	public static boolean fileExists(File f, String filename) {
		if (!f.exists()) {
			System.out.println("File " + filename + " does not exists.");
			return false;
		}
		return true;
	}

	public static ArrayList<Commit> readCommitFile(String f) {
		ArrayList<Commit> commits = new ArrayList<Commit>();

		try {

			CSVReader csvReader = new CSVReader(new FileReader(f));
			List<String[]> myCommits = csvReader.readAll();

			for (String[] myCommit : myCommits) {
				Commit commit = new Commit();
				commit.setProjectName(myCommit[0]);
				commit.setCommitOrder(Double.parseDouble(myCommit[1]));
				commit.setCommitHash(myCommit[2]);
				commits.add(commit);
			}

			csvReader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return commits;
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

	public static List<String[]> readCSVFile(String filename) {

		try {
			CSVReader csvReader = new CSVReader(new FileReader(filename));
			List<String[]> lines = csvReader.readAll();
			csvReader.close();
			return lines;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<String[]>();

	}

	public static List<String> readCSVFileByCollumn(String filename, int collumn) {

		List<String> fileLines = readAnyFile(filename);
		
		
		List<String> collumnRows = new ArrayList<>();

		for (String lineAll : fileLines) {
			String[] line = lineAll.split(",");

			if (collumn <= line.length) {
				String collumnData = line[collumn];
				if (collumnData.equals("Commit")) {
					continue;
				}
				if(filename.contains("org.apache.catalina.webresources.StandardRoot.listWebAppPaths(String)")){
				//	System.out.println(collumnData);
				}

				collumnRows.add(collumnData);
			}
		}
		return collumnRows;
	}
}
