


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataLoader implements Runnable {
    PageBenchmark benchmark;
    public static final String PAGERANKS_TEXT_FILE = "pageranks.txt";
    public static final String CHARSET = "SJIS";
    private Thread t;

    public DataLoader(PageBenchmark benchmark){
        this.benchmark = benchmark;
    }
    
    @Override
    public void run() {
        BufferedReader reader = null;
        
        try {
            System.out.println("Loading data");

            File pageRanksTextFile = new File(PAGERANKS_TEXT_FILE);
            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(pageRanksTextFile), CHARSET));

            String line;
            int count = 0;

            while ((line = reader.readLine()) != null) {
                String items[] = new String[5];

                items = line.split(" ");
                if (items.length != 4) {
                    continue;
                }

                int id = Integer.parseInt(items[0]);
                String title = items[1];
                Float pageRank = Float.parseFloat(items[2]);

                count++;
                benchmark.pages.add(new Page(id, title, pageRank));
            }

            System.out.println(count + " pages loaded");
            benchmark.dataLoaded();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                System.out.println("Loading complete");
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(DataLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void start() {
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }
}
