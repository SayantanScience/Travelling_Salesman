/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sushanta
 */
public class PathIterator {

    private  Scanner scanner;
    private  PrintWriter writer;
    private  File logFile;
    private  Travel initialTravel;

    public PathIterator(Travel initialTravel) {
        try {
            logFile = new File("log.txt");
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            scanner = new Scanner(logFile);
            writer = new PrintWriter(new FileWriter(logFile, true));
            this.initialTravel = initialTravel;

            RandomAccessFile raf = new RandomAccessFile(logFile, "rw");
            raf.setLength(0);

            writeAllTravels(initialTravel);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        } finally {
            writer.close();
        }
    }

    /*
    public ArrayList<Travel> generateAllPossiblePaths(Travel travel) {
        final int N = travel.getLength();

        ArrayList<City> path = new ArrayList<>();

        ArrayList<int[]> permutations = new ArrayList<>();
        int[] permutation = new int[N];

        for (int i = 0; i < N; i++) {
            permutation[i] = 0;
        }
        permutations.add(permutation);

        int i = 0;
        while (i < N) {
            if (permutation[i] < i) {
                int a = (i % 2 == 0) ? 0 : permutation[i];
                int tmp = permutation[a];
                permutation[a] = permutation[i];
                permutation[i] = tmp;
                permutations.add(permutation);
                permutation[i]++;
                i = 0;
            } else {
                permutation[i] = 0;
                i++;
            }
        }

        for (int[] travelPath : permutations) {
            for (int index : travelPath) {
                path.add(travel.getCity(index));
            }
            if (!isIncluded(path)) {
                paths.add(new Travel(path));
            }
        }
        return paths;
    }*/

 /*private boolean isIncluded(ArrayList<City> path) {
        ArrayList<City> temp = new ArrayList<>();
        ArrayList<Travel> tempList = new ArrayList<>();

        for (int i = 0; i < path.size(); i++) {
            for (int j = 0; j < path.size(); j++) {
                if (i + j < path.size()) {
                    temp.add(path.get(i + j));
                } else {
                    temp.add(path.get(i + j - path.size()));
                }
                tempList.add(new Travel(temp));
                temp=revertOrientation(temp);
                tempList.add(new Travel(temp));
            }
        }

        
        return false;
    }*/
 /*private ArrayList<City> revertOrientation(ArrayList<City> path){
        ArrayList<City> tmp=new ArrayList<>();
        for(int i=path.size()-1;i>=0;i--){
            tmp.add(path.get(i));
        }
        return tmp;
    }*/
    public  Travel getNextTravel(Travel previousTravel) {
        try {
            scanner = new Scanner(logFile);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        boolean gotIt = false;
        StringBuilder builder = new StringBuilder();

        while (scanner.hasNextLine()) {
            
            builder.append(scanner.nextLine());
            
            if (!gotIt) {
                if (toTravel(builder.toString()).equals(previousTravel)) {
                    gotIt = true;
                    builder = new StringBuilder();
                }else{
                    gotIt=false;
                    builder = new StringBuilder();
                }
            } else {
                scanner.close();
                return toTravel(builder.toString());
            }
            
        }
        
        scanner.close();
        return null;
    }

    private  Travel toTravel(String string) {
        ArrayList<City> travel = new ArrayList<>();
        for (int i = 2; i <= string.length(); i += 2) {
            travel.add(initialTravel.getCity(Integer.parseInt(string.substring(i - 2, i))));
        }
        return new Travel(travel);
    }

    private  int index = 0;

    private  void writeAllTravels(Travel initialTravel) {

        final int N = initialTravel.getLength() - 1;

        int[] permutation = new int[N];
        System.out.println("Got travel #\n");
        for (int i = 0; i < N; i++) {
            permutation[i] = i + 1;
        }

        int[] indexes = new int[N];
        for (int i = 0; i < N; i++) {
            indexes[i] = 0;
        }

        write(permutation);

        int i = 0;
        while (i < N) {
            if (indexes[i] < i) {
                swap(permutation, i % 2 == 0 ? 0 : indexes[i], i);
                write(permutation);
                indexes[i]++;
                i = 0;
            } else {
                indexes[i] = 0;
                i++;
            }
        }

        writer.close();
    }

    private  void swap(int[] input, int a, int b) {
        int tmp = input[a];
        input[a] = input[b];
        input[b] = tmp;
    }

    private  void write(int[] path) {
        int[] permutation = new int[path.length + 1];
        permutation[0] = 0;
        for (int i = 1; i < permutation.length; i++) {
            permutation[i] = path[i - 1];
        }
        if (permutation[1] < permutation[permutation.length - 1]) {
            String str = "";
            for (int point : permutation) {
                if (point < 10) {
                    str += "0" + point;
                } else {
                    str += point;
                }
            }
            str += "\n";
            writer.print(str);
            index++;
            //if(index%100==0) System.out.print("\r" + index);
        }
    }

}
