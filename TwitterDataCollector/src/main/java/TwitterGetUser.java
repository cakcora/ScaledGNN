import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

/*
 * Author: Ngo Bao(benjaminnNgo)
 * Last edited: 10th Jan 2022
 * Version: 2.0
 * Description: Description: Class provides necessary methods to get User and manipulate data
 * 
 */

public class TwitterGetUser extends TwitterUserAPI {
	private static String USER_FIELDS = "user.fields=description";
	private final String DATA_KEY = "data";
	
	
	
	private JSONObject dataObject;

	public JSONObject getUserByID(String id) throws ClientProtocolException, IOException, ParseException, LimitExcessError {
		JSONObject user = new JSONObject();
		pathVar = "/" + id;
		queryParam = "?" + USER_FIELDS;
		this.url = PROTOCOL + HOST + VERSION + RESOURCE + pathVar + queryParam;
		request = new HttpGet(url);
		responseString = excuteURL();
		
		dataObject = (JSONObject)createJSONObject();
		
		if(!dataObject.containsKey(DATA_KEY) && dataObject.get(STATUS_KEY).toString().equals(STATUS_CODES.EXCESS_LIMIT)) {
			throw new LimitExcessError();
		}
		if(dataObject.containsKey(DATA_KEY)) {
			user = (JSONObject)dataObject.get(DATA_KEY);
		}else {
			user = null;
		}
		

		return user;
	}

}
