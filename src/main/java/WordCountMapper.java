import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.io.InterruptedIOException;

/**
 * Created by guoch on 1/11/17.
 * The Mapper must implement a map() method. The map() method is where the map function is implemented.
 * The Mapper can also include setup() and cleanup() methods used to perform operations before the first map() method is run
 *     and after the last map() method is run for the InputSplit.
 * The Context object is used to pass information between processes in Hadoop.
 * Not tested
 */
public class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
                  // Mapper<map_input_key, map_input_value, map_output_key, map_output_value>

    private final static IntWritable one = new IntWritable(1);
    private Text wordObject = new Text();

    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();

        for(String word : line.split("\\W+")){
            if(word.length() > 0){
                wordObject.set(word);
                context.write(wordObject, one);
            }
        }
    }

}
