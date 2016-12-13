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
    public Vector<Location> allLocations;
    public Vector<Borrower> allBorrowers;

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
        allLocations = getAllLocations();
        allBorrowers = getAllBorrowers();
        allContainers = getAllContainers();
        allMovies = getAllMovies();
        allShows = getAllShows();

        gui = new GUI(this);

        //send the collections to the model/GUI
        gui.setContainerListData(allContainers);
        gui.setMovieListData(allMovies);
        gui.setTVShowListData(allShows);
    }

    public Vector<Container> getAllContainers(){
        return db.fetchAllContainers();
    }

    public Vector<Movie> getAllMovies(){
        return db.fetchAllMovies();
    }

    public Vector<TVShow> getAllShows() {
        return db.fetchAllShows();
    }

    public Vector<Location> getAllLocations() {
        return db.fetchAllLocations();
    }

    public Vector<Borrower> getAllBorrowers() {
        return db.fetchAllBorrowers();
    }

    public void addContainerToDatabase(Container container) {
        db.addContainer(container);
    }

    public void addMovie(Movie movie) {
        assert movie.containerID > -1;
        db.addMovie(movie);
    }

    public void addTVShow(TVShow tvShow) {
        assert tvShow.containerID > -1;
        db.addTVShow(tvShow);
    }
    public void updateContainer(int currentID, Container container){
        db.updateContainer(currentID, container);
    }

    public void updateMovie(int currentID, Movie movie) {
        db.updateMovie(currentID, movie);
    }

    public void updateTVShow(int currentID, TVShow tvShow) {
        db.updateTVShow(currentID, tvShow);
    }

    //user for either Movies or TV
    void deleteTitle(Title title) {
        db.deleteTitle(title);
    }

    void deleteContainer(int containerID) {
        db.deleteContainer(containerID);
    }

    public Location getLocationByID(int locationID) {
        assert locationID > 0;
        return db.getLocationByID(locationID);

    }

    public Borrower getBorrowerByID(int borrowerID) {
        assert borrowerID > 0;
        return db.getBorrowerByID(borrowerID);
    }

    public Container getContainerByID(int containerID) {
        assert containerID > 0;
        return db.getContainerByID(containerID);
    }

    //TODO Person methods

}
