package Action;

import App.CytoVisProject;
import Util.FilterUtil;
import Util.VisStyleUtil;
import org.cytoscape.app.CyAppAdapter;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SearchManager {
    // Variables
    private CySwingAppAdapter adapter;
    private CytoVisProject cytoVisProject;
    private ArrayList<String> list;
    int index = 0;

    public SearchManager(CytoVisProject cytoVisProject){
        super();
        // Initializations
        this.cytoVisProject = cytoVisProject;
        this.adapter = cytoVisProject.getAdapter();
        list = new ArrayList<String>();
    }

    public void add2List(String name)
    {
        if( null == list)
            list = new ArrayList<String>();
        list.add(index,name);
//        cytoVisProject.getMyControlPanel().setStatus(" SEARCH added "+name );
//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), "added to list "+name,
//                "Info!", JOptionPane.INFORMATION_MESSAGE);
    }

    public void emptyList()
    {
//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), "empty 001",
//                "Info!", JOptionPane.INFORMATION_MESSAGE);

        ImportNodesAction imp = cytoVisProject.getMyControlPanel().getImp();
//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), "empty 002",
//                "Info!", JOptionPane.INFORMATION_MESSAGE);

        VisStyleUtil visStyleUtil = cytoVisProject.getMyControlPanel().getVisStyleUtil();

//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), "empty 003",
//                "Info!", JOptionPane.INFORMATION_MESSAGE);

        CyApplicationManager manager = this.adapter.getCyApplicationManager();

//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), "empty 01",
//                "Info!", JOptionPane.INFORMATION_MESSAGE);

        CyNetworkView networkView = manager.getCurrentNetworkView();

//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), "empty 02",
//                "Info!", JOptionPane.INFORMATION_MESSAGE);

        //cytoVisProject.getMyControlPanel().setStatus(" SEARCH list empty size "+list.size() );

//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), "empty 1",
//                "Info!", JOptionPane.INFORMATION_MESSAGE);



        if(null != list) {

//            JOptionPane.showMessageDialog(this.adapter.getCySwingApplication().getJFrame()," before empty search "+String.valueOf(list.size()),
//                    "Info!", JOptionPane.INFORMATION_MESSAGE);

            for (String name : list) {
                //cytoVisProject.getMyControlPanel().setStatus(" SEARCH list name "+name );

//                JOptionPane.showMessageDialog(this.adapter.getCySwingApplication().getJFrame(), " empty search size" + String.valueOf(list.size()),
//                        "Info!", JOptionPane.INFORMATION_MESSAGE);

                //cytoVisProject.getMyControlPanel().setStatus(" SEARCH in for" );
                //cytoVisProject.getMyControlPanel().setStatus(" SEARCH name 1"+name +" size "+ imp.nodeInfo.size());
                CyNode node = imp.nodeInfo.get(name);
                ImportNodesAction.NodeType nodeType = imp.nodeTypes.get(name);
                //cytoVisProject.getMyControlPanel().setStatus(" empty search list "+name );


//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), " name " + name,
//                        "Info!", JOptionPane.INFORMATION_MESSAGE);


                if (nodeType == ImportNodesAction.NodeType.ACTIVITY) {
                    networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_FILL_COLOR, visStyleUtil.getCURRENT_ACTIVITY_COLOR());
                    //cytoVisProject.getMyControlPanel().setStatus(" empty search list activity " );
//                JOptionPane.showMessageDialog(this.adapter.getCySwingApplication().getJFrame()," empty search for --> an activity",
//                        "Info!", JOptionPane.INFORMATION_MESSAGE);
                }

//            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), "empty 3",
//                    "Info!", JOptionPane.INFORMATION_MESSAGE);

                if (nodeType == ImportNodesAction.NodeType.ENTITY) {
                    networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_FILL_COLOR, visStyleUtil.getCURRENT_ENTITY_COLOR());
                    //cytoVisProject.getMyControlPanel().setStatus(" empty search list entity " );
//                JOptionPane.showMessageDialog(this.adapter.getCySwingApplication().getJFrame()," empty search for --> an entity",
//                        "Info!", JOptionPane.INFORMATION_MESSAGE);
                }

//            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), "empty 4",
//                    "Info!", JOptionPane.INFORMATION_MESSAGE);

                if (nodeType == ImportNodesAction.NodeType.AGENT) {
                    networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_FILL_COLOR, visStyleUtil.getCURRENT_AGENT_COLOR());
                    //cytoVisProject.getMyControlPanel().setStatus(" empty search list agent " );
//                JOptionPane.showMessageDialog(this.adapter.getCySwingApplication().getJFrame()," empty search for --> an agent",
//                        "Info!", JOptionPane.INFORMATION_MESSAGE);
                }
            }

//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), "empty 5",
//                "Info!", JOptionPane.INFORMATION_MESSAGE);

            list.clear();
        }
        //cytoVisProject.getMyControlPanel().setStatus(" SEARCH after empty" );
        //cytoVisProject.getMyControlPanel().setStatus(" SEARCH list emptied " );

    }
}