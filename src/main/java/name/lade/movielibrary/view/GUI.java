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
    private JComboBox<Integer> pDateYYYYComboBox;
    private JComboBox<Integer> pDateMMComboBox;
    private JComboBox<Integer> pDateDDComboBox;
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
    private JTextField tvShowYearTextBox;
    private JTextField tvShowRatingTextBox;
    private JComboBox<Borrower> containerBorrowerComboBox;
    private JButton movieDeleteButton;
    private JButton tvShowDeleteButton;

    //https://docs.oracle.com/javase/tutorial/uiswing/components/tabbedpane.html
    //https://docs.oracle.com/javase/tutorial/uiswing/components/toolbar.html
    //http://www.oracle.com/technetwork/java/index-138612.html

    private ContainerTableModel containerTM;
    private MovieTableModel movieTM;
    private TVTableModel tvshowTM;

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
                    //TODO parse date into YYY, MM, and DD -OR- set up date picker
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
                //get from fields
                String name = containerNameTextField.getText();
                String barcodeStr = barCodeTextField.getText();
                Long barcode = null;
                Location location = (Location) containerLocationComboBox.getSelectedItem();
                int locID = location.locationID;
                //TODO something with date
                Borrower borrower = (Borrower) containerBorrowerComboBox.getSelectedItem();
                int brwID = borrower.borrowerID;
                boolean sell = sellCheckBox.isSelected();
                boolean sold = soldCheckBox.isSelected();

                //validate these
                if (! testStringNotNull(name,"product name")) return;
                if (!barcodeStr.isEmpty()) {
                    if (! testIsPositiveLong(barcodeStr)) {
                        return;
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
                //newContainer.purchaseDate = placeholderDate;
                newContainer.borrowerID = brwID;
                newContainer.sell = sell;
                newContainer.sold = sold;

                //add the Container to the database
                controller.addContainerToDatabase(newContainer);

                //clear the JTable selection
                containerTable.clearSelection();

                //clear the input JTextFields
                containerNameTextField.setText("");
                barCodeTextField.setText("");
                sellCheckBox.setSelected(false);
                soldCheckBox.setSelected(false);

                //reset the combo boxes
                containerLocationComboBox.setSelectedIndex(0);
                containerBorrowerComboBox.setSelectedIndex(0);

                //refresh to reflect the changes
                Vector<Container> allContainers = controller.getAllContainers();
                setContainerListData(allContainers);

            }
        });

        updateContainerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedContainer == -1) {
                    //we should not be able to get here once the button disabling logic is in place
                    return;
                }

                //get from fields
                String name = containerNameTextField.getText();
                String barcodeStr = barCodeTextField.getText();
                Long barcode = null;
                Location location = (Location) containerLocationComboBox.getSelectedItem();
                int locID = location.locationID;
                //TODO something with date
                Borrower borrower = (Borrower) containerBorrowerComboBox.getSelectedItem();
                int brwID = borrower.borrowerID;
                boolean sell = sellCheckBox.isSelected();
                boolean sold = soldCheckBox.isSelected();

                //validate these
                if (! testStringNotNull(name,"product name")) return;
                if (!barcodeStr.isEmpty()) {
                    if (! testIsPositiveLong(barcodeStr)) {
                        return;
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
                //newContainer.purchaseDate = placeholderDate;
                newContainer.borrowerID = brwID;
                newContainer.sell = sell;
                newContainer.sold = sold;

                //update the Container in the database
                //selectedContainer = .containerID selected, should have been set by ListSelectionListener
                controller.updateContainer(selectedContainer, newContainer);

                //clear the JTable selection
                containerTable.clearSelection();

                //clear the input JTextFields
                containerNameTextField.setText("");
                barCodeTextField.setText("");
                sellCheckBox.setSelected(false);
                soldCheckBox.setSelected(false);

                //reset the combo boxes
                containerLocationComboBox.setSelectedIndex(0);
                containerBorrowerComboBox.setSelectedIndex(0);

                //refresh to reflect the changes
                Vector<Container> allContainers = controller.getAllContainers();
                setContainerListData(allContainers);
            }
        });

        clearContainerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //reset all form fields
                containerNameTextField.setText("");
                barCodeTextField.setText("");
                sellCheckBox.setSelected(false);
                soldCheckBox.setSelected(false);
                containerLocationComboBox.setSelectedIndex(0);
                containerBorrowerComboBox.setSelectedIndex(0);


                //clear the JTable selection
                containerTable.clearSelection();
                selectedContainer = -1;

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
                    movieRatingTextField.setText(String.valueOf(movie.rating));


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
                //get from fields
                String name = movieTitleTextField.getText();
                String format = movieFormatTextField.getText();
                Container container = (Container) movieContainerComboBox.getSelectedItem();
                int containerID = container.containerID;
                String genre = movieGenreTextField.getText();
                String langStr = movieLangTextField.getText();
                char[] language = new char[2];
                //TODO convert to char[2];
                String yearStr = movieYearTextField.getText();
                int year = -1;
                String rating = movieRatingTextField.getText();

                //validate these
                if (! testStringNotNull(name, "movie name")) return;
                if (!langStr.isEmpty()) {
                    if (! testIsChar2(langStr)) {
                        return;
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
                //assign attributes if define
                if(format != null) newMovie.format = format;
                newMovie.containerID = containerID;
                if(genre != null) newMovie.genre = genre;
                if(language != null) newMovie.language = language;
                if(year > -1) newMovie.year = year;
                if(rating != null) newMovie.rating = rating;

                //add the Movie to the database
                controller.addMovie(newMovie);

                //clear the JTable selection
                movieTable.clearSelection();

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

                //refresh to reflect the changes
                Vector<Movie> allMovies = controller.getAllMovies();
                setMovieListData(allMovies);


            }
        });
        updateMovieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO implement

            }
        });
        clearMovieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO implement

            }
        });
    }



    //*** Listeners for TV Show tab ***
    private void addTVShowTabListeners() {

        addTVShowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO implement

            }
        });
        updateTVShowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO implement

            }
        });
        clearTVShowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO implement

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


}
