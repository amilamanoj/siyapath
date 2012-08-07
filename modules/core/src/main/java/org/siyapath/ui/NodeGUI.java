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
        taskPanel.setVisible(false);
        userMenu.setVisible(false);
        gossipLabel.setVisible(false);
        gossipProgressLabel.setVisible(false);
        this.setLocationRelativeTo(null);
    }

    private void initComponents() {

        nameLabel = new javax.swing.JLabel();
        exitButton = new javax.swing.JButton();
        aboutButton = new javax.swing.JButton();
        controlPane = new javax.swing.JLayeredPane();
        taskPanel = new javax.swing.JPanel();
        stateLabel1 = new javax.swing.JLabel();
        gossipPanel = new javax.swing.JPanel();
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
        taskButton = new javax.swing.JLabel();
        gossipButton = new javax.swing.JLabel();
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

        exitButton.setFont(new java.awt.Font("Bodoni MT", 0, 14)); // NOI18N
        exitButton.setText("Exit");
        exitButton.setIconTextGap(10);
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        aboutButton.setFont(new java.awt.Font("Bodoni MT", 0, 14)); // NOI18N
        aboutButton.setText("About");
        aboutButton.setIconTextGap(10);
        aboutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutButtonActionPerformed(evt);
            }
        });

        controlPane.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        controlPane.setOpaque(true);

        stateLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        stateLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        stateLabel1.setText("Waiting for a task...");

        javax.swing.GroupLayout taskPanelLayout = new javax.swing.GroupLayout(taskPanel);
        taskPanel.setLayout(taskPanelLayout);
        taskPanelLayout.setHorizontalGroup(
                taskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(taskPanelLayout.createSequentialGroup()
                                .addGap(193, 193, 193)
                                .addComponent(stateLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(201, Short.MAX_VALUE))
        );
        taskPanelLayout.setVerticalGroup(
                taskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(taskPanelLayout.createSequentialGroup()
                                .addGap(107, 107, 107)
                                .addComponent(stateLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(108, Short.MAX_VALUE))
        );

        taskPanel.setBounds(10, 60, 590, 240);
        controlPane.add(taskPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        listenerLabel.setFont(new java.awt.Font("Tahoma", 0, 12));
        listenerLabel.setText("Peer Listner:");

        listenerStatLabel.setFont(new java.awt.Font("BankGothic Lt BT", 0, 18)); // NOI18N
        listenerStatLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        listenerStatLabel.setText("OFF");

        workerLabel.setFont(new java.awt.Font("Tahoma", 0, 12));
        workerLabel.setText("Peer Worker:");

        workerStatLabel.setFont(new java.awt.Font("BankGothic Lt BT", 0, 18)); // NOI18N
        workerStatLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        workerStatLabel.setText("OFF");

        membersLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        membersLabel.setText("Member count:");

        membersLabel1.setFont(new java.awt.Font("Tahoma", 0, 12));
        membersLabel1.setText("Members:");

        mCountLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        mCountLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        mCountLabel.setText("0");

        mLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        mLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        mLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        gossipLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        gossipLabel.setText("Gossip in progress...");

        javax.swing.GroupLayout gossipPanelLayout = new javax.swing.GroupLayout(gossipPanel);
        gossipPanel.setLayout(gossipPanelLayout);
        gossipPanelLayout.setHorizontalGroup(
                gossipPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(gossipPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(gossipPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(membersLabel1)
                                        .addGroup(gossipPanelLayout.createSequentialGroup()
                                                .addGroup(gossipPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(listenerLabel)
                                                        .addComponent(membersLabel))
                                                .addGap(28, 28, 28)
                                                .addGroup(gossipPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(mCountLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(listenerStatLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGap(143, 143, 143)
                                                .addGroup(gossipPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(workerLabel)
                                                        .addComponent(gossipLabel))
                                                .addGap(45, 45, 45)
                                                .addGroup(gossipPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(gossipProgressLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(workerStatLabel)))
                                        .addGroup(gossipPanelLayout.createSequentialGroup()
                                                .addGap(10, 10, 10)
                                                .addComponent(mLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(97, Short.MAX_VALUE))
        );
        gossipPanelLayout.setVerticalGroup(
                gossipPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(gossipPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(gossipPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(listenerLabel)
                                        .addComponent(listenerStatLabel)
                                        .addComponent(workerStatLabel)
                                        .addComponent(workerLabel))
                                .addGap(18, 18, 18)
                                .addGroup(gossipPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(membersLabel)
                                        .addComponent(mCountLabel)
                                        .addComponent(gossipLabel)
                                        .addComponent(gossipProgressLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(membersLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                                .addContainerGap())
        );

        gossipPanel.setBounds(10, 60, 590, 240);
        controlPane.add(gossipPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        taskButton.setFont(new java.awt.Font("Tahoma", 0, 12));
        taskButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        taskButton.setText("Task");
        taskButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        taskButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                taskButtonMouseClicked(evt);
            }
        });
        taskButton.setBounds(310, 10, 290, 30);
        controlPane.add(taskButton, javax.swing.JLayeredPane.DEFAULT_LAYER);

        gossipButton.setFont(new java.awt.Font("Tahoma", 0, 12));
        gossipButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gossipButton.setText("Gossip");
        gossipButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        gossipButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                gossipButtonMouseClicked(evt);
            }
        });
        gossipButton.setBounds(10, 10, 290, 30);
        controlPane.add(gossipButton, javax.swing.JLayeredPane.DEFAULT_LAYER);

        stateLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        stateLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        stateLabel.setBounds(250, 310, 100, 25);
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

    private void aboutOkButtonActionPerformed(ActionEvent evt) {
        // add your handling code here:
    }

    private void gossipButtonMouseClicked(java.awt.event.MouseEvent evt) {
        gossipPanel.setVisible(true);
        taskPanel.setVisible(false);
        gossipButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        taskButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
    }

    private void taskButtonMouseClicked(java.awt.event.MouseEvent evt) {
        taskPanel.setVisible(true);
        gossipPanel.setVisible(false);
        taskButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        gossipButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
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
    private javax.swing.JLabel gossipButton;
    private javax.swing.JLabel gossipLabel;
    private javax.swing.JPanel gossipPanel;
    private javax.swing.JLabel gossipProgressLabel;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JLabel listenerLabel;
    private javax.swing.JLabel listenerStatLabel;
    private javax.swing.JLabel mCountLabel;
    private javax.swing.JLabel mLabel;
    private javax.swing.JMenuBar mainMenuBar;
    private javax.swing.JLabel membersLabel;
    private javax.swing.JLabel membersLabel1;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JLabel siyapathLogo;
    private javax.swing.JLabel stateLabel;
    private javax.swing.JLabel stateLabel1;
    private javax.swing.JMenu systemMenu;
    private javax.swing.JLabel taskButton;
    private javax.swing.JPanel taskPanel;
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

    public void setStatus(String status) {
        stateLabel.setText(status);
    }
}
