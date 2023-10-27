import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

// expecting errors in the methods below in areas relating to UUID and potentially ENUM, notice the first UUID is grabbed using fromString
// but afterwards I simply cast the result, this may not work.

public class DataLoader extends DataConstants {

	public static void main(String[] args) {
		UI ui = new UI();
		ui.runPrintData();
	}

	public static ArrayList<Task> getTasks() {
		UserList userList = UserList.getInstance();
		ArrayList<Task> tasks = new ArrayList<Task>();

		try {
			FileReader reader = new FileReader(TASK_FILE_NAME);
			JSONArray tasksJSON = (JSONArray) new JSONParser().parse(reader);

			if (tasksJSON != null) {
				for (int i = 0; i < tasksJSON.size(); i++) {
					JSONObject taskJSON = (JSONObject) tasksJSON.get(i);
					UUID id = UUID.fromString((String) taskJSON.get(TASK_ID));

					String taskName = (String) taskJSON.get(TASK_NAME);
					String content = (String) taskJSON.get(TASK_CONTENT);
					int priority = (int) (long) taskJSON.get(PRIORITY);
					int hoursToComplete = (int) (long) taskJSON.get(HOURS);
					UUID userID = UUID.fromString((String) taskJSON.get(TASK_USER_ID));
					int pointValue = (int) (long) taskJSON.get(POINT_VALUE);

					JSONObject logObject = (JSONObject) taskJSON.get(LOG);
					UUID logUserUUID = UUID.fromString((String) logObject.get(LOG_USER_ID));
					LogEnum logEnum = LogEnum.valueOf((String) logObject.get(LOG_ENUM));
					LocalDate logDate = LocalDate.parse((String) logObject.get(LOG_DATE));
					String logReason = (String) logObject.get(LOG_REASON);

					Log log = new Log(logDate, userList.getUserbyId(logUserUUID), logEnum, logReason);

					JSONArray commentsJSON = (JSONArray) taskJSON.get(COMMENTS);
					ArrayList<Comment> comments = new ArrayList<>();

					if (commentsJSON != null) {
						for (Object c : commentsJSON) {
							JSONObject toPass = (JSONObject) c;
							comments.add(parseComment(toPass));
						}
					}

					switch ((String) taskJSON.get(TASK_TYPE)) {
						// will need to add custom elements of these classes to the JSON and load them
						// here
						case "BugTask":
							tasks.add((Task) (new BugTask(id, taskName, content, priority, log, hoursToComplete, userID, comments,
									pointValue)));
							break;
						case "DesignTask":
							tasks.add((Task) (new DesignTask(id, taskName, content, priority, log, hoursToComplete, userID, comments,
									pointValue)));
							break;
						case "NewFeatureTask":
							tasks.add((Task) (new NewFeatureTask(id, taskName, content, priority, log, hoursToComplete, userID,
									comments, pointValue)));
							break;
						case "DocumentationTask":
							tasks.add((Task) (new NewFeatureTask(id, taskName, content, priority, log, hoursToComplete, userID,
									comments, pointValue)));
							break;
					}
				}
			}

			return tasks;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Comment parseComment(JSONObject commentJSON) {

		UUID commenterID = UUID.fromString((String) commentJSON.get(COMMENTER_ID));
		String content = (String) commentJSON.get(CONTENT);
		LocalDate date = LocalDate.parse((String) commentJSON.get(DATE));
		ArrayList<Comment> replies = new ArrayList<Comment>();
		JSONArray repliesJSON = (JSONArray) commentJSON.get(REPLIES);
		UserList userList = UserList.getInstance();

		if (repliesJSON != null) {
			for (int i = 0; i < repliesJSON.size(); i++) {
				JSONObject subcommentJson = (JSONObject) repliesJSON.get(i);
				Comment subcomment = parseComment(subcommentJson);
				replies.add(subcomment);
			}
		}

		return new Comment(userList.getUserbyId(commenterID), content, date, replies);
	}

	public static ArrayList<Project> getProjects() {
		UserList userList = UserList.getInstance();
		ArrayList<Project> projects = new ArrayList<Project>();
		ArrayList<Task> taskList = getTasks();

		try {
			FileReader reader = new FileReader(PROJECT_FILE_NAME);
			JSONArray projectsJSON = (JSONArray) new JSONParser().parse(reader);

			if (projectsJSON != null) {
				for (int i = 0; i < projectsJSON.size(); i++) {
					JSONObject projectJSON = (JSONObject) projectsJSON.get(i);
					UUID id = UUID.fromString((String) projectJSON.get(PROJECT_ID));
					String projectName = (String) projectJSON.get(PROJECT_NAME);

					JSONArray columnsJSON = (JSONArray) projectJSON.get(COLUMN_LIST);
					ArrayList<Column> columns = new ArrayList<>();

					for (Object column : columnsJSON) {
						JSONObject columnJSON = (JSONObject) column;
						LogEnum columnName = LogEnum.valueOf((String) columnJSON.get(COLUMN_NAME));
						JSONArray tasksIDsJSON = (JSONArray) projectJSON.get(TASK_IDS);
						ArrayList<Task> tasks = new ArrayList<>();
						if (tasksIDsJSON != null) {
							for (Object taskID : tasksIDsJSON) {
								JSONObject taskIDJSON = (JSONObject) taskID;
								UUID taskUuid = UUID.fromString((String) taskIDJSON.get(TASK_ID));
								if (!taskList.isEmpty()) {
									for (Task task : taskList) {
										if (task.getID() == taskUuid) {
											tasks.add(task);
											break;
										}
									}
								}
							}
						}

						columns.add(new Column(tasks, columnName));
					}

					JSONArray commentsJSON = (JSONArray) projectJSON.get(COMMENTS);
					ArrayList<Comment> comments = new ArrayList<>();

					if (commentsJSON != null) {
						for (Object c : commentsJSON) {
							JSONObject toPass = (JSONObject) c;
							comments.add(parseComment(toPass));
						}
					}

					JSONArray roleMapJSON = (JSONArray) projectJSON.get(ROLE_MAP);
					HashMap<UserRoleEnum, User> roleMap = new HashMap<UserRoleEnum, User>();

					if (roleMapJSON != null) {
						for (Object roleMapping : roleMapJSON) {
							JSONObject roleMappingJSON = (JSONObject) roleMapping;
							UserRoleEnum roleEnum = UserRoleEnum.valueOf((String) roleMappingJSON.get(ROLE));
							UUID userID = UUID.fromString((String) roleMappingJSON.get(ROLE_USER_ID));
							User user = userList.getUserbyId(userID);
							roleMap.put(roleEnum, user);
						}
					}

					projects.add((Project) (new Project(id, projectName, columns, comments, roleMap)));
				}
			}

			return projects;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ArrayList<User> getUsers() {
		ArrayList<User> users = new ArrayList<User>();

		try {
			FileReader reader = new FileReader(USER_FILE_NAME);
			JSONArray peopleJSON = (JSONArray) new JSONParser().parse(reader);

			if (peopleJSON != null) {
				for (int i = 0; i < peopleJSON.size(); i++) {
					JSONObject personJSON = (JSONObject) peopleJSON.get(i);
					UUID id = UUID.fromString((String) personJSON.get(USER_ID));
					String firstName = (String) personJSON.get(USER_FIRST_NAME);
					String lastName = (String) personJSON.get(USER_LAST_NAME);
					String email = (String) personJSON.get(USER_EMAIL);
					String userName = (String) personJSON.get(USER_USER_NAME);
					String password = ((String) personJSON.get(USER_PASSWORD));

					users.add(new User(id, firstName, lastName, email, userName, password));
				}
			}

			return users;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}