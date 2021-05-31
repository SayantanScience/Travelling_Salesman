public final class City{
    private int x,y;
    
    public City(){
        this.x=(int)(Math.random()*Config.CoordinateX);
        this.y=(int)(Math.random()*Config.CoordinateY);
    }
    
    public City(int x, int y){
        this.x=x;
        this.y=y;
    }
    
    public double distanceToCity(City city){
        int diffx=Math.abs(getX()-city.getX());
        int diffy=Math.abs(getY()-city.getY());
        return Math.sqrt(Math.pow(diffx, 2)+Math.pow(diffy, 2));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public double areaUnderLine(City city){
        double area;
        area=((city.getX()+getX())*(city.getY()-getY())/2);
        return area;
    }
    
    public double getAcuteAngleBetween(City city1, City city2){
        double angle1=Math.atan2(city1.getY()-getY(), city1.getX()-getX());
        double angle2=Math.atan2(city2.getY()-getY(), city2.getX()-getX());
        
        double angle=Math.abs(angle1+angle2);
        if(angle>Math.PI){
            return 2*Math.PI-angle;
        }else{
            return angle;
        }
    }
    
    public boolean equals(City other){
        return other.getX()==getX() && other.getY()==getY();
    }
}