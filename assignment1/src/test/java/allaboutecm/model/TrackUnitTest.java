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
    @DisplayName("Track name cannot be more than 100")
    public void trackNameLengthMoreThan100() {
        assertThrows(IllegalArgumentException.class, () -> track.setName("asfhvhfvEVFHBDSSJHDAJDGHDGADHJDGSJGDJHAGDAGDSGDGSDGAHGDHGDHGDGDHSDJADGHASDGJAGDHASGDJSADHGSGSADJDGSDQYGUDYEDGJSHBDASHBDJASHBDSHBDJASDBHSAHSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS"));
    }

    @Test
    @DisplayName("Track name cannot have 101 characters")
    public void trackNameLength101Check() {
        assertThrows(IllegalArgumentException.class, () -> track.setName("ljkajdjdsklasdbsdlkjbdsjgbdsjkgbdjsgbjbdskajdjkfdskaaaaalsdksksjdsskjfskdjfsskdfsdkjffjkdfnksdjfnskjd"));
    }

    @Test
    @DisplayName("Track name cannot be more than 100")
    public void constructorTrackNameLengthMoreThan100() {
        assertThrows(IllegalArgumentException.class, () -> new Track("asfhvhfvEVFHBDSSJHDAJDGHDGADHJDGSJGDJHAGDAGDSGDGSDGAHGDHGDHGDGDHSDJADGHASDGJAGDHASGDJSADHGSGSADJDGSDQYGUDYEDGJSHBDASHBDJASHBDSHBDJASDBHSAHSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS","4:11","Pop",2));
    }

    @Test
    @DisplayName("Track name cannot have 101 characters")
    public void constructorTrackNameLength101Check() {
        assertThrows(IllegalArgumentException.class, () -> new Track("ljkajdjdsklasdbsdlkjbdsjgbdsjkgbdjsgbjbdskajdjkfdskaaaaalsdksksjdsskjfskdjfsskdfsdkjffjkdfnksdjfnskjd","4:11","Pop",2));
    }

    @Test
    @DisplayName("Track name in constructor cannot be empty or blank")
    public void constructorTrackNameLength101() {
        assertThrows(IllegalArgumentException.class, () -> new Track("ljkajdjdsklasdbsdlkjbdsjgbdsjkgbdjsgbjbdskajdjkfdskaaaaalsdksksjdsskjfskdjfsskdfsdkjffjkdfnksdjfnskjd", "4:11", "Jazz",500));
    }

    @Test
    @DisplayName("track name cannot be different")
    public void trackNameSameOrNot() {
        track.setName("Track 1");
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
        track.setGenre("Jazz");
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
    public void trackDurationSameOrNot()
    {
        track.setDuration("4:11");
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
        track.setTrackNumber(1);
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
    @DisplayName("Equals should return a false in case of null")
    public void trackEqualsNull(){
        assertEquals(track.equals(null), false);
    }

    @Test
    @DisplayName("Equals should return a false when there are different objects")
    public void trackEqualsDifferentObject(){
        assertEquals(track.equals(new Rating(1,"Test")), false);
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

    @Test
    @DisplayName("Track name cannot be null")
    public void constructorTrackNameCannotBeNull() {
        assertThrows(NullPointerException.class, () -> new Track(null, "4:11", "Jazz",1));
    }

    @Test
    @DisplayName("Track duration cannot be null")
    public void constructorTrackDurationCannotBeNull() {
        assertThrows(NullPointerException.class, () -> new Track("Track 1", null, "Jazz",1));
    }

    @Test
    @DisplayName("Track genre cannot be null")
    public void constructorTrackGenreCannotBeNull() {
        assertThrows(NullPointerException.class, () -> new Track("Track 1", "4:11", null,1));
    }

    @Test
    @DisplayName("Track number cannot be zero")
    public void constructorTrackNumberCannotBeZero() {
        assertThrows(IllegalArgumentException.class, () -> new Track("Track 1", "4:11", "Jazz",0));
    }

    @Test
    @DisplayName("Track number cannot be more than 499")
    public void constructorTrackNumberCannotBeMoreThan499() {
        assertThrows(IllegalArgumentException.class, () -> new Track("Track 1", "4:11", "Jazz",500));
    }

}
