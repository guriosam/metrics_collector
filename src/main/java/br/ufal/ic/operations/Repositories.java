package br.ufal.ic.operations;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import br.ufal.ic.objects.Commit;
import br.ufal.ic.utils.IOUtils;
import br.ufal.ic.utils.Paths;
import br.ufal.ic.utils.WriterUtils;
import br.ufal.ic.utils.FileUtils;

public class Repositories {

	private String projectName;

	public Repositories(String projectName) {
		this.projectName = projectName;
	}

	/*
	 * Steps 1. Collect the pair of hashs of commits, from the real database to
	 * the ufal database. 2. Collect all metrics.csv from all commits. 3. Create
	 * the filtered CSV file to help the minning.
	 */

	/*************************** STEP 1 *********************/
	/*
	 * I have a file like this:
	 * 
	 * *apache_tomcat,9573,8a91cfc654aa0203867877c9228dbf099ba6fb95
	 * *apache_tomcat,9574,11a80aa6daf2789cde5d64a4bfa3d2d204248fb0 *...
	 * 
	 * And another file like this:
	 * 
	 * *cc36278028f271d724ca621123742cf268947026;
	 * 8a91cfc654aa0203867877c9228dbf099ba6fb95
	 * *91fbe2889bd48b75fad045063b9a96b49522e798;
	 * 11a80aa6daf2789cde5d64a4bfa3d2d204248fb0 *...
	 *
	 * I basically need to get the third collumn of the first file and search
	 * for it on the second collumn the secondary file. When I found it, I get
	 * the first collumn of the secondary file and add the the output, together
	 * with the row of first file. Like this:
	 * 
	 * *apache_tomcat,9574,11a80aa6daf2789cde5d64a4bfa3d2d204248fb0,
	 * 91fbe2889bd48b75fad045063b9a96b49522e798
	 */
	public void getGitPairToRepositoryHash() {

		List<Commit> commit = new ArrayList<Commit>();
		commit = IOUtils.readCommitList("commits.csv");

		String path = Paths.PATH_WORKSPACE + projectName + "/repositoryCommits/";

		List<String> commitFiles = FileUtils.filesOnFolder(path);

		for (String commitFile : commitFiles) {
			List<String> repositoryCommitFile = new ArrayList<String>();
			repositoryCommitFile = IOUtils.readAnyFile(path + commitFile + ".txt");

			for (int i = 0; i < commit.size(); i++) {
				for (int j = 0; j < repositoryCommitFile.size(); j++) {
					String[] hashs = repositoryCommitFile.get(j).split(";");
					if (hashs[1].equals(commit.get(i).getCommitHash())) {
						commit.get(i).setCommitOld(hashs[0]);
					}
				}
			}
		}

		WriterUtils.writeFileCommits("commits_repo_table", commit);
	}

	public static void setGitHashToCommit(List<Commit> commitList, List<String> gitCommits) {
		for (int i = 0; i < commitList.size(); i++) {
			for (int j = 0; j < gitCommits.size(); j++) {
				String[] hashs = gitCommits.get(j).split(";");
				if (hashs[1].contains(commitList.get(i).getCommitHash())) {
					commitList.get(i).setCommitOld(hashs[0]);
				}
			}
		}
	}

	/************************ STEP 2 ***********************/

	public void checkoutProject() {

		String path = Paths.PATH_WORKSPACE + projectName + "/repositoryCommits/";
		List<String> files = FileUtils.filesOnFolder(path);

		String currentWorkspace = Paths.PATH_REPOSITORIES + projectName + "/";

		for (String file : files) {
			List<String> commits = IOUtils.readAnyFile(path + file);
			int count = 0;
			for (String c : commits) {
				String[] com = c.split(";");
				File outputDirectory = new File(Paths.PATH_DATA + projectName + "/metrics/");

				if (!outputDirectory.exists()) {
					if (outputDirectory.mkdir()) {
					}
				}

				try {

					/*
					 * TODO Find a way to give a cd command before running it.
					 */
					String cmd = "git checkout -f " + com[0];

					Runtime run = Runtime.getRuntime();

					Process pr = run.exec(cmd);

					BufferedReader stdInput = new BufferedReader(new InputStreamReader(pr.getInputStream()));

					BufferedReader stdError = new BufferedReader(new InputStreamReader(pr.getErrorStream()));

					String s = null;
					while ((s = stdInput.readLine()) != null) {
						System.out.println(s);
					}

					while ((s = stdError.readLine()) != null) {
						System.out.println(s);
					}

					/*
					 * if you are running this in some of the subfolders of
					 * Paths.PATH_REPOSITORIES + projectName directory leave the
					 * first parameter as a blank string
					 * 
					 * else, fill with the complete path to the subfolder.
					 */
					IOUtils.copyMetricsFile("", outputDirectory + "/commit_" + com[0] + "/", "metrics.csv");
					System.out.println(count + "/" + commits.size());
				} catch (Exception e) {
					e.printStackTrace();
				}

				count++;

			}

		}
	}

}
