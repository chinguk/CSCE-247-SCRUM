public class SystemFACADE {
    public User currentUser;
    private User user;
    private Project project;

    public boolean login(String username, String password) {
        UserList userList = UserList.getInstance();
        currentUser = userList.checkUser(username, password);
        return currentUser != null;
    }

    public User logout(String username, String password) {
        User temp = new User("", "", "", "", "");
        return temp;
    }

    public boolean signUp(String firstName, String lastName, String email, String username, String password) {
        UserList userList = UserList.getInstance();

        // check if username is available
        if (userList.checkUsernameAvailability(username)) {
            // add user to user list
            userList.addUser(firstName, lastName, email, username, password);

            // save updated user list to data writer
            DataWriter.saveUsers();

            return true;
        }
        return false;

    }

    public ProjectList getProjects(User user) {
        return ProjectList.getInstance();
    }
}