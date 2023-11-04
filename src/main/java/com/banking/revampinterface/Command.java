package com.banking.revampinterface;

/**
 * Enum class to describe indices for the array of tokens in a command.
 * @author Patryk Dziedzic, Aveesh Patel
 */
public enum Command {
    ACCT(0),
    FNAME(1),
    LNAME(2),
    DOB(3),
    MONEY(4),
    CODE(5);

    private final int index;

    /**
     * Parameterized constructor for the command class which initializes the index variable
     * @param index the index to be initialized
     */
    Command(int index)
    {
        this.index = index;
    }

    /**
     * Returns the index of a given enum
     * @return the index of the given enum
     */
    public int getIndex()
    {
        return index;
    }
}