package allaboutecm.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AlbumUnitTest {
    private Album album;

    @BeforeEach
    public void setUp(){
        album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        List<MusicianInstrument> trackList = Arrays.asList(
                new MusicianInstrument(new Musician("Frank Frank"), new MusicalInstrument("Ukele")),
                new MusicianInstrument(new Musician("Adam Adam"), new MusicalInstrument("Guitar")),
                new MusicianInstrument(new Musician("Annie Annie"), new MusicalInstrument("Violin")));
        album.getInstruments().addAll(new HashSet<MusicianInstrument>(trackList));
        album.getTracks().addAll(Arrays.asList("Track1", "Track2", "Track3", "Track4"));
        List<Musician> musician = Arrays.asList(new Musician("Keith Jarrett"));
        Set<Musician> musicianSet = musician.stream().collect(Collectors.toSet());
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
    public void albumNameSameOrNot(){
        assertEquals(album.getAlbumName(),"The Köln Concert");
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
    public void recordNumberSameOrNot(){
        assertEquals(album.getRecordNumber(),"ECM 1064/65");
    }


    @DisplayName("Album release Year cannot be empty or zero")
    public void releaseYearCannotBeEmptyOrZero() {
        int arg=0;
        assertThrows(IllegalArgumentException.class, () -> album.setReleaseYear(arg));
    }

    @Test
    @DisplayName("record Number cannot be different")
    public void releaseYearSameOrNot(){
        assertEquals(album.getReleaseYear(),1975);
    }

    @Test
    public void sameNameAndNumberMeansSameAlbum() {
        Album album1 = new Album(1975, "ECM 1064/65", "The Köln Concert");

        assertEquals(album, album1);
    }

    @Test
    @DisplayName("Album musicians cannot be null")
    public void albumMusiciansCannotBeNull() {
        assertThrows(NullPointerException.class, () -> album.setFeaturedMusicians(null));
    }

    @Test
    @DisplayName("Album tracks cannot be null")
    public void albumTracksCannotBeNull() {
        assertThrows(NullPointerException.class, () -> album.setTracks(null));
    }

    @Test
    @DisplayName("Album url cannot be null")
    public void albumURLCannotBeNull() {
        assertThrows(NullPointerException.class, () -> album.setAlbumURL(null));
    }

    @Test
    @DisplayName("Album instruments cannot be different")
    public void albumInstrumentsCannotBeDifferent() {
        List<MusicianInstrument> list = Arrays.asList(new MusicianInstrument(new Musician("Frank Frank"),new MusicalInstrument("Ukele")),
                new MusicianInstrument(new Musician("Adam Adam"),new MusicalInstrument("Guitar")),
                new MusicianInstrument(new Musician("Annie Annie"),new MusicalInstrument("Violin")));
        Set<MusicianInstrument> musicianInstrument1 = new HashSet<MusicianInstrument>(list);
        assertEquals(album.getInstruments(), musicianInstrument1);
    }

    @Test
    @DisplayName("Album tracks cannot be different")
    public void albumTracksCannotBeDifferent() {
        List<String> trackList = Arrays.asList("Track1","Track2","Track3","Track4");
        assertEquals(album.getTracks(), trackList);
    }

    @Test
    @DisplayName("Album URL cannot be different")
    public void albumURLCannotBeDifferent() throws MalformedURLException {
        album.setAlbumURL(new URL("https://www.google.com/"));
        URL url1 = new URL("https://www.google.com/");
        assertEquals(album.getAlbumURL(), url1);
    }
}