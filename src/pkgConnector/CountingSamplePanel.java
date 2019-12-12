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
import javax.swing.table.DefaultTableModel;
import static pkgConnector.MainFrame.getPreferences;
/**
 *
 * @author user
 */
public class CountingSamplePanel extends javax.swing.JPanel {

 
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
     
     
    public CountingSamplePanel() {
        initComponents();
    }
    
    private static final long serialVersionUID = 57756123321L;
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        SCountPanel = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        ConnListText = new javax.swing.JTextField();
        Cinbrowes = new javax.swing.JButton();
        Cincancel = new javax.swing.JButton();
        jLabel98 = new javax.swing.JLabel();
        jLabel97 = new javax.swing.JLabel();
        OutputFolderText = new javax.swing.JTextField();
        Coutbrowes = new javax.swing.JButton();
        Coutcancel = new javax.swing.JButton();
        ExecButton = new javax.swing.JButton();
        CResetButton1 = new javax.swing.JButton();
        CCloseButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        ComboFeatBox = new javax.swing.JComboBox<>();
        jLabel99 = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        SCountPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(30, 1, 1, 1), "Sample Counting", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 14), new java.awt.Color(0, 51, 204))); // NOI18N
        SCountPanel.setLayout(new java.awt.GridBagLayout());

        jPanel27.setBackground(new java.awt.Color(248, 248, 248));
        jPanel27.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Files:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 3, 14))); // NOI18N
        jPanel27.setToolTipText(null);
        jPanel27.setLayout(new java.awt.GridBagLayout());

        ConnListText.setEditable(false);
        ConnListText.setToolTipText("RData storing the most probable clustered ConnectorList generated from the \"Best Cluster Extrapolation\" step.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 218;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.ABOVE_BASELINE;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel27.add(ConnListText, gridBagConstraints);

        Cinbrowes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/52b.png"))); // NOI18N
        Cinbrowes.setText("Browser");
        Cinbrowes.setToolTipText("Folder selection.");
        Cinbrowes.setMaximumSize(new java.awt.Dimension(110, 30));
        Cinbrowes.setMinimumSize(new java.awt.Dimension(110, 30));
        Cinbrowes.setPreferredSize(new java.awt.Dimension(110, 30));
        Cinbrowes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CinbrowesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.ABOVE_BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel27.add(Cinbrowes, gridBagConstraints);

        Cincancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/33b.png"))); // NOI18N
        Cincancel.setText("Cancel");
        Cincancel.setMaximumSize(new java.awt.Dimension(110, 30));
        Cincancel.setMinimumSize(new java.awt.Dimension(110, 30));
        Cincancel.setPreferredSize(new java.awt.Dimension(110, 30));
        Cincancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CincancelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.ABOVE_BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel27.add(Cincancel, gridBagConstraints);

        jLabel98.setText("Output Folder:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.ABOVE_BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel27.add(jLabel98, gridBagConstraints);

        jLabel97.setText("Connector List clustered:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.ABOVE_BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel27.add(jLabel97, gridBagConstraints);

        OutputFolderText.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 218;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.ABOVE_BASELINE;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel27.add(OutputFolderText, gridBagConstraints);

        Coutbrowes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/filebR.png"))); // NOI18N
        Coutbrowes.setText("Browser");
        Coutbrowes.setToolTipText("");
        Coutbrowes.setMaximumSize(new java.awt.Dimension(110, 30));
        Coutbrowes.setMinimumSize(new java.awt.Dimension(110, 30));
        Coutbrowes.setPreferredSize(new java.awt.Dimension(110, 30));
        Coutbrowes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CoutbrowesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.ABOVE_BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel27.add(Coutbrowes, gridBagConstraints);

        Coutcancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/33b.png"))); // NOI18N
        Coutcancel.setText("Cancel");
        Coutcancel.setMaximumSize(new java.awt.Dimension(110, 30));
        Coutcancel.setMinimumSize(new java.awt.Dimension(110, 30));
        Coutcancel.setPreferredSize(new java.awt.Dimension(110, 30));
        Coutcancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CoutcancelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.ABOVE_BASELINE_TRAILING;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel27.add(Coutcancel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        SCountPanel.add(jPanel27, gridBagConstraints);

        ExecButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/exec.png"))); // NOI18N
        ExecButton.setText("Execute");
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
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        SCountPanel.add(ExecButton, gridBagConstraints);

        CResetButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/reset.png"))); // NOI18N
        CResetButton1.setText("Reset");
        CResetButton1.setToolTipText("Settings reset.");
        CResetButton1.setMaximumSize(new java.awt.Dimension(100, 30));
        CResetButton1.setMinimumSize(new java.awt.Dimension(100, 30));
        CResetButton1.setPreferredSize(new java.awt.Dimension(100, 30));
        CResetButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CResetButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        SCountPanel.add(CResetButton1, gridBagConstraints);

        CCloseButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pkgConnector/images/close.png"))); // NOI18N
        CCloseButton1.setText("Close");
        CCloseButton1.setToolTipText("");
        CCloseButton1.setMaximumSize(new java.awt.Dimension(100, 30));
        CCloseButton1.setMinimumSize(new java.awt.Dimension(100, 30));
        CCloseButton1.setPreferredSize(new java.awt.Dimension(100, 30));
        CCloseButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CCloseButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        SCountPanel.add(CCloseButton1, gridBagConstraints);

        jPanel1.setBackground(new java.awt.Color(248, 248, 248));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Parameter:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 3, 14))); // NOI18N
        jPanel1.setLayout(new java.awt.GridBagLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 250;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(ComboFeatBox, gridBagConstraints);

        jLabel99.setText("Feature:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 124);
        jPanel1.add(jLabel99, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        SCountPanel.add(jPanel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(SCountPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void CinbrowesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CinbrowesActionPerformed
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

    }//GEN-LAST:event_CinbrowesActionPerformed

    private void CincancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CincancelActionPerformed
        ConnListText.setText("");
    }//GEN-LAST:event_CincancelActionPerformed

    private void CoutbrowesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CoutbrowesActionPerformed
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
                MainFrame.ClusteredDataCheck(this, f, ConnListText, 2 ); 
                MainFrame.UpdateComboBox(f, ComboFeatBox, 1);
                OutputFolderText.setText(openDir.getCurrentDirectory().getAbsolutePath());
            } catch (IOException ex) {
                Logger.getLogger(ConsensusMatrix.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        getPreferences().put("open-dir",openDir.getCurrentDirectory().getAbsolutePath());
        
    }//GEN-LAST:event_CoutbrowesActionPerformed

    private void CoutcancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CoutcancelActionPerformed
        OutputFolderText.setText("");
    }//GEN-LAST:event_CoutcancelActionPerformed

    private void ExecButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExecButtonActionPerformed

        if (ConnListText.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "You have to specified the count file","Error: Input file",JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (OutputFolderText.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "You have to specified an output folders","Error: Output folder",JOptionPane.ERROR_MESSAGE);
            return;
        }
        File f =new File(ConnListText.getText());

        //execute code
        Runtime rt = Runtime.getRuntime();
        try{
            String[] cmd = {"/bin/bash","-c"," bash ./ExecFile/ExecCount.sh "};
            cmd[2]+= " input.file=\\\""+ConnListText.getText()+"\\\"";
            cmd[2]+= " output.folder=\\\""+OutputFolderText.getText()+"\\\"";
            cmd[2]+= " feature=\\\""+ComboFeatBox.getItemAt(ComboFeatBox.getSelectedIndex())+"\\\"";
            cmd[2]+=" "+OutputFolderText.getText() +" >& "+OutputFolderText.getText()+"/outputExecution ";            
//ProcessStatus.setText(pr.toString());
            
            if (MainFrame.listProcRunning.size()<MainFrame.GS.getMaxSizelistProcRunning()){
                Process pr = rt.exec(cmd);
                MainFrame.ElProcRunning tmp= new MainFrame.ElProcRunning("Counting ", OutputFolderText.getText(),pr,MainFrame.listModel.getSize());
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
                MainFrame.ElProcWaiting tmp= new MainFrame.ElProcWaiting("Counting ", OutputFolderText.getText(),cmd,MainFrame.listModel.getSize());
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
        JOptionPane.showMessageDialog(this, "Consensus Matrix extrapolation task was scheduled","Confermation",JOptionPane.INFORMATION_MESSAGE);
        //execute code
            
    }//GEN-LAST:event_ExecButtonActionPerformed

    private void CResetButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CResetButton1ActionPerformed
        ConnListText.setText("");
        OutputFolderText.setText("");
    }//GEN-LAST:event_CResetButton1ActionPerformed

    private void CCloseButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CCloseButton1ActionPerformed
        ConnListText.setText("");
        OutputFolderText.setText("");
        //RESET FIELDS
        CardLayout card = (CardLayout)MainFrame.MainPanel.getLayout();
        card.show(MainFrame.MainPanel, "Empty");
        MainFrame.CurrentLayout="Empty";
        // AnalysisTree.clearSelection();
    }//GEN-LAST:event_CCloseButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CCloseButton1;
    private javax.swing.JButton CResetButton1;
    private javax.swing.JButton Cinbrowes;
    private javax.swing.JButton Cincancel;
    private javax.swing.JComboBox<String> ComboFeatBox;
    private javax.swing.JTextField ConnListText;
    private javax.swing.JButton Coutbrowes;
    private javax.swing.JButton Coutcancel;
    private javax.swing.JButton ExecButton;
    private javax.swing.JTextField OutputFolderText;
    private javax.swing.JPanel SCountPanel;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel27;
    // End of variables declaration//GEN-END:variables
}
