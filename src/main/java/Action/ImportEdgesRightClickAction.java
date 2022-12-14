package Action;

import App.CytoVisProject;
import Util.EnhancedVersionOfBDM;
import com.opencsv.CSVReader;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.task.read.LoadNetworkFileTaskFactory;
import org.cytoscape.work.TaskIterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ImportEdgesRightClickAction implements MouseListener {
    // Variables
    private CySwingAppAdapter adapter;
    private CytoVisProject cytoVisProject;
    private EnhancedVersionOfBDM enhancedVersionOfBDM;
    private File file;
    private String path;

    public ImportEdgesRightClickAction(CytoVisProject cytoVisProject, String path, EnhancedVersionOfBDM enhancedVersionOfBDM){
        // Initializations of Variables
        this.cytoVisProject     = cytoVisProject;
        this.adapter            = cytoVisProject.getAdapter();
        this.enhancedVersionOfBDM = enhancedVersionOfBDM;
        this.path = path;
    }

    public void mouseClicked(MouseEvent e) {
        if(SwingUtilities.isRightMouseButton(e)){
            // Making a choice to the user for file selection
            file = new File(path);
            LoadNetworkFileTaskFactory EdgeFile = adapter.get_LoadNetworkFileTaskFactory();
            TaskIterator taskIterator = EdgeFile.createTaskIterator(file);
            adapter.getTaskManager().execute(taskIterator);
            cytoVisProject.getMyControlPanel().setStatus("Network is loaded.");
            cytoVisProject.getMyControlPanel().importTableButton.setEnabled(true);

            try {
                if(this.path.contains(".csv")){
                    executeImportEdges(file);
/*                    FileReader filereader       = new FileReader(file);
                    CSVReader csvReader         = new CSVReader(filereader);
                    JSONArray data              = new JSONArray();
                    ArrayList<String> headers   = new ArrayList<>();
                    JSONParser parser           = new JSONParser();
                    JSONObject temp;
                    String[] nextRecord;


                    nextRecord = csvReader.readNext();
                    if(nextRecord != null){
                        for (String header : nextRecord) {
                            headers.add(header);
                        }

                        Integer counter;
                        while ((nextRecord = csvReader.readNext()) != null) {
                            temp    = new JSONObject();
                            counter = 0;

                            for (String cell : nextRecord) {
                                temp.put(headers.get(counter++), cell);
                            }

                            data.add(temp);
                        }
                    }

                    long startTime = new Date().getTime();
                    for(Object object : data){
                        temp = (JSONObject) parser.parse(object.toString());
                        enhancedVersionOfBDM.updateState(temp.get("source").toString(), temp.get("dest").toString());
                    }

                    System.out.println("[" + new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format(new Date()) + "] Total time to run BDM: "
                            + (new Date().getTime() - startTime) + " ms.");*/
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public void executeImportEdges(File inputFile) throws IOException, ParseException {
        FileReader filereader       = new FileReader(inputFile);
        CSVReader csvReader         = new CSVReader(filereader);
        JSONArray data              = new JSONArray();
        ArrayList<String> headers   = new ArrayList<>();
        JSONParser parser           = new JSONParser();
        JSONObject temp;
        String[] nextRecord;


        nextRecord = csvReader.readNext();
        if(nextRecord != null){
            for (String header : nextRecord) {
                headers.add(header);
            }

            Integer counter;
            while ((nextRecord = csvReader.readNext()) != null) {
                temp    = new JSONObject();
                counter = 0;

                for (String cell : nextRecord) {
                    temp.put(headers.get(counter++), cell);
                }

                data.add(temp);
            }
        }

        long startTime = new Date().getTime();
        for(Object object : data){
            temp = (JSONObject) parser.parse(object.toString());
            enhancedVersionOfBDM.updateState(temp.get("source").toString(), temp.get("dest").toString());
        }

        System.out.println("[" + new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format(new Date()) + "] Total time to run BDM: "
                + (new Date().getTime() - startTime) + " ms.");
    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }
}