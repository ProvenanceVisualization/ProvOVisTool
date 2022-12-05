package App;

import Action.ControlPanelAction;
import Action.SearchManager;
import Action.ZoomController;
import Base.*;
import javafx.scene.input.ZoomEvent;
import javafx.scene.transform.Scale;
import org.cytoscape.app.swing.AbstractCySwingApp;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.swing.CyAction;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.model.events.AddedEdgesListener;
import org.cytoscape.model.events.NetworkDestroyedListener;
import org.cytoscape.model.events.RowsSetListener;

import org.cytoscape.property.PropertyUpdatedEvent;
import org.cytoscape.property.PropertyUpdatedListener;
import org.cytoscape.property.CyProperty;
import org.cytoscape.event.CyListener;
import org.cytoscape.property.SimpleCyProperty;
import org.cytoscape.task.NetworkViewTaskFactory;
import org.cytoscape.view.presentation.annotations.Annotation;

import java.awt.event.MouseWheelListener;
import java.io.IOException;
import java.util.Properties;

public class CytoVisProject extends AbstractCySwingApp{
    // Variables
    private MyControlPanel myControlPanel;
    private NodeSelectedListener nodeSelectedListener;
    private NodeSelectedListener2 nodeSelectedListener2;
    private TableSetListener tableSetListener;
    private EdgesAddedListener edgesAddedListener;
    private SearchManager searchManager;
    private NetworkDeletedListener networkDeletedListener;
    private ZoomController zoomController;

    public CytoVisProject(CySwingAppAdapter adapter) throws IOException {
        super(adapter);
        // Initializations and registrations
        // Creating and registering a new control panel tab
        this.myControlPanel = new MyControlPanel(this);
        adapter.getCyServiceRegistrar().registerService(myControlPanel, CytoPanelComponent.class,new Properties());

        ControlPanelAction controlPanelAction = new ControlPanelAction(adapter.getCySwingApplication(),myControlPanel,adapter);
        adapter.getCyServiceRegistrar().registerService(controlPanelAction, CyAction.class,new Properties());
        // Creating and registering a new RowsSetListener
        this.tableSetListener = new TableSetListener(this, myControlPanel.getEnhancedVersionOfBDM());
        adapter.getCyServiceRegistrar().registerService(tableSetListener, RowsSetListener.class,new Properties());
        // Creating and registering a new NetworkDestroyedListener
        networkDeletedListener = new NetworkDeletedListener(this);
        adapter.getCyServiceRegistrar().registerService(networkDeletedListener, NetworkDestroyedListener.class,new Properties());

        this.nodeSelectedListener = new NodeSelectedListener(this);
        adapter.getCyServiceRegistrar().registerService(nodeSelectedListener,RowsSetListener.class,new Properties());

        this.nodeSelectedListener2 = new NodeSelectedListener2(this);
        adapter.getCyServiceRegistrar().registerService(nodeSelectedListener2,RowsSetListener.class,new Properties());

        this.edgesAddedListener = new EdgesAddedListener(this);
        adapter.getCyServiceRegistrar().registerService(edgesAddedListener, AddedEdgesListener.class, new Properties());

        this.zoomController = new ZoomController(adapter);
        adapter.getCyServiceRegistrar().registerService(zoomController, MouseWheelListener.class, new Properties());

        searchManager = new SearchManager(this);
    }

    public SearchManager getSearchManager() {
        return searchManager;
    }
// Getter and setter methods

    public NodeSelectedListener getNodeSelectedListener() {
        return nodeSelectedListener;
    }

    public NodeSelectedListener2 getNodeSelectedListener2() {
        return nodeSelectedListener2;
    }

    public void setNodeSelectedListener(NodeSelectedListener nodeSelectedListener) {
        this.nodeSelectedListener = nodeSelectedListener;
    }

    public MyControlPanel getMyControlPanel() {
        return myControlPanel;
    }

    public CySwingAppAdapter getAdapter(){
        return this.swingAdapter;
    }

    public void setMyControlPanel(MyControlPanel myControlPanel) {
        this.myControlPanel = myControlPanel;
    }

    public TableSetListener getTableSetListener() {
        return tableSetListener;
    }

    public void setTableSetListener(TableSetListener tableSetListener) {
        this.tableSetListener = tableSetListener;
    }
}
