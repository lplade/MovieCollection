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
class Title {
    //Fields common to all media titles
    int titleID = -1;
    String name;
    String format; //Blu-ray, DVD, digital
    int containerID;
    String genre; //comedy, horror, scifi
    char[] language = new char[2]; // ISO code: en, de, jp

    Title(String name) {
        this.name = name;
    }

    public String getLanguageStr() {
        return new String(language);

    }
}

class Movie extends Title {
    int year;
    String rating; //MPAA rating or equiv.
    String cut; //director's cut, theatrical release

    Movie(String name) {
        super(name);
    }
}

class TVShow extends Title {
    //each object here refers to an individual season of a tv show.
    //TODO extend to also store episode information
    int season; //correspond to season number on thetvdb.com
    String rating; //TV-14 or such

    TVShow(String name) {
        super(name);
    }
}