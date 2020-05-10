package allaboutecm.model;

import allaboutecm.dataaccess.neo4j.URLConverter;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.ObjectUtils;

import java.net.URL;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * An artist that has been featured in (at least) one ECM record.
 *
 * See {@https://www.ecmrecords.com/artists/1435045745}
 */
@NodeEntity
public class Musician extends Entity {
    @Property(name="name")
    private String name;

    @Convert(URLConverter.class)
    @Property(name="musicianURL")
    private URL musicianUrl;

    @Relationship(type="albums")
    private Set<Album> albums;

    //Extension

    private String bio;

    private URL personalSite;

    private URL wikiPage;

    public Musician(String name) {
        //When we make a Musician is the only time we set the name and it must be not null and not blank
        notNull(name);
        notBlank(name);

        String[] substring = name.split(" ");
        //Split the string by spaces into a list of strings. If we only have one element in the list it doesn't follow the minimum firstname lastname constraint
        if(substring.length==1)
        {
           throw new IllegalArgumentException();
        }
        if (name.length() > 100){
            throw new IllegalArgumentException();
        }
        this.name = name;
        this.musicianUrl = null;

        albums = Sets.newLinkedHashSet();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        notNull(name);
        notBlank(name);
        String[] substring = name.split(" ");
        //Split the string by spaces into a list of strings. If we only have one element in the list it doesn't follow the minimum firstname lastname constraint
        if(substring.length == 1)
        {
            throw new IllegalArgumentException();
        }
        if (name.length() > 100){
            throw new IllegalArgumentException();
        }
        this.name = name;
    }

    public Set<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(Set<Album> albums) {
        //The album list can't be set to null, and no element of the list may be set to null.
        notNull(albums);
        Iterator<Album> itr = albums.iterator();
        while(itr.hasNext()){
            notNull(itr.next());
        }
        this.albums = albums;

    }

    @Override
    public boolean equals(Object o) {
        //A musician object is the same as another musician object if and only if the names, album lists and url match.
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

    public void setMusicianUrl(URL musicianUrl){
        //The URL submitted must be on our website, the first 19 characters must be https://www.ecm.com/, anything after that may be the band name.
        //We could avoid hardcoding the string in a real project by referring to some global constant
        if (!musicianUrl.toString().contains("https://www.ecm.com/")){
            throw new IllegalArgumentException();
        }
        this.musicianUrl = musicianUrl;
    }


    //Extension functionality
    //bio is a string attribute with set get methods. This is intended to represent a short biography of the band,
    //bio has the constraints that it must not be blank, and is bounded at 500 words. Bio may be null.
    public void setBio(String bio){
        notBlank(bio);
        String[] bioWords = bio.split(" ");
        if (bioWords.length > 500){
            throw new IllegalArgumentException();
        }
        this.bio = bio;
    }

    public String getBio(){return this.bio;}
    //personalSite is a URL attribute with set get methods representing the personal website of the band.
    //personalSite has the constraint that it may not begin with the substrings https://www.ecm.com/ or http://xx.wikipedia.org/wiki/
    public void setPersonalSite(URL site) {
        String siteString = site.toString();
        if (siteString.contains("www.ecm.com") || siteString.contains("wikipedia.org")) {
                throw new IllegalArgumentException();
        }
        this.personalSite = site;
    }

    public URL getPersonalSite(){return this.personalSite;}
    //wikiPage is a URL attribute with set get methods representing the wikipedia page of the band.
    //wikiPage has the constraint that it must begin with a substring following the format http://xx.wikipedia.org/wiki/
    public void setWikiPage (URL wikiPage){
        String siteString = wikiPage.toString();
        if (!siteString.contains("wikipedia.org")) {
                throw new IllegalArgumentException();
        }
        this.wikiPage = wikiPage;
    }

    public URL getWikiPage(){return this.wikiPage;}

}
