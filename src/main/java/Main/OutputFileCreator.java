
package Main;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class OutputFileCreator {
    
    public void createOutputFile(List<ArrayList> outputData, String path) throws IOException{

        try (
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(path));

            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withDelimiter(';')
                    .withHeader("LineNumber", "Status", "Message"));
        ) {
            for(int i=0; i<outputData.size(); i++){
                
                csvPrinter.printRecord(outputData.get(i));
            }
            csvPrinter.flush();            
        }
    }
}
