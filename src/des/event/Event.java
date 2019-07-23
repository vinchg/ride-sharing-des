package des.event;

import des.Party;
import des.Vehicle;

/**
 * Event
 *
 * @author Vincent Cheong
 * Student ID: 004299384
 */

public class Event implements Comparable<Event> {
    double time = 0;
    // Reservation type
    // 0: Reservation, 1: Reservation Assignment, 2: Intersection Arrival, 3: Pick Up, 4: Drop Off, 5: Idle Arrival
    int type = 0;
    Vehicle v;
    Party p;

    public Event() {
    }

    public Event(double time_input) {
        time = time_input;
    }

    public int type() {
        return type;
    }

    public double time() { return time; }

    public void modifyTime(double t) { time = t; }

    public Vehicle vehicle() {return v; }

    public Party party() { return p; }

    public int compareTo(Event e) {
        if (this.time == e.time)
            return 0;
        else
            return this.time > e.time ? 1 : -1;
    }
}