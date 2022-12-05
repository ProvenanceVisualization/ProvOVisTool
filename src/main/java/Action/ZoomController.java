package Action;

import Util.FilterUtil;
import org.cytoscape.app.CyAppAdapter;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

import javax.swing.*;

import static org.cytoscape.view.presentation.property.BasicVisualLexicon.NETWORK_SCALE_FACTOR;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

public class ZoomController implements MouseWheelListener {
    // Variables
    private CySwingAppAdapter adapter;
    private String nodeType;
    CyNetworkView networkView;

    public ZoomController(CySwingAppAdapter adapter){
        super();
        // Initialization
        this.adapter = adapter;
        CyApplicationManager manager = adapter.getCyApplicationManager();
        networkView = manager.getCurrentNetworkView();
        CyNetwork network = networkView.getModel();
        CyTable table = network.getDefaultNodeTable();
    }
    // This will hide nodes which has same node type with "nodeType" string
    public void run(TaskMonitor taskMonitor){
        // Getting necessary components from network
        double scale = networkView.getVisualProperty(NETWORK_SCALE_FACTOR).doubleValue();
        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," scale --> "+String.valueOf(scale),
          "file mile", JOptionPane.INFORMATION_MESSAGE);

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

        double scale = networkView.getVisualProperty(NETWORK_SCALE_FACTOR).doubleValue();

        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," scale --> "+String.valueOf(scale),
                "file mile", JOptionPane.INFORMATION_MESSAGE);

    }
}