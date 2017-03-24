package br.ufal.br.operations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.ufal.br.json.BugInfo;
import br.ufal.ic.objects.Metric;

public class Collector {

	public void collectMetricsInProjects() {
		Reader r = new Reader();

		List<String> listHashs = r.readSecundaryFile("hashs.txt");

		List<String> elementsToCollect = r.readSecundaryFile("elementsToGetMetrics.txt");

		List<BugInfo> bugs = new ArrayList<BugInfo>();

		for (String s : elementsToCollect) {
			BugInfo b = new BugInfo();

			String[] line = s.split(",");
			b.setElement(line[0]);
			b.setOrder_init(Double.parseDouble(line[1]));
			b.setOrder_reported(Double.parseDouble(line[2]));

			bugs.add(b);
		}
		
		HashMap<String, List<Metric>> output = new HashMap<String, List<Metric>>();

		for (int i = 0; i < listHashs.size(); i++) {
			String c = listHashs.get(i);

			for (BugInfo b : bugs) {
				if (b.getOrder_init() <= i && i <= b.getOrder_reported()) {
					List<Metric> metrics = r.readMetricsCSV("C:/Users/Gurio/metrics/commit_" + c + "/");
					for (Metric m : metrics) {
						String e = b.getElement().substring(0, b.getElement().lastIndexOf("."));
						e = b.getElement().substring(e.lastIndexOf(".") + 1);

						if(m.getName().contains(e)){
							if(output.containsKey(b.getElement())){
								List<Metric> aux = output.get(b.getElement());
								aux.add(m);
								output.put(b.getElement(), aux);
							} else {
								List<Metric> aux = new ArrayList<Metric>();
								aux.add(m);
								output.put(b.getElement(), aux);
							}
						}

					}
				}
			}
		}

	}

}
