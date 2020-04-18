package allaboutecm.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TrackUnitTest {

    private Track track;

    @BeforeEach
    public void setUp() {
        track = new Track("Track 1", "4:11", "Jazz",1);
        track.getReviews().addAll(Arrays.asList("Nice song" , "Brilliant track"));
    }

    @Test
    @DisplayName("Track name cannot be null")
    public void trackNameCannotBeNull() {
        assertThrows(NullPointerException.class, () -> track.setName(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t"})
    @DisplayName("Track name cannot be empty or blank")
    public void trackNameCannotBeEmptyOrBlank(String arg) {
        assertThrows(IllegalArgumentException.class, () -> track.setName(arg));
    }

    @Test
    @DisplayName("track name cannot be different")
    public void trackNameSameOrNot() {
        assertEquals(track.getName(), "Track 1");
    }

    @Test
    @DisplayName("Track genre cannot be null")
    public void trackGenreCannotBeNull() {
        assertThrows(NullPointerException.class, () -> track.setGenre(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t"})
    @DisplayName("Track genre cannot be empty or blank")
    public void trackGenreCannotBeEmptyOrBlank(String arg) {
        assertThrows(IllegalArgumentException.class, () -> track.setGenre(arg));
    }

    @Test
    @DisplayName("track genre cannot be different")
    public void trackGenreSameOrNot() {
        assertEquals(track.getGenre(), "Jazz");
    }

    @Test
    @DisplayName("Track duration cannot be null")
    public void trackDurationCannotBeNull() {
        assertThrows(NullPointerException.class, () -> track.setDuration(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t"})
    @DisplayName("Track duration cannot be empty or blank")
    public void trackDurationCannotBeEmptyOrBlank(String arg) {
        assertThrows(IllegalArgumentException.class, () -> track.setDuration(arg));
    }

    @Test
    @DisplayName("track genre cannot be different")
    public void trackDurationSameOrNot() {
        assertEquals(track.getDuration(), "4:11");
    }
}
