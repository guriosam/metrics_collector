package br.ufal.ic.utils;

import java.util.ArrayList;
import java.util.List;

import br.ufal.ic.objects.Bug;

public class BugUtils {

	public static List<Bug> assignBugList(String projectName) {

		double maxCommit = 0;

		List<Bug> bugs = new ArrayList<Bug>();

		List<String> elementsToCollect = IOUtils.readAnyFile(projectName + "/elementsToGetMetrics.txt");

		List<String> files = FileUtils.filesOnFolder(projectName + "/validation_metrics2");
		
		for (String s : elementsToCollect) {

			Bug b = new Bug();

			String[] line = s.split("%");

			if (line[0].contains("org.apache")) {
				line[0] = line[0].substring(line[0].indexOf("org.apache"));
			} else if (line[0].contains("javax.")) {
				line[0] = line[0].substring(line[0].indexOf("javax"));
			}

			if (files.contains(line[0])) {
				//continue;
			}
			
			b.setElement(line[0]);
			b.setOrder_init(Double.parseDouble(line[1]));
			b.setOrder_reported(Double.parseDouble(line[2]));
			// b.setOrder_fixed(Double.parseDouble(line[3]));

			if (b.getOrder_reported() > maxCommit) {
				maxCommit = b.getOrder_reported();
			}

			if (b.getOrder_init() > b.getOrder_reported()) {
			} else {
				bugs.add(b);
			}

		}
		
		return bugs;

	}
}
