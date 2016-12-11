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

    @Override
    public String toString(){
        String nameYear = this.name;
        //append the year if it's defined
        //motion pictures didn't exist prior to 1889
        if(this.year > 1888) nameYear += " (" + this.year + ")";
        return nameYear;
    }
}
