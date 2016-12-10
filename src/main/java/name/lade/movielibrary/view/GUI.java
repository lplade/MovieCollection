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
    private JButton newShelfItemButton;
    private JButton updateContainerButton;
    private JButton deleteButton;
    private JTextField nameTextField;
    private JTextField barCodeTextField;
    private JComboBox locationComboBox;
    private JComboBox pDateYYYYComboBox;
    private JComboBox pDateMMComboBox;
    private JComboBox pDateDDComboBox;
    private JCheckBox sellCheckBox;
    private JCheckBox soldCheckBox;
    private JButton clearContainerButton;
    private JButton addMovieButton;
    private JButton updateMovieButton;
    private JButton clearMovieButton;
    private JTextField movieTitleTextField;
    private JTextField movieFormatTextField;
    private JComboBox movieContainerComboBox;
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
    private JComboBox tvShowContainerComboBox;
    private JTextField tvShowGenreTextBox;
    private JTextField tvShowLangTextBox;
    private JTextField tvShowYearTextBox;
    private JTextField tvShowRatingTextBox;

    //https://docs.oracle.com/javase/tutorial/uiswing/components/tabbedpane.html
    //https://docs.oracle.com/javase/tutorial/uiswing/components/toolbar.html
    //http://www.oracle.com/technetwork/java/index-138612.html

    private ContainerTableModel containerTM;
    private MovieTableModel movieTM;
    private TVTableModel tvshowTM;

    private Controller controller;

    private int selectedRecord;

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

        selectedRecord = -1 ; //this means no record is selected

        addListeners();
        addContainerTabListeners();
        addMovieTabListeners();
        addTVShowTabListeners();
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

    }



    // Listeners for whole window
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

    //Listeners for the container tab
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
                    nameTextField.setText(container.name);
                    barCodeTextField.setText(String.valueOf(container.barcode));
                    //TODO location field
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

                    selectedRecord = id;

                    //leave these there until the user updates

                    //TODO toggle availability of GUI items

                }
            }
        });

        newShelfItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //get from fields
                String name = nameTextField.getText();
                String barcodeStr = barCodeTextField.getText();
                Long barcode = null;
                //TODO something with location
                //TODO something with date
                int borrowerID; //TODO make a form field!
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
                //TODO validate location?
                //TODO validate date?

                //Date placeholderDate = (Date) new java.util.Date();

                //shouldn't need to validate the checkboxes


                //construct a new Container object
                name.lade.movielibrary.model.Container newContainer = new Container(name);
                //assign attributes if defined
                //TODO ifdefined checks
                if(barcode != null) newContainer.barcode = barcode;
                //newContainer.locationID
                //newContainer.purchaseDate = placeholderDate;
                //newContainer.borrowerID = borrowerID;
                newContainer.sell = sell;
                newContainer.sold = sold;

                //update the Container in the database
                controller.addContainerToDatabase(newContainer);

                //clear the JTable selection
                containerTable.clearSelection();

                //clear the input JTextFields
                nameTextField.setText("");
                barCodeTextField.setText("");
                sellCheckBox.setSelected(false);
                soldCheckBox.setSelected(false);

                //refresh to reflect the changes
                Vector<Container> allContainers = controller.getAllContainers();
                setContainerListData(allContainers);

            }
        });

        updateContainerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO implement
            }
        });

        clearContainerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO clear the text fields
                //TODO set selectedRecord back to -1
                //TODO unselect JTable if needed
                //TODO re-enable Add button if needed
                //TODO disable the Update button
                //TODO disable the Delete button
            }
        });


    }

    //Listeners for Movie tab
    private void addMovieTabListeners() {
        addMovieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        updateMovieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        clearMovieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    //Listeners for TV Show tab
    private void addTVShowTabListeners() {

        addTVShowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        updateTVShowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        clearTVShowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

    }

    public void setContainerListData(Vector<Container> allContainers) {
        containerTM.updateData(allContainers);
    }

    public void setMovieListData(Vector<Movie> allMovies) {
        //TODO set up
    }

    public void setTVShowListData(Vector<TVShow> allShows) {
        //TODO set up
    }


    //helper methods

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


}
