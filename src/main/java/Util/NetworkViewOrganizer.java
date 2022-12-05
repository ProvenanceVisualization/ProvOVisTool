package Util;

import App.MyControlPanel;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;

import java.util.ArrayList;
import java.util.List;

import static org.cytoscape.view.presentation.property.BasicVisualLexicon.*;

public class NetworkViewOrganizer {

    private CySwingAppAdapter adapter;
    private MyControlPanel panel;
    private Integer maxNode;
    private CyNetworkView networkView;

    public NetworkViewOrganizer(MyControlPanel panel){
        this.panel          = panel;
        this.adapter        = panel.getAdapter();
        this.maxNode        = panel.getMaxNode();
        this.networkView    = adapter.getCyApplicationManager().getCurrentNetworkView();
    }

    public void reOrganizeNetwork(){

        CyTable nodeTable = adapter.getCyApplicationManager().getCurrentNetwork().getDefaultNodeTable();
        CyNetwork network = adapter.getCyApplicationManager().getCurrentNetwork();
        Integer i;

        FilterUtil filter               = new FilterUtil(network,nodeTable, this.adapter);
        ArrayList<CyNode> allNodes      = filter.getAllNodes();
        ArrayList<CyNode> deletedNodes  = new ArrayList<CyNode>();

        if(allNodes.size() > maxNode){

            Integer deleteCount = allNodes.size() - maxNode;

            for(i=0; i<deleteCount; i++){
                deletedNodes.add(allNodes.get(i));
            }

            network.removeNodes(deletedNodes);
        }

        panel.getEntityBasedSorting().doClick();
        networkView.updateView();

    }

    // This method sets visibility of nodes to false which are not in the given node id list
    // @param nodesToBeShown:   Node id list to show only
    // @param filterUtil:       Filter class that includes useful features

    public void showOnly(ArrayList<String> nodesToBeShown, FilterUtil filterUtil){
        List<CyNode> allNodes = adapter.getCyApplicationManager().getCurrentNetwork().getNodeList();
        double avgXPos=0 , avgYPos=0, numNode = 0;
        for(CyNode node : allNodes){
            if(!nodesToBeShown.contains(filterUtil.getNodeId(node, adapter, "name"))){
                adapter.getCyApplicationManager().getCurrentNetworkView().getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_VISIBLE, false);
            }
            else
            {
                numNode++;
                avgXPos+=adapter.getCyApplicationManager().getCurrentNetworkView().getNodeView(node).getVisualProperty(BasicVisualLexicon.NODE_X_LOCATION);
                avgYPos+=adapter.getCyApplicationManager().getCurrentNetworkView().getNodeView(node).getVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION);
            }
        }

        avgXPos /= numNode;
        avgYPos /= numNode;

//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"network updated before--> ",
//                "Info!", JOptionPane.INFORMATION_MESSAGE);

        adapter.getCyApplicationManager().getCurrentNetworkView().setVisualProperty(NETWORK_SCALE_FACTOR,1.0);
        adapter.getCyApplicationManager().getCurrentNetworkView().setVisualProperty(NETWORK_CENTER_X_LOCATION, avgXPos);
        adapter.getCyApplicationManager().getCurrentNetworkView().setVisualProperty(NETWORK_CENTER_Y_LOCATION, avgYPos);
        adapter.getCyApplicationManager().getCurrentNetworkView().updateView();

//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"network updated after--> ",
//                "Info!", JOptionPane.INFORMATION_MESSAGE);

    }

    public void showExcept(ArrayList<String> nodesToBeShown, FilterUtil filterUtil){
        List<CyNode> allNodes = adapter.getCyApplicationManager().getCurrentNetwork().getNodeList();
        double avgXPos=0 , avgYPos=0, numNode = 0;
        for(CyNode node : allNodes){
            if(nodesToBeShown.contains(filterUtil.getNodeId(node, adapter, "name"))){
                adapter.getCyApplicationManager().getCurrentNetworkView().getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_VISIBLE, false);
            }
            else
            {
                numNode++;
                avgXPos+=adapter.getCyApplicationManager().getCurrentNetworkView().getNodeView(node).getVisualProperty(BasicVisualLexicon.NODE_X_LOCATION);
                avgYPos+=adapter.getCyApplicationManager().getCurrentNetworkView().getNodeView(node).getVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION);
            }
        }

        avgXPos /= numNode;
        avgYPos /= numNode;

//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"network updated before--> ",
//                "Info!", JOptionPane.INFORMATION_MESSAGE);

        adapter.getCyApplicationManager().getCurrentNetworkView().setVisualProperty(NETWORK_SCALE_FACTOR,1.0);
        adapter.getCyApplicationManager().getCurrentNetworkView().setVisualProperty(NETWORK_CENTER_X_LOCATION, avgXPos);
        adapter.getCyApplicationManager().getCurrentNetworkView().setVisualProperty(NETWORK_CENTER_Y_LOCATION, avgYPos);
        adapter.getCyApplicationManager().getCurrentNetworkView().updateView();

//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"network updated after--> ",
//                "Info!", JOptionPane.INFORMATION_MESSAGE);

    }

}
