package des;

/**
 * Console
 *
 * @author Vincent Cheong
 * Student ID: 004299384
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Console {
    public Console() {
        Scanner s = new Scanner(System.in);
        menu();

        while(true) {
            String line = s.nextLine();
            line.replaceAll("\\s+","");

            if ("1".equals(line)) {
                System.out.println("[Vehicle]");
                System.out.print("Vehicle Capacity = ");
                int cap = s.nextInt();
                System.out.print("Location x = ");
                int x = s.nextInt();
                System.out.print("Location y = ");
                int y = s.nextInt();
                Vehicle v = new Vehicle(new Location(x, y), cap, 0);

                System.out.println("\n[Reservation]");
                System.out.print("Pickup x = ");
                int src_x = s.nextInt();
                System.out.print("Pickup y = ");
                int src_y = s.nextInt();
                System.out.print("Dropoff x = ");
                int dest_x = s.nextInt();
                System.out.print("Dropoff y = ");
                int dest_y = s.nextInt();
                System.out.print("Reservation size = ");
                int size = s.nextInt();
                System.out.print("Reservation time = ");
                double time = s.nextDouble();
                System.out.print("Carpools? (y/n) = ");
                String input = s.next();
                boolean carpools = input.equalsIgnoreCase("y") ? true : false;
                Party p = new Party(size, new Location(src_x, src_y), new Location(dest_x, dest_y), time, carpools, 0);

                boolean again = true;
                List<Party> stops = new ArrayList<>();
                while (again) {
                    System.out.println("\n[Potential Stop]");
                    System.out.print("Location x = ");
                    x = s.nextInt();
                    System.out.print("Location y = ");
                    y = s.nextInt();
                    System.out.print("Time = ");
                    time = s.nextDouble();
                    stops.add(new Party(0, new Location(x, y), new Location(x, y), time, true, 0));

                    System.out.print("Again? (y/n) = ");
                    input = s.next();
                    again = input.equalsIgnoreCase("y") ? true : false;
                }

                //System.out.print("Verbose? (y/n) = ");
                //input = s.next();
                //boolean verbose = input.equalsIgnoreCase("y") ? true : false;

                Lynx l = new Lynx(v, p, stops, true);
                //Lynx l = new Lynx(v, p, stops, verbose);
            } else if ("2".equals(line)) {
                System.out.print("Numbers of Drivers = ");
                int num = s.nextInt();
                System.out.print("Number of Reservations = ");
                int num2 = s.nextInt();
                Lynx l = new Lynx(num, num2, true);
            } else if ("3".equals(line)) {
                int n = 1000;
                double z = 1.96;

                // Test for 45 drivers
                int num = 45;
                double[] results = new double[n];
                for(int i = 0; i < n; i++) {
                    Lynx l = new Lynx(num, false);
                    results[i] = (double) l.free_riders/l.total_riders;
                }
                double mean = mean(results);
                double sd = sd(results, mean);
                System.out.println("45 Driver Left Bound Confidence: " + (mean - z*sd/Math.sqrt(n))*100);

                // Test for 47 drivers
                num = 47;
                results = new double[n];
                for(int i = 0; i < n; i++) {
                    Lynx l = new Lynx(num, false);
                    results[i] = (double) l.free_riders/l.total_riders;
                }
                mean = mean(results);
                sd = sd(results, mean);
                System.out.println("47 Driver Left Bound Confidence: " + (mean - z*sd/Math.sqrt(n))*100);

                // Test for 48 drivers
                num = 48;
                results = new double[n];
                for(int i = 0; i < n; i++) {
                    Lynx l = new Lynx(num, false);
                    results[i] = (double) l.free_riders/l.total_riders;
                }
                mean = mean(results);
                sd = sd(results, mean);
                System.out.println("48 Driver Left Bound Confidence: " + (mean - z*sd/Math.sqrt(n))*100);

                // Test for 50 drivers
                num = 50;
                results = new double[n];
                for(int i = 0; i < n; i++) {
                    Lynx l = new Lynx(num, false);
                    results[i] = (double) l.free_riders/l.total_riders;
                }
                mean = mean(results);
                sd = sd(results, mean);
                System.out.println("50 Driver Left Bound Confidence: " + (mean - z*sd/Math.sqrt(n))*100);
                System.out.println("Done.");
            } else if ("menu".equalsIgnoreCase(line)) {
                menu();
            } else if ("quit".equalsIgnoreCase(line)) {
                return;
            } else {
                System.out.println();
                menu();
            }
        }
    }

    private void menu() {
        System.out.println("----Discrete-Event Simulation of Ride Sharing - Assignment 3----");
        System.out.println(" 1 | Single Vehicle Simulation");
        System.out.println(" 2 | 2 Hour Simulation");
        System.out.println(" 3 | Solution to Lynx Question");
        System.out.println(" menu | Print Menu");
        System.out.println(" quit | Quit");
        System.out.println("----------------------------------------------------------------");
    }

    private double mean(double[] results) {
        double sum = 0;
        for(int i = 0; i < results.length; i++) {
            sum += results[i];
        }
        return (double) sum/results.length;
    }

    private double sd(double[] results, double mean) {
        double sum = 0;
        for(int i = 0; i < results.length; i++) {
            sum += Math.pow((results[i] - mean), 2);
        }
        return Math.sqrt((sum/results.length));
    }
}
