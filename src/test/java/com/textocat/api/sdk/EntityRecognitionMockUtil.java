package com.textocat.api.sdk;

import com.textocat.api.sdk.EntityHttpService;
import com.textocat.api.sdk.model.Batch;
import com.textocat.api.sdk.model.Document;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.json.JSONArray;

import java.util.Arrays;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

/**
 * @author Nikita Zhiltsov
 */
public class EntityRecognitionMockUtil {
    public static Document[] documents;

    static {
        documents = new Document[2];
        documents[0] = new Document("Председатель совета директоров ОАО «МДМ Банк» Олег Вьюгин — о том," +
                " чему приведет обмен санкциями между Россией и Западом в следующем году. Беседовала Светлана Сухова.");
        documents[1] = new Document("Не перепутает Генри Форда и компанию «Форд» в документах", "tag1");
    }

    private static JSONArray makeAnnotatedDocuments() {
        String source = "[\n" +
                "      {\n" +
                "        \"status\": \"SUCCESS\",\n" +
                "        \"entities\": [\n" +
                "          {\n" +
                "            \"span\": \"Олег Вьюгин\",\n" +
                "            \"beginOffset\": 46,\n" +
                "            \"endOffset\": 57,\n" +
                "            \"category\": \"PERSON\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"span\": \"Олег Вьюгин\",\n" +
                "            \"beginOffset\": 46,\n" +
                "            \"endOffset\": 57,\n" +
                "            \"category\": \"PERSON\"\n" +
                "          },\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"status\": \"SUCCESS\",\n" +
                "        \"tag\": \"tag1\",\n" +
                "        \"entities\": [\n" +
                "          {\n" +
                "            \"span\": \"Генри Форда\",\n" +
                "            \"beginOffset\": 11,\n" +
                "            \"endOffset\": 20,\n" +
                "            \"category\": \"PERSON\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"span\": \"компанию «Форд»\",\n" +
                "            \"beginOffset\": 28,\n" +
                "            \"endOffset\": 43,\n" +
                "            \"category\": \"ORGANIZATION\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "]";
        JSONArray documents = new JSONArray(source);
        return documents;
    }

    private static JSONArray makeAnnotatedDocumentForSearch() {
        String source = " [\n" +
                "      {\n" +
                "        \"status\": \"SUCCESS\",\n" +
                "        \"tag\": \"tag1\",\n" +
                "        \"entities\": [\n" +
                "          {\n" +
                "            \"span\": \"Генри Форда\",\n" +
                "            \"beginOffset\": 11,\n" +
                "            \"endOffset\": 20,\n" +
                "            \"category\": \"PERSON\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"span\": \"компанию «Форд»\",\n" +
                "            \"beginOffset\": 28,\n" +
                "            \"endOffset\": 43,\n" +
                "            \"category\": \"ORGANIZATION\"\n" +
                "          },\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "]";
        JSONArray documents = new JSONArray(source);
        return documents;
    }

    private static Matcher<Batch> hasNoDocuments() {
        return new TypeSafeMatcher<Batch>() {
            @Override
            protected boolean matchesSafely(Batch batch) {
                return batch.getDocuments().length == 0;
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }

    private static Matcher<Batch> areTheSameDocuments() {
        return new TypeSafeMatcher<Batch>() {
            @Override
            protected boolean matchesSafely(Batch batch) {
                Document[] batchDocuments = batch.getDocuments();
                return Arrays.deepEquals(batchDocuments, documents);
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }

    public static EntityHttpService getEntityHttpServiceMock() throws Exception {
        EntityHttpService entityHttpService = mock(EntityHttpService.class);
        doReturn("123").when(entityHttpService).queue(argThat(areTheSameDocuments()));
        doThrow(new RuntimeException("Documents cannot be empty")).when(entityHttpService).
                queue(argThat(hasNoDocuments()));
        doNothing().when(entityHttpService).waitUntilCompleted("123");
        when(entityHttpService.retrieve("123", null)).thenReturn(makeAnnotatedDocuments());
        when(entityHttpService.retrieve("123", "PERSON:форд")).thenReturn(makeAnnotatedDocumentForSearch());
        return entityHttpService;
    }
}
