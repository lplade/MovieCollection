import java.sql.*;

/*
Handles all database access methods

Database structure:
Container(_ContainerID_, Name, UPC, LocationID, PurchaseDate, BorrowerID, Sell, Sold)   physical package
Title(_TitleID_, Name, Format, ContainerID, Genre, Language)   common to media titles
Movie(_TitleID_, Year, Rating, Cut)   subtype of movies
TVShow(_TitleID_, Season, Rating)   subtype of tv shows and such
Location(_LocationID_, Name)   where it is physically stored
Borrower(_BorrowerID_, Name, Email, Phone)   who we loaned it to
Person(_PersonID_, Surname, GivenName)   stores biographical data for actors, producers, etc.
 */
public class DB {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/movie_library";
    private static final String USER = "lade";
    private static final String PASSWORD = "agram";

    DB() {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Can't instantiate driver class; check drives and classpath");
            cnfe.printStackTrace();
            System.exit(-1); //exit if driver doesn't work
        }
    }

    void createTables() {
        try (
                Connection conn = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD);
                Statement statement = conn.createStatement()) {
            //Database should already have been created

            //Create the tables in the database if it doesn't already exist
            String createTableSQL[] = {
                    "CREATE TABLE IF NOT EXISTS Borrower(" +
                            "BorrowerID int NOT NULL AUTO_INCREMENT, " +
                            "Name varchar(128), " +
                            "Email varchar(128), " +
                            "Phone decimal(10)" + //store as 10-digit int
                            ")",
                    "CREATE TABLE IF NOT EXISTS Location(" +
                            "LocationID int NOT NULL AUTO_INCREMENT," +
                            "Name varchar(128)" +
                            ")",
                    "CREATE TABLE IF NOT EXISTS Person(" +
                            "PersonID int NOT NULL AUTO_INCREMENT," +
                            "Surname varchar(128)," +
                            "GivenName varchar(128)" +
                            ")",
                    "CREATE TABLE IF NOT EXISTS Container(" +
                            "ContainerID int NOT NULL AUTO_INCREMENT, " +
                            "Name varchar(128)," +
                            "UPC decimal(12)," + //UPC-A
                            "LocationID int," +
                            "PurchaseDate date," +
                            "BorrowerID int," +
                            "Sell bool," +
                            "Sold bool," +
                            "PRIMARY KEY(ContainerID)," +
                            "FOREIGN KEY(LocationID) REFERENCES Location(LocationID)," +
                            "FOREIGN KEY(BorrowerID) REFERENCES Borrower(BorrowerID)" +
                            ")",
                    "CREATE TABLE IF NOT EXISTS Title(" +
                            "TitleID int NOT NULL AUTO_INCREMENT, " +
                            "Name varchar(128), " +
                            "Format varchar(32), " +
                            "ContainerID int, " +
                            "Genre varchar(128), " +
                            "Language varchar(2), " +
                            "PRIMARY KEY(TitleID)," +
                            "FOREIGN KEY(ContainerID) REFERENCES Container(ContainerID)" +
                            ")",
                    "CREATE TABLE IF NOT EXISTS Movie(" +
                            "TitleID int NOT NULL, " +
                            "Year year, " +
                            "Rating varchar(8), " + //"PG-13"
                            "Cut varchar(64)," + // "Director's Cut"
                            "PRIMARY KEY(TitleID)," +
                            "FOREIGN KEY(TitleID) REFERENCES Title(TitleID)" +
                            ")",
                    "CREATE TABLE IF NOT EXISTS TVShow(" +
                            "TitleID int NOT NULL," +
                            "Season int," +
                            "Rating varchar(8) " + //"TV-14"
                            ")"
            };
            for(int s = 0; s < createTableSQL.length; s++) {
                statement.execute(createTableSQL[s]);
            }
            System.out.println("Created cube solution table");


            statement.close();
            conn.close();

        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

}
