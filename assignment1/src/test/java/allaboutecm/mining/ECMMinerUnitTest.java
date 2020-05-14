package allaboutecm.mining;

import allaboutecm.dataaccess.DAO;
import allaboutecm.dataaccess.neo4j.Neo4jDAO;
import allaboutecm.model.Album;
import allaboutecm.model.MusicalInstrument;
import allaboutecm.model.MusicianInstrument;
import allaboutecm.model.Musician;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Array;
import java.util.*;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TODO: perform unit testing on the ECMMiner class, by making use of mocking.
 */
class ECMMinerUnitTest {
    private DAO dao;
    private ECMMiner ecmMiner;

    @BeforeEach
    public void setUp() {

        dao = mock(Neo4jDAO.class);
        ecmMiner = new ECMMiner(dao);
    }

    @Test
    public void shouldReturnTheMusicianWhenThereIsOnlyOneProlific() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Musician musician = new Musician("Keith Jarrett");
        musician.setAlbums(Sets.newHashSet(album));
        when(dao.loadAll(Musician.class)).thenReturn(Sets.newHashSet(musician));

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
        when(dao.loadAll(MusicianInstrument.class)).thenReturn(Sets.newHashSet(onlyEntry));

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
        when(dao.loadAll(MusicianInstrument.class)).thenReturn(Sets.newHashSet(firstMusician, secondMusician, thirdMusician));
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
        //Intercept the dao call, give it our data instead
        when(dao.loadAll(MusicianInstrument.class)).thenReturn(Sets.newHashSet(musicianInstruments));
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
        //Intercept the dao call, give it our data instead
        when(dao.loadAll(MusicianInstrument.class)).thenReturn(Sets.newHashSet(musicianInstruments));
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

        when(dao.loadAll(MusicianInstrument.class)).thenReturn(Sets.newHashSet(musicianInstruments));
        assertThrows(IllegalArgumentException.class, () -> ecmMiner.mostTalentedMusicians(6));
    }

    @Test
    public void findBusiestYearByReleaseYearCount(){
        ArrayList<Album> myArray = new ArrayList<Album>();
        myArray.add(new Album(2011,"ECM123","Album1"));
        myArray.add(new Album(2011,"ECM773","Album10"));
        myArray.add(new Album(2001,"ECM800","Album11"));
        when(dao.loadAll(Album.class)).thenReturn(Sets.newHashSet(myArray));

        List<Integer> years = ecmMiner.busiestYears(1);

        assertEquals(1, years.size());
    }

    @Test
        public void findBusiestYearByReleaseYear(){
            ArrayList<Album> myArray = new ArrayList<Album>();
            myArray.add(new Album(2011,"ECM123","Album1"));
            myArray.add(new Album(2011,"ECM773","Album10"));
            myArray.add(new Album(2001,"ECM800","Album11"));
            when(dao.loadAll(Album.class)).thenReturn(Sets.newHashSet(myArray));

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

        when(dao.loadAll(Musician.class)).thenReturn(Sets.newHashSet(fakeMusicianSet));

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

        when(dao.loadAll(Musician.class)).thenReturn(Sets.newHashSet(fakeMusicianSet));

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

        when(dao.loadAll(Musician.class)).thenReturn(Sets.newHashSet(fakeMusicianSet));

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
        when(dao.loadAll(Musician.class)).thenReturn(fakeMusicianSet);

        List<Musician> socialMusicians = ecmMiner.mostSocialMusicians(4);
        assertEquals(4, socialMusicians.size());

        assertEquals(socialMusicians.get(0).getName(), musicianList.get(2).getName());
        assertEquals(socialMusicians.get(3).getName(), musicianList.get(0).getName());
    }

    @Test
    public void findTopSocialMusicianInMulti(){
        //Unlike above, this is hardcoded for a n=6, as it is difficult to elegantly manage collaborators dynamically
        //Create a musician set to hold our musicians
        ArrayList<Musician> musicianList = new ArrayList<>();
        List<Album> albumList = new ArrayList<>();
        Album album1 = new Album (1994, "ECM-1090", "Album1");
        Album album2 = new Album (1994, "ECM-1090", "Album2");
        Album album3 = new Album (1994, "ECM-1090", "Album3");
        Album album4 = new Album (1994, "ECM-1090", "Album4");
        Album album5 = new Album (1994, "ECM-1090", "Album5");
        Album album6 = new Album (1994, "ECM-1090", "Album6");
        Album album7 = new Album (1994, "ECM-1090", "Album6");
        for (int i = 0; i < 6; i++){
            musicianList.add(new Musician("Musician "+(i+1)));
        }
        //We expect Musician 1 to have 0 collaborators and be last. We expect Musician 3 to have 2 collaborators and be first.
        musicianList.get(0).setAlbums(new HashSet<Album>(Arrays.asList(album5))); // C = 1
        musicianList.get(1).setAlbums(new HashSet<Album>(Arrays.asList(album1, album2))); // C = 1
        musicianList.get(2).setAlbums(new HashSet<Album>(Arrays.asList(album2, album3, album6, album7))); //C = 4
        musicianList.get(3).setAlbums(new HashSet<Album>(Arrays.asList(album3, album4))); //C = 2
        musicianList.get(4).setAlbums(new HashSet<Album>(Arrays.asList(album5, album6, album4))); // C = 3
;       musicianList.get(4).setAlbums(new HashSet<Album>(Arrays.asList(album7))); //C = 1

        HashSet<Musician> fakeMusicianSet = new HashSet<>(musicianList);
        when(dao.loadAll(Musician.class)).thenReturn(fakeMusicianSet);

        List<Musician> socialMusicians = ecmMiner.mostSocialMusicians(3);
        assertEquals(3, socialMusicians.size());

        assertEquals(socialMusicians.get(0).getName(), musicianList.get(2).getName());
        assertEquals(socialMusicians.get(1).getName(), musicianList.get(4).getName());
        assertEquals(socialMusicians.get(2).getName(), musicianList.get(3).getName());
    }


    @ParameterizedTest
    @ValueSource(ints = {-1,0})
    public void impossibleKValueSocial(int number){
        assertThrows(IllegalArgumentException.class, () -> ecmMiner.mostSocialMusicians(number));
    }

    @Test
    public void busiestYearSingleKSingleList(){
        //Create a set to hold the albums we make for the test.

        //Add a new album to the set.

        //Do the when(dao....) line

        //Create a years array and assign it using the busiest years method.

        //Assert that years.size() == 1.
        //Assert that the year you set the album to on line 405 is the same as the element years[0]
    }

    @Test
    public  void busiestYearMultiKExactList(){
        //Create a set to hold the albums we make for the test.

        //Add new albums to the set:
        //Add 3 albums released in one year, 2 albums released in a different year, and 1 album released in the third year.

        //Do when(dao...)

        //Create a years array and assign it using the busiest years method (k = 3).

        // Assert that years.size() == 3.
        //Assert that years[0] == 3 album year, years[1] == 2 album year, years[2] == 1 album year.
    }

    public void busiestYearMultiKLargeList(){
        //Create a set to hold the albums we make for the test.

        //Add new albums to the set:
        //Add 4 albums released in one year, 3 albums released in a different year, and 2 album released in the third year and add 3 additional albums each...
        //...on a different year to each other (the years with multiple other albums)

        //Do when(dao...)

        //Create a years array and assign it using the busiest years method (k = 3).

        // Assert that years.size() == 3.
        //Assert that years[0] == 4 album year, years[1] == 3 album year, years[2] == 2 album year.
    }



    @Test
    public void similarAlbumSingleKTwoList(){
        //Create a set to hold the albums we make for the test.
        //Create a set to hold musicians we're going to make for the test.

        //Add 2 albums to this set, one album we're going to search using, and one album we're going to use as a search result.
        //Create a Musician to be used for the test.
        //Add the set of both albums to the musician using musician.setAlbums()
        //Add the musician to the musician set

        //when(dao.loadall(Album.class...) using the album set.
        //when(dao.loadall(Musician.class...) using the musician set.

        //Create a results array (Should hold album objects) and assign it using the mostSimilarAlbum(1, searchAlbum) method

        //Assert that results.size() == 1
        //Assert that results[0] == the album we made to be a search result.

    }

    @Test
    public void similarAlbumsSingleKLargeList(){
        //Create a set to hold the albums we make for the test. Call this fakeAlbumSet
        //Create a set to hold musicians we're going to make for the test.

        //Create a set to hold albums. Call this similarAlbumSet
        //Add 2 albums to this set, one album we're going to search using, and one album we're going to use as a search result. *ALSO* Add these albums to fakeAlbumSet
        //This should look like:

            //Album searchAlbum = new Album(...);
            //simlarAlbumSet.add(searchAlbum);
            //fakeAlbumSet.add(searchAlbum);
            //Album resultAlbum = new Album(...)
            //similarAlbumSet.add(resultAlbum);
            //fakeAlbumSet.add(fakeAlbum);

        //Create a musician and add similarAlbumSet to the musician.
        //Add the musician to the musicianSet

        //Create a new set, and add 3 albums to this set. Call this unSimilarAlbumSet *ALSO* add these albums to fake album set
        //This should look like:
            //Album unsimilarAlbum1 = new Album(...);
            //unsimilarAlbumSet.add(unsimilarAlbum1);
            //fakeAlbumSet.add(unsimilarAlbum1);
            //and so on until unsimilarAlbum3 ...

        //Create a new musician and add unsimilarAlbumSet to this musician
        //Add the musician to the musician set

        //when(dao.loadall(Album.class...) using the album set.
        //when(dao.loadall(Musician.class...) using the musician set.

        //Create a results array (Should hold album objects) and assign it using the mostSimilarAlbum(1, searchAlbum) method

        //Assert that results.size() == 1;
        //Assert that results[0] == resultAlbum;
    }

    @Test
    public void multipleKExactList(){}

    @Test
    public void multipleKLargeLsit(){}

    @Test
    public void excludeSearchAlbumFromResults(){}

    @Test
    public void impossibleKValueSimilarity(){}

    @Test
    public void tooLargeKValueSimilarity(){}


}