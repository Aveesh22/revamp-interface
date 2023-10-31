package com.banking.revampinterface;

/**
 * Enum class to describe indices for the array of tokens in an A command.
 * @author Aveesh Patel, Patryk Dziedzic
 */
public enum Campus
{
    NEW_BRUNSWICK (0),
    NEWARK (1),
    CAMDEN (2);

    private final int code;

    /**
     * Constructor for the command class which initializes the index variable
     * @param code the campus code to be initialized
     */
    Campus(int code) {
        this.code = code;
    }

    /**
     * Returns the index of a given enum
     * @return the index of the given enum
     */
    public int getCode()
    {
        return code;
    }
}
