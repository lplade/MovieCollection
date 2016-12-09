package name.lade.movielibrary.model;

public class Movie extends Title {
    public int year;
    public String rating; //MPAA rating or equiv.
    public String cut; //director's cut, theatrical release

    Movie(String name) {
        super(name);
    }
}
