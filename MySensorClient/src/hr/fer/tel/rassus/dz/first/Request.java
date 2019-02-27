package hr.fer.tel.rassus.dz.first;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author marinco
 */
public class Request {

	public static void main(String[] args) throws Exception {

		Request http = new Request();
		String username="marinko";

		System.out.println("\nTesting 1 - Send Http GET request");
		http.sendGet("http://localhost:8080/search");
		
		System.out.println("\nTesting 2 - Send Http POST request");
		http.sendPost("http://localhost:8080/sensors",username);

	}

	// HTTP GET request
	public void sendGet(String url) throws Exception {


		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		//con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());

	}
	
	// HTTP POST request
	public static void sendPost(String url, String username) throws Exception {

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		//con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		String urlParameters = "username="+username+"&latitude=22&longitude=23&IPaddress=localhost&port=1950";
		
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		//System.out.println("\nSending 'POST' request to URL : " + url);
		//System.out.println("Post parameters : " + urlParameters);
		//System.out.println("Response Code : " + responseCode);

		//print result
		
	}


}
