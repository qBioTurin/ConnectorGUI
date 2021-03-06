/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgConnector;

import java.awt.CardLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import static pkgConnector.MainFrame.getPreferences;

/**
 *
 * @author beccuti
 */
public class DBindex extends javax.swing.JPanel {
    
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

    /**
     * Creates new form BestClusterChoice
     */
    public DBindex() {
        initComponents();
    }

    private static final long serialVersionUID = 5778212333L;
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        MultiQCGroup = new javax.swing.ButtonGroup();
        DBindex = new javax.swing.JPanel();
        ExecButton = new javax.swing.JButton();
        jButton47 = new javax.swing.JButton();
        vCloseButton7 = new javax.swing.JButton();
        jPanel36 = new javax.swing.JPanel();
        ConnListText = new javax.swing.JTextField();
        jToggleButton40 = new javax.swing.JToggleButton();
        jToggleButton41 = new javax.swing.JToggleButton();
        jButton31 = new javax.swing.JToggleButton();
        jToggleButton43 = new javax.swing.JToggleButton();
        OutputFolderText = new javax.swing.JTextField();
        jLabel128 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        DBindex.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(30, 1, 1, 1), "fDB indexes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 14), new java.awt.Color(0, 51, 204))); // NOI18N
        DBindex.setToolTipText(null);
        DBindex.setLayout(new java.awt.GridBagLayout());

        ExecButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/exec.png"))); // NOI18N
        ExecButton.setText("Execute");
        ExecButton.setToolTipText("Save the fDB indexes as csv.");
        ExecButton.setMaximumSize(new java.awt.Dimension(140, 30));
        ExecButton.setMinimumSize(new java.awt.Dimension(140, 30));
        ExecButton.setPreferredSize(new java.awt.Dimension(140, 30));
        ExecButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExecButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        DBindex.add(ExecButton, gridBagConstraints);

        jButton47.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/reset.png"))); // NOI18N
        jButton47.setText("Reset");
        jButton47.setToolTipText("Settings reset.");
        jButton47.setMaximumSize(new java.awt.Dimension(100, 30));
        jButton47.setMinimumSize(new java.awt.Dimension(100, 30));
        jButton47.setPreferredSize(new java.awt.Dimension(100, 30));
        jButton47.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton47ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        DBindex.add(jButton47, gridBagConstraints);

        vCloseButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/close.png"))); // NOI18N
        vCloseButton7.setText("Close");
        vCloseButton7.setToolTipText(null);
        vCloseButton7.setMaximumSize(new java.awt.Dimension(100, 30));
        vCloseButton7.setMinimumSize(new java.awt.Dimension(100, 30));
        vCloseButton7.setPreferredSize(new java.awt.Dimension(100, 30));
        vCloseButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vCloseButton7ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        DBindex.add(vCloseButton7, gridBagConstraints);

        jPanel36.setBackground(new java.awt.Color(248, 248, 248));
        jPanel36.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Files:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 3, 14))); // NOI18N
        jPanel36.setToolTipText(null);
        jPanel36.setLayout(new java.awt.GridBagLayout());

        ConnListText.setEditable(false);
        ConnListText.setToolTipText("RData storing the clustered ConnectorList(s) generated from the \"FCM execution\" step.");
        ConnListText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConnListTextActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 250;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 30, 10, 10);
        jPanel36.add(ConnListText, gridBagConstraints);

        jToggleButton40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/filebR.png"))); // NOI18N
        jToggleButton40.setText("Browser");
        jToggleButton40.setToolTipText("Selection of the RData storing the clustered ConnectorList.");
        jToggleButton40.setMaximumSize(new java.awt.Dimension(110, 30));
        jToggleButton40.setMinimumSize(new java.awt.Dimension(110, 30));
        jToggleButton40.setPreferredSize(new java.awt.Dimension(110, 30));
        jToggleButton40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton40ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel36.add(jToggleButton40, gridBagConstraints);

        jToggleButton41.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/33b.png"))); // NOI18N
        jToggleButton41.setText("Cancel");
        jToggleButton41.setToolTipText(null);
        jToggleButton41.setMaximumSize(new java.awt.Dimension(110, 30));
        jToggleButton41.setMinimumSize(new java.awt.Dimension(110, 30));
        jToggleButton41.setPreferredSize(new java.awt.Dimension(110, 30));
        jToggleButton41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton41ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel36.add(jToggleButton41, gridBagConstraints);

        jButton31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/52b.png"))); // NOI18N
        jButton31.setText("Browser");
        jButton31.setToolTipText("Folder selection.");
        jButton31.setMaximumSize(new java.awt.Dimension(110, 30));
        jButton31.setMinimumSize(new java.awt.Dimension(110, 30));
        jButton31.setPreferredSize(new java.awt.Dimension(110, 30));
        jButton31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton31ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel36.add(jButton31, gridBagConstraints);

        jToggleButton43.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/33b.png"))); // NOI18N
        jToggleButton43.setText("Cancel");
        jToggleButton43.setToolTipText(null);
        jToggleButton43.setMaximumSize(new java.awt.Dimension(110, 30));
        jToggleButton43.setMinimumSize(new java.awt.Dimension(110, 30));
        jToggleButton43.setPreferredSize(new java.awt.Dimension(110, 30));
        jToggleButton43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton43ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel36.add(jToggleButton43, gridBagConstraints);

        OutputFolderText.setEditable(false);
        OutputFolderText.setToolTipText("Output folder where the fDB indexes will be saved as csv files.");
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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 30, 10, 10);
        jPanel36.add(OutputFolderText, gridBagConstraints);

        jLabel128.setText("Output Folder:");
        jLabel128.setToolTipText(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.ABOVE_BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel36.add(jLabel128, gridBagConstraints);

        jLabel2.setText("<html>Connector List<br>with multiple clust:</html>");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.ABOVE_BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel36.add(jLabel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        DBindex.add(jPanel36, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        add(DBindex, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void ExecButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExecButtonActionPerformed

        if (ConnListText.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "You have to specified an input folder","Error: Data  folder",JOptionPane.ERROR_MESSAGE);
            //mFastQFolderText.requestFocusInWindow();
        }
        else
        {
            //Field check

        //execute code
        Runtime rt = Runtime.getRuntime();
        try{
            String[] cmd = {"/bin/bash","-c"," bash ./ExecFile/ExecExtrap.sh "};
            cmd[2]+= " input.file=\\\""+ConnListText.getText()+"\\\"";
            cmd[2]+= " output.folder=\\\""+OutputFolderText.getText()+"\\\"";
            cmd[2]+= " k=\\\"0\\\"";
            cmd[2]+= " mood=\\\"3\\\"" ;
            cmd[2]+=" "+OutputFolderText.getText() +" >& "+OutputFolderText.getText()+"/outputExecution ";            
//ProcessStatus.setText(pr.toString());
            
            if (MainFrame.listProcRunning.size()<MainFrame.GS.getMaxSizelistProcRunning()){
                Process pr = rt.exec(cmd);
                MainFrame.ElProcRunning tmp= new MainFrame.ElProcRunning("fDB indexes extrapolation ", OutputFolderText.getText(),pr,MainFrame.listModel.getSize());
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
                MainFrame.ElProcWaiting tmp= new MainFrame.ElProcWaiting("fDB indexes extrapolation", OutputFolderText.getText(),cmd,MainFrame.listModel.getSize());
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
        JOptionPane.showMessageDialog(this, "DB indexes extrapolation task was scheduled","Confermation",JOptionPane.INFORMATION_MESSAGE);
        //execute code
        }
        //execute code
    }//GEN-LAST:event_ExecButtonActionPerformed

    private void jButton47ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton47ActionPerformed
        ConnListText.setText("");
        OutputFolderText.setText("");
    }//GEN-LAST:event_jButton47ActionPerformed

    private void vCloseButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vCloseButton7ActionPerformed
        ConnListText.setText("");
        //RESET FIELDS
        CardLayout card = (CardLayout)MainFrame.MainPanel.getLayout();
        card.show(MainFrame.MainPanel, "Empty");
        MainFrame.CurrentLayout="Empty";
        //        AnalysisTree.clearSelection();
    }//GEN-LAST:event_vCloseButton7ActionPerformed

    private void ConnListTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConnListTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ConnListTextActionPerformed

    private void jToggleButton40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton40ActionPerformed
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
            String curDir = getPreferences().get("open-dir", null);
            openDir.setCurrentDirectory(curDir!=null ? new File(curDir) : null);
        }
        
        openDir.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (openDir.showOpenDialog(this)==JFileChooser.APPROVE_OPTION){
            File f = openDir.getSelectedFile();
            try {
                String outPath = openDir.getCurrentDirectory().getAbsolutePath();
                MainFrame.CallingR(this, f, ConnListText, null , null, null, 1, outPath );                 
                OutputFolderText.setText(outPath);                
            } catch (IOException ex) {
                Logger.getLogger(ConsensusMatrix.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                    Logger.getLogger(ConsensusMatrix.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        getPreferences().put("open-dir",openDir.getCurrentDirectory().getAbsolutePath());
        

    }//GEN-LAST:event_jToggleButton40ActionPerformed

    private void jToggleButton41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton41ActionPerformed
        ConnListText.setText("");
    }//GEN-LAST:event_jToggleButton41ActionPerformed

    private void jButton31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton31ActionPerformed
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
        
    }//GEN-LAST:event_jButton31ActionPerformed

    private void jToggleButton43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton43ActionPerformed
        OutputFolderText.setText("");
    }//GEN-LAST:event_jToggleButton43ActionPerformed

    private void OutputFolderTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OutputFolderTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_OutputFolderTextActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField ConnListText;
    private javax.swing.JPanel DBindex;
    private javax.swing.JButton ExecButton;
    private javax.swing.ButtonGroup MultiQCGroup;
    private javax.swing.JTextField OutputFolderText;
    private javax.swing.JToggleButton jButton31;
    private javax.swing.JButton jButton47;
    private javax.swing.JLabel jLabel128;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JToggleButton jToggleButton40;
    private javax.swing.JToggleButton jToggleButton41;
    private javax.swing.JToggleButton jToggleButton43;
    private javax.swing.JButton vCloseButton7;
    // End of variables declaration//GEN-END:variables
}
