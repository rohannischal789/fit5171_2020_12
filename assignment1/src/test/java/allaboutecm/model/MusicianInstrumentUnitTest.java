package allaboutecm.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
public class MusicianInstrumentUnitTest {

    private MusicianInstrument musicianInstrument;

    @BeforeEach
    public void setUp() {
        ArrayList<MusicalInstrument> myArray = new ArrayList<MusicalInstrument>();
        myArray.add(new MusicalInstrument("Piano"));
        myArray.add(new MusicalInstrument("Guitar"));
        Set<MusicalInstrument> musicalInstrumentList = new HashSet<>(myArray);
        musicianInstrument = new MusicianInstrument(new Musician("Keith Jarrett"), musicalInstrumentList);
    }

    @Test
    @DisplayName("Musical Instrument cannot be null")
    public void musicalInstrumentCannotBeNull() {
        assertThrows(NullPointerException.class, () -> musicianInstrument.setMusicalInstruments(null));
    }

    @Test
    @DisplayName("Musical Instrument cannot be null")
    public void constructorMusicalInstrumentCannotBeNull() {
        assertThrows(NullPointerException.class, () -> new MusicianInstrument(new Musician("Keith Jarrett"), null));
    }

    @Test
    @DisplayName("Musician cannot be null")
    public void constructorMusicianCannotBeNull() {
        ArrayList<MusicalInstrument> myArray = new ArrayList<MusicalInstrument>();
        myArray.add(new MusicalInstrument("Piano"));
        myArray.add(new MusicalInstrument("Guitar"));
        Set<MusicalInstrument> musicalInstrumentList = new HashSet<>(myArray);
        assertThrows(NullPointerException.class, () -> new MusicianInstrument(null, musicalInstrumentList));
    }

    @Test
    @DisplayName("Musical Instrument and Musician cannot be null")
    public void constructorBothCannotBeNull() {
        assertThrows(NullPointerException.class, () -> new MusicianInstrument(null, null));
    }

    @Test
    @DisplayName("Musical Instruments cannot be different")
    public void musicalInstrumentsEqualOrNot(){
        ArrayList<MusicalInstrument> myArray = new ArrayList<MusicalInstrument>();
        myArray.add(new MusicalInstrument("Piano"));
        myArray.add(new MusicalInstrument("Guitar"));
        Set<MusicalInstrument> musicalInstrumentList = new HashSet<>(myArray);
        musicianInstrument.setMusicalInstruments(musicalInstrumentList);
        assertEquals(musicianInstrument.getMusicalInstruments(), musicalInstrumentList);
    }

    @Test
    @DisplayName("Musicians cannot be different")
    public void musiciansEqualOrNot(){
        Musician musician1 = new Musician("Keith Jarrett" );
        assertEquals(musicianInstrument.getMusician(), musician1);
    }


    @Test
    @DisplayName("Musician cannot be null")
    public void musicianCannotBeNull() {
        assertThrows(NullPointerException.class, () -> musicianInstrument.setMusician(null));
    }

    @Test
    @DisplayName("Instrument cannot be empty")
    public void musicalInstrumentCannotBeEmpty() {
        assertThrows(IllegalArgumentException.class, () -> musicianInstrument.setMusicalInstruments(new HashSet<>()));
    }

    @Test
    @DisplayName("Equals should return a false when there are different values in objects")
    public void musicianInstrumentEqualsShouldBeFalse(){
        ArrayList<MusicalInstrument> myArray = new ArrayList<MusicalInstrument>();
        myArray.add(new MusicalInstrument("Piano"));
        myArray.add(new MusicalInstrument("Guitar"));
        Set<MusicalInstrument> musicalInstrumentList = new HashSet<>(myArray);
        MusicianInstrument musicianInstrument1 = new MusicianInstrument(new Musician("ABC Jarrett"), musicalInstrumentList);
        assertEquals(musicianInstrument.equals(musicianInstrument1), false);
    }

    @Test
    @DisplayName("Musician Instruments objects cannot be different")
    public void musicianInstrumentSameOrNot() {
        ArrayList<MusicalInstrument> myArray = new ArrayList<MusicalInstrument>();
        myArray.add(new MusicalInstrument("Piano"));
        myArray.add(new MusicalInstrument("Guitar"));
        Set<MusicalInstrument> musicalInstrumentList = new HashSet<>(myArray);
        MusicianInstrument musicianInstrument1 = new MusicianInstrument(new Musician("Keith Jarrett"), musicalInstrumentList);
        assertTrue(musicianInstrument.equals(musicianInstrument1));
    }
    @Test
    public void sameMusicianAndInstrumentHasSameMusicianInstrumentOrNot() {
        ArrayList<MusicalInstrument> myArray = new ArrayList<MusicalInstrument>();
        myArray.add(new MusicalInstrument("Piano"));
        myArray.add(new MusicalInstrument("Guitar"));
        Set<MusicalInstrument> musicalInstrumentList = new HashSet<>(myArray);
        MusicianInstrument anotherMusicianInstrument = new MusicianInstrument(new Musician("Keith Jarrett"), musicalInstrumentList);

        assertEquals(anotherMusicianInstrument, musicianInstrument);
    }

    @Test
    @DisplayName("Musician Instruments hashcodes cannot be different")
    public void musicianInstrumentHashCodeEqualOrNot() {
        ArrayList<MusicalInstrument> myArray = new ArrayList<MusicalInstrument>();
        myArray.add(new MusicalInstrument("Piano"));
        myArray.add(new MusicalInstrument("Guitar"));
        Set<MusicalInstrument> musicalInstrumentList = new HashSet<>(myArray);
        int musicianInstrumentHashCode1 = Objects.hash(new Musician("Keith Jarrett"), musicalInstrumentList);
        assertEquals(musicianInstrument.hashCode(), musicianInstrumentHashCode1);
    }

}
