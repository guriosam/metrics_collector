package br.ufal.ic.objects;

public class Bug {
	
	private String element;
	private double reported_id;
	private double creation_id;
	private double fix_id;
	
	
	
	public String getElement() {
		return element;
	}
	public void setElement(String element) {
		this.element = element;
	}
	public double getReported_id() {
		return reported_id;
	}
	public void setReported_id(double reported_id) {
		this.reported_id = reported_id;
	}
	public double getCreation_id() {
		return creation_id;
	}
	public void setCreation_id(double creation_id) {
		this.creation_id = creation_id;
	}
	public double getFix_id() {
		return fix_id;
	}
	public void setFix_id(double fix_id) {
		this.fix_id = fix_id;
	}
	
	
}
