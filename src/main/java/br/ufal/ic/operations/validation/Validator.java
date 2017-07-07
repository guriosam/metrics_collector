package br.ufal.ic.operations.validation;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.ufal.ic.objects.Bug;
import br.ufal.ic.utils.BugUtils;
import br.ufal.ic.utils.FileUtils;
import br.ufal.ic.utils.IOUtils;
import br.ufal.ic.utils.Paths;

public class Validator {

	public static void validateExistence(String projectName) {

		List<Bug> bugs = BugUtils.assignBugList(projectName);

		List<String> hashs = IOUtils.readAnyFile(projectName + "/hashs.txt");

		if (bugs.size() > 1) {
			HashMap<String, List<String>> output = new HashMap<>();

			for (int i = 0; i < hashs.size(); i++) {
				String h = hashs.get(i);

				System.out.println(i + "/" + hashs.size());

				List<String> lines = IOUtils
						.readAnyFile(Paths.PATH_DATA + projectName + "/metrics2/commit_" + h + "/metrics2.csv");

				for (Bug bug : bugs) {

					if (i < bug.getOrder_init()) {
						continue;
					}
					if (i >= bug.getOrder_reported()) {
						continue;
					}

					boolean flag = true;

					if (!output.containsKey(bug.getElement())) {
						output.put(bug.getElement(), new ArrayList<String>());
					}

					for (String line : lines) {

						if (line.contains(bug.getElement())) {

							flag = false;
							output.get(bug.getElement()).add(h + ",true");
						}
					}

					if (flag) {
						output.get(bug.getElement()).add(h + ",false");
					}

				}

			}

			String outputPath = projectName + "/validation_metrics2/";
			File outputDirectory = new File(outputPath);

			if (!outputDirectory.exists()) {
				if (outputDirectory.mkdir()) {
				}
			}

			System.out.println(output.keySet().size());
			for (String element : output.keySet()) {
				System.out.println(element);
				IOUtils.writeAnyFile(outputPath + element + ".csv", output.get(element));
			}

		}
		checkExistenceValidation(projectName);
	}

	private static void checkExistenceValidation(String projectName) {

		List<String> files = FileUtils.filesOnFolder(projectName + "/validation_metrics2/");

		HashMap<String, List<String>> output = new HashMap<>();

		HashMap<String, Integer> countLines = new HashMap<>();

		for (String file : files) {

			List<String> lines = IOUtils.readAnyFile(projectName + "/validation_metrics2/" + file + ".csv");
			for (String line : lines) {
				if (line.contains("false")) {
					if (!output.containsKey(file)) {
						output.put(file, new ArrayList<String>());
					}
					output.get(file).add(line.substring(0, line.indexOf(",")));
				}
			}

			countLines.put(file, lines.size());

		}

		List<String> out = new ArrayList<>();
		for (String k : output.keySet()) {

			String t = output.get(k).size() + "/" + countLines.get(k) + ",\"[";
			for (String s : output.get(k)) {
				t += s + ",";
			}

			t = t.substring(0, t.length() - 1);
			t += "]\"";

			out.add("\"" + k + "\"," + t);
		}

		IOUtils.writeAnyFile(projectName + "/validation.csv", out);
	}

}
