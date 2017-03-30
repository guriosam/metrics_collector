package br.ufal.ic.objects;

public class Metric {
	
	private String commit;
	private String kind;
	private String name;
	private String file;
	private String allValues;
	/*
	 * TODO
	 * Insert all metrics here
	 */
	
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
