package des.event;

import des.Vehicle;

/**
 * Created by Vince on 11/6/2017.
 */
public class IdleArrivalEvent extends Event {
    public IdleArrivalEvent(double t_input, Vehicle v_input) {
        type = 5;
        time = t_input;
        v = v_input;
    }
}
