#!/bin/bash
# Script to download all the centroid iterations from hdfs.

# Set the base HDFS output directory
base_hdfs_path="/user/declanbracken/output_kmeans_iteration__iteration_"

# Set the local directory where you want to download the iterations
local_directory="/Users/declanbracken/Development/UofT_Projects/MIE_1628/A1/CLI_Outputs/A1.2_n=6"

# The number of iterations you want to download
num_iterations=20

# Create the local directory if it doesn't exist
mkdir -p "$local_directory"

# Loop through the number of iterations
for i in $(seq 0 $((num_iterations-1)))
do
    # Construct the HDFS path for the current iteration
    current_hdfs_path="${base_hdfs_path}${i}/part-r-00000"

    # Construct the local path for the current iteration
    current_local_path="${local_directory}/iteration_${i}"

    # Download the current iteration from HDFS to the local directory
    hdfs dfs -get "${current_hdfs_path}" "${current_local_path}"

    echo "Downloaded iteration ${i} to ${current_local_path}"
done

echo "All iterations downloaded."
