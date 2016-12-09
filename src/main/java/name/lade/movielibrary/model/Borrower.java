package name.lade.movielibrary.model;

/**
 * Borrower.java
 *
 * This class stores information about people to whom media containers have been loaned
 */
public class Borrower {
    public int borrowerID;
    public String name;
    public String email;
    public int phone;

    //Construct with just name. Other fields are optional, use setters
    Borrower(String name) {
        this.name = name;
    }
}
