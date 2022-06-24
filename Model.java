import java.util.*;

public class Model {

    private List<View> views;

    static final int WX = 400;
    static final int WY = 400;
    static final double WD = Math.sqrt(WX*WX + WY*WY);

    static final double WIND_COEFF = 0.5;

    int mousePosX;
    int mousePosY;

    int posX = 200;
    int posY = 200;
    double vTh = 0.0;
    double vR = 2.0;

    double th;
    double thBnd;

    private Random rng = new Random();

    public Model() {
        views = new ArrayList<View>();
    }

    public void addView(View view) {
        views.add(view);
    }

    private double normalize(double rad) {
        if (rad < 0) 
            return 2*Math.PI + rad;
        else if (rad > 2*Math.PI) 
            return rad - 2*Math.PI;
        else
            return rad;
    }

    public void moveCircle() {
        double dx = posX - WX/2;
        double dy = posY - WY/2;
        double dPos = Math.sqrt(dx*dx + dy*dy);
        if (dPos != 0) {
            th = Math.acos(dx/dPos);
            if (dy > 0) th = 2*Math.PI - th;
            assert 0 <= th && th <= 2*Math.PI : "invalid th";

            // Half-range of the invalid speed angles.
            thBnd = (dPos/(WD/2)) * (Math.PI);
            assert 0 <= thBnd && thBnd <= Math.PI : "invalid thBnd";

            double vTh1 = vTh - th + WIND_COEFF * (rng.nextDouble() - 0.5);
            vTh1 = normalize(vTh1);

            //if (Math.abs(thBnd - vTh1) < 0.1)
            if (thBnd > vTh1)
                vTh1 = thBnd;
            //else if (Math.abs(vTh1 + thBnd - 2*Math.PI) < 0.1)
            else if (vTh1 + thBnd > 2*Math.PI)
                vTh1 = 2*Math.PI - thBnd;

            vTh = normalize( vTh1 + th );

            //System.out.printf("  %f, %f, %f, %f\n", th, thBnd, vTh1, dPos);
            assert 0 <= vTh1 && vTh1 <= 2*Math.PI : "invalid vTh1";
        }

        //double vy = rng.nextDouble() * 2*Math.PI;

        //System.out.printf("v: %f, %f\n", vR * Math.cos(vTh), vR * Math.sin(vTh));

        posX += vR * Math.cos(vTh);
        posY -= vR * Math.sin(vTh);

        // Update views.
        for (View v : views)
            v.repaint();
    }

    public int getDistance() {
        double dx = mousePosX - posX;
        double dy = mousePosY - posY;
        return (int)Math.sqrt(dx*dx + dy*dy);
    }

}

/* eof */
