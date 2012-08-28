package org.siyapath.client;

import org.siyapath.service.Job;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
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

    private javax.swing.JLabel JobNameLabel;
    private javax.swing.JButton addTaskButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jobNameText;
    private javax.swing.JButton okButton;
    private javax.swing.JLabel replicaCountLabel;
    private javax.swing.JTextField replicaCountTextField;
    private javax.swing.JLabel tasksLabel;
    private javax.swing.JList tasksList;

    private DefaultListModel listModel;
    private Map<String, TaskData> taskList = new HashMap<String, TaskData>();
    private UserGUI userGUI;
    private UserHandler handler;
    private int counter;

    /**
     * Creates new form JobEditor
     */
    public JobEditorGUI(java.awt.Frame parent, boolean modal, UserGUI userGUI, UserHandler handler) {
        super(parent, modal);
        this.listModel = new DefaultListModel();
        this.userGUI = userGUI;
        this.handler = handler;
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
        replicaCountLabel = new javax.swing.JLabel();
        replicaCountTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

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

        cancelButton.setFont(new java.awt.Font("Bodoni MT", 0, 14));
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        okButton.setFont(new java.awt.Font("Bodoni MT", 0, 14));
        okButton.setText("Submit");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        replicaCountLabel.setText("No. of replicas to run :");

        replicaCountTextField.setText("1");
//        replicaCountTextField.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                replicaCountTextFieldActionPerformed(evt);
//            }
//        });

        jLabel2.setText("Do you need to run the same task on multiple nodes?");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(tasksLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 292, Short.MAX_VALUE)
                                                .addComponent(addTaskButton))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(JobNameLabel)
                                                .addGap(28, 28, 28)
                                                .addComponent(jobNameText, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cancelButton))
                                        .addComponent(jLabel2)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(replicaCountLabel)
                                                .addGap(102, 102, 102)
                                                .addComponent(replicaCountTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(JobNameLabel)
                                        .addComponent(jobNameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(replicaCountLabel)
                                        .addComponent(replicaCountTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(tasksLabel)
                                        .addComponent(addTaskButton))
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(cancelButton)
                                        .addComponent(okButton))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {

        try {
            int replicaCount = Integer.parseInt(replicaCountTextField.getText());
            Job job = handler.createJob(taskList,replicaCount);
            userGUI.startSubmission(jobNameText.getText(), job);
            this.setVisible(false);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Could not read task files, please verify." + e.getCause().getMessage(), "Job Creation error", JOptionPane.ERROR_MESSAGE);

        }
//            JOptionPane.showMessageDialog(this, "Job submission failed: " + e.getCause().getMessage(), "Job Submission Failed", JOptionPane.ERROR_MESSAGE);
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
            taskList.put(taskName, new TaskData(taskName, sFile, "0,200000".getBytes(), "Medium"));  //TODO: update GUI
        }
        if (!listModel.isEmpty()) {
            okButton.setEnabled(true);
        }
    }


}
