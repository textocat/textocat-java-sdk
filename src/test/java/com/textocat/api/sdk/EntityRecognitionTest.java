package com.textocat.api.sdk;

import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.FutureCallback;
import com.textocat.api.sdk.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import static com.textocat.api.sdk.EntityRecognitionMockUtil.*;

/**
 * @author Nikita Zhiltsov
 */
public class EntityRecognitionTest {

    private EntityRecognition entityRecognition;

    @Before
    public void setup() throws Exception {
        EntityHttpService entityHttpService = getEntityHttpServiceMock();
        entityRecognition = new EntityRecognitionService(entityHttpService);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCorrectSubmit() throws InterruptedException {

        final CallbackResult callbackResult = new CallbackResult(false);
        FutureCallback<BatchMetadata> callback = new FutureCallback<BatchMetadata>() {
            public void onSuccess(BatchMetadata batchMetadata) {
                Assert.assertEquals(BatchStatus.FINISHED, batchMetadata.getStatus());
                Assert.assertEquals("123", batchMetadata.getId());
                callbackResult.passed = true;
            }

            public void onFailure(Throwable thrown) {
                Assert.fail("Failed to submit a test batch");
            }
        };
        entityRecognition.submit(new Batch(documents), callback);

        Thread.sleep(50);
        Assert.assertTrue("Failed to handle call while submitting", callbackResult.passed);
    }


    @Test
    public void testSubmitEmptyBatch() throws InterruptedException {
        Document[] documents = new Document[0];
        final CallbackResult callbackResult = new CallbackResult(true);
        FutureCallback<BatchMetadata> callback = new FutureCallback<BatchMetadata>() {
            public void onSuccess(BatchMetadata batchMetadata) {
                Assert.fail("Failed to raise an exception due to empty batch");
            }

            public void onFailure(Throwable thrown) {
                callbackResult.passed = false;
            }
        };
        entityRecognition.submit(new Batch(documents), callback);
        Thread.sleep(50);
        Assert.assertTrue("Failed to catch a failure while submitting", !callbackResult.passed);
    }

    @Test
    public void testRetrieve() throws Exception {
        final CallbackResult callbackResult = new CallbackResult(false);
        final FutureCallback<AnnotatedBatch> callback = new FutureCallback<AnnotatedBatch>() {

            public void onSuccess(AnnotatedBatch annotatedBatch) {
                AnnotatedDocument[] processedDocs = annotatedBatch.getDocuments();
                Assert.assertEquals(documents.length, processedDocs.length);
                Assert.assertEquals(null, processedDocs[0].getTag());
                Assert.assertEquals("tag1", processedDocs[1].getTag());
                EntityAnnotation[] entities = processedDocs[1].getEntities();
                Assert.assertEquals(2, entities.length);
                Assert.assertEquals("Генри Форда", entities[0].getSpan());
                Assert.assertEquals(28, entities[1].getBeginOffset());
                Assert.assertEquals(43, entities[1].getEndOffset());
                callbackResult.passed = true;
            }

            public void onFailure(Throwable throwable) {
                Assert.fail("Failed to retrieve the batch");
            }
        };
        entityRecognition.retrieve(callback, new BatchMetadata("123", BatchStatus.FINISHED));

        Thread.sleep(50);

        Assert.assertTrue("Failed to handle a call while retrieving", callbackResult.passed);
    }
    @Test
    public void testSearch() throws Exception {
        final CallbackResult callbackResult = new CallbackResult(false);
        final FutureCallback<SearchResult> callback = new FutureCallback<SearchResult>() {

            public void onSuccess(SearchResult searchResult) {
                AnnotatedDocument[] processedDocs = searchResult.getDocuments();
                Assert.assertEquals(1, processedDocs.length);
                Assert.assertEquals("tag1", processedDocs[0].getTag());
                EntityAnnotation[] entities = processedDocs[0].getEntities();
                Assert.assertEquals(2, entities.length);
                Assert.assertEquals("Генри Форда", entities[0].getSpan());
                Assert.assertEquals(28, entities[1].getBeginOffset());
                Assert.assertEquals(43, entities[1].getEndOffset());
                callbackResult.passed = true;
            }

            public void onFailure(Throwable throwable) {
                Assert.fail("Failed to retrieve the batch");
            }
        };
        entityRecognition.search("PERSON:форд", callback);
        Thread.sleep(50);

        Assert.assertTrue("Failed to handle a call while retrieving", callbackResult.passed);
    }

    static class CallbackResult {
        boolean passed;

        public CallbackResult(boolean passed) {
            this.passed = passed;
        }
    }
}
