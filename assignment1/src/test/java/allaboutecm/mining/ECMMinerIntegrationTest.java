package allaboutecm.mining;

import allaboutecm.dataaccess.DAO;
import allaboutecm.dataaccess.neo4j.Neo4jDAO;
import allaboutecm.model.Album;
import allaboutecm.model.MusicalInstrument;
import allaboutecm.model.Musician;
import allaboutecm.model.MusicianInstrument;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ECMMinerIntegrationTest {
    private static final String TEST_DB = "target/test-data/test-db.neo4j";

    private static DAO dao;
    private static Session session;
    private static SessionFactory sessionFactory;
    private ECMMiner ecmMiner;

    @BeforeEach
    public void setUp() {

        Configuration configuration = new Configuration.Builder().build();

        // Disk-based embedded store
        //Configuration configuration = new Configuration.Builder().uri(new File(TEST_DB).toURI().toString()).build();

        // HTTP data store, need to install the Neo4j desktop app and create & run a database first.
        //Configuration configuration = new Configuration.Builder().uri("http://neo4j:password@localhost:7474").build();

        sessionFactory = new SessionFactory(configuration, Musician.class.getPackage().getName());
        session = sessionFactory.openSession();

        dao = new Neo4jDAO(session);
        ecmMiner = new ECMMiner(dao);
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
    public void shouldReturnTheMusicianWhenThereIsOnlyOneProlific() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Musician musician = new Musician("Keith Jarrett");
        musician.setAlbums(Sets.newHashSet(album));
        dao.createOrUpdate(musician);

        List<Musician> musicians = ecmMiner.mostProlificMusicians(5, -1, -1);

        assertEquals(1, musicians.size());
        assertTrue(musicians.contains(musician));
    }

    @Test
    public void findTopMusicianByInstrumentCount(){
        //Create a musician and list of 1 instrument they play
        Musician musician1 = new Musician("Keith Jarrett");
        List <MusicalInstrument> MIList = new ArrayList<MusicalInstrument>();
        MIList.add(new MusicalInstrument("An Instrument"));
        //Convert the instrument to hashset and create a musicianinstrument from our musician and our hashset
        Set musicianSet = new HashSet<MusicalInstrument>(MIList);
        MusicianInstrument onlyEntry = new MusicianInstrument(musician1, musicianSet);
        //When we try to load from the dao, give them our Musician Instrument instead
        dao.createOrUpdate(onlyEntry);

        List<Musician> musicians = ecmMiner.mostTalentedMusicians(1);

        assertEquals(1, musicians.size());
        assertTrue(musicians.contains(musician1));
    }

    @Test
    public void findTopMusicianByInstrumentCountFromMany(){
        //Create the first Musician/Instrument entry
        Musician musician1 = new Musician("Musician 1");
        List <MusicalInstrument> musicalInstrumentList1 = new ArrayList<MusicalInstrument>();
        musicalInstrumentList1.add(new MusicalInstrument("An Instrument"));
        Set musician1Set = new HashSet<MusicalInstrument>(musicalInstrumentList1);
        MusicianInstrument firstMusician = new MusicianInstrument(musician1, musician1Set);
        //Second Musician to add to our search space
        Musician musician2 = new Musician("Musician 2");
        List <MusicalInstrument> musicalInstrumentList2 = new ArrayList<MusicalInstrument>();
        musicalInstrumentList2.add(new MusicalInstrument("An Instrument"));
        musicalInstrumentList2.add(new MusicalInstrument("An Instrument 2"));
        Set musician2Set = new HashSet<MusicalInstrument>(musicalInstrumentList2);
        MusicianInstrument secondMusician = new MusicianInstrument(musician2, musician2Set);
        //Third Musician to add to our search space
        Musician musician3 = new Musician("Musician 3");
        List <MusicalInstrument> musicalInstrumentList3 = new ArrayList<MusicalInstrument>();
        musicalInstrumentList3.add(new MusicalInstrument("An Instrument"));
        musicalInstrumentList3.add(new MusicalInstrument("An Instrument 2"));
        musicalInstrumentList3.add(new MusicalInstrument("An Instrument 3"));
        Set musician3Set = new HashSet<MusicalInstrument>(musicalInstrumentList3);
        MusicianInstrument thirdMusician = new MusicianInstrument(musician3, musician3Set);
        //When we try to load from database, pass them a hash set with our test musicians instead
        dao.createOrUpdate(firstMusician);
        dao.createOrUpdate(secondMusician);
        dao.createOrUpdate(thirdMusician);
        //We added the most instruments to our third musician, so we expect them, and only them, to be returned.
        List<Musician> musicians = ecmMiner.mostTalentedMusicians(1);
        assertEquals(1, musicians.size());
        assertTrue(musicians.contains(musician3));
    }

    @ParameterizedTest
    @ValueSource(ints = {2,5,10})
    public void findTopInstrumentCountMusicianInMultiExact(int number) {
        //Make our mock database set
        HashSet<MusicianInstrument> musicianInstruments = new HashSet<MusicianInstrument>();
        //Add twice as many entries as our provided int
        for (int i = 0; i < number; i++){
            Musician musician = new Musician("Keith Jarrett" + (i+1));
            List <MusicalInstrument> musicalInstrumentList = new ArrayList<MusicalInstrument>();
            //For each musican we create, create i instruments for them to be associated with.
            for (int j = 0; j < i+1; j++){
                musicalInstrumentList.add(new MusicalInstrument("An Instrument" + j));
            }
            //Convert our instrument list to a set
            Set instrumentSet = new HashSet<MusicalInstrument>(musicalInstrumentList);
            //Create the musician instrument object
            MusicianInstrument theMusicanInstrument = new MusicianInstrument(musician, instrumentSet);
            //Add it to our big hashset
            musicianInstruments.add(theMusicanInstrument);
        }
        for (MusicianInstrument instr:musicianInstruments) {
            dao.createOrUpdate(instr);
        }

        //Call ecmMiner and store the result
        List<Musician> musicians = ecmMiner.mostTalentedMusicians(number);
        assertEquals(number, musicians.size());
        for (int i = number; i > number; i--){
            assertEquals("Keith Jarrett" + i, musicians.get(number - i).getName());
        }

    }

    @ParameterizedTest
    @ValueSource(ints = {2,5,10})
    public void findTopInstrumentCountMusicianInMulti(int number) {
        //Make our mock database set
        HashSet<MusicianInstrument> musicianInstruments = new HashSet<MusicianInstrument>();
        //Add twice as many entries as our provided int
        for (int i = 0; i < (number * 2); i++){
            Musician musician = new Musician("Keith Jarrett" + (i+1));
            List <MusicalInstrument> musicalInstrumentList = new ArrayList<MusicalInstrument>();
            //For each musican we create, create i instruments for them to be associated with.
            for (int j = 0; j < i+1; j++){
                musicalInstrumentList.add(new MusicalInstrument("An Instrument" + j));
            }
            //Convert our instrument list to a set
            Set instrumentSet = new HashSet<MusicalInstrument>(musicalInstrumentList);
            //Create the musician instrument object
            MusicianInstrument theMusicanInstrument = new MusicianInstrument(musician, instrumentSet);
            //Add it to our big hashset
            musicianInstruments.add(theMusicanInstrument);
        }
        for (MusicianInstrument instr:musicianInstruments) {
            dao.createOrUpdate(instr);
        }
        //Call ecmMiner and store the result
        List<Musician> musicians = ecmMiner.mostTalentedMusicians(number);

        assertEquals(number, musicians.size());
        for (int i = (number*2); i > number; i--){
            assertEquals("Keith Jarrett" + i, musicians.get((number*2) - i).getName());
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {-1,0})
    public void impossibleKValueTalented(int number){
        assertThrows(IllegalArgumentException.class, () -> ecmMiner.mostTalentedMusicians(number));
    }

    @Test
    public void kTooLargeTalented(){
        HashSet<MusicianInstrument> musicianInstruments = new HashSet<MusicianInstrument>();
        for(int i = 0; i < 5; i++){
            Musician musician1 = new Musician("Keith Jarrett" + i);
            List <MusicalInstrument> MIList = new ArrayList<MusicalInstrument>();
            MIList.add(new MusicalInstrument("An Instrument" + i));
            //Convert the instrument to hashset and create a musicianinstrument from our musician and our hashset
            Set musicianSet = new HashSet<MusicalInstrument>(MIList);
            MusicianInstrument anEntry = new MusicianInstrument(musician1, musicianSet);
            musicianInstruments.add(anEntry);
        }

        for (MusicianInstrument instr:musicianInstruments) {
            dao.createOrUpdate(instr);
        }
        assertThrows(IllegalArgumentException.class, () -> ecmMiner.mostTalentedMusicians(6));
    }

    @Test
    public void findBusiestYearByReleaseYearCount(){
        ArrayList<Album> myArray = new ArrayList<Album>();
        myArray.add(new Album(2011,"ECM123","Album1"));
        myArray.add(new Album(2011,"ECM773","Album10"));
        myArray.add(new Album(2001,"ECM800","Album11"));
        for (Album alb:myArray) {
            dao.createOrUpdate(alb);
        }

        List<Integer> years = ecmMiner.busiestYears(1);

        assertEquals(1, years.size());
    }

    @Test
    public void findBusiestYearByReleaseYear(){
        ArrayList<Album> myArray = new ArrayList<Album>();
        myArray.add(new Album(2011,"ECM123","Album1"));
        myArray.add(new Album(2011,"ECM773","Album10"));
        myArray.add(new Album(2001,"ECM800","Album11"));
        for (Album alb:myArray) {
            dao.createOrUpdate(alb);
        }

        List<Integer> years = ecmMiner.busiestYears(1);

        assertEquals(2011, years.get(0));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1,0})
    public void impossibleKValueBusiestYear(int number){
        assertThrows(IllegalArgumentException.class, () -> ecmMiner.busiestYears(number));
    }

    @Test
    public void findMostSocialListSizeOne(){
        //Create a musician and an album, check that it is found by the "most social Musician" method
        Musician theMusician = new Musician("Social Musician");
        Album theAlbum = new Album(1994, "ECM-1000", "The Album");
        HashSet<Album> albumSet = new HashSet<>();
        albumSet.add(theAlbum);
        theMusician.setAlbums(albumSet);

        HashSet<Musician> fakeMusicianSet = new HashSet<>();
        fakeMusicianSet.add(theMusician);
        for (Musician mus:fakeMusicianSet) {
            dao.createOrUpdate(mus);
        }

        List socialMusicians = ecmMiner.mostSocialMusicians(1);

        assertEquals(1, socialMusicians.size());
        assertTrue(socialMusicians.get(0).equals(theMusician));
    }

    @Test
    public void findMostSocialListSizeN(){
        //Create 3 musicians. We are going make the third one the expected "Social" musician
        Musician theMusician1 = new Musician("Antisocial Musician1");
        Musician theMusician2 = new Musician("Antisocial Musician2");
        Musician theMusician3 = new Musician("Social Musician");
        //Create 2 albums. Our third musician will have both albums added to their album set.
        Album theAlbum1 = new Album(1994, "ECM-1000", "The Album1");
        Album theAlbum2 = new Album(1994, "ECM-1001", "The Album2");
        //Create the album set of musician1
        HashSet<Album> albumSet1 = new HashSet<>();
        albumSet1.add(theAlbum1);
        theMusician1.setAlbums(albumSet1);
        //Create the album set of musician 2
        HashSet<Album> albumSet2 = new HashSet<>();
        albumSet2.add(theAlbum2);
        theMusician2.setAlbums(albumSet2);
        //Create a set with both albums.
        HashSet<Album> albumSet3 = new HashSet<>();
        albumSet3.add(theAlbum1);
        albumSet3.add(theAlbum2);
        theMusician3.setAlbums(albumSet3);
        //Compile them into a set of musicians to pass to mockito.
        HashSet<Musician> fakeMusicianSet = new HashSet<>();
        fakeMusicianSet.add(theMusician1);
        fakeMusicianSet.add(theMusician2);
        fakeMusicianSet.add(theMusician3);
        for (Musician mus:fakeMusicianSet) {
            dao.createOrUpdate(mus);
        }

        List socialMusicians = ecmMiner.mostSocialMusicians(1);

        assertEquals(1, socialMusicians.size());
        assertTrue(socialMusicians.get(0).equals(theMusician3));
    }

    @Test
    public void findMostSocialListSizeNSpecialCase(){
        //This method checks that only the artist with the highest count of unique collaborators is returned.

        //Create 3 musicians. We are going make the third one the expected "Social" musician
        Musician theMusician1 = new Musician("Antisocial Musician1");
        Musician theMusician2 = new Musician("Antisocial Musician2");
        Musician theMusician3 = new Musician("Social Musician");
        Musician theMusician4 = new Musician("Antisocial Musician3");
        //Create 2 albums. Our third musician will have both albums added to their album set.
        Album theAlbum1 = new Album(1994, "ECM-1000", "The Album1");
        Album theAlbum2 = new Album(1994, "ECM-1001", "The Album2");
        Album theAlbum3 = new Album(1994, "ECM-1002", "The Album3");
        Album theAlbum4 = new Album(1994, "ECM-1003", "The Album4");
        Album theAlbum5 = new Album(1994, "ECM-1003", "The Album5");
        Album theAlbum6 = new Album(1994, "ECM-1003", "The Album6");
        //Create the album set of musician1
        HashSet<Album> albumSet1 = new HashSet<>();
        albumSet1.add(theAlbum1);
        albumSet1.add(theAlbum4);
        albumSet1.add(theAlbum5);
        albumSet1.add(theAlbum6);
        theMusician1.setAlbums(albumSet1);
        //Create the album set of musician 2
        HashSet<Album> albumSet2 = new HashSet<>();
        albumSet2.add(theAlbum2);
        theMusician2.setAlbums(albumSet2);
        //Create a set with albums 1,2 and 3.
        HashSet<Album> albumSet3 = new HashSet<>();
        albumSet3.add(theAlbum1);
        albumSet3.add(theAlbum2);
        albumSet3.add(theAlbum3);
        theMusician3.setAlbums(albumSet3);
        //Album Set 4 will be a set containing 4 albums total. This will give the highest non-unique collaborator count, at 4.
        //However, this artist will only have 2 collaborators, musician 1 and 3, where musician3 will have 3 collaborators.
        HashSet<Album> albumSet4 = new HashSet<>();
        albumSet4.add(theAlbum3);
        albumSet4.add(theAlbum4);
        albumSet4.add(theAlbum5);
        albumSet4.add(theAlbum6);
        theMusician4.setAlbums(albumSet4);
        //Compile them into a set of musicians to pass to mockito.
        HashSet<Musician> fakeMusicianSet = new HashSet<>();
        fakeMusicianSet.add(theMusician1);
        fakeMusicianSet.add(theMusician2);
        fakeMusicianSet.add(theMusician3);
        fakeMusicianSet.add(theMusician4);
        for (Musician mus:fakeMusicianSet) {
            dao.createOrUpdate(mus);
        }
        List socialMusicians = ecmMiner.mostSocialMusicians(1);

        assertEquals(1, socialMusicians.size());
        assertTrue(socialMusicians.get(0).equals(theMusician3));
    }

    @Test
    public void findTopSocialMusicianInMultiExact(){
        //Unlike above, this is hardcoded for a n=4, as it is difficult to elegantly manage collborators dynamically
        //Create a musician set to hold our musicians
        ArrayList<Musician> musicianList = new ArrayList<>();
        List<Album> albumList = new ArrayList<>();
        Album album1 = new Album (1994, "ECM-1090", "Album1");
        Album album2 = new Album (1994, "ECM-1090", "Album2");
        Album album3 = new Album (1994, "ECM-1090", "Album3");
        Album album4 = new Album (1994, "ECM-1090", "Album4");
        Album album5 = new Album (1994, "ECM-1090", "Album5");
        for (int i = 0; i < 4; i++){
            musicianList.add(new Musician("Musician "+(i+1)));
        }
        //We expect Musician 1 to have 0 collaborators and be last. We expect Musician 3 to have 2 collaborators and be first.
        musicianList.get(0).setAlbums(new HashSet<Album>(Arrays.asList(album5)));
        musicianList.get(1).setAlbums(new HashSet<Album>(Arrays.asList(album1, album2)));
        musicianList.get(2).setAlbums(new HashSet<Album>(Arrays.asList(album2, album3)));
        musicianList.get(3).setAlbums(new HashSet<Album>(Arrays.asList(album3, album4)));

        HashSet<Musician> fakeMusicianSet = new HashSet<>(musicianList);
        for (Musician mus:fakeMusicianSet) {
            dao.createOrUpdate(mus);
        }
        List<Musician> socialMusicians = ecmMiner.mostSocialMusicians(4);
        assertEquals(4, socialMusicians.size());

        assertEquals(socialMusicians.get(0).getName(), musicianList.get(2).getName());
        assertEquals(socialMusicians.get(3).getName(), musicianList.get(0).getName());
    }
}