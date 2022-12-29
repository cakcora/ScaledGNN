import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.http.client.ClientProtocolException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class taskHandler {

	public taskHandler() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		TwitterGetID getIDAPI = new TwitterGetID();
		TwitterGetFollowing getFollowing = new TwitterGetFollowing();
		try {
			
			
			JSONArray following = getFollowing.getFollowingByID("2916086925");
			String next_token = getFollowing.getNextToken();
			System.out.println(next_token);
			following = getFollowing.getFollowingByID("2916086925",next_token);
			next_token = getFollowing.getNextToken();
			System.out.println(next_token);
			following = getFollowing.getFollowingByID("2916086925",next_token);
			System.out.println(getFollowing.getNextToken());
			JSONObject tempObject = new JSONObject();
			
			tempObject.put("following", following);
			tempObject.put("username", "RepAdams");
			tempObject.put("id", "2916086925");
			
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonParser jp = new JsonParser();
			JsonElement je = jp.parse(tempObject.toJSONString());
			String prettyJsonString = gson.toJson(je);
			FileWriter fw = new FileWriter("RepAdams.txt",true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			pw.print(prettyJsonString);
			pw.flush();
			pw.close();
//			System.out.println("---------------------");
//			String following = getFollowing.getFollowingByID("2916086925","S6K4FU3V4G1HEZZZ");
//			System.out.println(following);
		}catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		

	}

}
