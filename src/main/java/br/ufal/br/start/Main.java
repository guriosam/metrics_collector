package br.ufal.br.start;

import java.util.HashSet;
import java.util.Set;

import br.ufal.br.json.BugInfo;
import br.ufal.br.operations.Collector;
import br.ufal.br.operations.Filter;
import br.ufal.br.operations.MineData;
import br.ufal.br.operations.Reader;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		MineData m = new MineData();
		//m.mineData();

		Filter f = new Filter();
		//f.getReportedCommitOfMissingFiles();
		//f.filterMinedData();

		Reader r = new Reader();

		int count = 0;
		int countBugs = 0;
		HashSet<String> map = new HashSet();
		for (BugInfo b : r.readJSON("all_bugs.json")) {
			countBugs += b.getElements().size();
			for (String e : b.getElements()) {
				boolean flag = false;
				map.add(e);
				for (String s : f.filesOnFolder("timeline/")) {
					//System.out.println(e);
					e = e.substring(e.indexOf("org.apache"));

					
					if (s.contains(e)) {
						flag = true;
						//System.out.println(e);
						//System.out.println(s);
						count++;
						break;
					}
				}
				if (!flag) {
					//map.add(e);
			//		System.out.println(e);
				}
			}
		}
		
		 
		System.out.println(map.size());
		//System.out.println(count);
		System.out.println(countBugs);
		
		for(String s : map){
		//	System.out.println(s);
		}
		 
		Collector c = new Collector();
		//c.setMap(map);
		//sc.collectMetricsInProjects();
		
	}

}
