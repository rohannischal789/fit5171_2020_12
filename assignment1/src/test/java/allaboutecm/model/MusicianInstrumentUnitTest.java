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
    @DisplayName("Musician cannot be null")
    public void musicianCannotBeNull() {
        assertThrows(NullPointerException.class, () -> musicianInstrument.setMusician(null));
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
    @DisplayName("Musician Instruments objects cannot be different")
    public void musicianInstrumentObjectsEqualOrNot() {
        MusicianInstrument musicianInstrument1 = new MusicianInstrument(new Musician("Keith Jarrett"), new MusicalInstrument("Piano"));
        assertEquals(musicianInstrument, musicianInstrument1);
    }

    @Test
    @DisplayName("Musician Instruments objects cannot be different")
    public void musicianInstrumentEqualOrNot() {
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
