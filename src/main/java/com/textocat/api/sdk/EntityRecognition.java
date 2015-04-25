package com.textocat.api.sdk;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.textocat.api.sdk.model.AnnotatedBatch;
import com.textocat.api.sdk.model.Batch;
import com.textocat.api.sdk.model.BatchMetadata;

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
     * retrieves a batch with all annotated documents
     *
     * @param batchMetadata batch metadata
     * @param callback async callback to handle response
     */
    void retrieve(BatchMetadata batchMetadata, FutureCallback<AnnotatedBatch> callback);

    /**
     * matches and ranks documents in a batch with respect to the search query
     *
     * @param batchMetadata
     * @param searchQuery a search query
     * @param callback async callback to handle response
     * @see <a href="http://docs.textocat.com/filter-query-syntax.pdf">Textocat Search Query Syntax</a>
     */
    void search(BatchMetadata batchMetadata, String searchQuery, FutureCallback<AnnotatedBatch> callback);
}
