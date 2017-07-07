package br.ufal.ic.operations.catalog;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import br.ufal.ic.objects.Bug;
import br.ufal.ic.utils.IOUtils;
import br.ufal.ic.utils.JSONUtils;
import br.ufal.ic.utils.Paths;
import br.ufal.ic.utils.WriterUtils;
import br.ufal.ic.utils.FileUtils;

public class MineData {

	private String projectName;

	public MineData(String projectName) {
		this.projectName = projectName;
	}

	public void mineData() {

		List<Bug> jsonBugs = JSONUtils.writeBugJsonIntoBugList("all_bugs.json", projectName);

		List<String> listHashs = IOUtils.readAnyFile(projectName + "/hashs.txt");

		HashMap<String, Integer> info = new HashMap<String, Integer>();

		for (Bug bug : jsonBugs) {
			for (String element : bug.getElements()) {
				String e = element;

				// Tratando problema de namespace não bater
				if (e.contains("org.apache")) {
					e = e.substring(e.indexOf("org.apache"));
				}
				if (e.contains("javax.")) {
					e = e.substring(e.indexOf("javax"));
				}

				if (!info.containsKey(e)) {
					//Setando as key para saber quais os elementos que iremos trabalhar
					info.put(e, null);
				}
			}

		}

		System.out.println("Start reading Metrics..");

		for (int i = 0; i < listHashs.size(); i++) {
			String h = listHashs.get(i);
			System.out.println(i + "/" + listHashs.size());
			String cd = "cd " + Paths.PATH_DATA + projectName + "/metrics/commit_" + h;
			
			
			
		}

		System.out.println("Finished reading Metrics..");

	}


}
