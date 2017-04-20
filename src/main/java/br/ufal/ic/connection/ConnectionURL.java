/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufal.ic.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;

/**
 *
 * @author danie_000
 */
public class ConnectionURL {

    //public Class<?> classe;
    public ConnectionURL() {

    }

    public boolean pingHost(String host, int port, int timeout) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("localhost", port), timeout);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return false; // Either timeout or unreachable or failed DNS lookup.
        }
    }

    public HttpResponse getMethod(String urlString) throws ConnectionException {
        urlString = urlString.replace(" ", "%20");
        try {

            //Faz a conexão com o url correto
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            //connection.setRequestProperty("Authorization", "Bearer " + Constants.AUTHORIZATION);
            connection.setRequestProperty("charset", "utf-8");
            connection.connect();

            String response = "NO_RESPONSE";

            if (connection.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream()), "UTF-8"));
                response = br.readLine();
            }

            connection.disconnect();
            return new HttpResponse(HttpCode.getRespondeByCod(connection.getResponseCode()), response);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ConnectionException("Erro em evento desconhecido.");

        }

    }

    public HttpResponse postMethod(String urlString, String json) throws ConnectionException {
        
        urlString = urlString.replace(" ", "%20");
        //System.out.println(json);
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            //connection.setRequestProperty("Authorization", "Bearer " + Constants.AUTHORIZATION);
            connection.setRequestProperty("charset", "utf-8");
            connection.setUseCaches(false);

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            writer.write(json);
            writer.close();

            connection.connect();

            String response = "NO_RESPONSE";

            if (connection.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream()), "UTF-8"));
                response = br.readLine();
            }

            connection.disconnect();
            return new HttpResponse(HttpCode.getRespondeByCod(connection.getResponseCode()), response);

        } catch (java.io.IOException e) {
            e.printStackTrace();
            throw new ConnectionException("Erro em evento desconhecido.");

        }

    }

    public HttpResponse postCustomMethod(String urlString, String contentType, byte[] postDataBytes) throws ConnectionException {

        System.out.println(urlString);
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", contentType);
            connection.setRequestProperty("charset", "utf-8");
            connection.setUseCaches(false);
            connection.getOutputStream().write(postDataBytes);
            connection.connect();

            String response = "NO_RESPONSE";

            if (connection.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
                response = br.readLine();
            }
            connection.disconnect();
            return new HttpResponse(HttpCode.getRespondeByCod(connection.getResponseCode()), response);

        } catch (java.io.IOException e) {
            e.printStackTrace();
            throw new ConnectionException("Erro em evento no banco de dados.");

        }

    }

    public HttpResponse removeMethod(String urlString) throws ConnectionException {
        //System.out.println(urlString);

        try {

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            //connection.setRequestProperty("Authorization", "Bearer " + Constants.AUTHORIZATION);
            connection.setRequestProperty(
                    "Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("DELETE");
            connection.connect();

            String response = "NO_RESPONSE";

            if (connection.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
                response = br.readLine();
            }
            connection.disconnect();
            return new HttpResponse(HttpCode.getRespondeByCod(connection.getResponseCode()), response);

        } catch (java.io.IOException e) {
            e.printStackTrace();
            throw new ConnectionException("Erro em evento no banco de dados.");
        }

    }

}
