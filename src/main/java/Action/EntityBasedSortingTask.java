package Action;

import App.CytoVisProject;
import Util.FilterUtil;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.*;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.cytoscape.view.presentation.property.BasicVisualLexicon.*;

public class EntityBasedSortingTask extends AbstractTask{
    // Variables
    private FilterUtil filter;
    private CytoVisProject cytoVisProject;

    private CySwingAppAdapter adapter;
    private CyApplicationManager manager;
    private CyNetworkView networkView;
    private CyNetwork network;
    private CyTable table;

    private double entityX;
    private double entityY;

    public EntityBasedSortingTask(CytoVisProject cytoVisProject){
        // Initializations
        this.cytoVisProject = cytoVisProject;
        this.adapter = cytoVisProject.getAdapter();
        this.manager = adapter.getCyApplicationManager();
        this.networkView = manager.getCurrentNetworkView();
        this.network = networkView.getModel();
        this.table = network.getDefaultNodeTable();
        this.filter = new FilterUtil(network,table, this.adapter);
        // Entities will begin to relocate frome (0,0) coordinates
        entityX = 0;
        entityY = 0;
    }

    public void run(TaskMonitor taskMonitor) throws IOException {
        // Setting status
        taskMonitor.setStatusMessage("Sorting Entitys based on Activity Times ...");

//        if(false == cytoVisProject.getMyControlPanel().isBDMOn) {
            // Getting entity nodes
            ArrayList<CyNode> entitys = filter.FilterRowByNodeType("entity", "nodeType");
            removeIrrelevant();
            // Getting all activities listed based on their start time and relocating
            for (CyNode node : entitys) {
                List<CyNode> activities = listActivitiesByTime(node);
                relocate(activities, node);
            }

        networkView.setVisualProperty(NETWORK_SCALE_FACTOR, 0.45);
        networkView.setVisualProperty(NETWORK_CENTER_X_LOCATION, 1300);
        networkView.setVisualProperty(NETWORK_CENTER_Y_LOCATION, 150);

        networkView.updateView();


//        }
//        else
//        {
//            // Getting entity nodes
//            ArrayList<CyNode> entitys = filter.FilterRowByNodeType("entity", "nodeType");
//            removeIrrelevant();
//            // Getting all activities listed based on their start time and relocating
//            for (CyNode node : entitys) {
//                List<CyNode> activities = listActivitiesByTime(node);
//                relocateBDM(activities, node);
//            }
//
//        }
    }
    // This method lists activities by their time
    public List<CyNode> listActivitiesByTime(CyNode node){
        // Gettin neighbors of entity
        List<CyNode> neighbors = filter.findNodeNeighbors("activity",node);
        List<CyNode> activities = new ArrayList<CyNode>();
        // Getting all activities and their start times
        List<CyNode> allActivities = filter.FilterRowByNodeType("activity","nodeType");
        List<String> startTimes = new ArrayList<>();
        if(table != null){
            if(table.getColumn("startTime") != null){
                startTimes = getTimeFromColumn(table.getColumn("startTime"));
            }
        }
        // Parsing start times froms String to Date
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Date> dates = new ArrayList<Date>();

        try{
            for(int i=0;i<startTimes.size();i++){
                Date date = format.parse(startTimes.get(i));
                dates.add(date);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        // Sorting all activities by their time
        sort(dates,allActivities);
        // Getting listed activities which is related with this entity by their time
        List<Integer> index = new ArrayList<Integer>();
        for(int i=0;i<neighbors.size();i++){
            index.add(allActivities.indexOf(neighbors.get(i)));
        }
        // Making a list from index of listed activities which is CyNode type
        Collections.sort(index);
        for(int i=0;i<index.size();i++){
            activities.add(allActivities.get(index.get(i)));
        }
        return activities;
    }
    // This method relocates activities and agents which is related with one entity
    public void relocate(List<CyNode> activities,CyNode entity){
        double agentX = -120, agentY = -150, activityX = -120, activityY = 150;
        // Relocating entity
        networkView.getNodeView(entity).setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION,entityX);
        networkView.getNodeView(entity).setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION,entityY);
        // Relocating agents
        List<CyNode> agents = filter.findNodeNeighbors("agent",entity);
        for(CyNode node : agents){
            networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, agentX + entityX);
            networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, agentY);
            agentX = agentX + 80;
        }
        // Relocating activities
        for(CyNode node : activities){
            networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, activityX + entityX);
            networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, activityY);
            activityX = activityX + 80;
        }

        if(activities.size() > agents.size()){
            entityX = entityX + 80 + activities.size()*50; // 120 ve 80
        }else{
            entityX = entityX + 80 + agents.size()*50; //120 ve 80
        }
    }


    public void relocateBDM(List<CyNode> activities,CyNode entity) throws IOException {
//        double agentX = -120, agentY = -150, activityX = -120, activityY = 150;
//        int numActivity=0, numAgent=0;

        //FileWriter writer = new FileWriter("C:\\tmp\\bdm.txt");
        //FileWriter writer2 = new FileWriter("C:\\tmp\\reres2.txt");


        double agentX = 80, agentY = 80, activityX = 80, activityY = 80;
        int numActivity=0, numAgent=0;


//        try (BufferedWriter buffer = new BufferedWriter(writer)) {
//            buffer.write("Welcome to javaTpoint.");
//            buffer.newLine();
//
//
//            buffer.write("nodes to be shown");
//            for(int i=0;i<cytoVisProject.getMyControlPanel().nodesToBeShown.size();i++)
//            {
//                buffer.write(cytoVisProject.getMyControlPanel().nodesToBeShown.get(i).toString());
//                buffer.newLine();
//            }

            // Relocating entity
            networkView.getNodeView(entity).setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, entityX);
            networkView.getNodeView(entity).setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, entityY);

//            buffer.write("entityX --> "+String.valueOf(entityX));
//            buffer.write("  entityY --> "+String.valueOf(entityY));
//            buffer.write(" nodesToBeShown --> "+String.valueOf(cytoVisProject.getMyControlPanel().nodesToBeShown.size()));
//            buffer.newLine();

            // Relocating agents
            List<CyNode> agents = filter.findNodeNeighbors("agent", entity);

            for (CyNode node : agents) {
                String name = filter.findNodeName(node);

//                buffer.write("node -->"+name);
//                buffer.newLine();

                if (cytoVisProject.getMyControlPanel().nodesToBeShown.contains(name)) {
                    networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, agentX + entityX);
                    networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, agentY);

//                    buffer.write("agentX --> "+String.valueOf(agentX + entityX));
//                    buffer.write("   agentY --> "+String.valueOf(agentY));
//                    buffer.newLine();


                    agentX = agentX + 80;
                    numAgent += 1;
                }
            }

//            buffer.write("   numAgent --> "+String.valueOf(numAgent));
//            buffer.newLine();
            // Relocating activities
            for (CyNode node : activities) {
                String name = filter.findNodeName(node);

//                buffer.write("node -->"+name);
//                buffer.newLine();

                if (cytoVisProject.getMyControlPanel().nodesToBeShown.contains(name)) {
                    networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, activityX + entityX);
                    networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, activityY);

//                    buffer.write("activityX --> "+String.valueOf(activityX + entityX));
//                    buffer.write("   activityY --> "+String.valueOf(activityY));
//                    buffer.newLine();

                    activityX = activityX + 80;
                    numActivity += 1;
                }
            }

//            buffer.write("   numActivity --> "+String.valueOf(numActivity));
//            buffer.newLine();

            entityX = entityX + 80;

//            if (numActivity > numAgent) {
//                entityX = entityX + 80 + numActivity * 50; // 120 ve 80
//            } else {
//                entityX = entityX + 80 + numAgent * 50; //120 ve 80
//            }

//            buffer.write("son son entityX --> "+String.valueOf(entityX));
//            buffer.newLine();
//
//            buffer.close();
       // }


//        writer.close();

    }
    // This will remove nodes which does not have any relation with any entity
    public void removeIrrelevant(){
        double tmpX = 0;
        boolean flag;
        // Getting all agents in the network
        List<CyNode> nodes = filter.FilterRowByNodeType("agent","nodeType");
        for(CyNode node : nodes){

            List<CyNode> neighbors = network.getNeighborList(node, CyEdge.Type.ANY);
            flag = false;
            // If an agent does not have an neighbor which is in entity node type then remove it from the bottom side of network
            for(int i=0;i<neighbors.size();i++){
                if(filter.findNodeType(neighbors.get(i)) == "entity"){
                    flag = true;
                }
            }
            if(flag == false){
                networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, tmpX);
                networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, 500.0);
                tmpX = tmpX + 80;
            }
        }

        nodes = filter.FilterRowByNodeType("activity","nodeType");
        for(CyNode node : nodes){

            List<CyNode> neighbors = network.getNeighborList(node, CyEdge.Type.ANY);
            flag = false;
            // If an activity does not have an neighbor which is in entity node type then remove it from the bottom side of network
            for(int i=0;i<neighbors.size();i++){
                if(filter.findNodeType(neighbors.get(i)) == "entity"){
                    flag = true;
                }
            }
            if(flag == false){
                networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, tmpX);
                networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, 500.0);
                tmpX = tmpX + 80;
            }
        }
    }
    // This sorts dates and nodes at the same time
    public void sort(List<Date> dates,List<CyNode> allActivities){
        int i,min,j;
        Date tmp;
        CyNode tmp2;
        // It uses selection sort algorithm for sorting
        for (i=0;i<(dates.size()-1);i++){
            min=i;
            for(j=i+1;j<dates.size();j++){
                if(dates.get(min).compareTo(dates.get(j)) > 0){
                    tmp = dates.get(min);
                    dates.set(min,dates.get(j));
                    dates.set(j,tmp);

                    tmp2 = allActivities.get(min);
                    allActivities.set(min,allActivities.get(j));
                    allActivities.set(j,tmp2);
                }
            }
        }
    }
    // This method gets time from a column and removes null values from it.
    public List<String> getTimeFromColumn(CyColumn column){
        List<String> list = new ArrayList<>();
        if(column != null){
            list = column.getValues(String.class);
        }
        int i=0;
        while(i<list.size()){
            if(list.get(i) == null){
                list.remove(i);
            }else{
                i++;
            }
        }
        return list;
    }
}