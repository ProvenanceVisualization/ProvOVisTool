package Action;

import App.CytoVisProject;
import Util.EnhancedVersionOfBDM;
import Util.EnhancedVersionOfFDM;
import com.opencsv.CSVReader;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.model.CyRow;
import org.cytoscape.task.read.LoadNetworkFileTaskFactory;
import org.cytoscape.work.TaskIterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ImportEdgesAction extends AbstractCyAction {
    // Variables
    private CySwingAppAdapter adapter;
    private CytoVisProject cytoVisProject;
    private EnhancedVersionOfBDM enhancedVersionOfBDM;
    private EnhancedVersionOfFDM enhancedVersionOfFDM;
    private File file;

    public ImportEdgesAction(CytoVisProject cytoVisProject, EnhancedVersionOfBDM enhancedVersionOfBDM, EnhancedVersionOfFDM enhancedVersionOfFDM){
        // Initializations of Variables
        super("<html>Import<br/>Edges</html>");
        this.cytoVisProject         = cytoVisProject;
        this.adapter                = cytoVisProject.getAdapter();
        this.enhancedVersionOfBDM = enhancedVersionOfBDM;
        this.enhancedVersionOfFDM = enhancedVersionOfFDM;
    }

    public void actionPerformed(ActionEvent e){
        // Loading edges which has been extracted


            try {

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Choose Network File");
                if(fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION){
                    file = fileChooser.getSelectedFile();
                }

                LoadNetworkFileTaskFactory EdgeFile = adapter.get_LoadNetworkFileTaskFactory();
                TaskIterator taskIterator = EdgeFile.createTaskIterator(file);
                adapter.getTaskManager().execute(taskIterator);
                cytoVisProject.getMyControlPanel().setStatus("Network is loaded.");
                cytoVisProject.getMyControlPanel().importTableButton.setEnabled(true);

                if(file.toString().contains(".csv")){
                    FileReader filereader       = new FileReader(file);
                    CSVReader csvReader         = new CSVReader(filereader);
                    JSONArray data              = new JSONArray();
                    ArrayList<String> headers   = new ArrayList<>();
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
//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"temp is added to data  -->x" +temp
//                        ,"info", JOptionPane.INFORMATION_MESSAGE);
                            data.add(temp);
                        }
                    }

                    for(Object object : data){
                        JSONObject line = (JSONObject) object;
                        enhancedVersionOfBDM.updateState(line.get("source").toString(), line.get("dest").toString());
                        enhancedVersionOfFDM.updateState(line.get("dest").toString(), line.get("source").toString(), line.get("edgeType").toString());
                    }

                    long startTime = new Date().getTime();

                    for(CyRow row : adapter.getCyApplicationManager().getCurrentNetwork().getDefaultEdgeTable().getAllRows()){
                        enhancedVersionOfBDM.updateState(row.get("Source", String.class), row.get("Destination", String.class));
                        enhancedVersionOfFDM.updateState(row.get("Destination", String.class), row.get("Source", String.class),row.get("EdgeType", String.class));
                    }

                    System.out.println("[" + new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format(new Date()) + "] Total time to run BDM: "
                            + (new Date().getTime() - startTime));
                }
            }catch (Exception es){
//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"The files that" +
//                        " you choosed are not valid!","error", JOptionPane.INFORMATION_MESSAGE);
                es.printStackTrace();
            }
    }
}