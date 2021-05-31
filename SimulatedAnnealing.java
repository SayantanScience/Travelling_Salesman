
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

public class SimulatedAnnealing extends JFrame {

    private static JPanel panelDist, panelAre, panel[];
    private static JProgressBar progress;
    private static JLabel labelDist, labelAre, bottomLabel;

    private static Polygon polyDist, polyAre;

    private static int noOfCities, numberOfIterations;

    private static Scanner scanner;

    private static Travel travel;
    private static int i;

    private static final long serialVersionUID = 1L;

    public SimulatedAnnealing() {
        try {
            System.out.println(simulateAnnealing(new Travel(Config.NUMBER_OF_CITIES)));
            Thread.sleep(10000);
            System.exit(0);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    public SimulatedAnnealing(ArrayList<Integer> x, ArrayList<Integer> y){
        try {
            System.out.println(simulateAnnealing(new Travel(x,y)));
            Thread.sleep(10000);
            System.exit(0);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private final int factorial(int n) {
        int F = 1;
        while (n > 0) {
            F *= n;
            n--;
        }
        return F;
    }

    public final String simulateAnnealing(Travel travela) throws InterruptedException {

        try {
            scanner = new Scanner(new File("log.txt"));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        
        noOfCities = travela.getLength();
        numberOfIterations = factorial(noOfCities - 1) / 2;
        int delayMillis = Config.DELAY_MILLIS;

        travel = travela;

        new PathIterator(travel);

        System.out.println("Starting SA with temperature: # of iterations: " + numberOfIterations);

        travel.generateInitialTravel();

        Travel bestRouteforDistance = travel, bestRouteforArea = travel;

        double bestDistance = travel.getDistance();
        System.out.println("Initial distance of travel: " + bestDistance);

        Travel currentSolution = travel;

        double bestArea = travel.getArea();
        System.out.println("Initial Area of Travel Path: " + bestArea);

        ArrayList<Travel> bestAreas, bestDistances;
        bestAreas = new ArrayList<>();
        bestDistances = new ArrayList<>();

        setupCanvus();
        drawTravelArea(currentSolution);
        drawTravelDistance(currentSolution);

        Thread.sleep(2000);

        for (i = 0; i < numberOfIterations; i++) {

            drawTravelDistance(currentSolution);
            drawTravelArea(currentSolution);

            double currentDistance = currentSolution.getDistance();
            double currentArea = currentSolution.getArea();

            if (currentDistance < bestDistance) {
                bestDistance = currentDistance;
                bestRouteforDistance = currentSolution;
                bestDistances.clear();
                bestDistances.add(currentSolution);
            } else if (currentDistance == bestDistance) {
                bestDistances.add(currentSolution);
            }

            if (currentArea > bestArea) {
                bestArea = currentArea;
                bestRouteforArea = currentSolution;
                bestAreas.clear();
                bestAreas.add(currentSolution);
            } else if (currentArea == bestArea) {
                bestAreas.add(currentSolution);
            }

            Thread.sleep(delayMillis);

            if (i % 100 == 0 || i == numberOfIterations - 1) {
                System.out.println("Iteration #" + i);
            }

            float c = i * 100 / numberOfIterations;
            progress.setValue(i);
            progress.setString(Math.round(c) + "%");
            bottomLabel.repaint();
            repaint();

            currentSolution = getNextTravel();
        }

        progress.setValue(numberOfIterations);
        progress.setString("Completed");
        repaint();

        for (Travel trav : bestDistances) {
            drawTravelDistance(trav);
            Thread.sleep(Config.DELAY_IN_SAME);
            if (trav.getArea() > bestRouteforDistance.getArea()) {
                bestRouteforDistance = trav;
            }
        }

        for (Travel trav : bestAreas) {
            drawTravelArea(trav);
            Thread.sleep(Config.DELAY_IN_SAME);
            if (trav.getDistance() < bestRouteforArea.getDistance()) {
                bestRouteforArea = trav;
            }
        }

        drawTravelDistance(bestRouteforDistance);
        drawTravelArea(bestRouteforArea);

        return (bestRouteforArea == bestRouteforDistance) + "\n" + bestRouteforArea.printroute() + "\n" + bestRouteforDistance.printroute() + "\n\nBest Dimension=" + bestArea + "\twith Distance " + bestRouteforArea.getDistance() + "\nBest Distance=" + bestDistance + "\twith Dimension " + bestRouteforDistance.getArea();
    }

    private Travel getNextTravel() {
        return toTravel(scanner.next());
    }

    private Travel toTravel(String string) {
        ArrayList<City> travel = new ArrayList<>();
        for (int i = 2; i <= string.length(); i += 2) {
            travel.add(this.travel.getCity(Integer.parseInt(string.substring(i - 2, i))));
        }
        return new Travel(travel);
    }

    private void drawTravelDistance(Travel travel) {
        int[] x = travel.getAllX(), y = travel.getAllY();
        polyDist = new Polygon(x, y, noOfCities);
        labelDist.setText("Distance=" + travel.getDistance() + "                                                 Area=" + travel.getArea());
        bottomLabel.setText("Created by: Sayantan Chakraborty Iteration #" + i);
        panelDist.repaint();
        bottomLabel.repaint();
        repaint();
    }

    private void drawTravelArea(Travel travel) {
        int[] x = travel.getAllX(), y = travel.getAllY();
        polyAre = new Polygon(x, y, noOfCities);
        labelAre.setText("Area=" + travel.getArea() + "                                                     Distance=" + travel.getDistance());
        panelAre.repaint();
        bottomLabel.repaint();
        bottomLabel.setText("Created by: Sayantan Chakraborty Iteration #" + i);
        repaint();
    }

    private void setupCanvus() {

        setNativeLookAndFeel();
        setTitle("Annealing Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container content = getContentPane();
        content.setLayout(new BorderLayout());

        panel = new JPanel[3];
        panel[0] = new JPanel(new BorderLayout());
        panel[1] = new JPanel(new BorderLayout());
        panel[2] = new JPanel(new GridLayout(1, 2));
        panel[0].setBorder(BorderFactory.createCompoundBorder());
        panel[1].setBorder(BorderFactory.createCompoundBorder());
        panel[2].setBorder(BorderFactory.createCompoundBorder());

        panelDist = new JPanel(new BorderLayout()) {
            @Override
            public void paint(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.RED);
                g.drawPolygon(polyDist);
            }
        };
        panelDist.setBorder(BorderFactory.createEtchedBorder());

        panelAre = new JPanel(new BorderLayout()) {
            @Override
            public void paint(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.RED);
                g.drawPolygon(polyAre);
            }
        };
        panelAre.setBorder(BorderFactory.createEtchedBorder());

        labelDist = new JLabel();
        labelAre = new JLabel();

        progress = new JProgressBar();
        progress.setMaximum(numberOfIterations);
        progress.setMinimum(0);
        progress.setStringPainted(true);

        panel[0].add(labelDist, BorderLayout.NORTH);
        panel[0].add(panelDist, BorderLayout.CENTER);

        panel[1].add(labelAre, BorderLayout.NORTH);
        panel[1].add(panelAre, BorderLayout.CENTER);

        panel[2].add(panel[0]);
        panel[2].add(panel[1]);

        content.add(progress, BorderLayout.NORTH);
        content.add(panel[2]);
        content.add(bottomLabel = new JLabel("Created by: Sayantan Chakraborty Iteration #" + i), BorderLayout.SOUTH);
        
        GraphicsEnvironment env=GraphicsEnvironment.getLocalGraphicsEnvironment();
        setMaximizedBounds(env.getMaximumWindowBounds());
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        pack();
        setVisible(true);
    }

    private void setNativeLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.out.println("Error setting native LAF: " + e);
        }
    }
}
