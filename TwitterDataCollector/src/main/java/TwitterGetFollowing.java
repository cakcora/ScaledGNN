import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
/*
 * Author: Ngo Bao(benjaminnNgo)
 * Last edited: 10th Jan 2022
 * Version: 2.0
 * Description: Class provides necessary methods to manipulate data from get following API
 * 
 */
public class TwitterGetFollowing extends TwitterUserAPI {
	private final static int MAX_RESULT = 1000;
	private final static String CUSTOM_FIELDS_MAX = "user.fields=description&max_results=" + MAX_RESULT;
	private final String FOLLOWING_KEY = "data";
	private final String META_KEY = "meta";
	private final String NEXT_TOKEN_KEY = "next_token";
	
	private String nextToken;
	private JSONArray following;
	private JSONObject dataObject;
	private JSONObject meta;
	
	public JSONArray getFollowingByID(String id) throws ClientProtocolException, IOException, ParseException, LimitExcessError {
		pathVar = "/" + id + "/following";
		queryParam = "?" + CUSTOM_FIELDS_MAX;
		this.url = PROTOCOL + HOST + VERSION + RESOURCE + pathVar + queryParam;
		request = new HttpGet(url);
		responseString = excuteURL();
		
		dataObject = (JSONObject)createJSONObject();
		
		if(!dataObject.containsKey(FOLLOWING_KEY) && dataObject.get(STATUS_KEY).toString().equals(STATUS_CODES.EXCESS_LIMIT)) {
			throw new LimitExcessError();
		}
		updateNextToken();
		following = (JSONArray)dataObject.get(FOLLOWING_KEY);
		return following;
	}

	public JSONArray getFollowingByID(String id, String paginationToken) throws ClientProtocolException, IOException, ParseException, LimitExcessError {
		pathVar = "/" + id + "/following";
		queryParam = "?" + CUSTOM_FIELDS_MAX + "&pagination_token=" + paginationToken;
		this.url = PROTOCOL + HOST + VERSION + RESOURCE + pathVar + queryParam;
		request = new HttpGet(url);
		responseString = excuteURL();
		dataObject = (JSONObject)createJSONObject();
		if(!dataObject.containsKey(FOLLOWING_KEY) && dataObject.get(STATUS_KEY).toString().equals(STATUS_CODES.EXCESS_LIMIT)) {
			throw new LimitExcessError();
		}
		updateNextToken();
		
		JSONArray nextFollowing = (JSONArray)dataObject.get(FOLLOWING_KEY);
		
		if(following == null) {
			following = (JSONArray)dataObject.get(FOLLOWING_KEY);
		}else {
			if(nextFollowing != null)following.addAll(nextFollowing);			
		}
		return following;
	}
	
	private void updateNextToken() {
		meta = (JSONObject)dataObject.get(META_KEY);
		if(meta == null) {
			nextToken = null;
		}else {
			nextToken = (String)meta.get(NEXT_TOKEN_KEY);
		}
		
	}
	
	public String getNextToken() {
		return nextToken;
	}
}
