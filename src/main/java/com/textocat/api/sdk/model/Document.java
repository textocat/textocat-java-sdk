package com.textocat.api.sdk.model;

import com.google.gson.Gson;

/**
 * @author Nikita Zhiltsov
 */
public class Document {
    /**
     * A text to annotate (required)
     */
    private final String text;
    /**
     * A user's custom field
     */
    private final String tag;

    public Document(String text) {
        this.text = text;
        this.tag = null;
    }

    public Document(String text, String tag) {
        this.text = text;
        this.tag = tag;
    }

    public String getText() {
        return text;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
