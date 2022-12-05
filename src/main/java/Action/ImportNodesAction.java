package Action;

import App.CytoVisProject;
import Util.EnhancedVersionOfFDM;
import Util.FilterUtil;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.task.read.LoadTableFileTaskFactory;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImportNodesAction extends AbstractCyAction {

    private CytoVisProject cytoVisProject;
    private CySwingAppAdapter adapter;
    private String path;
    private CyNetworkView cyNetworkView;
    public CyNetwork network;
    public CyTable table;
    //public HashMap<String, CyNode> allNodes;
    //public ArrayList<CyNode> allNodes;
    List<CyNode> allNodes;
    public ArrayList<Double> XposArray;
    public ArrayList<Double> YposArray;

    public HashMap<String, Double> Xpos;
    public HashMap<String, Double> Ypos;
    public HashMap<String, CyNode> nodeInfo;
    public HashMap<String, NodeType> nodeTypes;

    public HashMap<String, String> activityAgentMap;

    public ArrayList<CyNode> getActivities() {
        return activities;
    }

    ArrayList<CyNode> agents;
    ArrayList<CyNode> activities ;
    ArrayList<CyNode> entities;

    FilterUtil filter;
    FileWriter fr;
    BufferedWriter br;

    private File file;

    public enum NodeType {
        ACTIVITY,
        AGENT,
        ENTITY
    }

    private EnhancedVersionOfFDM enhancedVersionOfFDM;




    public ImportNodesAction(CytoVisProject cytoVisProject, EnhancedVersionOfFDM enhancedVersionOfFDM) throws IOException {
        super("<html>Import<br/>Nodes</html>");
        this.cytoVisProject = cytoVisProject;
        this.adapter = cytoVisProject.getAdapter();
        this.path = path;
        this.enhancedVersionOfFDM = enhancedVersionOfFDM;

        XposArray=new ArrayList<Double>();
        //cytoVisProject.getMyControlPanel().setStatus("Table is loaded001.");
        YposArray=new ArrayList<Double>();
        //cytoVisProject.getMyControlPanel().setStatus("Table is loaded002.");


        nodeInfo = new HashMap<String, CyNode>();
        nodeTypes = new HashMap<String, NodeType>();

        agents = new ArrayList<CyNode>();
        activities = new ArrayList<CyNode>();
        entities = new ArrayList<CyNode>();

        fr =null;
        try {
            fr = new FileWriter("C:\\tmp\\importNodes2.txt", true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        br = new BufferedWriter(fr);
        try {
            br.write("in MyControl App before call");
            br.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//
//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"in import nodes action path -> " +path,
//          "Info",JOptionPane.INFORMATION_MESSAGE);
    }

    public void actionPerformed(ActionEvent e){

        // Making a choice to the user for file selection
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose Table File");
        if(fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION){
            file = fileChooser.getSelectedFile();
        }
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

//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"MELEK1 in import nodes action path -> " +path,
//                "Info",JOptionPane.INFORMATION_MESSAGE);


        //since the edge information is registered before node registration, the stateCurrent is updated with the Agent related information
//        try {
//            updateEnhancedFDMStateCurrentWithAgentValues();
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }

//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"MELEK2 in import nodes action path -> " +path,
//                "Info",JOptionPane.INFORMATION_MESSAGE);


/*            //imy
            //File file1 = new File("C:\\yazma.txt");
            FileWriter fr = new FileWriter("C:\\tmp\\yazma.txt", true);
            //fr = new FileWriter(file1, true);
            br = new BufferedWriter(fr);
            br.write("data");
            br.newLine();*//*
            XposArray=new ArrayList<Double>();
            cytoVisProject.getMyControlPanel().setStatus("Table is loaded001.");
            YposArray=new ArrayList<Double>();
            cytoVisProject.getMyControlPanel().setStatus("Table is loaded002.");

            cytoVisProject.getMyControlPanel().setStatus("Table is loaded2.");
            cyNetworkView = adapter.getCyApplicationManager().getCurrentNetworkView();

            Double x, y ;
            View<CyNode> view;

            allNodes= this.adapter.getCyApplicationManager().getCurrentNetworkView().getModel().getNodeList();

           cytoVisProject.getMyControlPanel().setStatus("Table is loaded3.");
            for(int i=0;i<allNodes.size();i++){
                view = cyNetworkView.getNodeView(allNodes.get(i));
                x = view.getVisualProperty(BasicVisualLexicon.NODE_X_LOCATION);
                y = view.getVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION);
                XposArray.add(i,x);
                YposArray.add(i,y);
            }

        cytoVisProject.getMyControlPanel().setStatus("Table is loaded4.");
        cytoVisProject.getMyControlPanel().setStatus("node size -->  : "+String.valueOf(XposArray.size()));
*//*          br.write(String.valueOf(i));
            br.newLine();
            br.write("bitti");*//*

        cytoVisProject.getMyControlPanel().importVisStyleButton.setEnabled(true);
*//*        try {
            br.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        try {
            fr.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }*/
    }

    public ArrayList<CyNode> getAgents() {
        return agents;
    }

    private void updateEnhancedFDMStateCurrentWithAgentValues() throws IOException {

        FilterUtil filterUtil = new FilterUtil(cyNetworkView.getModel(), table, this.adapter) ;
        for (CyNode agentNode:agents)
        {
            String agentName = filterUtil.findNodeName(agentNode);

//            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
//                    agentName,"ImportNodesAction",JOptionPane.INFORMATION_MESSAGE);

            ArrayList<String> valueList = enhancedVersionOfFDM.getStateCurrent().get(agentName);

            for(String agentValue: valueList)
            {

//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
//                        agentName + "  -->x " + agentValue ,"mapping",JOptionPane.INFORMATION_MESSAGE);

                br.write("agent is -->"+agentName);
                br.newLine();

                if(NodeType.ACTIVITY == nodeTypes.get(agentValue))
                {
//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
//                           agentName + "  1 is about to added to " + agentValue ,"type",JOptionPane.INFORMATION_MESSAGE);

                    ArrayList<String> innerValueList = enhancedVersionOfFDM.getStateCurrent().get(agentValue);

//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
//                            agentName + "  2 is about to added to " + agentValue ,"type",JOptionPane.INFORMATION_MESSAGE);

                    innerValueList.add(agentName);
//
//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
//                            agentName + "  3 is about to added to " + agentValue ,"type",JOptionPane.INFORMATION_MESSAGE);

                    enhancedVersionOfFDM.getStateCurrent().put(agentValue,innerValueList);

                    //enhancedVersionOfFDM.getStateCurrent().get(agentValue).add(agentName);

                    br.write("******* value is added -->"+agentName);
                    br.newLine();

//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
//                            agentName + "  4 is about to added to " + agentValue ,"type",JOptionPane.INFORMATION_MESSAGE);
                }
            }

            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
                    "ended " ,"mapping",JOptionPane.INFORMATION_MESSAGE);
        }

//            String agentName = filterUtil.findNodeName(node,true);
//
//            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
//                    "in agent 3 --> " + agentName ,"Info",JOptionPane.INFORMATION_MESSAGE);
//
//            //find the hash values of the corresponding agent
//            for(String agentValue:enhancedVersionOfFDM.getStateCurrent().get(agentName))
//            {
//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
//                        "in agent 2 1--> " + agentName ,"Info",JOptionPane.INFORMATION_MESSAGE);
//                enhancedVersionOfFDM.getStateCurrent().get(agentValue).add(agentName);
//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
//                        "agent  2 2--> " + agentName + " is added to the "+agentValue,"Info",JOptionPane.INFORMATION_MESSAGE);
//
//            }


//
//        //agents = filterUtil.FilterRowByNodeType("agent", "nodeType");
//        int i = 0;
//        String tempName = "";
//        ArrayList<CyNode> tempAgents = new ArrayList<CyNode>();
//
//        tempAgents= filterUtil.FilterRowByNodeType("agent", "nodeType");
//
//        for (CyNode agentNode:allNodes)
//        {
//            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
//                    "i --> "+i,"Info",JOptionPane.INFORMATION_MESSAGE);
//
//            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
//                    network.getRow(agentNode).getAllValues(),"Info",JOptionPane.INFORMATION_MESSAGE);
//            i++;
//        }
//
//        for(CyNode node: tempAgents) {
//
//            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
//                    "node 2 "+network.getRow(node).getAllValues()+"  name: ","Info",JOptionPane.INFORMATION_MESSAGE);
//
////            String agentName = filterUtil.findNodeName(node,true);
////
////            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
////                    "in agent 3 --> " + agentName ,"Info",JOptionPane.INFORMATION_MESSAGE);
////
////            //find the hash values of the corresponding agent
////            for(String agentValue:enhancedVersionOfFDM.getStateCurrent().get(agentName))
////            {
////                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
////                        "in agent 2 1--> " + agentName ,"Info",JOptionPane.INFORMATION_MESSAGE);
////                enhancedVersionOfFDM.getStateCurrent().get(agentValue).add(agentName);
////                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
////                        "agent  2 2--> " + agentName + " is added to the "+agentValue,"Info",JOptionPane.INFORMATION_MESSAGE);
////
////            }
//        }

    }


    String nodeTypeToString(NodeType type)
    {
        if(type == NodeType.ACTIVITY)
            return "activity";
        if(type == NodeType.AGENT)
            return "agent";
        if(type == NodeType.ENTITY)
            return "entity";

        return "";
    }
    public void activateImport()
    {
        //cytoVisProject.getMyControlPanel().setStatus("Table is loaded2.");
        cyNetworkView = adapter.getCyApplicationManager().getCurrentNetworkView();

        Double x, y ;
        View<CyNode> view;

        allNodes= this.adapter.getCyApplicationManager().getCurrentNetworkView().getModel().getNodeList();

        //cytoVisProject.getMyControlPanel().setStatus("Table is loaded3.");
        for(int i=0;i<allNodes.size();i++){
            view = cyNetworkView.getNodeView(allNodes.get(i));
            x = view.getVisualProperty(BasicVisualLexicon.NODE_X_LOCATION);
            y = view.getVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION);
            XposArray.add(i,x);
            YposArray.add(i,y);
        }

        //cytoVisProject.getMyControlPanel().setStatus("Table is loaded4.");
        //cytoVisProject.getMyControlPanel().setStatus("node size -->  : "+String.valueOf(XposArray.size()));


        cytoVisProject.getMyControlPanel().importVisStyleButton.setEnabled(true);

    }
    public void removeNodes()  {

        for(int i=0;i<allNodes.size();i++){
            cyNetworkView.getNodeView(allNodes.get(i)).setVisualProperty(BasicVisualLexicon.NODE_VISIBLE,false);
        }
        cyNetworkView.updateView();
    }

    public void resetNodes() {
        //cytoVisProject.getMyControlPanel().setStatus(" in reset node");

        for(int i=0;i<allNodes.size();i++){
 /*           //JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"node2 each --> " + String.valueOf(XposArray.get(i)),"Info",JOptionPane.ERROR_MESSAGE);
            cytoVisProject.getMyControlPanel().setStatus("i --> " + String.valueOf(i));*/

            cyNetworkView.getNodeView(allNodes.get(i)).setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION,XposArray.get(i));
            cyNetworkView.getNodeView(allNodes.get(i)).setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION,YposArray.get(i));
            cyNetworkView.getNodeView(allNodes.get(i)).setVisualProperty(BasicVisualLexicon.NODE_VISIBLE,true);

            //cytoVisProject.getMyControlPanel().setStatus("1 --> i : "+String.valueOf(i));
        }

        cyNetworkView.updateView();

        //cytoVisProject.getMyControlPanel().setStatus("from  2 2 "+ String.valueOf(XposArray.size() ));
        //JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"node2 --> " + String.valueOf(allNodes.size()),"Info",JOptionPane.INFORMATION_MESSAGE);

    }

    public HashMap<String, NodeType> getNodeTypes() {
        return nodeTypes;
    }

    public void setNodeTypes(HashMap<String, NodeType> nodeTypes) {
        this.nodeTypes = nodeTypes;
    }

    public void hashNodes() throws IOException {

        cyNetworkView = adapter.getCyApplicationManager().getCurrentNetworkView();
        CyNetwork network = cyNetworkView.getModel();
        CyTable table = network.getDefaultNodeTable();

        FilterUtil filterUtil = new FilterUtil(network, table, this.adapter) ;
        agents = filterUtil.FilterRowByNodeType("agent", "nodeType");
        entities = filterUtil.FilterRowByNodeType("entity", "nodeType");
        activities = filterUtil.FilterRowByNodeType("activity", "nodeType");



        String name = "test";
        int index = 0;



        for(CyNode node: entities) {
            name= filterUtil.findNodeName(node);
            nodeTypes.put(name,NodeType.ENTITY);
            nodeInfo.put(name,node);
        }

        for(CyNode node: activities) {
            name=filterUtil.findNodeName(node);
            nodeTypes.put(name,NodeType.ACTIVITY);
            nodeInfo.put(name,node);
        }

        //fill activity agent map according to enhancedFDM
        HashMap<String, ArrayList<String>> stateCurrent = enhancedVersionOfFDM.getStateCurrent();
        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"in hash node",
                "Info!", JOptionPane.INFORMATION_MESSAGE);


        activityAgentMap = new HashMap<String, String>();

        for(CyNode node: agents) {
            name= filterUtil.findNodeName(node);
            nodeTypes.put(name,NodeType.AGENT);
            nodeInfo.put(name,node);

//            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"DEBUG1 for agent  "+name,
//                    "Info!", JOptionPane.INFORMATION_MESSAGE);

            if(stateCurrent.containsKey(name))
            {
                br.write("agent--> "+name);
                br.newLine();

//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"DEBUG2 for agent  "+name,
//                        "Info!", JOptionPane.INFORMATION_MESSAGE);

                for(String inneractivity: stateCurrent.get(name))
                {

//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"DEBUG3 for agent  "+name,
//                            "Info!", JOptionPane.INFORMATION_MESSAGE);
//
//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), name+" ---> item "+inneractivity,
//                                 "Info!", JOptionPane.INFORMATION_MESSAGE);

                    br.write("*********************** mappedItem"+inneractivity);
                    br.newLine();
//                    if(nodeTypes.containsKey(inneractivity) && nodeTypes.get(inneractivity) == NodeType.ACTIVITY)
//                    {
//                        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), "agent  :" + name + " has activity " + inneractivity,
//                            "Info!", JOptionPane.INFORMATION_MESSAGE);
//
//                        if(!activityAgentMap.keySet().contains(inneractivity))
//                        {
//                            activityAgentMap.put(inneractivity,name);
////                            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), "ACTIVITYAGENT MAP UPDATED",
////                                    "Info!", JOptionPane.INFORMATION_MESSAGE);
//                        }
//                        else {
//                            String temString = activityAgentMap.get(inneractivity);
//
//                            if (!temString.equals(name)) {
//                                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), "BURAYA GELMEMELI cunku hashmap'de bu activity icin :"+
//                                        inneractivity +" is mapped with "+temString+" now comes "+name,
//                                        "Info!", JOptionPane.INFORMATION_MESSAGE);
//                            }
//                        }
//
//
//                    }
                }
            }
            else
            {
                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),name+ "  agent not in StateCurrent",
                        "Info!", JOptionPane.INFORMATION_MESSAGE);

            }
        }
        br.close();




    }
}
