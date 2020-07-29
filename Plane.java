import greenfoot.*;

public class Plane implements Renderable
{
    private Vector normal, up, cross, collide, point, dimentions;
    private Vector[][] img;
    private double distance;
    public Vector location;

    public Plane(Vector _location_, Vector _normal_, Vector _up_, double _height_, double _width_, Vector[][] _img_)
    {
        location = _location_; 
        normal = _normal_; 
        up = _up_;
        cross = _up_.crossProduct(_normal_);
        dimentions = new Vector(_height_, _width_, 0.0);
        img = _img_;
    }

    @Override
    public void prepare(Vector _location_, Vector _vector_)
    {
        double b = _vector_.dotProduct(normal);
        if (b>=0)
        {
            collide = null;
            point = null;
            return ;
        }
        distance = location.subtract(_location_).dotProduct(normal)/b;
        collide = _location_.add(_vector_.scale(distance));
        point = collide.transform(normal, up, cross);
    }

    @Override
    public boolean willCollide()
    {
        if (collide == null)
        {
            return false;
        }
        return((collide.x>-dimentions.x/2) & (collide.y>-dimentions.y/2) & (collide.x<dimentions.x/2) & (collide.y<dimentions.y/2));
    }

    @Override
    public double vectorDistance()
    {
        return distance;
    }

    @Override
    public Vector collision()
    {
        return collide;
    }

    @Override
    public Vector normal(Vector _location_)
    {
        return normal;
    }

    @Override
    public Vector color(Vector _location_)
    {
        return img[(int)(point.x+dimentions.x/2)%img.length][(int)(point.y+dimentions.y/2)%img[0].length];
    }
    
    @Override
    public double reflectiveness()
    {
        return 0;
    }
}
