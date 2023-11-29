package vn.edu.iuh.fit.models;
public enum Grant {
    DISABLE("0"),
    ENABLE("1");

    private final String value;

    Grant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Grant fromValue(String value) {
        for (Grant grant : Grant.values()) {
            if (grant.getValue().equals(value)) {
                return grant;
            }
        }
        throw new IllegalArgumentException("Invalid value for Grant: " + value);
    }
}