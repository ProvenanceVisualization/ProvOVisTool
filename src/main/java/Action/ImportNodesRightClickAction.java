package Action;

import App.CytoVisProject;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.task.read.LoadTableFileTaskFactory;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

public class ImportNodesRightClickAction implements MouseListener {
    // Variables
    private CySwingAppAdapter adapter;
    private CytoVisProject cytoVisProject;
    private File file;
    private String path;

    public ImportNodesRightClickAction(CytoVisProject cytoVisProject, String path){
        // Initializations of Variables
        this.cytoVisProject = cytoVisProject;
        this.adapter = cytoVisProject.getAdapter();
        this.path = path;
    }

    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
//            // Making a choice to the user for file selection
//            JFileChooser fileChooser = new JFileChooser();
//            fileChooser.setDialogTitle("Choose Table File");
//            if(fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION){
//                file = fileChooser.getSelectedFile();
//            }

            //        File file = new File(path);
//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"1 -> " +path,
//                "Info",JOptionPane.INFORMATION_MESSAGE);
//            LoadTableFileTaskFactory NodeFile = adapter.getCyServiceRegistrar().getService(LoadTableFileTaskFactory.class);
//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"2 -> nodeFile" ,
//                "Info",JOptionPane.INFORMATION_MESSAGE);
//            adapter.getTaskManager().execute(NodeFile.createTaskIterator(file));
//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"3 -> execute",
//                "Info",JOptionPane.INFORMATION_MESSAGE);
//            cytoVisProject.getMyControlPanel().setStatus("Table is loaded.");
//            hashNodes();
//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"4 -> hashNodes",
//                "Info",JOptionPane.INFORMATION_MESSAGE);
//            activateImport();
//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"5 -> activateImport",
//                "Info",JOptionPane.INFORMATION_MESSAGE);
            File file = new File(path);
            // Loading edges which has choosen by the user
            LoadTableFileTaskFactory NodeFile = adapter.getCyServiceRegistrar().getService(LoadTableFileTaskFactory.class);
            adapter.getTaskManager().execute(NodeFile.createTaskIterator(file));
            cytoVisProject.getMyControlPanel().setStatus("Table is loaded.");
            cytoVisProject.getMyControlPanel().importVisStyleButton.setEnabled(true);

            try {
                cytoVisProject.getMyControlPanel().getImp().hashNodes();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            cytoVisProject.getMyControlPanel().getImp().activateImport();
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