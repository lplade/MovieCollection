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
    public Borrower(String name) {
        this.name = name;
    }

    //Full constructor
    public Borrower(String name, int borrowerID, String email, int phone){
        this.name = name;
        this.borrowerID = borrowerID;
        this.email = email;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
