package name.lade.movielibrary.model;

import java.sql.Date;

/**
 * Container.java
 *
 * This class contains information about packages that media come in.
 * It normally corresponds to a single UPC-coded retail item
 * Could refer to a single movie, but can also be a compilation that comes as a set
 *
 * In the case of digital files, this will be the file container (MP4, MKV) itself
 * In case of VOD services, this will be the name of the entry in the library
 */

//TODO re-write everything to store Container, Location, and Borrower instead of indexes
    //populate these during SQL retrieval
    //will let us have more control over how fields in ContainerTableModel are returned

public class Container {
    public int containerID = -1; //autogenerated by DB
    public String name;
    public long barcode; //UPC-A, or EAN-13
    public int locationID = -1;
    public Date purchaseDate = new Date(0);
    public int borrowerID = -1;
    public boolean sell;
    public boolean sold;

    //TODO store Vector of Titles here?

    //Construct with just name. Other fields are optional, use setters
    public Container(String name) {
        this.name = name;
    }

    //Construct it all in one shebang
    public Container(String name, int containerID, long barcode, int locationID,
                     Date purchaseDate, int borrowerID, boolean sell, boolean sold){
        this.containerID = containerID;
        this.name = name;
        this.barcode = barcode;
        this.locationID = locationID;
        this.purchaseDate = purchaseDate;
        this.borrowerID = borrowerID;
        this.sell = sell;
        this.sold = sold;
    }

    //need this defined to populate JComboBox sensibly
    @Override
    public String toString(){
        return this.name;
    }

}
