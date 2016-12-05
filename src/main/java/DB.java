import java.sql.*;
import java.util.Vector;

/*
Handles all database access methods

Database structure:
Container(_ContainerID_, Name, UPC, LocationID, PurchaseDate, BorrowerID, sell, sold)   physical package
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

    //set up a logging object
    private Log log = new Log();

    DB() {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException cnfe) {
            log.error("Can't instantiate driver class; check drives and classpath");
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
                            "GivenName varchar(128)," +
                            "TMDBID int," + //https://developers.themoviedb.org/3/people
                            "IMDBNM int," +  //name entry from undocumented IMDB API
                            "UNIQUE(TMDBID)," + //these will work as alternate keys
                            "UNIQUE(IMDBNM)" +
                            ")",
                    "CREATE TABLE IF NOT EXISTS Container(" +
                            "ContainerID int NOT NULL AUTO_INCREMENT, " +
                            "Name varchar(128)," +
                            "Barcode decimal(13)," + //UPC-A or EAN-13
                            "LocationID int," +
                            "PurchaseDate date," +
                            "BorrowerID int," +
                            "Sell bool," +
                            "Sold bool," +
                            "PRIMARY KEY(ContainerID)," +
                            "FOREIGN KEY(LocationID) REFERENCES Location(LocationID)," +
                            "FOREIGN KEY(BorrowerID) REFERENCES Borrower(BorrowerID)," +
                            "UNIQUE(Barcode)" + //alternate key
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
                            "ON DELETE CASCADE " +
                            ")",
                    "CREATE TABLE IF NOT EXISTS TVShow(" +
                            "TitleID int NOT NULL," +
                            "Season int," +
                            "Rating varchar(8)," + //"TV-14"
                            "PRIMARY KEY(TitleID)," +
                            "FOREIGN KEY(TitleID) REFERENCES Title(TitleID)" +
                            "ON DELETE CASCADE " +
                            ")"
            };
            for(int s = 0; s < createTableSQL.length; s++) {
                statement.execute(createTableSQL[s]);
            }
            log.info("Created cube solution table");


            statement.close();
            conn.close();

        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    void addBorrower(Borrower borrower) {
        //try with resources connect to database
        try (Connection conn = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD)) {

            //set up preparedStatement
            String prepStatStr = "INSERT INTO Borrower VALUES(?, ?, ?, ?)";
            PreparedStatement insertPS = conn.prepareStatement(prepStatStr);
            insertPS.setInt(1, 0); //auto-increment key
            insertPS.setString(2, borrower.name);
            insertPS.setString(3, borrower.email);
            insertPS.setInt(4, borrower.phone);

            //actually put it in the database
            insertPS.execute();

            log.info("Added Borrower for " + borrower);

            insertPS.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void addLocation(Location location) {
        //try with resources connect to database
        try (Connection conn = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD)) {

            //set up preparedStatement
            String prepStatStr = "INSERT INTO Location VALUES(?, ?)";
            PreparedStatement insertPS = conn.prepareStatement(prepStatStr);
            insertPS.setInt(1, 0); //auto-increment key
            insertPS.setString(2, location.name);

            //actually put it in the database
            insertPS.execute();

            log.info("Added Location for " + location);

            insertPS.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void addPerson(Person person) {
        //try with resources connect to database
        try (Connection conn = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD)) {

            //set up preparedStatement
            String prepStatStr = "INSERT INTO Person VALUES(?, ?, ?, ?, ?)";
            PreparedStatement insertPS = conn.prepareStatement(prepStatStr);
            insertPS.setInt(1, 0); //auto-increment key
            insertPS.setString(2, person.surname);
            insertPS.setString(3, person.givenName);
            insertPS.setInt(4, person.tmdbID);
            insertPS.setInt(5, person.imdbNM);

            //actually put it in the database
            insertPS.execute();

            log.info("Added Person for " + person);

            insertPS.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void addContainer(Container container) {
        //try with resources connect to database
        try (Connection conn = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD)) {

            //set up preparedStatement
            String prepStatStr = "INSERT INTO Container VALUES(?, ?, ?, ?, ?, ?, ?, ? )";
            PreparedStatement insertPS = conn.prepareStatement(prepStatStr);
            insertPS.setInt(1, 0); //auto-increment key
            insertPS.setString(2, container.name);
            insertPS.setLong(3, container.barcode);
            insertPS.setInt(4, container.locationID);
            insertPS.setDate(5, container.purchaseDate);
            insertPS.setInt(6, container.borrowerID);
            insertPS.setBoolean(7, container.sell); //always set to false on creation
            insertPS.setBoolean(8, container.sold);

            //actually put it in the database
            insertPS.execute();

            log.info("Added Container for " + container);

            insertPS.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Right now we're assuming all titles are either Movies or TVShows, so no method to add Title directly

    void addMovie(Movie movie) {
        try (Connection conn = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD)) {
            //first we store the Title
            String prepStatStr = "INSERT INTO Title VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement insertPS = conn.prepareStatement(prepStatStr, Statement.RETURN_GENERATED_KEYS);
            insertPS.setInt(1, 0); //auto-increment
            insertPS.setString(2, movie.name);
            insertPS.setString(3, movie.format);
            insertPS.setInt(4, movie.containerID);
            insertPS.setString(5, movie.genre);
            insertPS.setString(6, movie.getLanguageStr());

            insertPS.executeUpdate();
            log.info("Added Title for " + movie + "...");

            //get the generated primary key
            // http://stackoverflow.com/questions/5513180/java-preparedstatement-retrieving-last-inserted-id
            ResultSet rs = insertPS.getGeneratedKeys();
            if(rs.next()) {
                int last_inserted_id = rs.getInt(1);

                //now we can store the Movie elements
                prepStatStr = "INSERT INTO Movie VALUES (?, ?, ?, ?)";
                insertPS = conn.prepareStatement(prepStatStr);
                insertPS.setInt(1, last_inserted_id);
                insertPS.setInt(2, movie.year);
                insertPS.setString(3, movie.rating);
                insertPS.setString(4, movie.cut);

                insertPS.execute();
                log.info("Added Movie for " + movie);

            } else {
                //something is wrong if execute failed to return something
                log.error("Added Title, but cannot add Movie!");
                throw new SQLSyntaxErrorException();
            }

            insertPS.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void addTVShow(TVShow tvshow) {
        try (Connection conn = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD)) {
            //first we store the Title
            String prepStatStr = "INSERT INTO Title VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement insertPS = conn.prepareStatement(prepStatStr, Statement.RETURN_GENERATED_KEYS);
            insertPS.setInt(1, 0); //auto-increment
            insertPS.setString(2, tvshow.name);
            insertPS.setString(3, tvshow.format);
            insertPS.setInt(4, tvshow.containerID);
            insertPS.setString(5, tvshow.genre);
            insertPS.setString(6, tvshow.getLanguageStr());

            insertPS.executeUpdate();

            //get the generated primary key
            // http://stackoverflow.com/questions/5513180/java-preparedstatement-retrieving-last-inserted-id
            ResultSet rs = insertPS.getGeneratedKeys();
            if(rs.next()) {
                int last_inserted_id = rs.getInt(1);

                //now we can store the TV elements
                prepStatStr = "INSERT INTO TVShow VALUES (?, ?, ?)";
                insertPS = conn.prepareStatement(prepStatStr);
                insertPS.setInt(1, last_inserted_id);
                insertPS.setInt(2, tvshow.season);
                insertPS.setString(3, tvshow.rating);

                insertPS.execute();

            } else {
                //something is wrong if execute failed to return something
                throw new SQLSyntaxErrorException();
            }

            insertPS.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    Vector<Title> fetchAllTitles() {
        //TODO query
        return new Vector<>();
    }

    void updateContainer(int currentID, Container container) {
    }

    void updateMovie(int currentID, Movie movie) {
        //TODO update
    }

    void updateTVShow(int currentID, TVShow tvShow) {
        //TODO update
    }

    void delete(Title title) {
        //TODO drop - be sure to cascade to Movie or TVShow
    }


    void deleteTitle(Title title) {
        //only used for accidental entries - should really drop parent Container
        //TODO drop - be sure to casade to Movie or TVShow
    }

    void deleteContainer(Container container) {
        //TODO drop
    }
}
