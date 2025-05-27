package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class Api{

    public static void main(String[] args) {
        String endpoint = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=40.748440,-73.984559&radius=500&type=restaurant&key=AIzaSyA6-T4EZ2TnGfLvIEma89qj7DncsKL4szE";
    }
    public static String getData(String endpoint) throws Exception {
    /*endpoint is a url (string) that you get from an API website*/
    URL url = new URL(endpoint);
    /*connect to the URL*/
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    /*creates a GET request to the API.. Asking the server to retrieve information for our program*/
    connection.setRequestMethod("GET");
    /* When you read data from the server, it wil be in bytes, the InputStreamReader will convert it to text. 
    The BufferedReader wraps the text in a buffer so we can read it line by line*/
    BufferedReader buff = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    String inputLine;//variable to store text, line by line
    /*A string builder is similar to a string object but faster for larger strings, 
    you can concatenate to it and build a larger string. Loop through the buffer 
    (read line by line). Add it to the stringbuilder */
    StringBuilder content = new StringBuilder();
    while ((inputLine = buff.readLine()) != null) {
        content.append(inputLine);
    }
    buff.close(); //close the bufferreader
    connection.disconnect(); //disconnect from server 
    return content.toString(); //return the content as a string
    }

}