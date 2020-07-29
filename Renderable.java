public interface Renderable  
{
    public double reflectiveness=0;
    
    public void prepare(Vector location, Vector vector);
    public boolean willCollide();
    public double vectorDistance();
    public Vector collision();
    public Vector normal(Vector location);
    public Vector color(Vector location);
    public double reflectiveness();
}
