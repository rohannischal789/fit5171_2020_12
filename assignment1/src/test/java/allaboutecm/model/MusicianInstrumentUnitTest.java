package allaboutecm.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
public class MusicianInstrumentUnitTest {

    private MusicianInstrument musicianInstrument;

    @BeforeEach
    public void setUp() {
        musicianInstrument = new MusicianInstrument(new Musician("Keith Jarrett"), new MusicalInstrument("Piano"));
    }

    @Test
    @DisplayName("Musical Instrument cannot be null")
    public void musicalInstrumentCannotBeNull() {
        assertThrows(NullPointerException.class, () -> musicianInstrument.setMusicalInstrument(null));
    }

    @Test
    @DisplayName("Musical Instrument cannot be null")
    public void constructorMusicalInstrumentCannotBeNull() {
        assertThrows(NullPointerException.class, () -> new MusicianInstrument(new Musician("Keith Jarrett"), null));
    }

    @Test
    @DisplayName("Musician cannot be null")
    public void constructorMusicianCannotBeNull() {
        assertThrows(NullPointerException.class, () -> new MusicianInstrument(null, new MusicalInstrument("Piano")));
    }

    @Test
    @DisplayName("Musical Instrument and Musician cannot be null")
    public void constructorBothCannotBeNull() {
        assertThrows(NullPointerException.class, () -> new MusicianInstrument(null, null));
    }

    @Test
    @DisplayName("Musical Instruments cannot be different")
    public void musicalInstrumentsEqualOrNot(){
        MusicalInstrument musicalInstrument1 = new MusicalInstrument("Piano" );
        assertEquals(musicianInstrument.getMusicalInstrument(), musicalInstrument1);
    }

    @Test
    @DisplayName("Musicians cannot be different")
    public void musiciansEqualOrNot(){
        Musician musician1 = new Musician("Keith Jarrett" );
        assertEquals(musicianInstrument.getMusician(), musician1);
    }

    @Test
    @DisplayName("Equals should return a false when there are different values in objects")
    public void musicianInstrumentEqualsShouldBeFalse(){
        MusicianInstrument musicianInstrument1 = new MusicianInstrument(new Musician("ABC Jarrett"), new MusicalInstrument("Piano"));
        assertEquals(musicianInstrument.equals(musicianInstrument1), false);
    }

    @Test
    @DisplayName("Musician Instruments objects cannot be different")
    public void musicianInstrumentSameOrNot() {
        MusicianInstrument musicianInstrument1 = new MusicianInstrument(new Musician("Keith Jarrett"), new MusicalInstrument("Piano"));
        assertTrue(musicianInstrument.equals(musicianInstrument1));
    }

    @Test
    @DisplayName("Musician Instruments hashcodes cannot be different")
    public void musicianInstrumentHashCodeEqualOrNot() {
        int musicianInstrumentHashCode1 = Objects.hash(new Musician("Keith Jarrett"), new MusicalInstrument("Piano"));
        assertEquals(musicianInstrument.hashCode(), musicianInstrumentHashCode1);
    }

}
