package App;

import Base.*;
import Action.*;
import Util.*;

import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.model.*;
import org.cytoscape.task.write.ExportTableTaskFactory;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskManager;
/*import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;*/

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class MyControlPanel extends JPanel implements CytoPanelComponent{

    // Variables and Tools

    private ProvoImportCore         provoImportCore;
    private CySwingAppAdapter       adapter;
    private CytoVisProject          cytoVisProject;
    private SliderVisualization     sliderVisualization;
    private CompareGraphsCore       compareGraphsCore;
    public  DrawComparedGraphs      drawComparedGraphs;
    private EnhancedVersionOfBDM    enhancedVersionOfBDM;
    private EnhancedVersionOfFDM    enhancedVersionOfFDM;
    private BackwardDependency      backwardDependency;
    private ForwardDependency       forwardDependency;
    private NetworkViewOrganizer    networkViewOrganizer;
    public VisStyleUtil visStyleUtil;
    RecreateNetworkAction recreateNetworkAction;
    public KMeansClustering kMeansClustering;

    private List<String> nodeTypes;
    private JSlider slider;
    private boolean sliderStop;
    private Double cytoPanelHeight;
    private Double cytoPanelWidth;
    private Double mainPanelHeight;
    private Double mainPanelWidth;
    private Double visualStyleTemplatePanelWidth;
    private Integer maxNode;

    private JButton showHideRelationButton;
    private JButton entityBasedSorting;
    public  JButton importVisStyleButton;
    public  JButton importNetworkButton;
    public  JButton importTableButton;
    private JButton extractFilesButton;
    private JButton closeButton;
    private JButton helpButton;
    private JButton chooseXmlButton;
    private JButton chooseVisStyleButton;
    private JButton chooseSaxonButton;
    private JButton resetNetworkButton;
    private JButton recreateNetworkButton;
    private JButton groupByNodeTypeButton;
    private JButton showOnlyButton;
    private JButton hideButton;
    private JButton highLightButton;
    private JButton sortActivitiesByTime;
    private JButton svLeftArrow;
    private JButton svRightArrow;
    private JButton svPlay;
    private JButton svStop;
    private JButton showNodeProperties;
    private JButton chooseFirstGraphsNodeButton;
    private JButton chooseFirstGraphsEdgeButton;
    private JButton chooseSecondGraphsNodeButton;
    private JButton chooseSecondGraphsEdgeButton;
    private JButton compareGraphsButton;
    private JButton exportAsPngButton;
    private JButton exportNetworkButton;
    private JButton exportTableButton;
    private JButton importGraphsButton;
    private JButton startClusteringButton;
    private JButton stopClusteringButton;
    private JButton getBackwardProvenanceButton;

    private JButton getForwardProvenanceButton;
    private JButton showAllNodesEdges;
    private JButton resetComparison;
    private JTextField searchArea;
    public JLabel comparisonArea;
    private JButton  searchButton;
    private JButton  searchUndoButton;
    private JButton applyVisStyle;

    private JButton vsActivity;
    private JButton vsEntity;

    public JButton getVsActivity() {
        return vsActivity;
    }

    public JButton getVsEntity() {
        return vsEntity;
    }

    public JButton getVsAgent() {
        return vsAgent;
    }

    private JButton vsAgent;

    private JCheckBox sliderCheckBox;
    private JCheckBox compareAllProperties;
    private JCheckBox ignoreDifferentNodeTypes;
    private JRadioButton active;
    private JRadioButton inactive;
    private JRadioButton vsTemplate1;
    private JRadioButton vsTemplate2;
    private JRadioButton vsTemplate3;
    private JRadioButton activateRealTime;
    private JRadioButton deactivateRealTime;
    private ButtonGroup radioButtons;
    private ButtonGroup templatesButtonGroup;
    private ButtonGroup realTimeVisButtonGroup;
    private Timer timer1;
    private JComboBox showOnly;
    private JComboBox hide;
    private JComboBox highLight;

    private JPanel mainPanel;
    private JPanel appNamePanel;
    private JPanel provoPanel;
    private JPanel helpExitPanel;
    private JPanel toolboxPanel;
    private JPanel queryPanel;
    private JPanel propsPanel;
    private JPanel showRelationsPanel;
    private JPanel showOnlyPanel;
    private JPanel searchPanel;
    private JPanel hidePanel;
    private JPanel highLightPanel;
    private JPanel sliderVisualizationPanel;
    private JPanel visualStyleTemplatesPanel;
    private JPanel relationsPanel;
    private JPanel sortPanel;
    private JPanel realTimeVisPanel;
    private JPanel compareGraphsPanel;
    private JPanel exportPanel;
    private JPanel clusteringPanel;

    private JScrollPane scrollPane;

    private JSpinner clusteringSpinner;
    private JSpinner nodeCount;
    private JSpinner nodeWeight;
    private JSpinner edgeWeight;
    private JSpinner neighbourNodeWeight;
    private JSpinner threshold;
    private JSpinner minThreshold;

    private JLabel statusLabel;
    private JLabel appName;
    private JLabel xmlFileNameLabel;
    private JLabel visStyleFileNameLabel;
    private JLabel saxonFileNameLabel;
    private JLabel activeLabel;
    private JLabel inactiveLabel;
    public  JLabel sliderLabel;
    private JLabel vsTemplate1Label;
    private JLabel vsTemplate2Label;
    private JLabel vsTemplate3Label;
    private JLabel versionLabel;
    private JLabel nodeCountString;
    private JLabel firstGraphsNodeLabel;
    private JLabel firstGraphsEdgeLabel;
    private JLabel secondGraphsNodeLabel;
    private JLabel secondGraphsEdgeLabel;
    private JLabel firstGraphLabel;
    private JLabel secondGraphLabel;
    private JLabel nodeWeightLabel;
    private JLabel edgeWeightLabel;
    private JLabel neighbourNodeWeightLabel;
    private JLabel thresholdLabel;
    private JLabel minThresholdLabel;
    private JLabel searchStatusLabel;

    public JFrame loadingFrame;
    //public JProgressBar progressBar;
    public  JPanel contentPane;
    public JFrame ff;
    public JLabel ll;


    private Subscriber subscriber;

    ImportNodesAction imp;

    public HashMap<String, Boolean> getNodesToBeShownHash() {
        return nodesToBeShownHash;
    }
    public ArrayList<ArrayList<String>> clusters;


    public ArrayList nodesToBeShown = new ArrayList<>();
    public ArrayList nodesToBeShownComplement = new ArrayList<>();

    public boolean isBDMOn=false;
    public boolean isComparisonOn=false;
    private HashMap<String, Boolean> nodesToBeShownHash;


    int NODE = 0;
    int EDGE = 1;

    int FIRST = 0;
    int SECOND = 1;


    FileWriter frNew;
    BufferedWriter brNew;


    FileWriter fr;
    BufferedWriter br;


    public MyControlPanel(CytoVisProject cytoVisProject) throws IOException {
        super();
        // Initializing Variables and Tools
        enhancedVersionOfBDM = new EnhancedVersionOfBDM(cytoVisProject.getAdapter());
        enhancedVersionOfFDM = new EnhancedVersionOfFDM(cytoVisProject);
        visStyleUtil = new VisStyleUtil(cytoVisProject);
        backwardDependency = new BackwardDependency();
        forwardDependency = new ForwardDependency(cytoVisProject);
        cytoPanelHeight = (Toolkit.getDefaultToolkit().getScreenSize().height * 0.81);
        cytoPanelWidth = (Toolkit.getDefaultToolkit().getScreenSize().width * 0.20);
        mainPanelHeight = (cytoPanelHeight * 2.6);
        mainPanelWidth = (cytoPanelWidth * 0.9);
        this.cytoVisProject = cytoVisProject;
        this.adapter = cytoVisProject.getAdapter();
        this.provoImportCore = new ProvoImportCore(cytoVisProject);
        this.setPreferredSize(new Dimension(cytoPanelWidth.intValue(), cytoPanelHeight.intValue()));
        this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        this.sliderStop = false;
        // Initializing tools
        initializeToolbox();
        nodeWeight.setEnabled(false);
        edgeWeight.setEnabled(false);
        neighbourNodeWeight.setEnabled(false);
        threshold.setEnabled(false);
        minThreshold.setEnabled(false);
        this.setAutoscrolls(true);
        compareGraphsButton.setEnabled(false);
        this.compareGraphsCore = new CompareGraphsCore(cytoVisProject);
        compareGraphsCore.changeFile(1, 0.0, 0.0, 0.0,0.0, 0.0);
        networkViewOrganizer = new NetworkViewOrganizer(this);
    }


    // This will initialize all the tools which will be on the control panel
    public void initializeToolbox() throws IOException {
        if(this.enhancedVersionOfBDM == null){
            this.enhancedVersionOfBDM = new EnhancedVersionOfBDM(this.adapter);
        }

        if(this.enhancedVersionOfFDM == null){
            this.enhancedVersionOfFDM = new EnhancedVersionOfFDM(this.cytoVisProject);
        }

        if(this.backwardDependency == null){
            this.backwardDependency = new BackwardDependency();
        }

        if(this.forwardDependency == null){
            this.forwardDependency = new ForwardDependency(this.cytoVisProject);
        }

        initializePanels();
        initializeAppNameToolbox();
        initializeFileToolBox();
        initializeImportToolBox();
        initializeShowHideToolbox();
        initializeSearchPanel();
        initializeRelationsPanel();
        initializeVisualStyleTemplatesToolBox();
        initializeShowRelationsToolbox();
        initializeSortPanel();
        initializeRealTimeVisToolBox();
        initializeActivityToolbox();
        initializeSliderToolbox();
        initializeCompareGraphsPanel();
        initializeExportPanel();
        initializeClusteringPanel();
        initializeHelpCloseToolbox();
        initializeNetworkAvailability();
        actionListeners();

        addingComponentsToProvoPanel();
        //addingComponentsToShowHidePanels();
        addingComponentsToSearchPanels();
        //addinComponentsToVisualStyleTemplatePanel();
        addingComponentsToRelationsPanel();
        addingComponentsToSortPanel();
        addingComponentsToSliderPanel();
        //addingComponentsToRealTimeVisPanel();
        addingComponentsToToolboxPanel();
        addingComponentsToQueryPanel();
        addingComponentsToPropsPanel();
        addingComponentsToClusteringPanel();
        addingComponentsToCompareGraphsPanel();
        //addingComponentsToExportPanel();
        addingComponentsToHelpClosePanel();
        addingComponentsToMainPanel();
    }

    public void initializePanels(){
        this.mainPanel                  = new JPanel();
        this.showOnlyPanel              = new JPanel();
        this.hidePanel                  = new JPanel();
        this.highLightPanel             = new JPanel();
        this.searchPanel                = new JPanel();
        this.helpExitPanel              = new JPanel();
        this.provoPanel                 = new JPanel();
        this.toolboxPanel               = new JPanel();
        this.queryPanel                 = new JPanel();
        this.propsPanel                 = new JPanel();
        this.showRelationsPanel         = new JPanel();
        this.sliderVisualizationPanel   = new JPanel();
        this.visualStyleTemplatesPanel  = new JPanel();
        this.relationsPanel             = new JPanel();
        this.sortPanel                  = new JPanel();
        this.realTimeVisPanel           = new JPanel();
        this.compareGraphsPanel         = new JPanel();
        this.exportPanel                = new JPanel();
        this.clusteringPanel            = new JPanel();

        this.sliderVisualizationPanel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.95)).intValue(), ((Double)(mainPanelHeight * 0.06)).intValue())); //0.08
        this.mainPanel.setPreferredSize(new Dimension(mainPanelWidth.intValue(), mainPanelHeight.intValue()));
        this.showOnlyPanel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.25)).intValue(), ((Double)(mainPanelHeight * 0.035)).intValue())); //0.045
        this.hidePanel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.25)).intValue(), ((Double)(mainPanelHeight * 0.035)).intValue()));
        this.highLightPanel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.25)).intValue(), ((Double)(mainPanelHeight * 0.022)).intValue()));
        this.searchPanel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.85)).intValue(), ((Double)(mainPanelHeight * 0.027)).intValue())); //0.039 //0.30 //0.27
        this.showRelationsPanel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.7)).intValue(), ((Double)(mainPanelHeight * 0.020)).intValue()));
        this.provoPanel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.95)).intValue(), ((Double)(mainPanelHeight * 0.04)).intValue())); //0.083
        this.helpExitPanel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.95)).intValue(), ((Double)(mainPanelHeight * 0.035)).intValue())); //0.040
        this.toolboxPanel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.95)).intValue(), ((Double)(mainPanelHeight * 0.13)).intValue())); //31 //23 //0.12
        this.queryPanel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.95)).intValue(), ((Double)(mainPanelHeight * 0.050)).intValue())); //31 //23
        this.propsPanel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.95)).intValue(), ((Double)(mainPanelHeight * 0.030)).intValue())); //0.039
        //this.visualStyleTemplatesPanel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.95)).intValue(), ((Double)(mainPanelHeight * 0.067)).intValue()));
        this.relationsPanel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.85)).intValue(), ((Double)(mainPanelHeight * 0.085)).intValue()));
        this.sortPanel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.85)).intValue(), ((Double)(mainPanelHeight * 0.039)).intValue()));
        this.realTimeVisPanel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.95)).intValue(), ((Double)(mainPanelHeight * 0.055)).intValue()));
        this.compareGraphsPanel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.95)).intValue(), ((Double)(mainPanelHeight * 0.21)).intValue())); //0.24  //0.19
        this.exportPanel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.95)).intValue(), ((Double)(mainPanelHeight * 0.070)).intValue())); //0,75
        this.clusteringPanel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.95)).intValue(), ((Double)(mainPanelHeight * 0.027)).intValue()));

        // Setting border and titles to all panels
        this.mainPanel.setBorder(BorderFactory.createRaisedBevelBorder());
        //this.showRelationsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED),"Show Relations"));
        this.showRelationsPanel.setBorder(BorderFactory.createTitledBorder("Show Relations"));
        //this.provoPanel.setBorder(BorderFactory.createTitledBorder("PROV-O Import"));
        this.provoPanel.setBorder(BorderFactory.createTitledBorder(null, "PROV-O Import", TitledBorder.LEFT, TitledBorder.TOP, new Font("times new roman",Font.BOLD,12), Color.black));

        //this.helpExitPanel.setBorder(BorderFactory.createTitledBorder("Help & Exit"));
        this.helpExitPanel.setBorder(BorderFactory.createTitledBorder(null, "Help & Exit", TitledBorder.LEFT, TitledBorder.TOP, new Font("times new roman",Font.BOLD,12), Color.black));
        //this.toolboxPanel.setBorder(BorderFactory.createTitledBorder("Filter ")); /// Highlight
        //this.toolboxPanel.setBorder(BorderFactory.createTitledBorder("Utilities ")); /// Highlight
        this.toolboxPanel.setBorder(BorderFactory.createTitledBorder(null, "Utilities", TitledBorder.LEFT, TitledBorder.TOP, new Font("times new roman",Font.BOLD,12), Color.black));

        //this.queryPanel.setBorder(BorderFactory.createTitledBorder("Query ")); /// Highlight
        this.queryPanel.setBorder(BorderFactory.createTitledBorder(null, "Query", TitledBorder.LEFT, TitledBorder.TOP, new Font("times new roman",Font.BOLD,12), Color.black));

        //this.propsPanel.setBorder(BorderFactory.createTitledBorder("Props ")); /// Highlight
        this.propsPanel.setBorder(BorderFactory.createTitledBorder(null, "Props", TitledBorder.LEFT, TitledBorder.TOP, new Font("times new roman",Font.BOLD,12), Color.black));

        //this.searchPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), "Search"));
        this.searchPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), "Search"));

        //this.sliderVisualizationPanel.setBorder(BorderFactory.createTitledBorder("Realtime Visualization"));
        this.sliderVisualizationPanel.setBorder(BorderFactory.createTitledBorder(null, "Realtime Visualization", TitledBorder.LEFT, TitledBorder.TOP, new Font("times new roman",Font.BOLD,12), Color.black));

       // this.visualStyleTemplatesPanel.setBorder(BorderFactory.createTitledBorder("Visual Styles"));
        //this.visualStyleTemplatesPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), "Visual Styles"));
        this.relationsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), "Relations"));
        this.sortPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), "Sort"));
        //this.sortPanel.setBorder(BorderFactory.createTitledBorder(null, "Sort", TitledBorder.LEFT, TitledBorder.TOP, new Font("times new roman",Font.BOLD,12), Color.black));

        //this.sortPanel.setBorder(BorderFactory.createTitledBorder("Sort"));
        this.realTimeVisPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), "Real Time Visualization"));
        //this.toolboxPanel.setBorder(BorderFactory.createTitledBorder(null, "Utilities", TitledBorder.LEFT, TitledBorder.TOP, new Font("times new roman",Font.BOLD,12), Color.black));

        //this.compareGraphsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), "Compare Graphs"));
        //this.compareGraphsPanel.setBorder(BorderFactory.createTitledBorder("Compare Graphs"));
        this.compareGraphsPanel.setBorder(BorderFactory.createTitledBorder(null, "Comparison", TitledBorder.LEFT, TitledBorder.TOP, new Font("times new roman",Font.BOLD,12), Color.black));

        this.exportPanel.setBorder(BorderFactory.createTitledBorder("Export"));
        //this.toolboxPanel.setBorder(BorderFactory.createTitledBorder(null, "Utilities", TitledBorder.LEFT, TitledBorder.TOP, new Font("times new roman",Font.BOLD,12), Color.black));

        //this.exportPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), "Export"));
        //this.clusteringPanel.setBorder(BorderFactory.createTitledBorder("Cluster"));
        this.clusteringPanel.setBorder(BorderFactory.createTitledBorder(null, "Summarization", TitledBorder.LEFT, TitledBorder.TOP, new Font("times new roman",Font.BOLD,13), Color.black));


        this.scrollPane = new JScrollPane();
        this.scrollPane.setViewportView(this.mainPanel);
        this.scrollPane.setPreferredSize(new Dimension(cytoPanelWidth.intValue(),cytoPanelHeight.intValue()));
        this.scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        this.scrollPane.setMaximumSize(new Dimension(((Double)(mainPanelWidth * 0.1875)).intValue(), 1000000));
    }

    public void initializeAppNameToolbox(){
        this.appNamePanel = new JPanel();
        this.appName = new JLabel();
        appName.setText("CytoVisToolBox TEST");
        appName.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.5)).intValue(), ((Double)(mainPanelHeight * 0.0260)).intValue()));
        appName.setFont(new Font("Serif",Font.BOLD,18));
        appNamePanel.add(appName);
        appNamePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    }

    public void initializeFileToolBox(){
        this.chooseSaxonButton = new JButton("Choose Saxon File");
        this.chooseVisStyleButton = new JButton("Choose XSL File");
        this.chooseXmlButton = new JButton("Choose XML File");
        this.xmlFileNameLabel = new JLabel("None");
        this.visStyleFileNameLabel = new JLabel("None");
        this.saxonFileNameLabel = new JLabel("None");
        this.extractFilesButton = new JButton("Extract Files");
        this.resetNetworkButton = new JButton("Reset Network");
        this.recreateNetworkButton = new JButton("Recreate Network");

        chooseSaxonButton.setPreferredSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/2.2)).intValue(),((Double)(mainPanelWidth * 0.06)).intValue()));
        chooseVisStyleButton.setPreferredSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/2.2)).intValue(),((Double)(mainPanelWidth * 0.06)).intValue()));
        chooseXmlButton.setPreferredSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/2.2)).intValue(),((Double)(mainPanelWidth * 0.06)).intValue()));
        saxonFileNameLabel.setPreferredSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/2.5)).intValue(),((Double)(mainPanelWidth * 0.06)).intValue()));
        resetNetworkButton.setPreferredSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/1.2)).intValue(),((Double)(mainPanelWidth * 0.06)).intValue()));
        recreateNetworkButton.setPreferredSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/1.2)).intValue(),((Double)(mainPanelWidth * 0.06)).intValue()));
        xmlFileNameLabel.setPreferredSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/2.5)).intValue(),((Double)(mainPanelWidth * 0.06)).intValue()));
        visStyleFileNameLabel.setPreferredSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/2.5)).intValue(),((Double)(mainPanelWidth * 0.06)).intValue()));
        extractFilesButton.setPreferredSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/1.15)).intValue(),((Double)(mainPanelWidth * 0.07)).intValue()));
    }

    public void initializeImportToolBox(){
        this.importVisStyleButton = new JButton("<html>Import<br/>Vis Style</html>");
        this.importNetworkButton = new JButton();//("<html>Import<br/>Edges1</html>");
        this.importTableButton = new JButton(); //("<html>Import<br/>Nodes1</html>");
        this.statusLabel = new JLabel();
        importVisStyleButton.setPreferredSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/3.3)).intValue(),(provoPanel.getPreferredSize().width/9)));
        importNetworkButton.setPreferredSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/3.7)).intValue(),(provoPanel.getPreferredSize().width/9)));
        importTableButton.setPreferredSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/3.7)).intValue(),(provoPanel.getPreferredSize().width/9)));
        statusLabel.setPreferredSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/1.28)).intValue(), ((Double)(mainPanelWidth * 0.06)).intValue()));
    }

    public JTextField getSearchArea() {
        return searchArea;
    }

    public void initializeShowHideToolbox(){
        this.showOnly = new JComboBox();
        this.hide = new JComboBox();
        this.highLight = new JComboBox();
        this.showOnly.setPreferredSize(new Dimension(new Dimension(((Double)(provoPanel.getPreferredSize().width/4.5)).intValue(),((Double)(mainPanelWidth * 0.06)).intValue())));
        this.hide.setPreferredSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/4.5)).intValue(),((Double)(mainPanelWidth * 0.06)).intValue()));
        this.highLight.setPreferredSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/4.5)).intValue(),((Double)(mainPanelWidth * 0.06)).intValue()));
        this.showOnlyButton = new JButton("Action");
        this.highLightButton = new JButton("Action");
        this.hideButton = new JButton("Action");
        this.showOnlyButton.setPreferredSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/4.5)).intValue(),((Double)(mainPanelWidth * 0.06)).intValue()));
        this.hideButton.setPreferredSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/4.5)).intValue(),((Double)(mainPanelWidth * 0.06)).intValue()));
        this.highLightButton.setPreferredSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/4.5)).intValue(),((Double)(mainPanelWidth * 0.06)).intValue()));
        this.showOnlyPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED),"Show Only"));
        this.hidePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED),"Hide"));
        this.highLightPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED),"Highlight"));
    }

    public void initializeRelationsPanel(){
        this.showHideRelationButton = new JButton("Show / Hide Entity Relation");
        this.showHideRelationButton.setPreferredSize(new Dimension(230,40));
    }

    public void initializeVisualStyleTemplatesToolBox(){
        this.vsTemplate1 = new JRadioButton();
        this.vsTemplate2 = new JRadioButton();
        this.vsTemplate3 = new JRadioButton();
        this.templatesButtonGroup = new ButtonGroup();

        this.vsActivity = new JButton();
        this.vsActivity.setOpaque(true);
        this.vsActivity.setBackground(Color.BLUE);

        this.vsAgent = new JButton();
        this.vsAgent.setOpaque(true);
        this.vsAgent.setBackground(Color.ORANGE);

        this.vsEntity = new JButton();
        this.vsEntity.setOpaque(true);
        this.vsEntity.setBackground(Color.YELLOW);

        this.vsActivity.setPreferredSize(new Dimension(((Double)(mainPanelWidth*0.07)).intValue(),((Double)(mainPanelWidth * 0.07)).intValue()));
        this.vsAgent.setPreferredSize(new Dimension(((Double)(mainPanelWidth*0.07)).intValue(),((Double)(mainPanelWidth * 0.07)).intValue()));
        this.vsEntity.setPreferredSize(new Dimension(((Double)(mainPanelWidth*0.07)).intValue(),((Double)(mainPanelWidth * 0.07)).intValue()));

        this.applyVisStyle = new JButton("APPLY");
        this.applyVisStyle.setPreferredSize(new Dimension(((Double)(mainPanelWidth*0.5)).intValue(),((Double)(mainPanelWidth * 0.08)).intValue()));

        this.vsTemplate1Label = new JLabel("   Visual Style Activity          : ");  //Visual Style Template 1
        this.vsTemplate2Label = new JLabel("   Visual Style Agent             : ");
        this.vsTemplate3Label = new JLabel("   Visual Style Entity            : "); //1.8
        this.vsTemplate1Label.setSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/1.1)).intValue(), ((Double)(mainPanelWidth * 0.07)).intValue()));
        this.vsTemplate2Label.setSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/1.1)).intValue(), ((Double)(mainPanelWidth * 0.07)).intValue()));
        this.vsTemplate3Label.setSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/1.1)).intValue(), ((Double)(mainPanelWidth * 0.07)).intValue()));
        this.visualStyleTemplatesPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    }

    public void initializeSortPanel(){
        this.entityBasedSorting = new JButton("<html>Entity Based<br/>Sort</html");
        this.sortActivitiesByTime = new JButton("<html>Activity<br/>Based Sort</html>");

        this.entityBasedSorting.setPreferredSize(new Dimension(((Double)(mainPanelWidth*0.36)).intValue(),((Double)(mainPanelWidth * 0.11)).intValue()));
        this.sortActivitiesByTime.setPreferredSize(new Dimension(((Double)(mainPanelWidth*0.36)).intValue(),((Double)(mainPanelWidth * 0.11)).intValue()));
        this.sortPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    }

    public void initializeSearchPanel(){

        this.searchArea = new JTextField();
        this.searchButton = new JButton("Find");
        this.searchUndoButton = new JButton("Undo Find");
        this.searchStatusLabel = new JLabel("");

        this.searchArea.setPreferredSize(new Dimension(((Double)(mainPanelWidth*0.30)).intValue(),((Double)(mainPanelWidth * 0.08)).intValue())); //0.11
        this.searchButton.setPreferredSize(new Dimension(((Double)(mainPanelWidth*0.22)).intValue(),((Double)(mainPanelWidth * 0.08)).intValue()));
        //this.searchUndoButton.setPreferredSize(new Dimension(((Double)(mainPanelWidth*0.25)).intValue(),((Double)(mainPanelWidth * 0.08)).intValue()));
        this.searchStatusLabel.setPreferredSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/1.28)).intValue(), ((Double)(mainPanelWidth * 0.06)).intValue()));
        this.searchPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    }

    public void initializeClusteringPanel(){
        this.startClusteringButton = new JButton("Start Clustering");
        this.clusteringSpinner     = new JSpinner(new SpinnerNumberModel(0, 0, 20, 1));

        startClusteringButton.setMargin(new Insets(5,5,5,5));

        this.stopClusteringButton = new JButton("Cancel");
        stopClusteringButton.setMargin(new Insets(5,5,5,5));
        stopClusteringButton.setEnabled(false);
    }

    public void initializeShowRelationsToolbox(){
        this.radioButtons = new ButtonGroup();
        this.active = new JRadioButton();
        this.inactive = new JRadioButton();
        this.inactive.setSelected(true);
        this.activeLabel = new JLabel("Active: ");
        this.inactiveLabel = new JLabel("Inactive: ");
        this.showRelationsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    }

    public void initializeActivityToolbox(){
        this.groupByNodeTypeButton = new JButton("<html>Group By <br/>Node Type</html>");
        this.showNodeProperties = new JButton("Show Node Properties");

        this.showNodeProperties.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.4)).intValue(), ((Double)(mainPanelWidth * 0.11)).intValue()));
        this.groupByNodeTypeButton.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.4)).intValue(), ((Double)(mainPanelWidth * 0.11)).intValue()));
    }

    public void initializeSliderToolbox(){
        this.slider = new JSlider();
        this.sliderCheckBox = new JCheckBox();
        this.svRightArrow = new JButton();
        this.svLeftArrow = new JButton();
        this.svPlay = new JButton();
        this.svStop = new JButton();
        setIcons();
        this.slider.setValue(0);
        this.sliderVisualization = new SliderVisualization(this);
        this.sliderLabel = new JLabel();
        this.sliderLabel.setText("Activate");
        this.sliderLabel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.8)).intValue(), ((Double)(mainPanelWidth * 0.07)).intValue()));
        this.sliderLabel.setFont(new Font("Serif",Font.BOLD,12));
        sliderCheckBox.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.06)).intValue(), ((Double)(mainPanelWidth * 0.07)).intValue()));
        sliderCheckBox.setSelected(false);
        slider.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.65)).intValue(), ((Double)(mainPanelWidth * 0.07)).intValue()));
        svLeftArrow.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.09)).intValue(), ((Double)(mainPanelWidth * 0.09)).intValue()));
        svRightArrow.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.09)).intValue(), ((Double)(mainPanelWidth * 0.09)).intValue()));
        svPlay.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.09)).intValue(), ((Double)(mainPanelWidth * 0.09)).intValue()));
        svStop.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.09)).intValue(), ((Double)(mainPanelWidth * 0.09)).intValue()));
    }

    public void initializeRealTimeVisToolBox(){
        this.activateRealTime = new JRadioButton("Activate");
        this.deactivateRealTime = new JRadioButton("Deactivate");
        this.activateRealTime.setPreferredSize(new Dimension(((Double)(mainPanelWidth*0.30)).intValue(), ((Double)(mainPanelHeight*0.016)).intValue()));
        this.deactivateRealTime.setPreferredSize(new Dimension(((Double)(mainPanelWidth*0.30)).intValue(), ((Double)(mainPanelHeight*0.016)).intValue()));
        this.realTimeVisButtonGroup = new ButtonGroup();

        this.nodeCount = new JSpinner();
        this.nodeCountString = new JLabel("Maximum Node Count: ");
        this.nodeCountString.setPreferredSize(new Dimension(((Double)(mainPanelWidth*0.5)).intValue(), ((Double)(mainPanelHeight*0.014)).intValue()));
        nodeCount.setValue(20);
        setMaxNode(20);

        realTimeVisButtonGroup.add(activateRealTime);
        realTimeVisButtonGroup.add(deactivateRealTime);
    }

    public void initializeCompareGraphsPanel(){
        this.chooseFirstGraphsEdgeButton    = new JButton("<html>Choose<br/>Edge</html>");
        this.chooseFirstGraphsNodeButton    = new JButton("<html>Choose<br/>Node</html>");
        this.chooseSecondGraphsNodeButton   = new JButton("<html>Choose<br/>Node</html>");
        this.chooseSecondGraphsEdgeButton   = new JButton("<html>Choose<br/>Edge</html>");
        this.importGraphsButton             = new JButton("Import");
        this.getBackwardProvenanceButton    = new JButton("Get Backward Dependencies");
        this.getForwardProvenanceButton    = new JButton("Get Forward Dependencies");
        this.showAllNodesEdges              = new JButton("<html>ReGenerate <br/>Network</html>");
        this.resetComparison                = new JButton("Reset Network ");

        this.compareAllProperties           = new JCheckBox("Include all properties to comparison");
        this.ignoreDifferentNodeTypes       = new JCheckBox("Ignore nodes with different node types");
        this.nodeWeight                     = new JSpinner(new SpinnerNumberModel(1, -1000.0, 1000.0, 0.1));
        this.edgeWeight                     = new JSpinner(new SpinnerNumberModel(1, -1000.0, 1000.0, 0.1));
        this.neighbourNodeWeight            = new JSpinner(new SpinnerNumberModel(1, -1000.0, 1000.0, 0.1));
        this.threshold                      = new JSpinner(new SpinnerNumberModel(0.8, 0.0, 1, 0.1));
        this.minThreshold                   = new JSpinner(new SpinnerNumberModel(0.3,0.0,1,0.1));
        this.nodeWeightLabel                = new JLabel("Node Weight:");
        this.edgeWeightLabel                = new JLabel("Edge Weight:");
        this.neighbourNodeWeightLabel       = new JLabel("Adjacent Node Weight:");
        this.thresholdLabel                 = new JLabel("Threshold:");
        this.minThresholdLabel              = new JLabel("Minimum Threshold:");

        chooseFirstGraphsNodeButton.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.3)).intValue(), ((Double)(mainPanelWidth * 0.11)).intValue())); //30
        chooseFirstGraphsEdgeButton.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.3)).intValue(), ((Double)(mainPanelWidth * 0.11)).intValue()));
        chooseSecondGraphsNodeButton.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.3)).intValue(), ((Double)(mainPanelWidth * 0.11)).intValue()));
        chooseSecondGraphsEdgeButton.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.3)).intValue(), ((Double)(mainPanelWidth * 0.11)).intValue()));
        importGraphsButton.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.5)).intValue(), ((Double)(mainPanelWidth * 0.08)).intValue()));
        compareAllProperties.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.75)).intValue(), ((Double)(mainPanelWidth * 0.08)).intValue()));
        ignoreDifferentNodeTypes.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.75)).intValue(), ((Double)(mainPanelWidth * 0.08)).intValue()));
        getBackwardProvenanceButton.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.50)).intValue(), ((Double)(mainPanelWidth * 0.1)).intValue())); //0.75 0.11
        getForwardProvenanceButton.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.50)).intValue(), ((Double)(mainPanelWidth * 0.1)).intValue())); //50

        showAllNodesEdges.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.4)).intValue(), ((Double)(mainPanelWidth * 0.11)).intValue())); //50 height 11
        resetComparison.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.7)).intValue(), ((Double)(mainPanelWidth * 0.08)).intValue()));

        nodeWeight.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.20)).intValue(), ((Double)(mainPanelWidth * 0.06)).intValue()));
        edgeWeight.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.20)).intValue(), ((Double)(mainPanelWidth * 0.06)).intValue()));
        neighbourNodeWeight.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.20)).intValue(), ((Double)(mainPanelWidth * 0.06)).intValue()));
        threshold.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.20)).intValue(), ((Double)(mainPanelWidth * 0.06)).intValue()));
        minThreshold.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.20)).intValue(), ((Double)(mainPanelWidth * 0.06)).intValue()));
        this.nodeWeightLabel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.60)).intValue(), ((Double)(mainPanelWidth * 0.06)).intValue()));
        this.edgeWeightLabel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.60)).intValue(), ((Double)(mainPanelWidth * 0.06)).intValue()));
        this.neighbourNodeWeightLabel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.60)).intValue(), ((Double)(mainPanelWidth * 0.06)).intValue()));
        this.thresholdLabel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.60)).intValue(), ((Double)(mainPanelWidth * 0.06)).intValue()));
        this.minThresholdLabel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.60)).intValue(), ((Double)(mainPanelWidth * 0.06)).intValue()));

        this.firstGraphsEdgeLabel = new JLabel("",SwingConstants.CENTER);
        this.firstGraphsNodeLabel = new JLabel("",SwingConstants.CENTER);
        this.secondGraphsEdgeLabel = new JLabel("",SwingConstants.CENTER);
        this.secondGraphsNodeLabel = new JLabel("",SwingConstants.CENTER);

        firstGraphsEdgeLabel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.4)).intValue(), ((Double)(mainPanelWidth * 0.06)).intValue()));//30
        firstGraphsNodeLabel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.4)).intValue(), ((Double)(mainPanelWidth * 0.06)).intValue()));
        secondGraphsEdgeLabel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.4)).intValue(), ((Double)(mainPanelWidth * 0.06)).intValue()));
        secondGraphsNodeLabel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.4)).intValue(), ((Double)(mainPanelWidth * 0.06)).intValue()));

        this.compareGraphsButton = new JButton("Compare");
        compareGraphsButton.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.5)).intValue(), ((Double)(mainPanelWidth * 0.08)).intValue()));


        firstGraphLabel = new JLabel("First Graph", SwingConstants.CENTER);
        firstGraphLabel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.8)).intValue(), ((Double)(mainPanelWidth * 0.06)).intValue()));

        secondGraphLabel = new JLabel("Second Graph", SwingConstants.CENTER);
        secondGraphLabel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.8)).intValue(), ((Double)(mainPanelWidth * 0.06)).intValue()));

        this.comparisonArea = new JLabel("",SwingConstants.CENTER);
        comparisonArea.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.8)).intValue(), ((Double)(mainPanelWidth * 0.06)).intValue()));

    }

    public void initializeExportPanel(){
        this.exportPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.exportAsPngButton   = new JButton("Export as PNG");
        this.exportNetworkButton = new JButton("Export Network");
        this.exportTableButton   = new JButton("Export Table");

        this.exportAsPngButton.setPreferredSize(new Dimension(((Double)(exportPanel.getPreferredSize().width*0.6)).intValue(), 30));
        this.exportTableButton.setPreferredSize(new Dimension(((Double)(exportPanel.getPreferredSize().width*0.6)).intValue(), 30));
        this.exportNetworkButton.setPreferredSize(new Dimension(((Double)(exportPanel.getPreferredSize().width*0.6)).intValue(), 30));
    }

    public void addingComponentsToToolboxPanel(){
       // toolboxPanel.add(showOnlyPanel);
       // toolboxPanel.add(hidePanel);
       // toolboxPanel.add(highLightPanel);
        toolboxPanel.add(searchPanel);
        toolboxPanel.add(searchStatusLabel);
        //toolboxPanel.add(visualStyleTemplatesPanel);
        //toolboxPanel.add(sortPanel);
        //toolboxPanel.add(relationsPanel);
       // toolboxPanel.add(groupByNodeTypeButton);
        //toolboxPanel.add(showNodeProperties);
    }

    public void addingComponentsToQueryPanel(){
        //queryPanel.add(sortPanel);
        queryPanel.add(getBackwardProvenanceButton);
        queryPanel.add(getForwardProvenanceButton);
        //queryPanel.add(showAllNodesEdges);
    }
/**/
    public void addingComponentsToPropsPanel(){
        propsPanel.add(showNodeProperties);
    }


    public void initializeHelpCloseToolbox(){
        this.closeButton = new JButton("Close");
        this.helpButton = new JButton("Help");
        this.versionLabel = new JLabel("Version: 1.4");
        this.versionLabel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.7)).intValue(), ((Double)(mainPanelHeight * 0.035)).intValue())); //20
    }

    public void addingComponentsToProvoPanel(){
        provoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        //provoPanel.add(chooseSaxonButton);
        //provoPanel.add(saxonFileNameLabel);
        //provoPanel.add(chooseVisStyleButton);
        //provoPanel.add(visStyleFileNameLabel);
        //provoPanel.add(chooseXmlButton);
        //provoPanel.add(xmlFileNameLabel);
        //provoPanel.add(extractFilesButton);
        provoPanel.add(importVisStyleButton);
        provoPanel.add(importNetworkButton);
        provoPanel.add(importTableButton);
        //provoPanel.add(resetNetworkButton);
        //provoPanel.add(recreateNetworkButton);
        provoPanel.add(statusLabel);
    }

    public void addingComponentsToShowHidePanels(){
        showOnlyPanel.add(showOnly);
        showOnlyPanel.add(showOnlyButton);
        hidePanel.add(hide);
        hidePanel.add(hideButton);
        highLightPanel.add(highLight);
        highLightPanel.add(highLightButton);
    }

    public void addingComponentsToSearchPanels(){
        searchPanel.add(searchArea);
        searchPanel.add(searchButton);
        //searchPanel.add(searchUndoButton);
        //searchPanel.add(searchStatusLabel);
    }

    public void addingComponentsToRelationsPanel(){
        addingComponentsToShowRelationPanel();
        this.relationsPanel.add(showRelationsPanel);
        this.relationsPanel.add(showHideRelationButton);
    }

    public void addinComponentsToVisualStyleTemplatePanel(){
        //templatesButtonGroup.add(vsTemplate1);
        //templatesButtonGroup.add(vsTemplate2);
        //templatesButtonGroup.add(vsTemplate3);
        visualStyleTemplatesPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        visualStyleTemplatesPanel.add(vsTemplate1Label);
        visualStyleTemplatesPanel.add(vsActivity);
        //visualStyleTemplatesPanel.add(vsTemplate1);
        visualStyleTemplatesPanel.add(vsTemplate2Label);
        visualStyleTemplatesPanel.add(vsAgent);
        //visualStyleTemplatesPanel.add(vsTemplate2);
        visualStyleTemplatesPanel.add(vsTemplate3Label);
        visualStyleTemplatesPanel.add(vsEntity);
        visualStyleTemplatesPanel.add(applyVisStyle);
        //visualStyleTemplatesPanel.add(vsTemplate3);
    }

    public void addingComponentsToSortPanel(){
        sortPanel.add(sortActivitiesByTime);
        sortPanel.add(entityBasedSorting);
    }

    public void addingComponentsToRealTimeVisPanel(){
        realTimeVisPanel.add(activateRealTime);
        realTimeVisPanel.add(deactivateRealTime);
        realTimeVisPanel.add(nodeCountString);
        realTimeVisPanel.add(nodeCount);
    }

    public void addingComponentsToShowRelationPanel(){
        radioButtons.add(active);
        radioButtons.add(inactive);
        showRelationsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        showRelationsPanel.add(activeLabel);
        showRelationsPanel.add(active);
        showRelationsPanel.add(inactiveLabel);
        showRelationsPanel.add(inactive);
    }

    public void addingComponentsToSliderPanel(){
        sliderVisualizationPanel.add(sliderCheckBox);
        sliderVisualizationPanel.add(slider);
        sliderVisualizationPanel.add(sliderLabel);
        sliderVisualizationPanel.add(svLeftArrow);
        sliderVisualizationPanel.add(svPlay);
        sliderVisualizationPanel.add(svStop);
        sliderVisualizationPanel.add(svRightArrow);
    }

    public void addingComponentsToCompareGraphsPanel(){
        compareGraphsPanel.add(firstGraphLabel);

        compareGraphsPanel.add(chooseFirstGraphsNodeButton);
        compareGraphsPanel.add(chooseFirstGraphsEdgeButton);
        compareGraphsPanel.add(firstGraphsNodeLabel);
        compareGraphsPanel.add(firstGraphsEdgeLabel);

        compareGraphsPanel.add(secondGraphLabel);

        compareGraphsPanel.add(chooseSecondGraphsNodeButton);
        compareGraphsPanel.add(chooseSecondGraphsEdgeButton);
        compareGraphsPanel.add(secondGraphsNodeLabel);
        compareGraphsPanel.add(secondGraphsEdgeLabel);

        compareGraphsPanel.add(importGraphsButton);


        compareGraphsPanel.add(compareAllProperties);
        //compareGraphsPanel.add(ignoreDifferentNodeTypes);
        compareGraphsPanel.add(nodeWeightLabel);
        compareGraphsPanel.add(nodeWeight);
        compareGraphsPanel.add(edgeWeightLabel);
        compareGraphsPanel.add(edgeWeight);
        compareGraphsPanel.add(neighbourNodeWeightLabel);
        compareGraphsPanel.add(neighbourNodeWeight);
        compareGraphsPanel.add(thresholdLabel);
        compareGraphsPanel.add(threshold);
        compareGraphsPanel.add(minThresholdLabel);
        compareGraphsPanel.add(minThreshold);
        compareGraphsPanel.add(compareGraphsButton);
        // compareGraphsPanel.add(resetComparison);
        compareGraphsPanel.add(comparisonArea);


        //compareGraphsPanel.add(getBackwardProvenanceButton);
        //compareGraphsPanel.add(showAllNodesEdges);
    }

    public void addingComponentsToExportPanel(){
        exportPanel.add(exportAsPngButton);
        exportPanel.add(exportNetworkButton);
        exportPanel.add(exportTableButton);
    }

    public void addingComponentsToHelpClosePanel(){
        helpExitPanel.add(helpButton);
        helpExitPanel.add(closeButton);
    }

    public void addingComponentsToClusteringPanel(){
        clusteringPanel.add(startClusteringButton);
        clusteringPanel.add(clusteringSpinner);
        clusteringPanel.add(stopClusteringButton);
    }

    public void addingComponentsToMainPanel(){
        this.mainPanel.add(appNamePanel);
        this.mainPanel.add(provoPanel);
        this.mainPanel.add(toolboxPanel);
        this.mainPanel.add(queryPanel);
        //this.mainPanel.add(propsPanel);
        //this.mainPanel.add(sortPanel);
        toolboxPanel.add(sortPanel);
        toolboxPanel.add(showAllNodesEdges);
        toolboxPanel.add(showNodeProperties);
        toolboxPanel.add(resetComparison);
        //toolboxPanel.add(relationsPanel);
        // toolboxPanel.add(groupByNodeTypeButton);
        //this.mainPanel.add(showNodeProperties);
        //this.mainPanel.add(visualStyleTemplatesPanel);
        this.mainPanel.add(sliderVisualizationPanel);
        //this.mainPanel.add(realTimeVisPanel);
        this.mainPanel.add(clusteringPanel);
        this.mainPanel.add(compareGraphsPanel);
        //this.mainPanel.add(exportPanel);
        this.mainPanel.add(helpExitPanel);
        this.mainPanel.add(versionLabel);
        this.add(scrollPane);
    }

    public void initializeNetworkAvailability(){
        // Check for a network is available or not
        if(adapter.getCyApplicationManager().getCurrentNetwork() == null){
            // deactivate tools if there are no network loaded
            deActivateTools();
            this.chooseXmlButton.setEnabled(false);
            this.chooseVisStyleButton.setEnabled(false);
            this.extractFilesButton.setEnabled(false);
        }else{
            chooseXmlButton.setEnabled(false);
            chooseVisStyleButton.setEnabled(false);
            extractFilesButton.setEnabled(false);

            // Finding different node types and setting it to the related tools (Same as TableSetListener class)
            FilterUtil filter = new FilterUtil(adapter.getCyApplicationManager().getCurrentNetwork(),
                    adapter.getCyApplicationManager().getCurrentNetwork().getDefaultNodeTable(), this.adapter);
            List<String> newNodeTypes = new ArrayList<String>();
            newNodeTypes.add("None");
            ArrayList<CyNode> nodes = filter.getAllNodes();
            for(CyNode node : nodes){
                if(newNodeTypes.contains(filter.findNodeType(node)) == false){
                    newNodeTypes.add(filter.findNodeType(node));
                }
            }
            setNodeTypes(newNodeTypes);
            // Activating Tools
            activateTools();
        }
    }

    public CompareGraphsCore getCompareGraphsCore() {
        return compareGraphsCore;
    }

    public VisStyleUtil getVisStyleUtil() {
        return visStyleUtil;
    }

    public JLabel getSearchStatusLabel() {
        return searchStatusLabel;
    }

    public void actionListeners() throws IOException {

        // Setting action listener to "Choose XML File" button
        this.chooseXmlButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                provoImportCore.chooseXmlFile();
                if(provoImportCore.isFileControl() == true){
                    xmlFileNameLabel.setText(provoImportCore.getXmlFileName());
                    extractFilesButton.setEnabled(true);
                }else {
                    showInvalidWarning();
                }
            }
        });
        // Setting action listener to "Choose Visual Style File" button
        this.chooseVisStyleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                provoImportCore.chooseVisMapFile();
                if(provoImportCore.isFileControl() == true){
                    visStyleFileNameLabel.setText(provoImportCore.getVisStyleFileName());
                    chooseXmlButton.setEnabled(true);
                }else {
                    showInvalidWarning();
                }
            }
        });
        // Setting action listener to "Choose Saxon File" button
        this.chooseSaxonButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                provoImportCore.chooseSaxonFile();
                if(provoImportCore.isFileControl() == true){
                    saxonFileNameLabel.setText(provoImportCore.getSaxonFileName());
                    chooseVisStyleButton.setEnabled(true);
                }else {
                    showInvalidWarning();
                }
            }
        });

        //imy
        // Setting action listener to "Choose Saxon File" button
        this.resetNetworkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(adapter.getCyApplicationManager().getCurrentNetworkView() != null){
/*                    cytoVisProject.getMyControlPanel().setStatus("oncesi");
                    cytoVisProject.getMyControlPanel().setStatus("sonrasi 1 ");*/
                    imp.removeNodes();
                    //imp.resetNodes();
                }else{
                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"There is no visualization loaded yet ..!",
                            "Error!", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        //imy
        // Setting action listener to "Choose Saxon File" button
        this.recreateNetworkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //imy : burayi undo yap
                if(adapter.getCyApplicationManager().getCurrentNetworkView() != null){
                    //visStyleUtil.setDefaultVisStyle();
                    //adapter.getCyApplicationManager().getCurrentNetworkView().dispose();
                    recreateNetworkAction = new RecreateNetworkAction(cytoVisProject);
                    try {
                        recreateNetworkAction.recreateView();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }else{
                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"There is no visualization loaded yet ..!",
                            "Error!", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        // Setting action listener to "Extract Files" button
        this.extractFilesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                provoImportCore.extractFiles();
                importNetworkButton.setEnabled(true);
            }
        });
        // Setting action to "Import Network" button
        this.importNetworkButton.setAction(new ImportEdgesAction(cytoVisProject,this.enhancedVersionOfBDM,this.enhancedVersionOfFDM));
        //this.importNetworkButton.addMouseListener(new ImportEdgesRightClickAction(cytoVisProject,"C:\\tmp\\edges.csv", this.enhancedVersionOfBDM));

        // Setting action to "Import Visual Style" Button
        //imy
        //imp = new ImportNodesAction(cytoVisProject, "C:\\provoTransformerPlugin\\nodes.csv");
        imp = new ImportNodesAction(cytoVisProject,this.enhancedVersionOfFDM);
        this.importTableButton.setAction(imp);

        //this.importTableButton.addMouseListener(new ImportNodesRightClickAction(cytoVisProject,"C:\\tmp\\nodes.csv"));


        this.importVisStyleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                provoImportCore.importVisStyleTask();
                resetComparison.setEnabled(true);
            }
        });

        // Adding action listener to the components of the Toolbox panel

        // Setting action listener to action button of Show Only panel
        showOnlyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TaskManager taskManager = adapter.getCyServiceRegistrar().getService(TaskManager.class);
                taskManager.execute(new TaskIterator(new ShowOnlyTask(adapter,showOnly.getSelectedItem().toString())));
            }
        });
        // Setting action listener to action button of Hide panel
        hideButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TaskManager taskManager2 = adapter.getCyServiceRegistrar().getService(TaskManager.class);
                taskManager2.execute(new TaskIterator(new HideTask(adapter,nodeTypes.get(hide.getSelectedIndex()))));
            }
        });
        // Setting action listener to action button of highlight panel
        highLightButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TaskManager taskManager3 = adapter.getCyServiceRegistrar().getService(TaskManager.class);
                taskManager3.execute(new TaskIterator(new HighLightTask(cytoVisProject,nodeTypes.get(highLight.getSelectedIndex()))));
            }
        });

        //imy
        // Setting action listener to action button of search panel
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),searchArea.getText(),
//                        "Info!", JOptionPane.INFORMATION_MESSAGE);

                TaskManager taskManager3 = adapter.getCyServiceRegistrar().getService(TaskManager.class);
//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),searchArea.getText(),
//                        "Info!", JOptionPane.INFORMATION_MESSAGE);
//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," in here  2",
//                        "Info!", JOptionPane.INFORMATION_MESSAGE);

                taskManager3.execute(new TaskIterator(new SearchTask(cytoVisProject,searchArea.getText(),true)));
//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),searchArea.getText(),
//                        "Info!", JOptionPane.INFORMATION_MESSAGE);

//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," in here 3",
//                        "Info!", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        //imy
        // Setting action listener to action button of search panel
//        searchUndoButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                TaskManager taskManager3 = adapter.getCyServiceRegistrar().getService(TaskManager.class);
///*                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),searchArea.getText(),
//                        "Info!", JOptionPane.INFORMATION_MESSAGE);*/
//                taskManager3.execute(new TaskIterator(new SearchTask(cytoVisProject,searchArea.getText(),false)));
//            }
//        });

        applyVisStyle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ChangeVisualStyleTemplate changeVisualStyleTemplate = new ChangeVisualStyleTemplate(cytoVisProject);
                changeVisualStyleTemplate.changeVisualStyle(visStyleUtil.getCURRENT_AGENT_COLOR(),
                                                            visStyleUtil.getCURRENT_ACTIVITY_COLOR(),
                                                            visStyleUtil.getCURRENT_ENTITY_COLOR());
            }
        });

        vsActivity.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(null, "Choose a color", (Color)visStyleUtil.getDEFAULT_ACTIVITY_COLOR());
                vsActivity.setBackground(newColor);
                visStyleUtil.setCURRENT_ACTIVITY_COLOR(newColor);
            }
        });

        vsAgent.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(null, "Choose a color", (Color)visStyleUtil.getDEFAULT_AGENT_COLOR());
                vsAgent.setBackground(newColor);
                visStyleUtil.setCURRENT_AGENT_COLOR(newColor);
            }
        });

        vsEntity.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(null, "Choose a color", (Color)visStyleUtil.getDEFAULT_ENTITY_COLOR());
                vsEntity.setBackground(newColor);
                visStyleUtil.setCURRENT_ENTITY_COLOR(newColor);
            }
        });

        vsTemplate1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ChangeVisualStyleTemplate changeVisualStyleTemplate = new ChangeVisualStyleTemplate(cytoVisProject);
                changeVisualStyleTemplate.setTemplateNumber(1);
                changeVisualStyleTemplate.changeVisualStyle();
            }
        });

        vsTemplate2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ChangeVisualStyleTemplate changeVisualStyleTemplate = new ChangeVisualStyleTemplate(cytoVisProject);
                changeVisualStyleTemplate.setTemplateNumber(2);
                changeVisualStyleTemplate.changeVisualStyle();
            }
        });

        vsTemplate3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ChangeVisualStyleTemplate changeVisualStyleTemplate = new ChangeVisualStyleTemplate(cytoVisProject);
                changeVisualStyleTemplate.setTemplateNumber(3);
                changeVisualStyleTemplate.changeVisualStyle();
            }
        });
        // Setting action listener to radio button named active which is in the show relations panel
        // This flag used for understanding which radio button is selected
        active.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cytoVisProject.getNodeSelectedListener().setFlag(true);
            }
        });
        // Setting action listener to radio button named inactive which is in the show relations panel
        inactive.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cytoVisProject.getNodeSelectedListener().setFlag(false);
            }
        });
        // Setting action listener to "Group By Node Type" button
        groupByNodeTypeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TaskManager taskManager6 = adapter.getCyServiceRegistrar().getService(TaskManager.class);
                taskManager6.execute(new GroupByNodeTypeTaskFactory(adapter).createTaskIterator());
            }
        });
        // Setting action listener to "Sort Activities By Time" button
        sortActivitiesByTime.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TaskManager taskManager7 = adapter.getCyServiceRegistrar().getService(TaskManager.class);
                taskManager7.execute(new SortActivitesByTimeTaskFactory(adapter).createTaskIterator());
            }
        });
        // Setting action listener to "Sort Entities Based on Activity Time" button
        entityBasedSorting.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TaskManager taskManager8 = adapter.getCyServiceRegistrar().getService(TaskManager.class);
                taskManager8.execute(new EntityBasedSortingTaskFactory(cytoVisProject).createTaskIterator());
            }
        });
        // Setting action listener to "Show / Hide Entity Relation" button
        showHideRelationButton.setAction(new ShowHideEntityRelationAction(cytoVisProject));

        this.showNodeProperties.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CyNetwork network = cytoVisProject.getAdapter().getCyApplicationManager().getCurrentNetwork();
                List<CyNode> selected = CyTableUtil.getNodesInState(network,"selected",true);


                for(CyNode node : selected){
                    NodePropertyWindow nodePropertyWindow = new NodePropertyWindow(cytoVisProject, node);
                }
            }
        });

 /*       this.activateRealTime.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final JedisPoolConfig poolConfig = new JedisPoolConfig();
                final JedisPool jedisPool = new JedisPool(poolConfig, "localhost", 6379, 0);
                final Jedis subscriberJedis = jedisPool.getResource();
                subscriber = new Subscriber(getInstance());

                new Thread(new Runnable() {

                    public void run() {
                        try {
                            subscriberJedis.subscribe(subscriber, "channel");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });*/

        this.nodeCount.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent changeEvent) {
                setMaxNode((Integer) nodeCount.getValue());

                if(adapter.getCyApplicationManager().getCurrentNetwork() != null){
                    NetworkViewOrganizer networkViewOrganizer = new NetworkViewOrganizer(getInstance());
                    networkViewOrganizer.reOrganizeNetwork();
                }
            }
        });

/*        deactivateRealTime.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                subscriber.unsubscribe();
            }
        });*/

        // Setting action listener to "Close" button
        this.closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });
        // Setting action listener to "Help" button
        this.helpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                FilterUtil filterUtil                   = new FilterUtil(adapter.getCyApplicationManager().getCurrentNetwork(), adapter.getCyApplicationManager().getCurrentTable(), adapter);
                ArrayList<String> selectedNodeIdList    = filterUtil.getSelectedNodeIdList(adapter, "name");
                //ArrayList nodesToBeShown = new ArrayList<>();

                nodesToBeShown = new ArrayList<>();
                nodesToBeShownComplement = new ArrayList<>();

                long startTime = System.currentTimeMillis();
                for (String nodeId : selectedNodeIdList){
                    try {
                        nodesToBeShown.addAll(enhancedVersionOfBDM.getBackwardProvenance(nodeId, enhancedVersionOfBDM.getStateCurrent(), new ArrayList<>(),false));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"nodes to be shown size --> "+String.valueOf(nodesToBeShown.size())+" ne tutuyor "
//                        +nodesToBeShown.get(0),
//                        "Info!", JOptionPane.INFORMATION_MESSAGE);

                //nodesToBeShown.addAll(selectedNodeIdList);
                System.out.println("[" + new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format(new Date()) + "] Total time to get backward provenance of " + selectedNodeIdList.size() + " nodes: "
                        + (System.currentTimeMillis() - startTime) + " ms.");

                networkViewOrganizer.showExcept(nodesToBeShown, filterUtil);
                //enhancedVersionOfBDM.setDoesFilterApplied(true);
                //enhancedVersionOfBDM.setFilterNode(nodesToBeShown);
                //enhancedVersionOfBDM.setSelectedNodeIdList(selectedNodeIdList);

                isBDMOn = true;

                filterUtil.deSelectAllNodes(adapter);

                try {
                    enhancedVersionOfBDM.closeFile();
                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," file closed ",
                            "Info!", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        this.slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if(sliderCheckBox.isSelected()){
                    CyApplicationManager manager = adapter.getCyApplicationManager();
                    CyNetworkView networkView = manager.getCurrentNetworkView();
                    CyNetwork network = networkView.getModel();
                    CyTable table = network.getDefaultNodeTable();
                    FilterUtil filter = new FilterUtil(network,table, adapter);
                    ArrayList<CyNode> activities = filter.FilterRowByNodeType("activity", "nodeType");

                    CyColumn timeColumn = table.getColumn("startTime"); // Getting start time column
                    List<String> timeList = filter.getTimeFromColumn(timeColumn); // Gets value of start time column without null value
                    sliderVisualization.hideFutureNodes(timeList, filter, network, networkView);
                    networkView.updateView();
                }

            }
        });

        this.svStop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sliderStop = true;
            }
        });

        this.svPlay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sliderStop = false;
                if(sliderCheckBox.isSelected() == true){
                    timer1.start();
                }
            }
        });

        ActionListener actionListenerForTimer = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                slider.setValue(slider.getValue() + 1);
                if(slider.getValue() == slider.getMaximum() || sliderStop == true){
                    timer1.stop();
                }
            }
        };

        this.timer1 = new Timer(500, actionListenerForTimer);

        this.sliderCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                slider.setValue(slider.getValue());
            }
        });

        this.svRightArrow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                slider.setValue(slider.getValue()+1);
            }
        });

        this.svLeftArrow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                slider.setValue(slider.getValue()-1);
            }
        });

        //this.chooseFirstGraphsNodeButton.addMouseListener(new ImportComparisonRightClickAction(cytoVisProject, NODE, FIRST));
        this.chooseFirstGraphsNodeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if(compareGraphsCore.chooseFirstGraphsNode()){
                    firstGraphsNodeLabel.setText(compareGraphsCore.getNode1FileName());
//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),firstGraphsNodeLabel.getText(),
//                            "Info!", JOptionPane.INFORMATION_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"The file that you choosed are not valid!",
                            "Error!", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        //this.chooseFirstGraphsNodeButton.addMouseListener(new ImportComparisonRightClickAction(cytoVisProject, EDGE, FIRST));
        this.chooseFirstGraphsEdgeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if(compareGraphsCore.chooseFirstGraphsEdge()){
                    firstGraphsEdgeLabel.setText(compareGraphsCore.getEdge1FileName());
//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),firstGraphsEdgeLabel.getText(),
//                            "Info!", JOptionPane.INFORMATION_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"The file that you choosed are not valid!",
                            "Error!", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        //this.chooseFirstGraphsNodeButton.addMouseListener(new ImportComparisonRightClickAction(cytoVisProject, NODE, SECOND));
        this.chooseSecondGraphsNodeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if(compareGraphsCore.chooseSecondGraphsNode()){
                    secondGraphsNodeLabel.setText(compareGraphsCore.getNode2FileName());
//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),secondGraphsNodeLabel.getText(),
//                            "Info!", JOptionPane.INFORMATION_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"The file that you choosed are not valid!",
                            "Error!", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        //this.chooseFirstGraphsNodeButton.addMouseListener(new ImportComparisonRightClickAction(cytoVisProject, EDGE, SECOND));
        this.chooseSecondGraphsEdgeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if(compareGraphsCore.chooseSecondGraphsEdge()){
                    secondGraphsEdgeLabel.setText(compareGraphsCore.getEdge2FileName());
//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),secondGraphsEdgeLabel.getText(),
//                            "Info!", JOptionPane.INFORMATION_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"The file that you choosed are not valid!",
                        "Error!", JOptionPane.INFORMATION_MESSAGE);}
            }
        });

        importGraphsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                comparisonArea.setText("Comparison graphs are being imported  ...");
                Integer result = compareGraphsCore.compareGraphs();

                if(result == 0){
                }else if(result == -1){
                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
                            "Graphs must have 1 root ..!",
                            "Warning!", JOptionPane.INFORMATION_MESSAGE);
                }else if(result == 1){
                    drawComparedGraphs = new DrawComparedGraphs(compareGraphsCore, cytoVisProject);
                    try {
                        drawComparedGraphs.draw();



                        comparisonArea.setText("   Comparison graphs imported ...");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    compareGraphsButton.setEnabled(true);
                }
            }
        });

        compareGraphsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                isComparisonOn=true;
                comparisonArea.setText("Comparison is in progress ...");

                long startTime = System.currentTimeMillis();

                try {
                    compareGraphsCore.createAttendanceList();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    compareGraphsCore.findSimilarNodePairsWithSorting();
                } catch (IOException e) {
                    e.printStackTrace();
                }


//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
//                        "compare graph drawing",
//                        "Warning!", JOptionPane.INFORMATION_MESSAGE);

                System.out.println("Total time to compare graphs: " + (System.currentTimeMillis() - startTime));
                drawComparedGraphs.changeColors(compareGraphsCore);
                comparisonArea.setText("Comparison is done ...");
                //resetComparison.setEnabled(true);
            }
        });

        resetComparison.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                adapter.getCyNetworkViewManager().destroyNetworkView(adapter.getCyApplicationManager().getCurrentNetworkView());
                comparisonArea.setText("");
                compareGraphsButton.setEnabled(false);
                //resetComparison.setEnabled(false);
                isComparisonOn = false;
                //drawComparedGraphs.draw();
            }
        });

        this.ignoreDifferentNodeTypes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(ignoreDifferentNodeTypes.isSelected()){
                    compareGraphsCore.setIgnorDifferentNodeTypes(true);
                }else {
                    compareGraphsCore.setIgnorDifferentNodeTypes(false);
                }
            }
        });

        this.compareAllProperties.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(compareAllProperties.isSelected()){

//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
//                            "algo selected to 2",
//                            "Warning!", JOptionPane.INFORMATION_MESSAGE);


                    compareGraphsCore.changeFile(2, Double.parseDouble(nodeWeight.getValue().toString()),
                            Double.parseDouble(edgeWeight.getValue().toString()), Double.parseDouble(neighbourNodeWeight.getValue().toString()),
                            Double.parseDouble(threshold.getValue().toString()), Double.parseDouble(minThreshold.getValue().toString()));
                    nodeWeight.setEnabled(true);
                    edgeWeight.setEnabled(true);
                    neighbourNodeWeight.setEnabled(true);
                    threshold.setEnabled(true);
                    minThreshold.setEnabled(true);
                }else {

                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
                            " algo selected to 1!",
                            "Warning!", JOptionPane.INFORMATION_MESSAGE);

                    compareGraphsCore.changeFile(1, 0.0, 0.0, 0.0,0.0, 0.0);
                    nodeWeight.setEnabled(false);
                    edgeWeight.setEnabled(false);
                    neighbourNodeWeight.setEnabled(false);
                    threshold.setEnabled(false);
                    minThreshold.setEnabled(false);
                }
            }
        });

        nodeWeight.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                compareGraphsCore.changeFile(2, Double.parseDouble(nodeWeight.getValue().toString()),
                        Double.parseDouble(edgeWeight.getValue().toString()), Double.parseDouble(neighbourNodeWeight.getValue().toString()),
                        Double.parseDouble(threshold.getValue().toString()), Double.parseDouble(minThreshold.getValue().toString()));
            }
        });

        edgeWeight.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                compareGraphsCore.changeFile(2, Double.parseDouble(nodeWeight.getValue().toString()),
                        Double.parseDouble(edgeWeight.getValue().toString()), Double.parseDouble(neighbourNodeWeight.getValue().toString()),
                        Double.parseDouble(threshold.getValue().toString()), Double.parseDouble(minThreshold.getValue().toString()));
            }
        });

        neighbourNodeWeight.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                compareGraphsCore.changeFile(2, Double.parseDouble(nodeWeight.getValue().toString()),
                        Double.parseDouble(edgeWeight.getValue().toString()), Double.parseDouble(neighbourNodeWeight.getValue().toString()),
                        Double.parseDouble(threshold.getValue().toString()), Double.parseDouble(minThreshold.getValue().toString()));
            }
        });

        threshold.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                compareGraphsCore.changeFile(2, Double.parseDouble(nodeWeight.getValue().toString()),
                        Double.parseDouble(edgeWeight.getValue().toString()), Double.parseDouble(neighbourNodeWeight.getValue().toString()),
                        Double.parseDouble(threshold.getValue().toString()), Double.parseDouble(minThreshold.getValue().toString()));
            }
        });

        this.exportTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                if(adapter.getCyApplicationManager().getCurrentNetworkView() != null){
                    ExportTableTaskFactory exportTableTaskFactory = adapter.get_ExportTableTaskFactory();
                    TaskIterator taskIterator = exportTableTaskFactory.createTaskIterator(adapter.getCyApplicationManager().
                            getCurrentNetwork().getDefaultNodeTable());
                    adapter.getTaskManager().execute(taskIterator);
                }else{
                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"There is no visualization loaded yet ..!",
                            "Error!", JOptionPane.INFORMATION_MESSAGE);
                }

            }
        });

        this.exportNetworkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                if(adapter.getCyApplicationManager().getCurrentNetworkView() != null){
                //imy
//                cytoVisProject.getMyControlPanel().setStatus("oncesi");
        /*            try {*/
//                        cytoVisProject.getMyControlPanel().setStatus("sonrasi 1 ");
                        imp.removeNodes();
                        //cytoVisProject.getMyControlPanel().setStatus("sonrasi 2 ");
       /*             } catch (IOException e) {
                        e.printStackTrace();
                    }*/
/*                    ExportNetworkViewTaskFactory exportNetworkViewTaskFactory = adapter.get_ExportNetworkViewTaskFactory();
                    TaskIterator taskIterator = exportNetworkViewTaskFactory.createTaskIterator(adapter.getCyApplicationManager().getCurrentNetworkView());
                    adapter.getTaskManager().execute(taskIterator);*/

                }else{
                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"There is no visualization loaded yet ..!",
                            "Error!", JOptionPane.INFORMATION_MESSAGE);
                }

            }
        });

        this.exportAsPngButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                if(adapter.getCyApplicationManager().getCurrentNetworkView() != null){
                    imp.resetNodes();
            /*        ExportNetworkImageTaskFactory exportNetworkImageTaskFactory = adapter.get_ExportNetworkImageTaskFactory();
                    TaskIterator taskIterator = exportNetworkImageTaskFactory.createTaskIterator(adapter.getCyApplicationManager().getCurrentNetworkView());
                    adapter.getTaskManager().execute(taskIterator);*/
                }else{
                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"There is no visualization loaded yet ..!",
                            "Error!", JOptionPane.INFORMATION_MESSAGE);
                }

            }
        });

        startClusteringButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                kMeansClustering = new KMeansClustering(cytoVisProject,adapter);
                //ArrayList<ArrayList<String>> clusters = kMeansClustering.applyKMeansClustering(Integer.parseInt(clusteringSpinner.getValue().toString()));
                try {
//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), "flag set to true",
//                            "Info!", JOptionPane.INFORMATION_MESSAGE);
//
                   cytoVisProject.getNodeSelectedListener2().setFlag(true);

//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), "2",
//                            "Info!", JOptionPane.INFORMATION_MESSAGE);
                   clusters = kMeansClustering.applyKMeansClusteringByName(Integer.parseInt(clusteringSpinner.getValue().toString()));
                   kMeansClustering.drawClusters(clusters);
//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), "7",
//                            "Info!", JOptionPane.INFORMATION_MESSAGE);
                   startClusteringButton.setText("In Clustering Mode");
                   startClusteringButton.setEnabled(false);

//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), "8",
//                            "Info!", JOptionPane.INFORMATION_MESSAGE);
                   stopClusteringButton.setEnabled(true);

//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(), "9",
//                            "Info!", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        stopClusteringButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                cytoVisProject.getNodeSelectedListener2().setFlag(false);
                //imy : burayi undo yap
                if(adapter.getCyApplicationManager().getCurrentNetworkView() != null){
                    //visStyleUtil.setDefaultVisStyle();
                    //adapter.getCyApplicationManager().getCurrentNetworkView().dispose();
                    if(recreateNetworkAction == null)
                          recreateNetworkAction = new RecreateNetworkAction(cytoVisProject);

                    try {
                        recreateNetworkAction.recreateView();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }else{
                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"There is no visualization loaded yet ..!",
                            "Error!", JOptionPane.INFORMATION_MESSAGE);
                }

                stopClusteringButton.setEnabled(false);
                startClusteringButton.setText("Start Clustering");
                startClusteringButton.setEnabled(true);
                cytoVisProject.getNodeSelectedListener2().setCloseClusterFlag(true);
            }
        });

        this.getBackwardProvenanceButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                FilterUtil filterUtil                   = new FilterUtil(adapter.getCyApplicationManager().getCurrentNetwork(), adapter.getCyApplicationManager().getCurrentTable(), adapter);
                ArrayList<String> selectedNodeIdList    = filterUtil.getSelectedNodeIdList(adapter, "name");
                //ArrayList nodesToBeShown = new ArrayList<>();

                nodesToBeShown = new ArrayList<>();

                fr =null;
                try {
                    fr = new FileWriter("C:\\tmp\\bdmNew.txt", false);
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

//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," here 1 ",
//                        "Info!", JOptionPane.INFORMATION_MESSAGE);

                long startTime = System.currentTimeMillis();

                for (String nodeId : selectedNodeIdList){
                    try {
//                        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," here  "+nodeId,
//                                "Info!", JOptionPane.INFORMATION_MESSAGE);
                        br.write("selected node is --> "+nodeId);
                        nodesToBeShown.addAll(enhancedVersionOfBDM.getBackwardProvenance(nodeId, enhancedVersionOfBDM.getStateCurrent(), new ArrayList<>(),true));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    br.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," here 2 ",
//                        "Info!", JOptionPane.INFORMATION_MESSAGE);
//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"nodes to be shown size --> "+String.valueOf(nodesToBeShown.size())+" ne tutuyor "
//                        +nodesToBeShown.get(0),
//                        "Info!", JOptionPane.INFORMATION_MESSAGE);

                nodesToBeShown.addAll(selectedNodeIdList);

//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," here 3 ",
//                        "Info!", JOptionPane.INFORMATION_MESSAGE);

                System.out.println("[" + new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format(new Date()) + "] Total time to get backward provenance of " + selectedNodeIdList.size() + " nodes: "
                        + (System.currentTimeMillis() - startTime) + " ms.");


                try {
                    br.write("Full List Check");
                    br.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


                for(Object node2Display:nodesToBeShown)
                {
                    try {
                        br.write(node2Display.toString());
                        br.newLine();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }


                networkViewOrganizer.showOnly(nodesToBeShown, filterUtil);
                enhancedVersionOfBDM.setDoesFilterApplied(true);
                enhancedVersionOfBDM.setFilterNode(nodesToBeShown);
                enhancedVersionOfBDM.setSelectedNodeIdList(selectedNodeIdList);

//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," here 4 ",
//                        "Info!", JOptionPane.INFORMATION_MESSAGE);

                isBDMOn = true;

                filterUtil.deSelectAllNodes(adapter);

                try {
                    br.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," here 5 ",
//                        "Info!", JOptionPane.INFORMATION_MESSAGE);
                try {
                    enhancedVersionOfBDM.closeTheFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            private void createShownNodesHash(ArrayList<String> nodesToBeShown) {
                for(String node:nodesToBeShown)
                {
                    nodesToBeShownHash.put(node,true);
                }
            }
        });

        this.getForwardProvenanceButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                FilterUtil filterUtil                   = new FilterUtil(adapter.getCyApplicationManager().getCurrentNetwork(), adapter.getCyApplicationManager().getCurrentTable(), adapter);
                ArrayList<String> selectedNodeIdList    = filterUtil.getSelectedNodeIdList(adapter, "name");
                //ArrayList nodesToBeShown = new ArrayList<>();

                nodesToBeShown = new ArrayList<>();


                frNew =null;
                try {
                    frNew = new FileWriter("C:\\tmp\\fdmNew2.txt", false);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                brNew = new BufferedWriter(frNew);
                try {
                    brNew.newLine();
                    brNew.newLine();
                    brNew.write("in MyControl App before call");
                    brNew.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," in forward dep query ",
                        "Info!", JOptionPane.INFORMATION_MESSAGE);

//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," node types size1 "+getImp().nodeTypes.size(),
//                        "Info!", JOptionPane.INFORMATION_MESSAGE);

                long startTime = System.currentTimeMillis();

                //*********************************
//
//
//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
//                        "DEBUG1 ", "ImportNodesAction", JOptionPane.INFORMATION_MESSAGE);
//
//                for (CyNode agentNode:imp.getAgents()) {
//                    String agentName = filterUtil.findNodeName(agentNode);
//
////                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
////                            agentName, "ImportNodesAction", JOptionPane.INFORMATION_MESSAGE);
//
//                    try {
//                        brNew.write("  agent is --> " + agentName);
//                        brNew.newLine();
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//
//
//                //HashMap<String, ArrayList<String>> tempStateCurrent = enhancedVersionOfFDM.getStateCurrent();
//
//                //ArrayList<String> valueList = (ArrayList<String>) tempStateCurrent.keySet();
//
//               // ArrayList<String> yourList = new ArrayList<>(tempStateCurrent.keySet());
//
////                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
////                        "size of keySet  "+yourList.size(), "ImportNodesAction", JOptionPane.INFORMATION_MESSAGE);
//
//                //her node'un hasha listesinin boyutu nedir, onu gorelim.
//
//                //for(String nodeKey: yourList)
//
//                for(CyNode node:imp.getAgents())
//                {
////                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
////                            "DEBUG3 1 nodeKey "+nodeKey, "ImportNodesAction", JOptionPane.INFORMATION_MESSAGE);
//                    String nodeKey = filterUtil.findNodeName(node);
//                    try {
//                        brNew.write("  key node is --> " + nodeKey );
//                        brNew.newLine();
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//
//
//                    //ArrayList<String> hashValueList = tempStateCurrent.get(nodeKey);
//
//                    for(String ssNode:enhancedVersionOfFDM.getStateCurrent().get(nodeKey))
//                    {
//
//                        if(ImportNodesAction.NodeType.ACTIVITY == imp.nodeTypes.get(ssNode)) {
//
//                            try {
//                                brNew.write("  *****************key node is --> " + nodeKey + " activity value --> " + ssNode);
//                                brNew.newLine();
//                            } catch (IOException e) {
//                                throw new RuntimeException(e);
//                            }
//
////                            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
////                                    " key value is " + nodeKey + "  value:" + ssNode, "mapping", JOptionPane.INFORMATION_MESSAGE);
//
//
//                            if(!enhancedVersionOfFDM.getStateCurrent().keySet().contains(ssNode)){
////                                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
////                                        " node  value was NOT existing ", "mapping", JOptionPane.INFORMATION_MESSAGE);
//                                try {
//                                    brNew.write(ssNode +" WAS NOT existing");
//                                    brNew.newLine();
//                                    brNew.newLine();
//                                } catch (IOException e) {
//                                    throw new RuntimeException(e);
//                                }
//                                enhancedVersionOfFDM.getStateCurrent().put(ssNode, new ArrayList<String>() {{ add(nodeKey); }});
//                            }else {
////                                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
////                                        " node value was existing " + nodeKey + "  value:" + ssNode, "mapping", JOptionPane.INFORMATION_MESSAGE);
//                                try {
//                                    brNew.write(ssNode +" WAS existing");
//                                    brNew.newLine();
////                                    brNew.newLine();
//                                } catch (IOException e) {
//                                    throw new RuntimeException(e);
//                                }
//                                enhancedVersionOfFDM.getStateCurrent().get(ssNode).add(nodeKey);
//                            }
//                        }
//
//                        //enhancedVersionOfFDM.getStateCurrent().get(ssNode).add();
////                        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
////                                " key value is " + nodeKey + "  value:" + ssNode, "mapping", JOptionPane.INFORMATION_MESSAGE);
//                    }
//                    }
//
//                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
//                        "DEBUG2 ", "ImportNodesAction", JOptionPane.INFORMATION_MESSAGE);
//
//

                    //*************************

                for (String nodeId : selectedNodeIdList){
                    try {
                        brNew.write("selected node is --> "+nodeId);
//                        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," here  "+nodeId,
//                                "Info!", JOptionPane.INFORMATION_MESSAGE);
                        nodesToBeShown.addAll(enhancedVersionOfFDM.getForwardProvenance(nodeId, enhancedVersionOfFDM.getStateCurrent(), new ArrayList<>(),true,brNew));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                try {
                    brNew.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                nodesToBeShown.addAll(selectedNodeIdList);

                System.out.println("[" + new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format(new Date()) + "] Total time to get backward provenance of " + selectedNodeIdList.size() + " nodes: "
                        + (System.currentTimeMillis() - startTime) + " ms.");

                try {
                    brNew.write("Full List Check");
                    brNew.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


                for(Object node2Display:nodesToBeShown)
                {
                    try {
                        brNew.write(node2Display.toString());
                        brNew.newLine();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }


                try {
                    brNew.newLine();
                    brNew.newLine();
                    brNew.write("Full Agent Check");
                    brNew.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                //HashMap<String, ArrayList<String>> entityAgentHashMap = enhancedVersionOfFDM.getStateEntityAgent();
                for(CyNode node:imp.getAgents())
                {
                    String sAgent = filterUtil.findNodeName(node);
                    try {
                        brNew.write(sAgent);
                        brNew.newLine();
                    }catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
                try {
                    brNew.write("end of file");
                    brNew.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }






                ///*****************************


                try {
                    brNew.write("*********************ActivityList*****************");
                    brNew.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                for(CyNode node:imp.getActivities()) {
                    String activityName = filterUtil.findNodeName(node);
//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
//                            " Activity "+activityName, "DEBUG", JOptionPane.INFORMATION_MESSAGE);
                    try {
                        brNew.write("activity in ActivityList  " + activityName);
                        brNew.newLine();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                for(Object ss:nodesToBeShown)
                {
                    try {
                    brNew.write("--------> activity is in nodesToBeShown "+ss.toString());
                    brNew.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                }
                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
                        " DEBUG4", "DEBUG", JOptionPane.INFORMATION_MESSAGE);

                try {
                    brNew.write("--------> Activity Agent Mapping PHASE ");
                    brNew.newLine();
                    brNew.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                for(String ss : enhancedVersionOfFDM.getStateActivityAgent().keySet())
                {
                    try {
                        brNew.write("--------> Activity "+ss+"  Agent -->x "+enhancedVersionOfFDM.getStateActivityAgent().get(ss));
                        brNew.newLine();
                        brNew.newLine();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }


                try {
                    brNew.write("--------> CHECK PHASE ");
                    brNew.newLine();
                    brNew.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                int activityNumber=0;

                for(Object debugNode:nodesToBeShown)
                {
                    if(imp.nodeTypes.get(debugNode) == ImportNodesAction.NodeType.ACTIVITY)
                    {
                        activityNumber++;
                    }
                }

                int finalActivityNumber=0;

                ArrayList<String> agentListToBeAdded = new ArrayList<>();


                for(CyNode actNode:imp.getActivities()) {
                    String nn = filterUtil.findNodeName(actNode);
//
                    if(nodesToBeShown.contains(nn))
                    {
                        if(enhancedVersionOfFDM.getStateActivityAgent().containsKey(nn))
                        {
                           String agentToBeAdded = enhancedVersionOfFDM.getStateActivityAgent().get(nn);
                           agentListToBeAdded.add(agentToBeAdded);

//                            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
//                                    "--------> agent "+ agentToBeAdded+" for Activity :"+ nn, "DEBUG", JOptionPane.INFORMATION_MESSAGE);
                            try {
                                brNew.write("--------> agent "+ agentToBeAdded+" for Activity :"+ nn);
                                brNew.newLine();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        else
                        {
//                            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
//                                    " Activity "+nn+" is not found in Activity-Agent Association Map", "DEBUG", JOptionPane.INFORMATION_MESSAGE);

                        }

                        finalActivityNumber++;
                    }
                }

                if(activityNumber == finalActivityNumber)
                {
                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
                            " CHECK SUM is OK", "DEBUG", JOptionPane.INFORMATION_MESSAGE);

                    for(String ssNode:agentListToBeAdded)
                    {
                        nodesToBeShown.add(ssNode);
                    }

                }
                else
                {
                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
                            " CHECK SUM is FAILED", "DEBUG", JOptionPane.INFORMATION_MESSAGE);
                }


                try {
                    brNew.write("Full List Check");
                    brNew.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


                for(Object node2Display:nodesToBeShown)
                {
                    try {
                        brNew.write(node2Display.toString());
                        brNew.newLine();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }


                networkViewOrganizer.showOnly(nodesToBeShown, filterUtil);
                enhancedVersionOfBDM.setDoesFilterApplied(true);
                enhancedVersionOfBDM.setFilterNode(nodesToBeShown);
                enhancedVersionOfBDM.setSelectedNodeIdList(selectedNodeIdList);

                isBDMOn = true;

                filterUtil.deSelectAllNodes(adapter);




                try {
                    brNew.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                try {
                    enhancedVersionOfFDM.closeTheFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }




                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame()," node types size2 "+getImp().nodeTypes.size(),
                        "Info!", JOptionPane.INFORMATION_MESSAGE);

            }

            private void createShownNodesHash(ArrayList<String> nodesToBeShown) {
                for(String node:nodesToBeShown)
                {
                    nodesToBeShownHash.put(node,true);
                }
            }
        });

        this.showAllNodesEdges.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(adapter.getCyApplicationManager().getCurrentNetworkView() != null){
                    //visStyleUtil.setDefaultVisStyle();
                    isBDMOn = false;
                    cytoVisProject.getNodeSelectedListener2().setFlag(false);
                    comparisonArea.setText("");

//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"1",
//                    "Error!", JOptionPane.INFORMATION_MESSAGE);

                    cytoVisProject.getNodeSelectedListener2().nodePropHash.clear();
//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"2",
//                            "Error!", JOptionPane.INFORMATION_MESSAGE);

//                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"BDM is falsed ..!",
//                            "Error!", JOptionPane.INFORMATION_MESSAGE);

                    recreateNetworkAction = new RecreateNetworkAction(cytoVisProject);
                    try {
//                        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"3",
//                                "Error!", JOptionPane.INFORMATION_MESSAGE);
                        recreateNetworkAction.recreateView();
//                        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"4",
//                                "Error!", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }


//                    //imy : burayi undo yap
//                    if(adapter.getCyApplicationManager().getCurrentNetworkView() != null){
//                        //visStyleUtil.setDefaultVisStyle();
//                        //adapter.getCyApplicationManager().getCurrentNetworkView().dispose();
//                        recreateNetworkAction = new RecreateNetworkAction(cytoVisProject);
//                        try {
//                            recreateNetworkAction.recreateView();
//                        } catch (IOException ioException) {
//                            ioException.printStackTrace();
//                        }
//                    }else{
//                        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"There is no visualization loaded yet ..!",
//                                "Error!", JOptionPane.INFORMATION_MESSAGE);
//                    }

                    //nodesToBeShownHash.clear();



                }else{
                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"There is no visualization loaded yet ..!",
                            "Error!", JOptionPane.INFORMATION_MESSAGE);
                }

            }
        });

    }

    // This method sets image icons to the buttons of sliderVisualization panel
    public void setIcons(){
        try {
/*            Image img = ImageIO.read(getClass().getClassLoader().getResource("next.png"));
            Image img2 = ImageIO.read(getClass().getClassLoader().getResource("previous.png"));
            Image img3 = ImageIO.read(getClass().getClassLoader().getResource("play.png"));
            Image img4 = ImageIO.read(getClass().getClassLoader().getResource("pause.png"));*/

            Image img = ImageIO.read(getClass().getResource("/resources/next.png"));
            Image img2 = ImageIO.read(getClass().getResource("/resources/previous.png"));
            Image img3 = ImageIO.read(getClass().getResource("/resources/play.png"));
            Image img4 = ImageIO.read(getClass().getResource("/resources/pause.png"));

            this.svRightArrow.setIcon(new ImageIcon(img));
            this.svLeftArrow.setIcon(new ImageIcon(img2));
            this.svPlay.setIcon(new ImageIcon(img3));
            this.svStop.setIcon(new ImageIcon(img4));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // This method close the control panel when the close button is clicked.
    private void closeButtonActionPerformed(ActionEvent evt) {
        adapter.getCyServiceRegistrar().unregisterService(this,CytoPanelComponent.class);
    }
    // This method works when the wrong type of file was tryed to choose.
    public void showInvalidWarning(){
        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"The file that you choosed are not valid!",
                "Error!", JOptionPane.INFORMATION_MESSAGE);
        this.statusLabel.setText("Files are not valid!");
    }

    public void setStatus(String message){
        this.statusLabel.setText(message);
    }

    public void reCreateSlider(){
        CyApplicationManager manager = adapter.getCyApplicationManager();
        CyNetworkView networkView = manager.getCurrentNetworkView();
        CyNetwork network = networkView.getModel();
        CyTable table = network.getDefaultNodeTable();
        FilterUtil filter = new FilterUtil(network,table, this.adapter);

        ArrayList<CyNode> activities = filter.FilterRowByNodeType("activity", "nodeType");
        slider.setMaximum(activities.size()-1);

    }

    // Activating all tools
    public void activateTools(){
        this.showOnlyButton.setEnabled(true);
        this.hideButton.setEnabled(true);
        this.highLightButton.setEnabled(true);
        setShowOnly();
        setHide();
        setHighLight();
        this.groupByNodeTypeButton.setEnabled(true);
        this.importVisStyleButton.setEnabled(true);
        this.sortActivitiesByTime.setEnabled(true);
        this.showHideRelationButton.setEnabled(true);
        this.entityBasedSorting.setEnabled(true);
        this.getBackwardProvenanceButton.setEnabled(true);
        this.getForwardProvenanceButton.setEnabled(true);
        this.showAllNodesEdges.setEnabled(true);
        this.resetComparison.setEnabled(true);
        this.compareGraphsButton.setEnabled(true);
        this.svStop.setEnabled(true);
        this.svPlay.setEnabled(true);
        this.svLeftArrow.setEnabled(true);
        this.svRightArrow.setEnabled(true);
        this.slider.setEnabled(true);
        this.sliderCheckBox.setEnabled(true);
        this.showNodeProperties.setEnabled(true);
        this.startClusteringButton.setEnabled(true);
        this.searchButton.setEnabled(true);
        reCreateSlider();
    }
    // Deactivating tools
    public void deActivateTools(){
        nodeTypes = new ArrayList<String>();
        nodeTypes.add("None");
        this.highLight.setModel(new DefaultComboBoxModel(this.nodeTypes.toArray()));
        this.hide.setModel(new DefaultComboBoxModel(this.nodeTypes.toArray()));
        this.showOnly.setModel(new DefaultComboBoxModel(this.nodeTypes.toArray()));
        this.importVisStyleButton.setEnabled(false);
        this.showOnlyButton.setEnabled(false);
        this.hideButton.setEnabled(false);
        this.highLightButton.setEnabled(false);
        this.groupByNodeTypeButton.setEnabled(false);
        this.sortActivitiesByTime.setEnabled(false);
        this.showHideRelationButton.setEnabled(false);
        this.entityBasedSorting.setEnabled(false);
        this.resetComparison.setEnabled(false);
        this.getBackwardProvenanceButton.setEnabled(false);
        this.getForwardProvenanceButton.setEnabled(false);
        this.compareGraphsButton.setEnabled(false);
        this.showAllNodesEdges.setEnabled(false);
        this.svStop.setEnabled(false);
        this.svPlay.setEnabled(false);
        this.svLeftArrow.setEnabled(false);
        this.svRightArrow.setEnabled(false);
        this.slider.setEnabled(false);
        this.sliderCheckBox.setEnabled(false);
        this.showNodeProperties.setEnabled(false);
        this.startClusteringButton.setEnabled(false);
        this.searchButton.setEnabled(false);
    }

    // Getter and setter methods.

    public MyControlPanel getInstance(){
        return this;
    }

    public Integer getMaxNode(){
        return maxNode;
    }

    public void setMaxNode(Integer maxNode){
        this.maxNode = maxNode;
    }

    public JSlider getSlider() {
        return slider;
    }

    public JLabel getSliderLabel() {
        return sliderLabel;
    }

    public CySwingAppAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(CySwingAppAdapter adapter) {
        this.adapter = adapter;
    }

    public JRadioButton getActive() {
        return active;
    }

    public void setActive(JRadioButton active) {
        this.active = active;
    }

    public JRadioButton getInactive() {
        return inactive;
    }

    public void setInactive(JRadioButton inactive) {
        this.inactive = inactive;
    }

    public void setHighLight() {
        this.highLight.setModel(new DefaultComboBoxModel(this.nodeTypes.toArray()));
    }

    public void setHide() {
        this.hide.setModel(new DefaultComboBoxModel(this.nodeTypes.toArray()));
    }

    public void setShowOnly() {
        this.showOnly.setModel(new DefaultComboBoxModel(this.nodeTypes.toArray()));
        showOnly.addItem("All");
    }

    public ProvoImportCore getProvoImportCore() {
        return provoImportCore;
    }

    public void setProvoImportCore(ProvoImportCore provoImportCore) {
        this.provoImportCore = provoImportCore;
    }

    public void setXmlFileNameLabel(String xmlFileName) {
        this.xmlFileNameLabel.setText(xmlFileName);
    }

    public void setVisStyleFileNameLabel(String visStyleFileName) {
        this.visStyleFileNameLabel.setText(visStyleFileName);
    }

    public void setNodeTypes(List<String> nodeTypes){
        this.nodeTypes = nodeTypes;
    }

    public Component getComponent() {
        return this;
    }

    public CytoPanelName getCytoPanelName() {
        return CytoPanelName.WEST;
    }

    public String getTitle() {
        return "CytoVisProject Panel";
    }

    public Icon getIcon() {
        return null;
    }

    public JButton getEntityBasedSorting() {
        return entityBasedSorting;
    }

    public void setEntityBasedSorting(JButton entityBasedSorting) {
        this.entityBasedSorting = entityBasedSorting;
    }

    public CytoVisProject getCytoVisProject() {
        return cytoVisProject;
    }

    public void setCytoVisProject(CytoVisProject cytoVisProject) {
        this.cytoVisProject = cytoVisProject;
    }

    public JButton getChooseFirstGraphsNodeButton() {
        return chooseFirstGraphsNodeButton;
    }

    public void setChooseFirstGraphsNodeButton(JButton chooseFirstGraphsNodeButton) {
        this.chooseFirstGraphsNodeButton = chooseFirstGraphsNodeButton;
    }

    public JButton getChooseFirstGraphsEdgeButton() {
        return chooseFirstGraphsEdgeButton;
    }

    public void setChooseFirstGraphsEdgeButton(JButton chooseFirstGraphsEdgeButton) {
        this.chooseFirstGraphsEdgeButton = chooseFirstGraphsEdgeButton;
    }

    public JButton getChooseSecondGraphsNodeButton() {
        return chooseSecondGraphsNodeButton;
    }

    public void setChooseSecondGraphsNodeButton(JButton chooseSecondGraphsNodeButton) {
        this.chooseSecondGraphsNodeButton = chooseSecondGraphsNodeButton;
    }

    public JButton getChooseSecondGraphsEdgeButton() {
        return chooseSecondGraphsEdgeButton;
    }

    public void setChooseSecondGraphsEdgeButton(JButton chooseSecondGrapshEdgeButton) {
        this.chooseSecondGraphsEdgeButton = chooseSecondGrapshEdgeButton;
    }

    public JButton getCompareGraphsButton() {
        return compareGraphsButton;
    }


    public ImportNodesAction getImp() {
        return imp;
    }

    public void setCompareGraphsButton(JButton compareGraphsButton) {
        this.compareGraphsButton = compareGraphsButton;
    }

    public JLabel getFirstGraphsNodeLabel() {
        return firstGraphsNodeLabel;
    }

    public void setFirstGraphsNodeLabel(JLabel firstGraphsNodeLabel) {
        this.firstGraphsNodeLabel = firstGraphsNodeLabel;
    }

    public JLabel getFirstGraphsEdgeLabel() {
        return firstGraphsEdgeLabel;
    }

    public void setFirstGraphsEdgeLabel(JLabel firstGraphsEdgeLabel) {
        this.firstGraphsEdgeLabel = firstGraphsEdgeLabel;
    }

    public JLabel getSecondGraphsNodeLabel() {
        return secondGraphsNodeLabel;
    }

    public void setSecondGraphsNodeLabel(JLabel secondGraphsNodeLabel) {
        this.secondGraphsNodeLabel = secondGraphsNodeLabel;
    }

    public JLabel getSecondGraphsEdgeLabel() {
        return secondGraphsEdgeLabel;
    }

    public void setSecondGraphsEdgeLabel(JLabel secondGraphsEdgeLabel) {
        this.secondGraphsEdgeLabel = secondGraphsEdgeLabel;
    }

    public JButton getImportVisStyleButton() {
        return importVisStyleButton;
    }

    public void setImportVisStyleButton(JButton importVisStyleButton) {
        this.importVisStyleButton = importVisStyleButton;
    }

    public JButton getImportNetworkButton() {
        return importNetworkButton;
    }

    public void setImportNetworkButton(JButton importNetworkButton) {
        this.importNetworkButton = importNetworkButton;
    }

    public JButton getImportTableButton() {
        return importTableButton;
    }

    public void setImportTableButton(JButton importTableButton) {
        this.importTableButton = importTableButton;
    }

    public EnhancedVersionOfBDM getEnhancedVersionOfBDM() {
        return enhancedVersionOfBDM;
    }

    public void setEnhancedVersionOfBDM(EnhancedVersionOfBDM enhancedVersionOfBDM) {
        this.enhancedVersionOfBDM = enhancedVersionOfBDM;
    }

    public BackwardDependency getBackwardDependency() {
        return backwardDependency;
    }

    public void setForwardDependency(ForwardDependency forwardDependency) {
        this.forwardDependency = forwardDependency;
    }

    public ForwardDependency getForwardDependency() {
        return forwardDependency;
    }

    public void setBackwardDependency(BackwardDependency backwardDependency) {
        this.backwardDependency = backwardDependency;
    }

    public NetworkViewOrganizer getNetworkViewOrganizer() {
        return networkViewOrganizer;
    }

    public void setNetworkViewOrganizer(NetworkViewOrganizer networkViewOrganizer) {
        this.networkViewOrganizer = networkViewOrganizer;
    }
}