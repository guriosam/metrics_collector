package br.ufal.br.operations;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

	public void readMetricsCSV(String path){
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
				
				System.out.println(m);
				
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
		
	}
	
}
