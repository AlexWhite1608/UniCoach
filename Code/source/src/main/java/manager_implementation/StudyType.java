package manager_implementation;

//TODO: aggiungere/togliere roba
public enum StudyType {
    LECTURE("Lezione"),
    LECTURE_REVIEW("Ripasso"),
    PROJECT("Progetto"),
    EXAM_STUDY("Studio per esame");

    private final String displayName;

    StudyType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
