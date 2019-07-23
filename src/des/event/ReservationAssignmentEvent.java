package des.event;

import des.Party;
import des.Vehicle;

/**
 * Created by Vince on 11/6/2017.
 */
public class ReservationAssignmentEvent extends Event {
    public ReservationAssignmentEvent(double t_input, Party p_input, Vehicle v_input) {
        type = 1;
        time = t_input;
        p = p_input;
        v = v_input;
    }
}
