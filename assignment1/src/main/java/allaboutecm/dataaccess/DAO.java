package allaboutecm.dataaccess;

import allaboutecm.model.*;

import java.util.Collection;

public interface DAO {
    <T extends Entity> T load(Class<T> clazz, Long id);

    <T extends Entity> T createOrUpdate(T entity);

    <T extends Entity> Collection<T> loadAll(Class<T> clazz);

    <T extends Entity> void delete(T entity);

    Musician findMusicianByName(String name);
    MusicalInstrument findMusicalInstrumentByName(String name);
    Album findAlbumByName(String name);
    Album findAlbumByReleaseYear(int releaseYear);
    Album findAlbumByRecordNumber(String recordNumber);
    Album findAlbumBySales(int sales);
    Track findTrackByName(String name);
    Track findTrackByDuration(String duration);
    Track findTrackByGenre(String genre);
    Track findTrackByTrackNumber(int trackNumber);
    Rating findRatingByScore(int score);
    Rating findRatingBySource(String source);
}
