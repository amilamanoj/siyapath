package org.siyapath.client;

import org.siyapath.service.Task;

import javax.swing.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JobEditor.java
 *
 * Created on Jun 17, 2012, 4:05:29 PM
 */

/**
 * @author Amila Manoj
 */
public class JobEditorGUI extends javax.swing.JDialog {

    private DefaultListModel listModel;
    private Map<String, TaskData> taskFileList = new HashMap<String, TaskData>();
    private UserGUI userGUI;
    private int counter;


    /**
     * Creates new form JobEditor
     */
    public JobEditorGUI(java.awt.Frame parent, boolean modal, UserGUI userGUI) {
        super(parent, modal);
        this.listModel = new DefaultListModel();
        this.userGUI = userGUI;
        initComponents();
        okButton.setEnabled(false);
        this.setLocationRelativeTo(null);
    }

    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tasksList = new javax.swing.JList();
        jobNameText = new javax.swing.JTextField();
        JobNameLabel = new javax.swing.JLabel();
        addTaskButton = new javax.swing.JButton();
        tasksLabel = new javax.swing.JLabel();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        tasksList.setModel(listModel);
        jScrollPane1.setViewportView(tasksList);

        jobNameText.setText("SiyapathJob0001");

        JobNameLabel.setFont(new java.awt.Font("Bodoni MT", 0, 14)); // NOI18N
        JobNameLabel.setText("Job Name");

        addTaskButton.setFont(new java.awt.Font("Bodoni MT", 0, 14)); // NOI18N
        addTaskButton.setText("Add Task");
        addTaskButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addTaskButtonActionPerformed(evt);
            }
        });

        tasksLabel.setFont(new java.awt.Font("Bodoni MT", 0, 14)); // NOI18N
        tasksLabel.setText("Tasks");

        cancelButton.setFont(new java.awt.Font("Bodoni MT", 0, 14)); // NOI18N
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        okButton.setFont(new java.awt.Font("Bodoni MT", 0, 14)); // NOI18N
        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
                                        .addComponent(jobNameText, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(tasksLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 224, Short.MAX_VALUE)
                                                .addComponent(addTaskButton))
                                        .addComponent(JobNameLabel)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(okButton)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cancelButton)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(JobNameLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jobNameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(tasksLabel)
                                        .addComponent(addTaskButton))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(cancelButton)
                                        .addComponent(okButton))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
        userGUI.jobUpdated(jobNameText.getText(), taskFileList);
        this.dispose();
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }

    private void addTaskButtonActionPerformed(java.awt.event.ActionEvent evt) {
        final JFileChooser fc = new JFileChooser(".");
        fc.showOpenDialog(this);
        File sFile = fc.getSelectedFile();
        if (sFile != null) {
            String taskName = "Task-" + counter++ + ": " + sFile.getName();
            listModel.addElement(taskName);
            taskFileList.put(taskName, new TaskData(taskName, sFile, "0,200000"));  //TODO: update GUI
        }
        if (!listModel.isEmpty()) {
            okButton.setEnabled(true);
        }
    }

    private javax.swing.JLabel JobNameLabel;
    private javax.swing.JButton addTaskButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jobNameText;
    private javax.swing.JButton okButton;
    private javax.swing.JLabel tasksLabel;
    private javax.swing.JList tasksList;
}
