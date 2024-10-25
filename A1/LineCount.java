import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import java.io.IOException;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class LineCount {
    // Mapper class
    public static class LineCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
        private final static IntWritable one = new IntWritable(1); // New integer value 'one'
        private Text word = new Text("Lines"); //New string 'lines'
        //void function which will run onc per line.
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            // For each line, emit the key value pair (line with value 1).
            context.write(word, one);
        }
    }
    // Reducer class
    public static class LineCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        // reduce method which runs once for each key recieved from the map phase
        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            // Initialize a sum of 0 for counting the lines
            int sum = 0;
            // for loop that loops through the values recieved for a single key
            for (IntWritable val : values) {
                // Add value to sum
                sum += val.get();
            }
            // Write sum to key
            context.write(key, new IntWritable(sum));
        }
    }
    // main class which runs upon running this script
    public static void main(String[] args) throws Exception {
        // Set up a new Hadoop job configuration
        Configuration conf = new Configuration();
        // Create a new job based on the configuration
        Job job = Job.getInstance(conf, "line count");
        job.setJarByClass(LineCount.class); // Set the JAR class
        job.setMapperClass(LineCountMapper.class); // Set the Mapper class
        job.setCombinerClass(LineCountReducer.class); // Set the Combiner class (optional, for optimization)
        job.setReducerClass(LineCountReducer.class); // Set the Reducer class
        job.setOutputKeyClass(Text.class); // Set the class for the output key
        job.setOutputValueClass(IntWritable.class); // Set the class for the output value
        // Set the paths for the input and output directiories based on the CLI commands
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        //Exit with job execution status
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
