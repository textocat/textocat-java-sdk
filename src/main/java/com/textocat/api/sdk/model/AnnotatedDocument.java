package com.textocat.api.sdk.model;

/**
 * @author Nikita Zhiltsov
 */
public class AnnotatedDocument {
    /**
     * the processing result
     */
    private final DocumentStatus status;
    /**
     * A user's custom tag, not null, if the user provides it while submitting
     */
    private final String tag;
    /**
     * entity recognition annotations
     */
    private final EntityAnnotation[] entities;

    public AnnotatedDocument(DocumentStatus status, String tag, EntityAnnotation[] entities) {
        this.status = status;
        this.tag = tag;
        this.entities = entities;
    }
    /**
     * the processing result
     */
    public DocumentStatus getStatus() {
        return status;
    }
    /**
     * A user's custom tag, not null, if the user provides it while submitting
     */
    public String getTag() {
        return tag;
    }
    /**
     * entity recognition annotations
     */
    public EntityAnnotation[] getEntities() {
        return entities;
    }
}
