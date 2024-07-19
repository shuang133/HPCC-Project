package org.example;

import java.io.File;
import java.time.LocalDateTime;

public class DataTransferTask {
    //public class DataTransferTask implements Runnable{


    private GoogleStorageClient gcsClient;
    //private HpccClient hpccClient;
    //private String hpccDestGroup;
    //private String hpccSourceFilename;
    //private String localDestPath;
    //private String localDestFilename;
    private String gcsBucket;
    private String gcsSubFolder;
    private String gcsFilename;

    public DataTransferTask(GoogleStorageClient gcsClient,
                            String gcsBucket,
                            String gcsSubFolder, String gcsFilename) {

        //this.hpccClient = hpccClient;
        this.gcsClient = gcsClient;
        //this.hpccDestGroup = hpccDestGroup;
        //this.hpccSourceFilename = hpccSourceFilename;
        //this.localDestPath = localDestPath;
        //this.localDestFilename = localDestFilename;
        this.gcsBucket = gcsBucket;
        this.gcsSubFolder = gcsSubFolder;
        this.gcsFilename = gcsFilename;
    }

    /*
    @Override
    public void run(){
        try {
            //System.out.println("Run at: " + LocalDateTime.now() + " to download file: " + localDestFilename + " from hpcc");
            //hpccClient.despray(hpccDestGroup, ".::" + hpccSourceFilename, localDestPath, localDestFilename);

            int pollCount = 10;

            while (pollCount > 0) {
                //String localFilePath = localDestPath + "/" + localDestFilename;
                String localFilePath = "/Users/scarletthuang/Downloads/sampledatafoodinfo-despray.csv";
                File localFile = new File(localFilePath);
                if (localFile.isFile()) {
                    gcsClient.uploadFile(gcsBucket, gcsSubFolder, gcsFilename, localFilePath);
                    localFile.delete();
                    System.out.println(LocalDateTime.now() + " Successfully upload file to google cloud storage");
                    break;
                }
                Thread.sleep(1000 * 60); // Sleep 1 minute
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

     */

/*
    public void testDespray() throws Exception {
        hpccClient.despray(hpccDestGroup, ".::" + hpccSourceFilename, localDestPath, localDestFilename);
    }
*/

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
