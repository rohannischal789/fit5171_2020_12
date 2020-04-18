package allaboutecm.model;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

public class Track extends Entity {

    private String name;

    private String duration;

    private String genre;

    private int trackNumber;

    private List<String> reviews;

    private double rating;

    public Track(String name, String duration, String genre, int trackNumber) {
        notNull(name);
        notNull(duration);
        notNull(genre);

        notBlank(name);
        notBlank(duration);
        notBlank(genre);

        if(trackNumber <= 0)
        {
            throw new IllegalArgumentException();
        }

        this.name = name;
        this.duration = duration;
        this.genre = genre;
        this.trackNumber = trackNumber;
        this.reviews = Lists.newArrayList();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        notNull(name);
        notBlank(name);
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        notNull(duration);
        notBlank(duration);
        this.duration = duration;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        notNull(genre);
        notBlank(genre);
        this.genre = genre;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(int trackNumber) {
        if(trackNumber <= 0)
        {
            throw new IllegalArgumentException();
        }
        this.trackNumber = trackNumber;
    }

    public List<String> getReviews() {
        return reviews;
    }

    public void setReviews(List<String> reviews) {
        notNull(reviews);
        this.reviews = reviews;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Track track = (Track) o;
        return name.equals(track.name) &&
                duration == track.duration &&
                trackNumber == track.trackNumber &&
                genre.equals(track.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, duration, trackNumber, genre);
    }

}
