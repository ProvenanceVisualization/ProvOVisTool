package Base;

import App.CytoVisProject;
import Util.FilterUtil;

import Action.NodePropertyWindow;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.*;
import org.cytoscape.model.events.RowsSetEvent;
import org.cytoscape.model.events.RowsSetListener;
import org.cytoscape.view.model.CyNetworkView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.cytoscape.view.presentation.property.BasicVisualLexicon.*;

public class NodeSelectedListener2 implements RowsSetListener {
    // Varibles
    private CytoVisProject cytoVisProject;
    private CySwingAppAdapter adapter;
    private boolean flag;
    private boolean closeClusterFlag;
    private boolean bdmFlag;
    List<CyNode> oldNodes = new ArrayList<>();
    NodePropertyWindow nodePropertyWindow;
    public HashMap<String,NodePropertyWindow> nodePropHash = new HashMap<>();
//    CyApplicationManager manager;
//    CyNetworkView networkView;
//    CyNetwork network;


    public NodeSelectedListener2(CytoVisProject cytoVisProject){
        // Initializations
        this.cytoVisProject = cytoVisProject;
        this.adapter = cytoVisProject.getAdapter();
        this.flag = false;
        this.bdmFlag = false;
        this.closeClusterFlag = true;

//        this.manager = adapter.getCyApplicationManager();
//        this.networkView = manager.getCurrentNetworkView();
//        this.network = networkView.getModel();
//
//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), "init",
//                "Info!", JOptionPane.INFORMATION_MESSAGE);
    }

    public void handleEvent(RowsSetEvent e){
        // This flag for the active - inactive button which is in the MyControlPanel class.
        // If it is active then method will show the neighbors of the selected node.

        CyApplicationManager manager = adapter.getCyApplicationManager();
        CyNetworkView networkView = manager.getCurrentNetworkView();
        CyNetwork network = networkView.getModel();

        CyTable table = network.getDefaultNodeTable();
        FilterUtil filter = new FilterUtil(network,table, this.adapter);

        List<CyNode> selectedNodes = CyTableUtil.getNodesInState(network, "selected", true);

        String selectedNode = "null";
        int selectedCluster = -1;

        if(selectedNodes.size() == 1 && (null != cytoVisProject.getMyControlPanel().kMeansClustering))
        {
            selectedNode =  cytoVisProject.getMyControlPanel().kMeansClustering.filter.findNodeName(selectedNodes.get(0));
            selectedCluster = cytoVisProject.getMyControlPanel().kMeansClustering.whichClusterIAmIn(selectedNodes.get(0));
        }

        if(cytoVisProject.getMyControlPanel().isBDMOn)
        {
            List<CyNode> selectedNodesBDM = CyTableUtil.getNodesInState(network, "selected", true);
            CyNode nodenode = selectedNodesBDM.get(0);


//            if( null == nodenode)
//            {
//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), " there is null node selection ",
//                        "Info!", JOptionPane.INFORMATION_MESSAGE);
//            }
//            else
//            {
//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), " there is not null node selection ",
//                        "Info!", JOptionPane.INFORMATION_MESSAGE);
//            }


            selectedNode =  filter.findNodeName(selectedNodes.get(0));


            if(null != selectedNode && false == selectedNode.isEmpty()) {

//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), " selected node " + selectedNode,
//                        "Info!", JOptionPane.INFORMATION_MESSAGE);

                if (null == nodePropHash.get(selectedNode)) {

//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), "BDM node selection1 selected nodes size " +
//                                    String.valueOf(selectedNodes.size() + " selection -> " + selectedNode),
//                            "Info!", JOptionPane.INFORMATION_MESSAGE);


                    nodePropertyWindow = new NodePropertyWindow(cytoVisProject, selectedNodes.get(0));
//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), "BDM node selection2 ",
//                            "Info!", JOptionPane.INFORMATION_MESSAGE);


                    nodePropHash.put(selectedNode, nodePropertyWindow);
//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), "BDM node selection3 ",
//                            "Info!", JOptionPane.INFORMATION_MESSAGE);

                }
                else
                {
                    nodePropertyWindow = nodePropHash.get(selectedNode);
                    if(!nodePropertyWindow.isVisible())
                        nodePropertyWindow.setVisible(true);

                }
            }
        }

        if(cytoVisProject.getMyControlPanel().isComparisonOn)
        {
            List<CyNode> selectedNodesBDM = CyTableUtil.getNodesInState(network, "selected", true);
            CyNode nodenode = selectedNodesBDM.get(0);


//            if( null == nodenode)
//            {
//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), " there is null node selection ",
//                        "Info!", JOptionPane.INFORMATION_MESSAGE);
//            }
//            else
//            {
//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), " there is not null node selection ",
//                        "Info!", JOptionPane.INFORMATION_MESSAGE);
//            }


            selectedNode =  filter.findNodeName(selectedNodes.get(0));


            if(null != selectedNode && false == selectedNode.isEmpty()) {

//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), " selected node " + selectedNode,
//                        "Info!", JOptionPane.INFORMATION_MESSAGE);

                if (null == nodePropHash.get(selectedNode)) {

//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), "BDM node selection1 selected nodes size " +
//                                    String.valueOf(selectedNodes.size() + " selection -> " + selectedNode),
//                            "Info!", JOptionPane.INFORMATION_MESSAGE);


                    nodePropertyWindow = new NodePropertyWindow(cytoVisProject, selectedNodes.get(0));
//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), "BDM node selection2 ",
//                            "Info!", JOptionPane.INFORMATION_MESSAGE);


                    nodePropHash.put(selectedNode, nodePropertyWindow);
//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), "BDM node selection3 ",
//                            "Info!", JOptionPane.INFORMATION_MESSAGE);

                }
                else
                {
                    nodePropertyWindow = nodePropHash.get(selectedNode);
                    if(!nodePropertyWindow.isVisible())
                        nodePropertyWindow.setVisible(true);

                }
            }
        }

        if(isFlag())
        {
//            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), " isFlag true ",
//                    "Info!", JOptionPane.INFORMATION_MESSAGE);
           if( isCloseClusterFlag())
            {
//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), " isCloseFlag true ",
//                        "Info!", JOptionPane.INFORMATION_MESSAGE);
                if (0 != selectedNodes.size())
                {
                    String str = String.valueOf(selectedNodes.size());
                    double newScale = networkView.getVisualProperty(NETWORK_SCALE_FACTOR).doubleValue();

                    double X = networkView.getVisualProperty(NETWORK_CENTER_X_LOCATION).doubleValue();
                    double Y = networkView.getVisualProperty(NETWORK_CENTER_Y_LOCATION).doubleValue();


                    try {
                        cytoVisProject.getMyControlPanel().kMeansClustering.reDrawClusters(cytoVisProject.getMyControlPanel().clusters, selectedNodes.get(0));
                        setCloseClusterFlag(false);
//                        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"n2 closeClusterFlag set to False ..!",
//                                "Info!", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
           else
           {

                 if( selectedCluster != -1 && selectedCluster != cytoVisProject.getMyControlPanel().kMeansClustering.openCluster && null != selectedNode) {

                     cytoVisProject.getMyControlPanel().kMeansClustering.undoCluster(cytoVisProject.getMyControlPanel().kMeansClustering.openCluster);
                     setCloseClusterFlag(true);
                 }
                 else if( selectedCluster == cytoVisProject.getMyControlPanel().kMeansClustering.openCluster)
                 {
                     if( null != selectedNode)
                     {
                         if(null == nodePropHash.get(selectedNode)) {
                             nodePropertyWindow = new NodePropertyWindow(cytoVisProject, selectedNodes.get(0));
                             nodePropHash.put(selectedNode,nodePropertyWindow);
                         }
                     }
                 }
           }

        }
    }



//        if(isFlag()){
////            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"node selected ",
////                    "Info!", JOptionPane.INFORMATION_MESSAGE);
//
//            CyApplicationManager manager = adapter.getCyApplicationManager();
//            CyNetworkView networkView = manager.getCurrentNetworkView();
//            CyNetwork network = networkView.getModel();
//            FilterUtil filter = new FilterUtil(network,network.getDefaultNodeTable());
//            List<CyNode> selectedNodes = CyTableUtil.getNodesInState(network, "selected", true);
//            // Showing neighbors of the selected nodes
//            for(int i=0;i<selectedNodes.size();i++){
//                List<CyNode> neighborList = network.getNeighborList(selectedNodes.get(i), CyEdge.Type.ANY);
//                for (int j=0;j<neighborList.size();j++){
//                    networkView.getNodeView(neighborList.get(j)).setVisualProperty(BasicVisualLexicon.NODE_VISIBLE,true);
//                    List<CyEdge> edges = network.getAdjacentEdgeList(neighborList.get(j), CyEdge.Type.ANY);
//                    for(CyEdge edge : edges){
//                        networkView.getEdgeView(edge).setVisualProperty(BasicVisualLexicon.EDGE_VISIBLE, true);
//                    }
//                }
//            }
//            networkView.updateView();
//        }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean isBDMFlag() {
        return bdmFlag;
    }

    public void setBDMFlag(boolean flag) {
        this.bdmFlag = flag;
    }

    public boolean isCloseClusterFlag() {
        return closeClusterFlag;
    }

    public void setCloseClusterFlag(boolean flag) {
        this.closeClusterFlag = flag;
    }
}
