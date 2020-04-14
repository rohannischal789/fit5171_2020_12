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

import static org.junit.jupiter.api.Assertions.*;

//nupur is here
class AlbumUnitTest {
    //Ben was here
    private Album album;

    @BeforeEach
    public void setUp() throws MalformedURLException {
        album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        List<MusicianInstrument> trackList = Arrays.asList(
                new MusicianInstrument(new Musician("Frank"),new MusicalInstrument("Ukele")),
                new MusicianInstrument(new Musician("Adam"),new MusicalInstrument("Guitar")),
                new MusicianInstrument(new Musician("Annie"),new MusicalInstrument("Violin")));
        album.getInstruments().addAll(new HashSet<MusicianInstrument>(trackList));
        album.getTracks().addAll(Arrays.asList("Track1","Track2","Track3","Track4"));
        album.setAlbumURL(new URL("https://www.google.com/"));
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
        List<MusicianInstrument> list = Arrays.asList(new MusicianInstrument(new Musician("Frank"),new MusicalInstrument("Ukele")),
                new MusicianInstrument(new Musician("Adam"),new MusicalInstrument("Guitar")),
                new MusicianInstrument(new Musician("Annie"),new MusicalInstrument("Violin")));
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
        URL url1 = new URL("https://www.google.com/");
        assertEquals(album.getAlbumURL(), url1);
    }
}