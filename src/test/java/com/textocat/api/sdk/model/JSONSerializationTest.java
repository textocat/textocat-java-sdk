package com.textocat.api.sdk.model;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Nikita Zhiltsov
 */
public class JSONSerializationTest {
    @Test
    public void testDocumentToJSON() {
        Document document = new Document("Пример текста");
        String expected = "{\"text\":\"Пример текста\"}";
        Assert.assertEquals(expected, document.toString());
    }
    @Test
    public void testDocumentWithTagToJSON() {
        Document document = new Document("Пример текста", "tag1");
        String expected = "{\"text\":\"Пример текста\",\"tag\":\"tag1\"}";
        Assert.assertEquals(expected, document.toString());
    }

    @Test
    public void testBatchToJSON() {
        Document[] documents = new Document[2];
        documents[0] = new Document("Пример текста");
        documents[1] = new Document("Пример текста", "tag1");
        String expected = "[{\"text\":\"Пример текста\"},{\"text\":\"Пример текста\",\"tag\":\"tag1\"}]";
        Assert.assertEquals(expected, new Batch(documents).toString());
    }
}
