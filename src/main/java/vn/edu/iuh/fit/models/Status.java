package vn.edu.iuh.fit.models;

public enum Status {
    ACTIVE(1),
    UNACTIVE(0),
    QUIT(-1);
    private int number;

    Status(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public static Status fromNumber(int number) {
        for (Status status : Status.values()) {
            if (status.getNumber() == number) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid AccountStatus number: " + number);
    }
}
