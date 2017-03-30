package br.ufal.br.operations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import br.ufal.br.json.BugInfo;
import br.ufal.ic.objects.Metric;

public class Collector {

	//private HashSet<String> map;

	public void collectMetricsInProjects() {
		Reader r = new Reader();

		List<String> listHashs = r.readSecundaryFile("hashs.txt");

		List<String> elementsToCollect = r.readSecundaryFile("elementsToGetMetrics.txt");

		List<BugInfo> bugs = new ArrayList<BugInfo>();

		double max = 0;
		// List<HashMap<String, List<Metric>>> metricas = new
		// ArrayList<HashMap<String, List<Metric>>>();
		int count = 0;

		for (String s : elementsToCollect) {
			BugInfo b = new BugInfo();

			String[] line = s.split("%");

			// if (line[0].contains("org.apache")) {
			// line[0] = line[0].substring(line[0].indexOf("org.apache"));
			// }

			b.setElement(line[0]);
			b.setOrder_init(Double.parseDouble(line[1]));
			b.setOrder_reported(Double.parseDouble(line[2]));

			if (b.getOrder_reported() > max) {
				max = b.getOrder_reported();
			}

			if (b.getOrder_init() > b.getOrder_reported()) {
				count++;
			} else {

				if (line[0].contains("org.apache")) {
					line[0] = line[0].substring(line[0].indexOf("org.apache"));
				}

				// System.out.println(line[0]);
				bugs.add(b);
			}

		}

		System.out.println(count);
		// System.out.println(bugs.size());

		for (BugInfo bs : bugs) {

			if (bs.getOrder_init() == 0.0) {
				// System.out.println(bs.getOrder_init() + "_" +
				// bs.getElement());
			}

		}

		if (count > 0) {
			// return;
		}

		HashMap<String, List<Metric>> output = new HashMap<String, List<Metric>>();

		for (int i = 0; i < listHashs.size(); i++) {
			System.out.println(i + "/" + max);

			if (i > max) {
				break;
			}

			String c = listHashs.get(i);
			HashMap<String, Metric> metrics = r.getMetricsCSV("C:/Users/Gurio/metrics/commit_" + c + "/");

			for (String s : metrics.keySet()) {
				System.out.println(s);
			}
			//checkBugs(bugs, metrics, output, i, c);

		}

		int aux = 0;
		for (String s : output.keySet()) {
			//System.out.println(aux + "/" + output.keySet().size() + "_" + s);
			//r.writeMetrics("timeline/" + s, output.get(s));
			//aux++;
		}
	}

	public void checkBugs(List<BugInfo> bugs, HashMap<String, Metric> metrics, HashMap<String, List<Metric>> output,
			int i, String c) {
		for (BugInfo b : bugs) {
			if (b.getOrder_init() <= i && i <= (b.getOrder_reported() + 1)) {

				/// String e = b.getElement().substring(0,
				/// b.getElement().lastIndexOf("."));
				// e = b.getElement().substring(e.lastIndexOf(".") + 1);
				// if (e.contains("(")) {
				// e = e.substring(0, e.lastIndexOf("("));
				// }

				String e = b.getElement().substring(b.getElement().indexOf("org.apache"));

				// System.out.println(metrics.keySet().size());
				// for (String s : metrics.keySet()) {
				// if (s.contains(e)) {

				if (metrics.containsKey(e)) {
					Metric m = metrics.get(e);
					if (output.containsKey(b.getElement())) {
						List<Metric> aux = output.get(b.getElement());
						m.setCommit(c);
						aux.add(m);
						output.put(b.getElement(), aux);
					} else {
						List<Metric> aux = new ArrayList<Metric>();
						m.setCommit(c);
						aux.add(m);
						output.put(b.getElement(), aux);
					}
				}
				// }

			}
		}
	}


}
