import java.util.ArrayList;
import java.util.UUID;

public class NewFeatureTask extends Task {
    public NewFeatureTask(UUID id, String name, String content, int priority, Log log, int hoursToComplete, UUID userID,
            ArrayList<Comment> comments, int pointValue) {
        super(id, name, content, priority, log, hoursToComplete, userID, comments, pointValue);
        // TODO Auto-generated constructor stub
    }

    private String justification;
    private String intention;
}
