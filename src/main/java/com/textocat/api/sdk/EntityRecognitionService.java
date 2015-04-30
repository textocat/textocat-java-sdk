package com.textocat.api.sdk;

import com.google.common.util.concurrent.*;
import com.textocat.api.sdk.model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

/**
 * @author Nikita Zhiltsov
 */
class EntityRecognitionService implements EntityRecognition {
    ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
    /**
     * http request layer
     */
    private final EntityHttpService entityHttpService;


    public EntityRecognitionService(EntityHttpService entityHttpService) {
        this.entityHttpService = entityHttpService;
    }

    @Override
    public void submit(final Batch batch, FutureCallback<BatchMetadata> callback) {
        ListenableFuture<BatchMetadata> response = service.submit(new Callable<BatchMetadata>() {
            public BatchMetadata call() throws Exception {
                String batchId = entityHttpService.queue(batch);
                entityHttpService.waitUntilCompleted(batchId, getAdaptiveRequestInterval(batch.getDocuments().length));
                return new BatchMetadata(batchId, BatchStatus.FINISHED);
            }
        });
        Futures.addCallback(response, callback);
    }

    @Override
    public void retrieve(final Set<BatchMetadata> batchMetadataSet, FutureCallback<AnnotatedBatch> callback) {
        ListenableFuture<AnnotatedBatch> response = service.submit(new Callable<AnnotatedBatch>() {
            public AnnotatedBatch call() throws Exception {
                Set<String> batchIds = new HashSet<String>();
                for (BatchMetadata batch : batchMetadataSet) {
                    String batchId = batch.getId();
                    batchIds.add(batchId);
                }
                return convert(entityHttpService.retrieve(batchIds), batchIds);
            }
        });
        Futures.addCallback(response, callback);
    }

    @Override
    public void search(final String searchQuery, FutureCallback<SearchResult> callback) {
        ListenableFuture<SearchResult> response = service.submit(new Callable<SearchResult>() {
            public SearchResult call() throws Exception {
                return convert(entityHttpService.search(searchQuery), searchQuery);
            }
        });
        Futures.addCallback(response, callback);
    }

    /**
     * returns the request interval (in secs)
     *
     * @param batchSize
     * @return
     */
    private int getAdaptiveRequestInterval(int batchSize) {
        // TODO: plug the smart formula depending on the batch size and API throughput values here
        return 1;
    }

    private AnnotatedBatch convert(JSONArray documents, Set<String> batchIds) {
        AnnotatedDocument[] annotatedDocuments = getAnnotatedDocuments(documents);
        return new AnnotatedBatch(batchIds, annotatedDocuments);
    }

    private SearchResult convert(JSONArray documents, String searchQuery) {
        AnnotatedDocument[] annotatedDocuments = getAnnotatedDocuments(documents);
        return new SearchResult(searchQuery, annotatedDocuments);
    }

    private AnnotatedDocument[] getAnnotatedDocuments(JSONArray documents) {
        AnnotatedDocument[] annotatedDocuments = new AnnotatedDocument[documents.length()];
        for (int i = 0; i < documents.length(); i++) {
            JSONObject document = documents.getJSONObject(i);
            JSONArray entities = document.getJSONArray("entities");
            EntityAnnotation[] entityAnnotations = new EntityAnnotation[entities.length()];
            for (int j = 0; j < entities.length(); j++) {
                JSONObject entity = entities.getJSONObject(j);
                entityAnnotations[j] = new EntityAnnotation(entity.getString("span"),
                        entity.getInt("beginOffset"),
                        entity.getInt("endOffset"),
                        EntityAnnotationCategory.valueOf(entity.getString("category")));
            }
            annotatedDocuments[i] = new AnnotatedDocument(DocumentStatus.valueOf(document.getString("status")),
                    document.optString("tag", null), entityAnnotations);
        }
        return annotatedDocuments;
    }
}
