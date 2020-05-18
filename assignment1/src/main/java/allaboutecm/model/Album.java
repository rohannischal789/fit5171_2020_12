package allaboutecm.model;

import allaboutecm.dataaccess.neo4j.URLConverter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.*;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * Represents an album released by ECM records.
 *
 * See {@https://www.ecmrecords.com/catalogue/143038750696/the-koln-concert-keith-jarrett}
 */
@NodeEntity
public class Album extends Entity {

    @Property(name="releaseYear")
    private int releaseYear;

    @Property(name="recordNumber")
    private String recordNumber;

    @Property(name="albumName")
    private String albumName;

    /**
     * CHANGE: instead of a set, now featuredMusicians is a list,
     * to better represent the order in which musicians are featured in an album.
     */
    @Relationship(type="featuredMusicians")
    private List<Musician> featuredMusicians;

    @Relationship(type="instruments")
    private Set<MusicianInstrument> instruments;

    @Convert(URLConverter.class)
    @Property(name="albumURL")
    private URL albumURL;

    @Relationship(type="tracks")
    private Set<Track> tracks;

    @Relationship(type="ratings")
    private Set<Rating> ratings;

    @Property(name="sales")
    private int sales;

    public Album(int releaseYear, String recordNumber, String albumName) {
        notNull(recordNumber);
        notNull(albumName);

        notBlank(recordNumber);
        notBlank(albumName);

        if (releaseYear > Calendar.getInstance().get(Calendar.YEAR) | releaseYear < 1969){
            throw new IllegalArgumentException();
        } else {

            this.releaseYear = releaseYear;
            this.recordNumber = recordNumber;
            this.albumName = albumName;
            this.sales = 0;

            this.albumURL = null;

            featuredMusicians = Lists.newArrayList();
            instruments = Sets.newHashSet();
            tracks = Sets.newHashSet();
            ratings = Sets.newHashSet();
        }
    }

    public String getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(String recordNumber) {
        notNull(recordNumber);
        notBlank(recordNumber);

        this.recordNumber = recordNumber;
    }

    public List<Musician> getFeaturedMusicians() {
        return featuredMusicians;
    }

    public void setFeaturedMusicians(List<Musician> featuredMusicians) {
        notNull(featuredMusicians);
        Iterator<Musician> itr = featuredMusicians.iterator();
        while(itr.hasNext()){
            notNull(itr.next());
        }
        this.featuredMusicians = featuredMusicians;
    }

    public Set<MusicianInstrument> getInstruments() {
        return instruments;
    }

    public void setInstruments(Set<MusicianInstrument> instruments) {
        notNull(instruments);
        Iterator<MusicianInstrument> itr = instruments.iterator();
        while(itr.hasNext()){
            notNull(itr.next());
        }
        this.instruments = instruments;
    }

    public URL getAlbumURL() {
        return albumURL;
    }

    public void setAlbumURL(URL albumURL) {
        notNull(albumURL);
        String checkURL = albumURL.toString();
        if (!(checkURL.contains("https://www.ecm.com/"))){
            throw new IllegalArgumentException();
        }
        this.albumURL = albumURL;
    }

    public Set<Track> getTracks() {
        return tracks;
    }

    public void setTracks(Set<Track> tracks) {
        notNull(tracks);
        Iterator<Track> itr = tracks.iterator();
        while(itr.hasNext()){
            notNull(itr.next());
        }
        this.tracks = tracks;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        if(sales < 0)
        {
            throw new IllegalArgumentException();
        }
        this.sales = sales;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        if (releaseYear > Calendar.getInstance().get(Calendar.YEAR) | releaseYear < 1969){
            throw new IllegalArgumentException();
        } else {
            this.releaseYear = releaseYear;
        }
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        notNull(albumName);
        notBlank(albumName);

        this.albumName = albumName;
    }

    public Set<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(Set<Rating> ratings) {
        notNull(ratings);
        Iterator<Rating> itr = ratings.iterator();
        while(itr.hasNext()){
            notNull(itr.next());
        }
        this.ratings = ratings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return releaseYear == album.releaseYear &&
                recordNumber.equals(album.recordNumber) &&
                albumName.equals(album.albumName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(releaseYear, recordNumber, albumName);
    }
}
