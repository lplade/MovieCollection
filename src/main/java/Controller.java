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
    Vector<Title> allTitles;

    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.startApp();
    }

    private void startApp() {
        db = new DB();
        db.createTables();
        allTitles = db.fetchAllTitles();
        gui = new GUI(this);
        gui.setListData(allTitles);
    }

    Vector<Title> getAllTitles(){
        return db.fetchAllTitles();
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

    void deleteTitle(Title title) {
        db.deleteTitle(title);
    }

    void deleteContainer(Container container) {
        db.deleteContainer(container);
    }

    //TODO Borrower methods

    //TODO Person methods

}
