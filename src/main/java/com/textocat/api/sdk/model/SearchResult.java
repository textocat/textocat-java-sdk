package com.textocat.api.sdk.model;

/**
 * @author Nikita Zhiltsov
 */
public class SearchResult {
    private final String searchQuery;
    private final AnnotatedDocument[] documents;

    public SearchResult(String searchQuery, AnnotatedDocument[] documents) {
        this.searchQuery = searchQuery;
        this.documents = documents;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public AnnotatedDocument[] getDocuments() {
        return documents;
    }
}
