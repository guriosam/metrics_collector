/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufal.ic.connection;

/**
 *
 * @author danieltmo
 */
public class ConnectionException extends Exception {

    public ConnectionException(HttpCode response) {
        super(Integer.toString(response.getCode()));
    }

    public ConnectionException(String erro) {
        super(erro);
    }
}
