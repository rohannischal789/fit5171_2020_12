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


    //Constraint we should throw illegal argument exception if we give a K > MusicianInstrument Count
}