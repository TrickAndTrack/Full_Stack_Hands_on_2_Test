package com.reactjsfsaws.bucket;

public enum BucketName {

	PROFILE_IMAGE("nz-image-upload");

    private final String bucketName;

    BucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
