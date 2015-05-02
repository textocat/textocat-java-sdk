package com.textocat.api.sdk.model;

/**
 * @author Nikita Zhiltsov
 */
public class SearchResult extends RecognitionResult {
    private final String searchQuery;

    public SearchResult(String searchQuery, AnnotatedDocument[] documents) {
        super(documents);
        this.searchQuery = searchQuery;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

}
