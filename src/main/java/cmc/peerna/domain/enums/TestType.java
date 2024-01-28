package cmc.peerna.domain.enums;

public enum TestType {
    D,I,S,C;

    public static boolean isValidTestType(String testType) {
        for (TestType value : TestType.values()) {
            if (value.name().equals(testType)) {
                return true;
            }
        }
        return false;
    }
}
