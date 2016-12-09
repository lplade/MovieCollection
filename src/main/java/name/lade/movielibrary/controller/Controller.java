package name.lade.movielibrary.controller;

import name.lade.movielibrary.model.*;
import name.lade.movielibrary.view.*;

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
    public Vector<Container> allContainers;
    public Vector<Movie> allMovies;
    public Vector<TVShow> allShows;

    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.startApp();
    }

    private void startApp() {
        db = new DB();

        //generate new tables if they aren't there
        db.createTables();
        //create some initial values needed for table dependencies
        db.initBorrower();
        db.initLocation();

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

    public Vector<Container> getAllContainers(){
        return db.fetchAllContainers();
    }

    public void addContainerToDatabase(Container container) {
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

    void deleteContainer(int containerID) {
        db.deleteContainer(containerID);
    }

    //TODO Borrower methods

    //TODO Person methods

}
