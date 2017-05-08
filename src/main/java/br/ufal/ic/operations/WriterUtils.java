package br.ufal.ic.operations;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.List;

import br.ufal.ic.objects.Commit;
import br.ufal.ic.objects.Metric;

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

		String text = "Commit, Kind, Name, File, AvgCyclomatic, AvgCyclomaticModified,"
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
				+ "SumCyclomaticModified, SumCyclomaticStrict, SumEssential\n";

		for (Metric m : metrics) {
			text += m.getCommit();
			text += "," + m.getKind();
			text += "," + m.getName();
			text += "," + m.getFile();
			text += "," + m.getAvgCyclomatic();
			text += "," + m.getAvgCyclomaticModified();
			text += "," + m.getAvgCyclomaticStrict();
			text += "," + m.getAvgEssential();
			text += "," + m.getAvgLine();
			text += "," + m.getAvgLineBlank();
			text += "," + m.getAvgLineCode();
			text += "," + m.getAvgLineComment();
			text += "," + m.getCountClassBase();
			text += "," + m.getCountClassCoupled();
			text += "," + m.getCountClassDerived();
			text += "," + m.getCountDeclClass();
			text += "," + m.getCountDeclClassMethod();
			text += "," + m.getCountDeclClassVariable();
			text += "," + m.getCountDeclFile();
			text += "," + m.getCountDeclFunction();
			text += "," + m.getCountDeclInstanceMethod();
			text += "," + m.getCountDeclInstanceVariable();
			text += "," + m.getCountDeclMethod();
			text += "," + m.getCountDeclMethodAll();
			text += "," + m.getCountDeclMethodDefault();
			text += "," + m.getCountDeclMethodPrivate();
			text += "," + m.getCountDeclMethodProtected();
			text += "," + m.getCountDeclMethodPublic();
			text += "," + m.getCountInput();
			text += "," + m.getCountLine();
			text += "," + m.getCountLineBlank();
			text += "," + m.getCountLineCode();
			text += "," + m.getCountLineCodeDecl();
			text += "," + m.getCountLineCodeExe();
			text += "," + m.getCountLineComment();
			text += "," + m.getCountOutput();
			text += "," + m.getCountPath();
			text += "," + m.getCountSemicolon();
			text += "," + m.getCountStmt();
			text += "," + m.getCountStmtDecl();
			text += "," + m.getCountStmtExe();
			text += "," + m.getCyclomatic();
			text += "," + m.getCyclomaticModified();
			text += "," + m.getCyclomaticStrict();
			text += "," + m.getEssential();
			text += "," + m.getMaxCyclomatic();
			text += "," + m.getMaxCyclomaticModified();
			text += "," + m.getMaxCyclomaticStrict();
			text += "," + m.getMaxEssential();
			text += "," + m.getMaxInheritanceTree();
			text += "," + m.getMaxNesting();
			text += "," + m.getPercentLackOfCohesion();
			text += "," + m.getRatioCommentToCode();
			text += "," + m.getSumCyclomatic();
			text += "," + m.getSumCyclomaticModified();
			text += "," + m.getSumCyclomaticStrict();
			text += "," + m.getSumEssential() + "\n";

		}

		Writer wr = null;

		try {
			File f = new File(path + ".txt");
			wr = new FileWriter(f);
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

}
