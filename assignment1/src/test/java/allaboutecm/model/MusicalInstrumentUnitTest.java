package allaboutecm.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class MusicalInstrumentUnitTest {

    private MusicalInstrument musicalInstrument;

    @BeforeEach
    public void setUp() {
        musicalInstrument = new MusicalInstrument("Piano");
    }

    @Test
    @DisplayName("Musical Instrument name cannot be null")
    public void musicalInstrumentCannotBeNull() {
        assertThrows(NullPointerException.class, () -> musicalInstrument.setName(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t"})
    @DisplayName("Musical Instrument name cannot be empty or blank")
    public void musicalInstrumentCannotBeEmptyOrBlank(String arg) {
        assertThrows(IllegalArgumentException.class, () -> musicalInstrument.setName(arg));
    }

    @Test
    @DisplayName("Musical Instrument name cannot be different")
    public void musicalInstrumentNameSameOrNot(){
        assertEquals(musicalInstrument.getName(),"Piano");
    }

    @Test
    @DisplayName("Musical Instrument objects cannot be different")
    public void musicalInstrumentObjectsqualOrNot() {
        MusicalInstrument musicalInstrument1 = new MusicalInstrument("Piano");
        assertEquals(musicalInstrument, musicalInstrument1);
    }

    @Test
    @DisplayName("Musical Instrument objects cannot be different")
    public void musicalInstrumentEqualOrNot() {
        MusicalInstrument musicalInstrument1 = new MusicalInstrument("Piano");
        assertTrue(musicalInstrument.equals(musicalInstrument1));
    }

    @Test
    @DisplayName("Equals should return a false when there are different values in objects")
    public void musicalInstrumentEqualsShouldBeFalse(){
        MusicalInstrument musicalInstrument1 = new MusicalInstrument("Guitar");
        assertEquals(musicalInstrument.equals(musicalInstrument1), false);
    }

    @Test
    @DisplayName("Musical Instrument hashcodes cannot be different")
    public void musicalInstrumentHashcodesEqualOrNot() {
        int musicalInstrument1 = Objects.hash("Piano");
        assertEquals(musicalInstrument.hashCode(), musicalInstrument1);
    }

    @Test
    @DisplayName("Boundary Error, Name may be 100 characters long, but no longer")
    //Musical Instrument names must be permitted to be 100 characters long, but no longer
    public void musicianInstrumentLengthShouldAccept100Chars(){
        musicalInstrument.setName("a aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
    }

    @Test
    @DisplayName("Names are capped to 100 characters")
    //Musical Instrument name may not be longer than 100 characters. The tested string is 101 characters.
    public void musicianInstrumentLengthShouldNotBeMoreThan100Chars(){
        assertThrows(IllegalArgumentException.class, () -> new MusicalInstrument("a aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
    }
}
