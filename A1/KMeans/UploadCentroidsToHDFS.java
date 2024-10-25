package KMeans;
// Dependancies
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;

public class UploadCentroidsToHDFS {
    public static void main(String[] args) throws Exception {
        // Check arguments for local and HDFS paths
        if (args.length < 2) {
            System.err.println("Usage: UploadCentroidsToHDFS <local_centroids_file> <hdfs_destination>");
            System.exit(2);
        }

        String localCentroidsFile = args[0];
        String hdfsDestination = args[1];

        Configuration conf = new Configuration();
        // Assuming HDFS URI is hdfs://localhost:9000, adjust according to your Hadoop configuration
        FileSystem fs = FileSystem.get(URI.create("hdfs://localhost:9000"), conf);
        Path destPath = new Path(hdfsDestination);

        // Check if the destination already exists and optionally handle it (overwrite, error out, etc.)
        // if (fs.exists(destPath)) {
        //     System.err.println("Destination already exists: " + hdfsDestination);
        //     System.exit(3);
        // }

        try (InputStream in = new BufferedInputStream(new FileInputStream(localCentroidsFile))) {
            fs.copyFromLocalFile(false, true, new Path(localCentroidsFile), destPath);
            System.out.println("Uploaded centroids file to HDFS: " + hdfsDestination);
        } finally {
            fs.close();
        }
    }
}