package com.banking.revampinterface;

/**
 * Enum class to describe indices for the array of tokens in a command.
 * @author Patryk Dziedzic, Aveesh Patel
 */
public enum Command {
    COMMAND(0),
    ACCT(1),
    FNAME(2),
    LNAME(3),
    DOB(4),
    MONEY(5),
    CODE(6);

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