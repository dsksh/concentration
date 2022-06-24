
//import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ViewImpl extends JFrame implements View {

    private Model model;
    private JLabel distLabel;

    public ViewImpl(Model model) {
        this.model = model;
        model.addView(this);

        setTitle("Concentration Monitor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(model.WX, model.WY + 100);
        setLocationRelativeTo(null);

        TestPane canvas = new TestPane();
        getContentPane().add(canvas, BorderLayout.CENTER);

        JPanel south = new JPanel();
        south.setLayout(new GridLayout(2,1));

        JPanel ctrl = new JPanel();
        JLabel lDesc = new JLabel("Distance:");
        ctrl.add(lDesc);
        distLabel = new JLabel();
        ctrl.add(distLabel);

        JButton bClose = new JButton("Quit");
        bClose.addActionListener(e -> System.exit(0));
        ctrl.add(bClose);
        south.add(ctrl);

        getContentPane().add(south, BorderLayout.PAGE_END);

        setVisible(true);
    }

    @Override
    public void repaint() {
        distLabel.setText(Integer.toString(model.getDistance()));
        super.repaint();
    }

    class TestPane extends JPanel {

        private int dia = 30;

        public TestPane() {
            setBackground(Color.WHITE);

            addMouseMotionListener(new MouseAdapter() { 
                public void mouseMoved(MouseEvent me) { 
                    model.mousePosX = me.getX();
                    model.mousePosY = me.getY();
                } 
            }); 

            Timer timer = new Timer(40, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    model.moveCircle();
                }
            });
            timer.start();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(Model.WX, Model.WY);
        }

        AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            //g2d.setColor(Color.GRAY);
            //g2d.fillOval(0, 0, dia, dia);

            //g2d.setColor(Color.GRAY);
            //g2d.fillOval(Model.WX-dia, Model.WY-dia, dia, dia);

            g2d.setColor(Color.RED);
            g2d.fillOval(model.posX - dia/2, model.posY - dia/2, dia, dia);

            //g2d.setColor(Color.GRAY);
            //g2d.fillArc(model.posX, model.posY, dia, dia, 
            //        (int)Math.toDegrees(th - thBnd), 
            //        (int)Math.toDegrees(2 * thBnd) );

            //g2d.setColor(Color.BLUE);
            //g2d.drawLine(model.posX+dia/2, model.posY+dia/2, 
            //        model.posX+dia/2 + (int)(10 * vR * Math.cos(vTh)), 
            //        model.posY+dia/2 - (int)(10 * vR * Math.sin(vTh)) );

            g2d.setColor(Color.BLUE);
            
            g2d.setComposite(alcom);

            g2d.fillOval(model.mousePosX - dia/2, model.mousePosY - dia/2, dia, dia);

            g2d.dispose();
        }
    }

}

/* eof */
