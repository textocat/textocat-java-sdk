package com.textocat.api.sdk;

import com.google.common.util.concurrent.*;
import com.textocat.api.sdk.model.*;
import org.json.JSONArray;
import org.json.JSONObject;

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
                entityHttpService.waitUntilCompleted(batchId);
                return new BatchMetadata(batchId, BatchStatus.FINISHED);
            }
        });
        Futures.addCallback(response, callback);
    }

    @Override
    public void retrieve(final BatchMetadata batchMetadata, FutureCallback<AnnotatedBatch> callback) {
        ListenableFuture<AnnotatedBatch> response = service.submit(new Callable<AnnotatedBatch>() {
            public AnnotatedBatch call() throws Exception {
                String batchId = batchMetadata.getId();
                return convert(entityHttpService.retrieve(batchId, null), batchId);
            }
        });
        Futures.addCallback(response, callback);
    }

    @Override
    public void search(final BatchMetadata batchMetadata, final String searchQuery,
                       FutureCallback<AnnotatedBatch> callback) {
        ListenableFuture<AnnotatedBatch> response = service.submit(new Callable<AnnotatedBatch>() {
            public AnnotatedBatch call() throws Exception {
                String batchId = batchMetadata.getId();
                return convert(entityHttpService.retrieve(batchId, searchQuery), batchId);
            }
        });
        Futures.addCallback(response, callback);
    }

    private AnnotatedBatch convert(JSONArray documents, String batchId) {
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
        return new AnnotatedBatch(batchId, BatchStatus.FINISHED, annotatedDocuments);
    }
}

enum Command {
    QUEUE, REQUEST, RETRIEVE;

    public String toString() {
        return super.toString().toLowerCase();
    }
}
