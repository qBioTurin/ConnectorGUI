/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgConnector;

//import com.sun.xml.internal.ws.util.StringUtils;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.DefaultComboBoxModel;
import static jdk.nashorn.tools.ShellFunctions.input;


/**
 *
 * @author Simone Pernice
 */
public class DataCuttingPanel extends javax.swing.JPanel {


    /**
     * 
     * Creates new form DataVisualPanel
     */
    
     public class FileTypeFilter extends FileFilter {
        private String extension;
        private String description;

        public FileTypeFilter(String extension, String description) {
            this.extension = extension;
            this.description = description;
        }

        public boolean accept(File file) {
            if (file.isDirectory()) {
                return true;
            }
            return file.getName().endsWith(extension);
        }

        public String getDescription() {
            return description + String.format(" (*%s)", extension);
        }
    }
      
     FileFilter RDataFilter = new FileTypeFilter(".RData", "R enviroments");
     FileFilter txtFilter =  new FileTypeFilter(".txt", "Text Documents");

      
    public DataCuttingPanel() {
        initComponents();
           
    }
    private static final long serialVersionUID = 57756333321L;

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        DataType = new javax.swing.ButtonGroup();
        Covariates = new javax.swing.ButtonGroup();
        DataCuttingPanel = new javax.swing.JPanel();
        vCloseButton2 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        ResetButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        ConnListText = new javax.swing.JTextField();
        ConnListVisualButton = new javax.swing.JToggleButton();
        fCancelButton = new javax.swing.JToggleButton();
        OutputFolderText = new javax.swing.JTextField();
        jToggleButton13 = new javax.swing.JToggleButton();
        fCancelButton1 = new javax.swing.JToggleButton();
        jLabel35 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        ComboFeatBox = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        TitleText = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        XlabText = new javax.swing.JTextField();
        YlabText = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        TrunSlider = new javax.swing.JSlider();
        ShowPlotButton1 = new javax.swing.JToggleButton();

        java.awt.GridBagLayout layout = new java.awt.GridBagLayout();
        layout.columnWidths = new int[] {0};
        layout.rowHeights = new int[] {0, 5, 0};
        setLayout(layout);

        DataCuttingPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(30, 1, 1, 1), "Data Cutting", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 14), new java.awt.Color(0, 51, 204))); // NOI18N
        DataCuttingPanel.setToolTipText(null);
        DataCuttingPanel.setLayout(new java.awt.GridBagLayout());

        vCloseButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/close.png"))); // NOI18N
        vCloseButton2.setText("Close");
        vCloseButton2.setMaximumSize(new java.awt.Dimension(100, 30));
        vCloseButton2.setMinimumSize(new java.awt.Dimension(100, 30));
        vCloseButton2.setPreferredSize(new java.awt.Dimension(100, 30));
        vCloseButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vCloseButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        DataCuttingPanel.add(vCloseButton2, gridBagConstraints);

        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/exec.png"))); // NOI18N
        jButton15.setText("Execute");
        jButton15.setToolTipText("Truncation of the data.");
        jButton15.setMaximumSize(new java.awt.Dimension(140, 30));
        jButton15.setMinimumSize(new java.awt.Dimension(140, 30));
        jButton15.setPreferredSize(new java.awt.Dimension(140, 30));
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        DataCuttingPanel.add(jButton15, gridBagConstraints);

        ResetButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/reset.png"))); // NOI18N
        ResetButton.setText("Reset");
        ResetButton.setToolTipText("Settings reset.");
        ResetButton.setMaximumSize(new java.awt.Dimension(100, 30));
        ResetButton.setMinimumSize(new java.awt.Dimension(100, 30));
        ResetButton.setPreferredSize(new java.awt.Dimension(100, 30));
        ResetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ResetButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        DataCuttingPanel.add(ResetButton, gridBagConstraints);

        jPanel5.setBackground(new java.awt.Color(248, 248, 248));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Files:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 3, 14))); // NOI18N
        jPanel5.setToolTipText(null);
        jPanel5.setLayout(new java.awt.GridBagLayout());

        jLabel29.setText("Output folders:");
        jLabel29.setToolTipText(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.ABOVE_BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel5.add(jLabel29, gridBagConstraints);

        ConnListText.setEditable(false);
        ConnListText.setToolTipText("RData storing the ConnectorList generated from the \"Data Import\" step.");
        ConnListText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConnListTextActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.ABOVE_BASELINE_LEADING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 30, 10, 10);
        jPanel5.add(ConnListText, gridBagConstraints);

        ConnListVisualButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/filebR.png"))); // NOI18N
        ConnListVisualButton.setText("Browser");
        ConnListVisualButton.setToolTipText("Selection of the RData storing the ConnectorList to truncate.");
        ConnListVisualButton.setMaximumSize(new java.awt.Dimension(110, 30));
        ConnListVisualButton.setMinimumSize(new java.awt.Dimension(110, 30));
        ConnListVisualButton.setPreferredSize(new java.awt.Dimension(110, 30));
        ConnListVisualButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConnListVisualButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel5.add(ConnListVisualButton, gridBagConstraints);

        fCancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/33b.png"))); // NOI18N
        fCancelButton.setText("Cancel");
        fCancelButton.setMaximumSize(new java.awt.Dimension(110, 30));
        fCancelButton.setMinimumSize(new java.awt.Dimension(110, 30));
        fCancelButton.setPreferredSize(new java.awt.Dimension(110, 30));
        fCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fCancelButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel5.add(fCancelButton, gridBagConstraints);

        OutputFolderText.setEditable(false);
        OutputFolderText.setToolTipText("Output folder where the RData storing the ConnectorList with the truncated data will be saved.");
        OutputFolderText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OutputFolderTextActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 250;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.ABOVE_BASELINE_LEADING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 30, 10, 10);
        jPanel5.add(OutputFolderText, gridBagConstraints);

        jToggleButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/52b.png"))); // NOI18N
        jToggleButton13.setText("Browser");
        jToggleButton13.setToolTipText("Folder selection.");
        jToggleButton13.setMaximumSize(new java.awt.Dimension(110, 30));
        jToggleButton13.setMinimumSize(new java.awt.Dimension(110, 30));
        jToggleButton13.setPreferredSize(new java.awt.Dimension(110, 30));
        jToggleButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton13ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel5.add(jToggleButton13, gridBagConstraints);

        fCancelButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/33b.png"))); // NOI18N
        fCancelButton1.setText("Cancel");
        fCancelButton1.setMaximumSize(new java.awt.Dimension(110, 30));
        fCancelButton1.setMinimumSize(new java.awt.Dimension(110, 30));
        fCancelButton1.setPreferredSize(new java.awt.Dimension(110, 30));
        fCancelButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fCancelButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel5.add(fCancelButton1, gridBagConstraints);

        jLabel35.setText("Connector RData:");
        jLabel35.setToolTipText(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.ABOVE_BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel5.add(jLabel35, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        DataCuttingPanel.add(jPanel5, gridBagConstraints);

        jPanel11.setBackground(new java.awt.Color(248, 248, 248));
        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Plot info:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 3, 14))); // NOI18N
        jPanel11.setToolTipText(null);
        jPanel11.setLayout(new java.awt.GridBagLayout());

        jLabel38.setText("Features:");
        jLabel38.setToolTipText(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 70);
        jPanel11.add(jLabel38, gridBagConstraints);

        ComboFeatBox.setToolTipText("Feature selection. The curve colors will be defined depending the feature choosen.");
        ComboFeatBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboFeatBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 250;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel11.add(ComboFeatBox, gridBagConstraints);

        jLabel1.setText("Title:");
        jLabel1.setToolTipText(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 70);
        jPanel11.add(jLabel1, gridBagConstraints);

        jLabel2.setText("X-axis label:");
        jLabel2.setToolTipText(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 70);
        jPanel11.add(jLabel2, gridBagConstraints);

        TitleText.setToolTipText("Plot title.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 250;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel11.add(TitleText, gridBagConstraints);

        jLabel3.setText("Y-axis label:");
        jLabel3.setToolTipText(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 70);
        jPanel11.add(jLabel3, gridBagConstraints);

        XlabText.setToolTipText("X-axis name.");
        XlabText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                XlabTextActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 250;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel11.add(XlabText, gridBagConstraints);

        YlabText.setToolTipText("Y-axis name.");
        YlabText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                YlabTextActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 250;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel11.add(YlabText, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        DataCuttingPanel.add(jPanel11, gridBagConstraints);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        DataCuttingPanel.add(jPanel1, gridBagConstraints);

        jPanel2.setBackground(new java.awt.Color(248, 248, 248));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Truncation:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 3, 14))); // NOI18N
        jPanel2.setToolTipText(null);
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel4.setText("Truncation time:");
        jLabel4.setToolTipText(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 35);
        jPanel2.add(jLabel4, gridBagConstraints);

        TrunSlider.setMaximum(0);
        TrunSlider.setToolTipText("Time point for truncating the data.");
        TrunSlider.setValue(0);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel2.add(TrunSlider, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        DataCuttingPanel.add(jPanel2, gridBagConstraints);

        ShowPlotButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/plotsmall.png"))); // NOI18N
        ShowPlotButton1.setText("Show plot");
        ShowPlotButton1.setToolTipText("Show the growth curve plot with a vertical line at the truncating time.");
        ShowPlotButton1.setMaximumSize(new java.awt.Dimension(140, 30));
        ShowPlotButton1.setMinimumSize(new java.awt.Dimension(140, 30));
        ShowPlotButton1.setOpaque(false);
        ShowPlotButton1.setPreferredSize(new java.awt.Dimension(140, 30));
        ShowPlotButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ShowPlotButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        DataCuttingPanel.add(ShowPlotButton1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        add(DataCuttingPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed

        //Field check

        if (ConnListText.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "You have to specified an input file","Error: input file",JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (OutputFolderText.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "You have to specified an output  folder","Error: output folder",JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String Titletxt;
        String Xtxt;
        String Ytxt;
        
        if (YlabText.getText().isEmpty()){
            Ytxt = "NA";
        }else{
            Ytxt = YlabText.getText();
        }
        
        if (XlabText.getText().isEmpty()){
            Xtxt = "NA";
        }else{
            Xtxt = XlabText.getText();
        }
        
        if (TitleText.getText().isEmpty()){
            Titletxt = "NA";
        }else{
            Titletxt = TitleText.getText();
        }

       /** try
        {
            Integer x = Integer.valueOf(pComponent1Text.getText());
            if (x<=0){
                JOptionPane.showMessageDialog(this, "You have to specified a value greater than 0.","Error: Component 1 number",JOptionPane.ERROR_MESSAGE);
                pComponent1Text.requestFocusInWindow();
                return;
            }
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "You have to specified the number of component 1.","Error: Component 1 number",JOptionPane.ERROR_MESSAGE);
            pComponent1Text.requestFocusInWindow();
            return;
        }

        try
        {
            Integer x = Integer.valueOf(pComponent2Text.getText());
            if (x<=0){
                JOptionPane.showMessageDialog(this, "You have to specified a value greater than 0.","Error: Component 2 number",JOptionPane.ERROR_MESSAGE);
                pComponent2Text.requestFocusInWindow();
                return;
            }
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "You have to specified the number of component 2.","Error: Component 2 number",JOptionPane.ERROR_MESSAGE);
            pComponent2Text.requestFocusInWindow();
            return;
        }*/

        Runtime rt = Runtime.getRuntime();
        //execute code

        try{
            String[] cmd = {"/bin/bash","-c","  bash ./ExecFile/ExecTrunc.sh "};
                        
            cmd[2]+= " input.file=\\\""+ConnListText.getText()+"\\\"";
            cmd[2]+= " output.PlotFolder=\\\""+OutputFolderText.getText()+"\\\"";
            cmd[2]+= " Trunc.Time=\\\""+TrunSlider.getValue()+"\\\"";
            cmd[2]+= " feature=\\\""+ ComboFeatBox.getItemAt(ComboFeatBox.getSelectedIndex()) + "\\\"";
            cmd[2]+=  " title=\\\""+Titletxt+"\\\" labels.x=\\\""+Xtxt+"\\\" labels.y=\\\""+Ytxt+ "\\\"";
            cmd[2]+=" "+ OutputFolderText.getText()+" >& "+OutputFolderText.getText()+"/outputExecution ";
            //ProcessStatus.setText(pr.toString());
            if (MainFrame.listProcRunning.size()<MainFrame.GS.getMaxSizelistProcRunning()){
                Process pr = rt.exec(cmd);
                System.out.println(cmd[2]);
                MainFrame.ElProcRunning tmp= new MainFrame.ElProcRunning("Truncation data ", OutputFolderText.getText() ,pr,MainFrame.listModel.getSize());
                MainFrame.listProcRunning.add(tmp);
                java.net.URL imgURL = getClass().getResource("/pkgConnector/images/running.png");
                ImageIcon image2 = new ImageIcon(imgURL);
                MainFrame.GL.setAvoidProcListValueChanged(-1);
                MainFrame.listModel.addElement(new MainFrame.ListEntry(" [Running]   "+tmp.toString(),"Running",tmp.path, image2 ));
                MainFrame.GL.setAvoidProcListValueChanged(0);
                if(MainFrame.listProcRunning.size()==1){
                    MainFrame.t=new Timer();
                    MainFrame.t.scheduleAtFixedRate(new MainFrame.MyTask(), 5000, 5000);
                }
            }
            else{
                MainFrame.ElProcWaiting tmp= new MainFrame.ElProcWaiting("Truncation data ", OutputFolderText.getText(),cmd,MainFrame.listModel.getSize());
                MainFrame.listProcWaiting.add(tmp);
                java.net.URL imgURL = getClass().getResource("/pkgConnector/images/waiting.png");
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
        catch(IOException e) {
            JOptionPane.showMessageDialog(this, e.toString(),"Error execution",JOptionPane.ERROR_MESSAGE);
            System.out.println(e.toString());
        }
        JOptionPane.showMessageDialog(this, "A Truncation data task was scheduled","Confermation",JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButton15ActionPerformed

    private void ResetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ResetButtonActionPerformed
        //RESET FIELDS
        TitleText.setText("");
        XlabText.setText("");
        YlabText.setText("");
        ConnListText.setText("");
        OutputFolderText.setText("");
        ComboFeatBox.removeAllItems();
        TrunSlider.setMaximum(0);
        TrunSlider.setValue(0);
        //RESET FIELDS
    }//GEN-LAST:event_ResetButtonActionPerformed

    private void ConnListVisualButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                     
        JFileChooser openDir = new JFileChooser();
        openDir.addChoosableFileFilter(RDataFilter);
        openDir.setAcceptAllFileFilterUsed(false);
        if (!(ConnListText.getText().equals(""))){
            File file =new File(OutputFolderText.getText());
            if (file.isDirectory())
            openDir.setCurrentDirectory(file);
        }
        else
        {
            String curDir = MainFrame.getPreferences().get("open-dir", null);
            openDir.setCurrentDirectory(curDir!=null ? new File(curDir) : null);
        }
        openDir.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (openDir.showOpenDialog(this)==JFileChooser.APPROVE_OPTION){
            File f = openDir.getSelectedFile();            
            try {
                String outPath = openDir.getCurrentDirectory().getAbsolutePath();
                MainFrame.CallingR(this, f, ConnListText, ComboFeatBox , null, TrunSlider, 4, outPath );                 
                OutputFolderText.setText(openDir.getCurrentDirectory().getAbsolutePath());                
            } catch (IOException ex) {
                Logger.getLogger(ConsensusMatrix.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                    Logger.getLogger(ConsensusMatrix.class.getName()).log(Level.SEVERE, null, ex);
                }
            //UPDATE TO REMOVE OUTPUT FOLDER
            OutputFolderText.setText(openDir.getCurrentDirectory().getAbsolutePath());

        }
        MainFrame.getPreferences().put("open-dir",openDir.getCurrentDirectory().getAbsolutePath());
        //ShowPlotButton1.setEnabled(Files.exists(Paths.get(OutputFolderText.getText(), "DataTruncation.pdf")));

    }                         

    private void fCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fCancelButtonActionPerformed
        ConnListText.setText("");
        //UPDATE TO REMOVE OUTPUT FOLDER
        // OutputFolderText.setText("");
    }//GEN-LAST:event_fCancelButtonActionPerformed

    private void OutputFolderTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OutputFolderTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_OutputFolderTextActionPerformed

    private void jToggleButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton13ActionPerformed
        JFileChooser openDir = new JFileChooser();
        if (!(OutputFolderText.getText().equals(""))){
            File file =new File(OutputFolderText.getText());
            if (file.isDirectory())
            openDir.setCurrentDirectory(file);
        }
        else
        {
            String curDir = MainFrame.getPreferences().get("open-dir", null);
            openDir.setCurrentDirectory(curDir!=null ? new File(curDir) : null);
        }
        openDir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (openDir.showOpenDialog(this)==JFileChooser.APPROVE_OPTION){
            File f = openDir.getSelectedFile();
            OutputFolderText.setText(String.valueOf(f));
        }
        MainFrame.getPreferences().put("open-dir",openDir.getCurrentDirectory().getAbsolutePath());
        //ShowPlotButton1.setEnabled(Files.exists(Paths.get(OutputFolderText.getText(), "DataTruncation.pdf")));

    }//GEN-LAST:event_jToggleButton13ActionPerformed

    private void fCancelButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fCancelButton1ActionPerformed
        OutputFolderText.setText("");
        
    }//GEN-LAST:event_fCancelButton1ActionPerformed

    private void ComboFeatBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboFeatBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ComboFeatBoxActionPerformed

    private void XlabTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_XlabTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_XlabTextActionPerformed

    private void YlabTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_YlabTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_YlabTextActionPerformed

    private void ShowPlotButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ShowPlotButton1ActionPerformed
        
        if( Files.exists(Paths.get(OutputFolderText.getText(), "DataTruncation.pdf")) )
        {
            String pathfile = Paths.get(OutputFolderText.getText(), "DataTruncation.pdf").toString();
            Desktop desktop = Desktop.getDesktop();
            File file = new File(pathfile);
            try {
                desktop.open(file);
            } catch (IOException ex) {
                Logger.getLogger(PestimPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            JOptionPane.showMessageDialog(this, "No file named DataTruncation.pdf was found!","Error: No file named DataTruncation.pdf was found! ",JOptionPane.ERROR_MESSAGE);

        }
    }//GEN-LAST:event_ShowPlotButton1ActionPerformed

    private void vCloseButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vCloseButton2ActionPerformed

        //RESET FIELDS
        ConnListText.setText("");
        OutputFolderText.setText("");
        //RESET FIELDS
        CardLayout card = (CardLayout)MainFrame.MainPanel.getLayout();
        card.show(MainFrame.MainPanel, "Empty");
        MainFrame.CurrentLayout="Empty";
        //GL.setAvoidProcListValueChanged(-1);
        // AnalysisTree.clearSelection();
    }//GEN-LAST:event_vCloseButton2ActionPerformed

    private void ConnListTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConnListTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ConnListTextActionPerformed
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> ComboFeatBox;
    private javax.swing.JTextField ConnListText;
    private javax.swing.JToggleButton ConnListVisualButton;
    private javax.swing.ButtonGroup Covariates;
    private javax.swing.JPanel DataCuttingPanel;
    private javax.swing.ButtonGroup DataType;
    private javax.swing.JTextField OutputFolderText;
    private javax.swing.JButton ResetButton;
    private javax.swing.JToggleButton ShowPlotButton1;
    private javax.swing.JTextField TitleText;
    private javax.swing.JSlider TrunSlider;
    private javax.swing.JTextField XlabText;
    private javax.swing.JTextField YlabText;
    private javax.swing.JToggleButton fCancelButton;
    private javax.swing.JToggleButton fCancelButton1;
    private javax.swing.JButton jButton15;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JToggleButton jToggleButton13;
    private javax.swing.JButton vCloseButton2;
    // End of variables declaration//GEN-END:variables
}
