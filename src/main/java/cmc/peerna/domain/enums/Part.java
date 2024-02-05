package cmc.peerna.domain.enums;

public enum Part {
    PLANNER, DESIGNER, FRONT_END, BACK_END, MARKETER, OTHER,WITHDRAWAL;

    public static boolean isValidPart(String part) {
        for (Part value : Part.values()) {
            if (value.name().equals(part)) {
                return true;
            }
        }
        return false;
    }
}
