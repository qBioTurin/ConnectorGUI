/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgConnector;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListCellRenderer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.Preferences;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;
import static pkgConnector.ClusterPanel.iThreadText;
import static pkgConnector.PestimPanel.pMinText;
import static pkgConnector.PestimPanel.pMaxText;
/**
 *
 * @author beccuti
 */
public class MainFrame extends javax.swing.JFrame {
      
     
    private class TabBarController {
        private class SingleTabInfo {
            public final int numTab;
            public final Icon icon;
            public final String text, name;
            public final JScrollPane content;
            private boolean state; //true: visible, false: hidden

            public SingleTabInfo(int index, JTabbedPane tabbedPane) {
                this.numTab = index;
                this.icon = tabbedPane.getIconAt(index);
                this.text = tabbedPane.getTitleAt(index);
                this.content = (JScrollPane) tabbedPane.getComponentAt(index);
                this.name = this.content.getName();
            }

            public void setVisibility() {
                String varname = String.format("ConnectorGUI_EnableTab%s", this.name);
                this.state = getPreferences().get(varname, "true").equals("true");
            }

            public boolean getVisibility() {
                return this.state;
            }
        }

        private final JTabbedPane myJTabbedPane;
        private final ArrayList<SingleTabInfo> tabList;

        public TabBarController(JTabbedPane myTab) {
            this.tabList = new ArrayList<>();
            this.myJTabbedPane = myTab;

            //store all tabs in the list
            for (int i = 0, count = myTab.getTabCount(); i < count; i++)
                this.tabList.add(new SingleTabInfo(i, myTab));

        }

        public TabBarController refreshTabs() {
            // remove all tabs
            for (SingleTabInfo tab: this.tabList) {
                this.myJTabbedPane.remove(tab.content);
                tab.setVisibility();
            }
            // add those tabs that are not hidden
            for (SingleTabInfo tab: this.tabList)
                if (tab.getVisibility())
                    this.myJTabbedPane.addTab(tab.text, tab.icon, tab.content);

            return this;
        }
    }

    private class DockerImageManager {
        private final String countDockerImagesVariable = "ConnectorGUI_numDockerImages";
        private final String prefixDockerVariable = "ConnectorGUI_dockerImage_";
        private final javax.swing.JTable dockerTable;
        private final Map<String, DockerImageDescription> dockerImages;

        public class DockerImageDescription {
            //describe a docker image record
            public final String url, repository, name;

            /**
             * Describe a docker image record.
             * @param url dockerhub url
             */
            public DockerImageDescription(String url) {
                String tokens[] = url.split("/");
                /* accessing the array starting from the end,
                 * we take into account both parsed and unparsed docker url:
                 * case a: docker.io/repository/image
                 * case b: repository/image */
                this.name = tokens[tokens.length - 1]; //last token
                this.repository = tokens[tokens.length - 2];
                this.url = String.format("%s/%s", repository, name);
            }

            @Override
            public int hashCode() {
                int hash = 7;
                hash = 83 * hash + Objects.hashCode(this.url);
                return hash;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                if (obj == null) {
                    return false;
                }
                if (getClass() != obj.getClass()) {
                    return false;
                }
                final DockerImageDescription other = (DockerImageDescription) obj;
                return Objects.equals(this.url, other.url);
            }


        }

        /**
         * Initialize the docker manager.
         *
         * @param dockerTable Table component in GUI responsible to visualize and
         * manage docker images.
         */
        public DockerImageManager(javax.swing.JTable dockerTable) {
            this.dockerTable = dockerTable;
            this.dockerImages = new HashMap<>();

            loadConfiguration();
        }



        /**
         * Add new docker images to the system.
         *
         * @param imageListFile name of the file containing the list of docker
         * images needed to execute all the possible pipelines present in docker4seq
         */
        public void addImages(String imageListFile) {
            try {
                Files.lines(Paths.get(imageListFile)).forEach((String line) -> {
                    DockerImageDescription curr = new DockerImageDescription(line);

                    if (dockerImages.get(curr.url) == null)
                        dockerImages.put(curr.url, curr);
                });
            } catch (IOException ex) {
                System.out.println("IOException during " + imageListFile + " reading");
            }

            writeConfiguration();
        }

        /**
         * Show docker images in the apposite jtable.
         */
        public void updateGUI() {
            DefaultTableModel model = (DefaultTableModel) dockerImagesTable.getModel();
            model.setRowCount(0);

            dockerImages.entrySet().forEach((entry) -> {
                model.addRow(new Object[]{true, entry.getValue().url, 112233});
            });
        }

        /**
         * Remove the selected images from the system. Docker images whose checkbox
         * is deselected are removed from the system
         */
        public void removeImages() {
            getRecords(false).forEach((imageId) -> {
                if (removeDockerImage(imageId)) {
                    dockerImages.remove(imageId);
                }
            });

            updateGUI();
            writeConfiguration();
        }

        /**
         * Return the list of docker images (un)selected.
         * @param selected_flag
         */
        private ArrayList<String> getRecords(boolean selected_flag) {
            ArrayList<String> records = new ArrayList<>();
            DefaultTableModel model = (DefaultTableModel) dockerTable.getModel();

            for (int i = 0; i < model.getRowCount(); i++)
                if (((boolean) model.getValueAt(i, 0)) == selected_flag)
                    records.add((String) model.getValueAt(i,1));

            return records;
        }

        /**
         * Instantiate dockerImages map loading docker images' names from Preferences
         * values */
        private void loadConfiguration() {
            int numImages = getPreferences().getInt(countDockerImagesVariable, 0);

            dockerImages.clear();

            for (int i = 0; i < numImages; i++) {
                String varName = String.format("%s%d", prefixDockerVariable, i);
                String varValue = getPreferences().get(varName, null);

                if (varValue != null)
                    dockerImages.put(varValue, new DockerImageDescription(varValue));
            }
        }

        /**
         *
         */
        private void writeConfiguration() {
            //save number of images
            int numImages = dockerImages.size();
            int index = 0;

            getPreferences().putInt(countDockerImagesVariable, numImages);

            for (String imageUrl: dockerImages.keySet()) {
                String varName = String.format("%s%d", prefixDockerVariable, index);
                getPreferences().put(varName, imageUrl);
                index++;
            }
        }

    }


    private final TabBarController tabsController;
    private final DockerImageManager dockerManager;


    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();

        tabsController = new TabBarController(jTabbedPane1).refreshTabs();
        dockerManager = new DockerImageManager(dockerImagesTable);

        ImageIcon image = new ImageIcon(getClass().getResource("/pkgConnector/images/dna.png"));

        //DefaultTreeCellRenderer renderer =(DefaultTreeCellRenderer) AnalysisTree.getCellRenderer();
        //renderer.setLeafIcon(image);
        //imgURL = getClass().getResource("/pkgConnector/images/dna2.png");
       // ImageIcon image2 = new ImageIcon(imgURL);
        //renderer.setOpenIcon(image2);
       // expandAllNodes(AnalysisTree, 0, AnalysisTree.getRowCount());


        //ADDING PANEL
        BestCLchoice.setViewportView(new BestClusterChoice());
        ClustCurve.setViewportView(new ClustCurvePanel());
        Hestim.setViewportView(new PCAPanel());
        SplinePanel.setViewportView(new SplinePanel());
        ClusterPanel.setViewportView(new ClusterPanel());
        DataVisualPanel.setViewportView(new DataVisualPanel());
        Pestim.setViewportView(new PestimPanel());
        DataImportPanel.setViewportView(new DataImportPanel());
        DiscrPlotPanel.setViewportView(new DiscrPlotPanel());
        DataCuttingPanel.setViewportView(new DataCuttingPanel());
        CountingSample.setViewportView(new CountingSamplePanel());
        ConsMatrix.setViewportView(new ConsensusMatrix());
        DBindex.setViewportView(new DBindex());
//ADDING PANEL

        //indexingSTARPanel

        //DES
        contextMenu.add(pMinText);
        contextMenu.add(pMaxText);
        //indexingBW
        //indexingSalmonPanel
        //countingSalmon

        //filterCountMainFrame.

        //Configuration
        contextMenu.add(Adapter3TextField);
        contextMenu.add(Adapter5TextField);


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        String HorSplPan = getPreferences().get("ConnectorGUI_HorizontalSplitPanel", null);
        if (HorSplPan!=null){
         HorizontalSplitPanel.setDividerLocation(Integer.valueOf(HorSplPan));
        }
        else{
         HorizontalSplitPanel.setDividerLocation(screenSize.height*3/10);
        }

        String VerSplPan = getPreferences().get("ConnectorGUI_VerticalSplitPanel", null);
        if (VerSplPan!=null){
            VerticalSplitPanel.setDividerLocation(Integer.valueOf(VerSplPan));
        }
        else {
        VerticalSplitPanel.setDividerLocation(screenSize.height*7/10);
        }

        String  WindowWidth= getPreferences().get("ConnectorGUI_WindowWidth", null);
        String  WindowHeight= getPreferences().get("ConnectorGUI_WindowHeight", null);
        if ((WindowWidth!=null)&&(WindowHeight!=null)){
            setSize(Integer.valueOf(WindowWidth),Integer.valueOf(WindowHeight));
        }
        else{
            setSize(screenSize.width*95/100,screenSize.height*95/100);
        }

        //OUTPUT FRAME
        int OutputframeWidth= Integer.valueOf(getPreferences().get("ConnectorGUI_WindowOutputWidth", "0"));
        int OutputframeHeight= Integer.valueOf(getPreferences().get("ConnectorGUI_WindowOutputHeight", "0"));

        if ((OutputframeWidth==0)||(OutputframeHeight==0)){
              OutputframeWidth=screenSize.width*4/100;
              OutputframeHeight=screenSize.height*5/100;
        }

        OutputFrame.setSize(OutputframeWidth,OutputframeHeight);


        int DownloadframeWidth= Integer.valueOf(getPreferences().get("ConnectorGUI_WindowDownloadWidth", "0"));
        int DownloadframeHeight= Integer.valueOf(getPreferences().get("ConnectorGUI_WindowDownloadHeight", "0"));
        if ((DownloadframeWidth==0)||(DownloadframeHeight==0)){
              DownloadframeWidth=screenSize.width*4/100;
              DownloadframeHeight=screenSize.height*5/100;
        }

        DownloadFrame.setSize(DownloadframeWidth,DownloadframeHeight);

        String WidthGroup = getPreferences().get("ConnectorGUI_GroupCellWidth", null);
        String WidthBatch = getPreferences().get("ConnectorGUI_BatchCellWidth", null);
        String WidthFolder = getPreferences().get("ConnectorGUI_FolderCellWidth", null);


        String WidthGroup1 = getPreferences().get("ConnectorGUI_Group1CellWidth", null);
        String WidthBatch1 = getPreferences().get("ConnectorGUI_Batch1CellWidth", null);
        String WidthHeader = getPreferences().get("ConnectorGUI_HeaderCellWidth", null);


        setLocationRelativeTo(null);
        invalidate();
        doLayout();



    }
    private static final long serialVersionUID = 5778212334L;


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        OutputFrame = new javax.swing.JFrame();
        jScrollPane3 = new javax.swing.JScrollPane();
        OutputText = new javax.swing.JTextArea();
        CloseOutput = new javax.swing.JToggleButton();
        ReloadOutput = new javax.swing.JToggleButton();
        RemoveOutput = new javax.swing.JButton();
        DlogButton = new javax.swing.JButton();
        ConfigurationFrame = new javax.swing.JFrame();
        jLabel18 = new javax.swing.JLabel();
        ParallelTextField = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        ThreadTextField = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        Adapter5TextField = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        Adapter3TextField = new javax.swing.JTextField();
        ConfCancell = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jMenuItem5 = new javax.swing.JMenuItem();
        CovComboBox = new javax.swing.JComboBox<>();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jRadioButton1 = new javax.swing.JRadioButton();
        MExecution = new javax.swing.ButtonGroup();
        MSeq = new javax.swing.ButtonGroup();
        MRemoveDuplicates = new javax.swing.ButtonGroup();
        BatchComboBox = new javax.swing.JComboBox<>();
        DownloadFrame = new javax.swing.JFrame();
        jButton31 = new javax.swing.JButton();
        jButton32 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jButton33 = new javax.swing.JButton();
        jButton34 = new javax.swing.JButton();
        Downloadtext = new javax.swing.JTextField();
        AboutConnectorGUIFrame = new javax.swing.JFrame();
        jLabel96 = new javax.swing.JLabel();
        jLabel100 = new javax.swing.JLabel();
        jLabel101 = new javax.swing.JLabel();
        jButton36 = new javax.swing.JButton();
        jLabel99 = new javax.swing.JLabel();
        jLabel102 = new javax.swing.JLabel();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        ConfigureTabsFrame = new javax.swing.JFrame();
        enableTabsPanel = new javax.swing.JPanel();
        miRNATabChecker = new javax.swing.JCheckBox();
        singleCellTabChecker = new javax.swing.JCheckBox();
        toolsTabChecker = new javax.swing.JCheckBox();
        circRNATabChecker = new javax.swing.JCheckBox();
        rnaSeqTabChecker = new javax.swing.JCheckBox();
        chipseqTabChecker = new javax.swing.JCheckBox();
        miRNA2TabChecker = new javax.swing.JCheckBox();
        confermConfigureTabButton = new javax.swing.JButton();
        closeConfigureTabButton = new javax.swing.JButton();
        dockerImagesManager = new javax.swing.JFrame();
        jScrollPane1 = new javax.swing.JScrollPane();
        dockerImagesTable = new javax.swing.JTable();
        commandsPanel = new javax.swing.JPanel();
        addImagesButton = new javax.swing.JButton();
        removeImagesButton = new javax.swing.JButton();
        pullImagesButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        manageDockerImagesButton = new javax.swing.JButton();
        jButton26 = new javax.swing.JButton();
        ConfigureTabsButton = new javax.swing.JButton();
        jButton35 = new javax.swing.JButton();
        VerticalSplitPanel = new javax.swing.JSplitPane();
        BottomPanel = new javax.swing.JPanel();
        ProcStatusPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        ProcList = new javax.swing.JList<>();
        HorizontalSplitPanel = new javax.swing.JSplitPane();
        MainPanel = new javax.swing.JPanel();
        Empty = new javax.swing.JScrollPane();
        EmptyPanel = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        ClusterPanel = new javax.swing.JScrollPane();
        DataVisualPanel = new javax.swing.JScrollPane();
        Pestim = new javax.swing.JScrollPane();
        DataImportPanel = new javax.swing.JScrollPane();
        DataCuttingPanel = new javax.swing.JScrollPane();
        CountingSample = new javax.swing.JScrollPane();
        DBindex = new javax.swing.JScrollPane();
        SplinePanel = new javax.swing.JScrollPane();
        BestCLchoice = new javax.swing.JScrollPane();
        DiscrPlotPanel = new javax.swing.JScrollPane();
        ClustCurve = new javax.swing.JScrollPane();
        Hestim = new javax.swing.JScrollPane();
        ConsMatrix = new javax.swing.JScrollPane();
        LeftPanel = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        PreProcessScrollPane = new javax.swing.JScrollPane();
        PreProcPanel = new javax.swing.JPanel();
        DataImportPanelSub1 = new javax.swing.JPanel();
        LabelDataCreation = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        ImportButton = new javax.swing.JButton();
        DataVisualSub2 = new javax.swing.JPanel();
        RNAseqPanelSub2M = new javax.swing.JPanel();
        DataVisualButton = new javax.swing.JButton();
        CutDataButton = new javax.swing.JButton();
        jLabelRNAseq3 = new javax.swing.JLabel();
        ParamEstimScrollPanel = new javax.swing.JScrollPane();
        ParamEstimPanel = new javax.swing.JPanel();
        PestimSub1 = new javax.swing.JPanel();
        jLabelmiRNA1 = new javax.swing.JLabel();
        miRNApanelSub1M = new javax.swing.JPanel();
        PestimButton = new javax.swing.JButton();
        HestimpanelSub2 = new javax.swing.JPanel();
        jLabelmiRNA2 = new javax.swing.JLabel();
        miRNApanelSub2M = new javax.swing.JPanel();
        HestimButton = new javax.swing.JButton();
        ClustEstimationScrollPanel = new javax.swing.JScrollPane();
        FCMPanel = new javax.swing.JPanel();
        SubPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        KestimButton = new javax.swing.JButton();
        ConsMatrixButton = new javax.swing.JButton();
        DBindexButton = new javax.swing.JButton();
        BestClusterButton = new javax.swing.JButton();
        CountingClustScrollPanel = new javax.swing.JScrollPane();
        CountingPanle = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        Subpanel3 = new javax.swing.JPanel();
        CountButton = new javax.swing.JButton();
        ClustPlotsScrollPanel = new javax.swing.JScrollPane();
        ClustEstimPanel = new javax.swing.JPanel();
        SubPanel1ClusEstim = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        ClustCurveButton = new javax.swing.JButton();
        SplineButton = new javax.swing.JButton();
        DiscrPlotButton = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();

        jMenu1.setText("jMenu1");

        jMenu2.setText("jMenu2");

        OutputFrame.setTitle("Process Output");
        OutputFrame.setAlwaysOnTop(true);
        OutputFrame.setLocation(new java.awt.Point(100, 100));
        OutputFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                OutputFrameWindowClosing(evt);
            }
        });
        OutputFrame.getContentPane().setLayout(new java.awt.GridBagLayout());

        OutputText.setEditable(false);
        OutputText.setColumns(20);
        OutputText.setRows(5);
        jScrollPane3.setViewportView(OutputText);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        OutputFrame.getContentPane().add(jScrollPane3, gridBagConstraints);

        CloseOutput.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/33b.png"))); // NOI18N
        CloseOutput.setText("Close");
        CloseOutput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CloseOutputActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        OutputFrame.getContentPane().add(CloseOutput, gridBagConstraints);

        ReloadOutput.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/reset.png"))); // NOI18N
        ReloadOutput.setText("Reload Output");
        ReloadOutput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReloadOutputActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        OutputFrame.getContentPane().add(ReloadOutput, gridBagConstraints);

        RemoveOutput.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/remove.png"))); // NOI18N
        RemoveOutput.setText("Remove Entry");
        RemoveOutput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveOutputActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        OutputFrame.getContentPane().add(RemoveOutput, gridBagConstraints);

        DlogButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/log.png"))); // NOI18N
        DlogButton.setText("Docker Logs");
        DlogButton.setEnabled(false);
        DlogButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DlogButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        OutputFrame.getContentPane().add(DlogButton, gridBagConstraints);

        ConfigurationFrame.setTitle("Configuration");
        ConfigurationFrame.setLocation(new java.awt.Point(200, 200));
        ConfigurationFrame.setName("ConfigurationFrame"); // NOI18N
        ConfigurationFrame.setResizable(false);

        jLabel18.setText("Maximum number of parallel proceses:");

        jLabel19.setText("Maximum number of threads for process:");

        jLabel21.setText("Default adapter 5':");

        jLabel22.setText("Default adapter 3':");

        ConfCancell.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/33b.png"))); // NOI18N
        ConfCancell.setText("Cancel");
        ConfCancell.setToolTipText("");
        ConfCancell.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConfCancellActionPerformed(evt);
            }
        });

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/86b.png"))); // NOI18N
        jButton10.setText("Save&Close");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/reset.png"))); // NOI18N
        jButton11.setText("Reset");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ConfigurationFrameLayout = new javax.swing.GroupLayout(ConfigurationFrame.getContentPane());
        ConfigurationFrame.getContentPane().setLayout(ConfigurationFrameLayout);
        ConfigurationFrameLayout.setHorizontalGroup(
            ConfigurationFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ConfigurationFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ConfigurationFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ConfigurationFrameLayout.createSequentialGroup()
                        .addGroup(ConfigurationFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ConfigurationFrameLayout.createSequentialGroup()
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(ParallelTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE))
                            .addGroup(ConfigurationFrameLayout.createSequentialGroup()
                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(ThreadTextField)))
                        .addGap(627, 627, 627))
                    .addGroup(ConfigurationFrameLayout.createSequentialGroup()
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Adapter5TextField)
                        .addContainerGap())
                    .addGroup(ConfigurationFrameLayout.createSequentialGroup()
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Adapter3TextField)
                        .addContainerGap())))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ConfigurationFrameLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton11)
                .addGap(18, 18, 18)
                .addComponent(jButton10)
                .addGap(18, 18, 18)
                .addComponent(ConfCancell)
                .addContainerGap())
        );
        ConfigurationFrameLayout.setVerticalGroup(
            ConfigurationFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ConfigurationFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ConfigurationFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(ParallelTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(ConfigurationFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(ThreadTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(ConfigurationFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(Adapter5TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(ConfigurationFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(Adapter3TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(ConfigurationFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ConfCancell)
                    .addComponent(jButton10)
                    .addComponent(jButton11))
                .addContainerGap())
        );

        jMenuItem5.setText("jMenuItem5");

        CovComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cov.1", "Cov.2", "Cov.3", "Cov.4", "Cov.5", "Cov.6", "Cov.7", "Cov.8", "Cov.9", "Cov.10", "Cov.11", "Cov.12", "Cov.13", "Cov.14", "Cov.15", "Cov.16", "Cov.17", "Cov.18", "Cov.19", "Cov.20", "Cov.21", "Cov.22", "Cov.23", "Cov.24", "Cov.25", "Cov.26", "Cov.27", "Cov.28", "Cov.29", "Cov.30" }));

        jRadioButton1.setText("jRadioButton1");

        BatchComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Batch1", "Batch2", "Batch3", "Batch4", "Batch5", "Batch6", "Batch7", "Batch8", "Batch9", "Batch10", "Batch11", "Batch12", "Batch13", "Batch14", "Batch15", "Batch16", "Batch17", "Batch18", "Batch19", "Batch20" }));

        DownloadFrame.setTitle("Download Docker imges");
        DownloadFrame.getContentPane().setLayout(new java.awt.GridBagLayout());

        jButton31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/close.png"))); // NOI18N
        jButton31.setText("Cancel");
        jButton31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton31ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 8);
        DownloadFrame.getContentPane().add(jButton31, gridBagConstraints);

        jButton32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/downloadb.png"))); // NOI18N
        jButton32.setText("Download");
        jButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton32ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        DownloadFrame.getContentPane().add(jButton32, gridBagConstraints);

        jPanel1.setBackground(new java.awt.Color(194, 238, 194));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Select a subset of Images (Optional)", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12), new java.awt.Color(0, 102, 51))); // NOI18N
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel12.setText("Container list  file: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(15, 10, 10, 10);
        jPanel1.add(jLabel12, gridBagConstraints);

        jButton33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/33b.png"))); // NOI18N
        jButton33.setText("Cancel");
        jButton33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton33ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jButton33, gridBagConstraints);

        jButton34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/fileb.png"))); // NOI18N
        jButton34.setText("Browse ");
        jButton34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton34ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jButton34, gridBagConstraints);

        Downloadtext.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.weightx = 5.0;
        gridBagConstraints.insets = new java.awt.Insets(15, 10, 10, 10);
        jPanel1.add(Downloadtext, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
        DownloadFrame.getContentPane().add(jPanel1, gridBagConstraints);

        AboutConnectorGUIFrame.setTitle("About ConnectorGUI");
        AboutConnectorGUIFrame.setResizable(false);
        AboutConnectorGUIFrame.getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel96.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/ConnectorGUI.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.01;
        gridBagConstraints.insets = new java.awt.Insets(10, 1, 10, 1);
        AboutConnectorGUIFrame.getContentPane().add(jLabel96, gridBagConstraints);

        jLabel100.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel100.setText("Version 1.0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        AboutConnectorGUIFrame.getContentPane().add(jLabel100, gridBagConstraints);

        jLabel101.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        jLabel101.setText("Copyright © October 2019");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        AboutConnectorGUIFrame.getContentPane().add(jLabel101, gridBagConstraints);

        jButton36.setText("OK");
        jButton36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton36ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        AboutConnectorGUIFrame.getContentPane().add(jButton36, gridBagConstraints);

        jLabel99.setFont(jLabel99.getFont());
        jLabel99.setText("M. Beccuti, R. Calogero and F. Cordero");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 20, 10, 20);
        AboutConnectorGUIFrame.getContentPane().add(jLabel99, gridBagConstraints);
        AboutConnectorGUIFrame.getContentPane().add(jLabel102, new java.awt.GridBagConstraints());

        ConfigureTabsFrame.setTitle("Configure Tabs");
        ConfigureTabsFrame.getContentPane().setLayout(new java.awt.GridBagLayout());

        enableTabsPanel.setBackground(new java.awt.Color(248, 248, 248));
        enableTabsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        enableTabsPanel.setLayout(new java.awt.GridBagLayout());

        miRNATabChecker.setSelected(true);
        miRNATabChecker.setText("miRNA");
        miRNATabChecker.setName("mirna"); // NOI18N
        miRNATabChecker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miRNATabCheckerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        enableTabsPanel.add(miRNATabChecker, gridBagConstraints);

        singleCellTabChecker.setSelected(true);
        singleCellTabChecker.setText("Single Cell");
        singleCellTabChecker.setName("singlecell"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        enableTabsPanel.add(singleCellTabChecker, gridBagConstraints);

        toolsTabChecker.setSelected(true);
        toolsTabChecker.setText("Tools");
        toolsTabChecker.setName("tools"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 5.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        enableTabsPanel.add(toolsTabChecker, gridBagConstraints);

        circRNATabChecker.setSelected(true);
        circRNATabChecker.setText("CircRNA");
        circRNATabChecker.setName("circrna"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        enableTabsPanel.add(circRNATabChecker, gridBagConstraints);

        rnaSeqTabChecker.setSelected(true);
        rnaSeqTabChecker.setText("RNAseq");
        rnaSeqTabChecker.setName("rnaseq"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        enableTabsPanel.add(rnaSeqTabChecker, gridBagConstraints);

        chipseqTabChecker.setSelected(true);
        chipseqTabChecker.setText("ChipSeq");
        chipseqTabChecker.setName("chipseq"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        enableTabsPanel.add(chipseqTabChecker, gridBagConstraints);

        miRNA2TabChecker.setSelected(true);
        miRNA2TabChecker.setText("sncRNA");
        miRNA2TabChecker.setName("mirna2"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        enableTabsPanel.add(miRNA2TabChecker, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        ConfigureTabsFrame.getContentPane().add(enableTabsPanel, gridBagConstraints);

        confermConfigureTabButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/exec.png"))); // NOI18N
        confermConfigureTabButton.setText("Confirm");
        confermConfigureTabButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confermConfigureTabButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 3.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        ConfigureTabsFrame.getContentPane().add(confermConfigureTabButton, gridBagConstraints);

        closeConfigureTabButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/33b.png"))); // NOI18N
        closeConfigureTabButton.setText("Close");
        closeConfigureTabButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeConfigureTabButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.weighty = 3.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        ConfigureTabsFrame.getContentPane().add(closeConfigureTabButton, gridBagConstraints);

        dockerImagesManager.getContentPane().setLayout(new java.awt.GridBagLayout());

        dockerImagesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Flag", "Image name", "Size"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        dockerImagesTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(dockerImagesTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        dockerImagesManager.getContentPane().add(jScrollPane1, gridBagConstraints);

        commandsPanel.setLayout(new java.awt.GridBagLayout());

        addImagesButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/downloadb.png"))); // NOI18N
        addImagesButton.setText("Add");
        addImagesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addImagesButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        commandsPanel.add(addImagesButton, gridBagConstraints);

        removeImagesButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/33b.png"))); // NOI18N
        removeImagesButton.setText("Remove");
        removeImagesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeImagesButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        commandsPanel.add(removeImagesButton, gridBagConstraints);

        pullImagesButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/reset.png"))); // NOI18N
        pullImagesButton.setText("Update");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        commandsPanel.add(pullImagesButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        dockerImagesManager.getContentPane().add(commandsPanel, gridBagConstraints);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ConnectorGUI");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jToolBar1.setBackground(new java.awt.Color(237, 237, 237));
        jToolBar1.setBorder(null);
        jToolBar1.setRollover(true);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/52.png"))); // NOI18N
        jButton1.setToolTipText("Open a previous execution");
        jButton1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 5));
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/86.png"))); // NOI18N
        jButton2.setToolTipText("Save current executions");
        jButton2.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/preference.png"))); // NOI18N
        jButton3.setToolTipText("Configure ConnectorGUI");
        jButton3.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/docker.png"))); // NOI18N
        jButton9.setToolTipText("Remove docker containers");
        jButton9.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jButton9.setFocusable(false);
        jButton9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton9);

        manageDockerImagesButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/manage_docker.png"))); // NOI18N
        manageDockerImagesButton.setToolTipText("Manage Docker images");
        manageDockerImagesButton.setFocusable(false);
        manageDockerImagesButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        manageDockerImagesButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        manageDockerImagesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manageDockerImagesButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(manageDockerImagesButton);

        jButton26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/download.png"))); // NOI18N
        jButton26.setToolTipText("Download docker images");
        jButton26.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jButton26.setFocusable(false);
        jButton26.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton26.setMargin(new java.awt.Insets(2, 140, 2, 140));
        jButton26.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton26);

        ConfigureTabsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/tab.png"))); // NOI18N
        ConfigureTabsButton.setToolTipText("Configure Tabs");
        ConfigureTabsButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        ConfigureTabsButton.setEnabled(false);
        ConfigureTabsButton.setFocusable(false);
        ConfigureTabsButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ConfigureTabsButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ConfigureTabsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConfigureTabsButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(ConfigureTabsButton);

        jButton35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/ClustersEstimSmall.png"))); // NOI18N
        jButton35.setToolTipText("About ConnectorGUI");
        jButton35.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
        jButton35.setFocusable(false);
        jButton35.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton35.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton35ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton35);

        getContentPane().add(jToolBar1, java.awt.BorderLayout.NORTH);

        VerticalSplitPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2));
        VerticalSplitPanel.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        BottomPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        BottomPanel.setLayout(new java.awt.GridBagLayout());

        ProcStatusPanel.setLayout(new java.awt.GridBagLayout());

        jScrollPane2.setBorder(null);

        ProcList.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(20, 1, 1, 1), "Process status", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 14), new java.awt.Color(0, 82, 150))); // NOI18N
        ProcList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        ProcList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ProcListMouseClicked(evt);
            }
        });
        ProcList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                ProcListValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(ProcList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        ProcStatusPanel.add(jScrollPane2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        BottomPanel.add(ProcStatusPanel, gridBagConstraints);

        VerticalSplitPanel.setBottomComponent(BottomPanel);

        HorizontalSplitPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        MainPanel.setBackground(new java.awt.Color(255, 255, 255));
        MainPanel.setLayout(new java.awt.CardLayout());

        Empty.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        EmptyPanel.setBackground(new java.awt.Color(255, 255, 255));
        EmptyPanel.setLayout(new java.awt.GridBagLayout());

        jLabel24.setBackground(new java.awt.Color(255, 255, 255));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/background.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(12, 12, 12, 12);
        EmptyPanel.add(jLabel24, gridBagConstraints);

        Empty.setViewportView(EmptyPanel);

        MainPanel.add(Empty, "Empty");

        ClusterPanel.setBorder(null);
        MainPanel.add(ClusterPanel, "ClusterPanel");

        DataVisualPanel.setBorder(null);
        MainPanel.add(DataVisualPanel, "DataVisual");

        Pestim.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        MainPanel.add(Pestim, "Pestim");

        DataImportPanel.setBorder(null);
        MainPanel.add(DataImportPanel, "DataImport");

        DataCuttingPanel.setBorder(null);
        MainPanel.add(DataCuttingPanel, "DataCut");

        CountingSample.setBorder(null);
        MainPanel.add(CountingSample, "Counting");

        DBindex.setBorder(null);
        MainPanel.add(DBindex, "DBindex");

        SplinePanel.setBorder(null);
        MainPanel.add(SplinePanel, "spline");
        MainPanel.add(BestCLchoice, "BestCl");
        MainPanel.add(DiscrPlotPanel, "MACS");
        MainPanel.add(ClustCurve, "ClustCurvesPlot");
        MainPanel.add(Hestim, "pca_h");
        MainPanel.add(ConsMatrix, "ConsMatrix");

        HorizontalSplitPanel.setRightComponent(MainPanel);

        LeftPanel.setLayout(new java.awt.GridBagLayout());

        jTabbedPane1.setBackground(new java.awt.Color(238, 238, 238));
        jTabbedPane1.setBorder(javax.swing.BorderFactory.createCompoundBorder(null, new javax.swing.border.LineBorder(new java.awt.Color(0, 102, 255), 1, true)));
        jTabbedPane1.setAutoscrolls(true);

        PreProcessScrollPane.setBackground(new java.awt.Color(51, 153, 255));
        PreProcessScrollPane.setBorder(javax.swing.BorderFactory.createCompoundBorder(null, javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0))));
        PreProcessScrollPane.setHorizontalScrollBar(null);
        PreProcessScrollPane.setName("rnaseq"); // NOI18N

        PreProcPanel.setBackground(new java.awt.Color(255, 255, 255));
        PreProcPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        PreProcPanel.setName("rnaseq"); // NOI18N
        PreProcPanel.setLayout(new java.awt.GridBagLayout());

        DataImportPanelSub1.setBackground(new java.awt.Color(255, 255, 255));
        DataImportPanelSub1.setLayout(new java.awt.GridBagLayout());

        LabelDataCreation.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        LabelDataCreation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/PreProcSmall.png"))); // NOI18N
        LabelDataCreation.setText("Connector List Creation");
        LabelDataCreation.setOpaque(true);
        LabelDataCreation.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LabelDataCreationMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 320;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        DataImportPanelSub1.add(LabelDataCreation, gridBagConstraints);
        LabelDataCreation.getAccessibleContext().setAccessibleName("Connector List Generation");
        LabelDataCreation.getAccessibleContext().setAccessibleDescription("");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        ImportButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/Import.png"))); // NOI18N
        ImportButton.setText("Data Import");
        ImportButton.setBorderPainted(false);
        ImportButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ImportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ImportButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel2.add(ImportButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        DataImportPanelSub1.add(jPanel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        PreProcPanel.add(DataImportPanelSub1, gridBagConstraints);

        DataVisualSub2.setBackground(new java.awt.Color(255, 255, 255));
        DataVisualSub2.setLayout(new java.awt.GridBagLayout());

        RNAseqPanelSub2M.setBackground(new java.awt.Color(255, 255, 255));
        RNAseqPanelSub2M.setLayout(new java.awt.GridBagLayout());

        DataVisualButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/Curves.png"))); // NOI18N
        DataVisualButton.setText("Growth curves and Time grid");
        DataVisualButton.setBorderPainted(false);
        DataVisualButton.setHideActionText(true);
        DataVisualButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        DataVisualButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DataVisualButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        RNAseqPanelSub2M.add(DataVisualButton, gridBagConstraints);

        CutDataButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/CurvesCut.png"))); // NOI18N
        CutDataButton.setText("Data Cutting");
        CutDataButton.setBorderPainted(false);
        CutDataButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        CutDataButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CutDataButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        RNAseqPanelSub2M.add(CutDataButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        DataVisualSub2.add(RNAseqPanelSub2M, gridBagConstraints);

        jLabelRNAseq3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabelRNAseq3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/PreProcSmall.png"))); // NOI18N
        jLabelRNAseq3.setText("Data Visualization");
        jLabelRNAseq3.setMaximumSize(new java.awt.Dimension(224, 31));
        jLabelRNAseq3.setMinimumSize(new java.awt.Dimension(224, 31));
        jLabelRNAseq3.setOpaque(true);
        jLabelRNAseq3.setPreferredSize(new java.awt.Dimension(224, 31));
        jLabelRNAseq3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelRNAseq3MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 320;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        DataVisualSub2.add(jLabelRNAseq3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        PreProcPanel.add(DataVisualSub2, gridBagConstraints);

        PreProcessScrollPane.setViewportView(PreProcPanel);

        jTabbedPane1.addTab("Pre Processing", new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/PreProc.png")), PreProcessScrollPane); // NOI18N

        ParamEstimScrollPanel.setBackground(new java.awt.Color(51, 153, 255));
        ParamEstimScrollPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(null, javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0))));
        ParamEstimScrollPanel.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        ParamEstimScrollPanel.setName("mirna"); // NOI18N

        ParamEstimPanel.setBackground(new java.awt.Color(255, 255, 255));
        ParamEstimPanel.setName("ParameterEstimation"); // NOI18N
        ParamEstimPanel.setLayout(new java.awt.GridBagLayout());

        PestimSub1.setBackground(new java.awt.Color(255, 255, 255));
        PestimSub1.setLayout(new java.awt.GridBagLayout());

        jLabelmiRNA1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabelmiRNA1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/ParamEstimSmall_p.png"))); // NOI18N
        jLabelmiRNA1.setText("p-estimation");
        jLabelmiRNA1.setMaximumSize(new java.awt.Dimension(224, 31));
        jLabelmiRNA1.setMinimumSize(new java.awt.Dimension(224, 31));
        jLabelmiRNA1.setOpaque(true);
        jLabelmiRNA1.setPreferredSize(new java.awt.Dimension(224, 31));
        jLabelmiRNA1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelmiRNA1MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 320;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        PestimSub1.add(jLabelmiRNA1, gridBagConstraints);

        miRNApanelSub1M.setBackground(new java.awt.Color(255, 255, 255));
        miRNApanelSub1M.setLayout(new java.awt.GridBagLayout());

        PestimButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/CrossLogLike.png"))); // NOI18N
        PestimButton.setText("Cross LogLikelihood");
        PestimButton.setBorderPainted(false);
        PestimButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PestimButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        miRNApanelSub1M.add(PestimButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        PestimSub1.add(miRNApanelSub1M, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        ParamEstimPanel.add(PestimSub1, gridBagConstraints);

        HestimpanelSub2.setBackground(new java.awt.Color(255, 255, 255));
        HestimpanelSub2.setLayout(new java.awt.GridBagLayout());

        jLabelmiRNA2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabelmiRNA2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/ParamEstimSmall_h.png"))); // NOI18N
        jLabelmiRNA2.setText("h-estimation");
        jLabelmiRNA2.setMaximumSize(new java.awt.Dimension(224, 31));
        jLabelmiRNA2.setMinimumSize(new java.awt.Dimension(224, 31));
        jLabelmiRNA2.setOpaque(true);
        jLabelmiRNA2.setPreferredSize(new java.awt.Dimension(224, 31));
        jLabelmiRNA2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelmiRNA2MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 320;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        HestimpanelSub2.add(jLabelmiRNA2, gridBagConstraints);

        miRNApanelSub2M.setBackground(new java.awt.Color(255, 255, 255));
        miRNApanelSub2M.setLayout(new java.awt.GridBagLayout());

        HestimButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/PCA.png"))); // NOI18N
        HestimButton.setText("PCA");
        HestimButton.setBorderPainted(false);
        HestimButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HestimButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        miRNApanelSub2M.add(HestimButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        HestimpanelSub2.add(miRNApanelSub2M, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        ParamEstimPanel.add(HestimpanelSub2, gridBagConstraints);

        ParamEstimScrollPanel.setViewportView(ParamEstimPanel);

        jTabbedPane1.addTab("Parameter Estimation", new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/ParamEstim.png")), ParamEstimScrollPanel); // NOI18N

        ClustEstimationScrollPanel.setBackground(new java.awt.Color(51, 153, 255));
        ClustEstimationScrollPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(null, javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0))));
        ClustEstimationScrollPanel.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        ClustEstimationScrollPanel.setName("chipseq"); // NOI18N

        FCMPanel.setBackground(new java.awt.Color(255, 255, 255));
        FCMPanel.setAutoscrolls(true);
        FCMPanel.setName("Clustering"); // NOI18N
        FCMPanel.setPreferredSize(new java.awt.Dimension(547, 225));
        FCMPanel.setLayout(new java.awt.GridBagLayout());

        SubPanel1.setBackground(new java.awt.Color(255, 255, 255));
        SubPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/ClustersEstimSmall.png"))); // NOI18N
        jLabel1.setText("Number of clusters estimation");
        jLabel1.setToolTipText("");
        jLabel1.setMaximumSize(new java.awt.Dimension(224, 31));
        jLabel1.setMinimumSize(new java.awt.Dimension(224, 31));
        jLabel1.setOpaque(true);
        jLabel1.setPreferredSize(new java.awt.Dimension(224, 31));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 320;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        SubPanel1.add(jLabel1, gridBagConstraints);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel5.setLayout(new java.awt.GridBagLayout());

        KestimButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/Clusters.png"))); // NOI18N
        KestimButton.setText("FCM ");
        KestimButton.setBorderPainted(false);
        KestimButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KestimButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel5.add(KestimButton, gridBagConstraints);

        ConsMatrixButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/ConsMatrix.png"))); // NOI18N
        ConsMatrixButton.setText("Consensus Matrix ");
        ConsMatrixButton.setBorderPainted(false);
        ConsMatrixButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConsMatrixButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel5.add(ConsMatrixButton, gridBagConstraints);

        DBindexButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/ClustersInfo.png"))); // NOI18N
        DBindexButton.setText("DB indexes");
        DBindexButton.setBorderPainted(false);
        DBindexButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DBindexButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel5.add(DBindexButton, gridBagConstraints);

        BestClusterButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/ClustersOptim.png"))); // NOI18N
        BestClusterButton.setText("Best Cluster extrapolation");
        BestClusterButton.setBorderPainted(false);
        BestClusterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BestClusterButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel5.add(BestClusterButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        SubPanel1.add(jPanel5, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        FCMPanel.add(SubPanel1, gridBagConstraints);

        ClustEstimationScrollPanel.setViewportView(FCMPanel);

        jTabbedPane1.addTab("Clustering Estimation", new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/ClustersEstim.png")), ClustEstimationScrollPanel); // NOI18N

        CountingPanle.setBackground(new java.awt.Color(255, 255, 255));
        CountingPanle.setLayout(new java.awt.GridBagLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/ClustersCountSmall.png"))); // NOI18N
        jLabel3.setText("Clustering Examination");
        jLabel3.setMaximumSize(new java.awt.Dimension(224, 31));
        jLabel3.setMinimumSize(new java.awt.Dimension(224, 31));
        jLabel3.setOpaque(true);
        jLabel3.setPreferredSize(new java.awt.Dimension(224, 31));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 320;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        jPanel3.add(jLabel3, gridBagConstraints);

        Subpanel3.setBackground(new java.awt.Color(255, 255, 255));
        Subpanel3.setLayout(new java.awt.GridBagLayout());

        CountButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/ClustersCountButt.png"))); // NOI18N
        CountButton.setText("Counting");
        CountButton.setBorderPainted(false);
        CountButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CountButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        Subpanel3.add(CountButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel3.add(Subpanel3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        CountingPanle.add(jPanel3, gridBagConstraints);

        CountingClustScrollPanel.setViewportView(CountingPanle);

        jTabbedPane1.addTab("Cluster Curves Examination", new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/ClustersCount.png")), CountingClustScrollPanel); // NOI18N

        ClustEstimPanel.setBackground(new java.awt.Color(255, 255, 255));
        ClustEstimPanel.setLayout(new java.awt.GridBagLayout());

        SubPanel1ClusEstim.setLayout(new java.awt.GridBagLayout());

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/ClustPlotsSmall.png"))); // NOI18N
        jLabel2.setText("Clustering Plots");
        jLabel2.setMaximumSize(new java.awt.Dimension(224, 31));
        jLabel2.setMinimumSize(new java.awt.Dimension(224, 31));
        jLabel2.setOpaque(true);
        jLabel2.setPreferredSize(new java.awt.Dimension(224, 31));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 320;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        SubPanel1ClusEstim.add(jLabel2, gridBagConstraints);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setLayout(new java.awt.GridBagLayout());

        ClustCurveButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/ClustPlotButt.png"))); // NOI18N
        ClustCurveButton.setText("Cluster Curves");
        ClustCurveButton.setBorderPainted(false);
        ClustCurveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClustCurveButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel6.add(ClustCurveButton, gridBagConstraints);

        SplineButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/spline.png"))); // NOI18N
        SplineButton.setText("Fitting Plots");
        SplineButton.setBorderPainted(false);
        SplineButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SplineButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel6.add(SplineButton, gridBagConstraints);

        DiscrPlotButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/DiscrPlot.png"))); // NOI18N
        DiscrPlotButton.setText("Discrimination Plot");
        DiscrPlotButton.setBorderPainted(false);
        DiscrPlotButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DiscrPlotButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel6.add(DiscrPlotButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        SubPanel1ClusEstim.add(jPanel6, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        ClustEstimPanel.add(SubPanel1ClusEstim, gridBagConstraints);

        ClustPlotsScrollPanel.setViewportView(ClustEstimPanel);

        jTabbedPane1.addTab("Clustering Plots", new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/ClustPlots.png")), ClustPlotsScrollPanel); // NOI18N

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        LeftPanel.add(jTabbedPane1, gridBagConstraints);
        jTabbedPane1.getAccessibleContext().setAccessibleDescription("");

        HorizontalSplitPanel.setLeftComponent(LeftPanel);

        VerticalSplitPanel.setTopComponent(HorizontalSplitPanel);

        getContentPane().add(VerticalSplitPanel, java.awt.BorderLayout.CENTER);

        jMenuBar1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 3, 5, 3));

        jMenu3.setText("File");

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/52.png"))); // NOI18N
        jMenuItem3.setText("Open");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem3);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/86.png"))); // NOI18N
        jMenuItem4.setText("Save");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem4);

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/33.png"))); // NOI18N
        jMenuItem6.setText("Exit");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem6);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Edit");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/preference.png"))); // NOI18N
        jMenuItem1.setText("Configure ConnectorGUI");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem1);

        jMenuItem8.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/download.png"))); // NOI18N
        jMenuItem8.setText("Download Docker Images");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem8);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/docker.png"))); // NOI18N
        jMenuItem2.setText("Remove Docker Containers");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem2);

        jMenuItem9.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/tab.png"))); // NOI18N
        jMenuItem9.setText("Configure Tabs");
        jMenuItem9.setEnabled(false);
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem9);

        jMenuBar1.add(jMenu4);

        jMenu5.setText("?");
        jMenu5.setToolTipText("");

        jMenuItem7.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/iconDNA-small.png"))); // NOI18N
        jMenuItem7.setText("About ConnectorGUI");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem7);

        jMenuBar1.add(jMenu5);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

        public static final Pattern adapter = Pattern.compile("[acgtACGT]+"
);

        public static final Pattern org = Pattern.compile("[a-zA-Z0-9]+"
);

        public static final Pattern miRBase = Pattern.compile("[a-zA-Z0-9]{3,3}"
);



    private void DownloadMenuItemActionPerformed(java.awt.event.ActionEvent evt){

        DownloadFrame.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int DownloadframeWidth= Integer.valueOf(getPreferences().get("ConnectorGUI_WindowDownloadWidth", "0"));
        int DownloadframeHeight= Integer.valueOf(getPreferences().get("ConnectorGUI_WindowDownloadHeight", "0"));
        if ((DownloadframeWidth==0)||(DownloadframeHeight==0)){
              DownloadframeWidth=screenSize.width*50/100;
              DownloadframeHeight=screenSize.height*20/100;
        }

        DownloadFrame.setSize(DownloadframeWidth,DownloadframeHeight);

        DownloadFrame.setVisible(true);
    }


    private void configurationMenuItemActionPerformed(java.awt.event.ActionEvent evt){
        ConfigurationFrame.pack();
        ConfigurationFrame.setVisible(true);
        ParallelTextField.setText(Integer.toString(GS.getMaxSizelistProcRunning()));
        ThreadTextField.setText(Integer.toString(GS.getDefaultThread()));
        Adapter5TextField.setText(GS.getDefaultAdapter5());
        Adapter3TextField.setText(GS.getDefaultAdapter3());

    }

    private boolean  removeDockerImage(String imageName){
        boolean returnValue = false;
        String[] cmd = {
            "/bin/bash",
            "-c",
            " docker rmi " + imageName
        };

        try {
            Runtime.getRuntime().exec(cmd);
            JOptionPane.showMessageDialog(this,
                "Docker image " + imageName + " has been removed.",
                "Confermation",
                JOptionPane.INFORMATION_MESSAGE);
            returnValue = true;
        }
        catch (IOException e){
            System.out.println("Docker image has not been removed\n");
        }
        return returnValue;
    }

    private void  removeDockerContainer(java.awt.event.ActionEvent evt){
        String[] cmd = {
            "/bin/bash",
            "-c",
            " docker rm $(docker ps -q -f status=exited);" +
            " docker rm $(docker ps -q -f status=dead)"
        };
        try {
            Runtime.getRuntime().exec(cmd);
            JOptionPane.showMessageDialog(this, "All docker containers were removed!!","Confermation",JOptionPane.INFORMATION_MESSAGE);
        }
        catch (IOException e){
            System.out.println("Docker containers were not removed\n");
        }
    }


      public void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {


        if (!"Empty".equals(CurrentLayout)){
            JFileChooser openFile = new JFileChooser();
            String curDir = getPreferences().get("saved-file", null);
            openFile.setCurrentDirectory(curDir!=null ? new File(curDir) : null);
            if (openFile.showOpenDialog(this)==JFileChooser.APPROVE_OPTION){
                try{
                File f = openFile.getSelectedFile();
                FileReader fw = new FileReader(f.getAbsoluteFile());
                    try (BufferedReader br = new BufferedReader(fw)) {
                        if ("vmRNA".equals(CurrentLayout)){
                           
                        }
                        else
                            if ( "mRNA".equals(CurrentLayout)){
                                
                                
                            }
                            else
                                if ("ClusterPanel".equals(CurrentLayout)){/*
                                    int line=0;
                                    //chekc right file
                                    if (!br.readLine().equals("indexing")){
                                        throw(new NumberFormatException());
                                    }
                                    for (String x = br.readLine(); x != null; x = br.readLine()){
                                        switch (line){
                                            case 0:
                                                if (x.equals("sudo"))
                                                    iSudoRadioButton.setSelected(true);
                                                else
                                                    iDockerRadioButton.setSelected(true);
                                                break;
                                            case 1:
                                                iGenomeFolderText.setText(x);
                                                break;
                                            case 2:
                                                iThreadText.setText(x);
                                                break;
                                            case 3:
                                                iGenomeURLText.setText(x);
                                                break;
                                            case 4:
                                                iGTFURLText.setText(x);
                                                break;
                                            default:
                                                throw(new NumberFormatException());
                                        }
                                        line++;
                                    }
                                */}
                                else
                                    if ("FPKMCounting".equals(CurrentLayout)){
                                        
                                    }
                                    else
                                        if("DataVisual".equals(CurrentLayout)){
                                            /*
                                            if (!br.readLine().equals("DataVisual")){
                                                throw(new NumberFormatException());
                                            }
                                            int line=0;
                                            for (String x = br.readLine(); x != null; x = br.readLine()){
                                                switch (line){
                                                    case 0:
                                                        pFPKMfileText.setText(x);
                                                        break;
                                                    case 1:
                                                        pComponent1Text.setText(x);
                                                        break;
                                                    case 2:
                                                        pComponent2Text.setText(x);
                                                        break;
                                                    case 3:
                                                        switch (x) {
                                                            case "Counts":
                                                                pCountsRadioButton.setSelected(true);
                                                                break;
                                                            case "FPKM":
                                                                pFPKMRadioButton.setSelected(true);
                                                                break;
                                                            default:
                                                                pTPMRadioButton.setSelected(true);
                                                                break;
                                                        }
                                                        break;
                                                    case 4:
                                                        pLegendComboBox.setSelectedIndex(Integer.valueOf(x));
                                                        break;
                                                    case 5:
                                                        if (x.equals("Yes"))
                                                            pCovYesRadioButton.setSelected(true);
                                                        else
                                                            pCovNoRadioButton.setSelected(true);
                                                        break;
                                                    case 6:
                                                        pOutputFolderText.setText(x);
                                                        break;
                                                }
                                                line++;
                                            }
                                        */
                                        }
                                        else
                                            if("Pestim".equals(CurrentLayout)){/*
                                                if (!br.readLine().equals("DES")){
                                                    throw(new NumberFormatException());
                                                }
                                                int line=0;
                                                for (String x = br.readLine(); x != null; x = br.readLine()){
                                                    switch (line){
                                                        case 0:
                                                            dFPKMfileText.setText(x);
                                                            break;
                                                        case 1:
                                                            dLog2fcText.setText(x);
                                                            break;
                                                        case 2:
                                                            dFDRText.setText(x);
                                                            break;
                                                        case 3:
                                                            dCovComboBox.setSelectedIndex(Integer.valueOf(x));
                                                            break;
                                                        case 4:
                                                            switch (x) {
                                                                case "isoform":
                                                                    dIsoformRadioButton.setSelected(true);
                                                                    break;
                                                                case "miRNA":
                                                                    dmiRNARadioButton.setSelected(true);
                                                                    break;
                                                                default:
                                                                    dGeneRadioButton.setSelected(true);
                                                                    break;
                                                            }
                                                            break;
                                                        case 5:
                                                            dOutputFolderText.setText(x);
                                                            break;
                                                        case 6:
                                                            if (x.equals("TRUE"))
                                                                dBatchesTrue.setSelected(true);
                                                            else
                                                                DBatchesFalse.setSelected(true);
                                                            break;
                                                    }
                                                    line++;
                                                }
                                                */
                                            }
                                            else
                                                if ("DataImport".equals(CurrentLayout)){/*
                                                    int line=0;
                                                    //chekc right file
                                                    if (!br.readLine().equals("DataImport")){
                                                        throw(new NumberFormatException());
                                                    }
                                                    for (String x = br.readLine(); x != null; x = br.readLine()){
                                                        switch (line){
                                                            case 0:
                                                                if (x.equals("sudo"))
                                                                    iSudoRadioButton.setSelected(true);
                                                                else
                                                                    iDockerRadioButton.setSelected(true);
                                                                break;
                                                            case 1:
                                                                iGenomeFolderBText.setText(x);
                                                                break;
                                                            case 2:
                                                                iThreadBText.setText(x);
                                                                break;
                                                            case 3:
                                                                iGenomeURLBText.setText(x);
                                                                break;
                                                            case 4:
                                                                if (x.equals("true"))
                                                                    iBTrueRadioButton.setSelected(true);
                                                                else
                                                                    iBFalseRadioButton.setSelected(true);
                                                                break;
                                                            case 5:
                                                                i1000GenomeText.setText(x);
                                                                break;
                                                            case 6:
                                                                idbSPNText.setText(x);
                                                                break;
                                                            default:
                                                                throw(new NumberFormatException());
                                                        }
                                                        line++;
                                                    }
                                                */}
                                                else
                                                    if ("MACS".equals(CurrentLayout)){
                                                        /*
                                                        int line=0;
                                                        //chekc right file
                                                        if (!br.readLine().equals("MACS")){
                                                            throw(new NumberFormatException());
                                                        }
                                                        for (String x = br.readLine(); x != null; x = br.readLine()){
                                                            switch (line){
                                                                case 0:
                                                                    if (x.equals("sudo"))
                                                                        MSudoRadioButton.setSelected(true);
                                                                    else
                                                                        MDockerRadioButton.setSelected(true);
                                                                    break;
                                                                case 1:
                                                                    MOutputFolderText.setText(x);
                                                                    break;
                                                                case 2:
                                                                    MScratchFolderText.setText(x);
                                                                    break;
                                                                case 3:
                                                                    MGenomeFolderText.setText(x);
                                                                    break;
                                                                case 4:
                                                                    MTestFolderText.setText(x);
                                                                    break;
                                                                case 5:
                                                                    MMockFolderText.setText(x);
                                                                    break;
                                                                case 6:
                                                                    MThreadText.setText(x);
                                                                    break;
                                                                case 7:
                                                                    MAdapter5Text.setText(x);
                                                                    break;
                                                                case 8:
                                                                    MAdapter3Text.setText(x);
                                                                    break;
                                                                case 9:
                                                                    MMinLengthText.setText(x);
                                                                    break;
                                                                case 10:
                                                                    MReadLengthTextField.setText(x);
                                                                    break;
                                                                case 11:
                                                                    MOrgComboBox.setSelectedIndex(Integer.valueOf(x));
                                                                    break;
                                                                case 12:
                                                                    if (x.equals("no"))
                                                                        MRnoRadioButton.setSelected(true);
                                                                    else
                                                                        MRyesRadioButton.setSelected(true);
                                                                    break;
                                                                case 13:
                                                                    MMaxUpstreamText.setText(x);
                                                                    break;
                                                                case 14:
                                                                    MTTSText.setText(x);
                                                                    break;
                                                                case 15:
                                                                    MMinmfoldText.setText(x);
                                                                    break;
                                                                case 16:
                                                                    MMpvalueText.setText(x);
                                                                    break;
                                                                case 17:
                                                                    MMaxmfoldText.setText(x);
                                                                    break;
                                                                case 18:
                                                                    MSWindowsText.setText(x);
                                                                    break;
                                                                case 19:
                                                                    MSgsizeText.setText(x);
                                                                    break;
                                                                case 20:
                                                                    MSFDRText.setText(x);
                                                                    break;
                                                                case 21:
                                                                    MToolComboBox.setSelectedIndex(Integer.valueOf(x));
                                                                    break;
                                                                default:
                                                                    throw(new NumberFormatException());
                                                            }
                                                            line++;
                                                        }
                                                    */}
                                                    else
                                                        if("SampleSize".equals(CurrentLayout)){
                                                            /*
                                                            int line=0;
                                                            //chekc right file
                                                            if (!br.readLine().equals("SampleSize")){
                                                                throw(new NumberFormatException());
                                                            }
                                                            for (String x = br.readLine(); x != null; x = br.readLine()){
                                                                switch (line){
                                                                    case 0:
                                                                        SSCountTableText.setText(x);
                                                                        break;
                                                                    case 1:
                                                                        SSOutputFolderText.setText(x);
                                                                        break;
                                                                    case 2:
                                                                        SSPowerText.setText(x);
                                                                        break;
                                                                    case 3:
                                                                        SSGeneText.setText(x);
                                                                        break;
                                                                    case 4:
                                                                        SSlog2Text.setText(x);
                                                                        break;
                                                                    case 5:
                                                                        SSFDRtext.setText(x);
                                                                        break;
                                                                    default:
                                                                        throw(new NumberFormatException());
                                                                }
                                                                line++;

                                                            }

                                                        */}
                                                        else
                                                            if("ExperimentPower".equals(CurrentLayout)){/*


                                                                //chekc right file
                                                                if (!br.readLine().equals("ExperimentPower")){
                                                                    throw(new NumberFormatException());
                                                                }
                                                                int line=0;
                                                                for (String x = br.readLine(); x != null; x = br.readLine()){
                                                                    switch (line){
                                                                        case 0:
                                                                            EPCountTableText.setText(x);
                                                                            break;
                                                                        case 1:
                                                                            EPOutputFolderText.setText(x);
                                                                            break;
                                                                        case 2:
                                                                            EPSampleText.setText(x);
                                                                            break;
                                                                        case 3:
                                                                            EPGeneText.setText(x);
                                                                            break;
                                                                        case 4:
                                                                            EPlog2Text.setText(x);
                                                                            break;
                                                                        case 5:
                                                                            EPFDRtext.setText(x);
                                                                            break;
                                                                        default:
                                                                            throw(new NumberFormatException());
                                                                    }
                                                                    line++;

                                                                }
                                                                */
                                                            }
                                                            else
                                                                if("Counting".equals(CurrentLayout)){/*
                                                                    if (!br.readLine().equals("AddingCovmRNA")){
                                                                        throw(new NumberFormatException());
                                                                    }
                                                                    int line=0;
                                                                    for (String x = br.readLine(); x != null; x = br.readLine()){
                                                                        switch (line){
                                                                            case 0:
                                                                                CCovInputFileText.setText(x);
                                                                                break;
                                                                            case 1:
                                                                                COutputFolderText.setText(x);
                                                                                break;
                                                                            default:
                                                                                DefaultTableModel model = (DefaultTableModel) CCountHeaderTable.getModel();
                                                                                String col2 = br.readLine();
                                                                                String col3 = br.readLine();
                                                                                if (col2==null)
                                                                                    col2="Cov.1";
                                                                                if (col3==null)
                                                                                    col3="Batch1";
                                                                                model.addRow(new Object[]{x,col2,col3});
                                                                        }
                                                                        line++;
                                                                    }
                                                                */
                                                                }
                                                                else
                                                                    if ("indexingSalmon".equals(CurrentLayout)){/*
                                                                        //chekc right file
                                                                        if (!br.readLine().equals("indexingSalmon")){
                                                                            throw(new NumberFormatException());
                                                                        }
                                                                        int line=0;
                                                                        for (String x = br.readLine(); x != null; x = br.readLine()){
                                                                            switch (line){
                                                                                case 0:
                                                                                    if (x.equals("sudo"))
                                                                                        iSudoRadioSButton.setSelected(true);
                                                                                    else
                                                                                        iDockerRadioSButton.setSelected(true);
                                                                                    break;
                                                                                case 1:
                                                                                    iGenomeFolderSText.setText(x);
                                                                                    break;
                                                                                case 2:
                                                                                    iThreadSText.setText(x);
                                                                                    break;
                                                                                case 3:
                                                                                    iGenomeURLSText.setText(x);
                                                                                    break;
                                                                                case 4:
                                                                                    iGTFURLSText.setText(x);
                                                                                    break;
                                                                                case 5:
                                                                                    iKmerSText.setText(x);
                                                                                    break;
                                                                                default:
                                                                                    throw(new NumberFormatException());
                                                                            }
                                                                            line++;
                                                                        }*/
                                                                    }
                                                                    else
                                                                        if ("countingSalmon".equals(CurrentLayout)){/*

                                                                            //chekc right file
                                                                            String y=br.readLine();
                                                                            if (!y.equals("countingSalmon")){
                                                                                throw(new NumberFormatException());
                                                                            }
                                                                            int line=0;
                                                                            for (String x = br.readLine(); x != null; x = br.readLine()){
                                                                                switch (line){
                                                                                    case 0:
                                                                                        if (x.equals("sudo"))
                                                                                            cSudoRadioButton.setSelected(true);
                                                                                        else
                                                                                            cDockerRadioButton.setSelected(true);
                                                                                        break;
                                                                                    case 1:
                                                                                        cFastQFolderText.setText(x);
                                                                                        break;
                                                                                    case 2:
                                                                                        cOutputFolderText.setText(x);
                                                                                        break;
                                                                                    case 3:
                                                                                        cAdapter5Text.setText(x);
                                                                                        break;
                                                                                    case 4:
                                                                                        cAdapter3Text.setText(x);
                                                                                        break;
                                                                                    case 5:
                                                                                        if (x.equals("se"))
                                                                                            cSeRadioButton.setSelected(true);
                                                                                        else
                                                                                            cPeRadioButton.setSelected(true);
                                                                                        break;
                                                                                    case 6:
                                                                                        cThreadText.setText(x);
                                                                                        break;
                                                                                    case 7:
                                                                                        cMinLengthText.setText(x);
                                                                                        break;
                                                                                    case 8:
                                                                                        cGenomeFolderText.setText(x);
                                                                                        break;
                                                                                    case 9:
                                                                                        switch (x) {
                                                                                            case "none":
                                                                                                cSNoneRadioButton.setSelected(true);
                                                                                                break;
                                                                                            case "forward":
                                                                                                cSForwardRadioButton.setSelected(true);
                                                                                                break;
                                                                                            default:
                                                                                                cSReverseRadioButton.setSelected(true);
                                                                                                break;
                                                                                        }
                                                                                        break;
                                                                                    default:
                                                                                        System.out.print("Too much lines\n");
                                                                                        throw(new NumberFormatException());
                                                                                }
                                                                                line++;
                                                                            }*/
                                                                        }
                                                                        else
                                                                            if("SplinePanel".equals(CurrentLayout)){/*
                                                                               if (!br.readLine().equals("filtercounts")){
                                                                                    throw(new NumberFormatException());
                                                                                }
                                                                                int line=0;
                                                                                for (String x = br.readLine(); x != null; x = br.readLine()){
                                                                                    switch (line){
                                                                                        case 0:
                                                                                            fFPKMfileText.setText(x);
                                                                                            break;
                                                                                        case 1:
                                                                                            switch (x) {
                                                                                                case "gene":
                                                                                                    fgeneRadioButton.setSelected(true);
                                                                                                    break;
                                                                                                case "isoform":
                                                                                                    fisoformRadioButton.setSelected(true);
                                                                                                    break;
                                                                                                default:
                                                                                                    fmirnaRadioButton.setSelected(true);
                                                                                                    break;
                                                                                            }
                                                                                            break;
                                                                                        case 2:
                                                                                            fOutputFolderText.setText(x);
                                                                                            break;
                                                                                    }
                                                                                    line++;
                                                                                }*/
                                                                            }
                        getPreferences().put("saved-file",openFile.getCurrentDirectory().getAbsolutePath());
                    }
                }
                catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error opening file","Error",JOptionPane.ERROR_MESSAGE);
                }
                catch (NumberFormatException e) {
                  JOptionPane.showMessageDialog(this, "Error reading file","Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public void saveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        if (!"Empty".equals(CurrentLayout)){
            JFileChooser saveFile = new JFileChooser();
            String curDir = getPreferences().get("saved-file", null);
            saveFile.setCurrentDirectory(curDir!=null ? new File(curDir) : null);
            if (saveFile.showSaveDialog(this)==JFileChooser.APPROVE_OPTION){
                try
                {
                    File f = saveFile.getSelectedFile();
                    if (!f.exists()) {
                        f.createNewFile();
                    }
                    FileWriter fw = new FileWriter(f.getAbsoluteFile());
                    try (BufferedWriter bw = new BufferedWriter(fw)) {
                        if ("vmRNA".equals(CurrentLayout)){
                            
                        }
                        else
                            if ( CurrentLayout=="mRNA"){
                                
                            }
                            else{
                                if ("ClusterPanel".equals(CurrentLayout)){/*
                                    bw.write("indexingSTAR\n");
                                    if (iSudoRadioButton.isSelected()){
                                        bw.write("sudo\n");
                                    }
                                    else{
                                        bw.write("docker\n");
                                    }

                                    bw.write(iGenomeFolderText.getText());
                                    bw.write("\n");
                                    bw.write(iThreadText.getText());
                                    bw.write("\n");
                                    bw.write(iGenomeURLText.getText());
                                    bw.write("\n");
                                    bw.write(iGTFURLText.getText());*/
                                }
                                else
                                    if("FPKMCounting".equals(CurrentLayout)){
                               
                                    }
                                    else
                                        if("DataVisual".equals(CurrentLayout)){/*
                                            bw.write("PCA\n");
                                            bw.write(pFPKMfileText.getText());
                                            bw.write("\n");
                                            bw.write(pComponent1Text.getText());
                                            bw.write("\n");
                                            bw.write(pComponent2Text.getText());
                                            bw.write("\n");
                                            if (pCountsRadioButton.isSelected()){
                                                bw.write("Counts\n");
                                            }
                                            else
                                                if (pFPKMRadioButton.isSelected()){
                                                    bw.write("FPKM\n");
                                                }
                                                else{
                                                    bw.write("TPM\n");
                                                }
                                            bw.write(Integer.toString(pLegendComboBox.getSelectedIndex()));
                                            bw.write("\n");
                                            if (pCovYesRadioButton.isSelected()){
                                                bw.write("Yes\n");
                                            }
                                            else
                                                bw.write("No\n");
                                            bw.write(pOutputFolderText.getText());*/
                                        }
                                        else
                                            if("Pestim".equals(CurrentLayout)){
                                                /**bw.write("DES\n");
                                                bw.write(dFPKMfileText.getText());
                                                bw.write("\n");
                                                bw.write(pMinText.getText());
                                                bw.write("\n");
                                                bw.write(pMaxText.getText());
                                                bw.write("\n");
                                                bw.write(Integer.toString(dCovComboBox.getSelectedIndex()));
                                                bw.write("\n");*/
                                                
                                            }
                                            else
                                                if ("DataImport".equals(CurrentLayout)){/*
                                                    bw.write("DataImport\n");
                                                    if (iSudoBRadioButton.isSelected()){
                                                        bw.write("sudo\n");
                                                    }
                                                    else{
                                                        bw.write("docker\n");
                                                    }
                                                    bw.write(iGenomeFolderBText.getText());
                                                    bw.write("\n");
                                                    bw.write(iThreadBText.getText());
                                                    bw.write("\n");
                                                    bw.write(iGenomeURLBText.getText());
                                                    bw.write("\n");
                                                    if (iBTrueRadioButton.isSelected()){
                                                        bw.write("true\n");
                                                    }
                                                    else{
                                                        bw.write("false\n");
                                                    }
                                                    bw.write(i1000GenomeText.getText());
                                                    bw.write("\n");
                                                    bw.write(idbSPNText.getText());
                                               */ }
                                                else
                                                    if ("MACS".equals(CurrentLayout)){
                                                        /*
                                                        bw.write("MACS\n");
                                                        if (MSudoRadioButton.isSelected()){
                                                            bw.write("sudo\n");
                                                        }
                                                        else{
                                                            bw.write("docker\n");
                                                        }
                                                        bw.write(MOutputFolderText.getText());
                                                        bw.write("\n");
                                                        bw.write(MScratchFolderText.getText());
                                                        bw.write("\n");
                                                        bw.write(MGenomeFolderText.getText());
                                                        bw.write("\n");
                                                        bw.write(MTestFolderText.getText());
                                                        bw.write("\n");
                                                        bw.write(MMockFolderText.getText());
                                                        bw.write("\n");
                                                        bw.write(MThreadText.getText());
                                                        bw.write("\n");
                                                        bw.write(MAdapter5Text.getText());
                                                        bw.write("\n");
                                                        bw.write(MAdapter3Text.getText());
                                                        bw.write("\n");
                                                        bw.write(MMinLengthText.getText());
                                                        bw.write("\n");
                                                        bw.write(MReadLengthTextField.getText());
                                                        bw.write("\n");
                                                        bw.write(Integer.toString(MOrgComboBox.getSelectedIndex()));
                                                        bw.write("\n");
                                                        if ( MRnoRadioButton.isSelected()){
                                                            bw.write("no\n");
                                                        }
                                                        else{
                                                            bw.write("yes\n");
                                                        }
                                                        bw.write(MMaxUpstreamText.getText());
                                                        bw.write("\n");
                                                        bw.write(MTTSText.getText());
                                                        bw.write("\n");
                                                        bw.write(MMinmfoldText.getText());
                                                        bw.write("\n");
                                                        bw.write(MMpvalueText.getText());
                                                        bw.write("\n");
                                                        bw.write(MMaxmfoldText.getText());
                                                        bw.write("\n");
                                                        bw.write(MSWindowsText.getText());
                                                        bw.write("\n");
                                                        bw.write(MSgsizeText.getText());
                                                        bw.write("\n");
                                                        bw.write(MSFDRText.getText());
                                                        bw.write("\n");
                                                        bw.write(Integer.toString(MToolComboBox.getSelectedIndex()));
                                                    */
                                                    }else
                                                        if("SampleSize".equals(CurrentLayout)){
                                                            /*
                                                            bw.write("SampleSize\n");
                                                            bw.write(SSCountTableText.getText());
                                                            bw.write("\n");
                                                            bw.write(SSOutputFolderText.getText());
                                                            bw.write("\n");
                                                            bw.write(SSPowerText.getText());
                                                            bw.write("\n");
                                                            bw.write(SSGeneText.getText());
                                                            bw.write("\n");
                                                            bw.write(SSlog2Text.getText());
                                                            bw.write("\n");
                                                            bw.write(SSFDRtext.getText());
                                                            */
                                                        }
                                                        else
                                                            if("ExperimentPower".equals(CurrentLayout)){/*
                                                                bw.write("ExperimentPower\n");
                                                                bw.write(EPCountTableText.getText());
                                                                bw.write("\n");
                                                                bw.write(EPOutputFolderText.getText());
                                                                bw.write("\n");
                                                                bw.write(EPSampleText.getText());
                                                                bw.write("\n");
                                                                bw.write(EPGeneText.getText());
                                                                bw.write("\n");
                                                                bw.write(EPlog2Text.getText());
                                                                bw.write("\n");
                                                                bw.write(EPFDRtext.getText());
                                                           */
                                                            }
                                                            else
                                                                if("Counting".equals(CurrentLayout)){/*
                                                                    bw.write("AddingCovmRNA\n");
                                                                    bw.write(CCovInputFileText.getText());
                                                                    bw.write("\n");
                                                                    bw.write(COutputFolderText.getText());
                                                                    bw.write("\n");
                                                                    for (int i=0;i<CCountHeaderTable.getRowCount();i++){
                                                                        bw.write(CCountHeaderTable.getModel().getValueAt(i,0).toString());
                                                                        bw.write("\n");
                                                                        bw.write(CCountHeaderTable.getModel().getValueAt(i,1).toString());
                                                                        bw.write("\n");
                                                                        bw.write(CCountHeaderTable.getModel().getValueAt(i,2).toString());
                                                                        bw.write("\n");
                                                                    }
                                                                */
                                                                }
                                                                else
                                                                    if ("indexingSalmon".equals(CurrentLayout)){/*
                                                                        bw.write("indexingSalmon\n");
                                                                        if (iSudoRadioSButton.isSelected()){

                                                                            bw.write("sudo\n");
                                                                        }
                                                                        else{
                                                                            bw.write("docker\n");
                                                                        }
                                                                        bw.write(iGenomeFolderSText.getText());
                                                                        bw.write("\n");
                                                                        bw.write(iThreadSText.getText());
                                                                        bw.write("\n");
                                                                        bw.write(iGenomeURLSText.getText());
                                                                        bw.write("\n");
                                                                        bw.write(iGTFURLSText.getText());
                                                                        bw.write("\n");
                                                                        bw.write(iKmerSText.getText());*/
                                                                    }
                                                                    else
                                                                        if ("countingSalmon".equals(CurrentLayout)){/*
                                                                            bw.write("countingSalmon\n");
                                                                            if (cSudoRadioButton.isSelected()){
                                                                                bw.write("sudo\n");
                                                                            }
                                                                            else{
                                                                                bw.write("docker\n");
                                                                            }
                                                                            bw.write(cFastQFolderText.getText());
                                                                            bw.write("\n");
                                                                            bw.write(cOutputFolderText.getText());
                                                                            bw.write("\n");
                                                                            bw.write(cAdapter5Text.getText());
                                                                            bw.write("\n");
                                                                            bw.write(cAdapter3Text.getText());
                                                                            bw.write("\n");
                                                                            if (cPeRadioButton.isSelected()){
                                                                                bw.write("pe\n");
                                                                            }
                                                                            else{
                                                                                bw.write("se\n");
                                                                            }
                                                                            bw.write(cThreadText.getText());
                                                                            bw.write("\n");
                                                                            bw.write(cMinLengthText.getText());
                                                                            bw.write("\n");
                                                                            bw.write(cGenomeFolderText.getText());
                                                                            bw.write("\n");
                                                                            if (cSNoneRadioButton.isSelected())
                                                                                bw.write("none\n");
                                                                            else
                                                                                if (cSForwardRadioButton.isSelected())
                                                                                    bw.write("forward\n");
                                                                                else
                                                                                    bw.write("reverse\n");
                                                                        */
                                                                        }
                                                                        else
                                                                            if("SplinePanel".equals(CurrentLayout)){/*
                                                                                bw.write("filtercounts\n");
                                                                                bw.write(fFPKMfileText.getText());
                                                                                bw.write("\n");
                                                                                if (fgeneRadioButton.isSelected()){
                                                                                    bw.write("gene\n");
                                                                                }
                                                                                else
                                                                                    if (fisoformRadioButton.isSelected()){
                                                                                        bw.write("isoform\n");
                                                                                    }
                                                                                    else{
                                                                                        bw.write("mirna\n");
                                                                                    }
                                                                                bw.write(fOutputFolderText.getText());*/
                                                                            }
                            }
                    }
                    JOptionPane.showMessageDialog(this,"File "+f.getName()+" saved","Save",JOptionPane.INFORMATION_MESSAGE);
                    getPreferences().put("saved-file",saveFile.getCurrentDirectory().getAbsolutePath());
                }

                catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error saving file","Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        }

    }


    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        saveAsMenuItemActionPerformed(evt);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
         openMenuItemActionPerformed(evt);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void ProcListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_ProcListValueChanged


        if (!evt.getValueIsAdjusting()){

        //System.out.println("****Open ProcListValueChanged : \n"+GL.getAvoidProcListValueChanged()+" "+GL.getListProcStatuSelection()+" "+evt.getLastIndex());
        if ((GL.getAvoidProcListValueChanged()==-1)){
           // GL.setAvoidProcListValueChanged(0);
            return;
        }
        //if (evt.getLastIndex()<0 ||evt.getLastIndex()>=listModel.getSize()){
        //    return;
        //}

        if ((evt!=null)){
        OutputFrame.pack();
         Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int OutputframeWidth= Integer.valueOf(getPreferences().get("ConnectorGUI_WindowOutputWidth", "0"));
        int OutputframeHeight= Integer.valueOf(getPreferences().get("ConnectorGUI_WindowOutputHeight", "0"));

        if ((OutputframeWidth==0)||(OutputframeHeight==0)){
              OutputframeWidth=screenSize.width*40/100;
              OutputframeHeight=screenSize.height*50/100;
        }

        OutputFrame.setSize(OutputframeWidth,OutputframeHeight);
//automatically update file



        OutputFrame.setLocationRelativeTo(null);
        OutputFrame.setVisible(true);
        OutputFrame.setAlwaysOnTop(true);

        //System.out.println("QUII->-Inizio\n"+listModel.getSize()+" "+evt.getLastIndex()+"\n"+evt.getSource());
        ListEntry tmpListEntry;
        if ((evt.getLastIndex()!=GL.getListProcStatuSelection()&& (evt.getLastIndex()>=0 && evt.getLastIndex()<listModel.getSize())) ){
                tmpListEntry=  listModel.get(evt.getLastIndex());
                GL.setListProcStatuSelection(evt.getLastIndex());

                   //System.out.println("\t------- Selected Last\n");
        }
        else    {
                if (evt.getFirstIndex()>=0 && evt.getFirstIndex()<listModel.getSize()){
                tmpListEntry=  listModel.get(evt.getFirstIndex());
                 //System.out.println("\t------- Selected First\n");
                GL.setListProcStatuSelection(evt.getFirstIndex());
                }
                else
                    return;
        }

        if (tmpListEntry.status.equals("Running") || tmpListEntry.status.equals("Finished")|| tmpListEntry.status.equals("Error")){
            //System.out.println("\tQUII->-RUNNING\n");
            String text="";
            OutputText.setEnabled(true);
            try{
                File file = new File( tmpListEntry.path+"/Routput.Rout");
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String strLine;
                    //Read File Line By Line
                    while ((strLine = reader.readLine()) != null)   {
                        text+="\n"+strLine;
                    }
                }
            }
            catch (IOException e){//Catch exception if any
                //System.err.println("Error: " + e.getMessage());
                JOptionPane.showMessageDialog(this, "Error reading R output file","Error",JOptionPane.ERROR_MESSAGE);
            }
            OutputText.setText(text);
           if (tmpListEntry.status.equals("Running")){
                outputTime=new Timer();
                outputTime.scheduleAtFixedRate(new MyFileUpdate(), 5000, 5000);
            }
           else{
            DlogButton.setEnabled(true);
           }
        }
        else
        {
          OutputText.setEnabled(false);
          //System.out.println("\tQUIFINE WAITING\n");
        }
        //System.out.println("QUIFINE\n");
        //GL.setListProcStatuSelection(evt.getLastIndex());
        //System.out.println("End ProcListValueChanged\n");
        }
        }

    }//GEN-LAST:event_ProcListValueChanged

    private void CloseOutputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CloseOutputActionPerformed
    OutputFrame.setVisible(false);
    OutputText.setText("");
    OutputText.setEnabled(true);
    DlogButton.setEnabled(false);
    GL.setAvoidProcListValueChanged(-1);
    ProcList.clearSelection();
    GL.setAvoidProcListValueChanged(0);
    getPreferences().put("ConnectorGUI_WindowOutputWidth", Integer.toString(OutputFrame.getWidth()));
    getPreferences().put("ConnectorGUI_WindowOutputHeight", Integer.toString(OutputFrame.getHeight()));
    //System.out.println("@@@@@@@@@@@@Close: \n");
    outputTime.cancel();
    }//GEN-LAST:event_CloseOutputActionPerformed

    private void ProcListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ProcListMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_ProcListMouseClicked

    private void ReloadOutputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ReloadOutputActionPerformed

         ListEntry tmpListEntry =  listModel.get(GL.getListProcStatuSelection());
        //
        if (tmpListEntry.status.equals("Running") || tmpListEntry.status.equals("Finished")|| tmpListEntry.status.equals("Error")){
             OutputText.setEnabled(true);
            String text="";
            try{
                File file = new File( tmpListEntry.path+"/Routput.Rout");
                 try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                     String strLine;
                     //Read File Line By Line
                     while ((strLine = reader.readLine()) != null)   {
                         text+="\n"+strLine;
                     }}
            }
            catch (IOException e){//Catch exception if any
                System.err.println("Error: " + e.getMessage());
                JOptionPane.showMessageDialog(this, "Error reading file","Error",JOptionPane.ERROR_MESSAGE);
            }
            OutputText.setText(text);
        }
        ReloadOutput.setSelected(false);
    }//GEN-LAST:event_ReloadOutputActionPerformed


    private void RemoveOutputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RemoveOutputActionPerformed

        int pos=GL.getListProcStatuSelection();
        //System.out.println("Pos:"+pos+"\n");
        int tmpPos=-1;
        for(int i=0;i<listProcRunning.size();i++){

            if (listProcRunning.get(i).pos>pos){
                listProcRunning.get(i).pos--;
            }
            else
                if (listProcRunning.get(i).pos==pos){
                    tmpPos=i;
                }
        }
        if (tmpPos!=-1){
            //try{
            //if Docker is runnning
            String[] cmd = {"/bin/bash","-c"," "};
            try{
                    File file = new File(listProcRunning.get(tmpPos).path+"/dockerID");
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String dockerID = reader.readLine();
                    cmd[2]="docker kill " +dockerID +" ; rm " + listProcRunning.get(tmpPos).path+"/dockerID";
                    Runtime.getRuntime().exec(cmd);
                }
                }
            catch (IOException e){//Catch exception if any
                System.out.println("No docker running \n");
                }
            long pID=getPidOfProcess(listProcRunning.get(tmpPos).pr);
            //System.out.println("lanciato PID:"+pID +"\n");
            if (pID!=-1){
                try{
                   cmd[2]="kill $(./list_descendants.sh " +Long.toString(pID)+")";
                   Runtime.getRuntime().exec(cmd);
                }
                catch(IOException e){
                    System.err.println("Error in Killing the process children:" + e);
                }

            }
            try{
                    File file = new File(listProcRunning.get(tmpPos).path+"/tempFolderID");
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String tempFolderID = reader.readLine();
                    if (!(tempFolderID.equals(""))){
                        cmd[2]="rm -R " + tempFolderID +" ; rm " + listProcRunning.get(tmpPos).path+"/tempFolderID";
                        Runtime.getRuntime().exec(cmd);
                    }
                }
                }
            catch (IOException e){//Catch exception if any
                System.out.println("No temporary folder\n");
                }
            //listProcRunning.get(tmpPos).pr.destroy();
           // listProcRunning.get(tmpPos).pr.waitFor();
            //}
            //catch (InterruptedException e) {
           // System.err.println("Error in Killing the process:" + e);
            //}
            /*Field field;
            try{
            final Runtime runtime = Runtime.getRuntime();
            field=listProcRunning.get(tmpPos).pr.getClass().getField("pid");
            field.setAccessible(true);
            final Object processID = field.get(listProcRunning.get(tmpPos).pr);
            final int pid = (Integer) processID;
            runtime.exec("sudo kill -9 " + pid);
            }
            catch (IOException e) {
                   System.err.println("Error in Killing the process:" + e);
                } catch (SecurityException e) {
                    System.err.println("Error in Killing the process:" + e);
                } catch (NoSuchFieldException e) {
                    System.err.println("Error in Killing the process:" + e);
                } catch (IllegalArgumentException e) {
                     System.err.println("Error in Killing the process:" + e);
                } catch (IllegalAccessException e) {
                     System.err.println("Error in Killing the process:" + e);
                }
            */
            listProcRunning.remove(tmpPos);
            tmpPos=-1;
        }

        for(int i=0;i<listProcWaiting.size();i++){
            if (listProcWaiting.get(i).pos>pos){
                listProcWaiting.get(i).pos--;
            }
            else
                if (listProcWaiting.get(i).pos==pos){
                    tmpPos=i;
                }
        }
         if (tmpPos!=-1){
            listProcWaiting.remove(tmpPos);
            tmpPos=-1;
        }
        //System.out.println("Pos1:"+pos+"\n");
        GL.setListProcStatuSelection(-1);
        listModel.remove(pos);
        //System.out.println("Pos2:"+pos+"\n");
        OutputFrame.setVisible(false);
        OutputText.setText("");
        GL.setAvoidProcListValueChanged(-1);
        ProcList.clearSelection();
        GL.setAvoidProcListValueChanged(0);
    }//GEN-LAST:event_RemoveOutputActionPerformed

    private void ConfCancellActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConfCancellActionPerformed
        ConfigurationFrame.setVisible(false);
    }//GEN-LAST:event_ConfCancellActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed

        GS.setMaxSizelistProcRunning(Integer.valueOf(ParallelTextField.getText()));
        GS.setDefaultAdapter5(Adapter5TextField.getText());
        GS.setDefaultAdapter3(Adapter3TextField.getText());
        GS.setDefaultThread(Integer.valueOf(ThreadTextField.getText()));
        GS.save();
        ConfigurationFrame.setVisible(false);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        ParallelTextField.setText(Integer.toString(GS.getMaxSizelistProcRunning()));
        ThreadTextField.setText(Integer.toString(GS.getDefaultThread()));
        Adapter5TextField.setText(GS.getDefaultAdapter5());
        Adapter3TextField.setText(GS.getDefaultAdapter3());
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
       removeDockerContainer(evt);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
       configurationMenuItemActionPerformed(evt);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
       removeDockerContainer(evt);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        formWindowClosing(null);
        setVisible(false);
        dispose();
        System.exit(0);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        saveAsMenuItemActionPerformed(evt);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
     openMenuItemActionPerformed(evt);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
       getPreferences().put("ConnectorGUI_HorizontalSplitPanel", Integer.toString(HorizontalSplitPanel.getDividerLocation()));
       getPreferences().put("ConnectorGUI_VerticalSplitPanel", Integer.toString(VerticalSplitPanel.getDividerLocation()));
       getPreferences().put("ConnectorGUI_WindowWidth", Integer.toString(getSize().width));
       getPreferences().put("ConnectorGUI_WindowHeight",Integer.toString(getSize().height));
    }//GEN-LAST:event_formWindowClosing

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed
     DownloadMenuItemActionPerformed(evt);
    }//GEN-LAST:event_jButton26ActionPerformed

    private void jButton31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton31ActionPerformed
        DownloadFrame.setVisible(false);
        Downloadtext.setText("");
        getPreferences().put("ConnectorGUI_WindowDownloadWidth", Integer.toString(DownloadFrame.getWidth()));
        getPreferences().put("ConnectorGUI_WindowDownloadHeight", Integer.toString(DownloadFrame.getHeight()));
    }//GEN-LAST:event_jButton31ActionPerformed

    private void jButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton33ActionPerformed
        Downloadtext.setText("");
    }//GEN-LAST:event_jButton33ActionPerformed

    private void jButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton34ActionPerformed
        browseTextFieldContent(this, Downloadtext, JFileChooser.FILES_ONLY);

        DownloadFrame.toFront();
        DownloadFrame.requestFocus();
    }//GEN-LAST:event_jButton34ActionPerformed

    private void jButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32ActionPerformed
        String containerListFile = Downloadtext.getText();
        String commandArgs = String.format("containers.list=%s",
                containerListFile.isEmpty() ? "NULL" : "'" + containerListFile + "'")
                .replace("'", "\\\"");
        //execute code
        execCommand(this, "Download Docker images", "execDownloadImage.sh", commandArgs, System.getProperty("user.dir"));

        DownloadFrame.setVisible(false);
        Downloadtext.setText("");
        getPreferences().put("ConnectorGUI_WindowDownloadWidth", Integer.toString(DownloadFrame.getWidth()));
        getPreferences().put("ConnectorGUI_WindowDownloadHeight", Integer.toString(DownloadFrame.getHeight()));

        dockerManager.addImages(containerListFile);
        dockerManager.updateGUI();
    }//GEN-LAST:event_jButton32ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        openAboutConnectorGUI(evt);
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jButton35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton35ActionPerformed
        openAboutConnectorGUI(evt);
    }//GEN-LAST:event_jButton35ActionPerformed

    private void jButton36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton36ActionPerformed
       AboutConnectorGUIFrame.setVisible(false);
    }//GEN-LAST:event_jButton36ActionPerformed

    private void OutputFrameWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_OutputFrameWindowClosing
      outputTime.cancel();
    }//GEN-LAST:event_OutputFrameWindowClosing

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        DownloadMenuItemActionPerformed(evt);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void DlogButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DlogButtonActionPerformed
    JFileChooser openFile = new JFileChooser();
    ListEntry tmpListEntry =  listModel.get(GL.getListProcStatuSelection());
    openFile.setCurrentDirectory(new File(tmpListEntry.path));
    FileNameExtensionFilter filter = new FileNameExtensionFilter("LOG FILES", "log", "text");
    openFile.setFileFilter(filter);
    if (openFile.showOpenDialog(this)==JFileChooser.APPROVE_OPTION){

        try{
            File file = openFile.getSelectedFile();
            String text;
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String strLine;
                text = "";
                //Read File Line By Line
                while ((strLine = reader.readLine()) != null)   {
                    text+="\n"+strLine;
                }
            }
            OutputText.setText(text);
                }
                catch (IOException e){//Catch exception if any
                    System.err.println("Error: " + e.getMessage());
                    //JOptionPane.showMessageDialog(this, "Error reading R output file","Error",JOptionPane.ERROR_MESSAGE);
                }
        }

    }//GEN-LAST:event_DlogButtonActionPerformed

    private void ConfigureTabsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConfigureTabsButtonActionPerformed
        ConfigureTabsFrame.pack();
        ConfigureTabsFrame.setLocationRelativeTo(null);
        ConfigureTabsFrame.setVisible(true);

        //enable and disable checkboxes based on saved preferences
        for (Component c: enableTabsPanel.getComponents()) {
            JCheckBox cb = (JCheckBox) c;
            String varname = String.format("ConnectorGUI_EnableTab%s", cb.getName());
            String varvalue = getPreferences().get(varname, "true");

            cb.setSelected(varvalue.equals("true"));
        }
    }//GEN-LAST:event_ConfigureTabsButtonActionPerformed

    private void miRNATabCheckerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miRNATabCheckerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_miRNATabCheckerActionPerformed

    private void confermConfigureTabButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confermConfigureTabButtonActionPerformed
        ArrayList<String> disabledTabs = new ArrayList<>();

        //for each tab, set a variable to show/hide it
        for (Component c: enableTabsPanel.getComponents()) {
            JCheckBox cb = (JCheckBox) c;
            String varname = String.format("ConnectorGUI_EnableTab%s", cb.getName());

            getPreferences().put(varname, String.valueOf(cb.isSelected()));

            if (!cb.isSelected())
                disabledTabs.add(c.getName());
        }

        tabsController.refreshTabs();

       // visualizeTabs();
        ConfigureTabsFrame.setVisible(false);
    }//GEN-LAST:event_confermConfigureTabButtonActionPerformed

    private void closeConfigureTabButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeConfigureTabButtonActionPerformed
        ConfigureTabsFrame.setVisible(false);
    }//GEN-LAST:event_closeConfigureTabButtonActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        ConfigureTabsButtonActionPerformed(evt);
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        configurationMenuItemActionPerformed(evt);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void manageDockerImagesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manageDockerImagesButtonActionPerformed
        dockerImagesManager.pack();
        dockerImagesManager.setLocationRelativeTo(null);
        dockerImagesManager.setVisible(true);

        dockerManager.updateGUI();
    }//GEN-LAST:event_manageDockerImagesButtonActionPerformed




    private void addImagesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addImagesButtonActionPerformed
        DownloadMenuItemActionPerformed(evt);
    }//GEN-LAST:event_addImagesButtonActionPerformed

    private void removeImagesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeImagesButtonActionPerformed
        dockerManager.removeImages();
    }//GEN-LAST:event_removeImagesButtonActionPerformed

    private void HestimButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HestimButtonActionPerformed
        setCard("pca_h");
    }//GEN-LAST:event_HestimButtonActionPerformed

    private void jLabelmiRNA2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelmiRNA2MouseClicked
        toggleMenu(miRNApanelSub2M, jLabelmiRNA2, getClass().getResource("/pkgConnector/images/tool.png"));
    }//GEN-LAST:event_jLabelmiRNA2MouseClicked

    private void PestimButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PestimButtonActionPerformed
        setCard("Pestim");
    }//GEN-LAST:event_PestimButtonActionPerformed

    private void jLabelmiRNA1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelmiRNA1MouseClicked
        toggleMenu(miRNApanelSub1M, jLabelmiRNA1, getClass().getResource("/pkgConnector/images/miRNAtabB.png"));
    }//GEN-LAST:event_jLabelmiRNA1MouseClicked

    private void jLabelRNAseq3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelRNAseq3MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabelRNAseq3MouseClicked

    private void CutDataButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CutDataButtonActionPerformed
        setCard("DataCut");
    }//GEN-LAST:event_CutDataButtonActionPerformed

    private void DataVisualButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DataVisualButtonActionPerformed
        setCard("DataVisual");
    }//GEN-LAST:event_DataVisualButtonActionPerformed

    private void ImportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ImportButtonActionPerformed
        iThreadText.setText(Integer.toString(GS.getDefaultThread()));
        setCard("DataImport");
    }//GEN-LAST:event_ImportButtonActionPerformed

    private void LabelDataCreationMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LabelDataCreationMouseClicked
        toggleMenu(RNAseqPanelSub2M, LabelDataCreation, getClass().getResource("/pkgConnector/images/RNAtabB.png"));
    }//GEN-LAST:event_LabelDataCreationMouseClicked

    private void BestClusterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BestClusterButtonActionPerformed
        setCard("BestCl");
    }//GEN-LAST:event_BestClusterButtonActionPerformed

    private void DBindexButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DBindexButtonActionPerformed
        setCard("DBindex");
    }//GEN-LAST:event_DBindexButtonActionPerformed

    private void ConsMatrixButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConsMatrixButtonActionPerformed
        setCard("ConsMatrix");
    }//GEN-LAST:event_ConsMatrixButtonActionPerformed

    private void KestimButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KestimButtonActionPerformed
        setCard("ClusterPanel");
    }//GEN-LAST:event_KestimButtonActionPerformed

    private void ClustCurveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClustCurveButtonActionPerformed
        setCard("ClustCurvesPlot");
    }//GEN-LAST:event_ClustCurveButtonActionPerformed

    private void CountButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CountButtonActionPerformed
        setCard("Counting");
    }//GEN-LAST:event_CountButtonActionPerformed

    private void SplineButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SplineButtonActionPerformed
        setCard("spline");
    }//GEN-LAST:event_SplineButtonActionPerformed

    private void DiscrPlotButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DiscrPlotButtonActionPerformed
        setCard("MACS");
    }//GEN-LAST:event_DiscrPlotButtonActionPerformed

    /**
     * Toggle menu panel
     * @param panel Panel to toggle
     * @param label Label associated to the click event
     * @param img Icon to put when the panel is visible
     */
    private void toggleMenu(javax.swing.JPanel panel, javax.swing.JLabel label, java.net.URL img) {
        java.net.URL imgURLUP = getClass().getResource("/pkgConnector/images/download.png");
        boolean is_visible = panel.isVisible();

        ImageIcon image = new ImageIcon(is_visible ? imgURLUP : img);
        label.setIcon(image);
        panel.setVisible(!is_visible);
    }


    private void  openAboutConnectorGUI(java.awt.event.ActionEvent evt) {
        AboutConnectorGUIFrame.pack();
        AboutConnectorGUIFrame.setLocationRelativeTo(null);
        AboutConnectorGUIFrame.setVisible(true);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        //Anti-aliasing code
        System.setProperty("awt.useSystemAAFontSettings","on");
        System.setProperty("swing.aatext", "true");

        try {
            //set default look and feel
           javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            //set GTK look and feel, if it is present
            for (javax.swing.UIManager.LookAndFeelInfo info: javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("com.sun.java.swing.plaf.gtk.GTKLookAndFeel".equals(info.getClassName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());

                    break;
                }
            }



  //javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        try{
           Thread.sleep(800L);
        }
        catch ( InterruptedException e ) { }
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFrame AboutConnectorGUIFrame;
    private javax.swing.JTextField Adapter3TextField;
    private javax.swing.JTextField Adapter5TextField;
    private javax.swing.JComboBox<String> BatchComboBox;
    private javax.swing.JScrollPane BestCLchoice;
    private javax.swing.JButton BestClusterButton;
    public static javax.swing.JPanel BottomPanel;
    private javax.swing.JToggleButton CloseOutput;
    private javax.swing.JScrollPane ClustCurve;
    private javax.swing.JButton ClustCurveButton;
    private javax.swing.JPanel ClustEstimPanel;
    private javax.swing.JScrollPane ClustEstimationScrollPanel;
    private javax.swing.JScrollPane ClustPlotsScrollPanel;
    private javax.swing.JScrollPane ClusterPanel;
    private javax.swing.JButton ConfCancell;
    public static javax.swing.JFrame ConfigurationFrame;
    private javax.swing.JButton ConfigureTabsButton;
    private javax.swing.JFrame ConfigureTabsFrame;
    private javax.swing.JScrollPane ConsMatrix;
    private javax.swing.JButton ConsMatrixButton;
    private javax.swing.JButton CountButton;
    private javax.swing.JScrollPane CountingClustScrollPanel;
    private javax.swing.JPanel CountingPanle;
    private javax.swing.JScrollPane CountingSample;
    private javax.swing.JComboBox<String> CovComboBox;
    private javax.swing.JButton CutDataButton;
    private javax.swing.JScrollPane DBindex;
    private javax.swing.JButton DBindexButton;
    private javax.swing.JScrollPane DataCuttingPanel;
    private javax.swing.JScrollPane DataImportPanel;
    private javax.swing.JPanel DataImportPanelSub1;
    private javax.swing.JButton DataVisualButton;
    private javax.swing.JScrollPane DataVisualPanel;
    private javax.swing.JPanel DataVisualSub2;
    private javax.swing.JButton DiscrPlotButton;
    private javax.swing.JScrollPane DiscrPlotPanel;
    private javax.swing.JButton DlogButton;
    private javax.swing.JFrame DownloadFrame;
    private javax.swing.JTextField Downloadtext;
    private javax.swing.JScrollPane Empty;
    private javax.swing.JPanel EmptyPanel;
    private javax.swing.JPanel FCMPanel;
    private javax.swing.JScrollPane Hestim;
    private javax.swing.JButton HestimButton;
    private javax.swing.JPanel HestimpanelSub2;
    private javax.swing.JSplitPane HorizontalSplitPanel;
    private javax.swing.JButton ImportButton;
    private javax.swing.JButton KestimButton;
    private javax.swing.JLabel LabelDataCreation;
    private javax.swing.JPanel LeftPanel;
    private javax.swing.ButtonGroup MExecution;
    private javax.swing.ButtonGroup MRemoveDuplicates;
    private javax.swing.ButtonGroup MSeq;
    public static javax.swing.JPanel MainPanel;
    public static javax.swing.JFrame OutputFrame;
    public static javax.swing.JTextArea OutputText;
    private javax.swing.JTextField ParallelTextField;
    private javax.swing.JPanel ParamEstimPanel;
    private javax.swing.JScrollPane ParamEstimScrollPanel;
    private javax.swing.JScrollPane Pestim;
    private javax.swing.JButton PestimButton;
    private javax.swing.JPanel PestimSub1;
    private javax.swing.JPanel PreProcPanel;
    private javax.swing.JScrollPane PreProcessScrollPane;
    public static javax.swing.JList<ListEntry> ProcList;
    private javax.swing.JPanel ProcStatusPanel;
    private javax.swing.JPanel RNAseqPanelSub2M;
    public static javax.swing.JToggleButton ReloadOutput;
    private javax.swing.JButton RemoveOutput;
    private javax.swing.JButton SplineButton;
    private javax.swing.JScrollPane SplinePanel;
    private javax.swing.JPanel SubPanel1;
    private javax.swing.JPanel SubPanel1ClusEstim;
    private javax.swing.JPanel Subpanel3;
    private javax.swing.JTextField ThreadTextField;
    private javax.swing.JSplitPane VerticalSplitPanel;
    private javax.swing.JButton addImagesButton;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox chipseqTabChecker;
    private javax.swing.JCheckBox circRNATabChecker;
    private javax.swing.JButton closeConfigureTabButton;
    private javax.swing.JPanel commandsPanel;
    private javax.swing.JButton confermConfigureTabButton;
    private javax.swing.JFrame dockerImagesManager;
    private javax.swing.JTable dockerImagesTable;
    private javax.swing.JPanel enableTabsPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JLabel jLabelRNAseq3;
    private javax.swing.JLabel jLabelmiRNA1;
    private javax.swing.JLabel jLabelmiRNA2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton manageDockerImagesButton;
    private javax.swing.JCheckBox miRNA2TabChecker;
    private javax.swing.JCheckBox miRNATabChecker;
    private javax.swing.JPanel miRNApanelSub1M;
    private javax.swing.JPanel miRNApanelSub2M;
    private javax.swing.JButton pullImagesButton;
    private javax.swing.JButton removeImagesButton;
    private javax.swing.JCheckBox rnaSeqTabChecker;
    private javax.swing.JCheckBox singleCellTabChecker;
    private javax.swing.JCheckBox toolsTabChecker;
    // End of variables declaration//GEN-END:variables





static public class ElProcRunning {
    public String type;
    public String path;
    public Process pr;
    public int  pos;
    //constructor
    public ElProcRunning(String type,String path, Process pr,int i) {
        this.type = type;
        this.path = path;
        this.pr = pr;
        pos=i;
    }
   public String toString() {
      return new String(type+" ( data: "+path+" )");
   }
}

static public class ElProcWaiting {
    public String type;
    public String path;
    public String[] cmd;
    public int  pos;
    //constructor
    public ElProcWaiting(String type, String path, String[] cmd, int i) {
        this.type = type;
        this.path =path;
        this.cmd = cmd;
        pos=i;
    }
    public String toString() {
      return new String(type+" ( data: "+path+")");
   }
}


static public class GlobalStatus{
 private int ListProcStatusSelection;
 private int AvoidProcListValueChanged; //-1 avoid  ProcListValueChanged
 public GlobalStatus(){
     ListProcStatusSelection=-1;
     AvoidProcListValueChanged=0;
 }

 public int getListProcStatuSelection(){
     return ListProcStatusSelection;
 }
  public void setListProcStatuSelection(int ListProcStatusSelection){
     //System.out.print("Updating..."+ListProcStatusSelection+"\n");
     this.ListProcStatusSelection=ListProcStatusSelection;
 }

 public int getAvoidProcListValueChanged(){
     return AvoidProcListValueChanged;
 }
  public void setAvoidProcListValueChanged(int AvoidProcListValueChanged){
     this.AvoidProcListValueChanged=AvoidProcListValueChanged;
 }
}

static public class ListEntry
{
   private final String value;
   private final ImageIcon icon;
   private  String status;
   private  String path;
   private static final long serialVersionUID = 57782123311L;
   public ListEntry(String value, String status,String path, ImageIcon icon) {
      this.value = value;
      this.icon = icon;
      this.status=status;
      this.path=path;
   }

   public String getValue() {
      return value;
   }

   public ImageIcon getIcon() {
      return icon;
   }
   public String getStatus() {
      return status;
   }

   public String getPath() {
      return path;
   }

   public String toString() {
      return value;
   }
}

static public class GlobalSetting{
    private int DefaultThread= 8;
    private int MaxSizelistProcRunning=1;
    private String DefaultAdapter5="";
    private String DefaultAdapter3="";

    public GlobalSetting(){
    boolean findFile=false;
    try{
                File file = new File(".ConnectorGUI");
                BufferedReader reader = new BufferedReader(new FileReader(file));
                int line=0;
                //Read File Line By Line
                 for (String x = reader.readLine(); x != null; x = reader.readLine()){
                                switch (line){
                                    case 0:
                                        DefaultThread=Integer.valueOf(x);
                                        if (DefaultThread<=0){
                                            throw(new NumberFormatException());
                                        }
                                    break;
                                    case 1:
                                       MaxSizelistProcRunning=Integer.valueOf(x);
                                        if (MaxSizelistProcRunning<=0){
                                            throw(new NumberFormatException());
                                        }
                                    break;
                                    case 2:
                                        DefaultAdapter5=x;
                                    break;
                                    case 3:
                                        DefaultAdapter3=x;
                                    break;
                                    default:
                                        throw(new NumberFormatException());
                                }
                                line++;
                            }
                reader.close();
        }
    catch (Exception e){//Catch exception if any
                //System.err.println("Error: " + e.getMessage());
            return;
            }
    }

    public void save(){

        try{
            FileWriter fw = new FileWriter(".RData");
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(DefaultThread+"\n");
            bw.write(MaxSizelistProcRunning+"\n");
            bw.write(DefaultAdapter5+"\n");
            bw.write(DefaultAdapter3+"\n");
            bw.close();
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(ConfigurationFrame, "Error saving file","Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    int getDefaultThread(){
        return DefaultThread;
    }

    String getDefaultAdapter5(){
        return DefaultAdapter5;
    }

    String getDefaultAdapter3(){
    return DefaultAdapter3;
    }

    int getMaxSizelistProcRunning(){
        return MaxSizelistProcRunning;
    }


    void  setDefaultThread(int DefaultThread){
        this.DefaultThread=DefaultThread;
    }

    void setDefaultAdapter5(String DefaultAdapter5){
        this.DefaultAdapter5=DefaultAdapter5;
    }

    void setDefaultAdapter3(String DefaultAdapter3){
        this.DefaultAdapter3=DefaultAdapter3;
    }

    void setMaxSizelistProcRunning(int MaxSizelistProcRunning){
        this.MaxSizelistProcRunning=MaxSizelistProcRunning;
    }
}


static public class ListEntryCellRenderer
extends JLabel implements ListCellRenderer<Object>
{
   private JLabel label;
   private static final long serialVersionUID = 5778212331L;
   public Component getListCellRendererComponent(JList<?> list, Object value,
                                                 int index, boolean isSelected,
                                                 boolean cellHasFocus) {
      ListEntry entry = (ListEntry) value;

      setText(value.toString());
      setIcon(entry.getIcon());

      if (isSelected) {
         setBackground(list.getSelectionBackground());
         setForeground(list.getSelectionForeground());
      }
      else {
         setBackground(list.getBackground());
         setForeground(list.getForeground());
      }

      setEnabled(list.isEnabled());
      setFont(list.getFont());
      setOpaque(true);

      return this;
   }
}
static    String CurrentLayout="Empty";


 static public  Timer t,outputTime=new Timer();

 static public class MyFileUpdate extends TimerTask{
       public void run() {
        if (GL.getListProcStatuSelection()>=0){
            ListEntry tmpListEntry =  listModel.get(GL.getListProcStatuSelection());
            //
            if (tmpListEntry.status.equals("Running") || tmpListEntry.status.equals("Finished")){
             OutputText.setEnabled(true);
            String text="";
            try{
                File file = new File( tmpListEntry.path+"/Routput.Rout");
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String strLine;
                //Read File Line By Line
                while ((strLine = reader.readLine()) != null)   {
                    text+="\n"+strLine;
                }
                reader.close();
            }
            catch (Exception e){//Catch exception if any
                //To avoid to recall infinitelly this error
                outputTime.cancel();
                System.err.println("Error: " + e.getMessage());
                JOptionPane.showMessageDialog(OutputFrame, "Error reading file","Error",JOptionPane.ERROR_MESSAGE);
                }
            OutputText.setText(text);
            }
        ReloadOutput.setSelected(false);
       }
    }
  }
 static public class MyTask extends TimerTask {

        public void run() {

            //System.out.format("Checking running !%n");
            for (int i=0;i<listProcRunning.size();i++){
                if (listProcRunning.get(i).pr.isAlive()){
                    //System.out.format("TRUE\n");
                }
                else
                {
                    //System.out.format("False\n");
                    int index=listProcRunning.get(i).pos;
                    java.net.URL imgURL = getClass().getResource("/pkgConnector/images/end.png");
                    ImageIcon image2 = new ImageIcon(imgURL);
                    ImageIcon image3 = new ImageIcon(getClass().getResource("/pkgConnector/images/close.png"));
                    //listModel.remove(index+1);
                    //GL.setAvoidProcListValueChanged(-1);
                    boolean error=false;
                    try{
                        File f = new File(listProcRunning.get(i).path+"/ExitStatusFile");
                         System.out.print("file:"+ f.getAbsoluteFile().toString() );
                        FileReader fw = new FileReader(f.getAbsoluteFile());
                        BufferedReader br = new BufferedReader(fw);
                        String ExitStatus=br.readLine();
                        System.out.print("br: "+ExitStatus+" "+listProcRunning.get(i).path);
                        if (!(ExitStatus.equals("0"))){
                            error=true;
                        }
                    }
                    catch(IOException | NumberFormatException e) {
                       System.out.print(listProcRunning.get(i).path);
                       error=true;
                    }

                    if (error)
                        listModel.set(index,new ListEntry(" [Error]   " + listProcRunning.get(i).toString(), "Error",listProcRunning.get(i).path,image3));
                    else
                        listModel.set(index,new ListEntry(" [Finished]   " + listProcRunning.get(i).toString(), "Finished",listProcRunning.get(i).path,image2));
                    listProcRunning.remove(i);
                }
            }
            //System.out.format("End Check!\n");
            //System.out.format("Checking waiting !%n");
            while ((listProcRunning.size()<GS.getMaxSizelistProcRunning())&&(listProcWaiting.size()>0)){
                try{

                    Runtime rt = Runtime.getRuntime();
                    Process pr = rt.exec(listProcWaiting.get(0).cmd);
                    ElProcRunning tmp= new ElProcRunning(listProcWaiting.get(0).type,listProcWaiting.get(0).path,pr,listProcWaiting.get(0).pos);
                    listProcRunning.add(tmp);
                    java.net.URL imgURL = getClass().getResource("/pkgConnector/images/running.png");
                    ImageIcon image2 = new ImageIcon(imgURL);
                    //listModel.remove(listProcWaiting.get(0).pos);
                    //GL.setAvoidProcListValueChanged(-1);
                    listModel.set(listProcWaiting.get(0).pos,new ListEntry(" [Running]   " +  listProcWaiting.get(0).toString(),"Running", listProcWaiting.get(0).path,image2));
                    listProcWaiting.remove(0);
                    //System.out.format("Size:"+listProcRunning.size()+"\n");
                }
                catch(IOException e) {
                    JOptionPane.showMessageDialog(BottomPanel, e.toString(),"Error execution",JOptionPane.ERROR_MESSAGE);
                    System.out.println(e.toString());
                }
            }
            //System.out.format("End Check!\n");
            if (listProcRunning.isEmpty()){
                 //System.out.format("End TimerTask\n");
                 t.cancel();
            }

        }
    }


private void expandAllNodes(JTree tree, int startingIndex, int rowCount){
    for(int i=startingIndex;i<rowCount;++i){
        tree.expandRow(i);
    }

    if(tree.getRowCount()!=rowCount){
        expandAllNodes(tree, rowCount, tree.getRowCount());
    }
}


 public static synchronized long getPidOfProcess(Process p) {
    long pid = -1;

    try {
      if (p.getClass().getName().equals("java.lang.UNIXProcess")) {
        Field f = p.getClass().getDeclaredField("pid");
        f.setAccessible(true);
        pid = f.getLong(p);
        f.setAccessible(false);
      }
    } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
      pid = -1;
    }
    return pid;
  }


static public ArrayList <ElProcRunning> listProcRunning =  new  ArrayList <ElProcRunning> ();
static public ArrayList <ElProcWaiting> listProcWaiting = new  ArrayList <ElProcWaiting> ();
static public DefaultListModel<ListEntry> listModel= new DefaultListModel <ListEntry> ();


//int MaxSizelistProcRunning=1;
static public GlobalStatus GL =new GlobalStatus();
//String DefaultThread="8";
static public GlobalSetting GS =new GlobalSetting();




static public class DefaultContextMenu extends JPopupMenu
{
    private final Clipboard clipboard;

    private UndoManager undoManager;

    private final JMenuItem undo;
    private final JMenuItem redo;
    private final JMenuItem cut;
    private final JMenuItem copy;
    private final JMenuItem paste;
    private final JMenuItem delete;
    private final JMenuItem selectAll;

    private JTextComponent jTextComponent;
    private static final long serialVersionUID = 5778212333L;

    public DefaultContextMenu()
    {
        java.net.URL imgURL;

        undoManager = new UndoManager();
        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        undo = new JMenuItem("Undo");
        undo.setEnabled(false);
        imgURL = getClass().getResource("/pkgConnector/images/undomenu.png");
        ImageIcon imageundo = new ImageIcon(imgURL);
        undo.setIcon(imageundo);
        undo.setAccelerator(KeyStroke.getKeyStroke("control Z"));
        undo.addActionListener(event -> undoManager.undo());

        add(undo);

        redo = new JMenuItem("Redo");
        redo.setEnabled(false);
        imgURL = getClass().getResource("/pkgConnector/images/redomenu.png");
        ImageIcon imageredo = new ImageIcon(imgURL);
        redo.setIcon(imageredo);
        redo.setAccelerator(KeyStroke.getKeyStroke("control Y"));
        redo.addActionListener(event -> undoManager.redo());

        add(redo);

        add(new JSeparator());

        cut = new JMenuItem("Cut");
        cut.setEnabled(false);
        imgURL = getClass().getResource("/pkgConnector/images/cutmenu.png");
        ImageIcon imagecut = new ImageIcon(imgURL);
        cut.setIcon(imagecut);
        cut.setAccelerator(KeyStroke.getKeyStroke("control X"));
        cut.addActionListener(event -> jTextComponent.cut());

        add(cut);

        copy = new JMenuItem("Copy");
        copy.setEnabled(false);
        imgURL = getClass().getResource("/pkgConnector/images/copymenu.png");
        ImageIcon imagecopy = new ImageIcon(imgURL);
        copy.setIcon(imagecopy);
        copy.setAccelerator(KeyStroke.getKeyStroke("control C"));
        copy.addActionListener(event -> jTextComponent.copy());

        add(copy);

        paste = new JMenuItem("Paste");
        paste.setEnabled(false);
        imgURL = getClass().getResource("/pkgConnector/images/pastemenu.png");
        ImageIcon imagepaste = new ImageIcon(imgURL);
        paste.setIcon(imagepaste);
        paste.setAccelerator(KeyStroke.getKeyStroke("control V"));
        paste.addActionListener(event -> jTextComponent.paste());

        add(paste);

        delete = new JMenuItem("Delete");
        delete.setEnabled(false);
        imgURL = getClass().getResource("/pkgConnector/images/deletemenu.png");
        ImageIcon imagedelete = new ImageIcon(imgURL);
        delete.setIcon(imagedelete);
        delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        delete.addActionListener(event -> jTextComponent.replaceSelection(""));

        add(delete);

        add(new JSeparator());

        selectAll = new JMenuItem("Select All");
        selectAll.setEnabled(false);
        selectAll.setAccelerator(KeyStroke.getKeyStroke("control A"));
        selectAll.addActionListener(event -> jTextComponent.selectAll());

        add(selectAll);
    }

    public void add(JTextComponent jTextComponent)
    {
        jTextComponent.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent pressedEvent)
            {
                if ((pressedEvent.getKeyCode() == KeyEvent.VK_Z)
                        && ((pressedEvent.getModifiers() & KeyEvent.CTRL_MASK) != 0))
                {
                    if (undoManager.canUndo())
                    {
                        undoManager.undo();
                    }
                }

                if ((pressedEvent.getKeyCode() == KeyEvent.VK_Y)
                        && ((pressedEvent.getModifiers() & KeyEvent.CTRL_MASK) != 0))
                {
                    if (undoManager.canRedo())
                    {
                        undoManager.redo();
                    }
                }
            }
        });

        jTextComponent.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent releasedEvent)
            {
                if (releasedEvent.getButton() == MouseEvent.BUTTON3)
                {
                    processClick(releasedEvent);
                }
            }
        });

        jTextComponent.getDocument().addUndoableEditListener(event -> undoManager.addEdit(event.getEdit()));
    }

    private void processClick(MouseEvent event)
    {
        jTextComponent = (JTextComponent) event.getSource();
        jTextComponent.requestFocus();

        boolean enableUndo = undoManager.canUndo();
        boolean enableRedo = undoManager.canRedo();
        boolean enableCut = false;
        boolean enableCopy = false;
        boolean enablePaste = false;
        boolean enableDelete = false;
        boolean enableSelectAll = false;

        String selectedText = jTextComponent.getSelectedText();
        String text = jTextComponent.getText();

        if (text != null)
        {
            if (text.length() > 0)
            {
                enableSelectAll = true;
            }
        }

        if (selectedText != null)
        {
            if (selectedText.length() > 0)
            {
                enableCut = true;
                enableCopy = true;
                enableDelete = true;
            }
        }

        if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor) && jTextComponent.isEnabled())
        {
            enablePaste = true;
        }

        undo.setEnabled(enableUndo);
        redo.setEnabled(enableRedo);
        cut.setEnabled(enableCut);
        copy.setEnabled(enableCopy);
        paste.setEnabled(enablePaste);
        delete.setEnabled(enableDelete);
        selectAll.setEnabled(enableSelectAll);

        show(jTextComponent, event.getX(), event.getY());
    }
    }

public static DefaultContextMenu contextMenu = new DefaultContextMenu();

  static String prefRootNode = "/org/unito/HashChekerGUI";
   // Preferences
    public static Preferences getPreferences() {
        assert prefRootNode != null;
        Preferences root = Preferences.userRoot();
        return root.node(prefRootNode);
    }

    /**
     * @param stuff Component object calling the method
     * @param commandName Name of the task we are going to execute. It will appears in the process status panel
     * @param script ScriptCaller object containing parameters and so on
     */
    public static void execCommand(Component stuff, String commandName, ScriptCaller script) {
        String command = "";
        try {
            command = script.getCommandLineString();
        } 
        catch (IOException e) {
            System.out.println("Unable to create the temporary script");
            return;
        }

        String outputFolder = script.outputFolder;
        String[] cmd = {"/bin/bash","-c", String.format("%s >& %s/outputExecution", command, outputFolder)};
        Runtime rt = Runtime.getRuntime();

        try {
            if (MainFrame.listProcRunning.size() < MainFrame.GS.getMaxSizelistProcRunning()) {
                Process pr = rt.exec(cmd);
                MainFrame.ElProcRunning tmp= new MainFrame.ElProcRunning(commandName, outputFolder,pr,MainFrame.listModel.getSize());
                MainFrame.listProcRunning.add(tmp);
                java.net.URL imgURL = MainFrame.class.getResource("/pkgConnector/images/running.png");
                ImageIcon image2 = new ImageIcon(imgURL);
                MainFrame.GL.setAvoidProcListValueChanged(-1);
                MainFrame.listModel.addElement(new MainFrame.ListEntry(" [Running]   "+tmp.toString(),"Running",tmp.path, image2 ));
                MainFrame.GL.setAvoidProcListValueChanged(0);
                if (MainFrame.listProcRunning.size() == 1) {
                    MainFrame.t=new Timer();
                    MainFrame.t.scheduleAtFixedRate(new MainFrame.MyTask(), 5000, 5000);
                }
            }
            else {
                MainFrame.ElProcWaiting tmp= new MainFrame.ElProcWaiting(commandName, outputFolder, cmd,MainFrame.listModel.getSize());
                MainFrame.listProcWaiting.add(tmp);
                java.net.URL imgURL = MainFrame.class.getResource("/pkgConnector/images/waiting.png");
                ImageIcon image2 = new ImageIcon(imgURL);
                MainFrame.GL.setAvoidProcListValueChanged(-1);
                MainFrame.listModel.addElement(new MainFrame.ListEntry(" [Waiting]   "+tmp.toString(),"Waiting",tmp.path,image2));
                MainFrame.GL.setAvoidProcListValueChanged(0);
            }
            MainFrame.GL.setAvoidProcListValueChanged(-1);
            MainFrame.ProcList.setModel(MainFrame.listModel);
            MainFrame.ProcList.setCellRenderer(new MainFrame.ListEntryCellRenderer());
            MainFrame.GL.setAvoidProcListValueChanged(0);
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(stuff, e.toString(),"Error execution",JOptionPane.ERROR_MESSAGE);
            System.out.println(e.toString());
        }
        JOptionPane.showMessageDialog(stuff, String.format("%s task was scheduled", commandName),"Confermation",JOptionPane.INFORMATION_MESSAGE);
    }

    public static void execCommand(Component stuff, String commandName, String script, String commandArgs, String outputFolder) {
        //bash ./nomescript argList outputFolder >& outputFolder/outputExecution
        String bashString = String.format(
            "bash ./%s %s %s >& %s/outputExecution", script, commandArgs, outputFolder, outputFolder);
        String[] cmd = {"/bin/bash","-c", bashString};
        Runtime rt = Runtime.getRuntime();

        try {
            if (MainFrame.listProcRunning.size() < MainFrame.GS.getMaxSizelistProcRunning()) {
                Process pr = rt.exec(cmd);
                MainFrame.ElProcRunning tmp= new MainFrame.ElProcRunning(commandName, outputFolder,pr,MainFrame.listModel.getSize());
                MainFrame.listProcRunning.add(tmp);
                java.net.URL imgURL = MainFrame.class.getResource("/pkgConnector/images/running.png");
                ImageIcon image2 = new ImageIcon(imgURL);
                MainFrame.GL.setAvoidProcListValueChanged(-1);
                MainFrame.listModel.addElement(new MainFrame.ListEntry(" [Running]   "+tmp.toString(),"Running",tmp.path, image2 ));
                MainFrame.GL.setAvoidProcListValueChanged(0);
                if (MainFrame.listProcRunning.size() == 1) {
                    MainFrame.t=new Timer();
                    MainFrame.t.scheduleAtFixedRate(new MainFrame.MyTask(), 5000, 5000);
                }
            }
            else {
                MainFrame.ElProcWaiting tmp= new MainFrame.ElProcWaiting(commandName, outputFolder, cmd,MainFrame.listModel.getSize());
                MainFrame.listProcWaiting.add(tmp);
                java.net.URL imgURL = MainFrame.class.getResource("/pkgConnector/images/waiting.png");
                ImageIcon image2 = new ImageIcon(imgURL);
                MainFrame.GL.setAvoidProcListValueChanged(-1);
                MainFrame.listModel.addElement(new MainFrame.ListEntry(" [Waiting]   "+tmp.toString(),"Waiting",tmp.path,image2));
                MainFrame.GL.setAvoidProcListValueChanged(0);
            }
            MainFrame.GL.setAvoidProcListValueChanged(-1);
            MainFrame.ProcList.setModel(MainFrame.listModel);
            MainFrame.ProcList.setCellRenderer(new MainFrame.ListEntryCellRenderer());
            MainFrame.GL.setAvoidProcListValueChanged(0);
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(stuff, e.toString(),"Error execution",JOptionPane.ERROR_MESSAGE);
            System.out.println(e.toString());
        }
        JOptionPane.showMessageDialog(stuff, String.format("%s task was scheduled", commandName),"Confermation",JOptionPane.INFORMATION_MESSAGE);
    }

    public static void setCard(String cardName) {
        cardName = cardName == null ? "Empty" : cardName;

        CardLayout card = (CardLayout) MainPanel.getLayout();
        card.show(MainPanel, cardName);
        CurrentLayout = cardName;
    }

    public static String browsePath(Component caller, int mode, FileNameExtensionFilter filter) {
        JFileChooser chooser = new JFileChooser();
        String selectedPath = null; 
        
        if (filter != null) 
            chooser.setFileFilter(filter);
        
        chooser.setFileSelectionMode(mode);
        
        String current = MainFrame.getPreferences().get("open-dir", null);
        chooser.setCurrentDirectory(current != null ? new File(current) : null);
        
        if (chooser.showOpenDialog(caller) == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            MainFrame.getPreferences().put("open-dir", f.getParent());
            selectedPath = String.valueOf(f);
        }
        return selectedPath; 
    }
    
    public static JFileChooser browseTextFieldContent(Component caller, JTextField textfield, int mode) {
        /** mode = {JFileChooser.FILES_ONLY, JFileChooser.DIRECTORIES_ONLY, JFileChooser.FILES_AND_DIRECTORIES} */
        JFileChooser openDir = new JFileChooser();

        if (!textfield.getText().isEmpty()){
            File file = new File(textfield.getText());
            if (file.isDirectory())
                openDir.setCurrentDirectory(file);
        }
        else {
            String curDir = MainFrame.getPreferences().get("open-dir", null);
            openDir.setCurrentDirectory(curDir != null ? new File(curDir) : null);
        }

        openDir.setFileSelectionMode(mode);

        if (openDir.showOpenDialog(caller) == JFileChooser.APPROVE_OPTION) {
            File f = openDir.getSelectedFile();
            textfield.setText(String.valueOf(f));
        }
        MainFrame.getPreferences().put("open-dir",openDir.getCurrentDirectory().getAbsolutePath());

        return openDir;
    }
}
