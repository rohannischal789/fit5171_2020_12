package allaboutecm.dataaccess.neo4j;

import allaboutecm.dataaccess.DAO;
import allaboutecm.model.Album;
import allaboutecm.model.MusicalInstrument;
import allaboutecm.model.Musician;
import allaboutecm.model.MusicianInstrument;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * TODO: add test cases to adequately test the Neo4jDAO class.
 */
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
        // Configuration configuration = new Configuration.Builder().uri(new File(TEST_DB).toURI().toString()).build();

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
//            FileUtils.deleteDirectory(testDir.toPath());
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

        dao.createOrUpdate(musician);
        Musician loadedMusician = dao.load(Musician.class, musician.getId());

        assertNotNull(loadedMusician.getId());
        assertEquals(musician, loadedMusician);
        assertEquals(musician.getMusicianUrl(), loadedMusician.getMusicianUrl());

        assertEquals(1, dao.loadAll(Musician.class).size());

//        dao.delete(musician);
//        assertEquals(0, dao.loadAll(Musician.class).size());
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

//        dao.delete(musicalInstrument);
//        assertEquals(0, dao.loadAll(MusicalInstrument.class).size());
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

//        dao.delete(musicianInstrument);
//        assertEquals(0, dao.loadAll(MusicianInstrument.class).size());
    }

    @Test
    public void successfulCreationOfMusicianInstrumentAndMusicalInstrumentsAndMusician() throws MalformedURLException {
        Musician musician = new Musician("Keith Jarrett");
        ArrayList<MusicalInstrument> myArray = new ArrayList<MusicalInstrument>();
        MusicalInstrument musicalInstrument=new MusicalInstrument("Piano");
        myArray.add(musicalInstrument);
        Set<MusicalInstrument> musicalInstrumentList = new HashSet<>(myArray);
        MusicianInstrument musicianInstrument = new MusicianInstrument(musician, musicalInstrumentList);

        dao.createOrUpdate(musician);
        dao.createOrUpdate(musicalInstrument);
        dao.createOrUpdate(musicianInstrument);

        Collection<MusicianInstrument> musicianInstruments = dao.loadAll(MusicianInstrument.class);
        assertEquals(1, musicianInstruments.size());
        MusicianInstrument loadedMusicianInstrument = musicianInstruments.iterator().next();
        assertEquals(musicianInstrument, loadedMusicianInstrument);
        assertEquals(musicianInstrument.getMusicalInstruments(), loadedMusicianInstrument.getMusicalInstruments());
        assertEquals(musicianInstrument.getMusician(), loadedMusicianInstrument.getMusician());
    }


    @Test
    public void successfulCreationOfMusicianAndAlbum() throws MalformedURLException {
        Musician musician = new Musician("Keith Jarrett");
        musician.setMusicianUrl(new URL("https://www.ecm.com/keithjarrett"));

        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        musician.setAlbums(Sets.newHashSet(album));

        dao.createOrUpdate(album);
        dao.createOrUpdate(musician);

        Collection<Musician> musicians = dao.loadAll(Musician.class);
        assertEquals(1, musicians.size());
        Musician loadedMusician = musicians.iterator().next();
        assertEquals(musician, loadedMusician);
        assertEquals(musician.getMusicianUrl(), loadedMusician.getMusicianUrl());
        assertEquals(musician.getAlbums(), loadedMusician.getAlbums());
    }
}