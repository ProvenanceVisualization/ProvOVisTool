package Util;

import App.CytoVisProject;
import org.cytoscape.app.swing.CySwingAppAdapter;
import Action.ImportNodesAction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class EnhancedVersionOfFDM {

    private HashMap<String, ArrayList<String>>  stateCurrent;

    public HashMap<String, String> getStateActivityAgent() {
        return stateActivityAgent;
    }

    private HashMap<String, String>  stateActivityAgent;
    private HashMap<String, ArrayList<String>>  statePurge;
    private HashMap<String, String>             varIdToNodeIdCurrent;
    private HashMap<String, String>             varIdToNodeIdPurge;
    private Integer                             uniqueNodeId;
    private Boolean                             doesFilterApplied;
    private ArrayList<String>                   filterNode;
    private ArrayList<String>                   selectedNodeIdList;

    FileWriter fr;
    //FileWriter frNew;
    BufferedWriter br;
    //BufferedWriter brNew;

    private CySwingAppAdapter adapter;
    private CytoVisProject cytoVisProject;

    private ImportNodesAction imp;

    private FilterUtil filterUtil;

    public EnhancedVersionOfFDM(CytoVisProject pCytoVisProject) throws IOException {
        stateCurrent            = new HashMap<>();
        stateActivityAgent        = new HashMap<>();
        statePurge              = new HashMap<>();
        varIdToNodeIdCurrent    = new HashMap<>();
        varIdToNodeIdPurge      = new HashMap<>();
        uniqueNodeId            = new Integer(0);

        doesFilterApplied       = false;
        filterNode              = new ArrayList<>();
        cytoVisProject = pCytoVisProject;
        adapter = cytoVisProject.getAdapter();

        filterUtil  = new FilterUtil(adapter.getCyApplicationManager().getCurrentNetwork(), adapter.getCyApplicationManager().getCurrentTable(), adapter);

        //File file1 = new File("C:\\yazma.txt");
        fr = null;
        //frNew =null;
        try {
            fr = new FileWriter("C:\\tmp\\fdmUpdateState.txt", false);
            //frNew = new FileWriter("C:\\tmp\\fdmNew.txt", false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //fr = new FileWriter(file1, true);
        br = new BufferedWriter(fr);

        br.write("First Line in the file");
        br.newLine();
        //brNew = new BufferedWriter(frNew);

    }

    public void printMatrix(HashMap<String, ArrayList<String>> matrix){
        for(String row : matrix.keySet()){
            for(String cell : matrix.get(row)){
                System.out.print(cell + " ");
            }
            System.out.print("\n");
        }

        System.out.println("--------------------------------------------------------------------------------------------------------------------");
    }

    // @param element: A prov-o notification (an edge)
    // @param node1Id: Node Id of source node
    // @param node2Id: Node Id of dest node

    public void updateState(String sourceNode, String destNode, String edgeType) throws IOException {
        ArrayList<String> inputVars;
        String node1Id;
        String node2Id;

        br.write("  source--> "+sourceNode+"  dest--> "+destNode+"  edgeType--> "+edgeType);
        br.newLine();

        if(varIdToNodeIdCurrent.containsKey(sourceNode)){
            node1Id = varIdToNodeIdCurrent.get(sourceNode);
        }else {
            node1Id = (uniqueNodeId++).toString();
        }

        if(varIdToNodeIdCurrent.containsKey(destNode)){
            node2Id = varIdToNodeIdCurrent.get(destNode);
        }else {
            node2Id = (uniqueNodeId++).toString();
        }

        if(varIdToNodeIdCurrent.containsKey(sourceNode) && varIdToNodeIdCurrent.get(sourceNode) != node1Id){
            Object[] keyList = stateCurrent.keySet().toArray();
            for(int i=0; i<stateCurrent.size(); i++){
                if(keyList[i].toString().equals(sourceNode)){
                    cacheDependencies(keyList[i].toString());
                    stateCurrent.remove(keyList[i]);
                    break;
                }
            }
        }

        if(filterUtil.isEdgeTypeAssociation(edgeType))
        {
           if(!stateActivityAgent.containsKey(destNode))
           {
               stateActivityAgent.put(destNode,sourceNode);
               br.write("added to the stateActivityAgent");
               br.newLine();
           }
           else
           {
               br.write("ALERTTTTTTTT");
               br.newLine();
           }
        }


        // add new mapping sourceNode --> node1Id
        varIdToNodeIdCurrent.put(sourceNode, node1Id);

        // If source node is not in the matrix, add it to the matrix and also to rows list
        if(!stateCurrent.keySet().contains(sourceNode)){
//            br.write(" source node --> "+sourceNode);
//            br.newLine();
//            br.write("**** first dest node added--> "+destNode);
//            br.newLine();
            stateCurrent.put(sourceNode, new ArrayList<String>() {{ add(destNode); }});
//            br.newLine();
        }else {
//            br.write(" **** appended source node --> "+sourceNode+" destNode --> "+destNode);
//            br.newLine();
            stateCurrent.get(sourceNode).add(destNode);
        }


        // If source node equals to destNode then get backward provenance from state.purge, else get it from state.current
        if(sourceNode.equals(destNode)){
            inputVars = getForwardProvenance(destNode, statePurge, new ArrayList<>(),false, br);
        }else {
            inputVars = getForwardProvenance(destNode, stateCurrent, new ArrayList<>(),false,br);
        }

        // Update backward provenance of source node in state.current
        stateCurrent.get(sourceNode).addAll(inputVars);

        //br.write(" added input vars");
//        br.newLine();
//        br.newLine();

//        for(String nodeVar:inputVars)
//        {
//            br.write(nodeVar+" ");
//        }
//
//        br.newLine();

    }

    public void updateAgentState()
    {

    }

    public ArrayList<String> getForwardProvenance(String varId, HashMap<String, ArrayList<String>> matrix, ArrayList<String> resultList, boolean debugActivate, BufferedWriter brNew) throws IOException {

//        brNew.write("********************* FDM getting dependencies **************************");
//        brNew.newLine();
//        brNew.write("  fdm for -> "+varId);
//        brNew.newLine();
//

        if(!matrix.containsKey(varId)){
//            brNew.write(" fdm return list ");
//            brNew.newLine();
            return resultList;
        }else {
            for(String varItem : matrix.get(varId)){
                if(!resultList.contains(varItem)){
//                    brNew.write(" fdm dependency --> "+varItem);
//                    brNew.newLine();
                    resultList.add(varItem);
                    resultList = getForwardProvenance(varItem, matrix, resultList,debugActivate,brNew);
                }
            }
        }

        return resultList;
    }


    // Cache dependencies that removed from state.current
    public void cacheDependencies(String rowToCash){
        statePurge.put(rowToCash, stateCurrent.get(rowToCash));
    }

    public HashMap<String, String> getVarIdToNodeIdCurrent() {
        return varIdToNodeIdCurrent;
    }

    public void setVarIdToNodeIdCurrent(HashMap<String, String> varIdToNodeIdCurrent) {
        this.varIdToNodeIdCurrent = varIdToNodeIdCurrent;
    }

    public HashMap<String, String> getVarIdToNodeIdPurge() {
        return varIdToNodeIdPurge;
    }

    public void setVarIdToNodeIdPurge(HashMap<String, String> varIdToNodeIdPurge) {
        this.varIdToNodeIdPurge = varIdToNodeIdPurge;
    }

    public HashMap<String, ArrayList<String>> getStateCurrent() {
        return stateCurrent;
    }

    public void setStateCurrent(HashMap<String, ArrayList<String>> stateCurrent) {
        this.stateCurrent = stateCurrent;
    }

    public HashMap<String, ArrayList<String>> getStatePurge() {
        return statePurge;
    }

    public void setStatePurge(HashMap<String, ArrayList<String>> statePurge) {
        this.statePurge = statePurge;
    }

    public Boolean getDoesFilterApplied() {
        return doesFilterApplied;
    }

    public void setDoesFilterApplied(Boolean doesFilterApplied) {
        this.doesFilterApplied = doesFilterApplied;
    }

    public ArrayList<String> getFilterNode() {
        return filterNode;
    }

    public void setFilterNode(ArrayList<String> filterNode) {
        this.filterNode = filterNode;
    }

    public ArrayList<String> getSelectedNodeIdList() {
        return selectedNodeIdList;
    }

    public void setSelectedNodeIdList(ArrayList<String> selectedNodeIdList) {
        this.selectedNodeIdList = selectedNodeIdList;
    }

    public void closeFile() throws IOException {
        br.close();
        fr.close();
    }


    public void closeTheFile() throws IOException {
        closeFile();
    }
}
