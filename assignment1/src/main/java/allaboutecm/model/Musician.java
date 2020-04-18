package allaboutecm.model;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.ObjectUtils;

import java.net.URL;
import java.util.Objects;
import java.util.Set;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * An artist that has been featured in (at least) one ECM record.
 *
 * See {@https://www.ecmrecords.com/artists/1435045745}
 */
public class Musician extends Entity {
    private String name;

    private URL musicianUrl;

    private Set<Album> albums;

    public Musician(String name) {
        notNull(name);
        notBlank(name);

        String[] substring = name.split(" ");
        if(substring.length==1)
        {
           throw new IllegalArgumentException();
        }

        this.name = name;
        this.musicianUrl = null;

        albums = Sets.newLinkedHashSet();
    }

    public String getName() {
        return name;
    }

    public Set<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(Set<Album> albums) {
        //TODO Check for empty Set and write test
        if (albums == null) {
            throw new NullPointerException();
        } else {
            this.albums = albums;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Musician that = (Musician) o;
        return (Objects.equals(name, that.name) & Objects.equals(musicianUrl, that.getMusicianUrl()) & Objects.equals(albums, that.getAlbums())); //Delete later?
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public URL getMusicianUrl() {
        return musicianUrl;
    }

    public void setMusicianUrl(URL musicianUrl) {
        //TODO Check not blank and test
        this.musicianUrl = musicianUrl;
    }
}
