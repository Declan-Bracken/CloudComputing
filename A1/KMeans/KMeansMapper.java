package KMeans;
// Import Dependancies
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class KMeansMapper extends Mapper<Object, Text, IntWritable, Text> {
    private Point[] centroids;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        Configuration conf = context.getConfiguration();
        URI[] uris = context.getCacheFiles(); // This is for Hadoop 2.x. For older versions, use DistributedCache.
        
        if (uris != null && uris.length > 0) {
            FileSystem fs = FileSystem.get(conf);
            List<Point> pointsList = new ArrayList<>();
            BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(new Path(uris[0]))));
            String line;
            while ((line = br.readLine()) != null) {
                pointsList.add(Point.parse(line));
            }
            br.close();
            centroids = pointsList.toArray(new Point[0]);
        }
    }

    @Override
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        Point point = Point.parse(value.toString());
        int nearestCentroidIndex = getNearestCentroidIndex(point);
        context.write(new IntWritable(nearestCentroidIndex), new Text(point.toString()));
    }

    private int getNearestCentroidIndex(Point point) {
        int nearestIndex = 0;
        double nearestDistance = Double.MAX_VALUE;
        for (int i = 0; i < centroids.length; i++) {
            double distance = point.distance(centroids[i]);
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestIndex = i;
            }
        }
        return nearestIndex;
    }
}
