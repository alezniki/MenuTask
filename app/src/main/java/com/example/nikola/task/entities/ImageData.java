package com.example.nikola.task.entities;

/**
 * Image Data Item
 *
 * @author Nikola Aleksic
 */
public class ImageData {

    /**
     * Image thumbnail url
     */
    private String url;

    /**
     * Constructor
     *
     * @param url thumbnail url
     */
    public ImageData(String url) {
        this.url = url;
    }

    /**
     * Get image url
     *
     * @return thumbnail url
     */
    public String getUrl() {
        return url;
    }
}
