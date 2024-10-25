This directory is composed of multiple files and subdirectories:

1. CLI_Outputs
    Features command line interface outputs coppied from the terminal for questions 1 and 2. Note question 2 output is very long due to the multiple iterations of map-reduce jobs, so only the last few jobs were coppied.
2. Outputs
    Text file outputs from the mapreduce jobs for questions 1 and 2 of the assignment.
3. Text_Reader
    All the relevant code for Q1 of the assignment, with a .jar file featuring the compiled classes of the mapreduce job, along with the original shakespeare.txt file.
4. KMeans
    All the relevant java files and compiled classes for Question 2, along with the original data_points.txt and the initial centroid guesses for the n=3 and n=6 clustering cases. NOTE: The jar file 'KMeans.jar' is what gets run, and resides outside this subdirectory.

Other Files:
download_iterations.sh
    Simple script to download the output text files for the centroid positions from HDFS programatically.
upload-centroids.jar
    jar file to upload the initial centroid positions programatically to hdfs. This ended up not being used and instead the initial centroids were uploaded manually using the CLI.

For any questions regarding my code, please email me at declan.bracken@mail.utoronto.ca