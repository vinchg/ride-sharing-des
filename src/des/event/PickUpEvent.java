package des.event;

import des.Party;
import des.Vehicle;

/**
 * Created by Vince on 11/6/2017.
 */
public class PickUpEvent extends Event {
    public PickUpEvent(double t_input, Party p_input, Vehicle v_input) {
        type = 3;
        time = t_input;
        v = v_input;
        p = p_input;
    }
}
