package des;

/**
 * Location
 *
 * @author Vincent Cheong
 * Student ID: 004299384
 */

public class Location {
    int x_coord;
    int y_coord;

    public Location(int x_input, int y_input) {
        x_coord = x_input;
        y_coord = y_input;
    }

    public boolean equals(Location l) {
        if ((this.x_coord == l.x_coord) && (this.y_coord == l.y_coord))
            return true;
        else
            return false;
    }
}