package KMeans;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class KMeansReducer extends Reducer<IntWritable, Text, IntWritable, Text> {
    @Override
    protected void reduce(IntWritable centroidIndex, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        double sumX = 0;
        double sumY = 0;
        int count = 0;
        
        for (Text value : values) {
            Point point = Point.parse(value.toString());
            sumX += point.getX();
            sumY += point.getY();
            count++;
        }
        
        Point newCentroid = new Point(sumX / count, sumY / count);
        context.write(centroidIndex, new Text(newCentroid.toString()));
    }
}
