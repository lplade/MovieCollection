import java.util.Vector;

/**
 * Controller.java
 *
 * This is the main controller class for the application
 *
 */
public class Controller {

    private static GUI gui;
    private static DB db;
    Vector<Container> allContainers;
    Vector<Movie> allMovies;
    Vector<TVShow> allShows;

    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.startApp();
    }

    private void startApp() {
        db = new DB();

        //generate new tables if they aren't there
        db.createTables();

        //query the database to create the collections we need
        allContainers = db.fetchAllContainers();
        allMovies = db.fetchAllMovies();
        allShows = db.fetchAllShows();

        gui = new GUI(this);

        //send the collections to the model/GUI
        gui.setContainerListData(allContainers);
        gui.setMovieListData(allMovies);
        gui.setTVShowListData(allShows);
    }

    Vector<Container> getAllContainers(){
        return db.fetchAllContainers();
    }

    void addContainerToDatabase(Container container) {
        db.addContainer(container);
    }

    void addMovieToContainer(Movie movie, Container container) {
        db.addMovie(movie);
    }

    void addTVShowToContainer(TVShow tvShow, Container container) {
        db.addTVShow(tvShow);
    }
    void updateContainer(int currentID, Container container){
        db.updateContainer(currentID, container);
    }

    void updateMovie(int currentID, Movie movie) {
        db.updateMovie(currentID, movie);
    }

    void updateTVShow(int currentID, TVShow tvShow) {
        db.updateTVShow(currentID, tvShow);
    }

    //user for either Movies or TV
    void deleteTitle(Title title) {
        db.deleteTitle(title);
    }

    void deleteContainer(Container container) {
        db.deleteContainer(container);
    }

    //TODO Borrower methods

    //TODO Person methods

}
