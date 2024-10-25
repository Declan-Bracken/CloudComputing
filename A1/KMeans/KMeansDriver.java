package KMeans;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class KMeansDriver {
    public static final double CONVERGENCE_THRESHOLD = 0.01; // Threshold to determine convergence
    public static final int MAX_ITERATIONS = 20; // Maximum number of iterations

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.err.println("Usage: KMeansDriver <input path> <output path> <initial centroids path>");
            System.exit(-1);
        }

        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);
        String inputPath = args[0];
        String outputPathBase = args[1];
        String centroidsPath = args[2];
        boolean hasConverged = false;
        int iteration = 0;

        while (!hasConverged && iteration < MAX_ITERATIONS) {
            Job job = Job.getInstance(conf, "KMeans Clustering - Iteration " + iteration);
            job.setJarByClass(KMeansDriver.class);
            job.setMapperClass(KMeansMapper.class);
            job.setReducerClass(KMeansReducer.class);
            job.setOutputKeyClass(IntWritable.class);
            job.setOutputValueClass(Text.class);

            FileInputFormat.addInputPath(job, new Path(inputPath));
            Path outputPath = new Path(outputPathBase + "_iteration_" + iteration);
            if (fs.exists(outputPath)) { // Cleanup output path if already exists
                fs.delete(outputPath, true);
            }
            FileOutputFormat.setOutputPath(job, outputPath);

            // Add the current centroids to the distributed cache
            job.addCacheFile(new URI(centroidsPath));

            if (!job.waitForCompletion(true)) {
                System.err.println("ERROR: Iteration " + iteration + " failed.");
                System.exit(1);
            }

            // Check for convergence
            String newCentroidsPath = outputPath + "/part-r-00000";
            hasConverged = checkConvergence(fs, centroidsPath, newCentroidsPath, CONVERGENCE_THRESHOLD);
            centroidsPath = newCentroidsPath; // Update centroids path for next iteration
            iteration++;
        }

        if (hasConverged) {
            System.out.println("Convergence reached after " + iteration + " iterations.");
        } else {
            System.out.println("Maximum iterations reached without convergence.");
        }
    }

    private static boolean checkConvergence(FileSystem fs, String oldCentroidsPath, String newCentroidsPath, double threshold) throws Exception {
        List<Point> oldCentroids = readCentroids(fs, oldCentroidsPath);
        List<Point> newCentroids = readCentroids(fs, newCentroidsPath);

        for (int i = 0; i < oldCentroids.size(); i++) {
            double distance = oldCentroids.get(i).distance(newCentroids.get(i));
            if (distance > threshold) {
                return false; // Centroids have not converged
            }
        }
        return true; // Centroids have converged
    }

    private static List<Point> readCentroids(FileSystem fs, String centroidsPath) throws Exception {
        List<Point> centroids = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(new Path(centroidsPath))));
        String line;
        while ((line = br.readLine()) != null) {
            centroids.add(Point.parse(line));
        }
        br.close();
        return centroids;
    }
}


