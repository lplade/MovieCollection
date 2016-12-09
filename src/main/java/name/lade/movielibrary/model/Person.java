package name.lade.movielibrary.model;

/**
 * Person.java
 *
 * This class stores biographical details.
 * It is used for directors, producers, actors, etc.
 *
 * We should use API or scraper to populate this information automatically for a given title
 */
class Person {
    int personID = -1;
    String surname;
    String givenName;
    int tmdbID; //https://developers.themoviedb.org/3/people
    int imdbNM; //IMDB

    Person(String surname, String givenName) {
        this.surname = surname;
        this.givenName = givenName;
    }
}
