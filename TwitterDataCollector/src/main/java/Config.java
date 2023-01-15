/*
 * Author: Ngo Bao(benjaminnNgo)
 * Last edited: 28th Dec 2022
 * Version: 1.0
 * Description: All configuration that customize the program can be make in here 
 * 
 */
public abstract class Config {
	/*
	 * This variable will define how many users that the program will fetch once the program run. The time the program run will depend on this variable, the higher it is,
	 * the longer the program take.
	 */
	public static final int USERS_NUMBER_PER_ONE_RUN = 20;
	/*
	 * These variable will define the name of file that contain the necessary data the program need to run.
	 */
	public static final String V1_USERS_IDS_LIST_FILE_NAME = "V1.txt";
	public static final String CONGRESS_MEMBERS_FILE_NAME = "handles.txt";
}
