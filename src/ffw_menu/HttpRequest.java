package ffw_menu;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequest {

    private static HttpURLConnection con;
    private static String userAgent = "FFW Menu client";


    private static Api apiInstance = Api.getInstance();


    public static String get(String url, boolean useToken) {
        try {
            return req("GET", url, useToken, null);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static String post(String url, boolean useToken, byte[] postData) {
        try {
            return req("POST", url, useToken, postData);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private static String req(String httpVerb, String url, boolean useToken, byte[] postData) throws IOException {

//        HttpURLConnection con;

        try {
            URL myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();
//            con.setDoOutput(true);

            if (httpVerb.equals("GET") || httpVerb.equals("POST")) {
                con.setRequestMethod(httpVerb);
            } else {
                // throw unsupportedHttpVerbException
            }

            if (httpVerb.equals("POST")) {
                try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                    wr.write(postData);
                }
            }

            con.setRequestProperty("User-Agent", userAgent);

            if (useToken) {

                String token = apiInstance.getToken();

                if (token != null) {
                    con.setRequestProperty("token", token);
                } else {
                    // trow tokenNotAvailableException
                }
            }

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
            return response.toString();

        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
        finally {
//            con.disconnect();
        }
    }
}
