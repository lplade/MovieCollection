package name.lade.movielibrary.model;

/**
 * Location.java
 *
 * This stores a physical location where a container can be found
 *
 */
public class Location {
    int locationID = -1;
    String name;

    Location(String name){
        this.name = name;
    }
}
