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
 * Author: Ngo Bao(jimmNgo)
 * Last edited: 27th Dec 2022
 * Version: 1.3
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
	
	public JSONArray getFollowingByID(String id) throws ClientProtocolException, IOException, ParseException {
		pathVar = "/" + id + "/following";
		queryParam = "?" + CUSTOM_FIELDS_MAX;
		this.url = PROTOCOL + HOST + VERSION + RESOURCE + pathVar + queryParam;
		request = new HttpGet(url);
		responseString = excuteURL();
		
		dataObject = (JSONObject)createJSONObject();
		updateNextToken();
		following = (JSONArray)dataObject.get(FOLLOWING_KEY);
		return following;
	}

	public JSONArray getFollowingByID(String id, String paginationToken) throws ClientProtocolException, IOException, ParseException {
		pathVar = "/" + id + "/following";
		queryParam = "?" + CUSTOM_FIELDS_MAX + "&pagination_token=" + paginationToken;
		this.url = PROTOCOL + HOST + VERSION + RESOURCE + pathVar + queryParam;
		request = new HttpGet(url);
		responseString = excuteURL();
		dataObject = (JSONObject)createJSONObject();
		updateNextToken();
		
		JSONArray nextFollowing = (JSONArray)dataObject.get(FOLLOWING_KEY);
		
		if(following == null) {
			following = (JSONArray)dataObject.get(FOLLOWING_KEY);
		}else {
			following.addAll(nextFollowing);
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
