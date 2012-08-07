package org.siyapath.client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.Map;

/*
 * The graphical user interface of siyapath client
 */
public class UserGUI extends JFrame {

    UserHandler handler;
    private JobEditorGUI jobEditor;
    private String loggedPerson;
    private Map taskFileList;
    private JobStatusUI jobStatusUI;

    /**
     * @param handler
     */
    public UserGUI(UserHandler handler) {
        initComponents();
        siyapathLogo.setIcon(new javax.swing.ImageIcon(this.getClass().getResource("/siyapathLogo232x184.png")));
        this.handler = handler;
        this.jobEditor = new JobEditorGUI(this, true, this);
        jobSubmitButton.setEnabled(false);
        loginPanel.setVisible(true);
        userPanel.setVisible(false);
        busyPanel.setVisible(false);
        userMenu.setVisible(false);
        logoutMenu.setVisible(false);
        this.setLocationRelativeTo(null);
    }

    private void initComponents() {

        nameLabel = new javax.swing.JLabel();
        exitButton = new javax.swing.JButton();
        aboutButton = new javax.swing.JButton();
        controlPane = new javax.swing.JLayeredPane();
        loginPanel = new javax.swing.JPanel();
        loginWelcomeLabel = new javax.swing.JLabel();
        loginInfoLabel = new javax.swing.JLabel();
        loginButton = new javax.swing.JButton();
        siyapathLogo = new javax.swing.JLabel();
        userPanel = new javax.swing.JPanel();
        userWelcomeLabel = new javax.swing.JLabel();
        userLogoutButton = new javax.swing.JButton();
        jobSubmitButton = new javax.swing.JButton();
        jobInfoLabel = new javax.swing.JLabel();
        createEditJobButton = new javax.swing.JButton();
        busyPanel = new javax.swing.JPanel();
        busyLabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        statusPanel = new javax.swing.JPanel();
        mainMenuBar = new javax.swing.JMenuBar();
        systemMenu = new javax.swing.JMenu();
        logoutMenu = new javax.swing.JMenuItem();
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

        userWelcomeLabel.setFont(new java.awt.Font("Bodoni MT", 0, 16));
        userWelcomeLabel.setText("Welcome");

        userLogoutButton.setFont(new java.awt.Font("Bodoni MT", 0, 14));
        userLogoutButton.setText("Logout");
        userLogoutButton.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        userLogoutButton.setIconTextGap(40);
        userLogoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userLogoutButtonActionPerformed(evt);
            }
        });

        jobSubmitButton.setFont(new java.awt.Font("Bodoni MT", 0, 14));
        jobSubmitButton.setText("Submit Job");
        jobSubmitButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jobSubmitButton.setIconTextGap(20);
        jobSubmitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jobSubmitButtonActionPerformed(evt);
            }
        });

        jobInfoLabel.setFont(new java.awt.Font("Bodoni MT", 0, 24));
        jobInfoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jobInfoLabel.setText("No Job Defined");

        createEditJobButton.setFont(new java.awt.Font("Bodoni MT", 0, 14));
        createEditJobButton.setText("Create Job...");
        createEditJobButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        createEditJobButton.setIconTextGap(20);
        createEditJobButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createEditJobButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout userPanelLayout = new javax.swing.GroupLayout(userPanel);
        userPanel.setLayout(userPanelLayout);
        userPanelLayout.setHorizontalGroup(
                userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(userPanelLayout.createSequentialGroup()
                                .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(userPanelLayout.createSequentialGroup()
                                                .addComponent(userWelcomeLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 413, Short.MAX_VALUE))
                                        .addGroup(userPanelLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jobSubmitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(userLogoutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(userPanelLayout.createSequentialGroup()
                                .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(userPanelLayout.createSequentialGroup()
                                                .addGap(226, 226, 226)
                                                .addComponent(createEditJobButton, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jobInfoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE))
                                .addContainerGap())
        );
        userPanelLayout.setVerticalGroup(
                userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(userPanelLayout.createSequentialGroup()
                                .addComponent(userWelcomeLabel)
                                .addGap(3, 3, 3)
                                .addComponent(jobInfoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(userPanelLayout.createSequentialGroup()
                                                .addComponent(createEditJobButton, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                                                .addGap(113, 113, 113))
                                        .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(userLogoutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jobSubmitButton, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE))))
        );

        userPanel.setBounds(10, 10, 590, 340);
        controlPane.add(userPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        busyLabel.setFont(new java.awt.Font("Tahoma", 0, 18));
        busyLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        busyLabel.setText("Loading...");
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

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
                statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 590, Short.MAX_VALUE)
        );
        statusPanelLayout.setVerticalGroup(
                statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 340, Short.MAX_VALUE)
        );

        statusPanel.setBounds(10, 10, 590, 340);
        controlPane.add(statusPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        systemMenu.setText("System");
        systemMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                systemMenuActionPerformed(evt);
            }
        });

        logoutMenu.setText("Logout");
        logoutMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutMenuActionPerformed(evt);
            }
        });
        systemMenu.add(logoutMenu);

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
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 322, Short.MAX_VALUE)
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
                                        .addComponent(exitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12))
        );

        getAccessibleContext().setAccessibleName("Siyapath - Volunteer Computing");

        pack();
    }

    private void exitButtonActionPerformed(ActionEvent evt) {
        exit();
    }

    private void aboutButtonActionPerformed(ActionEvent evt) {
        about();
    }

    private void userLogoutButtonActionPerformed(ActionEvent evt) {
        logOut();
    }

    private void systemMenuActionPerformed(ActionEvent evt) {
    }

    private void logoutMenuActionPerformed(ActionEvent evt) {
        logOut();
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

    private void createEditJobButtonActionPerformed(ActionEvent evt) {
        jobEditor.setVisible(true);
    }

    private void JobStatusButtonActionPerformed(java.awt.event.ActionEvent evt) {
        StatusUiThread statusUiThread = new StatusUiThread();
        statusUiThread.run();
    }

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
        login();
    }

    private void jobSubmitButtonActionPerformed(ActionEvent evt) {
        handler.submitJob(taskFileList);
    }

    public void jobUpdated(String jobName, Map taskFileList) {
        this.taskFileList = taskFileList;
        int jobId = handler.generateJobID();
        jobInfoLabel.setText("<html>Job Id:" + jobId + "<br>Job Name:" + jobName + "<br>No. of tasks:" + taskFileList.keySet().size() + "</html>");
        jobSubmitButton.setEnabled(true);
        createEditJobButton.setText("Edit Job...");
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

    private void logOut() {
        if (JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logging out", 0) == 0) {
            loggedPerson = null;
            loggedOut();
        }
    }

    public void logged() {
        loginPanel.setVisible(false);
        busyPanel.setVisible(false);
        userPanel.setVisible(true);
        userMenu.setVisible(true);
        logoutMenu.setVisible(true);
        userWelcomeLabel.setText("Welcome " + loggedPerson);
    }

    public void busy() {
        busyPanel.setVisible(true);
        loginPanel.setVisible(false);
        userPanel.setVisible(false);
        userMenu.setVisible(false);
        logoutMenu.setVisible(false);
    }

    public void loggedOut() {
//        usernameText.setText(null);
//        passwordText.setText(null);
        busyPanel.setVisible(false);
        loginPanel.setVisible(true);
        userPanel.setVisible(false);
        userMenu.setVisible(false);
        logoutMenu.setVisible(false);
    }

    private void showUserProfile() {
        JOptionPane.showMessageDialog(this, "User Info", "User Profile", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showSplash() {
//        SplashScreen ss = new SplashScreen(this, true);
//        ss.setVisible(true);
    }

    private class StatusUiThread extends Thread {

        @Override
        public void run() {
            jobStatusUI = new JobStatusUI(handler);
            jobStatusUI.setVisible(true);
        }

    }

    private javax.swing.JButton aboutButton;
    private javax.swing.JMenuItem aboutMenu;
    private javax.swing.JLabel busyLabel;
    private javax.swing.JPanel busyPanel;
    private javax.swing.JLayeredPane controlPane;
    private javax.swing.JButton createEditJobButton;
    private javax.swing.JButton exitButton;
    private javax.swing.JMenuItem exitMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel siyapathLogo;
    private javax.swing.JLabel jobInfoLabel;
    private javax.swing.JButton jobSubmitButton;
    private javax.swing.JButton loginButton;
    private javax.swing.JLabel loginInfoLabel;
    private javax.swing.JPanel loginPanel;
    private javax.swing.JLabel loginWelcomeLabel;
    private javax.swing.JMenuItem logoutMenu;
    private javax.swing.JMenuBar mainMenuBar;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JMenu systemMenu;
    private javax.swing.JButton userLogoutButton;
    private javax.swing.JMenu userMenu;
    private javax.swing.JPanel userPanel;
    private javax.swing.JMenuItem userProfileMenu;
    private javax.swing.JLabel userWelcomeLabel;


}
