package br.ufal.ic.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import br.ufal.ic.objects.Bug;
import br.ufal.ic.objects.Commit;
import br.ufal.ic.objects.Metric;

public class ReaderUtils {

	public static List<Commit> readCSV(String filename) {

		List<Commit> commits = new ArrayList<Commit>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filename));

			// read file line by line
			String line = reader.readLine();

			if (line.contains("project")) {
				line = reader.readLine();
			}

			while (line != null) {
				String[] info = line.split(",");

				Commit commit = new Commit();
				commit.setProjectName(info[0]);
				commit.setCommitOrder(Double.parseDouble(info[1]));
				commit.setCommitHash(info[2]);
				commits.add(commit);

				line = reader.readLine();
			}

			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

		return commits;
	}

	public static HashMap<String, Metric> getHashMetricsCSV(String path) {
		HashMap<String, Metric> metrics = new HashMap<String, Metric>();

		try {

			File f = new File(path + "metrics2.csv");
			if (!f.exists()) {
				// System.out.println("Not exists " + path);
				return new HashMap<>();
			}

			String csvSplitBy = "\"";
			BufferedReader reader = new BufferedReader(new FileReader(f));
			String line;
			String[] csvLine = null;

			while ((line = reader.readLine()) != null) {

				if (line.contains("AvgCyclomatic")) {
					continue;
				}

				csvLine = line.split(csvSplitBy); // use Quotes as separator
				csvLine[0] = csvLine[0].substring(0, csvLine[0].length() - 1);
				csvLine[2] = csvLine[2].substring(1, csvLine[2].length());
				String[] aux = csvLine[2].split(",", -1);

				if (csvLine[0].contains("Method") || csvLine[0].contains("Constructor")) {
					Metric m = new Metric();
					m.setKind(csvLine[0]);

					if (csvLine[1].contains("?")) {
						csvLine[1] = csvLine[1].replace("?", "");
					}

					if (csvLine[1].contains("<")) {
						csvLine[1] = csvLine[1].replace("<", "");
					}

					if (csvLine[1].contains(">")) {
						csvLine[1] = csvLine[1].replace(">", "");
					}

					m.setName(csvLine[1]);
					m.setFile(aux[0]);
					m.setAvgCyclomatic(aux[1]);
					m.setAvgCyclomaticModified(aux[2]);
					m.setAvgCyclomaticStrict(aux[3]);
					m.setAvgEssential(aux[4]);
					m.setAvgLine(aux[5]);
					m.setAvgLineBlank(aux[6]);
					m.setAvgLineCode(aux[7]);
					m.setAvgLineComment(aux[8]);
					m.setCountClassBase(aux[9]);
					m.setCountClassCoupled(aux[10]);
					m.setCountClassDerived(aux[11]);
					m.setCountDeclClass(aux[12]);
					m.setCountDeclClassMethod(aux[13]);
					m.setCountDeclClassVariable(aux[14]);
					m.setCountDeclFile(aux[15]);
					m.setCountDeclFunction(aux[16]);
					m.setCountDeclInstanceMethod(aux[17]);
					m.setCountDeclInstanceVariable(aux[18]);
					m.setCountDeclMethod(aux[19]);
					m.setCountDeclMethodAll(aux[20]);
					m.setCountDeclMethodDefault(aux[21]);
					m.setCountDeclMethodPrivate(aux[22]);
					m.setCountDeclMethodProtected(aux[23]);
					m.setCountDeclMethodPublic(aux[24]);
					m.setCountInput(aux[25]);
					m.setCountLine(aux[26]);
					m.setCountLineBlank(aux[27]);
					m.setCountLineCode(aux[28]);
					m.setCountLineCodeDecl(aux[29]);
					m.setCountLineCodeExe(aux[30]);
					m.setCountLineComment(aux[31]);
					m.setCountOutput(aux[32]);
					m.setCountPath(aux[33]);
					m.setCountSemicolon(aux[34]);
					m.setCountStmt(aux[35]);
					m.setCountStmtDecl(aux[36]);
					m.setCountStmtExe(aux[37]);
					m.setCyclomatic(aux[38]);
					m.setCyclomaticModified(aux[39]);
					m.setCyclomaticStrict(aux[40]);
					m.setEssential(aux[41]);
					m.setMaxCyclomatic(aux[42]);
					m.setMaxCyclomaticModified(aux[43]);
					m.setMaxCyclomaticStrict(aux[44]);
					m.setMaxEssential(aux[45]);
					m.setMaxInheritanceTree(aux[46]);
					m.setMaxNesting(aux[47]);
					m.setPercentLackOfCohesion(aux[48]);
					m.setRatioCommentToCode(aux[49]);
					m.setSumCyclomatic(aux[50]);
					m.setSumCyclomaticModified(aux[51]);
					m.setSumCyclomaticStrict(aux[52]);
					m.setSumEssential(aux[53]);

					// metrics.add(m);
					metrics.put(m.getName(), m);
				}

			}

			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return metrics;

	}
	
	public static ArrayList<Metric> getListMetricsCSV(String path, String commit) {
		ArrayList<Metric> metrics = new ArrayList<Metric>();

		try {

			File f = new File(path + "metrics.csv");

			if (!f.exists()) {
				return null;
			}

			CSVReader csvReader = new CSVReader(new FileReader(f));
			List<String[]> myEntries = csvReader.readAll();

			for (String[] info : myEntries) {
				if (info[3].contains("AvgCyclomatic")) {
					continue;
				}

				if (info[0].contains("Method") || info[0].contains("Constructor")) {
					Metric m = new Metric();
					m.setKind(info[0]);
					m.setName(info[1]);
					m.setFile(info[2]);
					m.setAvgCyclomatic(info[3]);
					m.setAvgCyclomaticModified(info[4]);
					m.setAvgCyclomaticStrict(info[5]);
					m.setAvgEssential(info[6]);
					m.setAvgLine(info[7]);
					m.setAvgLineBlank(info[8]);
					m.setAvgLineCode(info[9]);
					m.setAvgLineComment(info[10]);
					m.setCountClassBase(info[11]);
					m.setCountClassCoupled(info[12]);
					m.setCountClassDerived(info[13]);
					m.setCountDeclClass(info[14]);
					m.setCountDeclClassMethod(info[15]);
					m.setCountDeclClassVariable(info[16]);
					m.setCountDeclFile(info[17]);
					m.setCountDeclFunction(info[18]);
					m.setCountDeclInstanceMethod(info[19]);
					m.setCountDeclInstanceVariable(info[20]);
					m.setCountDeclMethod(info[21]);
					m.setCountDeclMethodAll(info[22]);
					m.setCountDeclMethodDefault(info[23]);
					m.setCountDeclMethodPrivate(info[24]);
					m.setCountDeclMethodProtected(info[25]);
					m.setCountDeclMethodPublic(info[26]);
					m.setCountInput(info[27]);
					m.setCountLine(info[28]);
					m.setCountLineBlank(info[29]);
					m.setCountLineCode(info[30]);
					m.setCountLineCodeDecl(info[31]);
					m.setCountLineCodeExe(info[32]);
					m.setCountLineComment(info[33]);
					m.setCountOutput(info[34]);
					m.setCountPath(info[35]);
					m.setCountSemicolon(info[36]);
					m.setCountStmt(info[37]);
					m.setCountStmtDecl(info[38]);
					m.setCountStmtExe(info[39]);
					m.setCyclomatic(info[40]);
					m.setCyclomaticModified(info[41]);
					m.setCyclomaticStrict(info[42]);
					m.setEssential(info[43]);
					m.setMaxCyclomatic(info[44]);
					m.setMaxCyclomaticModified(info[45]);
					m.setMaxCyclomaticStrict(info[46]);
					m.setMaxEssential(info[47]);
					m.setMaxInheritanceTree(info[48]);
					m.setMaxNesting(info[49]);
					m.setPercentLackOfCohesion(info[50]);
					m.setRatioCommentToCode(info[51]);
					m.setSumCyclomatic(info[52]);
					m.setSumCyclomaticModified(info[53]);
					m.setSumCyclomaticStrict(info[54]);
					m.setSumEssential(info[55]);
					m.setCommit(commit);

					metrics.add(m);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return metrics;

	}

}
