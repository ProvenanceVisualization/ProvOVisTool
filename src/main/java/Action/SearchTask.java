package Action;

import App.CytoVisProject;
import Util.FilterUtil;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

import java.awt.*;
import java.util.ArrayList;

public class SearchTask extends AbstractTask {
    // Variables
    private CySwingAppAdapter adapter;
    private CytoVisProject cytoVisProject;
    private String itemName;
    private boolean isSearch;

    public SearchTask(CytoVisProject cytoVisProject, String itemName, boolean isSearch){
        super();
        // Initializations
        this.cytoVisProject = cytoVisProject;
        this.adapter = cytoVisProject.getAdapter();
        this.itemName = itemName;
        this.isSearch = isSearch;
    }

    public void run(TaskMonitor taskMonitor){
        // Getting necessary components from network
        CyApplicationManager manager = adapter.getCyApplicationManager();
        CyNetworkView networkView = manager.getCurrentNetworkView();
        CyNetwork network = networkView.getModel();
        CyTable table = network.getDefaultNodeTable();
        // Gets all nodes which has same node type value with "nodeType" string, and gets all nodes
        FilterUtil filter = new FilterUtil(network, table, this.adapter);

        //if(isSearch) {
            if (!this.itemName.isEmpty()) {
                boolean control = false;
                ArrayList<CyNode> allNodes = filter.getAllNodes();
                CyNode finalNode = allNodes.get(0);

                int i = 0;
                int j = 0;

                for (CyNode node : allNodes) {
                    if (!control) {
                        if (filter.findNodeName(node).compareTo(this.itemName) == 0) {
                            finalNode = node;
                            control = true;
                            j = i;
                        }
                    }
                    i++;
                }

                if (control) {
                    networkView.getNodeView(finalNode).setVisualProperty(BasicVisualLexicon.NODE_FILL_COLOR, Color.GREEN);
                    cytoVisProject.getSearchManager().add2List(this.itemName);
                    cytoVisProject.getMyControlPanel().getSearchStatusLabel().setText("");

                    networkView.updateView();

//                    JOptionPane.showMessageDialog(this.adapter.getCySwingApplication().getJFrame()," in search 1 "+this.itemName,
//                            "Info!", JOptionPane.INFORMATION_MESSAGE);

                } else {
                    cytoVisProject.getMyControlPanel().getSearchStatusLabel().setText("not a valid item name");
                }

            } else {
                cytoVisProject.getSearchManager().emptyList();
//                cytoVisProject.getMyControlPanel().getSearchStatusLabel().setText("emptied blank");
            }

//        JOptionPane.showMessageDialog(this.adapter.getCySwingApplication().getJFrame()," in search 1 1 "+this.itemName,
//                "Info!", JOptionPane.INFORMATION_MESSAGE);
//        }
//        else
//        {
//            cytoVisProject.getSearchManager().emptyList();
//            cytoVisProject.getMyControlPanel().getSearchStatusLabel().setText("emptied flag");
//
////            JOptionPane.showMessageDialog(this.adapter.getCySwingApplication().getJFrame()," empty search for all in undo",
////                    "Info!", JOptionPane.INFORMATION_MESSAGE);
//        }

//        JOptionPane.showMessageDialog(this.adapter.getCySwingApplication().getJFrame()," in search 2"+this.itemName,
//                "Info!", JOptionPane.INFORMATION_MESSAGE);
    }
}