/*
 * Distributed under the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.siyapath.client.ui;

import org.siyapath.client.TaskData;
import org.siyapath.client.UserHandler;
import org.siyapath.service.Job;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JobEditorGUI extends javax.swing.JDialog {

    private javax.swing.JLabel JobNameLabel;
    private javax.swing.JButton addTaskButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField inputDataText;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jobNameText;
    private javax.swing.JButton okButton;
    private javax.swing.JLabel replicaCountLabel;
    private javax.swing.JTextField replicaCountTextField;
    private javax.swing.JComboBox resourceCombo;
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
        resourceCombo.setSelectedItem("Medium");

        okButton.setEnabled(false);
        this.setLocationRelativeTo(null);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">
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
        jLabel3 = new javax.swing.JLabel();
        resourceCombo = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        inputDataText = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tasksList.setModel(listModel);
        jScrollPane1.setViewportView(tasksList);

        jobNameText.setText("SiyapathJob0001");

        JobNameLabel.setFont(new java.awt.Font("Bodoni MT", 0, 14));
        JobNameLabel.setText("Job Name");

        addTaskButton.setFont(new java.awt.Font("Bodoni MT", 0, 14));
        addTaskButton.setText("Add Task");
        addTaskButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addTaskButtonActionPerformed(evt);
            }
        });

        tasksLabel.setFont(new java.awt.Font("Bodoni MT", 0, 14));
        tasksLabel.setText("Tasks:");

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

        jLabel2.setText("Do you need to run the same task on multiple nodes?");

        jLabel3.setText("Required resource level:");

        resourceCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Low", "Medium", "High" }));

        jLabel4.setText("Input Data:");

        inputDataText.setText("1,10000");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
                                                .addContainerGap())
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(JobNameLabel)
                                                                .addGap(28, 28, 28)
                                                                .addComponent(jobNameText))
                                                        .addComponent(jLabel2)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(replicaCountLabel)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(replicaCountTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(168, 168, 168))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(tasksLabel)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 288, Short.MAX_VALUE)
                                                                .addComponent(addTaskButton))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cancelButton)))
                                                .addContainerGap())
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                                .addComponent(jLabel4)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(inputDataText, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                                .addComponent(jLabel3)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(resourceCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addContainerGap(166, Short.MAX_VALUE))))
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
                                        .addComponent(jLabel3)
                                        .addComponent(resourceCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(4, 4, 4)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(inputDataText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(tasksLabel)
                                        .addComponent(addTaskButton))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(okButton)
                                        .addComponent(cancelButton))
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>


    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {

        try {
            int replicaCount = Integer.parseInt(replicaCountTextField.getText());
            Job job = handler.createJob(taskList, replicaCount);
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
            String inputData = inputDataText.getText();
            listModel.addElement(taskName);
            taskList.put(taskName, new TaskData(taskName, sFile, inputData.getBytes(), resourceCombo.getSelectedItem().toString()));
        }
        if (!listModel.isEmpty()) {
            okButton.setEnabled(true);
        }
    }




}
