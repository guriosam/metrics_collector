package br.ufal.br.json;

import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

public class BugInfo {
	
	@SerializedName("project")
	private String project;
	@SerializedName("bug_id")
	private String bug_id;
	
	@SerializedName("order_reported")
	private double order_reported;
	
	@SerializedName("order_fixed")
	private double order_fixed;
	
	@SerializedName("elements")
	private List<String> elements;
	

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getBug_id() {
		return bug_id;
	}

	public void setBug_id(String bug_id) {
		this.bug_id = bug_id;
	}

	public double getOrder_reported() {
		return order_reported;
	}

	public void setOrder_reported(double order_reported) {
		this.order_reported = order_reported;
	}

	public double getOrder_fixed() {
		return order_fixed;
	}

	public void setOrder_fixed(double order_fixed) {
		this.order_fixed = order_fixed;
	}

	public List<String> getElements() {
		return elements;
	}

	public void setElements(List<String> elements) {
		this.elements = elements;
	}

	@Override
	public String toString() {
		return "Bug [project=" + project + ", bug_id=" + bug_id + ", order_reported=" + order_reported
				+ ", order_fixed=" + order_fixed + ", elements=" + elements + "]";
	}

	
	
	
}
