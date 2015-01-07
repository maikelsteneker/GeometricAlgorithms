package nl.tue.win.ga.gui;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import nl.tue.win.ga.io.ReadPolygonFromFile;
import nl.tue.win.ga.io.WritePolygonToFile;
import nl.tue.win.ga.model.*;
import nl.tue.win.ga.algorithms.*;
import nl.tue.win.ga.io.ExportPolygonToBitmap;

/**
 * GUI for drawing input files for DBL Algorithms
 *
 * @author maikel
 */
public class DrawInterface extends javax.swing.JFrame {

    ArrayList<Point> points = new ArrayList<>();
    File outputFile;
    final int MAX_POINTS = 100000;
    final static String EXTENSION = "txt";
    int selected = -1;
    List<LineSegment> segments = new ArrayList<>();

    /**
     * Creates new form DrawInterface
     */
    public DrawInterface() {
        initComponents();
        jLabel1.setVisible(false);
        jLabel2.setVisible(false);
        jLabel3.setVisible(false);
        jLabel4.setVisible(false);
        jTextFieldMin.setVisible(false);
        jTextFieldMax.setVisible(false);
        intensitySlider.setVisible(false);
        radiusSlider.setVisible(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        buttonGroup5 = new javax.swing.ButtonGroup();
        buttonGroup6 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                paintMainPanel(this, g);
            }};
            jTextFieldMin = new javax.swing.JTextField();
            jButton1 = new javax.swing.JButton();
            jTextFieldMax = new javax.swing.JTextField();
            jButton2 = new javax.swing.JButton();
            jLabel1 = new javax.swing.JLabel();
            radiusSlider = new javax.swing.JSlider();
            jLabel2 = new javax.swing.JLabel();
            intensitySlider = new javax.swing.JSlider();
            jLabel3 = new javax.swing.JLabel();
            jLabel4 = new javax.swing.JLabel();
            jLabel5 = new javax.swing.JLabel();
            jButton3 = new javax.swing.JButton();
            jCheckBox1 = new javax.swing.JCheckBox();
            jCheckBox2 = new javax.swing.JCheckBox();
            jButton4 = new javax.swing.JButton();
            jButton5 = new javax.swing.JButton();

            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

            jPanel1.setBackground(new java.awt.Color(255, 255, 255));
            jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    jPanel1MousePressed(evt);
                }
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    jPanel1MouseReleased(evt);
                }
            });
            jPanel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
                public void mouseDragged(java.awt.event.MouseEvent evt) {
                    jPanel1MouseDragged(evt);
                }
            });

            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 607, Short.MAX_VALUE)
            );
            jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 149, Short.MAX_VALUE)
            );

            jTextFieldMin.setText("3");

            jButton1.setText("Save");
            jButton1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });

            jTextFieldMax.setText("5");

            jButton2.setText("Clear");
            jButton2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton2ActionPerformed(evt);
                }
            });

            jLabel1.setText("to");

            radiusSlider.setMaximum(50);
            radiusSlider.setMinimum(4);
            radiusSlider.setValue(1);

            jLabel2.setText("Clusters");

            intensitySlider.setMaximum(50);
            intensitySlider.setMinimum(1);
            intensitySlider.setValue(1);

            jLabel3.setText("Intensity");

            jLabel4.setText("Radius");

            jLabel5.setText("Number of points: 0");

            jButton3.setText("Load");
            jButton3.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton3ActionPerformed(evt);
                }
            });

            jCheckBox1.setText("scaled");
            jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jCheckBox1ActionPerformed(evt);
                }
            });

            jCheckBox2.setText("invert Y");
            jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jCheckBox2ActionPerformed(evt);
                }
            });

            jButton4.setText("jButton4");
            jButton4.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton4ActionPerformed(evt);
                }
            });

            jButton5.setText("Export");
            jButton5.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton5ActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jTextFieldMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextFieldMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel2)
                                            .addGap(18, 18, 18)
                                            .addComponent(jLabel5))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jCheckBox1)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jCheckBox2)))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 242, Short.MAX_VALUE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                            .addComponent(jButton4)
                                            .addGap(31, 31, 31)
                                            .addComponent(jButton1))
                                        .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING)))
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(intensitySlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(radiusSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabel4)))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel3)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 228, Short.MAX_VALUE)
                                            .addComponent(jButton3))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                            .addGap(0, 0, Short.MAX_VALUE)
                                            .addComponent(jButton5)))))))
                    .addContainerGap())
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton1)
                        .addComponent(jTextFieldMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2)
                        .addComponent(jLabel5)
                        .addComponent(jButton4))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton2)
                        .addComponent(jCheckBox1)
                        .addComponent(jCheckBox2))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(intensitySlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel4)
                                .addComponent(radiusSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jButton3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton5)))
                    .addGap(7, 7, 7))
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        JFileChooser fc = new JFileChooser();
        fc.setDialogType(JFileChooser.SAVE_DIALOG);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Simple Polygon", EXTENSION);
        fc.setFileFilter(filter);
        if (outputFile != null) {
            fc.setCurrentDirectory(new File(outputFile, ".."));
        }
        int showSaveDialog = fc.showSaveDialog(this);

        outputFile = fc.getSelectedFile();
        if (fc.getFileFilter().equals(filter)) {
            if (outputFile != null && !outputFile.toString().endsWith("." + EXTENSION)) {
                outputFile = new File(outputFile.toString() + "." + EXTENSION);
            }
        }

        if (showSaveDialog == JFileChooser.APPROVE_OPTION) {
            try {
                WritePolygonToFile.writePolygonToFile(outputFile, points);
            } catch (IOException ex) {
                fc.showDialog(this, "There was a problem when writing to the "
                        + "specified file.");
            } catch (Exception ex) {
                //Logger.getLogger(DrawInterface.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, ex.toString(),
                        "error", JOptionPane.ERROR_MESSAGE);
            }
        }
        repaint();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jPanel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MousePressed
        //start drawing
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).distanceSq(evt.getPoint()) <= 50) {
                selected = i;
                break;
            }
        }
        //System.out.println("button:" + evt.getButton() + ", selected:" + selected);
        switch (evt.getButton()) {
            case MouseEvent.BUTTON3:
                if (selected >= 0) {
                    points.remove(selected);
                }
                selected = -1;
                break;
            default:
                if (selected < 0) {
                    points.add(evt.getPoint());
                }
                removeDuplicates();
        }
        repaint();
    }//GEN-LAST:event_jPanel1MousePressed

    private void jPanel1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseReleased
        //stop dragging
        selected = -1;
    }//GEN-LAST:event_jPanel1MouseReleased

    private void jPanel1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseDragged
        //points.add(evt.getPoint());
        /*if (points.size() < MAX_POINTS) {
         for (int i = 0; i < Math.min(intensitySlider.getValue(), MAX_POINTS-1); i++) {
         Point p = generateRandomPoint(evt.getX(), evt.getY());
         points.add(p);
         if(points.size() > MAX_POINTS){
         points.remove(p);
         }
         }
         removeDuplicates();
         } else {
         JOptionPane.showMessageDialog(this, "Maximum points has been reached.");
         }*/
        if (selected >= 0) {
            points.get(selected).setLocation(evt.getPoint());
        }
        repaint();
    }//GEN-LAST:event_jPanel1MouseDragged

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        points.clear();
        segments.clear();
        repaint();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        JFileChooser fc = new JFileChooser();
        fc.setDialogType(JFileChooser.OPEN_DIALOG);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Simple polygon", EXTENSION);
        fc.setFileFilter(filter);

        fc.setCurrentDirectory(new File("./Input/"));

        int showOpenDialog = fc.showOpenDialog(this);
        File currentFile = fc.getSelectedFile();
        if (showOpenDialog == JFileChooser.APPROVE_OPTION) {
            points.clear();
            SimplePolygon polygon = new SimplePolygon();
            try {
                polygon = ReadPolygonFromFile.readPolygonFromFile(currentFile);
            } catch (Exception ex) {
                //Logger.getLogger(DrawInterface.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, ex.toString(),
                        "error", JOptionPane.ERROR_MESSAGE);
            }
            points = new ArrayList<>(polygon.getHull());

            //this.jTextFieldMin.setText(input.getMinClusters() + "");
            //this.jTextFieldMax.setText(input.getMaxClusters() + "");
        }
        repaint();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        repaint();
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        repaint();
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        SweepLine sl =  new SweepLine(points);
        sl.sweep();
        segments = sl.getVerticals();
        repaint();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        JFileChooser fc = new JFileChooser();
        fc.setDialogType(JFileChooser.SAVE_DIALOG);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Bitmap", "bmp");
        fc.setFileFilter(filter);
        if (outputFile != null) {
            fc.setCurrentDirectory(new File(outputFile, ".."));
        }
        int showSaveDialog = fc.showSaveDialog(this);

        outputFile = fc.getSelectedFile();
        if (fc.getFileFilter().equals(filter)) {
            if (outputFile != null && !outputFile.toString().endsWith(".bmp")) {
                outputFile = new File(outputFile.toString() + ".bmp");
            }
        }

        if (showSaveDialog == JFileChooser.APPROVE_OPTION) {
            try {
                ExportPolygonToBitmap.exportPolygonToImage(outputFile, new SimplePolygon(points), "bmp", !jCheckBox2.isEnabled());
            } catch (IOException ex) {
                fc.showDialog(this, "There was a problem when writing to the "
                        + "specified file.");
            } catch (Exception ex) {
                //Logger.getLogger(DrawInterface.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, ex.toString(),
                        "error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void paintMainPanel(javax.swing.JPanel panel, Graphics g) {
        SimplePolygon polygon = new SimplePolygon(points);
        polygon.draw(g, jCheckBox1.isSelected(), jCheckBox2.isSelected());
        for(LineSegment ls: segments) {
            g.drawLine(ls.getStartPoint().x, ls.getStartPoint().y, ls.getEndPoint().x, ls.getEndPoint().y);
        }
        jLabel5.setText("Number of points: " + this.points.size()
                + (!polygon.invariant() ? "\t Invariant violated!!!" : ""));
        
        
    }

    private Point generateRandomPoint(int x, int y) {
        double module = Math.random() * (double) radiusSlider.getValue();
        double angle = Math.PI * 2.0 * Math.random();
        Point p = new Point();
        p.x = (int) (module * Math.cos(angle) + (double) x);
        p.y = (int) (module * Math.sin(angle) + (double) y);

        return p;
    }

    private void removeDuplicates() {
        HashSet h = new LinkedHashSet(points);
        points.clear();
        points.addAll(h);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DrawInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DrawInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DrawInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DrawInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new DrawInterface().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.ButtonGroup buttonGroup6;
    private javax.swing.JSlider intensitySlider;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextFieldMax;
    private javax.swing.JTextField jTextFieldMin;
    private javax.swing.JSlider radiusSlider;
    // End of variables declaration//GEN-END:variables
}
