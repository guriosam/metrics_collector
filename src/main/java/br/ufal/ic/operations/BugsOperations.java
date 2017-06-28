package br.ufal.ic.operations;

import java.util.ArrayList;
import java.util.List;

import br.ufal.ic.json.BugInfo;
import br.ufal.ic.utils.IO;

public class BugsOperations {

	private int countRepetedElements;
	private double maxCommit;

	public List<BugInfo> assignBugList(String projectName) {

		countRepetedElements = 0;
		
		maxCommit = 0;


		List<BugInfo> bugs = new ArrayList<BugInfo>();

		List<String> elementsToCollect = IO.readAnyFile(projectName + "/elementsToGetMetrics_" + projectName + ".txt");

		for (String s : elementsToCollect) {
			BugInfo b = new BugInfo();

			String[] line = s.split("%");

			if (line[0].contains("org.apache")) {
				line[0] = line[0].substring(line[0].indexOf("org.apache"));
			} else if (line[0].contains("javax.")) {
				line[0] = line[0].substring(line[0].indexOf("javax"));
			}

			b.setElement(line[0]);
			b.setOrder_init(Double.parseDouble(line[1]));
			b.setOrder_reported(Double.parseDouble(line[2]));
			//b.setOrder_fixed(Double.parseDouble(line[3]));

			if (b.getOrder_reported() > maxCommit) {
				maxCommit = b.getOrder_reported();
			}

			if (b.getOrder_init() > b.getOrder_reported()) {
				countRepetedElements++;
			} else {
				bugs.add(b);
			}

		}

		return bugs;

	}

	public int getCountRepetedElements() {
		return countRepetedElements;
	}

	public void setCountRepetedElements(int countRepetedElements) {
		this.countRepetedElements = countRepetedElements;
	}

	public double getMaxCommit() {
		return maxCommit;
	}

	public void setMaxCommit(int maxCommit) {
		this.maxCommit = maxCommit;
	}

}
