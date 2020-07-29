
public class Ball implements Renderable
{
    private double radius=21.4285714;
    private double dot_product, distance, length;
    private Vector collide;
    
    public int affliation;
    public Vector location;

    public Ball(Vector _location_ , int _affilation_)
    {
        location = _location_;
        affliation = _affilation_;
    }

    @Override
    public void prepare(Vector _location_, Vector _vector_)
    {
        length = Math.pow(location.x-_location_.x, 2)+Math.pow(location.y-_location_.y, 2)+Math.pow(location.z-_location_.z, 2);
        dot_product = location.subtract(_location_).dotProduct(_vector_);
        distance = dot_product-Math.sqrt(dot_product*dot_product+radius*radius-length);
        collide = _location_.add(_vector_.scale(distance));
    }

    @Override
    public boolean willCollide()
    {
        return (dot_product>0&&length<=radius*radius+dot_product*dot_product);
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
        return _location_.subtract(location).scale(1.0/radius);
    }

    @Override
    public Vector color(Vector _location_)
    {
        if (affliation == 1)
            return new Vector(20, 20, 240);
        else if (affliation == 2)
            return new Vector(240, 20, 20);
        else
            return new Vector(20, 240, 20);
    }
    
    @Override
    public double reflectiveness()
    {
        return .15;
    }
}
