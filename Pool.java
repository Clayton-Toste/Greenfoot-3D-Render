import greenfoot.*;
import java.util.Date;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.util.Arrays;

/* 
 * Table is 800x400
 * Ball radius is 21.42857142857143‬
 */

public class Pool extends World
{
    static Ball nullBall = new Ball(new Vector(0,0,0),0);
    static int width = 800;
    static int height = 600;
    static int threads = 1;
    static Double[][] lengths = new Double[width/2+1][height/2+1];

    Raycaster[] raycasters = new Raycaster[threads];
    GreenfootImage image = new GreenfootImage(width, height);
    Ball[] balls = new Ball[15];
    Plane[] planes = new Plane[5];
    Renderable[] renderables = new Renderable[16];

    Vector camera;
    Vector light;
    Vector bdisplacement, idisplacement, jdisplacement;
    double yaw, pitch = 0;

    double mx, my;
    double ox, oy;

    Robot robot; 
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    double sx=screenSize.getWidth()/2;
    double sy=screenSize.getHeight()/2;

    long start = -1;

    int spin;
    int frames;

    public Pool()
    {
        super(width, height, 1);
        try { robot=new Robot(); } catch (AWTException ae) { ae.printStackTrace(); }
        robot.mouseMove((int)sx, (int)sy);
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse!=null)
        {
            ox = mouse.getX();
            oy = mouse.getY();
        }
        for(int i=0; i<=width/2; i++)
            for(int j=0; j<=height/2; j++)
                lengths[i][j] = (double)1/Math.sqrt((Math.pow(i, 2)+Math.pow(j, 2))/90000+1);
        balls[0] = new Ball(new Vector(125.7692512031653, 0.0, 0.0), 1);
        balls[1] = new Ball(new Vector(162.8846256015827, 21.42857142857143, 0.0), 1);
        balls[2] = new Ball(new Vector(162.8846256015827, -21.42857142857143, 0.0), 1);
        balls[3] = new Ball(new Vector(200, 42.85714285714286, 0.0), 1);
        balls[4] = new Ball(new Vector(200, 0.0, 0.0) , 1);
        balls[5] = new Ball(new Vector(200, -42.85714285714286, 0.0), 1);
        balls[6] = new Ball(new Vector(237.1153743984174, 64.28571428571428, 0.0), 1);
        balls[7] = new Ball(new Vector(237.1153743984174, 21.42857142857143, 0.0), 0); 
        balls[8] = new Ball(new Vector(237.1153743984174, -21.42857142857143, 0.0), 2);
        balls[9] = new Ball(new Vector(237.1153743984174, -64.28571428571428, 0.0), 2);
        balls[10] = new Ball(new Vector(274.2307487968348, 85.71428571428572, 0.0), 2);
        balls[11] = new Ball(new Vector(274.2307487968348, 42.85714285714286, 0.0), 2);
        balls[12] = new Ball(new Vector(274.2307487968348, 0.0, 0.0), 2);
        balls[13] = new Ball(new Vector(274.2307487968348, -42.85714285714286, 0.0), 2);
        balls[14] = new Ball(new Vector(274.2307487968348, -85.71428571428572, 0.0), 2);
        planes[0] = new Plane(new Vector(0, 0, -21.4285714), new Vector(0, 0, 1), new Vector(0, 1, 0), 600.0, 800.0, readImage("grass.jpg"));
        System.arraycopy(balls, 0, renderables, 0, 15);
        System.arraycopy(planes, 0, renderables, 15, 1);
        setBackground(image);
        image.setColor(Color.WHITE);
        setLight(-60,-60);
        setCameraRot(0,0);
        setCameraLoc(0, 0, 100);
    }

    public void act()
    {   
        if (start==-1)
            start = new Date().getTime();
        robot.mouseMove((int)sx, (int)sy);
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse!=null)
        {
            mx=(ox-mouse.getX())/2+yaw;
            my=(oy-mouse.getY())/2+pitch;
            if(my>70)
                my=70;
            else if(my<-70)
                my=-70;
            setCameraRot(mx, my);
        }
        image.fill();
        for (int i=0; i<threads; i++)
        {
            raycasters[i] = new Raycaster(width/threads*i-width/2, width/threads*(i+1)-width/2);
            raycasters[i].start();
        }
        for (Raycaster raycaster: raycasters)
            try {raycaster.join();} catch(InterruptedException e) {}
        frames++;
        if (frames == 100)
        {
            System.out.println(new Date().getTime()-start);
        }
    }

    public void setCameraRot(double _yaw_, double _pitch_)
    {
        yaw = _yaw_;
        pitch = _pitch_;
        double yaw = Math.toRadians(_yaw_);
        double pitch = Math.toRadians(_pitch_);
        bdisplacement = new Vector(Math.cos(yaw)*Math.cos(pitch), Math.sin(yaw)*Math.cos(pitch), Math.sin(pitch));
        idisplacement = new Vector(Math.sin(yaw)/300.0, -Math.cos(yaw)/300.0, 0);
        jdisplacement = new Vector(-Math.cos(yaw)*Math.sin(pitch)/300, -Math.sin(yaw)*Math.sin(pitch)/300, Math.cos(pitch)/300.0);
    }

    public void setCameraLoc(double _x_, double _y_, double _z_)
    {
        camera = new Vector(_x_, _y_, _z_);
    }

    public void setLight(double _yaw_, double _pitch_)
    {
        double yaw = Math.toRadians(_yaw_);
        double pitch = Math.toRadians(_pitch_);
        light = new Vector(-Math.cos(pitch) * Math.cos(yaw), -Math.cos(pitch) * Math.sin(yaw), -Math.sin(pitch));
    }

    public Vector[][] readImage(String _img_)
    {
        GreenfootImage img = new GreenfootImage(_img_);
        int width = img.getWidth();
        int height = img.getHeight();
        Vector[][] end = new Vector[width][height];
        Color color;
        for (int i=0; i<width; i++)
            for (int j=0; j<height; j++)
            {
                color = img.getColorAt(i, j);
                end[i][j] = new Vector(color.getRed(), color.getGreen(), color.getBlue());
            }
        return end;
    }

    public class Raycaster extends Thread
    {
        public int lower, upper;

        public Raycaster(int _lower_, int _upper_)
        {
            super(_lower_+", "+_upper_);
            lower = _lower_;
            upper = _upper_;
        }

        public void run()
        {
            for(int i=lower; i<upper; i++)
                for(int j=-height/2; j<height/2; j++)
                    image.setColorAt(i+width/2, height/2-j-1, 
                        raycast(nullBall, camera, bdisplacement.add(jdisplacement.scale(j).add(idisplacement.scale(i))).scale(lengths[Math.abs(i)][Math.abs(j)]), -1).toColor());
        }

        private Vector raycast(Renderable _crenderable_, Vector _location_, Vector _vector_, int _reflect_)
        {
            double cdistance = Double.POSITIVE_INFINITY;
            Renderable crenderable = null;
            for(Renderable renderable: renderables)
            {
                if (renderable == _crenderable_)
                    continue;
                renderable.prepare(_location_, _vector_);
                if (renderable.willCollide())
                {
                    double distance = renderable.vectorDistance();
                    if(distance < cdistance)
                    {
                        cdistance = distance;
                        crenderable = renderable;
                    }
                }
            }
            if(cdistance == Double.POSITIVE_INFINITY)
                return new Vector(255,255,255);
            Vector location = _location_.add(_vector_.scale(cdistance));
            Vector normal = crenderable.normal(location);
            double brightness = normal.dotProduct(light);
            if (brightness<0)
                brightness=0;
            else
                for (Ball ball: balls)
                {
                    ball.prepare(location, light);
                    if (ball.willCollide())
                        brightness=0;
                }
            Vector color = crenderable.color(location).scale(brightness);
            if (_reflect_!=0 && crenderable.reflectiveness()!=0)
            {
                double n = 2*normal.dotProduct(_vector_);
                color = color.scale(1-crenderable.reflectiveness()).add(raycast(crenderable, location, _vector_.subtract(normal.scale(n)), _reflect_-1).scale(crenderable.reflectiveness()));
            }
            return color;
        }
    }
    
}
