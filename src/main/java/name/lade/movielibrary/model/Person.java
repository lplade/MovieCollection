package name.lade.movielibrary.model;

/**
 * Person.java
 *
 * This class stores biographical details.
 * It is used for directors, producers, actors, etc.
 *
 * We should use API or scraper to populate this information automatically for a given title
 */
public class Person {
    public int personID = -1;
    public String surname;
    public String givenName;
    public int tmdbID; //https://developers.themoviedb.org/3/people
    public int imdbNM; //IMDB

    Person(String surname, String givenName) {
        this.surname = surname;
        this.givenName = givenName;
    }
}
