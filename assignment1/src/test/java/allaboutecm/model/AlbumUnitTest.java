package allaboutecm.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AlbumUnitTest {
    private Album album;

    @BeforeEach
    public void setUp() {
        album = new Album(1975, "ECM 1064/65", "The Köln Concert");
    }

    @Test
    @DisplayName("Album name cannot be null")
    public void albumNameCannotBeNull() {
        assertThrows(NullPointerException.class, () -> album.setAlbumName(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t"})
    @DisplayName("Album name cannot be empty or blank")
    public void albumNameCannotBeEmptyOrBlank(String arg) {
        assertThrows(IllegalArgumentException.class, () -> album.setAlbumName(arg));
    }

    @Test
    @DisplayName("album Name cannot be different")
    public void albumNameSameOrNot() {
        assertEquals(album.getAlbumName(), "The Köln Concert");
    }

    @Test
    @DisplayName("Album Record Number cannot be null")
    public void recordNumberCannotBeNull() {
        assertThrows(NullPointerException.class, () -> album.setRecordNumber(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t"})
    @DisplayName("Album Record Number cannot be empty or blank")
    public void recordNumberCannotBeEmptyOrBlank(String arg) {
        assertThrows(IllegalArgumentException.class, () -> album.setRecordNumber(arg));
    }

    @Test
    @DisplayName("record Number cannot be different")
    public void recordNumberSameOrNot() {
        assertEquals(album.getRecordNumber(), "ECM 1064/65");
    }

    @Test
    @DisplayName("Album Release Year May be 1969 or greater")
    public void releaseYearMayBe1969orGreater() {
        album.setReleaseYear(1969);
        album.setReleaseYear(1970);
    }

    @Test
    @DisplayName("Album release Year cannot before 1969")
    public void releaseYearCannotBeBefore1969() {
        int arg = 1968;
        assertThrows(IllegalArgumentException.class, () -> album.setReleaseYear(arg));
    }

    @Test
    @DisplayName("Album release Year may be current year or earlier")
    public void releaseYearMayBeCurrentYearOrEarlier() {
        int curYear = Calendar.getInstance().get(Calendar.YEAR);
        album.setReleaseYear(curYear);
        album.setReleaseYear(curYear - 1);
    }

    @Test
    @DisplayName("Album release Year cannot be after current year")
    public void releaseYearCannotBePastCurrentYear() {
        int year = Calendar.getInstance().get(Calendar.YEAR) + 1;
        assertThrows(IllegalArgumentException.class, () -> album.setReleaseYear(year));
    }

    @Test
    @DisplayName("record Number cannot be different")
    public void releaseYearSameOrNot() {
        assertEquals(album.getReleaseYear(), 1975);
    }

    @Test
    @DisplayName("Album instruments cannot be null")
    public void albumInstrumentsCannotBeNull() {
        assertThrows(NullPointerException.class, () -> album.setInstruments(null));
    }

    @Test
    @DisplayName("Album instruments cannot be different")
    public void albumInstrumentsCannotBeDifferent() {
        //This needs to be given a set of musical instruments.
        List<MusicianInstrument> list = Arrays.asList(new MusicianInstrument(new Musician("Frank Frank"), new MusicalInstrument("Ukele")),
                new MusicianInstrument(new Musician("Adam Adam"), new MusicalInstrument("Guitar")),
                new MusicianInstrument(new Musician("Annie Annie"), new MusicalInstrument("Violin")));
        Set<MusicianInstrument> musicianInstrumentList = new HashSet<>(list);
        album.getInstruments().addAll(new HashSet<MusicianInstrument>(musicianInstrumentList));
        assertEquals(album.getInstruments(), musicianInstrumentList);
    }

    @Test
    @DisplayName("None of the MusicianInstruments can be null")
    //No element of what is passed to setInstruments may be null.
    public void nullMusicianInstrumentTest() {
        MusicianInstrument[] blankArray = {null, null};
        Set<MusicianInstrument> emptyItems = new HashSet<>(Arrays.asList(blankArray));
        assertThrows(NullPointerException.class, () -> album.setInstruments(emptyItems));
    }

    @Test
    @DisplayName("Album musicians cannot be null")
    public void albumMusiciansCannotBeNull() {
        assertThrows(NullPointerException.class, () -> album.setFeaturedMusicians(null));
    }

    @Test
    @DisplayName("Featured Musicians was not correctly set or read back")
    //Featured musicians set and get methods must match input and output
    public void albumFeaturedMusiciansCannotBeDifferent() {
        ArrayList<Musician> myArray = new ArrayList<Musician>();
        myArray.add(new Musician("Frank Frank"));
        myArray.add(new Musician("Adam Adam"));
        myArray.add(new Musician("Annie Annie"));

        Set<Musician> musiciansList = new HashSet<>(myArray);
        album.setFeaturedMusicians(musiciansList);
        assertEquals(album.getFeaturedMusicians(), musiciansList);
    }

    @Test
    @DisplayName("None of the musicians can be null")
    //No element of what is passed to setFeaturedMusicians() may be null.
    public void nullMusicianTest() {
        Musician[] blankArray = {null, null};
        Set<Musician> emptyItems = new HashSet<>(Arrays.asList(blankArray));
        assertThrows(NullPointerException.class, () -> album.setFeaturedMusicians(emptyItems));
    }

    @Test
    @DisplayName("Album tracks cannot be null")
    public void albumTracksCannotBeNull() {
        assertThrows(NullPointerException.class, () -> album.setTracks(null));
    }

    @Test
    @DisplayName("Album tracks cannot be different")
    public void albumTracksCannotBeDifferent() {
        List<Track> trackList = Arrays.asList( new Track("Track 1", "4:11", "Jazz", 1),
                new Track("Track 2", "3:05", "Jazz", 2),
                new Track("Track 3", "5:24", "Rock", 3),
                new Track("Track 4", "4:14", "Jazz", 4));
        album.getTracks().addAll(trackList);
        assertEquals(album.getTracks(), trackList);
    }

    @Test
    @DisplayName("None of the tracks can be null")
    //No element of what is passed to setTracks() may be null.
    public void nullTracksTest() {
        List<Track> emptyItems = Arrays.asList(null,null);
        assertThrows(NullPointerException.class, () -> album.setTracks(emptyItems));
    }

    @Test
    @DisplayName("Album url cannot be null")
    public void albumURLCannotBeNull() {
        assertThrows(NullPointerException.class, () -> album.setAlbumURL(null));
    }

    @Test
    @DisplayName("Album URL cannot be different")
    public void albumURLCannotBeDifferent() throws MalformedURLException {
        album.setAlbumURL(new URL("https://www.ecm.com/"));
        URL url1 = new URL("https://www.ecm.com/");
        assertEquals(album.getAlbumURL(), url1);
    }

    @Test
    @DisplayName("setReleaseYear should be able to set a post-1969 release year")
    //The checks the inside of the lower bound of our 1969-current year release year constraint.
    public void releaseYearNotEarlyValidate() {
        album.setReleaseYear(1969);
        album.setReleaseYear(1970);
    }

    @Test
    @DisplayName("Constructor can't set pre-1969 release year")
    //The checks the outside of the lower bound of our 1969-current year release year constraint.
    public void constructorReleaseYearEarlyValidate() {
        assertThrows(IllegalArgumentException.class, () -> new Album(0, "ECM 1064/65", "The Köln Concert"));
    }

    @Test
    @DisplayName("setReleaseYear should be able to set release date of this year or before")
    //The checks the inside of the upper bound of our 1969-current year release year constraint.
    public void releaseYearNotLateValidate() {
        int curYear = Calendar.getInstance().get(Calendar.YEAR);
        album.setReleaseYear(curYear);
        album.setReleaseYear(curYear - 1);
    }

    @Test
    @DisplayName("setReleaseYear should not be able to set a release year after the current year")
    //The checks the outside of the upper bound of our 1969-current year release year constraint.
    public void releaseYearLateValidate() {
        int curYear = Calendar.getInstance().get(Calendar.YEAR);
        assertThrows(IllegalArgumentException.class, () -> album.setReleaseYear(curYear + 1));
    }

    @Test
    @DisplayName("Constructor can't set null record number")
    //Constructor must not be able to set a null record number, similar to setRecordNumber() above
    public void constructorRecordNumberNullValidate() {
        assertThrows(NullPointerException.class, () -> new Album(1975, null, "The Köln Concert"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t"})
    @DisplayName("Constructor can't set empty record number")
    //Constructor must not be able to set an empty record number, similar to setRecordNumber() above
    public void constructorRecordNumberEmptyValidate(String arg) {
        assertThrows(IllegalArgumentException.class, () -> new Album(1975, arg, "The Köln Concert"));
    }

    @Test
    @DisplayName("Constructor can't set null album name")
    //Constructor must not be able to set a null album name, similar to setAlbumName() above
    public void constructorAlbumNameNullValidate() {
        assertThrows(NullPointerException.class, () -> new Album(1975, "ECM 1064/65", null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t"})
    @DisplayName("Constructor can't set empty album name")
    //Constructor must not be able to set an empty album name, similar to setAlbumName() above
    public void constructorAlbumNameEmptyValidate(String arg) {
        assertThrows(IllegalArgumentException.class, () -> new Album(1975, "ECM 1064/65", arg));
    }

    @Test
    @DisplayName("Album objects cannot be different")
    public void sameNameAndNumberMeansSameAlbum() {
        Album album1 = new Album(1975, "ECM 1064/65", "The Köln Concert");
        assertEquals(album, album1);
    }

    @Test
    @DisplayName("Equals should return a false when there are different values in objects")
    public void albumEqualsShouldBeFalse(){
        Album album1 = new Album(1970, "ECM 1064/65", "The Köln Concert");
        assertEquals(album.equals(album1), false);
    }

    @Test
    @DisplayName("Album hashcodes cannot be different")
    public void albumHashcodesEqualOrNot() {
        int albumHashCode = Objects.hash(1975, "ECM 1064/65", "The Köln Concert");
        assertEquals(album.hashCode(), albumHashCode);
    }


    @Test
    @DisplayName("URL must be on the ECM domain")
    //Test that URL may only be on the ECM domain
    public void albumURLMustContainECM(){
        try {
            URL theURL = new URL("https://www.google.com");
            assertThrows(IllegalArgumentException.class, () -> album.setAlbumURL(theURL));
        } catch (Exception e){

        }
    }

}