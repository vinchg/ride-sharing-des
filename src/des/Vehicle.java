package des;

import java.util.ArrayList;
import java.util.List;

/**
 * Vehicle
 *
 * @author Vincent Cheong
 */

public class Vehicle {
    Location loc; // Current location
    Location dest; // Current destination
    Party target = null; // Party target associated with destination
    final int MAX_CAPACITY;
    int current_capacity;  // Amount of passengers
    int available_capacity; // Amount of open seats
    List<Party> party_list = new ArrayList<>();
    List<Party> reservation_list = new ArrayList<>();
    int intersections_visited = 0;
    int mode = 0; // 0: idle, 1: drop off, 2: pick up
    boolean carpools = true;
    int vid; // Vehicle ID


    public Vehicle(Location loc_input, int capacity_input, int vid_input) {
        loc = loc_input;
        dest = null;
        MAX_CAPACITY = capacity_input;
        available_capacity = MAX_CAPACITY;
        vid = vid_input;
    }

    public boolean addReservation(Party p) {
        if (p.passengers > available_capacity)
            return false;
        else {
            current_capacity += p.passengers;
            available_capacity -= p.passengers;
            reservation_list.add(p);
            if (!p.carpools)
                carpools = false;
            updateDest();
            return true;
        }
    }

    // Adds target reservation to the party list
    public void pickUp() {
        if (target != null) {
            if (reservation_list.remove(target)) {
                party_list.add(target);
                updateDest(); // Updates target
            }
        }
    }

    // If there are passengers to drop off, they drop off here
    public void dropOff() {
        current_capacity -= target.passengers;
        available_capacity += target.passengers;
        party_list.remove(target);
        updateDest();  // Updates target
    }

    // Returns party with the lowest request time
    public Party getFirstParty() {
        Party lowest_p = null;
        double lowest_request_time = Integer.MAX_VALUE;
        for(Party p : party_list) {
            if (p.request_time < lowest_request_time) {
                lowest_request_time = p.request_time;
                lowest_p = p;
            }
        }
        for(Party p : reservation_list) {
            if (p.request_time < lowest_request_time) {
                lowest_request_time = p.request_time;
                lowest_p = p;
            }
        }
        return lowest_p;
    }

    // Updates the location by moving it ONE BLOCK closer to its destination if possible
    public void updateLocation() {
        int delta_x = dest.x_coord - loc.x_coord;
        int delta_y = dest.y_coord - loc.y_coord;
        if ((delta_x == 0) && (delta_y == 0)) {  // If dest == location
            return;  // Do nothing
        } else if (Math.abs(delta_x) >= Math.abs(delta_y)) { // Then move it along the x coord
            loc.x_coord = loc.x_coord + (int) Math.signum(delta_x);
            intersections_visited++;
        } else { // Move it along the y coord
            loc.y_coord = loc.y_coord + (int) Math.signum(delta_y);
            intersections_visited++;
        }

        for(Party p: party_list) {
            p.stops++;
        }
    }

    // Updates to newest destination, also sets mode
    private void updateDest() {
        if (reservation_list.size() == 0 && party_list.size() == 0) {
            if (mode != 0) {
                target = null;
                dest = new Location(9, 9);
                mode = 0;
                carpools = true;
            }
            return;
        }

        int lowest_distance = Integer.MAX_VALUE;
        Location lowest_dest = dest;
        int distance;
        // Check for closest party location
        for(int i = 0; i < party_list.size(); i++) {
            distance = Lynx.distance(loc, party_list.get(i).dest);
            if (distance < lowest_distance) {
                target = party_list.get(i);
                lowest_distance = distance;
                lowest_dest = target.dest;
                mode = 1;
            }
        }
        // Check for closest reservation location
        for(int i = 0; i < reservation_list.size(); i++) {
            distance = Lynx.distance(loc, reservation_list.get(i).src);
            if (distance < lowest_distance) {
                target = reservation_list.get(i);
                lowest_distance = distance;
                lowest_dest = target.src;
                mode = 2;
            }
        }
        dest = lowest_dest;
    }

    public void print() {
        System.out.println("mode = " + mode);
        System.out.println("party_s = " + party_list.size());
        System.out.println("res_s = " + reservation_list.size());
        System.out.println("curr cap = " + current_capacity);
        System.out.println("ava cap = " + available_capacity);
        if (target != null)
            System.out.println("target = " + target.passengers);
    }
}