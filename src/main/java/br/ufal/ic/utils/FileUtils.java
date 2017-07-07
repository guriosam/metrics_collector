package br.ufal.ic.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

	public static boolean fileExists(File f, String filename) {
		if (!f.exists()) {
			System.out.println("File " + filename + " does not exists.");
			return false;
		}
		return true;
	}

	public static List<String> filesOnFolder(String path) {

		List<String> fileNames = new ArrayList<String>();

		if (path == null) {
			System.out.println("Folder name is null");
			return new ArrayList<>();
		}

		try {
			File f = new File(path);

			File[] files = f.listFiles();
			if (files != null) {
				for (File file : files) {

					String fileName = file.getName().replace(".txt", "");
					fileName = fileName.replace(".csv", "");

					fileNames.add(fileName);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return fileNames;
	}

	public static boolean isFolderEmpty(String path) {

		if (path == null) {
			System.out.println("Folder name is null");
		}

		try {

			File f = new File(path);

			if (f.list() == null) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	public static void addQuotesToCSV(String path) {
		List<String> files = filesOnFolder(path);

		int count = 0;
		for (String file : files) {

			System.out.println(count + "/" + files.size());

			List<String> lines = IOUtils.readAnyFile(path + file + ".txt");

			List<String> newLines = new ArrayList<String>();
			boolean flag = false;
			for (String line : lines) {
				if (line.contains("AvgCyclomatic")) {
					newLines.add(line);
					continue;
				}

				if (line.contains(",")) {
					int f = ordinalIndexOf(line, ",", 2);

					if (line.charAt(f + 1) != '\"') {
						flag = true;
						String c1 = line.charAt(f - 3) + "";
						char c2 = line.charAt(f - 2);
						char c3 = line.charAt(f - 1);
						char c4 = line.charAt(f);

						line = line.replaceFirst(c1 + c2 + c3 + c4 + "", c1 + c2 + c3 + ",\"");

						int e = line.indexOf(")");

						if (line.charAt(e + 1) != '\"') {
							line = line.replaceFirst("\\)" + "", "\\)\"");
						}

					} else {
						break;
					}
				}

				newLines.add(line);
			}

			if (flag) {
				IOUtils.writeAnyFile("timeline/" + file + ".txt", newLines);
			}

			count++;

		}

	}

	private static int ordinalIndexOf(String str, String substr, int n) {
		int pos = str.indexOf(substr);
		while (--n > 0 && pos != -1)
			pos = str.indexOf(substr, pos + 1);
		return pos;
	}

}
