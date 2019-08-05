package des;

import java.util.ArrayList;
import java.util.List;

/**
 * Map
 *
 * @author Vincent Cheong
 */

public class Map {
    final int streets = 20;
    final int avenues = 20;
    CrossSection[][] squaresville = new CrossSection[streets][avenues];

    public Map(List<Party> p_list, List<Vehicle> v_list) {
        for(int i = 0; i < streets; i++) {
            for (int j = 0; j < streets; j++) {
                squaresville[i][j] = new CrossSection();
            }
        }
        for(Vehicle v : v_list) {
            addVehicle(v);
        }
        for(Party p : p_list) {
            addParty(p);
        }
        print();
    }

    public void addVehicle(Vehicle v) {
        squaresville[v.loc.x_coord][v.loc.y_coord].vehicles.add(v);
    }

    public void addParty(Party p) {
        squaresville[p.src.x_coord][p.src.y_coord].parties.add(p);
    }

    public void print() {
        for(int i = 0; i < streets; i++) {
            for(int j = 0; j < avenues; j++) {
                int type = squaresville[i][j].occupied();
                if (type == 1)
                    System.out.print("V");
                else if (type == 2)
                    System.out.print("P");
                else
                    System.out.print(".");
            }
            System.out.println();
        }

    }
}
