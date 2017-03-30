package br.ufal.br.operations;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.ufal.br.json.BugInfo;

public class Filter {

	public void filterMinedData() {
		Reader r = new Reader();

		List<String> mined = r.readSecundaryFile("minedData.txt");

		List<BugInfo> jsonBugs = r.readJSON("all_bugs.json");

		Set<String> set = new HashSet<String>();

		int count = 0;

		for (BugInfo bug : jsonBugs) {

			for (String element : bug.getElements()) {

				// String e = element.substring(0, element.lastIndexOf("."));
				// e = element.substring(e.lastIndexOf(".") + 1);

				// element = e;

				if (set.contains(element)) {
					// System.out.println(element);
				} else {
					set.add(element);
				}

			}

		}

		HashSet<String> setElemeents = new HashSet<String>();

		for (BugInfo b : jsonBugs) {

			for (String s : b.getElements()) {

				if (set.contains(s)) {
					for (String m : mined) {

						String aux = m.substring(0, m.indexOf("="));

						String aux2 = s.substring(0, s.lastIndexOf(")"));

						if (aux.contains(aux2)) {
							// System.out.println(e);
							String[] metrics = m.split(",");

							boolean flag = false;
							int c = 0;
							for (String ms : metrics) {
								if (ms.contains("apache_derby")) {
									flag = true;
								} else {
									if (flag) {
										c++;
									}

									if (c == 2) {
										flag = false;
										c = 0;

										if (Double.valueOf(ms) > (int) b.getOrder_reported()) {
											break;
										}

										setElemeents
												.add(s + "%" + Double.valueOf(ms) + "%" + (int) b.getOrder_reported());

										// System.out.println(
										// s + "%" + Double.valueOf(ms) + "%" +
										// (int) b.getOrder_reported());
									}
								}
							}

							count++;
						}
					}
				}
			}
		}

		for (String s : setElemeents) {
			System.out.println(s);
		}

		System.out.println(setElemeents.size());
	}

	public void getReportedCommitOfMissingFiles() {
		Reader r = new Reader();
		List<String> missingFiles = r.readSecundaryFile("elementsMissingMetrics.txt");
		List<BugInfo> bugs = r.readJSON("all_bugs.json");

		for (String mf : missingFiles) {
			String[] mfs = mf.split("%");
			for (BugInfo b : bugs) {
				for (String e : b.getElements()) {
					if(e.contains(mfs[0])){
						System.out.println(mf + "%" + b.getOrder_reported());
						break;
					}
				}
			}
		}

	}

	public static List<String> filesOnFolder(String path) {

		List<String> fileNames = new ArrayList<String>();

		if (path == null) {
			System.out.println("Folder name is null");
		}

		try {
			// filesNamesPath = path;
			File f = new File(path);

			File[] files = f.listFiles();

			for (File file : files) {
				fileNames.add(file.getName().replace(".txt", ""));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return fileNames;
	}

}
