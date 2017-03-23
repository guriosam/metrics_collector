package br.ufal.br.operations;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.ufal.br.json.BugInfo;

public class Filter {

	public void filterMinedData() {
		Reader r = new Reader();

		List<String> mined = r.readSecundaryFile("C:/Users/Gurio/Desktop/minedData.txt");

		List<BugInfo> jsonBugs = r.readJSON("all_bugs.json");

		Set<String> set = new HashSet<String>();

		int count = 0;

		for (BugInfo bug : jsonBugs) {

			for (String element : bug.getElements()) {

				//String e = element.substring(0, element.lastIndexOf("."));
				//e = element.substring(e.lastIndexOf(".") + 1);

				// element = e;

				if (set.contains(element)) {
					System.out.println(element);
				}

				set.add(element);

			}

		}

		for (String s : set) {

			for (String m : mined) {
				if (m.contains(s + "=")) {
					// System.out.println(e);
					count++;
				}
			}
		}

		System.out.println(count);
	}

}
