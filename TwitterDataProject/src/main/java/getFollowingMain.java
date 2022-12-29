import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import org.apache.http.client.ClientProtocolException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
/*
 * Author: Ngo Bao(jimmNgo)
 * Last edited: 28th Dec 2022
 * Version: 1.1
 * Description: -
 * 
 */
public class getFollowingMain {

	private static final int LIMIT_TWITTER_REQUEST = 15;
	private static final String USER_NAME_FIELD = "username";
	private static final String ID_FIELD = "id";
	private static final String FOLLOWING_FIELD = "following";

	private static int request = 0;
	private static TwitterGetID getIDAPI = new TwitterGetID();
	private static TwitterGetFollowing getFollowingAPI = new TwitterGetFollowing();

	public getFollowingMain() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		try {

			Queue<String> handles = getHandles(Config.USERS_NUMBER_PER_ONE_RUN);

			while (!handles.isEmpty()) {
				String userName = "";
				String id = "";
				String nextToken = null;

				checkLimit();
				userName = handles.remove();

				id = getID(userName);
				if (id == "" || id == null)continue;
				
				JSONArray following = getFollowing(id);
				if(following == null) continue;
		
				nextToken = getFollowingAPI.getNextToken();
				
				while (nextToken != null) {
					following = getFollowing(id, nextToken);
					nextToken = getFollowingAPI.getNextToken();
				}
				JSONObject congressMember = createJSONObject(following, userName, id);
				createDataFile(congressMember.toJSONString(), userName);

				System.out.println("Done " + userName);

			}
			System.out.println("Program ended!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}
	
	private static JSONArray getFollowing(String id, String nextToken) {
		JSONArray following = null;
		try {
			checkLimit();
			following = getFollowingAPI.getFollowingByID(id,nextToken);
			request++;
		} catch (InterruptedException e) {
			System.out.println("Cannot get following of user: " + id);
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			System.out.println("Cannot get following of user: " + id);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Cannot get following of user: " + id);
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("Cannot get following of user: " + id);
			e.printStackTrace();
		}
		return following;
	}
	
	private static JSONArray getFollowing(String id) {
		JSONArray following = null;
		try {
			checkLimit();
			following = getFollowingAPI.getFollowingByID(id);
			request++;
		} catch (InterruptedException e) {
			System.out.println("Cannot get following of user: " + id);
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			System.out.println("Cannot get following of user: " + id);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Cannot get following of user: " + id);
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("Cannot get following of user: " + id);
			e.printStackTrace();
		}
		return following;
		
	}

	private static String getID(String userName) {
		String id = null;
		if (userName != "" && userName != null) {
			try {
				checkLimit();
				id = getIDAPI.getIDByUserName(userName);
				request++;
			} catch (ClientProtocolException e) {
				System.out.println("Cannot get ID of " + userName);
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("Cannot get ID of " + userName);
				e.printStackTrace();
			} catch (ParseException e) {
				System.out.println("Cannot get ID of " + userName);
				e.printStackTrace();
			} catch (InterruptedException e) {
				System.out.println("Cannot get ID of " + userName);
				e.printStackTrace();
			}
		}
		return id;

	}

	private static JSONObject createJSONObject(JSONArray following, String userName, String id) {
		JSONObject congressMember = new JSONObject();
		congressMember.put(USER_NAME_FIELD, userName);
		congressMember.put(ID_FIELD, id);
		congressMember.put(FOLLOWING_FIELD, following);
		return congressMember;
	}

	private static void createDataFile(String JSONString, String userName) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(JSONString);
		String prettyJsonString = gson.toJson(je);
		FileWriter fw;
		try {
			fw = new FileWriter(userName + ".txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			pw.print(prettyJsonString);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Cannot create file for " + userName);
		}

	}

	private static void checkLimit() throws InterruptedException {
		if (request > LIMIT_TWITTER_REQUEST) {
			System.out.println("Waiting...");
			Thread.sleep(900000);
			System.out.println("End wait");
			request = 0;
		}
	}

	public static Queue<String> getHandles(int handlesNum) throws IOException {
		Queue<String> handles = new LinkedList<String>();
		Scanner file = new Scanner(new File("handles.txt"));

		for (int i = 0; i < handlesNum; i++) {
			if (!file.hasNextLine())
				break;
			handles.add(file.nextLine());
		}
		updateFile(file);
		return handles;
	}

	public static void updateFile(Scanner file) throws IOException {
		String updateFile = "handlesUpdate.txt";
		File previousFile = new File("handles.txt");
		File newFile = new File(updateFile);
		FileWriter fw = new FileWriter(updateFile, true);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter pw = new PrintWriter(bw);

		while (file.hasNextLine()) {
			pw.println(file.nextLine());
		}
		file.close();
		pw.flush();
		pw.close();
		previousFile.delete();
		newFile.renameTo(new File("handles.txt"));
	}

}
