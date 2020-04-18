package allaboutecm.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TrackUnitTest {

    private Track track;

    @BeforeEach
    public void setUp() {
        track = new Track("Track 1", "4:11", "Jazz",1);
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
    @DisplayName("Boundary Error, Name may be 100 characters long, but no longer")
    //Musicians names must be permitted to be 100 characters long, but no longer
    public void validButLongNameTest(){
        track.setName("a aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
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
    @DisplayName("track duration cannot be different")
    public void trackDurationSameOrNot() {
        assertEquals(track.getDuration(), "4:11");
    }

    @Test
    @DisplayName("track number cannot be less than one")
    public void trackNumberCannotBeLessThanOne() {
        assertThrows(IllegalArgumentException.class, () -> track.setTrackNumber(0));
    }

    @Test
    @DisplayName("track number cannot be greater than five hundred")
    public void trackNumberCannotBeGreaterThan500() {
        assertThrows(IllegalArgumentException.class, () -> track.setTrackNumber(501));
    }

    @Test
    @DisplayName("track number cannot be different")
    public void trackNumberSameOrNot() {
        assertEquals(track.getTrackNumber(), 1);
    }

    @Test
    @DisplayName("Track reviews cannot be null")
    public void trackReviewsCannotBeNull() {
        assertThrows(NullPointerException.class, () -> track.setReviews(null));
    }

    @Test
    @DisplayName("Track reviews was not correctly set or read back")
    //Featured musicians set and get methods must match input and output
    public void trackReviewsSameOrNot() {
        List<String> list = Arrays.asList("Nice song" , "Brilliant track");
        track.getReviews().addAll(list);
        assertEquals(track.getReviews(), list);
    }

    @Test
    @DisplayName("None of the track reviews can be null")
    //No element of what is passed to setFeaturedMusicians() may be null.
    public void nullTrackReviewsTest() {
        List<String> list = Arrays.asList(null , null);
        track.getReviews().addAll(list);
        assertThrows(NullPointerException.class, () -> track.setReviews(list));
    }

    @Test
    @DisplayName("Track objects cannot be different")
    public void tracksSameOrNot() {
        Track track1 = new Track("Track 1", "4:11", "Jazz",1);
        assertEquals(track, track1);
    }

    @Test
    @DisplayName("Equals should return a false when there are different values in objects")
    public void trackEqualsShouldBeFalse(){
        Track track1 = new Track("Track 9", "4:11", "Jazz",1);
        assertEquals(track.equals(track1), false);
    }

    @Test
    @DisplayName("track hashcodes cannot be different")
    public void trackHashcodesEqualOrNot() {
        int trackHashCode = Objects.hash("Track 1", "4:11", "Jazz", 1);
        assertEquals(track.hashCode(), trackHashCode);
    }
}
