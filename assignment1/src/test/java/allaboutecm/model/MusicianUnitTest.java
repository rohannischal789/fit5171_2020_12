package allaboutecm.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class MusicianUnitTest {
    private Musician musician;

    @BeforeEach
    public void setUp(){
        musician = new Musician("anything name");
    }

    @Test
    @DisplayName("Correctly match Musician name")
    public void matchMusicianName(){
        assertEquals(musician.getName(), "anything name");
    }

    @Test
    @DisplayName("Non-null name")
    public void nullMusicianNameError(){
        assertThrows(NullPointerException.class, () -> new Musician(null));
    }
}
