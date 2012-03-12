package org.siyapath.client;


import javax.swing.*;
import java.io.File;

public class SiyapathClient {
private UserGUI gui;


    public SiyapathClient() {
        this.gui = new UserGUI();

    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
        SiyapathClient client = new SiyapathClient();
        client.start();
        System.out.println(new File(".").getAbsolutePath());
    }

    private void start() {
           gui.setVisible(true);
    }
}
