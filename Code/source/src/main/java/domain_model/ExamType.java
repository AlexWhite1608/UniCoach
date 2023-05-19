package domain_model;

public enum ExamType {
    PROJECT("Progetto"),
    ORAL_EXAMINATION("Esame orale"),
    WRITTEN_TEST("Esame scritto"),
    WRITTEN_AND_ORAL_TEST("Esame scritto e orale");

    ExamType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ExamType getExamTypeFromString(String input) {
        for (ExamType examType : ExamType.values()) {
            if (examType.getDisplayName().equalsIgnoreCase(input)) {
                return examType;
            }
        }
        return null;  // Ritorna null se la stringa non corrisponde a nessun elemento dell'enum
    }

    private final String displayName;
}
