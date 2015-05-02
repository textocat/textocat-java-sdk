package com.textocat.api.sdk.model;

/**
 * @author Nikita Zhiltsov
 */
public class BatchMetadata {
    /**
     * batch id
     */
    private String id;
    /**
     * batch status
     */
    private BatchStatus status;

    public BatchMetadata() {}

    public BatchMetadata(String id, BatchStatus status) {
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BatchStatus getStatus() {
        return status;
    }

    public void setStatus(BatchStatus status) {
        this.status = status;
    }
}
