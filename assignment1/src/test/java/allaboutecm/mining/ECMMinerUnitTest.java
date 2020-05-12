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
    public void shouldReturnTheMusicianWhenThereIsOnlyOne() {
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

}