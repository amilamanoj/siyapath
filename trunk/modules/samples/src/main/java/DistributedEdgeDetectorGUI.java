/*
* image.SiyapathDemoGUI.java
*
* Created on Aug 17, 2012, 8:43:28 PM
*/

import org.apache.thrift.TException;
import org.siyapath.client.SubmissionFailedException;
import org.siyapath.client.TaskData;
import org.siyapath.client.UserHandler;
import org.siyapath.service.Job;
import org.siyapath.service.TaskResult;
import org.siyapath.service.TaskStatus;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Amila Manoj
 */
public class DistributedEdgeDetectorGUI extends JDialog {

    BufferedImage[][] imageSet;
    UserHandler handler = new UserHandler();
    int jobID;
    int rows = 4, columns = 6;
    int rowHeight, colWidth;
    BufferedImage source;

    public DistributedEdgeDetectorGUI(Frame parent, boolean modal) {
        super(parent, modal);
        imgLabel = new MyJLabel();
        initComponents();
        try {
            source = ImageIO.read(DistributedEdgeDetectorGUI.class.getResource("/demo_car.jpg"));
            imageSet = partitionImage(source, rows, columns);
            imgLabel.repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private MyJLabel imgLabel;
    private javax.swing.JTextField cText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton processButton;
    private javax.swing.JTextField rText;
    private javax.swing.JButton resultButton;

    private void initComponents() {

        processButton = new javax.swing.JButton();
        resultButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        rText = new javax.swing.JTextField();
        cText = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        processButton.setText("Submit");
        processButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                processButtonActionPerformed(evt);
            }
        });

        resultButton.setText("Browse");
        resultButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Rows:");

        jLabel2.setText("Columns:");

        rText.setText("4");

        cText.setText("6");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(rText, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel2)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cText, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(resultButton)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(processButton))
                                        .addComponent(imgLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 1100, Short.MAX_VALUE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(imgLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(processButton)
                                        .addComponent(resultButton)
                                        .addComponent(jLabel1)
                                        .addComponent(jLabel2)
                                        .addComponent(rText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>

    class MyJLabel extends JLabel {
        @Override
        public void paint(Graphics g) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    int top = i * rowHeight;
                    int left = j * colWidth;
                    g.drawImage(imageSet[i][j], left, top, colWidth, rowHeight, null);
                }
            }
        }
    }


    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (UnsupportedLookAndFeelException e) {
                } catch (ClassNotFoundException e) {
                } catch (InstantiationException e) {
                } catch (IllegalAccessException e) {
                }
                DistributedEdgeDetectorGUI dialog = new DistributedEdgeDetectorGUI(new JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    private void processButtonActionPerformed(java.awt.event.ActionEvent evt) {
        File algo = new File("EdgeDetectorTask.class");
        if (!algo.exists()) {
            JOptionPane.showMessageDialog(DistributedEdgeDetectorGUI.this, "Task Program not found", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        this.rows = Integer.parseInt(rText.getText());
        this.columns = Integer.parseInt(cText.getText());
        imageSet = partitionImage(source, rows, columns);
        imgLabel.repaint();

        try {

            Map<String, TaskData> taskList = new HashMap<String, TaskData>();
            for (int j = 0; j < columns; j++) {
                for (int i = 0; i < rows; i++) {
                    String taskName = "taskImg" + i + j;
                    byte[] binImage = imageToBinary(imageSet[i][j]);
                    byte[] inputData = new byte[binImage.length + 2];
                    inputData[0] = (byte) i;
                    inputData[1] = (byte) j;
                    System.arraycopy(binImage, 0, inputData, 2, binImage.length);
                    taskList.put(taskName + i, new TaskData(taskName, algo, inputData, "Cores:4-Speed:2267Mhz"));  //TODO: update GUI
                }
            }

            Job job = handler.createJob(taskList);
            handler.submitJob("MyJob", job);
            jobID = job.getJobID();

            Thread resThread = new Thread(new ResultThread());
            resThread.start();

        } catch (SubmissionFailedException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private class ResultThread implements Runnable {
        private boolean finished;

        @Override
        public void run() {
            Map<Integer, TaskResult> resMap = null;
            while (!finished) {
                try {
                    resMap = handler.pollStatusFromJobProcessor(jobID);
                } catch (TException e) {
                    e.printStackTrace();
                }
                finished = true;
                if (resMap != null) {
                    for (TaskResult res : resMap.values()) {
                        if (res.getStatus() == TaskStatus.DONE) {
                            byte[] resultData = res.getResults();
                            int i = resultData[0];
                            int j = resultData[1];
                            byte[] imgData = new byte[resultData.length - 2];
                            System.arraycopy(resultData, 2, imgData, 0, imgData.length);
                            try {
                                imageSet[i][j] = ImageIO.read(new ByteArrayInputStream(imgData));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            imgLabel.repaint();
                        } else {
                            finished = false;
                        }
                    }
                } else {
                    finished = false;
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
//            for (Integer resKey : resMap.keySet()) {
//                BufferedImage resImage = binaryToImage(resMap.get(resKey).getResults());
//                try {
//                    ImageIO.write(resImage, "jpg", new File("res" + resKey + ".jpg"));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
            JOptionPane.showMessageDialog(DistributedEdgeDetectorGUI.this, "Job completed. Owh yeah \\m/", "Done", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private byte[] imageToBinary(BufferedImage image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", baos);
            baos.flush();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {
        final JFileChooser fc = new JFileChooser(".");
        fc.showOpenDialog(this);
        File sFile = fc.getSelectedFile();
        if (sFile != null) {
            try {
                this.rows = Integer.parseInt(rText.getText());
                this.columns = Integer.parseInt(cText.getText());
                source = ImageIO.read(sFile);
                imageSet = partitionImage(source, rows, columns);
                imgLabel.repaint();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private BufferedImage[][] partitionImage(BufferedImage image, int rows, int columns) {

        int height = image.getHeight();
        int width = image.getWidth();

        rowHeight = height / rows;
        colWidth = width / columns;

        BufferedImage[][] subImageArray = new BufferedImage[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                int top = i * rowHeight;
                int left = j * colWidth;
                subImageArray[i][j] = image.getSubimage(left, top, colWidth, rowHeight);
            }
        }
        return subImageArray;
    }

}


