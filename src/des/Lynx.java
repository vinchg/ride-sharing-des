package des;

import des.event.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * Lynx
 *
 * @author Vincent Cheong
 * Student ID: 004299384
 */

public class Lynx {
    PriorityQueue<Event> pq = new PriorityQueue<>();
    double time = 0; // System clock, in minutes
    final double MAX_PERIOD = 120; // 120 mins
    Random r = new Random();
    final double[][] PARTY_PROBABILITY = {{0.60, 0.50}, {0.25, 0.65}, {0.10, 0.45}, {0.05, 0.30}}; // Column 1: Reservations, Column 2: Carpools
    final double[] VEHICLE_CAPACITY_PROBABLITY = {0.05, 0.05, 0.40, 0.30, 0.15, 0.05};
    final int[] AVENUE_INDEX = { 0, 1, 2, 4, 5, 6, 8, 9, 10, 12, 13, 14, 16, 17, 18};
    List<Party> r_list = new ArrayList<>();  // Reservation list
    List<Vehicle> v_list = new ArrayList<>();  // Vehicle list
    Map m;
    boolean verbose = false;
    boolean visual = false;
    int free_reservations = 0;
    int total_riders = 0;
    int free_riders = 0;

    PrintWriter r_writer = null;
    PrintWriter d_writer = null;
    PrintWriter e_writer = null;
    StringBuffer[] s_buffer = null;
    int vid = 0;
    int pid = 0;


    public Lynx() {
    }


    /**
     * Single Vehicle Simulation (1)
     */
    public Lynx(Vehicle v, Party p, List<Party> stops, boolean input_visual) {
        visual = input_visual;
        v_list.add(v);
        pq.add(new ReservationEvent(p.request_time, p));

        //pq.add(new ReservationAssignmentEvent(0, p, v));

        for(Party temp: stops) {
            pq.add(new ReservationEvent(temp.request_time, temp));
        }

        execute();
        System.out.println("Total Elapsed Time: " + time);
        System.out.println("Intersections Visited: " + v.intersections_visited);
    }


    /**
     * Multiple Vehicles Simulation for 2 Hr Period (2)
     */
    public Lynx(int num_drivers, int num_reservations, boolean input_verbose) {
        verbose = input_verbose;

        // Generate vehicles and vehicle capacity
        for(int i = 0; i < num_drivers; i++) {
            double prob = r.nextDouble();
            int vehicle_capacity;
            if (prob <= 0.05)
                vehicle_capacity = 1;
            else if (prob <= 0.1)
                vehicle_capacity = 2;
            else if (prob <= 0.5)
                vehicle_capacity = 3;
            else if (prob <= 0.8)
                vehicle_capacity = 4;
            else if (prob <= 0.95)
                vehicle_capacity = 5;
            else
                vehicle_capacity = 6;

            v_list.add(new Vehicle(new Location(r.nextInt(20), r.nextInt(20)), vehicle_capacity, vid));
            vid++;
        }

        if (verbose) {
            System.out.println();
            try {
                Party p;
                r_writer = new PrintWriter("reservations.txt", "UTF-8");
                d_writer = new PrintWriter("drivers.txt", "UTF-8");
                /*for(Vehicle v : v_list) {
                    d_writer.println(v.vid + ", " + v.MAX_CAPACITY);
                }
                d_writer.println();*/
                e_writer = new PrintWriter("events.txt", "UTF-8");
                s_buffer = new StringBuffer[v_list.size()];
                for(int i = 0; i < s_buffer.length; i++) {
                    s_buffer[i] = new StringBuffer();
                }
                for(Vehicle v : v_list) {
                    s_buffer[v.vid].append("VID: " + v.vid + ", Capacity: " + v.MAX_CAPACITY + '\n');
                }
            } catch (Exception excep) {
                // Do nothing
            }
        }

        double temp_t = 0;
        double temp = getExp();
        for(int i = 0; i < num_reservations; i++) {
            // Generate time with exponential distribution
            while (temp <= 0) {
                temp = getExp();
            }
            temp_t += getExp();

            if (temp_t > MAX_PERIOD) // Stop when we go over the 2 hrs
                break;

            int party_number;
            boolean carpools;
            double prob = r.nextDouble();
            if (prob <= 0.6) {
                party_number = 1;
                prob = r.nextDouble();
                carpools = prob <= 0.5 ? true : false;
            } else if (prob <= 0.85) {
                party_number = 2;
                prob = r.nextDouble();
                carpools = prob <= 0.65 ? true : false;
            } else if (prob <= 0.95) {
                party_number = 3;
                prob = r.nextDouble();
                carpools = prob <= 0.45 ? true : false;
            } else {
                party_number = 4;
                prob = r.nextDouble();
                carpools = prob <= 0.3 ? true : false;
            }

            prob = r.nextDouble();
            int s = r.nextInt(20);
            int a;
            if (prob <= 0.75) {
                a = 4*(r.nextInt(5) + 1) - 1;
            } else {
                a = AVENUE_INDEX[r.nextInt(15)];
            }

            Party ran_party = new Party(party_number,
                    new Location(r.nextInt(20), r.nextInt(20)),
                    new Location(s, a),
                    temp_t,
                    carpools,
                    pid);

            pq.add(new ReservationEvent(temp_t, ran_party));
            pid++;
        }

        execute();

        if (verbose) {
            System.out.println("Total Reservations: " + num_reservations);
            System.out.println("Free Reservations: " + free_reservations);
            System.out.println("Total Riders: " + total_riders);
            System.out.println("Percentage Free Riders: " + (double) free_riders/total_riders*100);

            /*e_writer.println();
            e_writer.println("Total Reservations: " + num_reservations);
            e_writer.println("Free Reservations: " + free_reservations);
            e_writer.println("Total Riders: " + total_riders);
            e_writer.println("Percentage Free Riders: " + (double) free_riders/total_riders*100);*/
        }
    }

    /**
     * Multiple Vehicles Simulation for 2 Hr Period (3)
     */
    public Lynx(int num_drivers, boolean input_verbose) {
        verbose = input_verbose;

        // Generate vehicles and vehicle capacity
        for(int i = 0; i < num_drivers; i++) {
            double prob = r.nextDouble();
            int vehicle_capacity;
            if (prob <= 0.05)
                vehicle_capacity = 1;
            else if (prob <= 0.1)
                vehicle_capacity = 2;
            else if (prob <= 0.5)
                vehicle_capacity = 3;
            else if (prob <= 0.8)
                vehicle_capacity = 4;
            else if (prob <= 0.95)
                vehicle_capacity = 5;
            else
                vehicle_capacity = 6;

            v_list.add(new Vehicle(new Location(r.nextInt(20), r.nextInt(20)), vehicle_capacity, vid));
            vid++;
        }

        if (verbose) {
            System.out.println();
            try {
                Party p;
                r_writer = new PrintWriter("reservations.txt", "UTF-8");
                d_writer = new PrintWriter("drivers.txt", "UTF-8");
                /*for(Vehicle v : v_list) {
                    d_writer.println(v.vid + ", " + v.MAX_CAPACITY);
                }
                d_writer.println();*/
                e_writer = new PrintWriter("events.txt", "UTF-8");
                s_buffer = new StringBuffer[v_list.size()];
                for(int i = 0; i < s_buffer.length; i++) {
                    s_buffer[i] = new StringBuffer();
                }
                for(Vehicle v : v_list) {
                    s_buffer[v.vid].append("VID: " + v.vid + ", Capacity: " + v.MAX_CAPACITY + '\n');
                }
            } catch (Exception excep) {
                // Do nothing
            }
        }


        double temp_t = 0;
        double temp = getExp();
        int num_reservations = 0;
        while (true) {
            // Generate time with exponential distribution
            while (temp <= 0) {
                temp = getExp();
            }
            temp_t += getExp();

            if (temp_t > MAX_PERIOD) // Stop when we go over the 2 hrs
                break;

            int party_number;
            boolean carpools;
            double prob = r.nextDouble();
            if (prob <= 0.6) {
                party_number = 1;
                prob = r.nextDouble();
                carpools = prob <= 0.5 ? true : false;
            } else if (prob <= 0.85) {
                party_number = 2;
                prob = r.nextDouble();
                carpools = prob <= 0.65 ? true : false;
            } else if (prob <= 0.95) {
                party_number = 3;
                prob = r.nextDouble();
                carpools = prob <= 0.45 ? true : false;
            } else {
                party_number = 4;
                prob = r.nextDouble();
                carpools = prob <= 0.3 ? true : false;
            }

            prob = r.nextDouble();
            int s = r.nextInt(20);
            int a;
            if (prob <= 0.75) {
                a = 4*(r.nextInt(5) + 1) - 1;
            } else {
                a = AVENUE_INDEX[r.nextInt(15)];
            }

            Party ran_party = new Party(party_number,
                    new Location(r.nextInt(20), r.nextInt(20)),
                    new Location(s, a),
                    temp_t,
                    carpools,
                    pid);

            pq.add(new ReservationEvent(temp_t, ran_party));
            pid++;
            num_reservations++;
        }

        execute();

        if (verbose) {
            System.out.println("Total Reservations: " + num_reservations);
            System.out.println("Free Reservations: " + free_reservations);
            System.out.println("Total Riders: " + total_riders);
            System.out.println("Percentage Free Riders: " + (double) free_riders/total_riders*100);

            /*e_writer.println();
            e_writer.println("Total Reservations: " + num_reservations);
            e_writer.println("Free Reservations: " + free_reservations);
            e_writer.println("Total Riders: " + total_riders);
            e_writer.println("Percentage Free Riders: " + (double) free_riders/total_riders*100);*/
        }
    }


    // Reservation type
    // 0: Reservation, 1: Reservation Assignment, 2: Intersection Arrival, 3: Pick Up, 4: Drop Off, 5: Idle Arrival
    private void execute() {
        /*if (verbose) {
            System.out.println();
            try {
                Party p;
                r_writer = new PrintWriter("reservations.txt", "UTF-8");
                d_writer = new PrintWriter("drivers.txt", "UTF-8");
                for(Vehicle v : v_list) {
                    d_writer.println(v.vid + ", " + v.MAX_CAPACITY);
                }
                d_writer.println();
                e_writer = new PrintWriter("events.txt", "UTF-8");
                s_buffer = new StringBuffer[v_list.size()];
                for(StringBuffer s : s_buffer) {
                    s = new StringBuffer();
                }
                for(Vehicle v : v_list) {
                    s_buffer[v.vid].append(v.vid + ", " + v.MAX_CAPACITY + "\n");
                }
            } catch (Exception excep) {
                // Do nothing
            }
        }*/
        Event e;
        while(!pq.isEmpty()) {
            e = pq.poll();
            time = e.time();
            switch(e.type()) {
                case 0:
                    if (visual)
                        System.out.print("Reservation | ");
                    if (verbose)
                        e_writer.println(time + ", Handle Reservation, Party " + e.party().pid);
                    handleReservation((ReservationEvent) e);
                    break;
                case 1:
                    if (visual)
                        System.out.print("Reservation Assignment | ");
                    if (verbose)
                        e_writer.println(time + ", Reservation Assignment, Party " + e.party().pid + ", Vehicle " + e.vehicle().vid);
                    handleReservationAssignment((ReservationAssignmentEvent) e);
                    break;
                case 2:
                    if (visual)
                        System.out.print("Intersection Arrival | ");
                    if (verbose)
                        e_writer.println(time + ", Intersection Arrival, Vehicle " + e.vehicle().vid);
                    handleIntersectionArrival((IntersectionArrivalEvent) e);
                    break;
                case 3:
                    if (visual)
                        System.out.print("Pick Up | ");
                    if (verbose)
                        e_writer.println(time + ", Pick Up, Party " + e.party().pid + ", Vehicle " + e.vehicle().vid);
                    handlePickUp((PickUpEvent) e);
                    break;
                case 4:
                    if (visual)
                        System.out.print("Drop Off | ");
                    if (verbose)
                        e_writer.println(time + ", Drop Off, Vehicle " + e.vehicle().vid);
                    handleDropOff((DropOffEvent) e);
                    break;
                case 5:
                    if (visual)
                        System.out.print("Idle | ");
                    if (verbose)
                        e_writer.println(time + ", Idle, Vehicle " + e.vehicle().vid);
                    handleIdleArrival((IdleArrivalEvent) e);
                    break;
                default:
                    break;
            }

            if (visual) {
                System.out.println("Time: " + time);
                m = new Map(r_list, v_list); // Prints map based on reservation list and vehicle list

                // Debugging vehicle purposes
                v_list.get(0).print();

                // Pause
                try {
                    System.in.read();
                } catch
                        (IOException exc) {
                    exc.printStackTrace();
                }
            }
        }

        if (verbose) {
            r_writer.close();
            for(StringBuffer s : s_buffer) {
                for(int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == '\n')
                        d_writer.println();
                    else
                        d_writer.append(s.charAt(i));
                }
                d_writer.println();
                d_writer.println();
                d_writer.println();
            }
            d_writer.close();
            e_writer.close();
        }
    }


    // Chooses a vehicle and passes it to a reservation assignment event
    private void handleReservation(ReservationEvent e) {
        if (!r_list.contains(e.party()))
            r_list.add(e.party());

        // Take a reservation and assign it to a driver
        Location src = e.party().src;
        Location dest = e.party().dest;
        int[] distances = new int[v_list.size()];
        for(int i = 0; i < v_list.size(); i++)
            distances[i] = Integer.MAX_VALUE;

        boolean has_idle_car = false;
        Vehicle temp;
        int shortest_idle_distance = Integer.MAX_VALUE;
        int shortest_idle_index = -1;
        int shortest_non_idle_distance = Integer.MAX_VALUE;
        int shortest_non_idle_index = -1;
        // Prioritize idle cars
        for(int i = 0; i < v_list.size(); i++) {
            temp = v_list.get(i);
            if (temp.available_capacity >= e.party().passengers) {
                distances[i] = distance(temp.loc, src);
                if (temp.mode == 0) { // If the vehicle is in idle mode
                    has_idle_car = true;
                    if (distances[i] < shortest_idle_distance) {
                        shortest_idle_distance = distances[i];
                        shortest_idle_index = i;
                    }
                } else if (e.party().carpools && temp.carpools) {  // If the vehicle has passengers that want to carpool and the party is willing to carpool
                    // Check to see if reservation would be in the right direction as the first party in the car
                    int x_v_direction = temp.getFirstParty().dest.x_coord - temp.loc.x_coord;
                    int y_v_direction = temp.getFirstParty().dest.y_coord - temp.loc.y_coord;
                    int x_p_direction = e.party().dest.x_coord - temp.loc.x_coord;
                    int y_p_direction = e.party().dest.y_coord - temp.loc.y_coord;

                    // If they are the same direction, then consider the vehicle for assignment
                    if (Math.signum(x_v_direction) == Math.signum(x_p_direction) &&
                            Math.signum(y_v_direction) == Math.signum(y_p_direction)) {
                        // Also check to make sure that they fit in the "box"
                        if (checkBound(e.party().dest.x_coord, temp.getFirstParty().dest.x_coord, temp.loc.x_coord) &&
                                checkBound(e.party().dest.y_coord, temp.getFirstParty().dest.y_coord, temp.loc.y_coord)) {
                            if (distances[i] < shortest_non_idle_distance) {
                                shortest_non_idle_distance = distances[i];
                                shortest_non_idle_index = i;
                            }
                        }
                    }
                }
            }
        }

        // If no car found
        if (shortest_idle_index == -1 && shortest_non_idle_index == -1) {
            // Throw it back into the queue at the time interval of the next drop off event
            e.modifyTime(nextDropOffTime() + 0.001);

            pq.add(e); // Add event back into queue at next event time to see if anything has changed
            return;
        }

        // If there is an idle car, assign the closest idle car to the party
        if (has_idle_car == true) {
            //v_list.get(shortest_idle_index).mode = 2;
            // Add car back with destination updated
            pq.add(new ReservationAssignmentEvent(time, e.party(), v_list.get(shortest_idle_index)));
        } else {  // No idle cars - pick closest available empty car which is also headed in the same direction
            pq.add(new ReservationAssignmentEvent(time, e.party(), v_list.get(shortest_non_idle_index)));
        }
    }

    //  Reservation is assigned to vehicle
    private void handleReservationAssignment(ReservationAssignmentEvent e) {
        // Add that party to the vehicle
        e.vehicle().addReservation(e.party());

        //pq.add(new IntersectionArrivalEvent(time, e.vehicle()));

        // If reservation is in the same location, trigger intersection arrival without travel time
        if (e.vehicle().dest.equals(e.vehicle().loc)) {
            pq.add(new IntersectionArrivalEvent(time, e.vehicle()));
        } else
            pq.add(new IntersectionArrivalEvent(time + getTravelTime(), e.vehicle()));
    }


    // Attempts to update location by moving one square in a closer direction to destination
    private void handleIntersectionArrival(IntersectionArrivalEvent e) {
        // Remove other intersection arrivals on pq for this vehicle
        cleanPQ(e);

        // Updates position of vehicle by one block closer to its destination
        // Does nothing if dest is equal to loc
        e.vehicle().updateLocation();

        if (verbose) {
            Vehicle v = e.vehicle();
            //d_writer.println(v.vid + ", " + time + ", (" + v.loc.x_coord + "," + v.loc.y_coord + ")");
            s_buffer[v.vid].append(v.vid + ", " + time + ", (" + v.loc.x_coord + "," + v.loc.y_coord + ")" + '\n');
        }

        // Attempt to drop off passengers if loc == dest
        if (e.vehicle().dest.equals(e.vehicle().loc)) {
            if (e.vehicle().mode == 2) // Pick up
                pq.add(new PickUpEvent(time, e.vehicle().target, e.vehicle()));
            else if (e.vehicle().mode == 1)  // Drop off
                pq.add(new DropOffEvent(time, e.vehicle()));
        } else if (e.vehicle().mode != 0) // If no pick up or drop off and not idle, add intersection event for new location
            pq.add(new IntersectionArrivalEvent(time + getTravelTime(), e.vehicle()));
        else // Idle
            pq.add(new IdleArrivalEvent(time, e.vehicle()));
    }

    private void handlePickUp(PickUpEvent e) {
        if (e.vehicle().target != null) {
            if ((time - e.vehicle().target.request_time) > 15) {
                e.vehicle().target.free_ride = true;
                free_reservations++;
                free_riders += e.vehicle().target.passengers;
            }
            total_riders += e.vehicle().target.passengers;
        }
        e.vehicle().pickUp();
        r_list.remove(e.party());

        if (verbose) {
            Vehicle v = e.vehicle();
            //d_writer.println(v.vid + ", " + time + ", (" + v.loc.x_coord + "," + v.loc.y_coord + "), Pickup: " + e.party().passengers);
            s_buffer[v.vid].append(v.vid + ", " + time + ", (" + v.loc.x_coord + "," + v.loc.y_coord + "), Pickup: " + e.party().passengers + '\n');
        }

        if (e.vehicle().dest.equals(e.vehicle().loc)) {
            pq.add(new IntersectionArrivalEvent(time, e.vehicle()));
        } else
            pq.add(new IntersectionArrivalEvent(time + getTravelTime(), e.vehicle()));

    }

    private void handleDropOff(DropOffEvent e) {
        Party p = e.vehicle().target;
        if (verbose)
            r_writer.println(p.pid + ", " + p.request_time + ", " + p.passengers + ", " + p.carpools + ", " +
                    "(" + p.src.x_coord + "," + p.src.y_coord + "), " + time + ", " + e.vehicle().vid +
                    ", " + p.stops + ", (" + p.dest.x_coord + "," + p.dest.y_coord + "), " + p.free_ride);

        e.vehicle().dropOff();

        if (verbose) {
            Vehicle v = e.vehicle();
            //d_writer.println(v.vid + ", " + time + ", (" + v.loc.x_coord + "," + v.loc.y_coord + "), DropOff: " + p.passengers);
            s_buffer[v.vid].append(v.vid + ", " + time + ", (" + v.loc.x_coord + "," + v.loc.y_coord + "), DropOff: " + p.passengers + '\n');
        }

        if (e.vehicle().dest.equals(e.vehicle().loc)) {
            pq.add(new IntersectionArrivalEvent(time, e.vehicle()));
        } else
            pq.add(new IntersectionArrivalEvent(time + getTravelTime(), e.vehicle()));

    }

    private void handleIdleArrival(IdleArrivalEvent e) {
        if (r_list.isEmpty()) {
            e.vehicle().dest = new Location(9, 9); // Go towards center
            if (e.vehicle().dest.equals(e.vehicle().loc))
                return;  // Do nothing
            pq.add(new IntersectionArrivalEvent(time + getTravelTime(), e.vehicle()));
        } else {
            // Do nothing
        }
    }

    static int distance(Location x, Location y) {
        return (Math.abs(x.x_coord - y.x_coord) + Math.abs(x.y_coord - y.y_coord));
    }

    private void cleanPQ(IntersectionArrivalEvent target) {
        // Search for any Intersection Arrival events containing that vehicle and remove them
        /*for(Event e: pq) {
            if (e.type() == 2 && e != target) {
                if (e.vehicle() == target.vehicle()) {
                    pq.remove(e);
                }
            }
        }*/
        Event[] event_array = pq.toArray(new Event[pq.size()]);
        int size = pq.size();
        for(int i = 0; i < size; i++) {
            if (event_array[i].type() == 2 && event_array[i].vehicle() == target.vehicle())
                if (event_array[i].time() != target.time())
                    pq.remove(event_array[i]);
                //if ((IntersectionArrivalEvent) event_array[i] != target)
        }
    }

    private double nextDropOffTime() {
        for(Event e : pq) {
            if (e.type() == 2) {
                return e.time();
            }
        }
        return 1;
    }

    private double getTravelTime() {
        double r_time = 0;
        while (r_time <= 0) { // Can't have a negative time value if it somehow manages to
            r_time = 1.0 + r.nextGaussian()*20/60; // Mean of 1 min, SD of 20 seconds
        }
        return r_time;
    }

    private double getExp() {
        return Math.log(1-r.nextDouble())/(-2);
    }

    private boolean checkBound(int location, int bound1, int bound2) {
        if (bound2 >= bound1) {
            if (location <= bound2 && location >= bound1)
                return true;
            else
                return false;
        } else {
            if (location >= bound2 && location <= bound1)
                return true;
            else
                return false;
        }
    }
}
