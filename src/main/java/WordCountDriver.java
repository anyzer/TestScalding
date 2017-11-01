import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
/**
 * Created by guoch on 1/11/17.
 * Not tested
 */

public class WordCountDriver extends Configured implements Tool{
    public int run(String[] args) throws Exception {
        if(args.length != 2) {
            System.out.printf("Usage: %s [generic options] <inputdir> <outputdir>\n", getClass().getSimpleName());
            return -1;
        }

        Job job = new Job(getConf());
        job.setJarByClass(WordCountDriver.class);
        job.setJobName("Word Count");

        FileInputFormat.setInputPaths(job, new Path(args[0]));//first parameter is input file path
        FileOutputFormat.setOutputPath(job, new Path(args[1]));//second parameter is output file path

        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        boolean success = job.waitForCompletion(true);
        return success ? 0 : 1;
    }

    /**
     * This driver leverages a class ToolRunner, which is used to parse command line options.
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        int existCode = ToolRunner.run(new Configuration(), new WordCountDriver(), args);
        System.exit(existCode);
    }

    /**
     * The run() method in ToolRunner performs the following actions:
     * (1) Parses the command line such as input dir and output dir
     * (2) Creates a new Job object instance, using the getConf() method to obtain configuration
     * (3) Gives the Job a name (can verify the name in the ResouceManager UI)
     * (4) Sets the InputFormat and OutputFormat for the Job and determines the InputSplits for the Job
     * (5) Defines the Mapper and Reducer classes to be used for the Job (These must be available in the Java classpath
     *     where the Driver is run—typically these classes are packaged alongside the Driver.)
     * (6) Sets the Intermediate Key and Value Classes (Box classes in this case).
     *     Recall that these are the key-value pairs emitted by the Mapper and serve as input to the Reducer
     * (7) Sets the final output key and value classes, which will be written out files in the outputdir
     */

}
