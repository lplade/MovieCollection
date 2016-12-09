package name.lade.movielibrary.model;

public class TVShow extends Title {
    //each object here refers to an individual season of a tv show.
    //TODO extend to also store episode information
    public int season; //correspond to season number on thetvdb.com
    public String rating; //TV-14 or such

    TVShow(String name) {
        super(name);
    }
}
