package br.ufal.ic.utils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

import br.ufal.ic.objects.Bug;

public class JSONUtils {

	/*
	 * Retorna os dados do JSON Ex: { "project": "apache_derby", "bug_id":
	 * "5734", "order_reported": 6531, "order_fixed": 6545, "elements": [
	 * "java.testing.org.apache.derbyTesting.junit.CleanDatabaseTestSetup.setUp()"]
	 * }
	 */

	public static List<Bug> writeBugJsonIntoBugList(String path, String projectName) {

		List<Bug> bugs = new ArrayList<Bug>();

		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String fileData = new String(Files.readAllBytes(Paths.get(path)));
			List<LinkedTreeMap> treeBugs = gson.fromJson(fileData, List.class);

			for (LinkedTreeMap<?, ?> b : treeBugs) {
				if (b.get("project").equals(projectName)) {
					Bug bug = new Bug();
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

	public static List<String> getElementsByProject(String path, String projectName) {

		List<String> bugs = new ArrayList<String>();

		try {

			List<Bug> reports = getElements(path);

			for (Bug report : reports) {
				if (report.getProject().equals(projectName)) {
					for (String s : report.getElements()) {

						String e = s;
						if (e.contains("org.apache")) {
							e = e.substring(e.indexOf("org.apache"));
						}
						if (e.contains("javax.")) {
							e = e.substring(e.indexOf("javax"));
						}
						if (!bugs.contains(e)) {
							bugs.add(e);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return bugs;
	}

	public static List<Bug> getElements(String path) {

		List<Bug> bugs = new ArrayList<>();

		try {
			// Get Gson object
			Gson gson = new GsonBuilder().setPrettyPrinting().create();

			// read JSON file data as String
			String fileData = new String(Files.readAllBytes(Paths.get(path)));

			// parse json string to object
			List<LinkedTreeMap> treeBugs = gson.fromJson(fileData, List.class);

			for (LinkedTreeMap b : treeBugs) {
				Bug bug = new Bug();
				bug.setProject((String) b.get("project"));
				bug.setElements((List) b.get("elements"));
				bug.setBug_id((String) b.get("b.bug_id"));
				bug.setOrder_fixed((Double) b.get("order_fixed"));
				bug.setOrder_reported((Double) b.get("order_reported"));

				bugs.add(bug);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return bugs;
	}

	public static List<String> getElementsById(String path, List<String> ids) {

		List<String> bugs = new ArrayList<String>();

		try {

			List<Bug> reports = getElements(path);

			for (Bug report : reports) {
				if (ids.contains(report.getBug_id())) {
					for (String s : report.getElements()) {

						String e = s;
						if (e.contains("org.apache")) {
							e = e.substring(e.indexOf("org.apache"));
						}
						if (e.contains("javax.")) {
							e = e.substring(e.indexOf("javax"));
						}

						if (!bugs.contains(e)) {
							bugs.add(e);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return bugs;
	}

}
