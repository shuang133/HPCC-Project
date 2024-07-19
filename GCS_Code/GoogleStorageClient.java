package org.example;

import com.google.cloud.storage.*;
import com.google.cloud.storage.Storage.BlobListOption;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;

public class GoogleStorageClient {
    private Storage googleStorage;
    public GoogleStorageClient(Storage googleStorage) {
        this.googleStorage = googleStorage;
    }

    public Bucket createBucket(String bucketName) {
        Bucket bucket = googleStorage.create(BucketInfo.of(bucketName));
        return bucket;
    }

    public void uploadFile(String bucketName, String folder, String objectName, String localFile) throws IOException {
        // adding a folder prefix to the file name
        BlobId blobId = BlobId.of(bucketName, folder + "/" + objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        googleStorage.createFrom(blobInfo, Paths.get(localFile));
        System.out.println("File: " + localFile + " uploaded to bucket " + bucketName + " as " + objectName);
    }

    public void deleteLocalFile(String localFile) throws IOException {
        Path path = Paths.get(localFile);
        try {
            Files.delete(path);
            System.out.println("File: " + localFile + " has been deleted from the landing zone.");
        } catch (IOException e) {
            System.err.println("Failed to delete file: " + localFile);
            throw e;
        }
    }

    public void downloadFile(String bucketName, String folder, String objectName, String localFile) throws IOException {
        // Create a new GCS client and get the blob object from the blob ID
        BlobId blobId = BlobId.of(bucketName, folder + "/" + objectName);
        Blob blob = googleStorage.get(blobId);
        blob.downloadTo(Paths.get(localFile));
        System.out.println("File " + objectName + " downloaded to " + localFile);
    }

    // list all files in a folder or bucket
    public void listFiles(String bucketName) throws IOException {
        System.out.println("Files in bucket " + bucketName + ":");
        // list all the blobs in the bucket
        for (Blob blob : googleStorage
                .list(bucketName, BlobListOption.currentDirectory(), BlobListOption.prefix(""))
                .iterateAll()) {
            System.out.println(blob.getName());
        }
    }

    public void deleteFile(String bucketName, String folder, String objectName) throws IOException {
        BlobId blobId = BlobId.of(bucketName,  folder + "/" + objectName);
        Blob blob = googleStorage.get(blobId);
        if (blob == null) {
            System.out.println("File " + objectName + " does not exist in bucket " + bucketName);
            return;
        }
        blob.delete();
        System.out.println("File " + objectName + " deleted from bucket " + bucketName);
    }

}
