package des;

import java.util.ArrayList;
import java.util.List;

/**
 * CrossSection
 *
 * @author Vincent Cheong
 * Student ID: 004299384
 */

public class CrossSection {
    List<Party> parties = new ArrayList<>();
    List<Vehicle> vehicles = new ArrayList<>();

    public int occupied() {
        if (parties.isEmpty() && vehicles.isEmpty())
            return 0;
        else if (!vehicles.isEmpty())
            return 1;
        else
            return 2;
    }
}
