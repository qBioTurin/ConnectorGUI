/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgConnector;
import java.awt.CardLayout;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import pkgConnector.MainFrame.MyTask;
import javax.swing.filechooser.FileFilter;
 
  
/**
 *
 * @author Simone Pernice
 */
public class DataImportPanel extends javax.swing.JPanel {
    
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
      
     FileFilter xlsxFilter = new FileTypeFilter(".xlsx", "Microsoft Excel Documents");
     FileFilter txtFilter =  new FileTypeFilter(".txt", "Text Documents");
    /**
     * Creates new form DataImportPanel
     */
    public DataImportPanel() {
        initComponents();
    }
    private static final long serialVersionUID = 57752123311L;
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        GATK = new javax.swing.ButtonGroup();
        IExecutionBWA = new javax.swing.ButtonGroup();
        DataImportPanel = new javax.swing.JPanel();
        iCloseButton1 = new javax.swing.JButton();
        ExecImportButton = new javax.swing.JButton();
        iResetButton1 = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        GrowthFolderText = new javax.swing.JTextField();
        BrowGrowthButton = new javax.swing.JToggleButton();
        CancGrowthButton = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();
        FeaturesFolderText = new javax.swing.JTextField();
        BrowFeatButton = new javax.swing.JToggleButton();
        CancelFeatButton = new javax.swing.JToggleButton();
        jLabel2 = new javax.swing.JLabel();
        OutPutFolderText = new javax.swing.JTextField();
        OutputBrowButton = new javax.swing.JToggleButton();
        CancelOutputButton = new javax.swing.JToggleButton();

        setLayout(new java.awt.GridBagLayout());

        DataImportPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(30, 1, 1, 1), "Data Import", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 14), new java.awt.Color(0, 51, 204))); // NOI18N
        DataImportPanel.setToolTipText(null);
        DataImportPanel.setLayout(new java.awt.GridBagLayout());

        iCloseButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/close.png"))); // NOI18N
        iCloseButton1.setText("Close");
        iCloseButton1.setMaximumSize(new java.awt.Dimension(100, 30));
        iCloseButton1.setMinimumSize(new java.awt.Dimension(100, 30));
        iCloseButton1.setPreferredSize(new java.awt.Dimension(100, 30));
        iCloseButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iCloseButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        DataImportPanel.add(iCloseButton1, gridBagConstraints);

        ExecImportButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/exec.png"))); // NOI18N
        ExecImportButton.setText("Execute");
        ExecImportButton.setToolTipText("ConnectorList generation.");
        ExecImportButton.setMaximumSize(new java.awt.Dimension(140, 30));
        ExecImportButton.setMinimumSize(new java.awt.Dimension(140, 30));
        ExecImportButton.setPreferredSize(new java.awt.Dimension(140, 30));
        ExecImportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExecImportButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        DataImportPanel.add(ExecImportButton, gridBagConstraints);

        iResetButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/reset.png"))); // NOI18N
        iResetButton1.setText("Reset");
        iResetButton1.setToolTipText("Settings reset.");
        iResetButton1.setMaximumSize(new java.awt.Dimension(100, 30));
        iResetButton1.setMinimumSize(new java.awt.Dimension(100, 30));
        iResetButton1.setPreferredSize(new java.awt.Dimension(100, 30));
        iResetButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iResetButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        DataImportPanel.add(iResetButton1, gridBagConstraints);

        jPanel15.setBackground(new java.awt.Color(247, 248, 248));
        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Files:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 3, 14))); // NOI18N
        jPanel15.setToolTipText(null);
        jPanel15.setLayout(new java.awt.GridBagLayout());

        jLabel37.setText("Growth Data:");
        jLabel37.setToolTipText(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.ABOVE_BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel15.add(jLabel37, gridBagConstraints);

        GrowthFolderText.setEditable(false);
        GrowthFolderText.setToolTipText("The excel file reporting the growth evolution data, composed from two columns for each sample containing the time points and the measure volume respectively.");
        GrowthFolderText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GrowthFolderTextActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 250;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.ABOVE_BASELINE_LEADING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 30, 10, 10);
        jPanel15.add(GrowthFolderText, gridBagConstraints);

        BrowGrowthButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/fileb.png"))); // NOI18N
        BrowGrowthButton.setText("Browser");
        BrowGrowthButton.setToolTipText("Excel file selection.");
        BrowGrowthButton.setMaximumSize(new java.awt.Dimension(110, 30));
        BrowGrowthButton.setMinimumSize(new java.awt.Dimension(110, 30));
        BrowGrowthButton.setPreferredSize(new java.awt.Dimension(110, 30));
        BrowGrowthButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BrowGrowthButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel15.add(BrowGrowthButton, gridBagConstraints);

        CancGrowthButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/33b.png"))); // NOI18N
        CancGrowthButton.setText("Cancel");
        CancGrowthButton.setMaximumSize(new java.awt.Dimension(110, 30));
        CancGrowthButton.setMinimumSize(new java.awt.Dimension(110, 30));
        CancGrowthButton.setPreferredSize(new java.awt.Dimension(110, 30));
        CancGrowthButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancGrowthButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel15.add(CancGrowthButton, gridBagConstraints);

        jLabel1.setText("Features Data:");
        jLabel1.setToolTipText(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.ABOVE_BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel15.add(jLabel1, gridBagConstraints);

        FeaturesFolderText.setEditable(false);
        FeaturesFolderText.setToolTipText("The csv file containing the annotation information associated with the samples. The file should be a comma-delimited file composed by a number of rows equal to the number of samples.");
        FeaturesFolderText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FeaturesFolderTextActionPerformed(evt);
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
        jPanel15.add(FeaturesFolderText, gridBagConstraints);

        BrowFeatButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/fileb.png"))); // NOI18N
        BrowFeatButton.setText("Browser");
        BrowFeatButton.setToolTipText("Csv file selection.");
        BrowFeatButton.setMaximumSize(new java.awt.Dimension(110, 30));
        BrowFeatButton.setMinimumSize(new java.awt.Dimension(110, 30));
        BrowFeatButton.setPreferredSize(new java.awt.Dimension(110, 30));
        BrowFeatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BrowFeatButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel15.add(BrowFeatButton, gridBagConstraints);

        CancelFeatButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/33b.png"))); // NOI18N
        CancelFeatButton.setText("Cancel");
        CancelFeatButton.setMaximumSize(new java.awt.Dimension(110, 30));
        CancelFeatButton.setMinimumSize(new java.awt.Dimension(110, 30));
        CancelFeatButton.setPreferredSize(new java.awt.Dimension(110, 30));
        CancelFeatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelFeatButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel15.add(CancelFeatButton, gridBagConstraints);

        jLabel2.setText("Output Folder:");
        jLabel2.setToolTipText(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.ABOVE_BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel15.add(jLabel2, gridBagConstraints);

        OutPutFolderText.setEditable(false);
        OutPutFolderText.setToolTipText("Output folder where the RData storing the ConnectorList will be saved.");
        OutPutFolderText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OutPutFolderTextActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 250;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.ABOVE_BASELINE_LEADING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 30, 10, 10);
        jPanel15.add(OutPutFolderText, gridBagConstraints);

        OutputBrowButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/52b.png"))); // NOI18N
        OutputBrowButton.setText("Browser");
        OutputBrowButton.setToolTipText("Folder selection.");
        OutputBrowButton.setMaximumSize(new java.awt.Dimension(110, 30));
        OutputBrowButton.setMinimumSize(new java.awt.Dimension(110, 30));
        OutputBrowButton.setPreferredSize(new java.awt.Dimension(110, 30));
        OutputBrowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OutputBrowButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel15.add(OutputBrowButton, gridBagConstraints);

        CancelOutputButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/33b.png"))); // NOI18N
        CancelOutputButton.setText("Cancel");
        CancelOutputButton.setMaximumSize(new java.awt.Dimension(110, 30));
        CancelOutputButton.setMinimumSize(new java.awt.Dimension(110, 30));
        CancelOutputButton.setPreferredSize(new java.awt.Dimension(110, 30));
        CancelOutputButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelOutputButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel15.add(CancelOutputButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        DataImportPanel.add(jPanel15, gridBagConstraints);
        jPanel15.getAccessibleContext().setAccessibleName("");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        add(DataImportPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void iCloseButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iCloseButton1ActionPerformed
        GrowthFolderText.setText("");
        FeaturesFolderText.setText("");
        CardLayout card = (CardLayout)MainFrame.MainPanel.getLayout();
        card.show(MainFrame.MainPanel, "Empty");
        MainFrame.CurrentLayout="Empty";
        // AnalysisTree.clearSelection();
    }//GEN-LAST:event_iCloseButton1ActionPerformed

    private void ExecImportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExecImportButtonActionPerformed
        if (GrowthFolderText.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "You have to specified a Growth Data folder","Error: Genome  folder",JOptionPane.ERROR_MESSAGE);
            return;
        }
        else
        if (FeaturesFolderText.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "You have to specified a Features Data folder","Error: Genome  folder",JOptionPane.ERROR_MESSAGE);
            return;
        }

        //execute code
        Runtime rt = Runtime.getRuntime();
        try{
            String[] cmd = {"/bin/bash","-c","  bash ./ExecFile/ExecDataImport.sh "};
                           
            cmd[2]+= "group=\\\"docker\\\"";                       
            cmd[2]+= " GrowDataFile=\\\""+GrowthFolderText.getText()+"\\\"";
            cmd[2]+= " AnnotationFile=\\\""+FeaturesFolderText.getText()+"\\\"";
            cmd[2]+= " output.folder=\\\""+OutPutFolderText.getText()+"\\\"";           
            cmd[2]+= " "+OutPutFolderText.getText() +" >& "+OutPutFolderText.getText()+"/outputExecution ";
            
        //ProcessStatus.setText(pr.toString());
            if (MainFrame.listProcRunning.size()<MainFrame.GS.getMaxSizelistProcRunning()){
                Process pr = rt.exec(cmd);
                System.out.println("Runing PID:"+ MainFrame.getPidOfProcess(pr)+"\n");
                System.out.println(cmd[2]);
                MainFrame.ElProcRunning tmp= new MainFrame.ElProcRunning("Data Importation ", OutPutFolderText.getText(),pr,MainFrame.listModel.getSize());
                MainFrame.listProcRunning.add(tmp);
                java.net.URL imgURL = getClass().getResource("/pkgConnector/images/running.png");
                ImageIcon image2 = new ImageIcon(imgURL);
                MainFrame.GL.setAvoidProcListValueChanged(-1);
                MainFrame.listModel.addElement(new MainFrame.ListEntry(" [Running]   "+tmp.toString(),"Running",tmp.path, image2 ));
                MainFrame.GL.setAvoidProcListValueChanged(0);
                if(MainFrame.listProcRunning.size()==1){
                    MainFrame.t=new Timer();
                    MainFrame.t.scheduleAtFixedRate(new MyTask(), 5000, 5000);
                }
            }
            else{
                MainFrame.ElProcWaiting tmp= new MainFrame.ElProcWaiting("Data Importation ",GrowthFolderText.getText(),cmd,MainFrame.listModel.getSize());
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

        JOptionPane.showMessageDialog(this, "The ConnectorList creation task was scheduled","Confermation",JOptionPane.INFORMATION_MESSAGE);

    }//GEN-LAST:event_ExecImportButtonActionPerformed

    private void iResetButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iResetButton1ActionPerformed
        GrowthFolderText.setText("");
        FeaturesFolderText.setText("");
    }//GEN-LAST:event_iResetButton1ActionPerformed

    private void CancelFeatButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelFeatButtonActionPerformed
        FeaturesFolderText.setText("");
    }//GEN-LAST:event_CancelFeatButtonActionPerformed

    private void BrowFeatButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BrowFeatButtonActionPerformed
            JFileChooser openDir = new JFileChooser();
            openDir.addChoosableFileFilter(txtFilter);
            openDir.setAcceptAllFileFilterUsed(false);
                if (!(FeaturesFolderText.getText().equals(""))){
                    File file =new File(OutPutFolderText.getText());
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
                    FeaturesFolderText.setText(String.valueOf(f));                    
                }
                MainFrame.getPreferences().put("open-dir",openDir.getCurrentDirectory().getAbsolutePath());
    
    }//GEN-LAST:event_BrowFeatButtonActionPerformed

    private void FeaturesFolderTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FeaturesFolderTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FeaturesFolderTextActionPerformed

    private void CancGrowthButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancGrowthButtonActionPerformed
        GrowthFolderText.setText("");
    }//GEN-LAST:event_CancGrowthButtonActionPerformed

    private void BrowGrowthButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BrowGrowthButtonActionPerformed
        JFileChooser openDir = new JFileChooser();
        openDir.addChoosableFileFilter(xlsxFilter);
        openDir.setAcceptAllFileFilterUsed(false);
                if (!(GrowthFolderText.getText().equals(""))){
                    File file =new File(OutPutFolderText.getText());
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
                    GrowthFolderText.setText(String.valueOf(f));
                    //UPDATE TO REMOVE OUTPUT FOLDER
                    OutPutFolderText.setText(openDir.getCurrentDirectory().getAbsolutePath());
                }
                MainFrame.getPreferences().put("open-dir",openDir.getCurrentDirectory().getAbsolutePath());
    }//GEN-LAST:event_BrowGrowthButtonActionPerformed

    private void GrowthFolderTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GrowthFolderTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_GrowthFolderTextActionPerformed

    private void OutPutFolderTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OutPutFolderTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_OutPutFolderTextActionPerformed

    private void OutputBrowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OutputBrowButtonActionPerformed
        JFileChooser openDir = new JFileChooser();
        if (!(OutPutFolderText.getText().equals(""))){
            File file =new File(OutPutFolderText.getText());
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
            OutPutFolderText.setText(String.valueOf(f));
        }
        MainFrame.getPreferences().put("open-dir",openDir.getCurrentDirectory().getAbsolutePath());
    }//GEN-LAST:event_OutputBrowButtonActionPerformed

    private void CancelOutputButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelOutputButtonActionPerformed
        OutPutFolderText.setText("");
    }//GEN-LAST:event_CancelOutputButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton BrowFeatButton;
    private javax.swing.JToggleButton BrowGrowthButton;
    private javax.swing.JToggleButton CancGrowthButton;
    private javax.swing.JToggleButton CancelFeatButton;
    private javax.swing.JToggleButton CancelOutputButton;
    private javax.swing.JPanel DataImportPanel;
    private javax.swing.JButton ExecImportButton;
    private javax.swing.JTextField FeaturesFolderText;
    private javax.swing.ButtonGroup GATK;
    private javax.swing.JTextField GrowthFolderText;
    private javax.swing.ButtonGroup IExecutionBWA;
    private javax.swing.JTextField OutPutFolderText;
    private javax.swing.JToggleButton OutputBrowButton;
    private javax.swing.JButton iCloseButton1;
    private javax.swing.JButton iResetButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JPanel jPanel15;
    // End of variables declaration//GEN-END:variables
}
