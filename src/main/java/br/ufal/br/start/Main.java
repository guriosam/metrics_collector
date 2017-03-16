package br.ufal.br.start;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import br.ufal.br.operations.Reader;
import br.ufal.ic.objects.Commit;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Reader jr = new Reader();

		/*
		 * Retorna os dados do JSON
		 * Ex:
		 *  {
		 *    "project": "apache_derby",
		 *    "bug_id": "5734",
		 *    "order_reported": 6531,
		 *    "order_fixed": 6545,
		 *     "elements": [ "java.testing.org.apache.derbyTesting.junit.CleanDatabaseTestSetup.setUp()"]
		 *  }
		 */
		// List<BugInfo> jsonBugs = jr.readJSON("all_bugs.json");

		/*
		 * Read commits file
		 */

		/*
		 * Retorna Lista de Commmits
		 * private String projectName;
		 * private double commitOrder;
		 * private String commitHash;
		 */
		//List<Commit> commits = jr.readCSV("commits.csv");

		//for (Commit c : commits) {
			//System.out.println("cd dengue_results_repo");
			//System.out.println("git checkout " + c.getCommitHash());
			//String cmd = "git clone https://github.com/guriosam/aluno-03";
			try {
				//Runtime run = Runtime.getRuntime();
			
				//Process pr = run.exec(cmd);
				//pr.waitFor();
				
				jr.readMetricsCSV("");
				/*System.out.println("cd ..");

				System.out.println("cd ufal1_results_repo");
				System.out.println("git checkout " + c.getCommitHash());
				System.out.println("cd ..");

				System.out.println("cd ufal2_results_repo");
				System.out.println("git checkout " + c.getCommitHash());
				System.out.println("cd ..");

				System.out.println("cd ufal3_results_repo");
				System.out.println("git checkout " + c.getCommitHash());
				System.out.println("cd ..");

				System.out.println("cd ufal4_results_repo");
				System.out.println("git checkout " + c.getCommitHash());
				System.out.println("cd ..");

				System.out.println("cd ufal5_results_repo");
				System.out.println("git checkout " + c.getCommitHash());
				System.out.println("cd ..");*/
				
				
			} catch (Exception e) {
					e.printStackTrace();
			}
			/*
			 * Check in metrics.csv if exists element if yes, save it, remove
			 * element from the list else, continue
			 */
			

	//}

	}

}
