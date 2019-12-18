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
import java.io.FileReader;
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
import pkgConnector.MainFrame.MyTask;
import static pkgConnector.MainFrame.getPreferences;

/**
 *
 * @author pernice
 */
public class SplinePanel extends javax.swing.JPanel {

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

    private void ClusteredDataCheck(File ConnectorListCL) throws FileNotFoundException, IOException
    {
        String line;
        String[]  lin2 = null;        
        ExecButton.setEnabled(false);
        
        Runtime rt = Runtime.getRuntime();
            String cmdcheck = ("Rscript --vanilla  ./Rscripts/ClusteredDataCheck.R "+ ConnectorListCL);
            Process pr = rt.exec(cmdcheck);            
            BufferedReader input =  new BufferedReader(new InputStreamReader(pr.getInputStream()));  
            
            while ((line = input.readLine()) != null) {  
                System.out.println(line);
                if(line.contentEquals("[1] 1"))
                {
                    ExecButton.setEnabled(true);
                }
                else{
                   JOptionPane.showMessageDialog(this, "You have to specified an RData storing a clustered ConnectorList (FCM execution step). Observe that the name of the ConnectorList in the RData must be ConnectorList.FCM! ","Error: Data  input file ",JOptionPane.ERROR_MESSAGE);     
                }
                
                // Bind it to the combobox
         
            }  
            input.close(); 
    }
              
    
    /**
     * Creates new form HeatmapPanel
     */
    public SplinePanel() {
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

        heatmapGroup = new javax.swing.ButtonGroup();
        heatmapGroupLog = new javax.swing.ButtonGroup();
        heatmapBaseGroup = new javax.swing.ButtonGroup();
        Heatmappanel = new javax.swing.JPanel();
        ExecButton = new javax.swing.JButton();
        jButton48 = new javax.swing.JButton();
        vCloseButton8 = new javax.swing.JButton();
        jPanel38 = new javax.swing.JPanel();
        OutputFolderText = new javax.swing.JTextField();
        jToggleButton42 = new javax.swing.JToggleButton();
        jToggleButton43 = new javax.swing.JToggleButton();
        ConnListText = new javax.swing.JTextField();
        jToggleButton44 = new javax.swing.JToggleButton();
        jToggleButton45 = new javax.swing.JToggleButton();
        jLabel135 = new javax.swing.JLabel();
        jLabel134 = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        Heatmappanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(30, 1, 1, 1), "FCM fitting", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 14), new java.awt.Color(0, 51, 204))); // NOI18N
        Heatmappanel.setToolTipText(null);
        Heatmappanel.setRequestFocusEnabled(false);
        Heatmappanel.setLayout(new java.awt.GridBagLayout());

        ExecButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/exec.png"))); // NOI18N
        ExecButton.setText("Execute");
        ExecButton.setToolTipText("Save the fitting plots exploiting the Cubic Spline.");
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
        Heatmappanel.add(ExecButton, gridBagConstraints);

        jButton48.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/reset.png"))); // NOI18N
        jButton48.setText("Reset");
        jButton48.setToolTipText("Settings reset.");
        jButton48.setMaximumSize(new java.awt.Dimension(100, 30));
        jButton48.setMinimumSize(new java.awt.Dimension(100, 30));
        jButton48.setPreferredSize(new java.awt.Dimension(100, 30));
        jButton48.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton48ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        Heatmappanel.add(jButton48, gridBagConstraints);

        vCloseButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/close.png"))); // NOI18N
        vCloseButton8.setText("Close");
        vCloseButton8.setToolTipText(null);
        vCloseButton8.setMaximumSize(new java.awt.Dimension(100, 30));
        vCloseButton8.setMinimumSize(new java.awt.Dimension(100, 30));
        vCloseButton8.setPreferredSize(new java.awt.Dimension(100, 30));
        vCloseButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vCloseButton8ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        Heatmappanel.add(vCloseButton8, gridBagConstraints);

        jPanel38.setBackground(new java.awt.Color(248, 248, 248));
        jPanel38.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Files:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 3, 14))); // NOI18N
        jPanel38.setToolTipText(null);
        jPanel38.setLayout(new java.awt.GridBagLayout());

        OutputFolderText.setEditable(false);
        OutputFolderText.setToolTipText("Output folder where the fitting plots exploiting the cubic spline will be saved.");
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
        jPanel38.add(OutputFolderText, gridBagConstraints);

        jToggleButton42.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/52b.png"))); // NOI18N
        jToggleButton42.setText("Browse");
        jToggleButton42.setToolTipText("Folder selection.");
        jToggleButton42.setMaximumSize(new java.awt.Dimension(110, 30));
        jToggleButton42.setMinimumSize(new java.awt.Dimension(110, 30));
        jToggleButton42.setPreferredSize(new java.awt.Dimension(110, 30));
        jToggleButton42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton42ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel38.add(jToggleButton42, gridBagConstraints);

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
        jPanel38.add(jToggleButton43, gridBagConstraints);

        ConnListText.setEditable(false);
        ConnListText.setToolTipText("RData storing the most probable clustered ConnectorList generated from the \"Best Cluster Extrapolation\" step.");
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
        jPanel38.add(ConnListText, gridBagConstraints);

        jToggleButton44.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/filebR.png"))); // NOI18N
        jToggleButton44.setText("Browser");
        jToggleButton44.setToolTipText("Selection of the RData storing the most probable clustered ConnectorList.");
        jToggleButton44.setMaximumSize(new java.awt.Dimension(110, 30));
        jToggleButton44.setMinimumSize(new java.awt.Dimension(110, 30));
        jToggleButton44.setPreferredSize(new java.awt.Dimension(110, 30));
        jToggleButton44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton44ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel38.add(jToggleButton44, gridBagConstraints);

        jToggleButton45.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/33b.png"))); // NOI18N
        jToggleButton45.setText("Cancel");
        jToggleButton45.setToolTipText(null);
        jToggleButton45.setMaximumSize(new java.awt.Dimension(110, 30));
        jToggleButton45.setMinimumSize(new java.awt.Dimension(110, 30));
        jToggleButton45.setPreferredSize(new java.awt.Dimension(110, 30));
        jToggleButton45.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton45ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel38.add(jToggleButton45, gridBagConstraints);

        jLabel135.setText("Output folder:");
        jLabel135.setToolTipText(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel38.add(jLabel135, gridBagConstraints);

        jLabel134.setText("Connector List clustered:");
        jLabel134.setToolTipText(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel38.add(jLabel134, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        Heatmappanel.add(jPanel38, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        add(Heatmappanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void ExecButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExecButtonActionPerformed

        
        

        if (ConnListText.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "You have to specified an input file","Error: Data  input file ",JOptionPane.ERROR_MESSAGE);
        }
        else
        if (OutputFolderText.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "You have to specified a output folder","Error: scratch folder ",JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            //execute code
            Runtime rt = Runtime.getRuntime();
            try{
            String[] cmd = {"/bin/bash","-c","  bash ./ExecFile/ExecClustVisual.sh "};
                        
            cmd[2]+= " input.file=\\\""+ConnListText.getText()+"\\\"";
            cmd[2]+= " output.folder=\\\""+OutputFolderText.getText()+"\\\"";
            cmd[2]+= " feature=\\\"ID\\\"";
            cmd[2]+= " mood=2";
            cmd[2]+=  " title=\\\""+"null"+"\\\" labels.x=\\\""+"null"+"\\\" labels.y=\\\""+"null"+ "\\\"";
            cmd[2]+=" "+ OutputFolderText.getText()+" >& "+OutputFolderText.getText()+"/outputExecution ";
           
           if (MainFrame.listProcRunning.size()<MainFrame.GS.getMaxSizelistProcRunning()){
                Process pr = rt.exec(cmd);
                MainFrame.ElProcRunning tmp= new MainFrame.ElProcRunning("FCM fitting ", OutputFolderText.getText(),pr,MainFrame.listModel.getSize());
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
                MainFrame.ElProcWaiting tmp= new MainFrame.ElProcWaiting("FCM fitting ", OutputFolderText.getText(),cmd,MainFrame.listModel.getSize());
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
        JOptionPane.showMessageDialog(this, "Saving the Spline fitting plots task was scheduled","Confermation",JOptionPane.INFORMATION_MESSAGE);
        //execute code     
           
        }

    }//GEN-LAST:event_ExecButtonActionPerformed

    private void jButton48ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton48ActionPerformed
        ConnListText.setText("");
        OutputFolderText.setText("");
    }//GEN-LAST:event_jButton48ActionPerformed

    private void vCloseButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vCloseButton8ActionPerformed
        ConnListText.setText("");
        OutputFolderText.setText("");
        CardLayout card = (CardLayout)MainFrame.MainPanel.getLayout();
        card.show(MainFrame.MainPanel, "Empty");
        MainFrame.CurrentLayout="Empty";
    }//GEN-LAST:event_vCloseButton8ActionPerformed

    private void ConnListTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConnListTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ConnListTextActionPerformed

    private void jToggleButton44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton44ActionPerformed
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
                MainFrame.CallingR(this, f, ConnListText, null , null, null, 2, outPath );                 
                OutputFolderText.setText(outPath);                
            } catch (IOException ex) {
                Logger.getLogger(ConsensusMatrix.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                    Logger.getLogger(ConsensusMatrix.class.getName()).log(Level.SEVERE, null, ex);
            }
            OutputFolderText.setText(openDir.getCurrentDirectory().getAbsolutePath());

        }
        MainFrame.getPreferences().put("open-dir",openDir.getCurrentDirectory().getAbsolutePath());
        
    }//GEN-LAST:event_jToggleButton44ActionPerformed

    private void jToggleButton45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton45ActionPerformed
        ConnListText.setText("");
    }//GEN-LAST:event_jToggleButton45ActionPerformed

    private void jToggleButton43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton43ActionPerformed
        OutputFolderText.setText("");
    }//GEN-LAST:event_jToggleButton43ActionPerformed

    private void jToggleButton42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton42ActionPerformed
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
        
    }//GEN-LAST:event_jToggleButton42ActionPerformed

    private void OutputFolderTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OutputFolderTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_OutputFolderTextActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField ConnListText;
    private javax.swing.JButton ExecButton;
    private javax.swing.JPanel Heatmappanel;
    private javax.swing.JTextField OutputFolderText;
    private javax.swing.ButtonGroup heatmapBaseGroup;
    private javax.swing.ButtonGroup heatmapGroup;
    private javax.swing.ButtonGroup heatmapGroupLog;
    private javax.swing.JButton jButton48;
    private javax.swing.JLabel jLabel134;
    private javax.swing.JLabel jLabel135;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JToggleButton jToggleButton42;
    private javax.swing.JToggleButton jToggleButton43;
    private javax.swing.JToggleButton jToggleButton44;
    private javax.swing.JToggleButton jToggleButton45;
    private javax.swing.JButton vCloseButton8;
    // End of variables declaration//GEN-END:variables
}
