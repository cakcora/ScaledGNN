import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
/*
 * Author: Ngo Bao(jimmNgo)
 * Last edited: 26th Dec 2022
 * Version: 1.1
 * Description: Parent class of get following class and get id class
 * 
 */
public class TwitterUserAPI {
	public final String PROTOCOL = "https:/";
	public final String HOST = "/api.twitter.com";
	public final String VERSION = "/2";
	public final String RESOURCE = "/users";
	public final String STATUS_KEY = "status";
	
	protected String queryParam;
	protected String pathVar;
	protected String url;
	protected CloseableHttpClient httpClient;
	protected HttpEntity entity;
	protected HttpGet request;
	protected CloseableHttpResponse response;
	protected String responseString ="";
	protected String statusCode;
	
	
	public TwitterUserAPI() {
		httpClient =  HttpClientBuilder.create().build();
	}
	
	public String excuteURL() throws ClientProtocolException, IOException {
		request.setHeader("Authorization", "Bearer " + APIToken.TWITTER_BEARER_TOKEN);
		request.setHeader("Cookie", APIToken.TWITTER_COOKIE);
		response = httpClient.execute(request);
		entity = response.getEntity();
		return EntityUtils.toString(entity);
	}
	
	public JSONObject createJSONObject() throws ParseException {
		JSONParser parser = new JSONParser();
		JSONObject dataObject =  (JSONObject)parser.parse(responseString);
		
		return dataObject;
	}
	
	
	

}
