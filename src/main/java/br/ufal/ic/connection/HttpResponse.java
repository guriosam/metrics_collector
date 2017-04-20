/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufal.ic.connection;

/**
 *
 * @author danie
 */
public class HttpResponse {

    private HttpCode code;
    private String message;

    public HttpResponse(HttpCode code, String message) {
        this.code = code;
        this.message = message;
    }

    public HttpCode getCode() {
        return this.code;
    }

    public String getMesage() {
        return this.message;
    }

}
