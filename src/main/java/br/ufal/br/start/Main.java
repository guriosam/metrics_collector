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
		
		//String projectName = "apache_tomcat";
		String projectName = "apache_derby";
		
		//Reader r = new Reader();
		
		//r.getOtherHash();

		MineData m = new MineData(projectName);
		m.mineData();
		//m.checkoutProject();

		Filter f = new Filter();
		
		//f.getReportedCommitOfMissingFiles();
		//f.filterMinedData("apache_derby");
		//f.filterMinedData("apache_tomcat");
		
		Collector c = new Collector();
		//c.setMap(map);
		//c.collectMetricsInProjects("apache_tomcat");
		//c.checkAllWasCollected("apache_tomcat");
		//c.checkAllWasCollected(projectName);
		
	}

}
