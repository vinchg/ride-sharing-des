package des;

/**
 * Party
 *
 * @author Vincent Cheong
 */

public class Party {
    int passengers;
    Location src;
    Location dest;
    double request_time;
    double wait_time;
    boolean carpools;
    int pid; // Party id
    int stops = 0;
    boolean free_ride = false;

    public Party(int passengers_input, Location src_input, Location dest_input, double request_time_input, boolean carpools_input, int pid_input) {
        passengers = passengers_input;
        src = src_input;
        dest = dest_input;
        request_time = request_time_input;
        carpools = carpools_input;
        pid = pid_input;
    }
}
