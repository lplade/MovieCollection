/**
 * Borrower.java
 *
 * This class stores information about people to whom media containers have been loaned
 */
class Borrower {
    int borrowerID;
    String name;
    String email;
    int phone;

    //Construct with just name. Other fields are optional, use setters
    Borrower(String name) {
        this.name = name;
    }
}
