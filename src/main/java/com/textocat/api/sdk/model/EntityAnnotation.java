package com.textocat.api.sdk.model;

/**
 * @author Nikita Zhiltsov
 */
public class EntityAnnotation {
    private final String span;

    private final int beginOffset;

    private final int endOffset;

    private final EntityAnnotationCategory category;

    public EntityAnnotation(String span, int beginOffset, int endOffset, EntityAnnotationCategory category) {
        this.span = span;
        this.beginOffset = beginOffset;
        this.endOffset = endOffset;
        this.category = category;
    }

    public String getSpan() {
        return span;
    }

    public int getBeginOffset() {
        return beginOffset;
    }

    public int getEndOffset() {
        return endOffset;
    }

    public EntityAnnotationCategory getCategory() {
        return category;
    }
}
