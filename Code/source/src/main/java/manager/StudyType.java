package manager;

public enum StudyType {
    LECTURE("Lezione"),
    LECTURE_REVIEW("Ripasso"),
    PROJECT("Progetto"),
    EXAM_STUDY("Studio per esame");

    StudyType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static StudyType getStudyTypeFromString(String input) {
        for (StudyType studyType : StudyType.values()) {
            if (studyType.getDisplayName().equalsIgnoreCase(input)) {
                return studyType;
            }
        }
        return null;  // Ritorna null se la stringa non corrisponde a nessun elemento dell'enum
    }

    private final String displayName;
}