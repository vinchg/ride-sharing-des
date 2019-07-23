package des.event;

import des.Party;
import des.Vehicle;

/**
 * Created by Vince on 11/6/2017.
 */
public class DropOffEvent extends Event {
    public DropOffEvent(double t_input, Vehicle v_input) {
        type = 4;
        time = t_input;
        v = v_input;
    }
}
