package name.lade.movielibrary.model;

public class Movie extends Title {
    public int year;
    public String rating; //MPAA rating or equiv.
    public String cut; //director's cut, theatrical release

    public Movie(String name) {
        super(name);
    }

    public Movie(String name, int titleID, String format, int containerID,
                 String genre, char[] language, int year, String rating,
                 String cut){
        super(name, titleID, format, containerID, genre, language);
        this.year = year;
        this.rating = rating;
        this.cut = cut;
    }
}
