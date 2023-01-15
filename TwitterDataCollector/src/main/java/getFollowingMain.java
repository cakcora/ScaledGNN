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
 * Author: Ngo Bao(benjaminnNgo)
 * Last edited: 11th Jan 2022
 * Version: 2.0
 * Description: Main class that include algorithm to fetch API to get following list and store result as a .txt file
 * 
 */
public class getFollowingMain {

	private static final int LIMIT_TWITTER_REQUEST = 15;
	private static final long WAIT_TIME = 900000;
	private static final String USER_NAME_FIELD = "username";
	private static final String USER_BIO_FIELD = "description";
	private static final String ID_FIELD = "id";
	private static final String FOLLOWING_FIELD = "following";

	private static int request = 0;
	private static TwitterGetID getIDAPI = new TwitterGetID();
	private static TwitterGetFollowing getFollowingAPI = new TwitterGetFollowing();
	private static TwitterGetUser getUserAPI = new TwitterGetUser();
	


	public static void main(String[] args) {
		/*
		 * /Method to collect data for V1
		 */
//		getFollowingByUsername(Config.CONGRESS_MEMBERS_FILE_NAME); 
		/*
		 * /Method to collect data for V2
		 */
		getFollowingByID(Config.V1_USERS_IDS_LIST_FILE_NAME);
		
	}

	/*
	 * Main method to collect data for V2
	 */
	private static void getFollowingByID(String fileName)   {
		try {
			Scanner file = new Scanner(new File(fileName));
			while (file.hasNext()) {
				String id = file.nextLine();
				String nextToken = null;
				JSONObject user = getUserById(id);
				String userName = user.get(USER_NAME_FIELD).toString();
				
				if(user == null) {
					System.out.println("Can't find user has id: " + id);
					continue;
				}
				JSONArray following = getFollowing(id);
				
				if (following == null) {
					System.out.println("Something went wrong with user that has ID: " + id);
					continue;
				}
				nextToken = getFollowingAPI.getNextToken();
				while (nextToken != null) {
					following = getFollowing(id, nextToken);
					nextToken = getFollowingAPI.getNextToken();
				}
				JSONObject congressMember = createJSONObject(following, userName,id, user.get(USER_BIO_FIELD).toString());

				createDataFile(congressMember.toJSONString(), userName);
				System.out.println("Done " + userName);
				
					
			}
		}catch(FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Please make sure your your file name in Config file is correct and locate your file properly");
		}
		
		

	}

	/*
	 * Main method to collect data for V1
	 */
	private static void getFollowingByUsername(String fileName) {
		try {
			Queue<String> handles = getHandles(Config.USERS_NUMBER_PER_ONE_RUN,fileName);

			while (!handles.isEmpty()) {
				String userName = "";
				String id = "";
				String nextToken = null;

				userName = handles.remove();
				id = getID(userName);
				if (id == "" || id == null)
					continue;
				JSONArray following = getFollowing(id);

				if (following == null)
					continue;
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
			e.printStackTrace();
		} 

	}
	
	private static JSONObject getUserById(String id) {
		JSONObject user = null;
		
		try {
			user = getUserAPI.getUserByID(id);
		} catch (ClientProtocolException e) {
			System.out.println("Cannot get user: " + id);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Cannot get user: " + id);
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("Cannot get user: " + id);
			e.printStackTrace();
		} catch (LimitExcessError e) {
			sleep();
			user = getUserById(id);
		}
		return user;
	}

	private static JSONArray getFollowing(String id, String nextToken) {
		JSONArray following = null;

		try {
			following = getFollowingAPI.getFollowingByID(id, nextToken);
			request++;
		}  catch (ClientProtocolException e) {
			System.out.println("Cannot get following of user: " + id);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Cannot get following of user: " + id);
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("Cannot get following of user: " + id);
			e.printStackTrace();
		} catch (LimitExcessError e) {
			sleep();
			following = getFollowing(id, nextToken);
		}
		return following;
	}

	private static JSONArray getFollowing(String id) {
		JSONArray following = null;
		try {
			following = getFollowingAPI.getFollowingByID(id);
			request++;
		}  catch (ClientProtocolException e) {
			System.out.println("Cannot get following of user: " + id);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Cannot get following of user: " + id);
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("Cannot get following of user: " + id);
			e.printStackTrace();
		} catch (LimitExcessError e) {
			sleep();
			following = getFollowing(id);
		}
		return following;

	}

	private static String getID(String userName) {
		String id = null;

		if (userName != "" && userName != null) {
			try {
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
			} catch (LimitExcessError e) {
				sleep();
				id = getID(userName);
			} 
		}
		return id;
	}

	private static JSONObject createJSONObject(JSONArray following, String userName, String id) {
		JSONObject user = new JSONObject();
		
		user.put(USER_NAME_FIELD, userName);
		user.put(ID_FIELD, id);
		user.put(FOLLOWING_FIELD, following);
		return user;
	}
	
	private static JSONObject createJSONObject(JSONArray following, String userName, String id,String bio) {
		JSONObject user = new JSONObject();
		user.put(USER_BIO_FIELD, bio);
		user.put(USER_NAME_FIELD, userName);
		user.put(ID_FIELD, id);
		user.put(FOLLOWING_FIELD, following);
		return user;
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
			System.out.println("Cannot create file for " + userName);
		}

	}

	private static void sleep()   {
		System.out.println("Waiting...");
		try {
			Thread.sleep(WAIT_TIME);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("End wait");

	}

	public static Queue<String> getHandles(int handlesNum,String fileName) throws IOException {
		Queue<String> handles = new LinkedList<String>();
		Scanner file = new Scanner(new File(fileName));

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
