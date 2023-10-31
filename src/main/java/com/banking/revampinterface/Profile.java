package com.banking.revampinterface;

/**
 * This class defines a Profile to be used for identification
 * on Accounts and all associated subclasses
 * @author Aveesh Patel, Patryk Dziedzic
 */
public class Profile implements Comparable<Profile>
{
    private String fname;
    private String lname;
    private Date dob;

    /**
     * Parameterized constructor to initialize a Profile instance
     * with a first name, last name, and date of birth
     * @param fname the first name for the Profile holder
     * @param lname the last name for the Profile holder
     * @param dob the date of birth of the Profile holder
     */
    public Profile(String fname, String lname, Date dob)
    {
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
    }

    /**
     * Compares two profiles and returns an integer value depicting the
     * result of the comparison between them on the basis of first name,
     * last name, and date of birth
     * @param profile the profile to be compared to
     * @return -1, 0, 1
     */
    @Override
    public int compareTo(Profile profile)
    {
        int lnameVal = lname.compareToIgnoreCase(profile.lname);
        int fnameVal = fname.compareToIgnoreCase(profile.fname);
        int dobVal = dob.compareTo(profile.dob);

        if (lnameVal > 0)
            return 1;
        else if (lnameVal < 0)
            return -1;
        else
        {
            if (fnameVal > 0)
                return 1;
            else if (fnameVal < 0)
                return -1;
            else {
                if (dobVal > 0)
                    return 1;
                else if (dobVal < 0)
                    return -1;
                else
                    return 0;
            }
        }
    }

    /**
     * Determines whether two profile holders are the same person or not
     * @param obj the Profile holder to check if they are the same holder
     * @return true or false depending on if the two profile holders are the same
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Profile) {
            Profile holder = (Profile) obj;
            return fname.equalsIgnoreCase(holder.getFname()) &&
                    lname.equalsIgnoreCase(holder.getLname()) &&
                    dob.equals(holder.getDob());
        }
        return false;
    }

    /**
     * Gets the first name of the Profile holder of the Account
     * @return a String depicting the holder's first name
     */
    protected String getFname()
    {
        return fname;
    }

    /**
     * Gets the last name of the Profile holder of the Account
     * @return a String depicting the holder's last name
     */
    protected String getLname()
    {
        return lname;
    }

    /**
     * Gets the date of the birth of the Profile holder of the Account
     * @return a Date depicting the date of birth of the Profile holder
     */
    protected Date getDob()
    {
        return dob;
    }

    /**
     * Overridden method which returns the textual representation of a Profile
     * @return the textual representation of a Profile
     */
    @Override
    public String toString()
    {
        return fname + " " + lname + " " + dob;
    }
}