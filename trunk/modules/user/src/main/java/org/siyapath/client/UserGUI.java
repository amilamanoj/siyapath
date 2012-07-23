package org.siyapath.client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
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
        this.handler = handler;
        this.jobEditor = new JobEditorGUI(this, true, this);
        this.jobStatusUI = new JobStatusUI(handler);
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
        userPanel = new javax.swing.JPanel();
        userWelcomeLabel = new javax.swing.JLabel();
        userLogoutButton = new javax.swing.JButton();
        jobSubmitButton = new javax.swing.JButton();
        jobInfoLabel = new javax.swing.JLabel();
        createEditJobButton = new javax.swing.JButton();
        loginPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        usernameText = new javax.swing.JTextField();
        passwordText = new javax.swing.JPasswordField();
        loginButton = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        busyPanel = new javax.swing.JPanel();
        busyLabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        statusPanel = new javax.swing.JPanel();
        siyapathLogo = new javax.swing.JLabel();
        mainMenuBar = new javax.swing.JMenuBar();
        systemMenu = new javax.swing.JMenu();
        logoutMenu = new javax.swing.JMenuItem();
        exitMenu = new javax.swing.JMenuItem();
        userMenu = new javax.swing.JMenu();
        userProfileMenu = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutMenu = new javax.swing.JMenuItem();
        JobStatusButton = new javax.swing.JButton();

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

        userWelcomeLabel.setFont(new java.awt.Font("Bodoni MT", 0, 16)); // NOI18N
        userWelcomeLabel.setText("Welcome");

        userLogoutButton.setFont(new java.awt.Font("Bodoni MT", 0, 14)); // NOI18N
        userLogoutButton.setText("Logout");
        userLogoutButton.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        userLogoutButton.setIconTextGap(40);
        userLogoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userLogoutButtonActionPerformed(evt);
            }
        });

        jobSubmitButton.setFont(new java.awt.Font("Bodoni MT", 0, 14)); // NOI18N
        jobSubmitButton.setText("Submit Job");
        jobSubmitButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jobSubmitButton.setIconTextGap(20);
        jobSubmitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jobSubmitButtonActionPerformed(evt);
            }
        });

        jobInfoLabel.setFont(new java.awt.Font("Bodoni MT", 0, 24)); // NOI18N
        jobInfoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jobInfoLabel.setText("No Job Defined");

        createEditJobButton.setFont(new java.awt.Font("Bodoni MT", 0, 14)); // NOI18N
        createEditJobButton.setText("Create Job...");
        createEditJobButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        createEditJobButton.setIconTextGap(20);
        createEditJobButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createEditJobButtonActionPerformed(evt);
            }
        });

        JobStatusButton.setText("View Job Status");
        JobStatusButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JobStatusButtonActionPerformed(evt);
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
                                        .addComponent(jobInfoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
                                        .addGroup(userPanelLayout.createSequentialGroup()
                                                .addGap(226, 226, 226)
                                                .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(JobStatusButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(createEditJobButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE))))
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
                                                .addComponent(createEditJobButton, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(JobStatusButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(66, 66, 66))
                                        .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(userLogoutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jobSubmitButton, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE))))
        );

        userPanel.setBounds(10, 10, 590, 310);
        controlPane.add(userPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jLabel2.setFont(new java.awt.Font("Copperplate Gothic Light", 1, 14));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Welcome to Siyapath");
        jLabel2.setPreferredSize(new java.awt.Dimension(800, 40));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("You are not logged in. please login below...");
        jLabel3.setPreferredSize(new java.awt.Dimension(800, 40));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel6.setText("Username:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel7.setText("Password:");

        usernameText.setFont(new java.awt.Font("Tahoma", 0, 14));
        usernameText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernameTextActionPerformed(evt);
            }
        });
        usernameText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                usernameTextKeyPressed(evt);
            }
        });

        passwordText.setFont(new java.awt.Font("Tahoma", 0, 14));
        passwordText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordTextActionPerformed(evt);
            }
        });
        passwordText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                passwordTextKeyPressed(evt);
            }
        });

        loginButton.setText("Login");
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                loginButtonMouseClicked(evt);
            }
        });
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel8.setText("Please enter your credentials below:");

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout loginPanelLayout = new javax.swing.GroupLayout(loginPanel);
        loginPanel.setLayout(loginPanelLayout);
        loginPanelLayout.setHorizontalGroup(
                loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(loginPanelLayout.createSequentialGroup()
                                .addGap(91, 91, 91)
                                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
                                        .addComponent(jLabel3, 0, 0, Short.MAX_VALUE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, loginPanelLayout.createSequentialGroup()
                                                .addComponent(jLabel8)
                                                .addGap(182, 182, 182))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loginPanelLayout.createSequentialGroup()
                                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel6))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                                                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(passwordText, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                                                        .addComponent(usernameText, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                                                        .addComponent(loginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(93, 93, 93))
        );
        loginPanelLayout.setVerticalGroup(
                loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(loginPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(loginPanelLayout.createSequentialGroup()
                                                .addGap(15, 15, 15)
                                                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel6)
                                                        .addComponent(usernameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(passwordText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel7))
                                                .addGap(7, 7, 7))
                                        .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(loginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(61, Short.MAX_VALUE))
        );

        loginPanel.setBounds(10, 10, 590, 310);
        controlPane.add(loginPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

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
                                .addContainerGap(192, Short.MAX_VALUE)
                                .addComponent(busyLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addGap(85, 85, 85))
        );

        busyPanel.setBounds(10, 10, 590, 310);
        controlPane.add(busyPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
                statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 590, Short.MAX_VALUE)
        );
        statusPanelLayout.setVerticalGroup(
                statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 310, Short.MAX_VALUE)
        );

        statusPanel.setBounds(10, 10, 590, 310);
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
        userMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userMenuActionPerformed(evt);
            }
        });

        userProfileMenu.setText("My Profile");
        userProfileMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userProfileMenuActionPerformed(evt);
            }
        });
        userMenu.add(userProfileMenu);

        mainMenuBar.add(userMenu);

        helpMenu.setText("Help");
        helpMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpMenuActionPerformed(evt);
            }
        });

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
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addComponent(siyapathLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(nameLabel, 0, 0, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(aboutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 322, Short.MAX_VALUE)
                                                        .addComponent(exitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addComponent(controlPane, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(siyapathLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(nameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addComponent(controlPane, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(aboutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(exitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12))
        );

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

    private void userMenuActionPerformed(ActionEvent evt) {
    }

    private void helpMenuActionPerformed(ActionEvent evt) {
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
        jobStatusUI.setVisible(true);
    }

    private void usernameTextActionPerformed(ActionEvent evt) {
    }

    private void usernameTextKeyPressed(KeyEvent evt) {
        int keyCode = evt.getKeyCode();
        if (keyCode == 10) {
            passwordText.requestFocus();
            passwordText.selectAll();
        }
    }

    private void passwordTextActionPerformed(ActionEvent evt) {
    }

    private void passwordTextKeyPressed(KeyEvent evt) {
        int keyCode = evt.getKeyCode();
        if (keyCode == 10) {
            login();
        }
    }

    private void loginButtonMouseClicked(MouseEvent evt) {
    }

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
        String userName = usernameText.getText();
        String password = String.valueOf(passwordText.getPassword());
        String res = handler.authenticate(userName, password);
        if (res.equals("connecEx")) {
            JOptionPane.showMessageDialog(this, "Could not connect to bootstrapper", "Login Failed", JOptionPane.ERROR_MESSAGE);
            loggedOut();
        } else if (res.equals("success")) {
            loggedPerson = userName;
            logged();
        } else {
            loggedOut();
            JOptionPane.showMessageDialog(this, "Invalid username or password, access denied!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            usernameText.requestFocus();
            usernameText.selectAll();
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
        usernameText.setText(null);
        passwordText.setText(null);
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

    private javax.swing.JButton aboutButton;
    private javax.swing.JMenuItem aboutMenu;
    private javax.swing.JLabel busyLabel;
    private javax.swing.JPanel busyPanel;
    private javax.swing.JLayeredPane controlPane;
    private javax.swing.JButton createEditJobButton;
    private javax.swing.JButton exitButton;
    private javax.swing.JMenuItem exitMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jobInfoLabel;
    private javax.swing.JButton jobSubmitButton;
    private javax.swing.JButton loginButton;
    private javax.swing.JPanel loginPanel;
    private javax.swing.JMenuItem logoutMenu;
    private javax.swing.JMenuBar mainMenuBar;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JPasswordField passwordText;
    private javax.swing.JLabel siyapathLogo;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JMenu systemMenu;
    private javax.swing.JButton userLogoutButton;
    private javax.swing.JMenu userMenu;
    private javax.swing.JPanel userPanel;
    private javax.swing.JMenuItem userProfileMenu;
    private javax.swing.JLabel userWelcomeLabel;
    private javax.swing.JTextField usernameText;
    private javax.swing.JButton JobStatusButton;


}
