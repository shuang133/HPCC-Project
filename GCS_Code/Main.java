package org.example;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;



public class Main {
    public static void main(String[] args) throws Exception {

        
        Storage storage = StorageOptions.getDefaultInstance().getService();
        GoogleStorageClient gcsClient = new GoogleStorageClient(storage);


        String gcsBucket = "properties1_bucket";
        String gcsSubFolder = "properties";
        String gscFilename = "properties1.csv";

        DataTransferTask dataTransferTask = new DataTransferTask(gcsClient, gcsBucket, gcsSubFolder, gscFilename);

        //dataTransferTask.testUpload();

        while (true) {
            dataTransferTask.testUpload();
            Thread.sleep(1000 * 60 * 60 * 2);
        }
    }
}
