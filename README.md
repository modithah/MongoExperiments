# MongoExperiments

A set of test cases to evaluate cache performance of MongoDB with different document counts and sizes

# Prerequisites

  - MongoDB
  - PostgreSQL 
  - Working only under linux systems (not windows)

# How to run
 - Change the constants (Const.java) according to your setup (MongoDB path, logpath, config path, and directories for the database)
 - Run CreateData.java 
      * This will create different databases in different directories and save the ids of the documents in files for the experiments
- Run the code in schema.sql to make the table in PostgreSQL (you might have to create the index before)
- Run RunExper.java with appropriate PostgreSQL settings
  *  This will issue 50 000 queries (10ms sleep inbetween) with random access with primary id on each of the database and record the stats after each 100th query in postgres.
  *  Each database will have 10 runs of 50 000 executions
- After the experiments finish analyse the data with analyse.sql.
  *  This is an example SQL code to retrieve the JSON data from PostgreSQL with pages read, evicted and bytes in cache for both data and index together with the iteration.
  *  you can use the script in excel to get proper graphs.
              
# About the config

I have disabled the compression and limited the cache to 0.25GB to get the cache saturated faster.
