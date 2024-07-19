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

        // create Options object
       /* Options options = new Options();

        // add t option
        options.addOption("f", true, "run frequency for example: 5m (every 5 minutes) or 1h (every 1 hour");
        options.addOption("n", true, "hpcc file name");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        TimeUnit timeUnit;
        int interval;
        if(cmd.hasOption("f")) {
            String optionValue = cmd.getOptionValue("f");
            System.out.println("Parameters: " + optionValue);
            if (optionValue.endsWith("m")) {
                timeUnit = TimeUnit.MINUTES;
            } else if (optionValue.endsWith("h")) {
                timeUnit = TimeUnit.HOURS;
            } else {
                throw new IllegalArgumentException("Not support time unit: " + optionValue);
            }
            interval = Integer.parseInt(optionValue.substring(0, optionValue.length() -1));
        } else {
            throw new IllegalArgumentException("missing `-f` argument");
        }

        String downloadFilename;
        if(cmd.hasOption("n")) {
            downloadFilename = cmd.getOptionValue("n");
            System.out.println("Parameters: " + downloadFilename);
        } else {
            throw new IllegalArgumentException("missing `-n` argument");
        }
*/
        //String host = "localhost:8010";
        //HpccClient hpccClient = new HpccClient(host);

        Storage storage = StorageOptions.getDefaultInstance().getService();
        GoogleStorageClient gcsClient = new GoogleStorageClient(storage);


        String gcsBucket = "properties1_bucket";
        String gcsSubFolder = "properties";
        String gscFilename = "properties1.csv";

        DataTransferTask dataTransferTask = new DataTransferTask(gcsClient, gcsBucket, gcsSubFolder, gscFilename);

        //dataTransferTask.testDespray();
        //dataTransferTask.testUpload();

        while (true) {
            dataTransferTask.testUpload();
            Thread.sleep(1000 * 60 * 60 * 2);
        }
    }
}