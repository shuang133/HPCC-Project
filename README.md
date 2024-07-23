# BigQuery Integration with HPCC Systems
The goal of this repository is to provide a set of instructions, sample datasets, and code for creating a successful data transfer from HPCC Systems into BigQuery. The main priority is to find a simple, but effective method that would allow data to transfer from HPCC Systems to BigQuery seamlessly.
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
For this data transfer service, you first must have a dataset and table in BigQuery.
1. Go into your BigQuery project and click into BigQuery Studio.
2. On the top right next to Explorer, there should be an Add button. Click on Add.
3. Choose Local File (Upload a local file)
4. 

