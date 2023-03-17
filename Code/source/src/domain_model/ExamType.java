package domain_model;

public enum ExamType {
    PROJECT("Project"),
    ORAL_EXAMINATION("Oral examination"),
    WRITTEN_TEST("Written test"),
    WRITTEN_AND_ORAL_TEST("Written and oral test");

    private final String displayName;

    ExamType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
