
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.image.BufferedImage;

public class Travel {

    private ArrayList<City> travel = new ArrayList<>();
    private PathIterator pathIterator;
    
    //private final double Dimension;

    public Travel(int numberOfCities) {
        for (int i = 0; i < numberOfCities; i++) {
            travel.add(new City());
        }
        pathIterator = new PathIterator(this);
        //Dimension=computeDimension(100);
    }
    
    public Travel(ArrayList<Integer> x, ArrayList<Integer> y){
        if(x.size()!=y.size()) throw new IllegalArgumentException("Sizes don't match!!!");
        for(int i=0;i<x.size();i++){
            travel.add(new City(x.get(i), y.get(i)));
        }
        pathIterator = new PathIterator(this);
        //Dimension=computeDimension(100);
    }

    protected ArrayList<City> getValue() {
        return travel;
    }

    public int getLength() {
        return travel.size();
    }

    public int getIndex(City city) {
        return travel.indexOf(city);
    }

    public Travel(ArrayList<City> travel) {
        this.travel = travel;
        //pathIterator=new PathIterator(this);
        //Dimension=computeDimension(100);
    }
    
    /*public double getDimension(){
        return Dimension;
    }*/

    public void generateInitialTravel() {
        if (travel.isEmpty()) {
            new Travel(10);
        }
        Collections.shuffle(travel);
    }

    public void getNextTravel() {
        travel = pathIterator.getNextTravel(this).getValue();
    }

    private int generateRandomIndex() {
        return (int) (Math.random() * travel.size());
    }

    public City getCity(int index) {
        return travel.get(index);
    }

    public double getDistance() {
        double distance = 0;
        for (int index = 0; index < travel.size(); index++) {
            City starting = getCity(index);
            City destination;

            if (index + 1 < travel.size()) {
                destination = getCity(index + 1);
            } else {
                destination = getCity(0);
            }
            distance += starting.distanceToCity(destination);
        }
        return distance;
    }

    public boolean equals(Travel other) {
        if (other.getLength() == getLength()) {
            for (int i = 0; i < other.getLength(); i++) {
                if (!other.getCity(i).equals(getCity(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
    
    /*private double computeDimension(int dim){
        BufferedImage refShape=new BufferedImage(dim,dim,BufferedImage.TYPE_INT_RGB);
        Graphics gTemp=refShape.createGraphics();
        gTemp.setColor(Color.WHITE);
        gTemp.fillRect(0,0,dim,dim);
        gTemp.setColor(Color.RED);
        gTemp.drawPolygon(scale(getAllX(),dim,Config.CoordinateX), scale(getAllY(),dim,Config.CoordinateY), getLength());
        
        int pixelcount=0;
        for(int i=0;i<dim;i++){
            for(int j=0;j<dim;j++){
                int color=refShape.getRGB(i,j);
                int red=(color & 0x00ff0000)>>16;
                int green=(color & 0x0000ff00)>>8;
                int blue=color & 0x000000ff;
                
                if(red==255){
                    pixelcount++;
                }
            }
        }
        gTemp.dispose();
        
        refShape=new BufferedImage(2*dim,2*dim,BufferedImage.TYPE_INT_RGB);
        gTemp=refShape.createGraphics();
        gTemp.setColor(Color.WHITE);
        gTemp.fillRect(0,0,2*dim,2*dim);
        gTemp.setColor(Color.RED);
        gTemp.drawPolygon(scale(getAllX(),2*dim,Config.CoordinateX), scale(getAllY(),2*dim, Config.CoordinateY), getLength());
        
        int pixelcountLater=0;
        for(int i=0;i<2*dim;i++){
            for(int j=0;j<2*dim;j++){
                int color=refShape.getRGB(i,j);
                int red=(color & 0x00ff0000)>>16;
                int green=(color & 0x0000ff00)>>8;
                int blue=color & 0x000000ff;
                
                if(red==255){
                    pixelcountLater++;
                }
            }
        }
        gTemp.dispose();
        
        return pixelcountLater/pixelcount;
    }
    
    private int[] scale(int[] input, int presentDim, int pastDim){
        int[] temp=new int[input.length];
        for(int i=0;i<input.length;i++){
            temp[i]=input[i]*presentDim/pastDim;
        }
        return temp;
    }*/
    
    public double getArea() {
        if (!hasIntersection()) {
            double area = 0;
            int j = travel.size() - 1;
            for (int index = 0; index < travel.size(); index++) {

                City starting = getCity(index);
                City destination = getCity(j);

                j = index;

                area += starting.areaUnderLine(destination);
            }
            return Math.abs(area);
        } else {
            return 0;
        }
    }

    private boolean hasIntersection() {
        if (travel == null || travel.size() < 4) {
            return false;
        }
        for (int i = 0; i < travel.size() - 1; i++) {
            for (int j = i + 2; j < travel.size(); j++) {
                if ((i == 0) && (j == (travel.size() - 1))) {
                    continue;
                }
                boolean cut = Line2D.linesIntersect(
                        getCity(i).getX(),
                        getCity(i).getY(),
                        getCity(i + 1).getX(),
                        getCity(i + 1).getY(),
                        getCity(j).getX(),
                        getCity(j).getY(),
                        getCity((j + 1) % travel.size()).getX(),
                        getCity((j + 1) % travel.size()).getY());
                
                if(cut){
                    return true;
                }
            }
        }
        return false;
    }

    public double getAnglesSum() {
        double angle = 0;

        for (int i = 0; i < travel.size(); i++) {
            if (i > 0 && i < travel.size() - 1) {
                angle += travel.get(i).getAcuteAngleBetween(travel.get(i - 1), travel.get(i + 1));
            } else if (i == 0) {
                angle += travel.get(i).getAcuteAngleBetween(travel.get(travel.size() - 1), travel.get(1));
            } else {
                angle += travel.get(i).getAcuteAngleBetween(travel.get(travel.size() - 2), travel.get(0));
            }
        }

        return angle;
    }

    public String printroute() {
        String route = "";
        for (int i = 0; i < travel.size(); i++) {
            route += ("(" + travel.get(i).getX() + "," + travel.get(i).getY() + ")" + ((i == travel.size() - 1) ? "" : ","));
        }
        return route + ";";
    }

    public int[] getAllX() {
        int[] x = new int[travel.size()];
        for (int i = 0; i < travel.size(); i++) {
            x[i] = travel.get(i).getX();
        }
        return x;
    }

    public int[] getAllY() {
        int[] y = new int[travel.size()];
        for (int i = 0; i < travel.size(); i++) {
            y[i] = travel.get(i).getY();
        }
        return y;
    }
}
