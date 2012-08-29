package org.siyapath.client.ui;


import org.siyapath.client.UserHandler;

import javax.swing.*;
import java.io.File;

public class SiyapathUI {
private UserGUI gui;

    public SiyapathUI() {

        this.gui = new UserGUI(new UserHandler());
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
        SiyapathUI UI = new SiyapathUI();
        UI.start();
        System.out.println(new File(".").getAbsolutePath());
    }

    private void start() {
           gui.setVisible(true);
    }
}
