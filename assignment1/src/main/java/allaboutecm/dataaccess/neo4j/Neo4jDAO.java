package allaboutecm.dataaccess.neo4j;

import allaboutecm.dataaccess.DAO;
import allaboutecm.model.*;
import com.google.common.collect.Sets;
import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.cypher.Filters;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.transaction.Transaction;

import java.util.Collection;

import static org.neo4j.ogm.cypher.ComparisonOperator.EQUALS;

public class Neo4jDAO implements DAO {
    private static final int DEPTH_LIST = 0;
    private static final int DEPTH_ENTITY = 1;

    private Session session;

    public Neo4jDAO(Session session) {
        this.session = session;
    }

    @Override
    public <T extends Entity> T load(Class<T> clazz, Long id) {
        return session.load(clazz, id, DEPTH_ENTITY);
    }

    @Override
    public <T extends Entity> T createOrUpdate(T entity) {
        Class clazz = entity.getClass();

        T existingEntity = findExistingEntity(entity, clazz);
        if (null != existingEntity) {
            entity.setId(existingEntity.getId());
        }
        Transaction tx = session.beginTransaction();
        session.save(entity, DEPTH_ENTITY);
        tx.commit();
        return entity;
    }

    @Override
    public <T extends Entity> Collection<T> loadAll(Class<T> clazz) {
        return session.loadAll(clazz, DEPTH_LIST);


    }

    @Override
    public <T extends Entity> void delete(T entity) {
        session.delete(entity);
    }

    @Override
    public Musician findMusicianByName(String name) {
        Filters filters = new Filters();
        filters.add(new Filter("name", EQUALS, name));
        Collection<Musician> musicians = session.loadAll(Musician.class, filters);
        if (musicians.isEmpty()) {
            return null;
        } else {
            return musicians.iterator().next();
        }
    }



    @Override
    public Album findAlbumByName(String name) {
        Filters filters = new Filters();
        filters.add(new Filter("albumName", EQUALS, name));
        Collection<Album> album = session.loadAll(Album.class, filters);
        if (album.isEmpty()) {
            return null;
        } else {
            return album.iterator().next();
        }
    }

    @Override
    public Album findAlbumByReleaseYear(int releaseYear) {
        Filters filters = new Filters();
        filters.add(new Filter("releaseYear", EQUALS, releaseYear));
        Collection<Album> album = session.loadAll(Album.class, filters);
        if (album.isEmpty()) {
            return null;
        } else {
            return album.iterator().next();
        }
    }

    @Override
    public Album findAlbumByRecordNumber(String recordNumber) {
        Filters filters = new Filters();
        filters.add(new Filter("recordNumber", EQUALS, recordNumber));
        Collection<Album> album = session.loadAll(Album.class, filters);
        if (album.isEmpty()) {
            return null;
        } else {
            return album.iterator().next();
        }
    }

    @Override
    public Album findAlbumBySales(int sales) {
        Filters filters = new Filters();
        filters.add(new Filter("sales", EQUALS, sales));
        Collection<Album> album = session.loadAll(Album.class, filters);
        if (album.isEmpty()) {
            return null;
        } else {
            return album.iterator().next();
        }
    }

    public MusicalInstrument findMusicalInstrumentByName(String name) {
        Filters filters = new Filters();
        filters.add(new Filter("name", EQUALS, name));
        Collection<MusicalInstrument> musicalInstruments = session.loadAll(MusicalInstrument.class, filters);
        if (musicalInstruments.isEmpty()) {
            return null;
        } else {
            return musicalInstruments.iterator().next();
        }
    }

    public Track findTrackByName(String name) {
        Filters filters = new Filters();
        filters.add(new Filter("name", EQUALS, name));
        Collection<Track> tracks = session.loadAll(Track.class, filters);
        if (tracks.isEmpty()) {
            return null;
        } else {
            return tracks.iterator().next();
        }
    }

    public Track findTrackByDuration(String duration) {
        Filters filters = new Filters();
        filters.add(new Filter("duration", EQUALS, duration));
        Collection<Track> tracks = session.loadAll(Track.class, filters);
        if (tracks.isEmpty()) {
            return null;
        } else {
            return tracks.iterator().next();
        }
    }

    public Track findTrackByGenre(String genre) {
        Filters filters = new Filters();
        filters.add(new Filter("genre", EQUALS, genre));
        Collection<Track> tracks = session.loadAll(Track.class, filters);
        if (tracks.isEmpty()) {
            return null;
        } else {
            return tracks.iterator().next();
        }
    }

    @Override
    public Rating findRatingByScore(int score) {
        Filters filters = new Filters();
        filters.add(new Filter("ratingScore", EQUALS, score));
        Collection<Rating> ratings = session.loadAll(Rating.class, filters);
        if (ratings.isEmpty()) {
            return null;
        } else {
            return ratings.iterator().next();
        }
    }

    @Override
    public Rating findRatingBySource(String source) {
        Filters filters = new Filters();
        filters.add(new Filter("source", EQUALS, source));
        Collection<Rating> ratings = session.loadAll(Rating.class, filters);
        if (ratings.isEmpty()) {
            return null;
        } else {
            return ratings.iterator().next();
        }
    }

    public Track findTrackByTrackNumber(int trackNumber) {
        Filters filters = new Filters();
        filters.add(new Filter("trackNumber", EQUALS, trackNumber));
        Collection<Track> tracks = session.loadAll(Track.class, filters);
        if (tracks.isEmpty()) {
            return null;
        } else {
            return tracks.iterator().next();
        }
    }

    private <T extends Entity> T findExistingEntity(Entity entity, Class clazz) {
        Filters filters = new Filters();
        Collection<? extends Entity> collection = Sets.newLinkedHashSet();
        if (clazz.equals(Album.class)) {
            // Album
            Album album = (Album) entity;
            filters.add(new Filter("albumName", EQUALS, album.getAlbumName())
                    .and(new Filter("recordNumber", EQUALS, album.getRecordNumber()))
                    .and(new Filter("releaseYear", EQUALS, album.getReleaseYear())));
            collection = session.loadAll(Album.class, filters);
        } else if (clazz.equals(Musician.class)) {
            // Musician
            Musician musician = (Musician) entity;
            filters.add(new Filter("name", EQUALS, musician.getName()));
            collection = session.loadAll(Musician.class, filters);
        } else if (clazz.equals(MusicalInstrument.class)) {
            // MusicalInstrument
            MusicalInstrument musicalInstrument = (MusicalInstrument) entity;
            filters.add(new Filter("name", EQUALS, musicalInstrument.getName()));
            collection = session.loadAll(MusicalInstrument.class, filters);
        } else if (clazz.equals(MusicianInstrument.class)) {
            // MusicianInstrument
            MusicianInstrument musicianInstrument = (MusicianInstrument) entity;
            filters.add(new Filter("musician", EQUALS, musicianInstrument.getMusician()))
                    .and(new Filter("musicalInstruments", EQUALS, musicianInstrument.getMusicalInstruments()));
            collection = session.loadAll(MusicianInstrument.class, filters);
        } else if (clazz.equals(Track.class)) {
            Track track = (Track) entity;
            filters.add(new Filter("name", EQUALS, track.getName()))
                    .and(new Filter("duration", EQUALS, track.getDuration()))
                    .and(new Filter("genre", EQUALS, track.getGenre()))
                    .and(new Filter("trackNumber", EQUALS, track.getTrackNumber()));
            collection = session.loadAll(Track.class, filters);
        } else if (clazz.equals(Rating.class)) {
            Rating rating = (Rating) entity;
            filters.add(new Filter("ratingScore", EQUALS, rating.getRatingScore()))
                    .and(new Filter("source", EQUALS, rating.getSource()));
            collection = session.loadAll(Rating.class, filters);
        }
        Entity existingEntity = null;
        if (!collection.isEmpty()) {
            existingEntity = collection.iterator().next();
        }
        return (T) existingEntity;
    }
}
