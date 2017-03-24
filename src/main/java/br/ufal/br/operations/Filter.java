package br.ufal.br.operations;

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

		for (BugInfo b : jsonBugs) {

			for (String s : b.getElements()) {

				if (set.contains(s)) {
					for (String m : mined) {
						if (m.contains(s + "=")) {
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
										System.out.println(
												s + "," +  ms + "," + (int) b.getOrder_reported());
									}
								}
							}

							count++;
						}
					}
				}
			}
		}

		System.out.println(count);
	}

}
