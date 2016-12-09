package name.lade.movielibrary.model;

import name.lade.Log;

import javax.swing.table.AbstractTableModel;
import java.util.Vector;

/**
 * MovieTableModel.java
 *
 * This models Movie objects for JTable display
 */
public class MovieTableModel extends AbstractTableModel {

    private Log log = new Log();

    private Vector<Movie> allMovies;

    //Column names, displayed as table headers in the JTable

    private String[] columnNames = {
            "ID", //TODO hide this
            "Title",
            "Format",
            "In Box", //TODO hide this?
            "Genre",
            "Lang",
            "Year",
            "Rating",
            "Cut"
    };

    public MovieTableModel(Vector<Movie> movies) {
        this.allMovies = movies;
    }

    void updateData(Vector<Movie> updatedMovies) {
        //completely replace data in table fresh DB data
        this.allMovies = updatedMovies;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return allMovies.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0: // ID
                return allMovies.get(rowIndex).titleID;
            case 1: // Title
                return allMovies.get(rowIndex).name;
            case 2: // Format
                return allMovies.get(rowIndex).format;
            case 3: // ContainerID
                //TODO query Container and return name field
                return allMovies.get(rowIndex).containerID;
            case 4: // Genre
                return allMovies.get(rowIndex).genre;
            case 5: // Language
                //TODO some kind of lookup table for long string?
                //this returns a char[2]
                return allMovies.get(rowIndex).language;
            case 6: // Year
                return allMovies.get(rowIndex).year;
            case 7: // Rating
                return allMovies.get(rowIndex).cut;
            default: //should never get here
                log.warn("Trying to access OOB column in Movie");
                return null;
        }
    }

    Movie getMovieAtRow(int rowIndex){
        return allMovies.get(rowIndex);
    }

    @Override
    public String getColumnName(int col){
        return columnNames[col];
    }

}
