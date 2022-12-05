package Action;

import App.CytoVisProject;
import Util.FilterUtil;
import Util.MathUtil;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

import static org.cytoscape.view.presentation.property.BasicVisualLexicon.*;

public class KMeansClustering {

    public MathUtil mathUtil;
    public CyNetwork network;
    public CyTable table;
    public CySwingAppAdapter adapter;
    public HashMap<String, CyNode> allNodes;
    public FilterUtil filter;
    public CyNetworkView networkView;
    public static int THRESHOLD = 10;

    public int openCluster = -1;
    String REFERENCE_STRING = "teststringformyalgorithm";

    ArrayList<ArrayList<String>> clusters;
    public ArrayList<HashMap<CyNode,Integer>> XPosHashMap;
    public ArrayList<HashMap<CyNode,Integer>> YPosHashMap;

    CytoVisProject cytoVisProject;

    HashMap<CyNode,Integer> localXHash;
    HashMap<CyNode,Integer> localYHash;

    public KMeansClustering(CytoVisProject cytoVisProject, CySwingAppAdapter adapter){
        this.mathUtil = new MathUtil();
        this.adapter  = adapter;

        // Getting necessary components from network
        CyApplicationManager manager = adapter.getCyApplicationManager();
        networkView = manager.getCurrentNetworkView();
        network = networkView.getModel();
        table = network.getDefaultNodeTable();
        this.cytoVisProject = cytoVisProject;

        // Get all nodes from Cytoscape node table
        filter = new FilterUtil(network, table, this.adapter);
        allNodes = filter.getAllNodesWithId();
    }

    // This method applies incremental K-Means clustering algorithm according to a max cluster count
    // @param maxClusterCount: Maximum number of clusters
    // @return clusters: name(id) of all nodes within each cluster
    public ArrayList<ArrayList<String>> applyKMeansClustering(int maxClusterCount) throws IOException {
        clusters = new ArrayList<>();              // Stores cluster names(names are actually represents id values)
        ArrayList<ArrayList<Integer>> clusterCentroids = new ArrayList<>();     // Numeric centroid values of clusters

        //applyInformationGain();
        try {
            int totalClusterCount = 0;  // total cluster count
            Iterator<String> keyIterator = allNodes.keySet().iterator();        // get all keys(names) from nodes
            while (keyIterator.hasNext()){
                String key = keyIterator.next();                                // extract key for current node
                Integer clusterIndex = doesNodeFitsInAnyCluster(clusterCentroids, key); // check if it fits to any cluster
                if(clusterIndex != null){
                    // if it is, then put it into that cluster
                    clusters.get(clusterIndex).add(key);
                    // recalculate cluster centroid for that cluster
                    ArrayList<Integer> newCentroid = reCalculateClusterCentroid(clusters.get(clusterIndex));
                    clusterCentroids.get(clusterIndex).clear();
                    clusterCentroids.get(clusterIndex).addAll(newCentroid);
                }else {
                    // if it does not fit into that cluster, then create a new one
                    clusters.add(new ArrayList<String>(){{add(key);}});
                    clusterCentroids.add(extractAttributeValues(key));
                    totalClusterCount++;

                    // if total cluster count exceeds the max number, then merge two neares cluster
                    if(totalClusterCount > maxClusterCount){
                        clusters = mergeNearestClusters(clusters, clusterCentroids);
                    }
                }

            }

        }catch (Exception e){
            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),e.getMessage() + " -- " + e.toString(),
                    "Error applyKMeansClustering ", JOptionPane.INFORMATION_MESSAGE);
        }

        // draw clusters in cytocape
       // drawClusters();
        return clusters;
    }

    // This method applies incremental K-Means clustering algorithm according to a max cluster count
    // @param maxClusterCount: Maximum number of clusters
    // @return clusters: name(id) of all nodes within each cluster
    public ArrayList<ArrayList<String>> applyKMeansClusteringByName(int maxClusterCount) throws IOException {
        clusters = new ArrayList<>();  // Stores cluster names(names are actually represents id values)
        //ArrayList<Integer> clusterCentroids = new ArrayList<>();    // Numeric centroid values of clusters
        ArrayList<String> clusterCentroidsStr = new ArrayList<>();

        //applyInformationGain();
        try {
            int totalClusterCount = 0;  // total cluster count
            Iterator<String> keyIterator = allNodes.keySet().iterator();        // get all keys(names) from nodes
            String key;

            // initialize cluster and cluster cendroids
            // tüm clusterlar ilklendirildi su an


//            FileWriter writer = new FileWriter("C:\\tmp\\resKNN.txt");
//            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"file opened ",
//                    "file mile", JOptionPane.INFORMATION_MESSAGE);

//            try (BufferedWriter buffer = new BufferedWriter(writer)) {
//                buffer.write("Cluster init -> maxClusterCount --> "+maxClusterCount);
//                buffer.newLine();

                for (int i = 0; i < maxClusterCount & keyIterator.hasNext(); i++) {
//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"here in i "+i,
//                            "file mile", JOptionPane.INFORMATION_MESSAGE);

                    key = keyIterator.next();

//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"here in key1 "+key,
//                            "file mile", JOptionPane.INFORMATION_MESSAGE);

                    clusters.add(i, new ArrayList<String>());
                    clusters.get(i).add(key);
//
//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"here in key2 "+key,
//                            "file mile", JOptionPane.INFORMATION_MESSAGE);

//                    int distance = levenshteinDistance(REFERENCE_STRING, key);
//                    buffer.write(" cluster i --> "+i+" key --> "+key+" distance --> "+distance);
//                    buffer.newLine();
//                    clusterCentroids.add(i, distance);

                    clusterCentroidsStr.add(i, key);

//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"here in key3 "+key,
//                            "file mile", JOptionPane.INFORMATION_MESSAGE);
//
//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," cluster i --> "+i+" key --> "+key+" clusterCendroid --> "+clusterCentroidsStr.get(i),
//                            "file mile", JOptionPane.INFORMATION_MESSAGE);

//                    buffer.write(" cluster i --> "+i+" key --> "+key+" clusterCendroid --> "+clusterCentroidsStr.get(i));
//                    buffer.newLine();

                }


                while (keyIterator.hasNext()) {
                    key = keyIterator.next();
//                    buffer.newLine();
//                    buffer.write(" in while key --> "+key);
//                    buffer.newLine(); // extract key for current node

                    //Integer clusterIndex = doesNodeFitsInAnyClusterByName(clusterCentroids, key,buffer); // check if it fits to any cluster
                    Integer clusterIndex = doesNodeFitsInAnyClusterByName2(clusterCentroidsStr, key,clusters);

//                    buffer.write(" clusterIndex --> "+clusterIndex);
//                    buffer.newLine();

                    if (clusterIndex != null) {
                        // if it is, then put it into that cluster

                        clusters.get(clusterIndex).add(key);

//                        ArrayList<String>  ss = new ArrayList<>();
//                        ss = clusters.get(clusterIndex);
//                        Collections.sort(ss);
//
//                        int size = ss.size();
//                        int median = size/2;
//
//                        buffer.write(" now size -> "+size+" median -> "+ median);
//                        buffer.newLine();
//
//                        clusterCentroidsStr.set(clusterIndex, ss.get(median));

//                        buffer.write(" clusterCendroid value -> "+clusterCentroids.get(clusterIndex)+" cluster size -> "+ clusters.get(clusterIndex).size()
//                        + " value --> "+value);
//                        buffer.newLine();
//
//                        clusterCentroids.set(clusterIndex,
//                                (value + levenshteinDistance(REFERENCE_STRING, key)) /
//                                        (clusters.get(clusterIndex).size() + 1));
//                        clusters.get(clusterIndex).add(key);

//                        buffer.write(" new size -> "+clusters.get(clusterIndex).size());
//                        buffer.newLine();
                    }
                }
//                buffer.close();
//                writer.close();
//            }
        }catch (Exception e1){
            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),e1.getMessage() + " -- " + e1.toString(),
                    "Error applyKMeansClustering 1 ", JOptionPane.INFORMATION_MESSAGE);
        }

        // draw clusters in cytocape
        //drawClusters();

        return clusters;
    }

    /**
     * This method is used to apply information gain for the dataset which is currently loaded into Cytoscape
     *
     * @return columns with high info gain
     * */
    public ArrayList<Integer> applyInformationGain(){
        // List of column indexes with high info gain
        ArrayList<Integer> columnsWithHighInfoGain = new ArrayList<>();

        // Creating Weka's necessary objects for info gain calculation
        AttributeSelection attributeSelection = new AttributeSelection();
        InfoGainAttributeEval infoGainAttributeEval = new InfoGainAttributeEval();
        Ranker search = new Ranker();
        attributeSelection.setEvaluator(infoGainAttributeEval);
        attributeSelection.setSearch(search);

        ArrayList<Attribute> attributes = new ArrayList<>();
        // There are 6 different classes, (max cluster count)
        ArrayList<String> classValues = new ArrayList<String>(){{
            add("1");
            add("2");
            add("3");
            add("4");
            add("5");
            add("6");
        }};

        // getting attributes from "table"(CyTable) object of Cytoscape.
        Set<String> attributeNames = table.getAllRows().get(1).getAllValues().keySet();
        // for each attribute, create an Attribute object of Weka with an empty list
        Iterator<String> attributeNamesIterator = attributeNames.iterator();
        while (attributeNamesIterator.hasNext()){
            attributes.add(new Attribute(attributeNamesIterator.next(), new ArrayList<>()));
        }
        // add class values?
        attributes.add(new Attribute("@@class@@", classValues));
        try {
            // create empty instances
            Instances dataSet = new Instances("DataInstances", attributes, 0);
            // fill instances object
            int rowCounter = 0;
            for(CyRow row : table.getAllRows()){
                // get all values for each row
                Map<String, Object> rowValue = row.getAllValues();
                double[] instanceValues = new double[dataSet.numAttributes()];
                for(Object rowElement : rowValue.values()){
                    // fill a double array with the values that collected above
                    instanceValues[rowCounter] = dataSet.attribute(rowCounter).addStringValue(rowElement == null ? (String) null : rowElement.toString());
                    rowCounter++;
                }
                // add a new instance to instances object
                dataSet.add(new DenseInstance(1.0, instanceValues));
                rowCounter = 0;
            }
            // select attributes according to info gain
            attributeSelection.SelectAttributes(dataSet);

            // finally, rank them
            double[][] attrRanks = attributeSelection.rankedAttributes();
        }catch (Exception e ){
            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
                    e.getMessage() + " - " + e.toString(),
                    "Error applyInformationGain!", JOptionPane.INFORMATION_MESSAGE);
        }

        return columnsWithHighInfoGain;
    }

    // This method is used to calculate centroid of a given cluster
    // @param cluster: Contains names of elements of that cluster
    // @return newCentroid: Numeric centroid values of given cluster
    public ArrayList<Integer> reCalculateClusterCentroid(ArrayList<String> cluster){
        ArrayList<Integer> newCentroid = new ArrayList<>();

        try {
            // for every node of given cluster
            for (int j=0; j<cluster.size(); j++){
                // extract numeric attribute values
                ArrayList<Integer> attributeValues = extractAttributeValues(cluster.get(j));
                // for the first node, divide values to size and add it to new centroid array
                if(newCentroid.size() == 0){
                    // for each attribute, divide value to size and add it to it's place
                    for (int k=0; k<attributeValues.size(); k++){
                        newCentroid.add((attributeValues.get(k)/cluster.size()));
                    }
                }else {
                    // for other nodes than the first one, add it to the current value after dividing them to size
                    for (int k=0; k<attributeValues.size(); k++){
                        newCentroid.set(k, newCentroid.get(k) + (attributeValues.get(k)/cluster.size()));
                    }
                }
            }

        }catch (Exception e){
            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),e.getMessage() + " -- " + e.toString(),
                    "recal", JOptionPane.INFORMATION_MESSAGE);
        }

        return newCentroid;
    }

    // This method is used to find nearest clusters according to their attributes
    // @param clusters: Name list of all nodes of clusters
    // @param clusterCentroids: Numeric values of cluster centroids
    public ArrayList<ArrayList<String>> mergeNearestClusters(ArrayList<ArrayList<String>> clusters, ArrayList<ArrayList<Integer>> clusterCentroids){

        int minDistance = Integer.MAX_VALUE;    // minimum distance between two cluster
        int cluster1 = -1;                      // index of first cluster
        int cluster2 = -1;                      // index of second cluster

        try {
            // find min distance between clusters by comparing them all
            for (int i=0; i<clusterCentroids.size()-1; i++){
                for (int j=i+1; j<clusterCentroids.size(); j++){
                    // find distance between current clusters
                    int tempDistance = findDistanceBetweenClusters(clusterCentroids.get(i), clusterCentroids.get(j));
                    if(tempDistance < minDistance){
                        // save indexes if they are the nearest
                        minDistance = tempDistance;
                        cluster1 = i;
                        cluster2 = j;
                    }
                }
            }

            if(cluster1 != -1){
                // combine two clusters and their centroids
                clusters.get(cluster1).addAll(clusters.get(cluster2));
                clusters.remove(cluster2);

                ArrayList<Integer> attributes = new ArrayList<Integer>();
                attributes.addAll((clusterCentroids.get(cluster1)));

                for (int i=0; i<attributes.size(); i++){
                    attributes.set(i, (attributes.get(i) + clusterCentroids.get(cluster2).get(i))/2);
                }

                clusterCentroids.get(cluster1).clear();
                clusterCentroids.get(cluster1).addAll(attributes);
                clusterCentroids.remove(cluster2);
            }

        }catch (Exception e){
            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),e.getMessage() + " -- " + e.toString(),
                    "merge", JOptionPane.INFORMATION_MESSAGE);
        }

        return clusters;
    }

    // this method is used to find distance between two clusters
    // @param c1: first cluster
    // @param c2: second cluster
    // @return distance: Distance between them
    public int findDistanceBetweenClusters(ArrayList<Integer> c1, ArrayList<Integer> c2){
        int distance = 0;

        try {
            // sum all attribute values
            for(int i=0; i<c1.size(); i++){
                distance += Math.abs(c1.get(i) - c2.get(i));
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),e.getMessage() + " -- " + e.toString(),
                    "distance", JOptionPane.INFORMATION_MESSAGE);
        }

        return distance;
    }

    // This method is used to find if given node fits two any of clusters
    // @param clusterCentroids: Centroid value of clusters
    // @param key: Name of cluster
    // @return clusterIndex: Index of cluster which the given node fits best
    public Integer doesNodeFitsInAnyCluster(ArrayList<ArrayList<Integer>> clusterCentroids, String key){
        Integer minDistance = Integer.MAX_VALUE;                            // min distance between given node and clusters
        Integer clusterIndex = null;
        // index of that cluster

        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," doesNodeFits " +key ,
                "doesfits", JOptionPane.INFORMATION_MESSAGE);

        ArrayList<Integer> attributeValues = extractAttributeValues(key);   // extraxt values of given node
        try {
            // for all clusters
            for(int j=0; j<clusterCentroids.size(); j++){
                ArrayList<Integer> clusterCentroid = clusterCentroids.get(j);       // get cluster centroid

                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," for cluster " + String.valueOf(j) ,
                        "doesfits", JOptionPane.INFORMATION_MESSAGE);

                // find distance between given node and current cluster
                int tempDistance = 0;
                for (int i=0; i<clusterCentroid.size(); i++){
                    tempDistance += Math.abs(clusterCentroid.get(i) - attributeValues.get(i));
                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," clusterCendroid[i] " + String.valueOf(clusterCentroid.get(i)) ,
                            "df", JOptionPane.INFORMATION_MESSAGE);

                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," attrValues[i] " + String.valueOf(attributeValues.get(i)) ,
                            "df", JOptionPane.INFORMATION_MESSAGE);

                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," tempDistance " + String.valueOf(tempDistance) ,
                            "df", JOptionPane.INFORMATION_MESSAGE);

                }

                // save distance and index if it is the nearest one for now
                if(tempDistance < minDistance){
                    minDistance = tempDistance;
                    clusterIndex = j;
                }
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"doesifts: " + e.toString() + " " + e.getMessage(),
                    "doesfits", JOptionPane.INFORMATION_MESSAGE);
        }

        if(minDistance > THRESHOLD){
            clusterIndex = null;
        }
        return clusterIndex;
    }

    public int levenshteinDistance (String lhs, String rhs) {
        int len0 = lhs.length() + 1;
        int len1 = rhs.length() + 1;

        // the array of distances
        int[] cost = new int[len0];
        int[] newcost = new int[len0];

        // initial cost of skipping prefix in String s0
        for (int i = 0; i < len0; i++) cost[i] = i;

        // dynamically computing the array of distances

        // transformation cost for each letter in s1
        for (int j = 1; j < len1; j++) {
            // initial cost of skipping prefix in String s1
            newcost[0] = j;

            // transformation cost for each letter in s0
            for(int i = 1; i < len0; i++) {
                // matching current letters in both strings
                int match = (lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1;

                // computing cost for each transformation
                int cost_replace = cost[i - 1] + match;
                int cost_insert  = cost[i] + 1;
                int cost_delete  = newcost[i - 1] + 1;

                // keep minimum cost
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
            }

            // swap cost/newcost arrays
            int[] swap = cost; cost = newcost; newcost = swap;
        }

        // the distance is the cost for transforming all letters in both strings
        return cost[len0 - 1];
    }
    // This method is used to find if given node fits two any of clusters
    // @param clusterCentroids: Centroid value of clusters
    // @param key: Name of cluster
    // @return clusterIndex: Index of cluster which the given node fits best
    public Integer doesNodeFitsInAnyClusterByName(ArrayList<Integer> clusterCentroids, String key, BufferedWriter bf){
        Integer minDistance = Integer.MAX_VALUE;                            // min distance between given node and clusters
        Integer clusterIndex = null;
        // index of that cluster

//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," doesNodeFits " +key ,
//                "doesfits", JOptionPane.INFORMATION_MESSAGE);

        String clusterValueOfNode = key;

        try {
            // for all clusters
            int similarity = levenshteinDistance(REFERENCE_STRING,key);

            bf.newLine();
            bf.write(" similarity is  --> "+similarity);
            bf.newLine();
            int tempDistance = 0;
            for(int j=0; j<clusterCentroids.size(); j++){

                tempDistance = Math.abs(clusterCentroids.get(j) - similarity);

//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," for cluster " + String.valueOf(j) ,
//                        "doesfits", JOptionPane.INFORMATION_MESSAGE);
//
//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," clusterCendroid[i] " + String.valueOf(clusterCentroid.get(i)) ,
//                        "df", JOptionPane.INFORMATION_MESSAGE);
//
//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," attrValues[i] " + String.valueOf(attributeValues.get(i)) ,
//                        "df", JOptionPane.INFORMATION_MESSAGE);
//
//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," similarity --> "+similarity+" for cluster " + String.valueOf(j) ,
//                        "df", JOptionPane.INFORMATION_MESSAGE);

                // save distance and index if it is the nearest one for now

                bf.write("cluster --> "+j+" tempDistance   --> "+tempDistance);
                if(tempDistance < minDistance){
                    minDistance = tempDistance;
                    clusterIndex = j;

                    bf.write("cluster has been changed to --> "+j);

//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," final cluster " + String.valueOf(j) ,
//                            "df", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            bf.newLine();
        }catch (Exception e){
            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"doesifts: " + e.toString() + " " + e.getMessage(),
                    "doesfits", JOptionPane.INFORMATION_MESSAGE);
        }
//        if(minDistance > THRESHOLD){
//            clusterIndex = null;
//        }

        return clusterIndex;
    }

    public Integer doesNodeFitsInAnyClusterByName2(ArrayList<String> clusterCentroids, String key,ArrayList<ArrayList<String>> clusters){
        Integer minDistance = Integer.MAX_VALUE;                            // min distance between given node and clusters
        Integer clusterIndex = null;
        // index of that cluster

//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," doesNodeFits " +key ,
//                "doesfits", JOptionPane.INFORMATION_MESSAGE);

        String clusterValueOfNode = key;

        try {
            // for all clusters
           // int similarity = levenshteinDistance(REFERENCE_STRING,key);

            int tempDistance = 0;
//            bf.newLine();
            for(int clusterindex=0; clusterindex<clusters.size(); clusterindex++){

                tempDistance = 0;
                ArrayList<String> currentCluster = clusters.get(clusterindex);
                for (int clusterElem=0; clusterElem<currentCluster.size();clusterElem++)
                {
//                    bf.write("**"+currentCluster.get(clusterElem)+" ");
                    tempDistance += levenshteinDistance(currentCluster.get(clusterElem),key);
                }
//                bf.newLine();
//                bf.write(" cluster --> "+clusterindex+" tempDistance "+tempDistance);
//                bf.newLine();

                //tempDistance = levenshteinDistance(clusterCentroids.get(j),key);

                if(tempDistance < minDistance){
                    minDistance = tempDistance;
                    clusterIndex = clusterindex;

                    // bf.write("cluster has been changed to --> "+j);

//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," final cluster " + String.valueOf(j) ,
//                            "df", JOptionPane.INFORMATION_MESSAGE);
                }

               // tempDistance = Math.abs(clusterCentroids.get(j) - similarity);

//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," for cluster " + String.valueOf(j) ,
//                        "doesfits", JOptionPane.INFORMATION_MESSAGE);
//
//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," clusterCendroid[i] " + String.valueOf(clusterCentroid.get(i)) ,
//                        "df", JOptionPane.INFORMATION_MESSAGE);
//
//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," attrValues[i] " + String.valueOf(attributeValues.get(i)) ,
//                        "df", JOptionPane.INFORMATION_MESSAGE);
//
//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," similarity --> "+similarity+" for cluster " + String.valueOf(j) ,
//                        "df", JOptionPane.INFORMATION_MESSAGE);

                // save distance and index if it is the nearest one for now

//                if(tempDistance < minDistance){
//                    minDistance = tempDistance;
//                    clusterIndex = j;
//
//                   // bf.write("cluster has been changed to --> "+j);
//
////                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," final cluster " + String.valueOf(j) ,
////                            "df", JOptionPane.INFORMATION_MESSAGE);
//                }
            }
//            bf.newLine();
        }catch (Exception e){
            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"doesifts: " + e.toString() + " " + e.getMessage(),
                    "doesfits", JOptionPane.INFORMATION_MESSAGE);
        }
//        if(minDistance > THRESHOLD){
//            clusterIndex = null;
//        }

        return clusterIndex;
    }



    // This method is used to extract attributes of a given node
    // @param node: Name of node
    // @return attributeValues: Numeric attribute values of given node
    public ArrayList<Integer> extractAttributeValues(String node){
        ArrayList<Integer> attributeValues = new ArrayList<>();

        try {
            Set<String> attributeNames = table.getAllRows().get(1).getAllValues().keySet();     // Get all attribute names
            Iterator<String> attributeIterator = attributeNames.iterator();                     // Create an iterator for names
            while (attributeIterator.hasNext()){
                String attributeName = attributeIterator.next();                                // Get current attribute name
                if(attributeName.startsWith("*")){                                              // If it starts with *, then this means that it is a numerical attribute
                    int value = (filter.getValueById(node, "name", attributeName) == null ? 0 :         // get value of that attribute
                            Integer.parseInt(filter.getValueById(node, "name", attributeName).toString())) ;
                    attributeValues.add(value);
                }
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"doesifts: " + e.toString() + " " + e.getMessage(),
                    "extract", JOptionPane.INFORMATION_MESSAGE);
        }

        return attributeValues;
    }

    // This method is used to draw new clusters on Cytoscape
    // @param clusters: Id of all nodes of all clusters
    public void drawClusters( ArrayList<ArrayList<String>> clusters) throws IOException {
        int x = 0;      // x coordinate
        int y = 0;      // y coordinate


//        FileWriter writer = new FileWriter("C:\\tmp\\res.txt");
//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"file opened ",
//                "file mile", JOptionPane.INFORMATION_MESSAGE);
//        try (BufferedWriter buffer = new BufferedWriter(writer)) {
//            buffer.write("Welcome to javaTpoint.");
//            buffer.newLine();
            int xBase = 0;
            int yBase = 0;

            XPosHashMap = new ArrayList<>();
            YPosHashMap = new ArrayList<>();

//            buffer.write("cluster sayisi -->"+String.valueOf(clusters.size()));
//            buffer.newLine();

            int loc = (clusters.size() / 2) * 500;


            networkView.setVisualProperty(NETWORK_CENTER_X_LOCATION, loc); //700 //100
            networkView.setVisualProperty(NETWORK_CENTER_Y_LOCATION, loc); //600 //100
            networkView.setVisualProperty(NETWORK_SCALE_FACTOR, 0.28);

            for (int i = 0; i < clusters.size(); i++) {
//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"cluster -> "+String.valueOf(i),
//                        "file mile", JOptionPane.INFORMATION_MESSAGE);
                localXHash = new HashMap<CyNode, Integer>();
                localYHash = new HashMap<CyNode, Integer>();

//                buffer.write(" cluster "+ String.valueOf(i));
//                buffer.newLine();

                for (int j = 0; j < clusters.get(i).size(); j++) {

//                    buffer.write(" x --> "+x+" y --> "+y);
//                    buffer.write(" value --> "+clusters.get(i).get(j));
//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"cluster 2-> "+String.valueOf(i),
//                            "file mile", JOptionPane.INFORMATION_MESSAGE);

                    CyNode node = allNodes.get(clusters.get(i).get(j));

                    // update location of current node
                    networkView.getNodeView(allNodes.get(clusters.get(i).get(j))).setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, x);
                    networkView.getNodeView(allNodes.get(clusters.get(i).get(j))).setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, y);
                    // add random digits to current coordinates

//                    x += (new Random().nextInt(10)); //5
//                    y += (new Random().nextInt(10)); //5
//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"cluster 3-> "+String.valueOf(i),
//                            "file mile", JOptionPane.INFORMATION_MESSAGE);
                    localXHash.put(node,x);
                    localYHash.put(node,y);


                    x = (new Random().nextInt(50)); //5 // 200 //100
                    y = (new Random().nextInt(50)); //5 //200  //100

                    //x = x * 2;
                    //y = y * 2;

                    x += xBase;
                    y += yBase;

//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"cluster4 -> "+String.valueOf(i),
//                            "file mile", JOptionPane.INFORMATION_MESSAGE);
//                    buffer.newLine();

                }

                XPosHashMap.add(i,localXHash);
                YPosHashMap.add(i,localYHash);

//                buffer.newLine();

                // after visualizing a cluster, leave a huge space
                if (i != (clusters.size() - 1)) {
                    //x += 500; //500
                    //y += 500; //500

                    xBase += 600; //500 //350
                    yBase += 600; //500 //350
                }

                networkView.updateView();
            }
//            buffer.close();
//        }
//        writer.close();
    }

    public void reDrawClusters(ArrayList<ArrayList<String>> iClusters, CyNode iNode) throws IOException {
        int x = 0;      // x coordinate
        int y = 0;      // y coordinate


//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"file opened ",
//                "file mile", JOptionPane.INFORMATION_MESSAGE);

//        FileWriter writer = new FileWriter("C:\\tmp\\reres.txt");
//        //FileWriter writer2 = new FileWriter("C:\\tmp\\reres2.txt");

//        try (BufferedWriter buffer = new BufferedWriter(writer)) {
//            buffer.write("Welcome to javaTpoint.");
//            buffer.newLine();

            double newScale = 0.9; //networkView.getVisualProperty(NETWORK_SCALE_FACTOR).doubleValue() * scale;

            //1-> 200,200
            //2-> 600,600
            //3-> 1000,1000
            //4-> 1400,1400


            int whichCluster = whichClusterIAmIn(iNode);

            int xLoc = 200 + (whichCluster*500);
            int yLoc = 200 + (whichCluster*500);

            networkView.setVisualProperty(NETWORK_SCALE_FACTOR, newScale);
            networkView.setVisualProperty(NETWORK_CENTER_X_LOCATION, xLoc);
            networkView.setVisualProperty(NETWORK_CENTER_Y_LOCATION, yLoc);


//            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"which cluster -->  "+String.valueOf(whichCluster),
//                "file mile", JOptionPane.INFORMATION_MESSAGE);

            if( -1 != whichCluster)
            {
                extendCluster(whichCluster,iClusters);
                openCluster = whichCluster;
            }

//            if( -1 != openCluster)
//            {
//
//            }

//            if(openCluster != -1)
//            {
//               JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), "",
//               "Info!", JOptionPane.INFORMATION_MESSAGE);
//
//                //undoClusterExtension(openCluster,buffer,iClusters);
//                for (int j = 0; j < iClusters.get(openCluster).size(); j++)
//                {
//                    CyNode node = allNodes.get(iClusters.get(openCluster).get(j));
//
//                    int xx = localXHash.get(node);
//                    int yy = localYHash.get(node);
//
//                    networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, xx);
//                    networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, yy);
//
//                }
//            }


//            for (int i = 0; i < iClusters.size(); i++) {
//
//                buffer.write(" cluster --> "+String.valueOf(i));
//                buffer.newLine();
//
//                // This method is for finding node type of a node
//                String name = filter.findNodeName(iNode);
//
////                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), "selected name -->  "+name,
////                        "Info!", JOptionPane.INFORMATION_MESSAGE);
//
//                if (iClusters.get(i).contains(name))
//                {
//                    extendCluster(i,buffer,iClusters);
//                    openCluster = i;
//                }
//            }

//            buffer.close();
//        }



        //this.cytoVisProject.getNodeSelectedListener2().setFlag(false);

//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"closeClusterFlag set to False ..!",
//                "Info!", JOptionPane.INFORMATION_MESSAGE);
//        writer.close();
    }

    public int whichClusterIAmIn( CyNode pNode)
    {
        int ret = -1;
        String name = filter.findNodeName(pNode);
        for(int i = 0; i<clusters.size()& ret == -1;i++)
        {
            if( clusters.get(i).contains(name))
                ret = i;
        }

        return ret;
    }

    void extendCluster(int whichCluster,ArrayList<ArrayList<String>> iClusters) throws IOException {

/*        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), "in cluster 1-->  "+String.valueOf(i),
                "Info!", JOptionPane.INFORMATION_MESSAGE);*/

        localXHash = XPosHashMap.get(whichCluster);
        localYHash = YPosHashMap.get(whichCluster);

//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), "in cluster 2-->  "+String.valueOf(i),
//                           "Info!", JOptionPane.INFORMATION_MESSAGE);

        //new implement

        int maxSize = 480;
        int cellSize = 60;

        int rowSize= maxSize / cellSize;
        int columnSize = rowSize;

//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), "in cluster 2.1-->  "+String.valueOf(i),
//                "Info!", JOptionPane.INFORMATION_MESSAGE);

        int unit = rowSize*columnSize;

        ArrayList<Boolean> rooms = new ArrayList<Boolean>();

//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), "in cluster 2.2-->  unit -> "+String.valueOf(unit),
//                "Info!", JOptionPane.INFORMATION_MESSAGE);

//        buffer.write("unit -> "+String.valueOf(unit));
//        buffer.newLine();

        for(int ii=0;ii<unit;ii++)
        {
            rooms.add(ii,false);
            //buffer.write("added -> "+String.valueOf(ii));
        }

//        buffer.newLine();

//        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), "in cluster 3-->  "+String.valueOf(i),
//                "Info!", JOptionPane.INFORMATION_MESSAGE);

        for (int j = 0; j < iClusters.get(whichCluster).size(); j++) {

            int aa = (new Random().nextInt(unit));
//            buffer.write("name -> "+iClusters.get(whichCluster).get(j));
//            buffer.newLine();

            while(rooms.get(aa) != false)
            {
                aa = (new Random().nextInt(unit));
//                buffer.write("in while -> "+String.valueOf(aa));
            }
//            buffer.newLine();
//            buffer.write("available room -> "+String.valueOf(aa));
            rooms.set(aa,true);

            if(aa < rowSize)
                aa +=rowSize;

            int availableYCell = aa / rowSize;
            int availableXCell = aa % columnSize;

//            buffer.write(" availableXCell -> "+String.valueOf(availableXCell));
//            buffer.write(" availableYCell -> "+String.valueOf(availableYCell));

            int availableY = availableYCell * cellSize;
            int availableX;

            if( 0 == availableXCell)
            {
                availableX = (columnSize-1)* cellSize;
                availableY = (availableYCell-1)*cellSize;
            }
            else
            {
                availableX = (availableXCell-1) * cellSize;
            }


            availableX += 500*whichCluster;
            availableY += 500*whichCluster;

//            buffer.write(" availableX -> "+String.valueOf(availableX));
//            buffer.write(" availableY -> "+String.valueOf(availableY));
//
//            buffer.newLine();

            CyNode node = allNodes.get(iClusters.get(whichCluster).get(j));

            int xx= availableX;
            int yy = availableY;
//
//                        int step = 20;
//                        int xx = localXHash.get(node);
//                        int yy = localYHash.get(node);
//
//                        xx = ((xx / step) + 1) * step;
//                        yy = ((yy / step) + 1) * step;
//
//
//                        int xNew = (new Random().nextInt(200)); //5 // 200
//                        int yNew = (new Random().nextInt(200)); //5 //200
//
//                        //buffer.write("value --> " + iClusters.get(i).get(j) + " xx was --> " + String.valueOf(xx) + " yy was --> " + String.valueOf(yy));
//
//                        xx *= 2;
//                        yy *= 2;
//
//                        xx += xNew;
//                        yy += yNew;
//
//                        xx *= 3;
//                        yy *= 3;


            networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, xx);
            networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, yy);

//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), "here here3 xx --> "+String.valueOf(xx),
//                            "Info!", JOptionPane.INFORMATION_MESSAGE);

//                    for(int i=0;i<allNodes.size();i++){
//                        view = cyNetworkView.getNodeView(allNodes.get(i));
//                        x = view.getVisualProperty(BasicVisualLexicon.NODE_X_LOCATION);
//                        y = view.getVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION);
//                        XposArray.add(i,x);
//                        YposArray.add(i,y);
//                    }


//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), "here here4 --> ",
//                            "Info!", JOptionPane.INFORMATION_MESSAGE);
//
//                    Double yy = networkView.getNodeView(allNodes.get(iClusters.get(i).get(j))).getVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION);
//

//
//                    xx = 10 * xx;
//                    yy = 10 * yy;
//
//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), "here here3 --> ",
//                            "Info!", JOptionPane.INFORMATION_MESSAGE);
//
//                    // update location of current node
//                    networkView.getNodeView(allNodes.get(iClusters.get(i).get(j))).setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, xx);
//                    networkView.getNodeView(allNodes.get(iClusters.get(i).get(j))).setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, yy);
//
            // buffer.newLine();
            // buffer.write(" --> new xx  --> " + String.valueOf(networkView.getNodeView(allNodes.get(iClusters.get(i).get(j))).getVisualProperty(BasicVisualLexicon.NODE_X_LOCATION))
            //        + " new yy --> " + String.valueOf(networkView.getNodeView(allNodes.get(iClusters.get(i).get(j))).getVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION)));
            //buffer.newLine();
//
//
//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), "here here4 --> ",
//                            "Info!", JOptionPane.INFORMATION_MESSAGE);

            // add random digits to current coordinates

        }
        networkView.updateView();
    }

    public void undoCluster(int whichCluster)
    {
        HashMap<CyNode,Integer> localXHash = XPosHashMap.get(whichCluster);
        HashMap<CyNode,Integer> localYHash = YPosHashMap.get(whichCluster);

        for (int whichNode = 0; whichNode < clusters.get(whichCluster).size(); whichNode++) {

            CyNode node = allNodes.get(clusters.get(whichCluster).get(whichNode));

            // update location of current node
            networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, localXHash.get(node));
            networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, localYHash.get(node));
        }
        networkView.updateView();
    }

}



