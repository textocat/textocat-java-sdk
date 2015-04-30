package com.textocat.api.sdk;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.textocat.api.sdk.model.AnnotatedBatch;
import com.textocat.api.sdk.model.Batch;
import com.textocat.api.sdk.model.BatchMetadata;
import com.textocat.api.sdk.model.SearchResult;

import java.util.Set;

/**
 * Entity recognition in Textocat API
 * <p>All calls are asynchronous. Add the handling logic to callbacks.</p>
 * @author Nikita Zhiltsov
 */
public interface EntityRecognition {
    /**
     * submits a batch
     *
     * @param batch batch of documents
     * @param callback async callback to handle response
     */
    void submit(Batch batch, FutureCallback<BatchMetadata> callback);

    /**
     * retrieves a set of batches with all annotated documents
     *
     * @param batchMetadata  metadata of batches to retrieve
     * @param callback async callback to handle response
     */
    void retrieve(FutureCallback<AnnotatedBatch> callback, BatchMetadata... batchMetadataSet);

    /**
     * matches and ranks the user's documents with respect to the search query
     *
     * @param searchQuery a search query
     * @param callback async callback to handle response
     * @see <a href="http://docs.textocat.com/search-query-syntax.pdf">Textocat Search Query Syntax</a>
     */
    void search(String searchQuery, FutureCallback<SearchResult> callback);
}
