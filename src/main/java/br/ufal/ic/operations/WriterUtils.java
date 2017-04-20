package br.ufal.ic.operations;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.List;

import br.ufal.ic.model.Metric;
import br.ufal.ic.objects.Commit;

public class WriterUtils {


	public static void writeMiningOutput(String path, String text) {
		// TODO Auto-generated method stub
		
		Writer wr = null;
		
		try {
			wr = new FileWriter(path);
			wr.write(text);
			wr.close();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (wr != null) {
					wr.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
	}

	public static void writeFileCommits(String path, List<Commit> commits) {

		String text = "project,commit_order,commit_hash,commit_old\n";
		for (Commit c : commits) {
			text += c.getProjectName() + "," + c.getCommitOrder() + "," + c.getCommitHash() + "," + c.getCommitOld()
					+ "\n";
		}

		Writer wr = null;

		try {
			wr = new FileWriter(path + ".txt");
			wr.write(text);
			wr.close();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (wr != null) {
					wr.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public static void writeMetrics(String path, List<Metric> metrics) {

		String text = "";
				/*"Commit, Kind, Name, File, AvgCyclomatic, AvgCyclomaticModified,"
				+ " AvgCyclomaticStrict, AvgEssential, AvgLine, AvgLineBlank, "
				+ "AvgLineCode, AvgLineComment, CountClassBase, CountClassCoupled, "
				+ "CountClassDerived, CountDeclClass, CountDeclClassMethod, "
				+ "CountDeclClassVariable, CountDeclFile, CountDeclFunction, "
				+ "CountDeclInstanceMethod, CountDeclInstanceVariable, "
				+ "CountDeclMethod, CountDeclMethodAll, CountDeclMethodDefault, "
				+ "CountDeclMethodPrivate, CountDeclMethodProtected, "
				+ "CountDeclMethodPublic, CountInput, CountLine, "
				+ "CountLineBlank, CountLineCode, CountLineCodeDecl, "
				+ "CountLineCodeExe, CountLineComment, CountOutput, CountPath, "
				+ "CountSemicolon, CountStmt, CountStmtDecl, CountStmtExe, Cyclomatic,"
				+ " CyclomaticModified, CyclomaticStrict, Essential, MaxCyclomatic, "
				+ "MaxCyclomaticModified, MaxCyclomaticStrict, MaxEssential, MaxInheritanceTree, "
				+ "MaxNesting, PercentLackOfCohesion, RatioCommentToCode, SumCyclomatic, "
				+ "SumCyclomaticModified, SumCyclomaticStrict, SumEssential\n";*/
		for (Metric m : metrics) {
			//text += m + "," + m.getAllValues() + "\n";
		}

		Writer wr = null;

		try {
			File f = new File(path + ".txt");
			if (f.exists()) {
				System.out.println(path);
			} else {
				wr = new FileWriter(path + ".txt", true);
				wr.write(text);
				wr.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (wr != null) {
					wr.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}



}
