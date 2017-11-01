***Programming MapReduce Applications***

MapReduce is the foundational processing framework on which Hadoop was built. 
The MapReduce API implemented in Java still forms the basic building blocks for many other programming interfaces such as Hive or Pig

1. The difference in Hadoop is that a key or value is an object that is an instantiation of a class, with attributes and
    defined methods, that contains (or encapsulates) the data with methods defined for reading and writing data from and to the object.

    Cannot operate directly on a Hadoop data object without using the defined accessor or mutator methods

    Hadoop Box Class | Java Primitive
    BooleanWritable  |     boolean
    ByteWritable     |     byte
    IntWritable      |     int
    FloatWritable    |     float
    LongWritable     |     long
    DoubleWritable   |     double
    NullWritable     |     null
    Text             |     String
    
2. InputFormats and OutputFormats

   InputFormats and OutputFormats determine how files are read from and written to and how keys and values are extracted from the data.
    
       * InputFormats
        
            InputFormats specify how data are extracted from a file. InputFormats provide a factory for RecordReader object. The RecordReader
            objects are then used to actually extract the data from an input split (typically a HDFS block). 
            
            * TextInputFormat: InputFormat for plain text files. Files are broken into lines. Either linefeed or carriage-return are used 
            to signal end of line. Keys are the position in the file, and values ar the line of text.
            
            * KeyValueTextInputFormat: Same as TextInputFormat except each line is divided into a key and value by a separator.
            
            * SequenceFileInputFormat: InputFormat for SequenceFiles
            
            * NLineInputFormat: Similar to TextInputFormat and specifies how many lines should go to each map task
            
            * DBInputFormat: InputFormat to read data from a JDBC data source
            
            * FixedLengthInputFormat: InputFormat to read input files which origin
        
       * OutputFormats
       
            OutputFormats are similar to InputFormats except that OutputFormats determine how data is written out to files as opposed to how data is read.
            
                FileOutputFormat: OutputFormat to write output data to a file
                DBOutputFormat: OutputFormat to write outpert data to a JDN
    
3. MapReduce Program
    
   Contains the following components: Driver, Mapper, Reducer
   
   * Driver: is the program which sets up and starts the MapReduce application.
        Driver code is executed on the client; this code submits the application to the ResourceManager along with the application's configuration.
        The Driver can also accept parameters such as where the input data is to be read from, where to write the output data to.
        In Java MapReduce the Driver is implemented as a Java class containing the entry point (main() method) for the program.
    
   * Mapper: is the Java class that contains the map() method. Each object instance of the Mapper iterates through its assigned input split executing its
             map() method against each record read from the InputSplit, using the InputFormat and its associated RecordReader as discussed previously.
    
            Mappers do most of the heavy lifting when it comes to data processing in MapReduce, as by definition they read ALLof the data in scope for the
            application.
    
            Some key considerations when developing Mapper code:
                * No saving or sharing of state: should not attempt to persist or share data between Map tasks.
                * No side effects: As Map tasks may be executed in any sequence and may be executed more than once, any given Map task must not create any side effects.
                    Such as creating external events or triggering external processes.
                    or be careful of external IO operations inside of Map tasks.
                * No attempt to communicate with other map tasks
    
   * Reducer: runs against a partition and each key and its associated values are passed to a reduce() method inside the Reducer class.
    
    
    
    
    
    
    
    
    
    
    
    