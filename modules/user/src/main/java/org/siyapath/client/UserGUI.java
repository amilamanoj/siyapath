package org.siyapath.client;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;

import javax.swing.*;
import java.awt.event.*;

/*
 * The graphical user interface of siyapath client
 */
public class UserGUI extends JFrame {

    Timer busyTimer;
    private String loggedPerson;

    public UserGUI() {
        initComponents();
        busyTimer = new Timer(1000, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyTimerActionPerformed(e);
            }
        });
        loginPanel.setVisible(true);
        userPanel.setVisible(false);
        busyPanel.setVisible(false);
        userMenu.setVisible(false);
        logoutMenu.setVisible(false);
        this.setLocationRelativeTo(null);
    }


    private void initComponents() {

        nameLabel = new JLabel();
        exitButton = new JButton();
        aboutButton = new JButton();
        controlPane = new JLayeredPane();
        userPanel = new JPanel();
        userWelcomeLabel = new JLabel();
        userLogoutButton = new JButton();
        jobSubmitButton = new JButton();
        algorithmText = new JTextField();
        userWelcomeLabel1 = new JLabel();
        loginPanel = new JPanel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jLabel6 = new JLabel();
        jLabel7 = new JLabel();
        usernameText = new JTextField();
        passwordText = new JPasswordField();
        loginButton = new JButton();
        jLabel8 = new JLabel();
        jLabel9 = new JLabel();
        busyPanel = new JPanel();
        busyLabel = new JLabel();
        jLabel4 = new JLabel();
        userWelcomeLabel2 = new JLabel();
        mainMenuBar = new JMenuBar();
        systemMenu = new JMenu();
        logoutMenu = new JMenuItem();
        exitMenu = new JMenuItem();
        userMenu = new JMenu();
        userProfileMenu = new JMenuItem();
        helpMenu = new JMenu();
        aboutMenu = new JMenuItem();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Siyapath User");
        setBounds(new java.awt.Rectangle(0, 0, 700, 500));
        setResizable(false);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        nameLabel.setFont(new java.awt.Font("Copperplate Gothic Light", 1, 24));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setText("Siyapath - Volunteer Computing");
        nameLabel.setPreferredSize(new java.awt.Dimension(800, 40));

        exitButton.setFont(new java.awt.Font("Bodoni MT", 0, 14));
        exitButton.setText("Exit");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        aboutButton.setFont(new java.awt.Font("Bodoni MT", 0, 14));
        aboutButton.setText("About");
        aboutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                aboutButtonActionPerformed(evt);
            }
        });

        controlPane.setBorder(BorderFactory.createEtchedBorder());
        controlPane.setOpaque(true);

        userWelcomeLabel.setFont(new java.awt.Font("Bodoni MT", 0, 16));
        userWelcomeLabel.setText("Welcome");

        userLogoutButton.setFont(new java.awt.Font("Bodoni MT", 0, 14)); // NOI18N
        userLogoutButton.setText("Logout");
        userLogoutButton.setHorizontalTextPosition(SwingConstants.LEADING);
        userLogoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                userLogoutButtonActionPerformed(evt);
            }
        });

        jobSubmitButton.setFont(new java.awt.Font("Bodoni MT", 0, 14));
        jobSubmitButton.setText("Submit Job");
        jobSubmitButton.setHorizontalAlignment(SwingConstants.LEFT);
        jobSubmitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jobSubmitButtonActionPerformed(evt);
            }
        });

        userWelcomeLabel1.setFont(new java.awt.Font("Bodoni MT", 0, 16));
        userWelcomeLabel1.setText("Algorithm Location:");

        GroupLayout userPanelLayout = new GroupLayout(userPanel);
        userPanel.setLayout(userPanelLayout);
        userPanelLayout.setHorizontalGroup(
                userPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(userPanelLayout.createSequentialGroup()
                                .addGroup(userPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(userWelcomeLabel)
                                        .addGroup(GroupLayout.Alignment.TRAILING, userPanelLayout.createSequentialGroup()
                                                .addGap(25, 25, 25)
                                                .addComponent(userWelcomeLabel1)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 143, Short.MAX_VALUE)
                                                .addGroup(userPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(algorithmText)
                                                        .addGroup(GroupLayout.Alignment.TRAILING, userPanelLayout.createSequentialGroup()
                                                                .addComponent(jobSubmitButton)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(userLogoutButton, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE)))))
                                .addContainerGap())
        );
        userPanelLayout.setVerticalGroup(
                userPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(userPanelLayout.createSequentialGroup()
                                .addComponent(userWelcomeLabel)
                                .addGap(35, 35, 35)
                                .addGroup(userPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(algorithmText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(userWelcomeLabel1))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 184, Short.MAX_VALUE)
                                .addGroup(userPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jobSubmitButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(userLogoutButton, GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE))
                                .addContainerGap())
        );

        userPanel.setBounds(10, 10, 590, 310);
        controlPane.add(userPanel, JLayeredPane.DEFAULT_LAYER);

        jLabel2.setFont(new java.awt.Font("Copperplate Gothic Light", 1, 14));
        jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel2.setText("Welcome to Siyapath");
        jLabel2.setPreferredSize(new java.awt.Dimension(800, 40));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel3.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel3.setText("You are not logged in. please login below...");
        jLabel3.setPreferredSize(new java.awt.Dimension(800, 40));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel6.setText("Username:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel7.setText("Password:");

        usernameText.setFont(new java.awt.Font("Tahoma", 0, 14));
        usernameText.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                usernameTextActionPerformed(evt);
            }
        });
        usernameText.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                usernameTextKeyPressed(evt);
            }
        });

        passwordText.setFont(new java.awt.Font("Tahoma", 0, 14));
        passwordText.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                passwordTextActionPerformed(evt);
            }
        });
        passwordText.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                passwordTextKeyPressed(evt);
            }
        });

        loginButton.setText("Login");
        loginButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                loginButtonMouseClicked(evt);
            }
        });
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel8.setText("Please enter your credentials below:");

        jLabel9.setHorizontalAlignment(SwingConstants.CENTER);
        GroupLayout loginPanelLayout = new GroupLayout(loginPanel);
        loginPanel.setLayout(loginPanelLayout);
        loginPanelLayout.setHorizontalGroup(
                loginPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(loginPanelLayout.createSequentialGroup()
                                .addGap(91, 91, 91)
                                .addGroup(loginPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                        .addComponent(jLabel2, GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
                                        .addComponent(jLabel3, 0, 0, Short.MAX_VALUE)
                                        .addGroup(GroupLayout.Alignment.LEADING, loginPanelLayout.createSequentialGroup()
                                                .addComponent(jLabel8)
                                                .addGap(182, 182, 182))
                                        .addGroup(GroupLayout.Alignment.TRAILING, loginPanelLayout.createSequentialGroup()
                                                .addComponent(jLabel9, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(loginPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(jLabel7, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel6))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                                                .addGroup(loginPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                        .addComponent(passwordText, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                                                        .addComponent(usernameText, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                                                        .addComponent(loginButton, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE))))
                                .addGap(93, 93, 93))
        );
        loginPanelLayout.setVerticalGroup(
                loginPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(loginPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(jLabel8)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(loginPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(loginPanelLayout.createSequentialGroup()
                                                .addGap(15, 15, 15)
                                                .addGroup(loginPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel6)
                                                        .addComponent(usernameText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(loginPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(passwordText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel7))
                                                .addGap(7, 7, 7))
                                        .addComponent(jLabel9, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(loginButton, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(61, Short.MAX_VALUE))
        );

        loginPanel.setBounds(10, 10, 590, 310);
        controlPane.add(loginPanel, JLayeredPane.DEFAULT_LAYER);

        busyLabel.setFont(new java.awt.Font("Tahoma", 0, 18));
        busyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        busyLabel.setText("Loading...");
        busyLabel.setPreferredSize(new java.awt.Dimension(800, 40));

        jLabel4.setHorizontalAlignment(SwingConstants.CENTER);

        GroupLayout busyPanelLayout = new GroupLayout(busyPanel);
        busyPanel.setLayout(busyPanelLayout);
        busyPanelLayout.setHorizontalGroup(
                busyPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(busyPanelLayout.createSequentialGroup()
                                .addGap(216, 216, 216)
                                .addGroup(busyPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                        .addComponent(busyLabel, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel4))
                                .addContainerGap(215, Short.MAX_VALUE))
        );
        busyPanelLayout.setVerticalGroup(
                busyPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, busyPanelLayout.createSequentialGroup()
                                .addContainerGap(192, Short.MAX_VALUE)
                                .addComponent(busyLabel, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addGap(85, 85, 85))
        );

        busyPanel.setBounds(10, 10, 590, 310);
        controlPane.add(busyPanel, JLayeredPane.DEFAULT_LAYER);

        userWelcomeLabel2.setFont(new java.awt.Font("Bodoni MT", 0, 16));
        userWelcomeLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        userWelcomeLabel2.setText("<html><center>Department of Computer Science & Engineering<br />University of Moratuwa<center><html>");

        systemMenu.setText("System");
        systemMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                systemMenuActionPerformed(evt);
            }
        });

        logoutMenu.setText("Logout");
        logoutMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                logoutMenuActionPerformed(evt);
            }
        });
        systemMenu.add(logoutMenu);

        exitMenu.setText("Exit");
        exitMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                exitMenuActionPerformed(evt);
            }
        });
        systemMenu.add(exitMenu);

        mainMenuBar.add(systemMenu);

        userMenu.setText("User");
        userMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                userMenuActionPerformed(evt);
            }
        });

        userProfileMenu.setText("My Profile");
        userProfileMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                userProfileMenuActionPerformed(evt);
            }
        });
        userMenu.add(userProfileMenu);

        mainMenuBar.add(userMenu);

        helpMenu.setText("Help");
        helpMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                helpMenuActionPerformed(evt);
            }
        });

        aboutMenu.setText("About");
        aboutMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                aboutMenuActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenu);

        mainMenuBar.add(helpMenu);

        setJMenuBar(mainMenuBar);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(nameLabel, GroupLayout.Alignment.LEADING, 0, 0, Short.MAX_VALUE)
                                        .addGroup(GroupLayout.Alignment.LEADING, layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(aboutButton, GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 322, Short.MAX_VALUE)
                                                        .addComponent(exitButton, GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE))
                                                .addComponent(controlPane, GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(nameLabel, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(controlPane, GroupLayout.PREFERRED_SIZE, 330, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(aboutButton, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(exitButton, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE))
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
    }

    private void aboutOkButtonActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void busyTimerActionPerformed(ActionEvent e) {
        busyPanel.setVisible(false);
        controlPane.setVisible(true);
        busyTimer.stop();
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
        String userName = usernameText.getText();
        String password = String.valueOf(passwordText.getPassword());
        try {
        String res="failed";
            if (res.equals("failed")) {
                JOptionPane.showMessageDialog(this, "Invalid username or password, access denied!", "Login Failed", JOptionPane.ERROR_MESSAGE);
                usernameText.requestFocus();
                usernameText.selectAll();
            } else {
                loggedPerson = userName;
                logged(loggedPerson);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Could not connect to bootstrapper", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logOut() {

        if (JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logging out", 0) == 0) {
            showLogoutBusy();
            loggedPerson = null;
            usernameText.setText(null);
            passwordText.setText(null);
            userPanel.setVisible(false);
            loginPanel.setVisible(true);
            userMenu.setVisible(false);
            logoutMenu.setVisible(false);

        }
    }

    public void logged(String user) {
        loggedPerson = user;
        //student
        showLoginBusy();
        loginPanel.setVisible(false);
        userPanel.setVisible(true);
        userMenu.setVisible(true);
        logoutMenu.setVisible(true);
        userWelcomeLabel.setText("Welcome " + loggedPerson);
    }

    private void showUserProfile() {
        JOptionPane.showMessageDialog(this, "User Info", "User Profile", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showSplash() {
//        SplashScreen ss = new SplashScreen(this, true);
//        ss.setVisible(true);
    }

    private void showLoginBusy() {
        busyLabel.setText("Logging in...");
        controlPane.setVisible(false);
        busyPanel.setVisible(true);
        busyTimer.setDelay(1000);
        busyTimer.start();
    }

    private void showLogoutBusy() {
        busyLabel.setText("Logging out...");
        controlPane.setVisible(false);
        busyPanel.setVisible(true);
        busyTimer.setDelay(1000);
        busyTimer.start();
    }

    private JButton aboutButton;
    private JMenuItem aboutMenu;
    //    private JButton aboutOkButton;
//    private JPanel aboutPanel;
    private JTextField algorithmText;
    private JLabel busyLabel;
    private JPanel busyPanel;
    private JLayeredPane controlPane;
    private JButton exitButton;
    private JMenuItem exitMenu;
    private JMenu helpMenu;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JLabel jLabel9;
    private JButton jobSubmitButton;
    private JButton loginButton;
    private JPanel loginPanel;
    private JMenuItem logoutMenu;
    private JMenuBar mainMenuBar;
    private JLabel nameLabel;
    private JPasswordField passwordText;
    private JMenu systemMenu;
    private JButton userLogoutButton;
    private JMenu userMenu;
    private JPanel userPanel;
    private JMenuItem userProfileMenu;
    private JLabel userWelcomeLabel;
    private JLabel userWelcomeLabel1;
    private JLabel userWelcomeLabel2;
    private JTextField usernameText;
}
