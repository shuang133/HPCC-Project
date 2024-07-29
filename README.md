# Creating a data pipeline to move HPCC Systems data to BigQuery
The goal of this repository is to provide a set of instructions, sample datasets, and code for creating a successful data pipeline from HPCC Systems into BigQuery. The main priority is to find a simple, but effective method that would allow data to transfer from HPCC Systems to BigQuery seamlessly.

# Introduction to Data Pipeline
In computing, a data pipeline is an application designed to process data through a series of connected steps. Data pipelines are versatile and can be used for various tasks, such as transferring data between information systems, performing extract, transform, and load (ETL) operations, enriching data, and conducting real-time data analysis. They typically operate in two modes: batch processing, which runs and processes data in scheduled intervals, and streaming processing, which continuously processes data as it arrives.

In the realm of data warehousing, data pipelines are often employed to read data from transactional systems, apply necessary transformations, and then load the data into the data warehouse. Each transformation is defined by a function, where the output of one function becomes the input for the next. These interconnected functions form a Directed Acyclic Graph (DAG), meaning the data flows in a single direction from source to destination without any cycles or loops. Each node in the DAG represents a function, while the edges denote the data flowing between these functions. The initial nodes (sources) connect to the source data systems, and the final nodes (sinks) connect to the destination data systems.

In data pipeline terminology, sources typically refer to transactional systems like relational databases, and sinks refer to data warehouses. This arrangement is known as a data flow DAG. Additionally, DAGs can be used to orchestrate data movement between various pipelines and systems, known as orchestration or control flow DAGs.

# Requirements
* **HPCC Development:** Since this is an integration between HPCC Systems and BigQuery, you need to setup the HPCC development on a local machine. This can be done by following the steps in this documentation: https://cdn.hpccsystems.com/releases/CE-Candidate-9.8.4/docs/EN_US/Containerized_HPCC_Systems_Platform_EN_US-9.8.4-1.pdf (p7 - p14). After finishing the setup, check that ECL Watch is up and running.
  * **Docker Desktop and Helm:** These two applications are installed for the setup of the HPCC development. It is important to note that after Docker Desktop is installed, you need to go into settings and enable **Kubernetes**.
* **VSCode/ECL IDE:** VSCode/ECL IDE needs to be installed in order to write/run code. It is also a good idea to complete the HPCC Systems Data Tutorial if this is your first time working with HPCC Systems. Data Tutorial: https://cdn.hpccsystems.com/releases/CE-Candidate-9.8.4/docs/EN_US/HPCCDataTutorial_EN_US-9.8.4-1.pdf (VSCode/ECL IDE needed)
* **Google Cloud Platform Account:** A GCP Account needs to be created as you will need to utilize the different warehouses and services provided by Google Cloud Platform such as BigQuery and Pub/Sub.
# Method 1: BigQuery/Data Transfer Service
<img width="724" alt="Screenshot 2024-07-22 at 8 48 10 PM" src="https://github.com/user-attachments/assets/e865f2d1-ab1a-4230-88f3-abc07089117e">

### Steps to Despray (Manually) ###
1. Enter your ECL Watch. Go to files and then logical files. If you have logical files, you may skip this step. If you don't have any logical files, you need to click on Landing Zones and press Upload. From there, upload a csv file (you can export an excel file as csv). After it's uploaded, import it to logical files and rename the filename (for organization purposes).
2. After you have at least 1 file under logical files, choose a file you want to despray and click despray.
3. Check in the workunits to make sure your despray went through. Once it says complete, click on Landing Zones and your desprayed file should be there.
** Important: ** If you are only transferring one data file as a one-time job, you can skip the next steps (despray with ECL code).
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

### Upload to Google Cloud Storage (Java) ###
Although you can upload your file manually, using java or any language program can allow this process to be much more efficient when you need to upload multiple files.
1. Create a new project in BigQuery
2. If you have a bucket and folder, you may skip this step. If not, create a bucket and create a folder inside the bucket.
3. Now, you can write a program utilizing your bucket name, folder name, file name, and file path. I referenced this sample code provided by Google Cloud when writing my program: https://cloud.google.com/storage/docs/samples/storage-upload-file.

Here's a few snippets from my Java program (you can find the full program under the GCS_Code folder):

<img width="1095" alt="Screenshot 2024-07-23 at 2 18 23 PM" src="https://github.com/user-attachments/assets/2c08ea8b-182a-4e11-82e0-9f90f9b914a2">

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
<img width="756" alt="Screenshot 2024-07-23 at 10 34 48 PM" src="https://github.com/user-attachments/assets/b75ad998-f449-4c83-be69-4c3d26f1c5e1">

### Transferring Data from Landing Zone ###
(More time-consuming than from Roxie Server) 

This isn't the main path taken when using the Pub/Sub Messaging Service as it is much more tedious than using data from the Roxie server. Briefly mentioning this, the overall process is using the desprayed file data and manually altering it into JSON format so that it follows the schema in Pub/Sub. Since this process takes a long time, especially when transferring multiple rows of data, I won't go into detail.

### Transferring Data from Roxie Server ###
A quick overview of this method is that on the Roxie Server, there is a specific service that can be used to output JSON-formatted data. Since the Pub/Sub messaging service requires the messages to be in JSON format, it is much more efficient to utilize Roxie so that you can copy and past the data instead of rewriting it into JSON format. A more detailed step by step explanation of the entire process is below.

#### Gathering JSON-formatted Data from Roxie Server ####
1. Click on this link and login with your username and password for your HPCC Systems cluster: http://university-roxie.us-hpccsystems-dev.azure.lnrsg.io:8002/esp/files/Login.html.
2. Once inside the system, click on the plus sign in front of roxie.
3. Scroll down and click on "personsfilesearchservicevz".
4. You may enter any first name or last name you want to retrieve data, but make sure to capitalize every letter. Ex: SMITH
<img width="1270" alt="Screenshot 2024-07-24 at 5 44 37 PM" src="https://github.com/user-attachments/assets/a11890a2-83c2-44f8-b975-374c88ce450c">

5. Click submit.
6. A data table should be produced. Below is an example after entering SMITH in the lastname box:
<img width="1271" alt="Screenshot 2024-07-24 at 5 46 37 PM" src="https://github.com/user-attachments/assets/756eb5a4-cfa5-4d13-ac41-3ff27e05adf9">

7. Click on Links (next to Form). You should see something similar to this:
<img width="1267" alt="Screenshot 2024-07-24 at 5 48 37 PM" src="https://github.com/user-attachments/assets/d0c3f250-186f-46d9-8127-15f129ef539b">

8. Go down to JSON (Post JSON messages to this URL) and click /WsEcl/json/query/roxie/personsfilesearchservicevz. (for future reference, a faster way to get to that specific site is using this link: http://university-roxie.us-hpccsystems-dev.azure.lnrsg.io:8002/WsEcl/forms/json/query/roxie/personsfilesearchservicevz)
9. Now enter something for either firstname, lastname, sex, or zip (make sure to put quotes between strings). Then, press send request. Here's an example of what you might see:
<img width="1273" alt="Screenshot 2024-07-24 at 6 12 21 PM" src="https://github.com/user-attachments/assets/c3b6a36d-2b7a-4184-ad18-202ad8c55000">

You have now retrieved JSON-formatted data from the Roxie Server.

### Using Pub/Sub Messaging Service ###
Now you might think that all you need to do is copy the data from roxie into pub/sub, but it's not quite that simple (sorry).

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

3. Copy the JSON-formatted data from the roxie server and paste it in the message body.

Your screen should look similar to this: (you can test out one row at a time first) 

<img width="499" alt="Screenshot 2024-07-25 at 3 53 37 PM" src="https://github.com/user-attachments/assets/148b1ade-6edb-4992-8743-867d400d2182">

4. Click publish.
5. Now when you check the empty table you created, it should contain the message you just published.

You have now successfully transferred data from HPCC Systems (Roxie server) into BigQuery using the Pub/Sub messaging service.

### Publish Messages in Pub/Sub (Automation using Java) ###
1. Read over this Pub/Sub guide and reference the sample code to write your own program: https://cloud.google.com/pubsub/docs/publisher. (I used this to help write my Java program)
2. If you decide to code in Java, here are a few snippets of my program (you can view the entire program under the Pub/Sub folder):

<img width="885" alt="Screenshot 2024-07-25 at 4 04 51 PM" src="https://github.com/user-attachments/assets/4e1f9a95-a310-4d21-9b6f-7ef65c3acca0">

<img width="892" alt="Screenshot 2024-07-25 at 4 05 04 PM" src="https://github.com/user-attachments/assets/a65a0485-cfff-481a-aef2-8219ea41562c">

<img width="907" alt="Screenshot 2024-07-25 at 4 05 23 PM" src="https://github.com/user-attachments/assets/cb473af8-2508-49c8-b889-cd0712b76337">

3. Copy and paste your data from the roxie server into the correct places in your program to test it out. Once you run your program, the messages should be published into your BigQuery table.

You have now successfully transferred data from the Roxie server into BigQuery with a Java program. I recommend using the Java program when you need to publish multiple rows of data. If you only need to transfer one row, manually inputing it in Pub/Sub works just fine (when it's multiple rows of messages, Pub/Sub might not let it happen so use the Java program instead).

# Recommendations
* If you want to transfer entire files of data, use method 1. If you are looking for a more flexible way of data transfer that can allow you to choose specific rows of data to transfer, use method 2.
* Do the manual methods first and make sure they work before implementing an automation using a script/program.

# Bonus: Data Analysis 
So, the purpose of this documentation is to provide an effective method that would allow data that would allow data to transfer from HPCC Systems to BigQuery seamlessly. However, we are doing this because BigQuery offers a variety of functions that can be performed on data that cannot be done in HPCC Systems. To name a few, BigQuery can produce all types of charts and graphs, as well as visualizations with data in its warehouse.

Read more here: https://cloud.google.com/bigquery/docs/query-overview.

* Looker Studio Data Analysis


(food info data)

<img width="600" alt="Screenshot 2024-07-25 at 4 33 26 PM" src="https://github.com/user-attachments/assets/99a5f905-b31c-40b1-adef-a04c13f9e355">



(properties data)

<img width="612" alt="Screenshot 2024-07-25 at 4 37 35 PM" src="https://github.com/user-attachments/assets/8c49215a-8403-42fb-9cf5-db33e29d4002">
