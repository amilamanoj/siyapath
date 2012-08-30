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

import org.apache.thrift.TException;
import org.siyapath.client.JobData;
import org.siyapath.client.SubmissionFailedException;
import org.siyapath.client.UserHandler;
import org.siyapath.service.Job;
import org.siyapath.service.TaskResult;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/*
 * The graphical user interface of siyapath client
 */
public class UserGUI extends JFrame {

    private UserHandler handler;
    private JobEditorGUI jobEditor;
    //    private String loggedPerson;
//    private Map taskFileList;
    private JobStatusUI jobStatusUI;
    private DefaultComboBoxModel comboBoxModel;
    private DefaultTableModel tableModel;
    private Vector<String> tableHeaders;
    private Map<String, Integer> jobMap;

    //    Map<Integer, String> taskCompletionDataMap;
    private Vector<Vector<String>> allRows = new Vector<Vector<String>>();
    private Vector<String> eachRow = null;

    /**
     * @param handler
     */
    public UserGUI(UserHandler handler) {
        this.handler = handler;
        this.jobEditor = new JobEditorGUI(this, true, this, handler);
        this.comboBoxModel = new DefaultComboBoxModel();
        this.jobMap = new HashMap<String, Integer>();
        tableHeaders = new Vector<String>();
        tableHeaders.add("Task ID");
        tableHeaders.add("Status");
        tableHeaders.add("Result");
        this.tableModel = new DefaultTableModel(getAllRows(), tableHeaders);
        initComponents();
        siyapathLogo.setIcon(new javax.swing.ImageIcon(this.getClass().getResource("/siyapathLogo232x184.png")));
        busyLabel.setIcon(new javax.swing.ImageIcon(this.getClass().getResource("/busyIcon.gif")));

//        jobSubmitButton.setEnabled(false);
        addJobButton.setVisible(false);
        loginPanel.setVisible(true);
        startPanel.setVisible(false);
        busyPanel.setVisible(false);
        statusPanel.setVisible(false);
        userMenu.setVisible(false);
//        logoutMenu.setVisible(false);
        this.setLocationRelativeTo(null);
    }

    public Vector<Vector<String>> getAllRows() {
        return allRows;
    }

    private void initComponents() {

        nameLabel = new javax.swing.JLabel();
        exitButton = new javax.swing.JButton();
        aboutButton = new javax.swing.JButton();
        controlPane = new javax.swing.JLayeredPane();
        statusPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jobIDLabel = new javax.swing.JLabel();
        jobNameLabel = new javax.swing.JLabel();
        taskDistributorLabel = new javax.swing.JLabel();
        taskCountLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        taskStatusTable = new javax.swing.JTable(tableModel);
        getStatusButton = new javax.swing.JButton();
//        getResultsButton = new javax.swing.JButton();
        startPanel = new javax.swing.JPanel();
        jobListLabel = new javax.swing.JLabel();
        jobInfoLabel = new javax.swing.JLabel();
        jobComboBox = new javax.swing.JComboBox(comboBoxModel);
        loginPanel = new javax.swing.JPanel();
        loginWelcomeLabel = new javax.swing.JLabel();
        loginInfoLabel = new javax.swing.JLabel();
        loginButton = new javax.swing.JButton();
        siyapathLogo = new javax.swing.JLabel();
        busyPanel = new javax.swing.JPanel();
        busyLabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        addJobButton = new javax.swing.JButton();
        mainMenuBar = new javax.swing.JMenuBar();
        systemMenu = new javax.swing.JMenu();
//        logoutMenu = new javax.swing.JMenuItem();
        exitMenu = new javax.swing.JMenuItem();
        userMenu = new javax.swing.JMenu();
        userProfileMenu = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutMenu = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Siyapath - Volunteer Computing");
        setBounds(new java.awt.Rectangle(0, 0, 700, 500));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        nameLabel.setFont(new java.awt.Font("Copperplate Gothic Light", 1, 24));
        nameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nameLabel.setText("Siyapath - Volunteer Computing");
        nameLabel.setPreferredSize(new java.awt.Dimension(800, 40));

        exitButton.setFont(new java.awt.Font("Bodoni MT", 0, 14));
        exitButton.setText("Exit");
        exitButton.setIconTextGap(10);
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        aboutButton.setFont(new java.awt.Font("Bodoni MT", 0, 14));
        aboutButton.setText("About");
        aboutButton.setIconTextGap(10);
        aboutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutButtonActionPerformed(evt);
            }
        });

        controlPane.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        controlPane.setOpaque(true);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12));
        jLabel1.setText("Job ID:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12));
        jLabel2.setText("Name");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12));
        jLabel3.setText("Task Distributer:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12));
        jLabel5.setText("No of tasks:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12));
        jLabel6.setText("Status:");

        jobIDLabel.setFont(new java.awt.Font("Tahoma", 0, 12));
        jobIDLabel.setText("0");

        jobNameLabel.setFont(new java.awt.Font("Tahoma", 0, 12));
        jobNameLabel.setText("-");

        taskDistributorLabel.setFont(new java.awt.Font("Tahoma", 0, 12));
        taskDistributorLabel.setText("-");

        taskCountLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        taskCountLabel.setText("0");

        jScrollPane1.setViewportView(taskStatusTable);

        getStatusButton.setFont(new java.awt.Font("Bodoni MT", 0, 14));
        getStatusButton.setText("Update Results");
        getStatusButton.setIconTextGap(10);
        getStatusButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getStatusButtonActionPerformed(evt);
            }
        });

//        getResultsButton.setFont(new java.awt.Font("Bodoni MT", 0, 14));
//        getResultsButton.setText("Retrieve Results");
//        getResultsButton.setIconTextGap(10);
//        getResultsButton.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                getResultsButtonActionPerformed(evt);
//            }
//        });

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
                statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(statusPanelLayout.createSequentialGroup()
                                .addGap(39, 39, 39)
                                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel6)
                                        .addGroup(statusPanelLayout.createSequentialGroup()
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(35, 35, 35)
                                                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                                                        .addComponent(getResultsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(getStatusButton, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(statusPanelLayout.createSequentialGroup()
                                                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel3)
                                                        .addComponent(jLabel2)
                                                        .addComponent(jLabel1)
                                                        .addComponent(jLabel5))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(taskDistributorLabel)
                                                        .addComponent(jobNameLabel)
                                                        .addComponent(jobIDLabel)
                                                        .addComponent(taskCountLabel))))
                                .addContainerGap(40, Short.MAX_VALUE))
        );
        statusPanelLayout.setVerticalGroup(
                statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusPanelLayout.createSequentialGroup()
                                .addGap(53, 53, 53)
                                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(statusPanelLayout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel2)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel5))
                                        .addGroup(statusPanelLayout.createSequentialGroup()
                                                .addComponent(jobIDLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jobNameLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(taskDistributorLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(taskCountLabel)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(statusPanelLayout.createSequentialGroup()
                                                .addComponent(getStatusButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                )
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(42, 42, 42))
        );

        statusPanel.setBounds(10, 40, 590, 310);
        controlPane.add(statusPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jobListLabel.setFont(new java.awt.Font("Tahoma", 0, 14));
        jobListLabel.setText("Jobs:");

        jobInfoLabel.setFont(new java.awt.Font("Tahoma", 0, 18));
        jobInfoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jobInfoLabel.setText("No Job defined, add a new Job below");

        jobComboBox.setFont(new java.awt.Font("Tahoma", 0, 14));
        jobComboBox.setModel(comboBoxModel);
        jobComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jobComboBoxActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout startPanelLayout = new javax.swing.GroupLayout(startPanel);
        startPanel.setLayout(startPanelLayout);
        startPanelLayout.setHorizontalGroup(
                startPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(startPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(startPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(startPanelLayout.createSequentialGroup()
                                                .addComponent(jobListLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jobComboBox, 0, 533, Short.MAX_VALUE)
                                                .addContainerGap())
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, startPanelLayout.createSequentialGroup()
                                                .addComponent(jobInfoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(119, 119, 119))))
        );
        startPanelLayout.setVerticalGroup(
                startPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(startPanelLayout.createSequentialGroup()
                                .addGroup(startPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jobComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                                        .addComponent(jobListLabel))
                                .addGap(108, 108, 108)
                                .addComponent(jobInfoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(126, 126, 126))
        );

        startPanel.setBounds(10, 10, 590, 340);
        controlPane.add(startPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        loginWelcomeLabel.setFont(new java.awt.Font("Copperplate Gothic Light", 1, 18));
        loginWelcomeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        loginWelcomeLabel.setText("Welcome to Siyapath");
        loginWelcomeLabel.setPreferredSize(new java.awt.Dimension(800, 40));

        loginInfoLabel.setFont(new java.awt.Font("Tahoma", 0, 14));
        loginInfoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        loginInfoLabel.setText("You are not connected. please login...");
        loginInfoLabel.setPreferredSize(new java.awt.Dimension(800, 40));

        loginButton.setText("Connect");
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });

        siyapathLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout loginPanelLayout = new javax.swing.GroupLayout(loginPanel);
        loginPanel.setLayout(loginPanelLayout);
        loginPanelLayout.setHorizontalGroup(
                loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(loginPanelLayout.createSequentialGroup()
                                .addGap(91, 91, 91)
                                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                        .addComponent(siyapathLogo)
                                        .addComponent(loginInfoLabel, 0, 0, Short.MAX_VALUE)
                                        .addComponent(loginWelcomeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
                                        .addComponent(loginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(93, 93, 93))
        );
        loginPanelLayout.setVerticalGroup(
                loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(loginPanelLayout.createSequentialGroup()
                                .addComponent(loginWelcomeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(siyapathLogo, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(loginInfoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(loginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        loginPanel.setBounds(10, 10, 590, 340);
        controlPane.add(loginPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        busyLabel.setFont(new java.awt.Font("Tahoma", 0, 18));
        busyLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        busyLabel.setText("Connecting...");
        busyLabel.setPreferredSize(new java.awt.Dimension(800, 40));

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout busyPanelLayout = new javax.swing.GroupLayout(busyPanel);
        busyPanel.setLayout(busyPanelLayout);
        busyPanelLayout.setHorizontalGroup(
                busyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(busyPanelLayout.createSequentialGroup()
                                .addGap(216, 216, 216)
                                .addGroup(busyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                        .addComponent(busyLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel4))
                                .addContainerGap(215, Short.MAX_VALUE))
        );
        busyPanelLayout.setVerticalGroup(
                busyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, busyPanelLayout.createSequentialGroup()
                                .addContainerGap(222, Short.MAX_VALUE)
                                .addComponent(busyLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addGap(85, 85, 85))
        );

        busyPanel.setBounds(10, 10, 590, 340);
        controlPane.add(busyPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        addJobButton.setFont(new java.awt.Font("Bodoni MT", 0, 14));
        addJobButton.setText("Add New Job...");
        addJobButton.setIconTextGap(10);
        addJobButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addJobButtonActionPerformed(evt);
            }
        });

        systemMenu.setText("System");

//        logoutMenu.setText("Logout");
//        logoutMenu.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                logoutMenuActionPerformed(evt);
//            }
//        });
//        systemMenu.add(logoutMenu);

        exitMenu.setText("Exit");
        exitMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuActionPerformed(evt);
            }
        });
        systemMenu.add(exitMenu);

        mainMenuBar.add(systemMenu);

        userMenu.setText("User");

        userProfileMenu.setText("My Profile");
        userProfileMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userProfileMenuActionPerformed(evt);
            }
        });
        userMenu.add(userProfileMenu);

        mainMenuBar.add(userMenu);

        helpMenu.setText("Help");

        aboutMenu.setText("About");
        aboutMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenu);

        mainMenuBar.add(helpMenu);

        setJMenuBar(mainMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(controlPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
                                        .addComponent(nameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addComponent(aboutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 172, Short.MAX_VALUE)
                                                .addComponent(addJobButton, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(exitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(nameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(controlPane, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(aboutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(exitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(addJobButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12))
        );

        pack();
    }// </editor-fold>

    private void exitButtonActionPerformed(ActionEvent evt) {
        exit();
    }

    private void aboutButtonActionPerformed(ActionEvent evt) {
        about();
    }

//    private void logoutMenuActionPerformed(ActionEvent evt) {
//        logOut();
//    }

    private void exitMenuActionPerformed(ActionEvent evt) {
        exit();
    }

    private void aboutMenuActionPerformed(ActionEvent evt) {
        about();
    }

    private void userProfileMenuActionPerformed(ActionEvent evt) {
        showUserProfile();
    }

    private void formWindowClosing(WindowEvent evt) {
        exit();
    }

    private void getStatusButtonActionPerformed(java.awt.event.ActionEvent evt) {
        StatusUiThread statusUiThread = new StatusUiThread(comboBoxModel.getSelectedItem().toString(), jobMap.get(((String) jobComboBox.getSelectedItem())));
        statusUiThread.start();
    }

    private void getResultsButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            handler.getJobResults(jobMap.get(((String) jobComboBox.getSelectedItem())));
        } catch (TException e) {
            e.printStackTrace();
        }
    }

    private void addJobButtonActionPerformed(java.awt.event.ActionEvent evt) {
        jobEditor.setVisible(true);
    }

//    private void JobStatusButtonActionPerformed(java.awt.event.ActionEvent evt) {
//        StatusUiThread statusUiThread = new StatusUiThread();
//        statusUiThread.run();
//    }

//    private void usernameTextKeyPressed(KeyEvent evt) {
//        int keyCode = evt.getKeyCode();
//        if (keyCode == 10) {
//            passwordText.requestFocus();
//            passwordText.selectAll();
//        }
//    }

//    private void passwordTextKeyPressed(KeyEvent evt) {
//        int keyCode = evt.getKeyCode();
//        if (keyCode == 10) {
//            login();
//        }
//    }

    private void loginButtonActionPerformed(ActionEvent evt) {
        new Thread(new LoginThread()).start();
    }


    private void about() {
        JOptionPane.showMessageDialog(this, "<html><a href=http://siyapath.org>(c) Siyapath Team 2012</a></html>", "About", JOptionPane.INFORMATION_MESSAGE);
    }

    private void exit() {
        if (JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "Exiting", 0) == 0) {
            System.exit(0);
        } else {
            setVisible(true);
        }
    }

    private class SubmitThread implements Runnable {
        private String name;
        private Job job;

        private SubmitThread(String name, Job job) {
            this.name = name;
            this.job = job;
        }

        @Override
        public void run() {
            try {
                int jobID = handler.submitJob(name, job);
                submissionSuccessful(jobID);
            } catch (SubmissionFailedException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(UserGUI.this, "Job submission failed. Try again later." + e.getCause(), "Job submission error", JOptionPane.ERROR_MESSAGE);
                if (comboBoxModel.getSize() == 0) {
                    logged();
                }
                jobEditor.setVisible(true);
            }
        }
    }

    void startSubmission(String name, Job job) {
        busy();
        SubmitThread submitThread = new SubmitThread(name, job);
        new Thread(submitThread).start();
    }


    void submissionSuccessful(int jobID) {
        JobData jobData = handler.getJobData(jobID);
        String jobName = jobData.getName();
        jobMap.put(jobName, Integer.valueOf(jobID));
        comboBoxModel.addElement(jobName);
        startPanel.setVisible(true);
        statusPanel.setVisible(true);
        busyLabel.setVisible(false);
        updateJobInfo(jobData);
    }

    void updateJobInfo(JobData jobData) {
        jobIDLabel.setText(String.valueOf(jobData.getId()));
        jobNameLabel.setText(jobData.getName());
        taskDistributorLabel.setText(jobData.getDistributorNode().toString());
        taskCountLabel.setText(String.valueOf(jobData.getJob().getTasksSize()));
    }

    private void jobComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        String jobName = (String) jobComboBox.getSelectedItem();
        JobData jobData = handler.getJobData(jobMap.get(jobName));
        updateJobInfo(jobData);
    }


    private class LoginThread implements Runnable {

        @Override
        public void run() {
            loginButton.setEnabled(false);
            login();
            loginButton.setEnabled(true);
        }
    }

    private void login() {
        busy();
//        String userName = usernameText.getText();
//        String password = String.valueOf(passwordText.getPassword());
        String res = handler.authenticate("admin", "admin");
        if (res.equals("connecEx")) {
            JOptionPane.showMessageDialog(this, "Could not connect to bootstrapper", "Login Failed", JOptionPane.ERROR_MESSAGE);
            loggedOut();
        } else if (res.equals("success")) {
//            loggedPerson = userName;
            logged();
        } else {
            loggedOut();
            JOptionPane.showMessageDialog(this, "Invalid username or password, access denied!", "Login Failed", JOptionPane.ERROR_MESSAGE);
//            usernameText.requestFocus();
//            usernameText.selectAll();
        }
    }

//    private void logOut() {
//        if (JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logging out", 0) == 0) {
////            loggedPerson = null;
//            loggedOut();
//        }
//    }

    public void logged() {
        loginPanel.setVisible(false);
        busyPanel.setVisible(false);
        startPanel.setVisible(true);
        statusPanel.setVisible(false);
        userMenu.setVisible(true);
//        logoutMenu.setVisible(true);
        addJobButton.setVisible(true);
//        userWelcomeLabel.setText("Welcome " + loggedPerson);
    }

    public void busy() {
        busyPanel.setVisible(true);
        startPanel.setVisible(false);
        loginPanel.setVisible(false);
//        userPanel.setVisible(false);
        userMenu.setVisible(false);
//        logoutMenu.setVisible(false);
    }

    public void loggedOut() {
//        usernameText.setText(null);
//        passwordText.setText(null);
        busyPanel.setVisible(false);
        loginPanel.setVisible(true);
//        userPanel.setVisible(false);
        userMenu.setVisible(false);
//        logoutMenu.setVisible(false);
    }

    private void showUserProfile() {
        JOptionPane.showMessageDialog(this, "User Info", "User Profile", JOptionPane.INFORMATION_MESSAGE);
    }

//    private void showSplash() {
//        SplashScreen ss = new SplashScreen(this, true);
//        ss.setVisible(true);
//    }

    private class StatusUiThread extends Thread {

        int jobId;

        private StatusUiThread(String name, int jobId) {
            super(name);
            this.jobId = jobId;
        }

        @Override
        public void run() {
            Map<Integer, TaskResult> statusMap = null;
            try {
                statusMap = handler.pollStatusFromJobProcessor(jobId);
            } catch (TException e) {
                e.printStackTrace();
            }
            if (statusMap != null) {
                updateTableDataVectors(statusMap);
                DefaultTableModel model = (DefaultTableModel) taskStatusTable.getModel();
                model.fireTableDataChanged();
//            jobStatus = assessJobStatusFromTaskStatuses(taskCompletionDataMap);
            }

            if (handler.assessJobStatusFromTaskStatuses(statusMap)){
                getStatusButton.setEnabled(false);
            }
        }

        private void updateTableDataVectors(Map<Integer, TaskResult> statusMap) {

            if (!allRows.isEmpty()) {
                allRows.removeAllElements();
            }
            for (Map.Entry<Integer, TaskResult> task : statusMap.entrySet()) {
                eachRow = new Vector<String>();
                eachRow.add(task.getKey().toString());
                eachRow.add(task.getValue().getStatus().toString());
                eachRow.add(new String(task.getValue().getResults()));
                allRows.add(eachRow);
            }
        }

    }

    private javax.swing.JButton aboutButton;
    private javax.swing.JMenuItem aboutMenu;
    private javax.swing.JButton addJobButton;
    private javax.swing.JLabel busyLabel;
    private javax.swing.JPanel busyPanel;
    private javax.swing.JLayeredPane controlPane;
    //    private javax.swing.JButton createEditJobButton;
    private javax.swing.JButton exitButton;
    private javax.swing.JMenuItem exitMenu;
//    private javax.swing.JButton getResultsButton;
    private javax.swing.JButton getStatusButton;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel taskCountLabel;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox jobComboBox;
    private javax.swing.JLabel jobIDLabel;
    private javax.swing.JLabel jobInfoLabel;
    private javax.swing.JLabel jobListLabel;
    private javax.swing.JLabel jobNameLabel;
    private javax.swing.JButton loginButton;
    private javax.swing.JLabel loginInfoLabel;
    private javax.swing.JPanel loginPanel;
    private javax.swing.JLabel loginWelcomeLabel;
    //    private javax.swing.JMenuItem logoutMenu;
    private javax.swing.JMenuBar mainMenuBar;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JLabel siyapathLogo;
    private javax.swing.JPanel startPanel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JMenu systemMenu;
    private javax.swing.JLabel taskDistributorLabel;
    public javax.swing.JTable taskStatusTable;
    private javax.swing.JMenu userMenu;
    private javax.swing.JMenuItem userProfileMenu;


}
