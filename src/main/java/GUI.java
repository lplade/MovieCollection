import javax.naming.ldap.Control;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
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

    //https://docs.oracle.com/javase/tutorial/uiswing/components/tabbedpane.html
    //https://docs.oracle.com/javase/tutorial/uiswing/components/toolbar.html
    //http://www.oracle.com/technetwork/java/index-138612.html

    private ContainerTableModel containerTM;
    private MovieTableModel movieTM;
    private TVTableModel tvshowTM;

    private Controller controller;

    private int selectedRecord;

    GUI(Controller controller) {
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
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

    }

    private void addListeners() {

        //watch if user selects a row
        //http://stackoverflow.com/questions/10128064/jtable-selected-row-click-event
        containerTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                //first make sure item is not out of bounds
                //listener fires when clearSelection() fires and causes oobe error is not tested
                //http://stackoverflow.com/questions/13102246/remove-the-selection-from-a-jlist-in-java-swing
                if (!e.getValueIsAdjusting() && containerTable.getSelectedRow() >= 0){

                    //grab the contents of the selected record
                    String c_ID = containerTable.getValueAt(containerTable.getSelectedRow(), 1).toString();
                    String name = containerTable.getValueAt(containerTable.getSelectedRow(), 2).toString();
                    String barcode = containerTable.getValueAt(containerTable.getSelectedRow(), 3).toString();
                    //TODO get name field for Location and display it here instead
                    String l_ID = containerTable.getValueAt(containerTable.getSelectedRow(), 4).toString();
                    String pDate = containerTable.getValueAt(containerTable.getSelectedRow(), 5).toString();
                    String loan = containerTable.getValueAt(containerTable.getSelectedRow(), 6).toString();
                    //return as Yes/No?
                    boolean sell = (boolean) containerTable.getValueAt(containerTable.getSelectedRow(), 7);
                    boolean sold = (boolean) containerTable.getValueAt(containerTable.getSelectedRow(), 8);

                    //display these SOMEWHERE
                    //TODO setText() on some fields

                    //update the index records
                    int id;
                    try {
                        id = (int) containerTable.getValueAt(containerTable.getSelectedRow(),0);

                    } catch (ArrayIndexOutOfBoundsException oobe) {
                        //when we clear the selection, the listener fires and returns an invalid value here
                        id = -1;
                    }

                    selectedRecord = id;

                    //leave these there until the user updates

                    //TODO toggle availability of GUI items

                }
            }
        });

    }

    public void setContainerListData(Vector<Container> allContainers) {
        containerTM.updateData(allContainers);
    }

    public void setMovieListData(Vector<Movie> allMovies) {
    }

    public void setTVShowListData(Vector<TVShow> allShows) {
    }
}
