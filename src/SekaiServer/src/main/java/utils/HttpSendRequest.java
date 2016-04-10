package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;



@SuppressWarnings("deprecation")
public class HttpSendRequest {
	
	public final static String USERAGENT = "Mozilla/5.0";
	
	public static String sendGET(String url) throws IOException {
		return sendGET(url,USERAGENT);
	}

	
	public static String sendGET(String url,String userAgent) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", userAgent);
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
 
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else {
        	System.out.println("GET request not worked");
        	return null;
        }
    }
	
	public static String sendPost(String url, List<NameValuePair> urlParameters) throws Exception {
		return sendPost(url,USERAGENT,urlParameters);
	}
	public static String sendPost(String url, JSONObject urlParameters) throws Exception {
		return sendPost(url,USERAGENT,urlParameters);
	}
	public static String sendPost(String url, String agent,JSONObject json) throws Exception {
		
		HttpPost post = new HttpPost(url);
		post.setHeader("User-Agent", agent);
		post.setEntity(new StringEntity(json.toString()));
		return sendPost(url,agent,post);
	}

	
	public static String sendPost(String url, String agent,List<NameValuePair> urlParameters) throws Exception {
			
		HttpPost post = new HttpPost(url);
		post.setHeader("User-Agent", agent);
		post.setEntity(new UrlEncodedFormEntity(urlParameters));
		return sendPost(url,agent,post);

	}
	public static String sendPost(String url, String agent,HttpPost post) throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(post);
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + post.getEntity());
		System.out.println("Response Code : " + 
	                                    response.getStatusLine().getStatusCode());

		BufferedReader rd = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));
		
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		return result.toString();
		
	}


}