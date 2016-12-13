package name.lade.movielibrary.view;

import name.lade.Log;
import name.lade.movielibrary.controller.*;
import name.lade.movielibrary.model.*;
import name.lade.movielibrary.model.Container;


import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

/**
 * GUI.java
 *
 *
 *
 */
public class GUI extends JFrame {
    private JTabbedPane rootTabbedPane;
    private JPanel rootPanel;
    private JTable containerTable;
    private JTable movieTable;
    private JTable tvTable;
    private JToolBar mainJToolBar;
    private JButton addContainerButton;
    private JButton updateContainerButton;
    private JButton containerDeleteButton;
    private JTextField containerNameTextField;
    private JTextField barCodeTextField;
    private JComboBox<Location> containerLocationComboBox;
    private JCheckBox sellCheckBox;
    private JCheckBox soldCheckBox;
    private JButton clearContainerButton;
    private JButton addMovieButton;
    private JButton updateMovieButton;
    private JButton clearMovieButton;
    private JTextField movieTitleTextField;
    private JTextField movieFormatTextField;
    private JComboBox<Container> movieContainerComboBox;
    private JTextField movieGenreTextField;
    private JTextField movieLangTextField;
    private JTextField movieYearTextField;
    private JTextField movieRatingTextField;
    private JButton addTVShowButton;
    private JLabel tvShowDialogPanel;
    private JPanel movieDialogPanel;
    private JPanel containerDialogPanel;
    private JButton updateTVShowButton;
    private JButton clearTVShowButton;
    private JTextField tvShowTitleTextField;
    private JTextField tvShowFormatTextField;
    private JComboBox<Container> tvShowContainerComboBox;
    private JTextField tvShowGenreTextBox;
    private JTextField tvShowLangTextBox;
    private JTextField tvShowSeasonTextBox;
    private JTextField tvShowRatingTextBox;
    private JComboBox<Borrower> containerBorrowerComboBox;
    private JButton movieDeleteButton;
    private JButton tvShowDeleteButton;
    private JSpinner containerDateSpinner;

    //https://docs.oracle.com/javase/tutorial/uiswing/components/tabbedpane.html
    //https://docs.oracle.com/javase/tutorial/uiswing/components/toolbar.html
    //http://www.oracle.com/technetwork/java/index-138612.html

    private ContainerTableModel containerTM;
    private MovieTableModel movieTM;
    private TVTableModel tvshowTM;

    private SpinnerDateModel containerDM;

    private Controller controller;

    private int selectedContainer;
    private int selectedMovie;
    private int selectedTVShow;

    private Log log = new Log();

    public GUI(Controller controller) {
        super("Movie Collection");
        setContentPane(rootPanel);
        setPreferredSize(new Dimension(800, 600));


        //map the controller
        this.controller = controller;

        //set up the models
        containerTM = new ContainerTableModel(controller.allContainers);
        movieTM = new MovieTableModel(controller.allMovies);
        tvshowTM = new TVTableModel(controller.allShows);
        containerTable.setModel(containerTM);
        movieTable.setModel(movieTM);
        tvTable.setModel(tvshowTM);

        containerDM = new SpinnerDateModel(
                new java.util.Date(), //default to today
                null,
                null,
                Calendar.DAY_OF_MONTH
        );
        containerDateSpinner.setModel(containerDM);
        containerDateSpinner.setEditor(new JSpinner.DateEditor(containerDateSpinner, "yyyy MMM dd"));



        //hide some columns?
        //http://stackoverflow.com/questions/1492217/how-to-make-a-columns-in-jtable-invisible-for-swing-java

        selectedContainer = -1 ; //this means no record is selected
        selectedMovie = -1;
        selectedTVShow = -1;

        //populate the ComboBoxes
        configureLocationComboBox(containerLocationComboBox);
        configureBorrowerComboBox(containerBorrowerComboBox);
        configureContainerComboBox(movieContainerComboBox);
        configureContainerComboBox(tvShowContainerComboBox);

        //TODO set up a JToolbar and modal dialogs instead of all-in-one form/table

        //set up listeners
        addListeners();
        addContainerTabListeners();
        addMovieTabListeners();
        addTVShowTabListeners();

        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

    }

    //Populates a Borrower combo box tab with Borrowers
    private void configureBorrowerComboBox(JComboBox<Borrower> comboBox) {
        for (Borrower borrower : controller.allBorrowers) {
            comboBox.addItem(borrower);
        }
    }

    //Populates a Location combo box tab with Locations
    private void configureLocationComboBox(JComboBox<Location> comboBox) {
        for (Location location : controller.allLocations) {
            comboBox.addItem(location);
        }

    }

    //Populates a Container combo bow with Containers
    private void configureContainerComboBox(JComboBox<Container> comboBox) {
        for (Container container : controller.allContainers) {
            comboBox.addItem(container);
        }
    }

    //*** Listeners for whole window ***
    private void addListeners() {

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(
                        GUI.this,
                        "Are you sure you want to exit?",
                        "Exit?",
                        JOptionPane.OK_CANCEL_OPTION)) {
                    //reset the database
                    //controller.resetAllData();
                    //And quit.
                    System.exit(0);
                }
                //super.windowClosing(e);
            }
        });

    }

    //*** Listeners for the container tab ***
    private void addContainerTabListeners() {

        //watch if user selects a row
        //http://stackoverflow.com/questions/10128064/jtable-selected-row-click-event
        containerTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                //first make sure item is not out of bounds
                //listener fires when clearSelection() fires and causes oobe error is not tested
                //http://stackoverflow.com/questions/13102246/remove-the-selection-from-a-jlist-in-java-swing
                if (!e.getValueIsAdjusting() && containerTable.getSelectedRow() >= 0){

                    log.debug("Selected row = " + containerTable.getSelectedRow());

                    //re-get the model object so we can use .getRow() method
                    int row = containerTable.getSelectedRow();
                    ContainerTableModel containerTM = (ContainerTableModel) containerTable.getModel();
                    Container container = containerTM.getRow(row);

                    //display contents of that Container
                    containerNameTextField.setText(container.name);
                    barCodeTextField.setText(String.valueOf(container.barcode));
                    Location loc = controller.getLocationByID(container.locationID); //query Location...
                    containerLocationComboBox.setSelectedItem(loc);                  //...and update the ComboBox
                    Borrower brw = controller.getBorrowerByID(container.borrowerID);
                    containerBorrowerComboBox.setSelectedItem(brw);
                    containerDM.setValue(container.purchaseDate);
                    sellCheckBox.setSelected(container.sell);
                    soldCheckBox.setSelected(container.sold);

                    //update the index records
                    int id;
                    try {
                        id = container.containerID;
                        log.debug("Selected ContainerID = " + id);
                    } catch (ArrayIndexOutOfBoundsException oobe) {
                        //when we clear the selection, the listener fires and returns an invalid value here
                        log.debug("list selection OOB (this is not a problem)");
                        id = -1;
                    }

                    selectedContainer = id;

                    //leave these there until the user updates

                    //TODO toggle availability of GUI items

                }
            }
        });

        containerLocationComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO do stuff?
            }
        });

        containerBorrowerComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO do stuff?
            }
        });

        addContainerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Build a Container from the form fields
                Container newContainer = containerFormToObject();

                //abort if form data is not usable
                if (newContainer.name.equals("ERRORVOID")) return;

                //add the Container to the database
                controller.addContainerToDatabase(newContainer);

                //Clear the form when we're done
                resetContainerForm();

                //refresh to reflect the changes
                Vector<Container> allContainers = controller.getAllContainers();
                setContainerListData(allContainers);

                //TODO update the ComboBox models too

            }
        });

        updateContainerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedContainer == -1) {
                    //we should not be able to get here once the button disabling logic is in place
                    return;
                }

                //Build a Container from the form fields
                Container newContainer = containerFormToObject();

                //abort if form data is not usable
                if (newContainer.name.equals("ERRORVOID")) return;

                //update the Container in the database
                //selectedContainer = .containerID selected, should have been set by ListSelectionListener
                controller.updateContainer(selectedContainer, newContainer);

                //Clear the form once we're done
                resetContainerForm();

                //refresh to reflect the changes
                Vector<Container> allContainers = controller.getAllContainers();
                setContainerListData(allContainers);

                //TODO update the ComboBox models too
            }
        });

        clearContainerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                resetContainerForm();

                //TODO unselect JTable if needed
                //TODO re-enable Add button if needed
                //TODO disable the Update button
                //TODO disable the Delete button
            }
        });


    }

    //*** Listeners for Movie tab ***
    private void addMovieTabListeners() {
        //watch if user selects a row
        //http://stackoverflow.com/questions/10128064/jtable-selected-row-click-event
        movieTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                //first make sure item is not out of bounds
                //listener fires when clearSelection() fires and causes oobe error is not tested
                //http://stackoverflow.com/questions/13102246/remove-the-selection-from-a-jlist-in-java-swing
                if (!e.getValueIsAdjusting() && movieTable.getSelectedRow() >= 0){

                    log.debug("Selected row = " + movieTable.getSelectedRow());

                    //re-get the model object so we can use .getRow() method
                    int row = movieTable.getSelectedRow();
                    MovieTableModel movieTM = (MovieTableModel) movieTable.getModel();
                    Movie movie = movieTM.getRow(row);

                    //display contents of that Movie
                    movieTitleTextField.setText(movie.name);
                    movieFormatTextField.setText(movie.format);
                    Container cont = controller.getContainerByID(movie.containerID);
                    movieContainerComboBox.setSelectedItem(cont); //TODO this still doesn't update!
                    movieGenreTextField.setText(movie.genre);
                    movieLangTextField.setText(movie.getLanguageStr());
                    movieYearTextField.setText(String.valueOf(movie.year));
                    movieRatingTextField.setText(movie.rating);


                    //update the index records
                    int id;
                    try {
                        id = movie.titleID;
                        log.debug("Selected MovieID = " + id);
                    } catch (ArrayIndexOutOfBoundsException oobe) {
                        //when we clear the selection, the listener fires and returns an invalid value here
                        log.debug("list selection OOB (this is not a problem)");
                        id = -1;
                    }

                    selectedMovie = id;

                    //leave these there until the user updates

                    //TODO toggle availability of GUI items

                }
            }
        });

        movieContainerComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO do stuff?
            }
        });

        addMovieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Build Movie from form fields
                Movie newMovie = movieFormToObject();

                //Abort if form data is not usable
                if (newMovie.name.equals("ERRORVOID")) return;

                //add the Movie to the database
                controller.addMovie(newMovie);

                //Clear the form
                resetMovieForm();

                //refresh to reflect the changes
                Vector<Movie> allMovies = controller.getAllMovies();
                setMovieListData(allMovies);


            }
        });

        updateMovieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedMovie == -1){
                    //we should not be able to get here once button disabling logic is in place
                    return;
                }

                //Build a Movie from the form fields
                Movie newMovie = movieFormToObject();

                //Abort if form data is not usable
                if (newMovie.name.equals("ERRORVOID")) return;

                //update the Movie in the database
                //selectedMovie should have been set by ListSelectionListener
                controller.updateMovie(selectedMovie, newMovie);

                resetMovieForm();

                //refresh to reflect the changes
                Vector<Movie> allMovies = controller.getAllMovies();
                setMovieListData(allMovies);

            }
        });

        clearMovieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetMovieForm();
            }
        });
    }


    //*** Listeners for TV Show tab ***
    private void addTVShowTabListeners() {
        //watch if user selects a row
        //http://stackoverflow.com/questions/10128064/jtable-selected-row-click-event
        tvTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                //first make sure item is not out of bounds
                //listener fires when clearSelection() fires and causes oob error
                //http://stackoverflow.com/questions/13102246/remove-the-selection-from-a-jlist-in-java-swing
                if (!e.getValueIsAdjusting() && tvTable.getSelectedRow() >= 0){

                    log.debug("Selected row = " + tvTable.getSelectedRow());

                    //re-get the model object so we can use .getRow() method
                    int row = tvTable.getSelectedRow();
                    TVTableModel tvshowTM = (TVTableModel) tvTable.getModel();
                    TVShow tvShow = tvshowTM.getRow(row);

                    //display the contents of that TVShow
                    tvShowTitleTextField.setText(tvShow.name);
                    tvShowFormatTextField.setText(tvShow.format);
                    Container cont = controller.getContainerByID(tvShow.containerID);
                    tvShowContainerComboBox.setSelectedItem(cont); //TODO does this update?
                    tvShowGenreTextBox.setText(tvShow.genre);
                    tvShowLangTextBox.setText(tvShow.getLanguageStr());
                    tvShowSeasonTextBox.setText(String.valueOf(tvShow.season));
                    tvShowRatingTextBox.setText(tvShow.rating);

                    //update the index records
                    int id;
                    try {
                        id = tvShow.titleID;
                        log.debug("Selected TVShowID = " + id);
                    } catch (ArrayIndexOutOfBoundsException oobe) {
                        //when we clear the selection, the listener fires and returns an oobe
                        log.debug("list selection OOB (this is not a problem)");
                        id = -1;
                    }

                    selectedTVShow = id;

                    //leave these there until the user updates

                    //TODO toggle availability of GUI items
                }
            }
        });

        addTVShowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Build TVShow from form fields
                TVShow newShow = tvShowFormToObject();

                //Abort if form data is not usable
                if (newShow.name.equals("ERRORVOID")) return;

                //add the TVShow to the database
                controller.addTVShow(newShow);

                //Clear the form
                resetTVShowForm();

                //refresh to reflect the changes
                Vector<TVShow> allTVShows = controller.getAllShows();
                setTVShowListData(allTVShows);

            }
        });
        updateTVShowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedMovie == -1) {
                    //we should not be able to get here once button disabling logic is in place
                    return;
                }

                //Build a TVShow from the form fields
                TVShow newShow = tvShowFormToObject();

                //Abort if form data is not usable
                if (newShow.name.equals("ERRORVOID")) return;

                //update the TVShow in the database
                //selected should have been set by ListSelectionListener
                controller.updateTVShow(selectedTVShow, newShow);

                resetTVShowForm();

                //refresh to reflect the changes
                Vector<TVShow> allShows = controller.getAllShows();
                setTVShowListData(allShows);

            }
        });
        clearTVShowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetTVShowForm();

            }
        });

    }




    public void setContainerListData(Vector<Container> allContainers) {
        containerTM.updateData(allContainers);
    }

    public void setMovieListData(Vector<Movie> allMovies) {
        movieTM.updateData(allMovies);
    }

    public void setTVShowListData(Vector<TVShow> allShows) {
        tvshowTM.updateData(allShows);
    }


    //*** helper methods ***

    //tests if a string is empty
    //displays an error dialog if it is
    private boolean testStringNotNull(String inString, String fieldName) {
        // inString is the string to test
        // fieldName is the word displayed to the user in the error
        if ( inString == null || inString.length() == 0) {
            String errMsg = "Please enter a " + fieldName + " for this solver";
            JOptionPane.showMessageDialog(
                    GUI.this,
                    errMsg
            );
            return false;
        } else {
            return true;
        }
    }

    //tests if a string can be a valid long
    //displays error dialog if it is not
    //TODO use checksum method in object itself
    private boolean testIsPositiveLong(String str) {
        try {
            Long lng = Long.parseLong(str);
            if (lng <= 0) {
                String errMsg = "Barcode cannot be negative number";
                JOptionPane.showMessageDialog(
                        GUI.this,
                        errMsg
                );
                return false;
            } else {
                return true;
            }
        } catch (NumberFormatException e) {
            String errMsg = "Barcode is not a valid number";
            JOptionPane.showMessageDialog(
                    GUI.this,
                    errMsg
            );
            return false;
        }
    }

    //test if a string is a char[2]
    private boolean testIsChar2(String langStr) {
        //assuming 1 letter = 1 char
        //TODO filter for multibyte Unicode
        if (langStr.length() != 2) {
            String errMesg = "Language must be entered in two-letter ISO code";
            JOptionPane.showMessageDialog(
                    GUI.this,
                    errMesg
            );
            return false;
        } else {
            return true;
        }
    }

    //convert String to char[2]
    private char[] toChar2(String s){
        char[] outChars = new char[2];
        for (int i = 0; i < 2; i++) {
            outChars[i] = s.charAt(i);
        }
        return outChars;
    }

    // common code for container add and update
    private Container containerFormToObject() {
        //get from fields
        String name = containerNameTextField.getText();
        String barcodeStr = barCodeTextField.getText();
        Long barcode = null;
        Location location = (Location) containerLocationComboBox.getSelectedItem();
        int locID = location.locationID;
        Date utilDate = containerDM.getDate();
        java.sql.Date sDate = utilDateToSQLDate(utilDate);
        Borrower borrower = (Borrower) containerBorrowerComboBox.getSelectedItem();
        int brwID = borrower.borrowerID;
        boolean sell = sellCheckBox.isSelected();
        boolean sold = soldCheckBox.isSelected();

        //validate these -- return a blank Container with name ERRORVOID if there's a problem
        if (! testStringNotNull(name,"product name")) {
            return new Container("ERRORVOID");
        }
        if (!barcodeStr.isEmpty()) {
            if (! testIsPositiveLong(barcodeStr)) {
                return new Container("ERRORVOID");
            } else {
                //TODO really, should parse to legit barcode
                barcode = Long.parseLong(barcodeStr);
            }
        }
        //assert barcode >= 0;
        //shouldn't need to validate Location, it came from ComboBox
        //shouldn't need to validate Borrower, it came from ComboBox
        //TODO validate date?

        //Date placeholderDate = (Date) new java.util.Date();

        //shouldn't need to validate the checkboxes

        //construct a new Container object
        Container newContainer = new Container(name);
        //assign attributes if defined
        //TODO ifdefined checks
        if(barcode != null) newContainer.barcode = barcode;
        newContainer.locationID = locID;  //database enforces this field, not optional
        newContainer.purchaseDate = sDate;
        newContainer.borrowerID = brwID;
        newContainer.sell = sell;
        newContainer.sold = sold;

        return newContainer;
    }


    // common method for container form
    private void resetContainerForm(){
        //clear the JTable selection
        containerTable.clearSelection();
        selectedContainer = -1;

        //clear the input JTextFields
        containerNameTextField.setText("");
        barCodeTextField.setText("");
        sellCheckBox.setSelected(false);
        soldCheckBox.setSelected(false);

        //reset the combo boxes
        containerLocationComboBox.setSelectedIndex(0);
        containerBorrowerComboBox.setSelectedIndex(0);

        containerDM.setValue(new java.util.Date());


    }

    //common to movie add and update
    private Movie movieFormToObject() {
        //get from fields
        String name = movieTitleTextField.getText();
        String format = movieFormatTextField.getText();
        Container container = (Container) movieContainerComboBox.getSelectedItem();
        int containerID = container.containerID;
        String genre = movieGenreTextField.getText();
        String langStr = movieLangTextField.getText();
        char[] language = new char[2];
        String yearStr = movieYearTextField.getText();
        int year = -1;
        String rating = movieRatingTextField.getText();

        //validate these
        if (! testStringNotNull(name, "movie name")) {
            return new Movie("ERRORVOID");
        }
        if (!langStr.isEmpty()) {
            if (! testIsChar2(langStr)) {
                return new Movie("ERRORVOID");
            } else {
                language = toChar2(langStr);
            }
        }
        if (!yearStr.isEmpty()){
            //TODO check for years < 1888 and error
            year = Integer.parseInt(yearStr);
        }

        //Construct new Movie object
        Movie newMovie = new Movie(name);
        //assign attributes if defined
        if(format != null) newMovie.format = format;
        newMovie.containerID = containerID;
        if(genre != null) newMovie.genre = genre;
        if(language != null) newMovie.language = language;
        if(year > -1) newMovie.year = year;
        if(rating != null) newMovie.rating = rating;

        return newMovie;
    }

    private void resetMovieForm(){
        //clear the JTable selection
        movieTable.clearSelection();
        selectedMovie = -1;

        //clear the input fields
        movieTitleTextField.setText("");
        movieFormatTextField.setText("");
        movieGenreTextField.setText("");
        movieLangTextField.setText("");
        movieYearTextField.setText("");
        movieRatingTextField.setText("");

        //reset the combo box
        //movieContainerComboBox.setSelectedIndex(0);
        //actually, don't. Leave it where it was so we can add more titles to the same container

    }

    //common to tvshow add and update
    private TVShow tvShowFormToObject() {
        //get from fields
        String name = tvShowTitleTextField.getText();
        String format = tvShowFormatTextField.getText();
        Container container = (Container) movieContainerComboBox.getSelectedItem();
        int containerID = container.containerID;
        String genre = tvShowGenreTextBox.getText();
        String langStr = tvShowLangTextBox.getText();
        char[] language = new char[2];
        String seasonStr = tvShowSeasonTextBox.getText();
        int season = -1;
        String rating = tvShowRatingTextBox.getText();

        //validate these
        if (! testStringNotNull(name, "show name")) {
            return new TVShow("ERRORVOID");
        }
        if (!langStr.isEmpty()){
            if(! testIsChar2(langStr)) {
                return new TVShow("ERRORVOID");
            } else {
                language = toChar2(langStr);
            }
        }
        if(! seasonStr.isEmpty()){
            //TODO check if zero or positive integer
            season = Integer.parseInt(seasonStr);
        }

        //Construct new TVShow object
        TVShow newShow = new TVShow(name);
        //assign attributes if defined
        if(format != null) newShow.format = format;
        newShow.containerID = containerID;
        if(genre != null ) newShow.genre = genre;
        if(language != null) newShow.language =language;
        if(season > -1) newShow.season = season;
        if(rating != null) newShow.rating = rating;

        return newShow;

    }

    private void resetTVShowForm() {
        //clear the JTable selection
        tvTable.clearSelection();
        selectedTVShow = -1;

        //clear the input fields
        tvShowTitleTextField.setText("");
        tvShowFormatTextField.setText("");
        tvShowGenreTextBox.setText("");
        tvShowLangTextBox.setText("");
        tvShowSeasonTextBox.setText("");
        tvShowRatingTextBox.setText("");

        //reset the combo box
        //tvShowContainerComboBox.setSelectedIndex(0)
        //don't though, so we can enter multiple shows in a box

    }

    //https://examples.javacodegeeks.com/core-java/util/date/java-util-date-to-java-sql-date/
    private java.sql.Date utilDateToSQLDate(java.util.Date uDate) {
        return new java.sql.Date(uDate.getTime());
    }

}
