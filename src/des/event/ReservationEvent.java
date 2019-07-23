package des.event;
import des.Party;

/**
 * Created by Vince on 11/6/2017.
 */
public class ReservationEvent extends Event {
    public ReservationEvent(double t_input, Party p_input) {
        type = 0;
        time = t_input;
        p = p_input;
    }
}
