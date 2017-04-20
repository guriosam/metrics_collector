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
public enum HttpCode {

    OK(200),
    OK_NO_CONTENT(204),
    UNAUTHORIZED(401),
    SERVER_ERROR(500),
    NOT_FOUND(404),
    CODE_NOT_FOUND(999);

    private int code;

    private HttpCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static HttpCode getRespondeByCod(int code) {

        for (HttpCode v : HttpCode.values()) {
            if (v.getCode() == code) {
                return v;
            }
        }

        return CODE_NOT_FOUND;
    }

//    public static HttpCode getRespondeByCod(int code) {
//
//        switch (code) {
//            case 200:
//                return OK;
//            case 401:
//                return UNAUTHORIZED;
//            case 500:
//                return SERVER_ERROR;
//            case 404:
//                return NOT_FOUND;
//        }
//
//        return CODE_NOT_FOUND;
//    }

}
