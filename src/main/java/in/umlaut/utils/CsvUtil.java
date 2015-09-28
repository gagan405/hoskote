package in.umlaut.utils;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gbm on 27/09/15.
 */
public class CsvUtil {
    public CsvUtil(){
    }

    public List<String> readLines(String filePath) throws IOException {
        File f = new File(filePath);
        if(!f.exists()){
            //create the file and return
            f.createNewFile();
            return new ArrayList<>();
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filePath));
            String line = "";
            List<String> data = new ArrayList<String>();

            while ((line = br.readLine()) != null) {
                data.add(line);
            }
            return data;
        } catch (FileNotFoundException ex){
            System.err.println("File " + filePath + " not found");
            return new ArrayList<>();
        }finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void writeLines(List<String> lines, String filePath, boolean isOverwrite){
        File f = new File(filePath);
        FileWriter writer = null;
        try {
            writer = new FileWriter(f, !isOverwrite);
            for(String line : lines){
                try {
                    writer.write(line + System.lineSeparator());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
