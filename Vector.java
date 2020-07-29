import greenfoot.Color;

public class Vector  
{
    public double x, y, z;

    public Vector(double _x_, double _y_, double _z_)
    {
        x=_x_;
        y=_y_;
        z=_z_;
    }
    
    public Vector add(Vector _other_)
    {
        return new Vector(x+_other_.x, y+_other_.y, z+_other_.z);
    }
    
    public Vector subtract(Vector _other_)
    {
        return new Vector(x-_other_.x, y-_other_.y, z-_other_.z);
    }
    
    public Vector scale(double _scale_)
    {
        return new Vector(x*_scale_, y*_scale_, z*_scale_);
    }
    
    public Color toColor()
    {
        return new Color((int)x, (int)y, (int)z);
    }
    
    public double dotProduct(Vector _other_)
    {
        return x*_other_.x+y*_other_.y+z*_other_.z;
    }
    
    public Vector crossProduct(Vector _other_)
    {
        return new Vector(y*_other_.z-z*_other_.y, z*_other_.x-x*_other_.z, x*_other_.y-y*_other_.x);
    }
    
    public Vector transform(Vector _x_, Vector _y_, Vector _z_)
    {
        return new Vector(_x_.x*x+_y_.x*x+_z_.x*x,_x_.y*y+_y_.y*y+_z_.y*y, _x_.z*z+_y_.z*z+_z_.z*z);
    }
    
    public String toString()
    {
        return x+", "+y+", "+z;
    }
    
    public double size()
    {
        return Math.sqrt(x*x+y*y+z*z);
    }
}
