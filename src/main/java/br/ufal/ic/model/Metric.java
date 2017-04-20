package br.ufal.ic.model;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;



public class Metric implements IType{

   
    private Long id;
  
    private String commit;
    private String kind;
    private String name;
    private String file;

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
    private String essential;
    private String maxCyclomatic;
    private String maxCyclomaticModified;
    private String maxCyclomaticStrict;
    private String maxEssential;
    private String maxInheritanceTree;
    private String maxNesting;
    private String percentLackOfCohesion;
    private String ratioCommentToCode;
    private String sumCyclomatic;
    private String sumCyclomaticModified;
    private String sumCyclomaticStrict;
    private String sumEssential;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommit() {
        return commit;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }

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

    public String getAvgCyclomatic() {
        return avgCyclomatic;
    }

    public void setAvgCyclomatic(String avgCyclomatic) {
        this.avgCyclomatic = avgCyclomatic;
    }

    public String getAvgCyclomaticModified() {
        return avgCyclomaticModified;
    }

    public void setAvgCyclomaticModified(String avgCyclomaticModified) {
        this.avgCyclomaticModified = avgCyclomaticModified;
    }

    public String getAvgCyclomaticStrict() {
        return avgCyclomaticStrict;
    }

    public void setAvgCyclomaticStrict(String avgCyclomaticStrict) {
        this.avgCyclomaticStrict = avgCyclomaticStrict;
    }

    public String getAvgEssential() {
        return avgEssential;
    }

    public void setAvgEssential(String avgEssential) {
        this.avgEssential = avgEssential;
    }

    public String getAvgLine() {
        return avgLine;
    }

    public void setAvgLine(String avgLine) {
        this.avgLine = avgLine;
    }

    public String getAvgLineBlank() {
        return avgLineBlank;
    }

    public void setAvgLineBlank(String avgLineBlank) {
        this.avgLineBlank = avgLineBlank;
    }

    public String getAvgLineCode() {
        return avgLineCode;
    }

    public void setAvgLineCode(String avgLineCode) {
        this.avgLineCode = avgLineCode;
    }

    public String getAvgLineComment() {
        return avgLineComment;
    }

    public void setAvgLineComment(String avgLineComment) {
        this.avgLineComment = avgLineComment;
    }

    public String getCountClassBase() {
        return countClassBase;
    }

    public void setCountClassBase(String countClassBase) {
        this.countClassBase = countClassBase;
    }

    public String getCountClassCoupled() {
        return countClassCoupled;
    }

    public void setCountClassCoupled(String countClassCoupled) {
        this.countClassCoupled = countClassCoupled;
    }

    public String getCountClassDerived() {
        return countClassDerived;
    }

    public void setCountClassDerived(String countClassDerived) {
        this.countClassDerived = countClassDerived;
    }

    public String getCountDeclClass() {
        return countDeclClass;
    }

    public void setCountDeclClass(String countDeclClass) {
        this.countDeclClass = countDeclClass;
    }

    public String getCountDeclClassMethod() {
        return countDeclClassMethod;
    }

    public void setCountDeclClassMethod(String countDeclClassMethod) {
        this.countDeclClassMethod = countDeclClassMethod;
    }

    public String getCountDeclClassVariable() {
        return countDeclClassVariable;
    }

    public void setCountDeclClassVariable(String countDeclClassVariable) {
        this.countDeclClassVariable = countDeclClassVariable;
    }

    public String getCountDeclFile() {
        return countDeclFile;
    }

    public void setCountDeclFile(String countDeclFile) {
        this.countDeclFile = countDeclFile;
    }

    public String getCountDeclFunction() {
        return countDeclFunction;
    }

    public void setCountDeclFunction(String countDeclFunction) {
        this.countDeclFunction = countDeclFunction;
    }

    public String getCountDeclInstanceMethod() {
        return countDeclInstanceMethod;
    }

    public void setCountDeclInstanceMethod(String countDeclInstanceMethod) {
        this.countDeclInstanceMethod = countDeclInstanceMethod;
    }

    public String getCountDeclInstanceVariable() {
        return countDeclInstanceVariable;
    }

    public void setCountDeclInstanceVariable(String countDeclInstanceVariable) {
        this.countDeclInstanceVariable = countDeclInstanceVariable;
    }

    public String getCountDeclMethod() {
        return countDeclMethod;
    }

    public void setCountDeclMethod(String countDeclMethod) {
        this.countDeclMethod = countDeclMethod;
    }

    public String getCountDeclMethodAll() {
        return countDeclMethodAll;
    }

    public void setCountDeclMethodAll(String countDeclMethodAll) {
        this.countDeclMethodAll = countDeclMethodAll;
    }

    public String getCountDeclMethodDefault() {
        return countDeclMethodDefault;
    }

    public void setCountDeclMethodDefault(String countDeclMethodDefault) {
        this.countDeclMethodDefault = countDeclMethodDefault;
    }

    public String getCountDeclMethodPrivate() {
        return countDeclMethodPrivate;
    }

    public void setCountDeclMethodPrivate(String countDeclMethodPrivate) {
        this.countDeclMethodPrivate = countDeclMethodPrivate;
    }

    public String getCountDeclMethodProtected() {
        return countDeclMethodProtected;
    }

    public void setCountDeclMethodProtected(String countDeclMethodProtected) {
        this.countDeclMethodProtected = countDeclMethodProtected;
    }

    public String getCountDeclMethodPublic() {
        return countDeclMethodPublic;
    }

    public void setCountDeclMethodPublic(String countDeclMethodPublic) {
        this.countDeclMethodPublic = countDeclMethodPublic;
    }

    public String getCountInput() {
        return countInput;
    }

    public void setCountInput(String countInput) {
        this.countInput = countInput;
    }

    public String getCountLine() {
        return countLine;
    }

    public void setCountLine(String countLine) {
        this.countLine = countLine;
    }

    public String getCountLineBlank() {
        return countLineBlank;
    }

    public void setCountLineBlank(String countLineBlank) {
        this.countLineBlank = countLineBlank;
    }

    public String getCountLineCode() {
        return countLineCode;
    }

    public void setCountLineCode(String countLineCode) {
        this.countLineCode = countLineCode;
    }

    public String getCountLineCodeDecl() {
        return countLineCodeDecl;
    }

    public void setCountLineCodeDecl(String countLineCodeDecl) {
        this.countLineCodeDecl = countLineCodeDecl;
    }

    public String getCountLineCodeExe() {
        return countLineCodeExe;
    }

    public void setCountLineCodeExe(String countLineCodeExe) {
        this.countLineCodeExe = countLineCodeExe;
    }

    public String getCountLineComment() {
        return countLineComment;
    }

    public void setCountLineComment(String countLineComment) {
        this.countLineComment = countLineComment;
    }

    public String getCountOutput() {
        return countOutput;
    }

    public void setCountOutput(String countOutput) {
        this.countOutput = countOutput;
    }

    public String getCountPath() {
        return countPath;
    }

    public void setCountPath(String countPath) {
        this.countPath = countPath;
    }

    public String getCountSemicolon() {
        return countSemicolon;
    }

    public void setCountSemicolon(String countSemicolon) {
        this.countSemicolon = countSemicolon;
    }

    public String getCountStmt() {
        return countStmt;
    }

    public void setCountStmt(String countStmt) {
        this.countStmt = countStmt;
    }

    public String getCountStmtDecl() {
        return countStmtDecl;
    }

    public void setCountStmtDecl(String countStmtDecl) {
        this.countStmtDecl = countStmtDecl;
    }

    public String getCountStmtExe() {
        return countStmtExe;
    }

    public void setCountStmtExe(String countStmtExe) {
        this.countStmtExe = countStmtExe;
    }

    public String getCyclomatic() {
        return cyclomatic;
    }

    public void setCyclomatic(String cyclomatic) {
        this.cyclomatic = cyclomatic;
    }

    public String getCyclomaticModified() {
        return cyclomaticModified;
    }

    public void setCyclomaticModified(String cyclomaticModified) {
        this.cyclomaticModified = cyclomaticModified;
    }

    public String getCyclomaticStrict() {
        return cyclomaticStrict;
    }

    public void setCyclomaticStrict(String cyclomaticStrict) {
        this.cyclomaticStrict = cyclomaticStrict;
    }

    public String getEssential() {
        return essential;
    }

    public void setEssential(String essential) {
        this.essential = essential;
    }

  
    public String getMaxCyclomaticModified() {
        return maxCyclomaticModified;
    }

    public void setMaxCyclomaticModified(String maxCyclomaticModified) {
        this.maxCyclomaticModified = maxCyclomaticModified;
    }

 
    public String getMaxNesting() {
        return maxNesting;
    }

    public void setMaxNesting(String maxNesting) {
        this.maxNesting = maxNesting;
    }

    public String getPercentLackOfCohesion() {
        return percentLackOfCohesion;
    }

    public void setPercentLackOfCohesion(String percentLackOfCohesion) {
        this.percentLackOfCohesion = percentLackOfCohesion;
    }

 
    public String getSumCyclomaticModified() {
        return sumCyclomaticModified;
    }

    public void setSumCyclomaticModified(String sumCyclomaticModified) {
        this.sumCyclomaticModified = sumCyclomaticModified;
    }


    public String getMaxCyclomatic() {
		return maxCyclomatic;
	}

	public void setMaxCyclomatic(String maxCyclomatic) {
		this.maxCyclomatic = maxCyclomatic;
	}

	public String getMaxCyclomaticStrict() {
		return maxCyclomaticStrict;
	}

	public void setMaxCyclomaticStrict(String maxCyclomaticStrict) {
		this.maxCyclomaticStrict = maxCyclomaticStrict;
	}

	public String getMaxEssential() {
		return maxEssential;
	}

	public void setMaxEssential(String maxEssential) {
		this.maxEssential = maxEssential;
	}

	public String getMaxInheritanceTree() {
		return maxInheritanceTree;
	}

	public void setMaxInheritanceTree(String maxInheritanceTree) {
		this.maxInheritanceTree = maxInheritanceTree;
	}

	public String getRatioCommentToCode() {
		return ratioCommentToCode;
	}

	public void setRatioCommentToCode(String ratioCommentToCode) {
		this.ratioCommentToCode = ratioCommentToCode;
	}

	public String getSumCyclomatic() {
		return sumCyclomatic;
	}

	public void setSumCyclomatic(String sumCyclomatic) {
		this.sumCyclomatic = sumCyclomatic;
	}

	public String getSumCyclomaticStrict() {
		return sumCyclomaticStrict;
	}

	public void setSumCyclomaticStrict(String sumCyclomaticStrict) {
		this.sumCyclomaticStrict = sumCyclomaticStrict;
	}

	public String getSumEssential() {
        return sumEssential;
    }

    public void setSumEssential(String sumEssential) {
        this.sumEssential = sumEssential;
    }

    @Override
    public Type getType() {
        Type typeList = new TypeToken<List<Metric>>() {
        }.getType();

        return typeList;
    }

	@Override
	public String toString() {
		return "Metric [id=" + id + ", commit=" + commit + ", kind=" + kind + ", name=" + name + ", file=" + file
				+ ", avgCyclomatic=" + avgCyclomatic + ", avgCyclomaticModified=" + avgCyclomaticModified
				+ ", avgCyclomaticStrict=" + avgCyclomaticStrict + ", avgEssential=" + avgEssential + ", avgLine="
				+ avgLine + ", avgLineBlank=" + avgLineBlank + ", avgLineCode=" + avgLineCode + ", avgLineComment="
				+ avgLineComment + ", countClassBase=" + countClassBase + ", countClassCoupled=" + countClassCoupled
				+ ", countClassDerived=" + countClassDerived + ", countDeclClass=" + countDeclClass
				+ ", countDeclClassMethod=" + countDeclClassMethod + ", countDeclClassVariable="
				+ countDeclClassVariable + ", countDeclFile=" + countDeclFile + ", countDeclFunction="
				+ countDeclFunction + ", countDeclInstanceMethod=" + countDeclInstanceMethod
				+ ", countDeclInstanceVariable=" + countDeclInstanceVariable + ", countDeclMethod=" + countDeclMethod
				+ ", countDeclMethodAll=" + countDeclMethodAll + ", countDeclMethodDefault=" + countDeclMethodDefault
				+ ", countDeclMethodPrivate=" + countDeclMethodPrivate + ", countDeclMethodProtected="
				+ countDeclMethodProtected + ", countDeclMethodPublic=" + countDeclMethodPublic + ", countInput="
				+ countInput + ", countLine=" + countLine + ", countLineBlank=" + countLineBlank + ", countLineCode="
				+ countLineCode + ", countLineCodeDecl=" + countLineCodeDecl + ", countLineCodeExe=" + countLineCodeExe
				+ ", countLineComment=" + countLineComment + ", countOutput=" + countOutput + ", countPath=" + countPath
				+ ", countSemicolon=" + countSemicolon + ", countStmt=" + countStmt + ", countStmtDecl=" + countStmtDecl
				+ ", countStmtExe=" + countStmtExe + ", cyclomatic=" + cyclomatic + ", cyclomaticModified="
				+ cyclomaticModified + ", cyclomaticStrict=" + cyclomaticStrict + ", essential=" + essential
				+ ", maxCyclomatic=" + maxCyclomatic + ", maxCyclomaticModified=" + maxCyclomaticModified
				+ ", maxCyclomaticStrict=" + maxCyclomaticStrict + ", maxEssential=" + maxEssential
				+ ", maxInheritanceTree=" + maxInheritanceTree + ", maxNesting=" + maxNesting
				+ ", percentLackOfCohesion=" + percentLackOfCohesion + ", ratioCommentToCode=" + ratioCommentToCode
				+ ", sumCyclomatic=" + sumCyclomatic + ", sumCyclomaticModified=" + sumCyclomaticModified
				+ ", sumCyclomaticStrict=" + sumCyclomaticStrict + ", sumEssential=" + sumEssential + "]";
	}
    
    

    
}
