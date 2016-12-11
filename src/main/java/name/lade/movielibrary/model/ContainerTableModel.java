package name.lade.movielibrary.model;

import name.lade.Log;

import javax.swing.table.AbstractTableModel;
import java.util.Vector;

/**
 * ContainerTableModel.java
 *
 * This models Container objects for JTable display
 */
public class ContainerTableModel extends AbstractTableModel {

    private Log log = new Log();

    private Vector<Container> allContainers;

    //Column names, displayed as table headers in the JTable

    private String[] columnNames = {
            "ID", //TODO hide this
            "Name",
            "Barcode", //TODO hide this
            "Location",
            "Purchased",
            "Loaned to",
            "Sell?",
            "Sold"
    };

    public ContainerTableModel(Vector<Container> containers) {
        this.allContainers = containers;
    }

    public void updateData(Vector<Container> updatedContainers) {
        //completely replace data in table with fresh data from DB
        this.allContainers = updatedContainers;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return allContainers.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex){
            case 0: // ID
                return allContainers.get(rowIndex).containerID;
            case 1: // Name
                return allContainers.get(rowIndex).name;
            case 2: // Barcode
                //TODO return String method?
                return allContainers.get(rowIndex).barcode;
            case 3: // LocationID
                //TODO return a Container.Location.name instead of a Container.locationID
                return allContainers.get(rowIndex).locationID;
            case 4: // Purchase Date
                //TODO format string nicely
                return allContainers.get(rowIndex).purchaseDate;
            case 5: // Borrower
                //TODO return a Container.Borrower.name instead of a Container.borrowerID
                return allContainers.get(rowIndex).borrowerID;
            case 6: // Sell flag
                return allContainers.get(rowIndex).sell;
            case 7: // Sold flag
                return allContainers.get(rowIndex).sold;
            default: //should never get here
                log.warn("Trying to access OOB column in Container");
                return null;
        }
    }

    public Container getRow(int row) {
        return allContainers.get(row);
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

}
