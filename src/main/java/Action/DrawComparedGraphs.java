package Action;

import App.CytoVisProject;
import Base.CompareGraphsCore;
import Base.ImportVisualStyleTaskFactory;
import Util.FilterUtil;

import java.awt.*;
import java.io.IOException;
import java.util.*;

import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.model.*;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.work.TaskIterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import static org.cytoscape.view.presentation.property.BasicVisualLexicon.*;

public class DrawComparedGraphs {

    private CytoVisProject      cytoVisProject;
    private CySwingAppAdapter   adapter;
    private FilterUtil          filterUtil;
    private CyNetwork           network;
    private CyTable             nodeTable;
    private CompareGraphsCore   compareGraphsCore;
    private JSONArray           firstGraphNodes;
    private JSONArray           secondGraphNodes;
    private JSONArray           firstGraphEdges;
    private JSONArray           secondGraphEdges;
    private ArrayList           attendanceList;
    private CyNetworkFactory    cnf;
    private CyNetworkViewFactory cnvf;
    private CyNetworkViewManager networkViewManager;
    private CyNetworkManager    networkManager;
    private CyNetwork           myNet;
    private CyNetworkView       networkView;
    public HashMap<String,CyNode> whichNode;
    public HashMap<String,Double> xPosHashMap;
    public HashMap<String,Double> yPosHashMap;

    public ArrayList<String> firstGraphNames;
    public ArrayList<String> secondGraphNames;

    TaskIterator taskIterator;

    public String secondStart;
    public String nullString;

    public DrawComparedGraphs(CompareGraphsCore compareGraphsCore, CytoVisProject cytoVisProject){
        this.compareGraphsCore = compareGraphsCore;
        this.adapter           = compareGraphsCore.getAdapter();
        this.firstGraphNodes   = compareGraphsCore.getFirstGraphsNodes();
        this.secondGraphNodes  = compareGraphsCore.getSecondGraphsNodes();
        this.firstGraphEdges   = compareGraphsCore.getFirstGraphsEdges();
        this.secondGraphEdges  = compareGraphsCore.getSecondGraphsEdges();
        this.attendanceList    = compareGraphsCore.getSimilarNodePairs();
        this.cytoVisProject    = cytoVisProject;
        this.whichNode = new HashMap<String,CyNode>();

        this.firstGraphNames = new ArrayList<>();
        this.secondGraphNames = new ArrayList<>();

        this.xPosHashMap = new HashMap<>();
        this.yPosHashMap = new HashMap<>();
        this.nullString = "";

        initXYPos();
    }



    public void draw() throws IOException {
        Integer i;
        Integer j;

        cnf = adapter.getCyNetworkFactory();
        cnvf = adapter.getCyNetworkViewFactory();
        networkViewManager = adapter.getCyNetworkViewManager();
        networkManager = adapter.getCyNetworkManager();
        myNet = null;
        networkView = adapter.getCyApplicationManager().getCurrentNetworkView();

        myNet = cnf.createNetwork();
        networkManager.addNetwork(myNet);

        networkView = cnvf.createNetworkView(myNet);
        networkViewManager.addNetworkView(networkView);

        // Adding properties to the network tables
        Set<Map.Entry> properties = ((JSONObject) firstGraphNodes.get(0)).entrySet();
        Iterator iterator = properties.iterator();
        addNodeProperties(myNet, iterator);

        properties = ((JSONObject) secondGraphNodes.get(0)).entrySet();
        iterator = properties.iterator();
        addNodeProperties(myNet, iterator);

        properties = ((JSONObject) firstGraphEdges.get(0)).entrySet();
        iterator = properties.iterator();
        addEdgeProperties(myNet, iterator);


        properties = ((JSONObject) secondGraphEdges.get(0)).entrySet();
        iterator = properties.iterator();
        addEdgeProperties(myNet, iterator);

        //cytoVisProject.getMyControlPanel().loadingFrame.setVisible(true);

        filterUtil = new FilterUtil(myNet, myNet.getDefaultNodeTable(), this.adapter);
        // Visualizing nodes and edges
        visualizeNodes(myNet, networkView, firstGraphNodes, 1);


//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," here 1",
//        "Warning!", JOptionPane.INFORMATION_MESSAGE);

        visualizeEdges(myNet, networkView, filterUtil, firstGraphEdges, 1);


        cytoVisProject.getMyControlPanel().comparisonArea.setText(" Loading Nodes ...");
//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," here 2",
//                "Warning!", JOptionPane.INFORMATION_MESSAGE);

        visualizeNodes(myNet, networkView, secondGraphNodes, 2);


//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," here 3",
//                "Warning!", JOptionPane.INFORMATION_MESSAGE);

        visualizeEdges(myNet, networkView, filterUtil, secondGraphEdges, 2);

//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," here 4",
//                "Warning!", JOptionPane.INFORMATION_MESSAGE);

        //cytoVisProject.getMyControlPanel().loadingFrame.setVisible(false);

        cytoVisProject.getMyControlPanel().comparisonArea.setText(" Loading Edges ...");
//
        taskIterator = new ImportVisualStyleTaskFactory(cytoVisProject).createTaskIterator();
        adapter.getTaskManager().execute(taskIterator);

        FilterUtil filterUtil = new FilterUtil(adapter.getCyApplicationManager().getCurrentNetwork(),
                adapter.getCyApplicationManager().getCurrentNetwork().getDefaultNodeTable(), this.adapter);
        ArrayList<CyNode> allNodes = filterUtil.getAllNodes();

        Double x = 0.0, y = 0.0;

        CyNode node = allNodes.get(0);
        int index = 0;


//        FileWriter writer = new FileWriter("C:\\tmp\\nodenode.txt");
////        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"file opened ",
////                "file mile", JOptionPane.INFORMATION_MESSAGE);
//        try (BufferedWriter buffer = new BufferedWriter(writer)) {
//            buffer.write("Welcome to javaTpoint.");
//            buffer.newLine();
            int xBase = 0;
            int yBase = 0;

//            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), " here 1-->" + String.valueOf(index),
//            "Warning!", JOptionPane.INFORMATION_MESSAGE);


            while (node != null && !secondStart.equals(filterUtil.findNodeName(node)) && index < allNodes.size()) {

                String str = filterUtil.findNodeName(node);
                x = xPosHashMap.get(str);
                y = yPosHashMap.get(str);

                if (x != null && y != null) {

//                    buffer.write("part 1 node --> " + str + " --> type " + filterUtil.findNodeType(node));
//                    buffer.newLine();

                    networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, x);
                    networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, y);

                    whichNode.put(str,node);
                    firstGraphNames.add(str);
                } else {
                    networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_VISIBLE, false);

                    nullString += str;

                }

                if (str.contains("agent8_")) {
                    networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_VISIBLE, false);
                }
                index++;
                node = allNodes.get(index);
            }
            int lastIndex = index;

            while (index < allNodes.size()) {
                String strRaw = filterUtil.findNodeName(node);
                String str = strRaw.substring(1);
//            if( str.equals("_agent8_"))
//            {
//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," here in before null str "+String.valueOf(index),
//                        "Warning!", JOptionPane.INFORMATION_MESSAGE);
//            }
                if (!nullString.contains(str)) {
//                if( str.equals("_agent8_"))
//                {
//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," here in null str "+String.valueOf(index),
//                            "Warning!", JOptionPane.INFORMATION_MESSAGE);
//                }
                    x = 1500 + xPosHashMap.get(str);
                    y = yPosHashMap.get(str);


                    if (x != null && y != null) {
//                x = 2000.0 + (xLoc.get(index - lastIndex));
//                y = 0.0 + (yLoc.get(index - lastIndex));

//                    if( str.equals("_agent8_"))
//                    {
//                        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," here in not null "+String.valueOf(index)+" x-> "+x
//                                +" y->"+y,
//                                "Warning!", JOptionPane.INFORMATION_MESSAGE);
//                    }
                        networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, x);
                        networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, y);


                        whichNode.put(strRaw,node);
                        secondGraphNames.add(strRaw);


                    } else {

                        networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_VISIBLE, false);
                    }

                } else {
//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," part 2 str is in nullStr "+str+" remember nullSTR is -->"+nullString,
//                        "Warning!", JOptionPane.INFORMATION_MESSAGE);
                    networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_VISIBLE, false);
                }

                if (str.contains("agent8_")) {
                    networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_VISIBLE, false);
                }

                node = allNodes.get(index);
                index++;
            }

            networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_VISIBLE, false);

//            buffer.close();
     //   }

        networkView .setVisualProperty(NETWORK_CENTER_X_LOCATION, 700); //700 //100
        networkView .setVisualProperty(NETWORK_CENTER_Y_LOCATION, 200); //600 //100
        networkView.setVisualProperty(NETWORK_SCALE_FACTOR, 0.3);


        networkView.updateView();
    }

    public void reDraw()
    {
//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), " 1 ",
//        "Warning!", JOptionPane.INFORMATION_MESSAGE);

        try {
            adapter.getTaskManager().execute(taskIterator);
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("here ");
        }


        FilterUtil filterUtil = new FilterUtil(adapter.getCyApplicationManager().getCurrentNetwork(),
                adapter.getCyApplicationManager().getCurrentNetwork().getDefaultNodeTable(), this.adapter);
        ArrayList<CyNode> allNodes = filterUtil.getAllNodes();


        ArrayList<CyNode> agents = filterUtil.FilterRowByNodeType("agent", "nodeType");
        ArrayList<CyNode> entities = filterUtil.FilterRowByNodeType("entity", "nodeType");
        ArrayList<CyNode> activities = filterUtil.FilterRowByNodeType("activity", "nodeType");

        for(CyNode node: agents) {
            networkView.getNodeView(node).setLockedValue(BasicVisualLexicon.NODE_FILL_COLOR, cytoVisProject.getMyControlPanel().visStyleUtil.getDEFAULT_AGENT_COLOR());
        }
        for(CyNode node: entities) {
            networkView.getNodeView(node).setLockedValue(BasicVisualLexicon.NODE_FILL_COLOR, cytoVisProject.getMyControlPanel().visStyleUtil.getDEFAULT_ENTITY_COLOR());
        }
        for(CyNode node: activities) {
            networkView.getNodeView(node).setLockedValue(BasicVisualLexicon.NODE_FILL_COLOR, cytoVisProject.getMyControlPanel().visStyleUtil.getDEFAULT_ACTIVITY_COLOR());
        }

        networkView .setVisualProperty(NETWORK_CENTER_X_LOCATION, 700); //700 //100
        networkView .setVisualProperty(NETWORK_CENTER_Y_LOCATION, 200); //600 //100
        networkView.setVisualProperty(NETWORK_SCALE_FACTOR, 0.3);

        networkView.updateView();
    }

    public void changeColors(CompareGraphsCore compareGraphsCore){
        Integer i;
        //Changing color of similar and non-similar nodes
        this.attendanceList = compareGraphsCore.getSimilarNodePairs();
        FilterUtil filterUtil = new FilterUtil(adapter.getCyApplicationManager().getCurrentNetwork(),
                adapter.getCyApplicationManager().getCurrentNetwork().getDefaultNodeTable(), this.adapter);
        ArrayList<CyNode> allNodes = filterUtil.getAllNodes();

        for(CyNode node:allNodes){
            networkView.getNodeView(node).clearValueLock(BasicVisualLexicon.NODE_FILL_COLOR);
        }

        for(i=0; i<attendanceList.size(); i++){
            ArrayList<String> nodeDatas = (ArrayList<String>) attendanceList.get(i);
            String firstNode            = nodeDatas.get(0);
            String secondNode           = nodeDatas.get(1);

            for(CyNode node : allNodes){
                if(myNet.getRow(node).get("nodeID", String.class).equals(firstNode) ||
                        myNet.getRow(node).get("nodeID", String.class).equals(secondNode)){

//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"in color  pair with --> "+ firstNode + " "+secondNode,
//                            "file mile", JOptionPane.INFORMATION_MESSAGE);

                    networkView.getNodeView(node).setLockedValue(BasicVisualLexicon.NODE_FILL_COLOR, Color.GREEN);
                }
            }
        }

        for(CyNode node : allNodes){
            if(!networkView.getNodeView(node).isValueLocked(BasicVisualLexicon.NODE_FILL_COLOR)){
//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"without pair --> "+filterUtil.findNodeName(node),
//                        "file mile", JOptionPane.INFORMATION_MESSAGE);
                networkView.getNodeView(node).setLockedValue(BasicVisualLexicon.NODE_FILL_COLOR, Color.RED);
            }
        }
    }

    private void addEdgeProperties(CyNetwork myNet, Iterator iterator) {
        while (iterator.hasNext()){
            String tmp = iterator.next().toString();
            tmp = tmp.substring(8, tmp.length());
            if(myNet.getDefaultEdgeTable().getColumn(tmp) == null){
                myNet.getDefaultEdgeTable().createColumn(tmp, String.class, false);
            }
        }
    }

    private void addNodeProperties(CyNetwork myNet, Iterator iterator) {
        while (iterator.hasNext()){
            String tmp = iterator.next().toString();
            tmp = tmp.substring(8, tmp.length());
            if(myNet.getDefaultNodeTable().getColumn(tmp) == null){
                myNet.getDefaultNodeTable().createColumn(tmp, String.class, false);
            }
        }
    }

    private void visualizeEdges(CyNetwork myNet, CyNetworkView networkView, FilterUtil filterUtil, JSONArray edgeList, int graphNo) {
        Integer i;
        Set<Map.Entry> properties;
        Iterator iterator;

//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," in visualizeEdges 1",
//                "Warning!", JOptionPane.INFORMATION_MESSAGE);

        for(i=1; i<edgeList.size(); i++){

            JSONObject nodeObject = (JSONObject) edgeList.get(i);

//            if(2 == graphNo)// && i > 78)
//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," in visualizeEdges 2 -> " + String.valueOf(i),
//                        "Warning!", JOptionPane.INFORMATION_MESSAGE);
//
//            String sourceStr = nodeObject.get("node1ID").toString();
//            String destStr = nodeObject.get("node2ID").toString();
//
//            if(2 == graphNo && i >20)
//            {
//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," here sourceNode  -> " + sourceStr+" destSTr ->"+destStr,
//                        "Warning!", JOptionPane.INFORMATION_MESSAGE);
//            }
//
//            CyNode sourceNode = filterUtil.getNode(nodeObject.get("node1ID").toString(), adapter, "nodeID");
//            CyNode destNode = filterUtil.getNode(nodeObject.get("node2ID").toString(), adapter, "nodeID");
//
//            if(1 == graphNo && i >20)
//            {
//                if(null == sourceNode)
//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," sourceNode null ",
//                        "Warning!", JOptionPane.INFORMATION_MESSAGE);
//                else
//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," sourceNode -> "+filterUtil.findNodeName(sourceNode),
//                            "Warning!", JOptionPane.INFORMATION_MESSAGE);
//
//
//                if(null == destNode)
//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," destNode null ",
//                            "Warning!", JOptionPane.INFORMATION_MESSAGE);
//                else
//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," destNodeNode -> "+filterUtil.findNodeName(destNode),
//                            "Warning!", JOptionPane.INFORMATION_MESSAGE);
//
//            }


            CyEdge edge = myNet.addEdge(filterUtil.getNode(nodeObject.get("node1ID").toString(), adapter, "nodeID"),
                    filterUtil.getNode(nodeObject.get("node2ID").toString(), adapter, "nodeID"), true);

//            if(2 == graphNo && i ==21)
//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," in visualizeEdges 3 -> " + String.valueOf(i),
//                        "Warning!", JOptionPane.INFORMATION_MESSAGE);

            properties = ((JSONObject) edgeList.get(0)).entrySet();

//            if(2 == graphNo && i ==21)
//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," in visualizeEdges4 -> " + String.valueOf(i),
//                        "Warning!", JOptionPane.INFORMATION_MESSAGE);

            iterator = properties.iterator();

//            if(2 == graphNo && i ==21)
//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," in visualizeEdges 5 -> " + String.valueOf(i),
//                        "Warning!", JOptionPane.INFORMATION_MESSAGE);



//            if(2 == graphNo && i ==21)
//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," in visualizeEdges 6 -> " + String.valueOf(i),
//                        "Warning!", JOptionPane.INFORMATION_MESSAGE);
            while (iterator.hasNext()){

                String tmp = iterator.next().toString();
                tmp = tmp.substring(8, tmp.length());

//                if(2 == graphNo && i ==79)
//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," in visualizeEdges 7 -> " + tmp,
//                            "Warning!", JOptionPane.INFORMATION_MESSAGE);
                myNet.getDefaultEdgeTable().getRow(edge.getSUID()).set(tmp, nodeObject.get(tmp));

//                if(i == 20 && 2 == graphNo)
//                {
//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," in visualizeEdges 20 2 tmp-> " + tmp,
//                            "Warning!", JOptionPane.INFORMATION_MESSAGE);
//
//                }

                if(tmp.equals("edgeID")){
                    myNet.getDefaultEdgeTable().getRow(edge.getSUID()).set("name", nodeObject.get(tmp));
                    myNet.getDefaultEdgeTable().getRow(edge.getSUID()).set("shared name", nodeObject.get(tmp));

//                    if(i == 79 && 2 == graphNo)
//                    {
//                        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," in visualizeEdges 20 2 edgeID-> " + nodeObject.get(tmp),
//                                "Warning!", JOptionPane.INFORMATION_MESSAGE);
//
//                    }

                }


//                if(i == 20 && 2 == graphNo)
//                {
//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," in visualizeEdges 20 2 eoW-> " + nodeObject.get(tmp),
//                            "Warning!", JOptionPane.INFORMATION_MESSAGE);
//
//                }
            }

            networkView.updateView();
        }

//        if(2 == graphNo )
//            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," in visualizeEdges 2 bitti-> ",
//                    "Warning!", JOptionPane.INFORMATION_MESSAGE);
    }

    private void visualizeNodes(CyNetwork myNet, CyNetworkView networkView, JSONArray nodeList, int graphNo) {
        Integer i;
        Set<Map.Entry> properties;
        Iterator iterator;
        boolean control = true;

        int x=0, y=0;
        for(i=1; i< nodeList.size(); i++){
            CyNode node = myNet.addNode();
            JSONObject nodeObject = (JSONObject) nodeList.get(i);
            properties = ((JSONObject) nodeList.get(0)).entrySet();
            iterator = properties.iterator();
            while (iterator.hasNext()){
                String tmp = iterator.next().toString();
                tmp = tmp.substring(8, tmp.length());
                myNet.getDefaultNodeTable().getRow(node.getSUID()).set(tmp, nodeObject.get(tmp));

                if(tmp.equals("nodeID")){
                    myNet.getDefaultNodeTable().getRow(node.getSUID()).set("name", nodeObject.get(tmp));
                    myNet.getDefaultNodeTable().getRow(node.getSUID()).set("shared name", nodeObject.get(tmp));

                    if(graphNo==2 && control)
                    {
                        secondStart = nodeObject.get(tmp).toString();
                        control = false;
                    }
                }

//                if(i == 1 && 2 == graphNo)
//                {
//                    CyNode destNode = filterUtil.getNode(nodeObject.get("nodeID").toString(), adapter, "nodeID");
//
//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," in visualizeNodes -> tmp --> "
//                                    + tmp+ " value -->"+nodeObject.get(tmp)+" dest Node --> "+filterUtil.findNodeName(destNode),
//                            "Warning!", JOptionPane.INFORMATION_MESSAGE);
//                }

            }

            networkView.updateView();
        }


//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
//                "Second start --> "+secondStart,
//                "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    public ArrayList findNodeTypes(ArrayList idList, JSONArray nodes){
        Integer i = 0;
        Integer j;
        ArrayList<String> nodeTypes = new ArrayList<>();

        while(i < idList.size()){
            for(j=1; j<nodes.size(); j++){

                Integer id = Integer.parseInt(((JSONObject) nodes.get(j)).get("nodeID").toString());
                if(id == (Integer)idList.get(i)){
                    String type = ((JSONObject) nodes.get(j)).get("nodeType").toString();
                    nodeTypes.add(type);
                }

            }

            i++;
        }

        return nodeTypes;
    }

    private void initXYPos() {
        xPosHashMap.put("activity4" ,403.2287596113541);
        yPosHashMap.put("activity4" ,285.7079836796312);

        xPosHashMap.put("entity1" ,250.75537093947912);
        yPosHashMap.put("entity1" ,223.08869046674056);

        xPosHashMap.put("activity_6" ,364.1509703047135);
        yPosHashMap.put("activity_6" ,127.37555570111556);

        xPosHashMap.put("activity8" ,579.9245603926041);
        yPosHashMap.put("activity8" ,204.1184145878343);

        xPosHashMap.put("entity3" ,433.19451888869787);
        yPosHashMap.put("entity3" ,205.0488802860765);

        xPosHashMap.put("activity91" ,33.77126487563635);
        yPosHashMap.put("activity91" ,50.02926511273665);

        xPosHashMap.put("entity6" ,-31.91183868576502);
        yPosHashMap.put("entity6" ,131.55544156537337);

        xPosHashMap.put("activity_92" ,-124.99461380173182);
        yPosHashMap.put("activity_92" ,74.18009824261947);

        xPosHashMap.put("activity93" ,9.924430216059932);
        yPosHashMap.put("activity93" ,246.3477847050218);

        xPosHashMap.put("activity94" ,-122.0221253984115);
        yPosHashMap.put("activity94" ,199.89108914838118);

        xPosHashMap.put("activity95" ,-138.71763626266932);
        yPosHashMap.put("activity95" ,268.3654849003343);

        xPosHashMap.put("activity10" ,471.21893295119787);
        yPosHashMap.put("activity10" ,429.71869534955306);

        xPosHashMap.put("entity5" ,349.8399656660416);
        yPosHashMap.put("entity5" ,393.98529691205306);

        xPosHashMap.put("activity11" ,132.36196121047522);
        yPosHashMap.put("activity11" ,242.3851382206468);

        xPosHashMap.put("entity4" ,136.55296310256506);
        yPosHashMap.put("entity4" ,99.80357999310775);

        xPosHashMap.put("activity4_" ,-60.57045761276697);
        yPosHashMap.put("activity4_" ,-199.22098665728288);

        xPosHashMap.put("entity1_" ,-170.10865799118494);
        yPosHashMap.put("entity1_" ,-132.49135714068132);

        xPosHashMap.put("activity6_" ,-275.8269044511459);
        yPosHashMap.put("activity6_" ,-170.56980257525163);

        xPosHashMap.put("activity8_" ,-469.8479005448959);
        yPosHashMap.put("activity8_" ,-169.1691372920485);

        xPosHashMap.put("entity3_" ,-330.79550186325525);
        yPosHashMap.put("entity3_" ,-112.41877871050553);

        xPosHashMap.put("activity91_" ,53.18718704055334);
        yPosHashMap.put("activity91_" ,-428.52327852740007);

        xPosHashMap.put("entity6_" ,-80.61801162888025);
        yPosHashMap.put("entity6_" ,-386.0744565059157);

        xPosHashMap.put("activity92_" ,31.064632261500606);
        yPosHashMap.put("activity92_" ,-356.76107149615007);

        xPosHashMap.put("activity93_" ,1.7192941553452101);
        yPosHashMap.put("activity93_" ,-474.8552182246657);

        xPosHashMap.put("activity94_" ,-182.78036514450525);
        yPosHashMap.put("activity94_" ,-441.3499997188063);

        xPosHashMap.put("activity95_" ,-20.947399293675176);
        yPosHashMap.put("activity95_" ,-545.8402951289626);

        xPosHashMap.put("activity10_" ,-423.13946548630213);
        yPosHashMap.put("activity10_" ,-333.62423067583757);

        xPosHashMap.put("entity5_" ,-292.874298249974);
        yPosHashMap.put("entity5_" ,-301.5304196406813);

        xPosHashMap.put("activity11_" ,-513.9842530839584);
        yPosHashMap.put("activity11_" ,22.881869025090168);

        xPosHashMap.put("entity4_" ,-375.24993911911463);
        yPosHashMap.put("entity4_" ,-45.33624010576921);

        xPosHashMap.put("entity22" ,214.70880111526037);
        yPosHashMap.put("entity22" ,383.44034451947493);

        xPosHashMap.put("activity_22" ,137.84040817092443);
        yPosHashMap.put("activity_22" ,487.0379397343187);

        xPosHashMap.put("activity1" ,350.2630308515885);
        yPosHashMap.put("activity1" ,194.98940152631087);

        xPosHashMap.put("entity2" ,304.3847654707291);
        yPosHashMap.put("entity2" ,100.68622464642806);

        xPosHashMap.put("activity2" ,272.317352140651);
        yPosHashMap.put("activity2" ,-0.03937273516373807);

        xPosHashMap.put("activity3" ,528.3654173261979);
        yPosHashMap.put("activity3" ,120.31320828900618);

        xPosHashMap.put("activity5" ,176.218963468776);
        yPosHashMap.put("activity5" ,-7.975488000056316);

        xPosHashMap.put("activity7" ,343.26577743361975);
        yPosHashMap.put("activity7" ,521.9902712772874);

        xPosHashMap.put("activity9" ,-166.1744691484115);
        yPosHashMap.put("activity9" ,145.4592806767015);

        xPosHashMap.put("entity22_" ,8.016234005198726);
        yPosHashMap.put("entity22_" ,-46.69511061205583);

        xPosHashMap.put("activity22_" ,53.43872817824865);
        yPosHashMap.put("activity22_" ,-153.388009362361);

        xPosHashMap.put("activity1_" ,-148.11434951950525);
        yPosHashMap.put("activity1_" ,-30.66729884638505);

        xPosHashMap.put("entity2_" ,-232.20689407516932);
        yPosHashMap.put("entity2_" ,-234.81939058794694);

        xPosHashMap.put("activity2_" ,-178.47265640427088);
        yPosHashMap.put("activity2_" ,-327.66668062700944);

        xPosHashMap.put("activity3_" ,-342.67782608200525);
        yPosHashMap.put("activity3_" ,16.89680356488509);

        xPosHashMap.put("activity5_" ,-432.5497132890365);
        yPosHashMap.put("activity5_" ,71.26683682904525);

        xPosHashMap.put("activity7_" ,-302.976104890599);
        yPosHashMap.put("activity7_" ,-428.3218014766188);

        xPosHashMap.put("activity9_" ,-153.1221467607162);
        yPosHashMap.put("activity9_" ,-508.78475313677507);

        xPosHashMap.put("agent22" ,232.31773361037756);
        yPosHashMap.put("agent22" ,509.5069338749437);

        xPosHashMap.put("agent1" ,338.7277830488541);
        yPosHashMap.put("agent1" ,287.7981936405687);

        xPosHashMap.put("agent3" ,524.3654173261979);
        yPosHashMap.put("agent3" ,328.56241483197493);

        xPosHashMap.put("agent2" ,402.4910887129166);
        yPosHashMap.put("agent2" ,22.509611792424153);

        xPosHashMap.put("agent6" ,701.3828123457291);
        yPosHashMap.put("agent6" ,190.02724332318587);

        xPosHashMap.put("agent71" ,-218.37622085739588);
        yPosHashMap.put("agent71" ,367.6191470097093);

        xPosHashMap.put("agent5" ,421.7102049238541);
        yPosHashMap.put("agent5" ,509.6492678593187);

        xPosHashMap.put("agent8" ,90.78105148147131);
        yPosHashMap.put("agent8" ,358.9072024296312);

        xPosHashMap.put("agent22_" ,123.3394239790299);
        yPosHashMap.put("agent22_" ,-90.19218950762468);

        xPosHashMap.put("agent1_" ,-246.73692337204432);
        yPosHashMap.put("agent1_" ,-50.98572283281999);

        xPosHashMap.put("agent3_" , 46.17484649489904);
        yPosHashMap.put("agent3_" ,-254.53798800005632);

        xPosHashMap.put("agent2_" ,-355.753204499974);
        yPosHashMap.put("agent2_" ,-240.538399987361);

        xPosHashMap.put("agent6_" ,-588.0659181230209);
        yPosHashMap.put("agent6_" ,-214.49575167193132);

        xPosHashMap.put("agent71_" ,24.40621551345373);
        yPosHashMap.put("agent71_" ,-661.9892819453688);

        xPosHashMap.put("agent5_" ,-376.19247451950525);
        yPosHashMap.put("agent5_" ,-413.6122067500563);

        xPosHashMap.put("agent8_" ,-628.6867066972396);
        yPosHashMap.put("agent8_" ,71.47828549847884);

        xPosHashMap.put("agent4" ,82.00257095168615);
        yPosHashMap.put("agent4" ,182.72920865521712);

        xPosHashMap.put("agent7" ,-58.71175018478846);
        yPosHashMap.put("agent7" ,260.0881106327562);

        xPosHashMap.put("agent4_" ,-500.10943618942713);
        yPosHashMap.put("agent4_" ,-64.51282625689225);

        xPosHashMap.put("agent7_" ,-84.09727493454432);
        yPosHashMap.put("agent7_" ,-508.4516537715407);

        xPosHashMap.put("entity_56_" ,-84.09727493454432);
        yPosHashMap.put("entity_56_" ,-508.4516537715407);

        xPosHashMap.put("agent_75_" ,-513.9842530839584);
        yPosHashMap.put("agent_75_" ,22.881869025090168);

        xPosHashMap.put("entity_86_" ,364.1509703047135);
        yPosHashMap.put("entity_86_" ,127.37555570111556);

        xPosHashMap.put("agent_28_" ,-124.99461380173182);
        yPosHashMap.put("agent_28_" ,74.18009824261947);

        xPosHashMap.put("entity_58_" ,137.84040817092443);
        yPosHashMap.put("entity_58_" ,487.0379397343187);
    }

}
