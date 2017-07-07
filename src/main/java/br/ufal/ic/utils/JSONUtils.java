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

}
