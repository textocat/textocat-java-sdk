package com.textocat.api.sdk;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.textocat.api.sdk.model.Batch;
import org.json.JSONArray;

import java.util.Set;

import static com.textocat.api.sdk.Command.*;

/**
 * @author Nikita Zhiltsov
 */
enum Command {
    QUEUE, REQUEST, RETRIEVE, SEARCH;

    public String toString() {
        return super.toString().toLowerCase();
    }
}

class EntityHttpService implements TextocatParameters {
    /**
     * auth token
     */
    private final String authToken;

    public EntityHttpService(String authToken) {
        this.authToken = authToken;
    }

    public static final String REQUEST_URL = BASE_URL + "entity/";

    private HttpRequest prebuild(Command command) {
        HttpRequest request = command == QUEUE ? Unirest.post(REQUEST_URL + command) : Unirest.get(REQUEST_URL + command);
        return request.queryString("auth_token", authToken)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
    }

    public String queue(Batch batch) throws UnirestException {
        HttpResponse<JsonNode> response = ((HttpRequestWithBody) prebuild(QUEUE)).body(batch.toString()).asJson();
        return response.getBody().getObject().getString("batchId");
    }

    public JSONArray retrieve(Set<String> batchIds) throws UnirestException {
        HttpRequest prequery = prebuild(RETRIEVE);
        for (String batchId : batchIds) {
            prequery = prequery.queryString("batch_id", batchId);
        }
        HttpResponse<JsonNode> response = prequery.asJson();
        return response.getBody().getObject().getJSONArray("documents");
    }

    public JSONArray search(String searchQuery) throws UnirestException {
        HttpResponse<JsonNode> response = prebuild(SEARCH).queryString("search_query", searchQuery).asJson();
        return response.getBody().getObject().getJSONArray("documents");
    }

    /**
     * @param batchId         batch id
     * @param requestInterval time between HTTP requests (in sec)
     * @throws Exception
     */
    public void waitUntilCompleted(String batchId, int requestInterval) throws Exception {
        while (true) {
            Thread.sleep(requestInterval * 1000);
            HttpResponse<JsonNode> response = prebuild(REQUEST).queryString("batch_id", batchId).asJson();
            if (response.getBody().getObject().getString("status").equals("FINISHED")) {
                break;
            }
        }
    }
}
