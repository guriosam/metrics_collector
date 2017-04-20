/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufal.ic.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import br.ufal.ic.model.IType;

/**
 *
 * @author danie_000
 */
public class Parse<E> {

    //Pega um modelo json e transforma em uma lista de obj para uma tabela
    public List<E> parseJsonToClass(String json, IType classe) {

        try {
            char type = json.charAt(0);

            Gson gson = new Gson();

            if (type == '[') {

//                Object obj = (Object) Class.forName(classe.getCanonicalName()).newInstance();
//                Type typeResult = (Type) obj.getClass().getMethod("getTableType").invoke(obj);                
                List<E> objectsList = gson.fromJson(json, classe.getType());

                return objectsList;
            } else {

                Object obj = gson.fromJson(json, classe.getType());

                List<E> lista = new ArrayList();
                lista.add((E) obj);

                return lista;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}