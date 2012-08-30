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

package org.siyapath.ui;

import javax.swing.*;
import java.awt.event.*;

/*
* The graphical user interface of siyapath node
*/
public class NodeGUI extends JFrame {

    public NodeGUI() {
        initComponents();

        gossipProgressLabel.setIcon(new javax.swing.ImageIcon(this.getClass().getResource("/gossip_active.gif")));
        siyapathLogo.setIcon(new javax.swing.ImageIcon(this.getClass().getResource("/siyapathLogo100x80.png"))); // NOI18N
//        busyTimer = new Timer(1000, new ActionListener() {
//
//            public void actionPerformed(ActionEvent e) {
//                busyTimerActionPerformed(e);
//            }
//        });
        userMenu.setVisible(false);
        gossipLabel.setVisible(false);
        gossipProgressLabel.setVisible(false);
        this.setLocationRelativeTo(null);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        nameLabel = new javax.swing.JLabel();
        exitButton = new javax.swing.JButton();
        aboutButton = new javax.swing.JButton();
        controlPane = new javax.swing.JLayeredPane();
        infoPanel = new javax.swing.JPanel();
        listenerLabel = new javax.swing.JLabel();
        listenerStatLabel = new javax.swing.JLabel();
        workerLabel = new javax.swing.JLabel();
        workerStatLabel = new javax.swing.JLabel();
        membersLabel = new javax.swing.JLabel();
        membersLabel1 = new javax.swing.JLabel();
        mCountLabel = new javax.swing.JLabel();
        mLabel = new javax.swing.JLabel();
        gossipLabel = new javax.swing.JLabel();
        gossipProgressLabel = new javax.swing.JLabel();
        stateLabel1 = new javax.swing.JLabel();
        processingInfoLabel = new javax.swing.JLabel();
        label3 = new javax.swing.JLabel();
        totalTasks = new javax.swing.JLabel();
        stateLabel = new javax.swing.JLabel();
        siyapathLogo = new javax.swing.JLabel();
        mainMenuBar = new javax.swing.JMenuBar();
        systemMenu = new javax.swing.JMenu();
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

        listenerLabel.setFont(new java.awt.Font("Tahoma", 0, 12));
        listenerLabel.setText("Peer Listner:");

        listenerStatLabel.setFont(new java.awt.Font("BankGothic Lt BT", 0, 18));
        listenerStatLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        listenerStatLabel.setText("OFF");

        workerLabel.setFont(new java.awt.Font("Tahoma", 0, 12));
        workerLabel.setText("Peer Worker:");

        workerStatLabel.setFont(new java.awt.Font("BankGothic Lt BT", 0, 18));
        workerStatLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        workerStatLabel.setText("OFF");

        membersLabel.setFont(new java.awt.Font("Tahoma", 0, 12));
        membersLabel.setText("Member count:");

        membersLabel1.setFont(new java.awt.Font("Tahoma", 0, 12));
        membersLabel1.setText("Members:");

        mCountLabel.setFont(new java.awt.Font("Tahoma", 0, 18));
        mCountLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        mCountLabel.setText("0");

        mLabel.setFont(new java.awt.Font("Tahoma", 0, 12));
        mLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        mLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        gossipLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        gossipLabel.setText("Gossip in progress...");

        stateLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        stateLabel1.setText("Waiting for work...");

        processingInfoLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        label3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        label3.setText("Total tasks processed:");

        totalTasks.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        totalTasks.setText("0");

        javax.swing.GroupLayout infoPanelLayout = new javax.swing.GroupLayout(infoPanel);
        infoPanel.setLayout(infoPanelLayout);
        infoPanelLayout.setHorizontalGroup(
                infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(infoPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(membersLabel1)
                                        .addGroup(infoPanelLayout.createSequentialGroup()
                                                .addGap(10, 10, 10)
                                                .addComponent(mLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(infoPanelLayout.createSequentialGroup()
                                                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(listenerLabel)
                                                        .addComponent(membersLabel))
                                                .addGap(28, 28, 28)
                                                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(mCountLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(listenerStatLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(infoPanelLayout.createSequentialGroup()
                                                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(gossipLabel)
                                                        .addComponent(workerLabel))
                                                .addGap(57, 57, 57)
                                                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(gossipProgressLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(workerStatLabel)))
                                        .addComponent(stateLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(infoPanelLayout.createSequentialGroup()
                                                .addComponent(label3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(totalTasks, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(processingInfoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE))
                                .addContainerGap())
        );
        infoPanelLayout.setVerticalGroup(
                infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(infoPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(listenerLabel)
                                        .addComponent(listenerStatLabel)
                                        .addComponent(workerStatLabel)
                                        .addComponent(workerLabel))
                                .addGap(18, 18, 18)
                                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(membersLabel)
                                        .addComponent(mCountLabel)
                                        .addComponent(gossipProgressLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(gossipLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(stateLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(membersLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(mLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, infoPanelLayout.createSequentialGroup()
                                                .addComponent(processingInfoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(label3)
                                                        .addComponent(totalTasks, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap())
        );

        infoPanel.setBounds(10, 20, 590, 280);
        controlPane.add(infoPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        stateLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        stateLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        stateLabel.setText("----");
        stateLabel.setBounds(200, 300, 190, 25);
        controlPane.add(stateLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        systemMenu.setText("System");

        exitMenu.setText("Exit");
        exitMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuActionPerformed(evt);
            }
        });
        systemMenu.add(exitMenu);

        mainMenuBar.add(systemMenu);

        userMenu.setText("Volunteer");

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
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addComponent(siyapathLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(nameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addComponent(aboutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 322, Short.MAX_VALUE)
                                                .addComponent(exitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(siyapathLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(11, 11, 11)
                                                .addComponent(nameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(controlPane, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(aboutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(exitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>

    private void exitButtonActionPerformed(ActionEvent evt) {
        exit();
    }

    private void aboutButtonActionPerformed(ActionEvent evt) {
        about();
    }

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

//    private void busyTimerActionPerformed(ActionEvent e) {
//        busyPanel.setVisible(false);
//        controlPane.setVisible(true);
//        busyTimer.stop();
//    }

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


    private void showUserProfile() {
        JOptionPane.showMessageDialog(this, "User Info", "User Profile", JOptionPane.INFORMATION_MESSAGE);
    }

    private javax.swing.JButton aboutButton;
    private javax.swing.JMenuItem aboutMenu;
    private javax.swing.JLayeredPane controlPane;
    private javax.swing.JButton exitButton;
    private javax.swing.JMenuItem exitMenu;
    private javax.swing.JLabel gossipLabel;
    private javax.swing.JLabel gossipProgressLabel;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JPanel infoPanel;
    private javax.swing.JLabel label3;
    private javax.swing.JLabel listenerLabel;
    private javax.swing.JLabel listenerStatLabel;
    private javax.swing.JLabel mCountLabel;
    private javax.swing.JLabel mLabel;
    private javax.swing.JMenuBar mainMenuBar;
    private javax.swing.JLabel membersLabel;
    private javax.swing.JLabel membersLabel1;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JLabel processingInfoLabel;
    private javax.swing.JLabel siyapathLogo;
    private javax.swing.JLabel stateLabel;
    private javax.swing.JLabel stateLabel1;
    private javax.swing.JMenu systemMenu;
    private javax.swing.JLabel totalTasks;
    private javax.swing.JMenu userMenu;
    private javax.swing.JMenuItem userProfileMenu;
    private javax.swing.JLabel workerLabel;
    private javax.swing.JLabel workerStatLabel;


    public void setListenerStat(String stat) {
        listenerStatLabel.setText(stat);
    }

    public void setWorkerStat(String stat) {
        workerStatLabel.setText(stat);
        if (stat.equalsIgnoreCase("on")) {
            gossipLabel.setVisible(true);
            gossipProgressLabel.setVisible(true);
        } else {
            gossipLabel.setVisible(false);
            gossipProgressLabel.setVisible(false);
        }
    }

    public void setMemberCount(String memberCount) {
        mCountLabel.setText(memberCount);
    }

    public void setMembers(String members) {
        mLabel.setText(members);
    }

    public void setNodeStatus(String status) {
        stateLabel.setText(status);
    }

    public void setProcessStatus(String status) {
        stateLabel1.setText(status);
    }

    public void setProcessingInfo(String status) {
        processingInfoLabel.setText(status);
    }

    public void setTotalTasks(String total) {
        totalTasks.setText(total);
    }


}
