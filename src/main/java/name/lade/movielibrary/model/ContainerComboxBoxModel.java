package name.lade.movielibrary.model;

import name.lade.Log;

import javax.swing.*;
import java.util.Vector;

/**
 * ContainerComboBoxModel.java
 *
 * This populates container drop-down lists with names of Containers
 *
 */
public class ContainerComboxBoxModel extends AbstractListModel implements ComboBoxModel {

    private Log log = new Log();

    private Vector<Container> allContainers;

    public ContainerComboxBoxModel(Vector<Container> containers) {
        this.allContainers = containers;
    }

    //TODO implement this

    @Override
    public void setSelectedItem(Object anItem) {

    }

    @Override
    public Object getSelectedItem() {
        return null;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public Object getElementAt(int index) {
        return null;
    }
}
