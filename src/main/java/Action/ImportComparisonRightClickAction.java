package Action;

import App.CytoVisProject;
import Base.CompareGraphsCore;
import Util.EnhancedVersionOfBDM;
import com.opencsv.CSVReader;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.task.read.LoadNetworkFileTaskFactory;
import org.cytoscape.work.TaskIterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ImportComparisonRightClickAction implements MouseListener {
    // Variables
    private CySwingAppAdapter adapter;
    private CytoVisProject cytoVisProject;
    private File file;
    int nodeEdge;
    int graphNo;

    int NODE = 0;
    int EDGE = 1;

    int FIRST = 0;
    int SECOND = 1;

    public ImportComparisonRightClickAction(CytoVisProject cytoVisProject, int nodeEdge, int graphNo){
        // Initializations of Variables
        this.cytoVisProject     = cytoVisProject;
        this.adapter            = cytoVisProject.getAdapter();
        this.nodeEdge           = nodeEdge;
        this.graphNo            = graphNo;
    }

    public void mouseClicked(MouseEvent e) {
        if(SwingUtilities.isRightMouseButton(e)){
            // Making a choice to the user for file selection
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose Network File");
            if(fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION){
                file = fileChooser.getSelectedFile();
            }


            boolean res = true;
            CompareGraphsCore gCore = this.cytoVisProject.getMyControlPanel().getCompareGraphsCore();

            if ((this.nodeEdge == NODE ) && (this.graphNo == FIRST)) {
                res = gCore.chooseFirstGraphsNode();
                if(res)
                    this.cytoVisProject.getMyControlPanel().getFirstGraphsNodeLabel().setText(gCore.getNode1FileName());
            }

            if ((this.nodeEdge == EDGE ) && (this.graphNo == FIRST))
            {
                res = gCore.chooseFirstGraphsEdge();
                if(res)
                    this.cytoVisProject.getMyControlPanel().getFirstGraphsEdgeLabel().setText(gCore.getEdge1FileName());
            }

            if ((this.nodeEdge == NODE ) && (this.graphNo == SECOND))
            {
                res = gCore.chooseSecondGraphsNode();
                if(res)
                    this.cytoVisProject.getMyControlPanel().getSecondGraphsNodeLabel().setText(gCore.getNode2FileName());
            }

            if ((this.nodeEdge == EDGE ) && (this.graphNo == SECOND))
            {
                res = gCore.chooseSecondGraphsEdge();
                if(res)
                    this.cytoVisProject.getMyControlPanel().getSecondGraphsEdgeLabel().setText(gCore.getEdge2FileName());
            }

            if(!res)
            {
                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"The file that you choosed are not valid!" + " E/N --> "+
                        this.nodeEdge + " no of grph --> " + this.graphNo,
                        "Error!", JOptionPane.INFORMATION_MESSAGE);
            }

        }
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