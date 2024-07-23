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
1. Enter your ECL Watch. Go to files and then logical files. If you have logical files, you may skip this step. If you don't have any logical files, you need to click on Landing Zones and press Upload. From there, upload a csv file. After it's uploaded, import it to logical files and rename the filename (for organization purposes).
2. After you have at least 1 file under logical files, click on despray.
3. 
### Steps to Despray (ECL Code) ###
1. Enter your ECL Watch. Go to files and then logical files. If you have logical files, you may skip this step. If you don't have any logical files, you need to click on Landing Zones and press Upload. From there, upload a csv file. After it's uploaded, import it to logical files and rename the filename (for organization purposes).
2. ECL IDE or VSCode

