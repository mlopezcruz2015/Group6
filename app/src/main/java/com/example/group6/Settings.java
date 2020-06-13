package com.example.group6;

import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * Manages the settings information for the game
 */
public class Settings
{
    private SharedPreferences prefs;        //Grab the saved SharePreferences
    private SharedPreferences.Editor editor;//Create prefs editor
    private final static String sharedPrefFile = "com.example.group6.ledsettings";

    /**
     * Constructor for settings
     * @param prefs SharedPreferences object to store and retrieve the preferences.
     * @param editor editor for the shared preferences.
     * @author Michael Lopez
     */
    public Settings(SharedPreferences prefs, SharedPreferences.Editor editor)
    {
        this.prefs = prefs;
        this.editor = editor;
    }


    /**
     * Retrieves the use custom pattern value stored in preferences.
     * @return boolean value for the saved preference for use custom pattern
     * @author Michael Lopez
     */
    public boolean getUseCustomPattern()
    {
        return prefs.getBoolean("custom_pattern_on", false);
    }

    /**
     * Updates the autosave preference
     * @param useCustomPattern boolean value for the use custom pattern preference to set.
     * @author Michael Lopez
     */
    public void setUseCustomPattern(boolean useCustomPattern)
    {
        editor.putBoolean("custom_pattern_on", useCustomPattern);
        editor.commit();
    }

    /**
     * Retrieves the sensitivity value stored in preferences.
     * @return int value for the saved preference for the sensitivity
     * @author Michael Lopez
     */
    public int getSensitivity()
    {
        return prefs.getInt("sensitivity",1);
    }

    /**
     * Updates the theme preference
     * @param sensitivity int value for the theme preference to set.
     * @author Michael Lopez
     */
    public void setSensitivity(int sensitivity)
    {
        editor.putInt("sensitivity", sensitivity);
        editor.apply();
    }

    public boolean[][] getLEDPattern()
    {
        Gson gson = new Gson();
        String json = prefs.getString("LEDPattern", "");

        boolean[][] pattern = gson.fromJson(json, boolean[][].class);
        return pattern;
    }

    public void setLEDPattern(boolean[][] pattern)
    {
        Gson gson = new Gson();
        String json = gson.toJson(pattern);
        editor.putString("LEDPattern", json);
        editor.commit();
    }
}
