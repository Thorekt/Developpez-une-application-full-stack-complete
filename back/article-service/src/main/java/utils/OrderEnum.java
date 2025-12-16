package utils;

/**
 * Enumeration for ordering options.
 * 
 * @author Thorekt
 */
public enum OrderEnum {
    /**
     * Ascending order.
     */
    ASC,
    /**
     * Descending order.
     */
    DESC;

    /**
     * Checks if the order is ascending.
     * 
     * @return true if ascending, false otherwise
     */
    public boolean isAsc() {
        return this == ASC;
    }

    /**
     * Checks if the order is descending.
     * 
     * @return true if descending, false otherwise
     */
    public boolean isDesc() {
        return this == DESC;
    }
}
