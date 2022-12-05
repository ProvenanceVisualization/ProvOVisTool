package Util;

import App.CytoVisProject;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.view.model.CyNetworkView;

import java.awt.*;
import java.util.ArrayList;

public class VisStyleUtil {

    Paint DEFAULT_AGENT_COLOR;
    Paint DEFAULT_ACTIVITY_COLOR;
    Paint DEFAULT_ENTITY_COLOR;

    Paint CURRENT_AGENT_COLOR;
    Paint CURRENT_ACTIVITY_COLOR;
    Paint CURRENT_ENTITY_COLOR;

    CytoVisProject cytoVisProject;
    private CySwingAppAdapter adapter;

    public void setCURRENT_AGENT_COLOR(Paint CURRENT_AGENT_COLOR) {
        this.CURRENT_AGENT_COLOR = CURRENT_AGENT_COLOR;
    }

    public Paint getCURRENT_AGENT_COLOR() {
        return CURRENT_AGENT_COLOR;
    }

    public Paint getCURRENT_ACTIVITY_COLOR() {
        return CURRENT_ACTIVITY_COLOR;
    }

    public Paint getCURRENT_ENTITY_COLOR() {
        return CURRENT_ENTITY_COLOR;
    }

    public void setCURRENT_ACTIVITY_COLOR(Paint CURRENT_ACTIVITY_COLOR) {
        this.CURRENT_ACTIVITY_COLOR = CURRENT_ACTIVITY_COLOR;
    }

    public void setCURRENT_ENTITY_COLOR(Paint CURRENT_ENTITY_COLOR) {
        this.CURRENT_ENTITY_COLOR = CURRENT_ENTITY_COLOR;
    }

    public VisStyleUtil(CytoVisProject cytoVisProject)
    {
        this.cytoVisProject = cytoVisProject;
        this.adapter = cytoVisProject.getAdapter();

        DEFAULT_ACTIVITY_COLOR = new Color((int)153,(int)153,(int)255);
        DEFAULT_AGENT_COLOR = new Color((int)255,(int)153,(int)51);
        DEFAULT_ENTITY_COLOR = new Color((int)255,(int)255,(int)102);

        CURRENT_ACTIVITY_COLOR = DEFAULT_ACTIVITY_COLOR;
        CURRENT_AGENT_COLOR = DEFAULT_AGENT_COLOR;
        CURRENT_ENTITY_COLOR = DEFAULT_ENTITY_COLOR;
    }

    public Paint getDEFAULT_AGENT_COLOR() {
        return DEFAULT_AGENT_COLOR;
    }

    public Paint getDEFAULT_ACTIVITY_COLOR() {
        return DEFAULT_ACTIVITY_COLOR;
    }

    public Paint getDEFAULT_ENTITY_COLOR() {
        return DEFAULT_ENTITY_COLOR;
    }

    public void setDefaultVisStyle() {

        CyApplicationManager manager = this.adapter.getCyApplicationManager();
        CyNetworkView networkView = manager.getCurrentNetworkView();
        CyNetwork network = networkView.getModel();
        CyTable table = network.getDefaultNodeTable();

        FilterUtil filterUtil = new FilterUtil(network, table, this.adapter) ;
        ArrayList<CyNode> agents = filterUtil.FilterRowByNodeType("agent", "nodeType");
        ArrayList<CyNode> entities = filterUtil.FilterRowByNodeType("entity", "nodeType");
        ArrayList<CyNode> activities = filterUtil.FilterRowByNodeType("activity", "nodeType");

        //DEFAULT_ACTIVITY_COLOR = networkView.getNodeView(activities.get(0)).getVisualProperty(BasicVisualLexicon.NODE_FILL_COLOR);
        //DEFAULT_AGENT_COLOR = networkView.getNodeView(agents.get(0)).getVisualProperty(BasicVisualLexicon.NODE_FILL_COLOR);
        //DEFAULT_ENTITY_COLOR = networkView.getNodeView(entities.get(0)).getVisualProperty(BasicVisualLexicon.NODE_FILL_COLOR);

        DEFAULT_ACTIVITY_COLOR = new Color((int)153,(int)153,(int)255);
        DEFAULT_AGENT_COLOR = new Color((int)255,(int)153,(int)51);
        DEFAULT_ENTITY_COLOR = new Color((int)255,(int)255,(int)102);

    }


    public void setDEFAULT_AGENT_COLOR(Paint DEFAULT_AGENT_COLOR) {
        this.DEFAULT_AGENT_COLOR = DEFAULT_AGENT_COLOR;
    }

    public void setDEFAULT_ACTIVITY_COLOR(Paint DEFAULT_ACTIVITY_COLOR) {
        this.DEFAULT_ACTIVITY_COLOR = DEFAULT_ACTIVITY_COLOR;
    }

    public void setDEFAULT_ENTITY_COLOR(Paint DEFAULT_ENTITY_COLOR) {
        this.DEFAULT_ENTITY_COLOR = DEFAULT_ENTITY_COLOR;
    }
}
