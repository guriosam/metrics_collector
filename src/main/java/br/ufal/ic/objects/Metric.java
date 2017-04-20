package br.ufal.ic.objects;

public class Metric {

	private String commit;
	private String kind;
	private String name;
	private String file;
	private String allValues;
	
	/*
	 * Metrics
	 */
	private String avgCyclomatic;
	private String avgCyclomaticModified;
	private String avgCyclomaticStrict;
	private String avgEssential;
	private String avgLine;
	private String avgLineBlank;
	private String avgLineCode;
	private String avgLineComment;
	private String countClassBase;
	private String countClassCoupled;
	private String countClassDerived;
	private String countDeclClass;
	private String countDeclClassMethod;
	private String countDeclClassVariable;
	private String countDeclFile;
	private String countDeclFunction;
	private String countDeclInstanceMethod;
	private String countDeclInstanceVariable;
	private String countDeclMethod;
	private String countDeclMethodAll;
	private String countDeclMethodDefault;
	private String countDeclMethodPrivate;
	private String countDeclMethodProtected;
	private String countDeclMethodPublic;
	private String countInput;
	private String countLine;
	private String countLineBlank;
	private String countLineCode;
	private String countLineCodeDecl;
	private String countLineCodeExe;
	private String countLineComment;
	private String countOutput;
	private String countPath;
	private String countSemicolon;
	private String countStmt;
	private String countStmtDecl;
	private String countStmtExe;
	private String cyclomatic;
	private String cyclomaticModified;
	private String cyclomaticStrict;
	private String Essential;
	private String MaxCyclomatic;
	private String maxCyclomaticModified;
	private String MaxCyclomaticStrict;
	private String MaxEssential;
	private String MaxInheritanceTree;
	private String maxNesting;
	private String percentLackOfCohesion;
	private String RatioCommentToCode;
	private String SumCyclomatic;
	private String sumCyclomaticModified;
	private String SumCyclomaticStrict;
	private String sumEssential;


	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getAllValues() {
		return allValues;
	}

	public void setAllValues(String allValues) {
		this.allValues = allValues;
	}

	public String getCommit() {
		return commit;
	}

	public void setCommit(String commit) {
		this.commit = commit;
	}

	@Override
	public String toString() {
		return commit + "," + kind + "," + name + "," + file;
	}

}
