package br.ufal.ic.objects;

public class Commit {

	private String projectName;
	private double commitOrder;
	private String commitHash;
	
	
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public double getCommitOrder() {
		return commitOrder;
	}
	public void setCommitOrder(double commitOrder) {
		this.commitOrder = commitOrder;
	}
	public String getCommitHash() {
		return commitHash;
	}
	public void setCommitHash(String commitHash) {
		this.commitHash = commitHash;
	}
	
	
	
}
