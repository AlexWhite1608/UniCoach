package manager;

public class Activity {

    public Activity() {}

    public Activity(Activity activity) {
        this.id = activity.getId();
        this.name = activity.getName();
        this.date = activity.getDate();
        this.endTime = activity.getEndTime();
        this.startTime = activity.getStartTime();
    }
    public Activity(String name, String date, int startTime, int endTime) {
        this.name = name;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    private String id;
    private String name;
    private String date;
    private int startTime;
    private int endTime;
}