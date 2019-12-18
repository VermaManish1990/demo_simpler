package example.com.demo.model;

import java.util.ArrayList;

public class AlbumDataModel {

    public int limit;
    public String next;
    public int offset;
    public int total;
    public ArrayList<Items> items;
    public boolean isFailure;
    public String error;


    public class Items {
        public String added_at;
        public Album album;
    }

    public class Album {
        public ArrayList<Artist> artists;
        public ArrayList<Images> images;
        public String name;
        public int popularity;
        public String release_date;
        public String type;
        public Tracks tracks;
        public long duration_ms;

    }

    public class Artist {
        public String id;
        public String name;
        public String type;
        public String url;
    }

    public class Images {
        public int height;
        public int width;
        public String url;
    }

    public class Tracks {
        public ArrayList<Album> items;
    }
}
