/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufal.ic.utils;

/**
 *
 * @author daniel
 */
public enum Parameters {

  
	 ELEMENT_POST("/element"),
	    ELEMENT_GET_BY_NAME("/element/name/"),   
	    ELEMENT_REMOVE("/element/"),
	    ELEMENT_GET_ALL("/element/all"),
	    METRIC_POST("/metric"),
	    METRIC_GET_BY_NAME("/metric/name/"),   
	    METRIC_REMOVE("/metric/"),
	    METRIC_GET_ALL("/metric/all");
	   
	    private String url;

	    private Parameters(String url) {
	        this.url = url;
	    }

	    public String getText() {
	        return this.url;
	    }
	    
}
