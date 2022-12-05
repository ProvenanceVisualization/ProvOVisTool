package Action;

import App.CytoVisProject;
import Util.FilterUtil;
import Util.VisStyleUtil;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.task.read.LoadTableFileTaskFactory;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.cytoscape.view.presentation.property.BasicVisualLexicon.*;

public class RecreateNetworkAction {

    private CytoVisProject cytoVisProject;
    private CySwingAppAdapter adapter;
    private String path;
    private CyNetworkView cyNetworkView;
    public CyNetwork network;
    public CyTable table;
    //public HashMap<String, CyNode> allNodes;
    //public ArrayList<CyNode> allNodes;
    List<CyNode> allNodes;

    public RecreateNetworkAction(CytoVisProject cytoVisProject) {
        this.cytoVisProject = cytoVisProject;
        this.adapter = cytoVisProject.getAdapter();
        this.path = path;
    }

    public void recreateView() throws IOException {
        //cytoVisProject.getMyControlPanel().getImp().removeNodes();

        //cytoVisProject.getMyControlPanel().getImp().removeNodes();
        //cytoVisProject.getMyControlPanel().setStatus("h1");
        cytoVisProject.getMyControlPanel().getImp().resetNodes();
        //cytoVisProject.getMyControlPanel().setStatus("h2");
        //imy
        //cytoVisProject.getSearchManager().emptyList();
//        JOptionPane.showMessageDialog(this.adapter.getCySwingApplication().getJFrame()," rec 1 ",
//                "Info!", JOptionPane.INFORMATION_MESSAGE);

        VisStyleUtil visStyleUtil = this.cytoVisProject.getMyControlPanel().getVisStyleUtil();
        //cytoVisProject.getMyControlPanel().setStatus("h3");

        if(     visStyleUtil.getCURRENT_ENTITY_COLOR() != visStyleUtil.getDEFAULT_ENTITY_COLOR() ||
                visStyleUtil.getCURRENT_AGENT_COLOR() != visStyleUtil.getDEFAULT_AGENT_COLOR() ||
                visStyleUtil.getCURRENT_ACTIVITY_COLOR() != visStyleUtil.getDEFAULT_ACTIVITY_COLOR())
        {
/*            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," in style change of recreate view ..!",
                    "Info!", JOptionPane.INFORMATION_MESSAGE);*/
            //cytoVisProject.getMyControlPanel().setStatus("h4");

            ChangeVisualStyleTemplate changeVisualStyleTemplate = new ChangeVisualStyleTemplate(cytoVisProject);
            changeVisualStyleTemplate.changeVisualStyle(visStyleUtil.getDEFAULT_AGENT_COLOR(),
                    visStyleUtil.getDEFAULT_ACTIVITY_COLOR(),
                    visStyleUtil.getDEFAULT_ENTITY_COLOR());
            //cytoVisProject.getMyControlPanel().setStatus("h5");

            cytoVisProject.getMyControlPanel().getVsActivity().setBackground((Color) visStyleUtil.getDEFAULT_ACTIVITY_COLOR());
            cytoVisProject.getMyControlPanel().getVsAgent().setBackground((Color) visStyleUtil.getDEFAULT_AGENT_COLOR());
            cytoVisProject.getMyControlPanel().getVsEntity().setBackground((Color) visStyleUtil.getDEFAULT_ENTITY_COLOR());
            //cytoVisProject.getMyControlPanel().setStatus("h6");

            visStyleUtil.setCURRENT_ACTIVITY_COLOR(visStyleUtil.getDEFAULT_ACTIVITY_COLOR());
            visStyleUtil.setCURRENT_AGENT_COLOR(visStyleUtil.getDEFAULT_AGENT_COLOR());
            visStyleUtil.setCURRENT_ENTITY_COLOR(visStyleUtil.getDEFAULT_ENTITY_COLOR());
            //cytoVisProject.getMyControlPanel().setStatus("h7");
        }
        //cytoVisProject.getMyControlPanel().setStatus("h8");

//        JOptionPane.showMessageDialog(this.adapter.getCySwingApplication().getJFrame()," rec 2 ",
//                "Info!", JOptionPane.INFORMATION_MESSAGE);

        this.adapter.getCyApplicationManager().getCurrentNetworkView().setVisualProperty(NETWORK_CENTER_X_LOCATION, 400); //700
        this.adapter.getCyApplicationManager().getCurrentNetworkView().setVisualProperty(NETWORK_CENTER_Y_LOCATION, 100); //600
        this.adapter.getCyApplicationManager().getCurrentNetworkView().setVisualProperty(NETWORK_SCALE_FACTOR, 0.55);

//        JOptionPane.showMessageDialog(this.adapter.getCySwingApplication().getJFrame()," rec 3 get Search Manager "+String.valueOf(cytoVisProject.getSearchManager()),
//                "Info!", JOptionPane.INFORMATION_MESSAGE);

        if( null != cytoVisProject.getSearchManager()){
            cytoVisProject.getSearchManager().emptyList();
//            JOptionPane.showMessageDialog(this.adapter.getCySwingApplication().getJFrame()," in empty list ",
//                    "Info!", JOptionPane.INFORMATION_MESSAGE);
        }
//
//        cytoVisProject.getMyControlPanel().getSearchArea().setText("");
//        JOptionPane.showMessageDialog(this.adapter.getCySwingApplication().getJFrame()," rec 4 ",
//                "Info!", JOptionPane.INFORMATION_MESSAGE);

        //cytoVisProject.getMyControlPanel().setStatus("h9");

//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"before file read",
//        "Error!", JOptionPane.INFORMATION_MESSAGE);
//
//        FileWriter writer = new FileWriter("C:\\tmp\\locs.txt");
////        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"file opened ",
////                "file mile", JOptionPane.INFORMATION_MESSAGE);
//        try (BufferedWriter buffer = new BufferedWriter(writer)) {
//            buffer.write("Welcome to javaTpoint.");
//            buffer.newLine();
//
//            FilterUtil filterUtil = new FilterUtil(adapter.getCyApplicationManager().getCurrentNetwork(),
//                    adapter.getCyApplicationManager().getCurrentNetwork().getDefaultNodeTable());
//
//            List<CyNode> allNodes = filterUtil.getAllNodes();
//
//            for(CyNode node : allNodes)
//            {
//                Double x =   this.adapter.getCyApplicationManager().getCurrentNetworkView().getNodeView(node).getVisualProperty(BasicVisualLexicon.NODE_X_LOCATION);
//                Double y =   this.adapter.getCyApplicationManager().getCurrentNetworkView().getNodeView(node).getVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION);
//
//                buffer.write(" node --> "+filterUtil.findNodeName(node)+" x -> "+String.valueOf(x)+" y-> "+String.valueOf(y));
//                buffer.newLine();
//            }
//
//            buffer.close();
//        }
//
//        writer.close();
//
//
//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"after file read",
//                "Error!", JOptionPane.INFORMATION_MESSAGE);
    }


}
