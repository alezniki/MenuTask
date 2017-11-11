package com.example.nikola.task.entities;

/**
 * Restaurant Item Data
 *
 * @author Nikola Aleksic
 */
public class RestaurantData {

    /**
     * Restaurant name
     */
    private String name;

    /**
     * Restaurant intro
     */
    private String intro;

    /**
     * Welcome message
     */
    private String message;

    /**
     * Is restaurant open
     */
    private boolean isOpen;

    /**
     * Constructor
     *
     * @param name    restaurant name
     * @param intro   restaurant intro
     * @param message welcome message
     * @param isOpen  is restaurant open
     */
    public RestaurantData(String name, String intro, String message, boolean isOpen) {
        this.name = name;
        this.intro = intro;
        this.message = message;
        this.isOpen = isOpen;
    }

    /**
     * Get restaurant name
     *
     * @return restaurant name
     */
    public String getName() {
        return name;
    }

    /**
     * Get restaurant intro
     *
     * @return restaurant intro
     */
    public String getIntro() {
        return intro;
    }

    /**
     * Get welcome message
     *
     * @return welcome message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Is restaurant open
     *
     * @return is open
     */
    public boolean isOpen() {
        return isOpen;
    }
}
