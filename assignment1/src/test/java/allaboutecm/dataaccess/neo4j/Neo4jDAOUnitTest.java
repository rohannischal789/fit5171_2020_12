package allaboutecm.dataaccess.neo4j;

import allaboutecm.dataaccess.DAO;
import allaboutecm.model.*;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.ogm.support.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class Neo4jDAOUnitTest {
    private static final String TEST_DB = "target/test-data/test-db.neo4j";

    private static DAO dao;
    private static Session session;
    private static SessionFactory sessionFactory;

    @BeforeAll
    public static void setUp() {
        // See @https://neo4j.com/docs/ogm-manual/current/reference/ for more information.

        // To use an impermanent embedded data store which will be deleted on shutdown of the JVM,
        // you just omit the URI attribute.

        // Impermanent embedded store
        Configuration configuration = new Configuration.Builder().build();

        // Disk-based embedded store
        //Configuration configuration = new Configuration.Builder().uri(new File(TEST_DB).toURI().toString()).build();

        // HTTP data store, need to install the Neo4j desktop app and create & run a database first.
//        Configuration configuration = new Configuration.Builder().uri("http://neo4j:password@localhost:7474").build();

        sessionFactory = new SessionFactory(configuration, Musician.class.getPackage().getName());
        session = sessionFactory.openSession();

        dao = new Neo4jDAO(session);
    }

    @AfterEach
    public void tearDownEach() {
        session.purgeDatabase();
    }

    @AfterAll
    public static void tearDown() throws IOException {
        session.purgeDatabase();
        session.clear();
        sessionFactory.close();
        File testDir = new File(TEST_DB);
        if (testDir.exists()) {
            //FileUtils.deleteDirectory(testDir.toPath());
        }
    }

    @Test
    public void daoIsNotEmpty() {
        assertNotNull(dao);
    }


    @Test
    public void successfulCreationAndLoadingOfMusician() throws MalformedURLException {
        assertEquals(0, dao.loadAll(Musician.class).size());

        Musician musician = new Musician("Keith Jarrett");
        musician.setMusicianUrl(new URL("https://www.ecm.com/keithjarrett"));
        musician.setBio("I am a Musician");
        musician.setPersonalSite(new URL("https://keithjarrett.org"));
        musician.setWikiPage(new URL("https://www.wikipedia.org/keithjarrett"));

        dao.createOrUpdate(musician);
        Musician loadedMusician = dao.load(Musician.class, musician.getId());

        assertNotNull(loadedMusician.getId());
        assertEquals(musician, loadedMusician);
        assertEquals(musician.getMusicianUrl(), loadedMusician.getMusicianUrl());
        assertEquals(musician.getBio(), loadedMusician.getBio());
        assertEquals(musician.getPersonalSite(), loadedMusician.getPersonalSite());
        assertEquals(musician.getWikiPage(), loadedMusician.getWikiPage());

        assertEquals(1, dao.loadAll(Musician.class).size());
    }

    @Test
    public void successfulUpdatingOfMusician() throws MalformedURLException {
        assertEquals(0, dao.loadAll(Musician.class).size());

        Musician musician = new Musician("Keith Jarrett");
        musician.setMusicianUrl(new URL("https://www.ecm.com/keithjarrett"));
        musician.setBio("I am a Musician");
        musician.setPersonalSite(new URL("https://keithjarrett.org"));
        musician.setWikiPage(new URL("https://www.wikipedia.org/keithjarrett"));

        dao.createOrUpdate(musician);
        musician.setName("New name");
        dao.createOrUpdate(musician);
        assertEquals(1, dao.loadAll(Musician.class).size());
        Musician loadedMusician = dao.load(Musician.class, musician.getId());

        assertNotNull(loadedMusician.getId());
        assertEquals(musician, loadedMusician);
        assertEquals(musician.getName(), loadedMusician.getName());
    }

    @Test
    public void successfulDeletionOfMusician() throws MalformedURLException {
        assertEquals(0, dao.loadAll(Musician.class).size());

        Musician musician = new Musician("Keith Jarrett");
        musician.setMusicianUrl(new URL("https://www.ecm.com/keithjarrett"));
        musician.setBio("I am a Musician");
        musician.setPersonalSite(new URL("https://keithjarrett.org"));
        musician.setWikiPage(new URL("https://www.wikipedia.org/keithjarrett"));

        dao.createOrUpdate(musician);
        assertEquals(1, dao.loadAll(Musician.class).size());

        dao.delete(musician);
        assertEquals(0, dao.loadAll(Musician.class).size());
    }


    @Test
    public void successfulCreationAndLoadingOfAlbum() throws MalformedURLException {
        assertEquals(0, dao.loadAll(Album.class).size());

        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        album.setAlbumURL(new URL("https://www.ecm.com/keithjarrett"));

        dao.createOrUpdate(album);
        Album loadedAlbum = dao.load(Album.class, album.getId());

        assertNotNull(loadedAlbum.getId());
        assertEquals(album, loadedAlbum);
        assertEquals(album.getAlbumURL(), loadedAlbum.getAlbumURL());

        assertEquals(1, dao.loadAll(Album.class).size());
    }

    @Test
    public void successfulUpdatingOfAlbum() throws MalformedURLException {
        assertEquals(0, dao.loadAll(Album.class).size());

        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        album.setAlbumURL(new URL("https://www.ecm.com/keithjarrett"));

        dao.createOrUpdate(album);
        album.setAlbumName("The New Concert");
        dao.createOrUpdate(album);
        assertEquals(1, dao.loadAll(Album.class).size());
        Album loadedAlbum = dao.load(Album.class, album.getId());

        assertNotNull(loadedAlbum.getId());
        assertEquals(album, loadedAlbum);
        assertEquals(album.getAlbumURL(), loadedAlbum.getAlbumURL());
    }

    @Test
    public void successfulDeletionOfAlbum() throws MalformedURLException {
        assertEquals(0, dao.loadAll(Album.class).size());

        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        album.setAlbumURL(new URL("https://www.ecm.com/keithjarrett"));

        dao.createOrUpdate(album);
        assertEquals(1, dao.loadAll(Album.class).size());

        dao.delete(album);
        assertEquals(0, dao.loadAll(Album.class).size());
    }

    @Test
    public void successfulCreationAndLoadingOfRating()  {
        assertEquals(0, dao.loadAll(Rating.class).size());

        Rating rating = new Rating(4, "Life Magazine");

        dao.createOrUpdate(rating);
        Rating loadedRating = dao.load(Rating.class, rating.getId());

        assertNotNull(loadedRating.getId());
        assertEquals(rating, loadedRating);
        assertEquals(rating.getSource(), loadedRating.getSource());

        assertEquals(1, dao.loadAll(Rating.class).size());
    }

    @Test
    public void successfulUpdatingOfRating() {
        assertEquals(0, dao.loadAll(Rating.class).size());

        Rating rating = new Rating(4, "Life Magazine");

        dao.createOrUpdate(rating);
        rating.setRatingScore(3);
        dao.createOrUpdate(rating);
        assertEquals(1, dao.loadAll(Rating.class).size());
        Rating loadedRating = dao.load(Rating.class, rating.getId());

        assertNotNull(loadedRating.getId());
        assertEquals(rating, loadedRating);
        assertEquals(rating.getSource(), loadedRating.getSource());
    }

    @Test
    public void successfulDeletionOfRating() {
        assertEquals(0, dao.loadAll(Rating.class).size());

        Rating rating = new Rating(4, "Life Magazine");

        dao.createOrUpdate(rating);
        assertEquals(1, dao.loadAll(Rating.class).size());

        dao.delete(rating);
        assertEquals(0, dao.loadAll(Rating.class).size());
    }

    @Test
    public void successfulCreationAndLoadingOfTrack(){
        assertEquals(0, dao.loadAll(Track.class).size());

        Track track = new Track("Track 1","4:20","Jazz",1);
        List<String> list = Arrays.asList("Nice song" , "Brilliant track");
        track.setReviews(list);

        dao.createOrUpdate(track);
        Track loadedTrack = dao.load(Track.class, track.getId());

        assertNotNull(loadedTrack.getId());
        assertEquals(track, loadedTrack);
        assertEquals(track.getReviews(), loadedTrack.getReviews());

        assertEquals(1, dao.loadAll(Track.class).size());
    }

    @Test
    public void successfulUpdatingOfTrack()
    {
        Track track = new Track("Track 1","4:20","Jazz",1);
        List<String> list = Arrays.asList("Nice song" , "Brilliant track");
        track.setReviews(list);
        dao.createOrUpdate(track);
        track.setName("Track 2");
        dao.createOrUpdate(track);
        assertEquals(1, dao.loadAll(Track.class).size());

        Track loadedTrack = dao.load(Track.class, track.getId());
        assertNotNull(loadedTrack);
        assertEquals(track, loadedTrack);
        assertEquals(track.getName(), loadedTrack.getName());
    }

    @Test
    public void successfulDeletionOfTrack(){
        assertEquals(0, dao.loadAll(Track.class).size());

        Track track = new Track("Track 1","4:20","Jazz",1);
        List<String> list = Arrays.asList("Nice song" , "Brilliant track");
        track.setReviews(list);

        dao.createOrUpdate(track);
        Track loadedTrack = dao.load(Track.class, track.getId());

        assertNotNull(loadedTrack.getId());
        assertEquals(track, loadedTrack);
        assertEquals(track.getReviews(), loadedTrack.getReviews());

        assertEquals(1, dao.loadAll(Track.class).size());

        dao.delete(track);
        assertEquals(0, dao.loadAll(Track.class).size());
    }

    @Test
    public void successfulFindByAlbumSales()  {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Track track = new Track("Track 1","4:20","Jazz",1);
        List<String> list = Arrays.asList("Nice song" , "Brilliant track");
        track.setReviews(list);
        album.setTracks(Sets.newHashSet(track));
        album.setSales(10);
        dao.createOrUpdate(album);
        Album foundAlbum = dao.findAlbumBySales(10);

        assertEquals(album.getSales(), foundAlbum.getSales());
    }

    @Test
    public void FindByAlbumSalesIsNull()  {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Track track = new Track("Track 1","4:20","Jazz",1);
        List<String> list = Arrays.asList("Nice song" , "Brilliant track");
        track.setReviews(list);
        album.setTracks(Sets.newHashSet(track));
        album.setSales(10);
        dao.createOrUpdate(album);
        Album foundAlbum = dao.findAlbumBySales(5);

        assertEquals(null, foundAlbum);
    }

    @Test
    public void successfulFindByAlbumName()  {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Track track = new Track("Track 1","4:20","Jazz",1);
        List<String> list = Arrays.asList("Nice song" , "Brilliant track");
        track.setReviews(list);
        album.setTracks(Sets.newHashSet(track));

        dao.createOrUpdate(album);

        Album foundAlbum = dao.findAlbumByName("The Köln Concert");

        assertEquals(album.getAlbumName(), foundAlbum.getAlbumName());
    }
    @Test
    public void FindByAlbumNameIsNull()  {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Track track = new Track("Track 1","4:20","Jazz",1);
        List<String> list = Arrays.asList("Nice song" , "Brilliant track");
        track.setReviews(list);
        album.setTracks(Sets.newHashSet(track));

        dao.createOrUpdate(album);

        Album foundAlbum = dao.findAlbumByName("Concert");

        assertEquals(null, foundAlbum);
    }

    @Test
    public void successfulFindByAlbumReleaseYear()  {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Track track = new Track("Track 1","4:20","Jazz",1);
        List<String> list = Arrays.asList("Nice song" , "Brilliant track");
        track.setReviews(list);
        album.setTracks(Sets.newHashSet(track));

        dao.createOrUpdate(album);

        Album foundAlbum = dao.findAlbumByReleaseYear(1975);

        assertEquals(album.getReleaseYear(), foundAlbum.getReleaseYear());
    }
    @Test
    public void FindByAlbumReleaseYearIsNull()  {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Track track = new Track("Track 1","4:20","Jazz",1);
        List<String> list = Arrays.asList("Nice song" , "Brilliant track");
        track.setReviews(list);
        album.setTracks(Sets.newHashSet(track));

        dao.createOrUpdate(album);

        Album foundAlbum = dao.findAlbumByReleaseYear(1979);

        assertEquals(null, foundAlbum);
    }

    @Test
    public void successfulFindByAlbumRecordNumber()  {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Track track = new Track("Track 1","4:20","Jazz",1);
        List<String> list = Arrays.asList("Nice song" , "Brilliant track");
        track.setReviews(list);
        album.setTracks(Sets.newHashSet(track));

        dao.createOrUpdate(album);

        Album foundAlbum = dao.findAlbumByRecordNumber("ECM 1064/65");

        assertEquals(album.getRecordNumber(), foundAlbum.getRecordNumber());
    }
    @Test
    public void FindByAlbumRecordNumberIsNull()  {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Track track = new Track("Track 1","4:20","Jazz",1);
        List<String> list = Arrays.asList("Nice song" , "Brilliant track");
        track.setReviews(list);
        album.setTracks(Sets.newHashSet(track));

        dao.createOrUpdate(album);

        Album foundAlbum = dao.findAlbumByRecordNumber("ECM 1064/60");

        assertEquals(null, foundAlbum);
    }

    @Test
    public void successfulCreationAndLoadingOfMusicalInstrument() throws MalformedURLException {
        assertEquals(0, dao.loadAll(MusicalInstrument.class).size());

        MusicalInstrument musicalInstrument = new MusicalInstrument("Piano");

        dao.createOrUpdate(musicalInstrument);
        MusicalInstrument loadedMusicalInstrument = dao.load(MusicalInstrument.class, musicalInstrument.getId());

        assertNotNull(loadedMusicalInstrument.getId());
        assertEquals(musicalInstrument, loadedMusicalInstrument);
        assertEquals(musicalInstrument.getName(), loadedMusicalInstrument.getName());

        assertEquals(1, dao.loadAll(MusicalInstrument.class).size());

    }




    @Test
    public void successfulUpdatingOfMusicalInstrument() {
        assertEquals(0, dao.loadAll(MusicalInstrument.class).size());

        MusicalInstrument musicalInstrument = new MusicalInstrument("Piano");
        dao.createOrUpdate(musicalInstrument);
        musicalInstrument.setName("Guitar");
        dao.createOrUpdate(musicalInstrument);
        assertEquals(1, dao.loadAll(MusicalInstrument.class).size());

        MusicalInstrument loadedTrack = dao.load(MusicalInstrument.class, musicalInstrument.getId());
        assertNotNull(loadedTrack);
        assertEquals(musicalInstrument, loadedTrack);
        assertEquals(musicalInstrument.getName(), loadedTrack.getName());
    }

    @Test
    public void successfulDeletionOfMusicalInstrument() throws MalformedURLException {
        assertEquals(0, dao.loadAll(MusicalInstrument.class).size());

        MusicalInstrument musicalInstrument = new MusicalInstrument("Piano");

        dao.createOrUpdate(musicalInstrument);
        MusicalInstrument loadedMusicalInstrument = dao.load(MusicalInstrument.class, musicalInstrument.getId());

        assertNotNull(loadedMusicalInstrument.getId());
        assertEquals(musicalInstrument, loadedMusicalInstrument);
        assertEquals(musicalInstrument.getName(), loadedMusicalInstrument.getName());

        assertEquals(1, dao.loadAll(MusicalInstrument.class).size());

        dao.delete(musicalInstrument);
        assertEquals(0, dao.loadAll(MusicalInstrument.class).size());
    }

    @Test
    public void successfulCreationAndLoadingOfMusicianInstrument() throws MalformedURLException {
        assertEquals(0, dao.loadAll(MusicianInstrument.class).size());
        ArrayList<MusicalInstrument> myArray = new ArrayList<MusicalInstrument>();
        myArray.add(new MusicalInstrument("Piano"));
        myArray.add(new MusicalInstrument("Guitar"));
        Set<MusicalInstrument> musicalInstrumentList = new HashSet<>(myArray);
        MusicianInstrument musicianInstrument = new MusicianInstrument(new Musician("Keith Jarrett"), musicalInstrumentList);

        dao.createOrUpdate(musicianInstrument);
        MusicianInstrument loadedMusicianInstrument = dao.load(MusicianInstrument.class, musicianInstrument.getId());

        assertNotNull(loadedMusicianInstrument.getId());
        assertEquals(musicianInstrument, loadedMusicianInstrument);
        assertEquals(musicianInstrument.getMusician(), loadedMusicianInstrument.getMusician());
        assertEquals(musicianInstrument.getMusicalInstruments(), loadedMusicianInstrument.getMusicalInstruments());

        assertEquals(1, dao.loadAll(MusicianInstrument.class).size());
    }


    @Test
    public void successfulUpdatingOfMusicianInstrument() throws MalformedURLException {
        assertEquals(0, dao.loadAll(MusicianInstrument.class).size());
        ArrayList<MusicalInstrument> myArray = new ArrayList<MusicalInstrument>();
        myArray.add(new MusicalInstrument("Piano"));
        myArray.add(new MusicalInstrument("Guitar"));
        Set<MusicalInstrument> musicalInstrumentList = new HashSet<>(myArray);
        MusicianInstrument musicianInstrument = new MusicianInstrument(new Musician("Keith Jarrett"), musicalInstrumentList);

        dao.createOrUpdate(musicianInstrument);
        musicianInstrument.setMusician(new Musician("Keith J"));
        dao.createOrUpdate(musicianInstrument);
        assertEquals(1, dao.loadAll(MusicianInstrument.class).size());
        MusicianInstrument loadedMusicianInstrument = dao.load(MusicianInstrument.class, musicianInstrument.getId());

        assertNotNull(loadedMusicianInstrument.getId());
        assertEquals(musicianInstrument, loadedMusicianInstrument);
        assertEquals(musicianInstrument.getMusician(), loadedMusicianInstrument.getMusician());
        assertEquals(musicianInstrument.getMusicalInstruments(), loadedMusicianInstrument.getMusicalInstruments());
    }

    @Test
    public void successfulDeletionOfMusicianInstrument() throws MalformedURLException {
        assertEquals(0, dao.loadAll(MusicianInstrument.class).size());
        ArrayList<MusicalInstrument> myArray = new ArrayList<MusicalInstrument>();
        myArray.add(new MusicalInstrument("Piano"));
        myArray.add(new MusicalInstrument("Guitar"));
        Set<MusicalInstrument> musicalInstrumentList = new HashSet<>(myArray);
        MusicianInstrument musicianInstrument = new MusicianInstrument(new Musician("Keith Jarrett"), musicalInstrumentList);

        dao.createOrUpdate(musicianInstrument);
        MusicianInstrument loadedMusicianInstrument = dao.load(MusicianInstrument.class, musicianInstrument.getId());

        assertNotNull(loadedMusicianInstrument.getId());
        assertEquals(musicianInstrument, loadedMusicianInstrument);
        assertEquals(musicianInstrument.getMusician(), loadedMusicianInstrument.getMusician());
        assertEquals(musicianInstrument.getMusicalInstruments(), loadedMusicianInstrument.getMusicalInstruments());

        assertEquals(1, dao.loadAll(MusicianInstrument.class).size());

        dao.delete(musicianInstrument);
        assertEquals(0, dao.loadAll(MusicianInstrument.class).size());
    }

    @Test
    public void successfulFindMusicianByName()  throws MalformedURLException
    {
        assertEquals(0, dao.loadAll(Musician.class).size());

        Musician musician = new Musician("Keith Jarrett");
        dao.createOrUpdate(musician);
        Musician foundMusician=dao.findMusicianByName(musician.getName());
        assertNotNull(foundMusician.getId());
        assertEquals(musician, foundMusician);
        assertEquals(musician.getName(), foundMusician.getName());
    }
    @Test
    public void FindMusicianByNameIsNull()  throws MalformedURLException
    {
        assertEquals(0, dao.loadAll(Musician.class).size());

        Musician musician = new Musician("Keith Jarrett");
        dao.createOrUpdate(musician);
        Musician foundMusician=dao.findMusicianByName("Jarrett");

        assertEquals(null, foundMusician);
    }

    @Test
    public void successfulFindMusicalInstrumentByName() throws MalformedURLException {
        assertEquals(0, dao.loadAll(MusicalInstrument.class).size());

        MusicalInstrument musicalInstrument = new MusicalInstrument("Piano");
        dao.createOrUpdate(musicalInstrument);
        MusicalInstrument foundMusicalInstrument=dao.findMusicalInstrumentByName(musicalInstrument.getName());
        assertNotNull(foundMusicalInstrument);
        assertEquals(musicalInstrument, foundMusicalInstrument);
        assertEquals(musicalInstrument.getName(), foundMusicalInstrument.getName());
    }
    @Test
    public void FindMusicalInstrumentByNameIsNull() throws MalformedURLException {
        assertEquals(0, dao.loadAll(MusicalInstrument.class).size());

        MusicalInstrument musicalInstrument = new MusicalInstrument("Piano");
        dao.createOrUpdate(musicalInstrument);
        MusicalInstrument foundMusicalInstrument=dao.findMusicalInstrumentByName("Guitar");

        assertEquals(null, foundMusicalInstrument);
    }

    @Test
    public void successfulFindTrackByName(){
        assertEquals(0, dao.loadAll(Track.class).size());

        Track track = new Track("Track 1","4:20","Jazz",1);
        List<String> list = Arrays.asList("Nice song" , "Brilliant track");
        track.setReviews(list);
        dao.createOrUpdate(track);
        Track foundTrack = dao.findTrackByName(track.getName());
        assertNotNull(foundTrack);
        assertEquals(track, foundTrack);
        assertEquals(track.getName(), foundTrack.getName());
    }

    @Test
    public void FindTrackByNameIsNull(){
        assertEquals(0, dao.loadAll(Track.class).size());

        Track track = new Track("Track 1","4:20","Jazz",1);
        List<String> list = Arrays.asList("Nice song" , "Brilliant track");
        track.setReviews(list);
        dao.createOrUpdate(track);
        Track foundTrack = dao.findTrackByName("Song 1");
        assertEquals(null, foundTrack);
    }

    @Test
    public void successfulFindTrackByDuration(){
        assertEquals(0, dao.loadAll(Track.class).size());

        Track track = new Track("Track 1","4:20","Jazz",1);
        List<String> list = Arrays.asList("Nice song" , "Brilliant track");
        track.setReviews(list);
        dao.createOrUpdate(track);
        Track foundTrack = dao.findTrackByDuration(track.getDuration());
        assertNotNull(foundTrack);
        assertEquals(track, foundTrack);
        assertEquals(track.getDuration(), foundTrack.getDuration());
    }
    @Test
    public void findTrackByDurationIsNull(){
        assertEquals(0, dao.loadAll(Track.class).size());

        Track track = new Track("Track 1","4:20","Jazz",1);
        List<String> list = Arrays.asList("Nice song" , "Brilliant track");
        track.setReviews(list);
        dao.createOrUpdate(track);
        Track foundTrack = dao.findTrackByDuration("3:10");
        assertEquals(null, foundTrack);
    }

    @Test
    public void successfulFindTrackByGenre(){
        assertEquals(0, dao.loadAll(Track.class).size());

        Track track = new Track("Track 1","4:20","Jazz",1);
        List<String> list = Arrays.asList("Nice song" , "Brilliant track");
        track.setReviews(list);
        dao.createOrUpdate(track);
        Track foundTrack = dao.findTrackByGenre(track.getGenre());
        assertNotNull(foundTrack);
        assertEquals(track, foundTrack);
        assertEquals(track.getGenre(), foundTrack.getGenre());
    }

    @Test
    public void FindTrackByGenreIsNull(){
        assertEquals(0, dao.loadAll(Track.class).size());

        Track track = new Track("Track 1","4:20","Jazz",1);
        List<String> list = Arrays.asList("Nice song" , "Brilliant track");
        track.setReviews(list);
        dao.createOrUpdate(track);
        Track foundTrack = dao.findTrackByGenre("HipPop");
        assertEquals(null, foundTrack);
    }

    @Test
    public void successfulFindTrackByTrackNumber(){
        assertEquals(0, dao.loadAll(Track.class).size());

        Track track = new Track("Track 1","4:20","Jazz",1);
        List<String> list = Arrays.asList("Nice song" , "Brilliant track");
        track.setReviews(list);
        dao.createOrUpdate(track);
        Track foundTrack = dao.findTrackByTrackNumber(track.getTrackNumber());
        assertNotNull(foundTrack);
        assertEquals(track, foundTrack);
        assertEquals(track.getTrackNumber(), foundTrack.getTrackNumber());
    }
    @Test
    public void findTrackByTrackNumberIsNull(){
        assertEquals(0, dao.loadAll(Track.class).size());

        Track track = new Track("Track 1","4:20","Jazz",1);
        List<String> list = Arrays.asList("Nice song" , "Brilliant track");
        track.setReviews(list);
        dao.createOrUpdate(track);
        Track foundTrack = dao.findTrackByTrackNumber(2);
        assertEquals(null, foundTrack);
    }

    @Test
    public void successfulFindRatingByScore() {
        assertEquals(0, dao.loadAll(Rating.class).size());

        Rating rating = new Rating(4, "Life Magazine");

        dao.createOrUpdate(rating);
        Rating loadedRating = dao.findRatingByScore(rating.getRatingScore());

        assertNotNull(loadedRating);
        assertEquals(rating, loadedRating);
        assertEquals(rating.getRatingScore(), loadedRating.getRatingScore());
    }
    @Test
    public void findRatingByScoreIsNull() {
        assertEquals(0, dao.loadAll(Rating.class).size());

        Rating rating = new Rating(4, "Life Magazine");

        dao.createOrUpdate(rating);
        Rating loadedRating = dao.findRatingByScore(3);
        assertEquals(null, loadedRating);
    }

    @Test
    public void successfulFindRatingBySource() {
        assertEquals(0, dao.loadAll(Rating.class).size());

        Rating rating = new Rating(4, "Life Magazine");

        dao.createOrUpdate(rating);
        Rating loadedRating = dao.findRatingBySource(rating.getSource());

        assertNotNull(loadedRating);
        assertEquals(rating, loadedRating);
        assertEquals(rating.getSource(), loadedRating.getSource());
    }
    @Test
    public void findRatingBySourceIsNull() {
        assertEquals(0, dao.loadAll(Rating.class).size());

        Rating rating = new Rating(4, "Life Magazine");

        dao.createOrUpdate(rating);
        Rating loadedRating = dao.findRatingBySource("Bazzar Magazine");

        assertEquals(null, loadedRating);
    }

    @Test
    public void successfulCreationOfMusicianAndAlbum() throws MalformedURLException {
        Musician musician = new Musician("Keith Jarrett");
        musician.setMusicianUrl(new URL("https://www.ecm.com/keithjarrett"));

        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        musician.setAlbums(Sets.newHashSet(album));

        dao.createOrUpdate(musician);

        Collection<Musician> musicians = dao.loadAll(Musician.class);
        assertEquals(1, musicians.size());
        Musician loadedMusician = musicians.iterator().next();
        assertEquals(musician, loadedMusician);
        assertEquals(musician.getAlbums(), loadedMusician.getAlbums());
    }

    @Test
    public void successfulCreationOfAlbumAndMusician() throws MalformedURLException {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Musician musician = new Musician("Keith Jarrett");
        musician.setMusicianUrl(new URL("https://www.ecm.com/keithjarrett"));
        album.setFeaturedMusicians(Arrays.asList(musician));

        dao.createOrUpdate(album);

        Collection<Album> albums = dao.loadAll(Album.class);
        assertEquals(1, albums.size());
        Album loadedAlbum = albums.iterator().next();
        assertEquals(album, loadedAlbum);
        assertEquals(album.getFeaturedMusicians(), loadedAlbum.getFeaturedMusicians());
    }

    @Test
    public void successfulCreationOfAlbumAndRating() throws MalformedURLException {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        List<Rating> list = Arrays.asList(new Rating(2,"Rolling Stone Magazine"));
        Set<Rating> ratingsList = new HashSet<>(list);
        album.setRatings(ratingsList);

        dao.createOrUpdate(album);

        Collection<Album> albums = dao.loadAll(Album.class);
        assertEquals(1, albums.size());
        Album loadedAlbum = albums.iterator().next();
        assertEquals(album, loadedAlbum);
        assertEquals(album.getRatings(), loadedAlbum.getRatings());
    }

    @Test
    public void successfulCreationOfAlbumAndMusicianInstrument(){
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        ArrayList<MusicalInstrument> myArray = new ArrayList<MusicalInstrument>();
        myArray.add(new MusicalInstrument("Piano"));
        myArray.add(new MusicalInstrument("Guitar"));
        Set<MusicalInstrument> musicalInstrumentList = new HashSet<>(myArray);
        MusicianInstrument musicianInstrument = new MusicianInstrument(new Musician("Keith Jarrett"), musicalInstrumentList);
        album.setInstruments(Sets.newHashSet(musicianInstrument));

        dao.createOrUpdate(album);

        Collection<Album> albums = dao.loadAll(Album.class);
        assertEquals(1, albums.size());
        Album loadedAlbum = albums.iterator().next();
        assertEquals(album, loadedAlbum);
        assertEquals(album.getInstruments(), loadedAlbum.getInstruments());
    }

    @Test
    public void successfulCreationOfAlbumAndTrack()  {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Track track = new Track("Track 1","4:20","Jazz",1);
        List<String> list = Arrays.asList("Nice song" , "Brilliant track");
        track.setReviews(list);
        album.setTracks(Sets.newHashSet(track));

        dao.createOrUpdate(album);

        Collection<Album> albums = dao.loadAll(Album.class);
        assertEquals(1, albums.size());
        Album loadedAlbum = albums.iterator().next();
        assertEquals(album, loadedAlbum);
        assertEquals(album.getTracks(), loadedAlbum.getTracks());
    }

    @Test
    public void successfulCreationOfMusicianInstrumentAndMusicalInstrumentsAndMusician() throws MalformedURLException {
        Musician musician = new Musician("Keith Jarrett");
        ArrayList<MusicalInstrument> myArray = new ArrayList<MusicalInstrument>();
        MusicalInstrument musicalInstrument=new MusicalInstrument("Piano");
        myArray.add(musicalInstrument);
        Set<MusicalInstrument> musicalInstrumentList = new HashSet<>(myArray);
        MusicianInstrument musicianInstrument = new MusicianInstrument(musician, musicalInstrumentList);

        dao.createOrUpdate(musicianInstrument);

        Collection<MusicianInstrument> musicianInstruments = dao.loadAll(MusicianInstrument.class);
        assertEquals(1, musicianInstruments.size());
        MusicianInstrument loadedMusicianInstrument = musicianInstruments.iterator().next();
        assertEquals(musicianInstrument, loadedMusicianInstrument);
        assertEquals(musicianInstrument.getMusicalInstruments(), loadedMusicianInstrument.getMusicalInstruments());
        assertEquals(musicianInstrument.getMusician(), loadedMusicianInstrument.getMusician());
    }
}