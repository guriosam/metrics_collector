package br.ufal.ic.operations;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.opencsv.CSVWriter;

import br.ufal.ic.json.BugInfo;
import br.ufal.ic.objects.Metric;
import br.ufal.ic.utils.IO;
import br.ufal.ic.utils.Paths;

public class MineData {

	private String projectName;

	public MineData(String projectName) {
		this.projectName = projectName;
	}

	public void mineData() {

		/*
		 * Retorna os dados do JSON Ex: { "project": "apache_derby", "bug_id":
		 * "5734", "order_reported": 6531, "order_fixed": 6545, "elements": [
		 * "java.testing.org.apache.derbyTesting.junit.CleanDatabaseTestSetup.setUp()"]
		 * }
		 */
		List<BugInfo> jsonBugs = ReaderUtils.writeBugJsonIntoBugList("all_bugs.json", projectName);

		List<String> listHashs = IO.readAnyFile("hashs_" + projectName + ".txt");

		HashMap<String, Integer> info = new HashMap<String, Integer>();

		for (BugInfo b : jsonBugs) {
			for (String element : b.getElements()) {
				String e = element;
				if (e.contains("org.apache")) {
					e = e.substring(e.indexOf("org.apache"));
				}
				if (e.contains("javax.")) {
					e = e.substring(e.indexOf("javax"));
				}

				if (info.containsKey(e)) {
					// System.out.println("Repetida: " + e);
				} else {
					info.put(e, null);
				}
			}

		}

		System.out.println(info.keySet().size());

		System.out.println("Start reading Metrics..");

		for (int i = 1455; i < listHashs.size(); i++) {
			String h = listHashs.get(i);
			System.out.println(i + "/" + listHashs.size());
			String cd = "cd C:/Users/gurio/Desktop/Pesquisa/Puc/Dados/apache_tomcat/metrics/commit_" + h;
			// HashMap<String, Metric> metrics = ReaderUtils.getHashMetricsCSV(
			// "C:/Users/gurio/Desktop/Pesquisa/Puc/Dados/" + projectName +
			// "/metrics/commit_" + c + "/");

			// for (String e : info.keySet()) {
			// if (metrics.containsKey(e)) {
			// if (info.get(e) == null) {
			// info.put(e, i);
			// System.out.println(e + "%" + i);
			// }
			// }
			//
			String output = cd + "\n";

			for (String e : info.keySet()) {

				String path = e;

				if (e.contains("<")) {
					path = e.substring(0, e.indexOf("<"));
				} else if (e.length() > 100) {
					path = e.substring(0, 100);
				} else if (e.contains(">")) {
					path = e.substring(0, e.indexOf(">"));
				}

				String command = "csvcut -c Kind,Name,File,AvgCyclomatic,AvgCyclomaticModified,AvgCyclomaticStrict,AvgEssential,AvgLine,AvgLineBlank,AvgLineCode,AvgLineComment,CountClassBase,CountClassCoupled,CountClassDerived,CountDeclClass,CountDeclClassMethod,CountDeclClassVariable,CountDeclFile,CountDeclFunction,CountDeclInstanceMethod,CountDeclInstanceVariable,CountDeclMethod,CountDeclMethodAll,CountDeclMethodDefault,CountDeclMethodPrivate,CountDeclMethodProtected,CountDeclMethodPublic,CountInput,CountLine,CountLineBlank,CountLineCode,CountLineCodeDecl,CountLineCodeExe,CountLineComment,CountOutput,CountPath,CountSemicolon,CountStmt,CountStmtDecl,CountStmtExe,Cyclomatic,CyclomaticModified,CyclomaticStrict,Essential,MaxCyclomatic,MaxCyclomaticModified,MaxCyclomaticStrict,MaxEssential,MaxInheritanceTree,MaxNesting,PercentLackOfCohesion,RatioCommentToCode,SumCyclomatic,SumCyclomaticModified,SumCyclomaticStrict,SumEssential"
						+ " metrics.csv " + "| csvgrep -c Name -m " + e + " | csvlook > " + "\"" + path + ".txt\"";

				output += command + "\n";
			}

			WriterUtils.writeMiningOutput("commands/mined_data_" + projectName + "_" + h + ".bat", output);

		}

		System.out.println("Finished reading Metrics..");

	}

	public void checkMinning() {

		List<String> files = Filter.filesOnFolder(Paths.PATH_DATA + projectName + "/timeline_init_reported/");

		for (String file : files) {

			String outputFile = "";
			List<String> fileData = IO.readCSVFileByCollumn(
					Paths.PATH_DATA + projectName + "/timeline_init_reported/" + file + ".txt", 0);

			List<String> hashs = IO.readAnyFile(projectName + "/hashs_by_element/" + file + ".txt");
			for (String hash : hashs) {
				boolean flag = true;
				for (String data : fileData) {
					if (data.contains(hash)) {
						outputFile += hash + "," + "true\n";
						flag = false;
						break;
					}
				}
				if (flag) {
					outputFile += hash + "," + "false\n";
				}

			}

			String outputPath = projectName + "/validation/";
			File outputDirectory = new File(outputPath);

			if (!outputDirectory.exists()) {
				if (outputDirectory.mkdir()) {
				}
			}

			WriterUtils.writeMiningOutput(outputPath + file + ".txt", outputFile);
		}
	}

}
