# Creating a data pipeline to move HPCC Systems data to BigQuery
The goal of this repository is to provide a set of instructions, sample datasets, and code for creating a successful data pipeline from HPCC Systems into BigQuery for business intelligence and data analysis. The main priority is to find a simple, but effective method that would allow data to transfer from HPCC Systems to BigQuery seamlessly.

# Introduction

## Data Pipeline
In computing, a data pipeline is an application designed to process data through a series of connected steps. The purpose of a data pipeline is to transfer data from sources, such as business processes, event tracking systems, and data banks, into a data warehouse for business intelligence and analytics. Data pipelines are versatile and can be used for various tasks, such as transferring data between information systems, performing extract, transform, and load (ETL) operations, enriching data, and conducting real-time data analysis. They typically operate in two modes: batch processing, which runs and processes data in scheduled intervals, and streaming processing, which continuously processes data as it arrives.

## HPCC Systems
HPCC Systems is an open-source data platform developed by LexisNexis Risk Solutions, designed for efficient big data processing and analytics. It features two main components: the Thor Data Refinery Cluster for batch processing and the Roxie Rapid Data Delivery Cluster for real-time querying. Using the declarative Enterprise Control Language (ECL), HPCC simplifies complex data workflows, enhancing development speed and efficiency. Adaptable to both on-premises and cloud deployments, HPCC Systems integrates seamlessly with various data sources, making it a versatile tool for diverse data environments and enabling organizations to unlock valuable insights from their big data.

## BigQuery
BigQuery is a fully-managed, serverless data warehouse provided by Google Cloud, designed for scalable and fast SQL analytics. It enables users to run complex queries on large datasets quickly and efficiently, leveraging the power of Google's infrastructure. BigQuery supports real-time data analysis and offers features like built-in machine learning, automatic scaling, and high availability. BigQuery's integration with other Google Cloud services and its support for standard SQL make it an accessible and powerful tool for data analysts.

# Requirements
* **HPCC Development:** Since this is an integration between HPCC Systems and BigQuery, you need to setup the HPCC development on a local machine. This can be done by following the steps in this documentation: https://cdn.hpccsystems.com/releases/CE-Candidate-9.8.4/docs/EN_US/Containerized_HPCC_Systems_Platform_EN_US-9.8.4-1.pdf (p7 - p14). After finishing the setup, check that ECL Watch is up and running.
  * **Docker Desktop and Helm:** These two applications are installed for the setup of the HPCC development. It is important to note that after Docker Desktop is installed, you need to go into settings and enable **Kubernetes**.
* **VSCode/ECL IDE:** VSCode/ECL IDE needs to be installed in order to write/run code. It is also a good idea to complete the HPCC Systems Data Tutorial if this is your first time working with HPCC Systems. Data Tutorial: https://cdn.hpccsystems.com/releases/CE-Candidate-9.8.4/docs/EN_US/HPCCDataTutorial_EN_US-9.8.4-1.pdf (VSCode/ECL IDE needed)
* **Google Cloud Platform Account:** A GCP Account needs to be created as you will need to utilize the different warehouses and services provided by Google Cloud Platform such as BigQuery and Pub/Sub.
# Method 1: BigQuery/Data Transfer Service
<img width="498" alt="Screenshot 2024-08-05 at 11 22 54 AM" src="https://github.com/user-attachments/assets/ff230820-9cce-4846-a90d-faed547822fd">

### Steps to Despray (Manually) ###
1. Enter your ECL Watch. Go to files and then logical files. If you have logical files, you may skip this step. If you don't have any logical files, you need to click on Landing Zones and press Upload. From there, upload a csv file (you can export an excel file as csv). After it's uploaded, import it to logical files and rename the filename (for organization purposes).
2. After you have at least 1 file under logical files, choose a file you want to despray and click despray.
3. Check in the workunits to make sure your despray went through. Once it says complete, click on Landing Zones and your desprayed file should be there.

**Important:** If you are only transferring one data file as a one-time job/transfer, you can skip the next steps (despray with ECL code).

### Steps to Despray (ECL Code) ###
To despray a file from logical files into landing zone with an ECL script, you must first have files to despray.
1. Enter your ECL Watch. Go to files and then logical files. If you have already have a superfile with files in it, you may skip this step. If you don't, create a superfile. From there, go into the landing zone and upload a csv file (you can export an excel file as csv). After it's uploaded, import it to logical files, rename the filename, and add it to your newly created superfile.
2. Open your ECL IDE or VSCode (make sure you have the ECL extension for VSCode users). Use the ECL library fileservice to write a script to despray the logical file into the landing zone.

Here's a snippet of the ECL script I wrote:

<img width="749" alt="Screenshot 2024-07-23 at 1 40 43 PM" src="https://github.com/user-attachments/assets/cd0bbc25-c36e-4e2c-a5e8-75f5e5a97fea">

### Upload to Google Cloud Storage (manually) ###
1. Create a new project in BigQuery
2. Go into Cloud Storage and click on Buckets
3. Create a new bucket and create a folder inside the bucket
4. Once inside the folder, click Upload Files and choose the file you desprayed into the landing zone (if you can't find it, try looking under a mydropzone or dropzone folder). After uploading, it should be in the bucket/folder.

**Important:** If you are only uploading one data file as a one-time job/transfer, you can skip the next steps (upload with Java).

### Upload to Google Cloud Storage (Java) ###
Although you can upload your file manually, using java or any language program can allow this process to be much more efficient when you need to upload multiple files.
1. Create a new project in BigQuery
2. If you have a bucket and folder, you may skip this step. If not, create a bucket and create a folder inside the bucket.
3. Now, you can write a program utilizing your bucket name, folder name, file name, and file path. I referenced this sample code provided by Google Cloud when writing my program: https://cloud.google.com/storage/docs/samples/storage-upload-file.

Here's a few snippets from my Java program (you can find the full program under the GCS_Code folder):

<img width="1091" alt="Screenshot 2024-07-29 at 2 09 49 PM" src="https://github.com/user-attachments/assets/aa50287d-cf48-4de4-b62b-fc98ac93154a">

<img width="908" alt="Screenshot 2024-07-23 at 2 18 41 PM" src="https://github.com/user-attachments/assets/df2e758e-e4c7-4ac3-936b-51318e461e53">

### Data Transfer Service to BigQuery ###
For this data transfer service, you first must have a dataset and table in BigQuery. (Also be sure to enable the APIs needed for this data transfer)
1. Go into your BigQuery project and click into BigQuery Studio.
2. On the top right next to Explorer, there should be an Add button. Click on Add.
3. Choose Local File (Upload a local file)
4. Select create table from Upload and select your despray file from your Finder or Files Explorer (you may change the file format to csv if needed).
5. If you don't already have a dataset, select create dataset and create one. If you already have one, select it and type a table name on the line below.
6. For schema, you can either choose auto-detect or create one yourself. I recommend choosing auto-detect unless you want a different schema than what your file has.
7. Everything else can be left as default and click create table.

To get a better idea of where everything is laid out, here's a screenshot of the create table layout:

<img width="637" alt="Screenshot 2024-07-23 at 3 24 04 PM" src="https://github.com/user-attachments/assets/eb521422-9985-411a-a586-859836950c19">

8. Now that you have a dataset and table created, click on BigQuery and choose Data Transfers.
9. Click create transfer.
10. For the source, choose Google Cloud Storage.
11. Choose your display name and also the schedule you prefer.
12. For destination settings, choose the dataset and table you just created.
13. For the Cloud Storage URI, click browse and choose the bucket you uploaded your file into. Choose the folder after the bucket and then click on the file in the folder.
14. When you click on the file, a Filename line should appear near the bottom above select. Before choosing select, alter the filename slightly. I have attached a screenshot as an example:

<img width="570" alt="Screenshot 2024-07-23 at 3 31 11 PM" src="https://github.com/user-attachments/assets/6346ba36-354b-4450-948a-8f2c10ac1812">

15. Now, click select.
16. It is optional to check the box that says "Delete source files after transfer", but I recommend checking it. Everything else can be left as default.
17. Scroll to the bottom and click save. Once you wait for the first data transfer run to be successfully completed, Method 1 of the data transfer is complete.

# Method 2: Pub/Sub Messaging Service
<img width="478" alt="Screenshot 2024-08-05 at 11 23 26 AM" src="https://github.com/user-attachments/assets/e39422c0-05ec-4f2d-8419-119e177d6a20">

### Transferring Data from Landing Zone (manually) ###
(**Important:** Only use this process if you want to select specific rows/lines of data from your dataset/file) 

This isn't the main path taken when using the Pub/Sub Messaging Service as it is much more tedious than using a program to convert your csv file into JSON. Briefly mentioning this, the overall process is using the desprayed file data and manually converting it into JSON format so that it follows the schema in Pub/Sub. Since this process takes a long time, especially when transferring multiple rows of data, I won't go into detail.

### Transferring Data from Landing Zone (Java) ###
A quick overview of this method is that it doesn't require any manual work and the entire file will be transferred. The general process is converting the csv file into JSON using a reader that goes through the data line by line. Then, the JSON formatted data is loaded into Pub/Sub as messages and is published into the BigQuery table. A more detailed step by step explanation of the entire process is below.

### Using Pub/Sub Messaging Service ###
Before transferring the data through messages, you first need to set up the schema, topic, and subscriptions in Pub/Sub by following these steps:

1. Go into your BigQuery project and create an empty table under the dataset you created with a schema that matches the data you will use (you should have two tables under that dataset now).
2. Type Pub/Sub in the search bar next to your project name.
3. Click into Pub/Sub.

Your screen should look something like this:

<img width="1271" alt="Screenshot 2024-07-25 at 2 44 29 PM" src="https://github.com/user-attachments/assets/ea1853e8-51f3-437a-bf48-fe75f4484ac1">

3. Click on schemas and create a schema.
4. Give your schema a name and keep the schema type default as Avro. For your schema definition, it should match your dataset/table schema.

Here's an example of the schema I created:

<img width="570" alt="Screenshot 2024-07-25 at 2 50 32 PM" src="https://github.com/user-attachments/assets/b06a06eb-1198-4f34-8e25-01bd48b8c1e5">

5. After you write your schema definition, click create.
6. Now that you have a schema, go into topics and click create topic.
7. Give your topic a name and check the "Use a schema" box and also check the "Add a default subscription box".
8. After checking "Use a schema" you will be asked to select a Pub/Sub schema. Select the schema you just created and leave everything else as default and click create.

Your screen should look like this before clicking create:

<img width="715" alt="Screenshot 2024-07-25 at 3 07 17 PM" src="https://github.com/user-attachments/assets/b5a10c6e-26f3-4d38-8122-d41801bf2816">

9. After creating your topic, go into subscriptions.
10. You should have a subscription created in there already. The delivery type for it should be pull. We will be creating another subscription to write to BigQuery. Click create subscription.
11. Give a name to your subscription and select the Pub/Sub topic you just created when asked.
12. For the delivery type, select "Write to BigQuery" and select the dataset and table that you created in BigQuery. Everything else can be left as default.

Here's a screenshot of what your screen should look like:

<img width="542" alt="Screenshot 2024-07-25 at 3 18 00 PM" src="https://github.com/user-attachments/assets/2e2a9c8a-2a92-44d8-8239-8b10571aac4b">

13. Scroll to the bottom and click create.

Your screen should look similar to this (with less subscriptions):

<img width="1272" alt="Screenshot 2024-07-25 at 3 19 08 PM" src="https://github.com/user-attachments/assets/5e52f7c6-d372-44e4-8104-26e4249320eb">

You have successfully set up the Pub/Sub messaging service.

### Publish Messages in Pub/Sub (manually) ###
1. Click into the topic you just created and click on the "Messages" tab.
2. Click "Publish Message"

Your screen should look similar to this:

<img width="797" alt="Screenshot 2024-07-25 at 3 41 34 PM" src="https://github.com/user-attachments/assets/b660ff17-5145-4f93-a82b-f6bbcf817d42">

3. Write the data from the file you want to transfer (in JSON format) in the message body

Your screen should look similar to this: (you can test out one row at a time first) 

<img width="499" alt="Screenshot 2024-07-25 at 3 53 37 PM" src="https://github.com/user-attachments/assets/148b1ade-6edb-4992-8743-867d400d2182">

4. Click publish.
5. Now when you check the empty table you created, it should contain the message you just published.

You have now successfully transferred data from HPCC Systems (Landing Zone) into BigQuery using the Pub/Sub messaging service.

**Important:** If you are only publishing one message at a time or multiple messages, but not frequently or entire files, you can skip the next steps (publish messages with Java).

### Publish Messages in Pub/Sub (Automation using Java) ###
1. Read over this Pub/Sub guide and reference the sample code to write your own program: https://cloud.google.com/pubsub/docs/publisher. (I used this to help write my Java program)
This website is also a good reference for converting a csv file into JSON:


3. If you decide to code in Java, here are a few snippets of my program (you can view the entire program under the Pub/Sub folder):

<img width="885" alt="Screenshot 2024-07-25 at 4 04 51 PM" src="https://github.com/user-attachments/assets/4e1f9a95-a310-4d21-9b6f-7ef65c3acca0">

<img width="892" alt="Screenshot 2024-07-25 at 4 05 04 PM" src="https://github.com/user-attachments/assets/a65a0485-cfff-481a-aef2-8219ea41562c">

<img width="907" alt="Screenshot 2024-07-25 at 4 05 23 PM" src="https://github.com/user-attachments/assets/cb473af8-2508-49c8-b889-cd0712b76337">

3. Enter your project id/name, topic id/name, and also the filepath of the desprayed file in ECL Watch into the correct places in your program to test it out. Once you run your program, the messages should be published into your BigQuery table.

You have now successfully transferred an entire dataset/file from HPCC Systems (Landing Zone) into BigQuery with a Java program. If you only need to transfer one row, manually inputing it in Pub/Sub works just fine.

# Recommendations
* If you want to transfer entire files of data, use method 1 or the automation process for method 2. If you are looking for a more flexible way of data transfer that can allow you to choose specific rows of data to transfer, use the manual process for method 2.
* If you are looking to implement an automatic data pipeline utilizing a script/program, work on the manual methods first and make sure they work before implementing an automation using a script/program.

# Bonus: Data Analysis 
So, the purpose of this documentation is to provide an effective method that would allow data that would allow data to transfer from HPCC Systems to BigQuery seamlessly. However, we are doing this because BigQuery offers a variety of functions that can be performed on data that cannot be done in HPCC Systems. To name a few, BigQuery can produce complex and dynamic charts and graphs, as well as visualizations with data in its warehouse.

Read more here: https://cloud.google.com/bigquery/docs/query-overview.

* Looker Studio Data Analysis


(food info data)

<img width="600" alt="Screenshot 2024-07-25 at 4 33 26 PM" src="https://github.com/user-attachments/assets/99a5f905-b31c-40b1-adef-a04c13f9e355">



(properties data)

<img width="612" alt="Screenshot 2024-07-25 at 4 37 35 PM" src="https://github.com/user-attachments/assets/8c49215a-8403-42fb-9cf5-db33e29d4002">



(template report)
<img width="404" alt="Screenshot 2024-08-05 at 11 07 23 AM" src="https://github.com/user-attachments/assets/1a04d93b-7e49-41b2-88ca-e632bfcda68a">
