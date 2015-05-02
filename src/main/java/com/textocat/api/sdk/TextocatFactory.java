package com.textocat.api.sdk;

/**
 * @author Nikita Zhiltsov
 */
public class TextocatFactory {
    private static EntityRecognition entityRecognition;

    private TextocatFactory() {
    }

    public static EntityRecognition getEntityRecognitionInstance(String authToken) {
        if (entityRecognition == null) {
            entityRecognition = new EntityRecognitionService(new EntityHttpService(authToken));
        }
        return entityRecognition;
    }


}
