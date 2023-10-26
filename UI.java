import java.util.Scanner;

public class UI {
    private SystemFACADE systemFacade;

    public UI() {
        systemFacade = new SystemFACADE();
    }

    public void run() {
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Would you like to: ");
        System.out.println("1. Login");
        System.out.println("2. Sign up");
        int choice = keyboard.nextInt();
        keyboard.nextLine();
        switch (choice) {
            case 1:
                System.out.println("Enter your username");
                String username = keyboard.nextLine();
                System.out.println("Enter your password");
                String password = keyboard.nextLine();
                if (systemFacade.login(username, password)) {
                    System.out.println("Login successful");
                } else {
                    System.out.println("Login failed");
                }
                break;

            case 2:
                System.out.println("Enter your first name");
                String firstName = keyboard.nextLine();
                System.out.println("Enter your last name");
                String lastName = keyboard.nextLine();
                System.out.println("Enter your email");
                String email = keyboard.nextLine();
                System.out.println("Enter your username");
                String newUsername = keyboard.nextLine();
                System.out.println("Enter your password");
                String newPassword = keyboard.nextLine();
                switch (systemFacade.signUp(firstName, lastName, email, newUsername, newPassword)) {
                    case 0:
                        System.out.println("Sucessfully registered");
                        break;
                    case 1:
                        System.out.println(
                                "Password insufficient, please use at least one upper case, one lower case, and one digit.");
                        break;
                    case 2:
                        System.out.println("Username already exists, please choose a new Username.");
                        break;
                }
                break;
        }
        keyboard.close();
    }

    public void run2() {
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Would you like to make a project or view your projects?");
        System.out.println("1. Make 2. View");
        int projectChoice = keyboard.nextInt();
        if (projectChoice == 1) {
            // stuff to make a project
        } else if (projectChoice == 2) {
            // view projects
            // list projects
            ProjectList.viewProjects();

            System.out.println("Would you like to 1. add or 2. remove");
            int addRemove = keyboard.nextInt();
            if (addRemove == 1) {
                // add project
                System.out.println("Enter the name of the project you would like to add");
                String addName = keyboard.nextLine();
                ProjectList.addProject(addName);
            } else if (addRemove == 2) {
                // remove project
                System.out.println("Enter the name of the project you would like to remove");
                String removeName = keyboard.nextLine();
                ProjectList.removeProject(removeName);
            }

            System.out.println("Which project would you like to view?");
            // later change to match string
            System.out.println("1. Project 1, 2. Project 2");
            int projectView = keyboard.nextInt();

            if (projectView == 1) {
                // show columns
            } else if (projectView == 2) {
                // show columns
            } else {
                System.out.println("Please enter a valid choice");
            }

        } else {
            System.out.println("Please enter a valid choice");
        }
    }

    public void scenario() {

    }
}