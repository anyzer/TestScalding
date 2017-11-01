import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Created by guoch on 1/11/17.
 * Not tested
 */
public class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable>{
    private IntWritable wordCountWritable = new IntWritable();

    @Override
    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException{
        int wordCount = 0;
        for(IntWritable value : values){
            wordCount += value.get();
        }

        wordCountWritable.set(wordCount);
        context.write(key, wordCountWritable);
    }
}
