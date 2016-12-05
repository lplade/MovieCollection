import javax.swing.table.AbstractTableModel;
import java.util.Vector;

/**
 * TVTableModel.java
 *
 * This models TVShow objects for JTable display
 */
public class TVTableModel extends AbstractTableModel {

    private Log log = new Log();

    private Vector<TVShow> allShows;

    //Column names, displayed as table headers in the JTable

    private String[] columnNames = {
            "ID", //TODO hide this
            "Title",
            "Format",
            "In Box", //TODO hide this?
            "Genre",
            "Lang",
            "Season",
            "Rating"
    };

    TVTableModel(Vector<TVShow> shows) {
        this.allShows = shows;
    }

    void updateData(Vector<TVShow> updatedShows) {
        //completely replace data in table fresh DB data
        this.allShows = updatedShows;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return allShows.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0: // ID
                return allShows.get(rowIndex).titleID;
            case 1: // Title
                return allShows.get(rowIndex).name;
            case 2: // Format
                return allShows.get(rowIndex).format;
            case 3: // ContainerID
                //TODO query Container and return name field
                return allShows.get(rowIndex).containerID;
            case 4: // Genre
                return allShows.get(rowIndex).genre;
            case 5: // Language
                //TODO some kind of lookup table for long string?
                //this returns a char[2]
                return allShows.get(rowIndex).language;
            case 6: // Season
                return allShows.get(rowIndex).season;
            case 7: // Rating
                return allShows.get(rowIndex).rating;
            default: //should never get here
                log.warn("Trying to access OOB column in Movie");
                return null;
        }
    }

    TVShow getShowAtRow(int rowIndex){
        return allShows.get(rowIndex);
    }

    @Override
    public String getColumnName(int col){
        return columnNames[col];
    }

}
