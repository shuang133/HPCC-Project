package org.example;

import java.io.File;
import java.time.LocalDateTime;

public class DataTransferTask {
    


    private GoogleStorageClient gcsClient;
    
    private String gcsBucket;
    private String gcsSubFolder;
    private String gcsFilename;

    public DataTransferTask(GoogleStorageClient gcsClient,
                            String gcsBucket,
                            String gcsSubFolder, String gcsFilename) {

        
        this.gcsClient = gcsClient;
        this.gcsBucket = gcsBucket;
        this.gcsSubFolder = gcsSubFolder;
        this.gcsFilename = gcsFilename;
    }

    

    public void testUpload() throws Exception {
        // upload the file to google cloud storage
        //String localFilePath = "/Users/scarletthuang/Downloads/sampledatafoodinfo-despray.csv";
        String localFilePath = "/Users/scarletthuang/hpccdata/dropzone/intern::sh::properties.csv";
        gcsClient.uploadFile("properties1_bucket", "properties", "intern::sh::properties.csv", localFilePath);
        System.out.println(LocalDateTime.now() + " Successfully upload file to google cloud storage");
        gcsClient.deleteLocalFile(localFilePath);
        System.out.println(LocalDateTime.now() + " Successfully deleted file from the landing zone");
    }
}
