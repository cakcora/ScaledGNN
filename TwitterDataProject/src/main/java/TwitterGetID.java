import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
/*
 * Author: Ngo Bao(jimmNgo)
 * Last edited: 25th Dec 2022
 * Version: 1.0
 * Description: Description: Class provides necessary methods to manipulate data from get ID of user API
 * 
 */
public class TwitterGetID extends TwitterUserAPI {
	
	public String getIDByUserName  (String userName) throws ClientProtocolException, IOException, ParseException {
		pathVar = "/by/username/"+ userName;
		queryParam = "?user.fields=description";
		this.url = PROTOCOL+HOST+VERSION+RESOURCE+pathVar + queryParam;
		request = new HttpGet(url);
		responseString = excuteURL();
		JSONObject dataObject = (JSONObject)createJSONObject().get("data");
		return (String)dataObject.get("id");
	}
	
}
