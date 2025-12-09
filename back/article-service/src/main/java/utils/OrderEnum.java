package utils;

public enum OrderEnum {
    ASC,
    DESC;

    public boolean isAsc() {
        return this == ASC;
    }

    public boolean isDesc() {
        return this == DESC;
    }
}
