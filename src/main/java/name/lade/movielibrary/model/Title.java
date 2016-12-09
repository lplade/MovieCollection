package name.lade.movielibrary.model;

/**
 * Title.java
 *
 * This class contains information about individual media titles.
 * This can be a movie, a TV show, etc.
 *
 * This is the main object used for presenting information to the user.
 *
 * There are subclasses which contain additional fields that apply to Movies and TVShows
 */
public class Title {
    //Fields common to all media titles
    public int titleID = -1;
    public String name;
    public String format; //Blu-ray, DVD, digital
    public int containerID;
    public String genre; //comedy, horror, scifi
    public char[] language = new char[2]; // ISO code: en, de, jp

    Title(String name) {
        this.name = name;
    }

    public String getLanguageStr() {
        return new String(language);

    }
}

