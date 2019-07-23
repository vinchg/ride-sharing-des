package des.event;

import des.Vehicle;

/**
 * Created by Vince on 11/6/2017.
 */
public class IntersectionArrivalEvent extends Event {
    public IntersectionArrivalEvent(double t_input, Vehicle v_input) {
        type = 2;
        time = t_input;
        v = v_input;
    }
}
