package domain_model;

public enum ExamType {
    PROJECT("Progetto"),
    ORAL_EXAMINATION("Esame orale"),
    WRITTEN_TEST("Esame scritto"),
    WRITTEN_AND_ORAL_TEST("Esame scritto e orale");

    private final String displayName;

    ExamType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
