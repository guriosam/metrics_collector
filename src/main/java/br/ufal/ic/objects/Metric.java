package br.ufal.ic.objects;

public class Metric {
	
	private String kind;
	private String name;
	private String file;
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
	
	@Override
	public String toString() {
		return "Metric [kind=" + kind + ", name=" + name + ", file=" + file + "]";
	}
	
	
	

}
