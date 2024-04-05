package com.proriberaapp.ribera.Api.controllers.admin.dto;

public record S3UploadResponse(
        boolean result,
        String data,
        String timestamp,
        int status
) {
    public S3UploadResponse responseToEntity() {
        return new S3UploadResponse(result, data, timestamp, status);
    }
}
