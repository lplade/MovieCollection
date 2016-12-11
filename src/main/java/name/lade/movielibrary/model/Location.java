package name.lade.movielibrary.model;

/**
 * Location.java
 *
 * This stores a physical location where a container can be found
 *
 */
public class Location {
    public int locationID = -1;
    public String name;

    public Location(String name){
        this.name = name;
    }

    public Location(String name, int locationID) {
        this.name = name;
        this.locationID = locationID;
    }

    //need to define a toString method for fields that appear in JComboBox
    @Override
    public String toString(){
        return this.name;
    }
}
