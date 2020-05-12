package allaboutecm.mining;

import allaboutecm.dataaccess.DAO;
import allaboutecm.model.Album;
import allaboutecm.model.Musician;
import allaboutecm.model.MusicianInstrument;
import allaboutecm.model.MusicalInstrument;
import com.google.common.collect.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * TODO: implement and test the methods in this class.
 * Note that you can extend the Neo4jDAO class to make implementing this class easier.
 */
public class ECMMiner {
    private static Logger logger = LoggerFactory.getLogger(ECMMiner.class);

    private final DAO dao;

    public ECMMiner(DAO dao) {
        this.dao = dao;
    }

    /**
     * Returns the most prolific musician in terms of number of albums released.
     *
     * @Param k the number of musicians to be returned.
     * @Param startYear, endYear between the two years [startYear, endYear].
     * When startYear/endYear is negative, that means startYear/endYear is ignored.
     */
    public List<Musician> mostProlificMusicians(int k, int startYear, int endYear) {
        Collection<Musician> musicians = dao.loadAll(Musician.class);
        Map<String, Musician> nameMap = Maps.newHashMap();
        for (Musician m : musicians) {
            nameMap.put(m.getName(), m);
        }

        ListMultimap<String, Album> multimap = MultimapBuilder.treeKeys().arrayListValues().build();
        ListMultimap<Integer, Musician> countMap = MultimapBuilder.treeKeys().arrayListValues().build();

        for (Musician musician : musicians) {
            Set<Album> albums = musician.getAlbums();
            for (Album album : albums) {
                boolean toInclude =
                        !((startYear > 0 && album.getReleaseYear() < startYear) ||
                                (endYear > 0 && album.getReleaseYear() > endYear));

                if (toInclude) {
                    multimap.put(musician.getName(), album);
                }
            }
        }

        Map<String, Collection<Album>> albumMultimap = multimap.asMap();
        for (String name : albumMultimap.keySet()) {
            Collection<Album> albums = albumMultimap.get(name);
            int size = albums.size();
            countMap.put(size, nameMap.get(name));
        }

        List<Musician> result = Lists.newArrayList();
        List<Integer> sortedKeys = Lists.newArrayList(countMap.keySet());
        sortedKeys.sort(Ordering.natural().reverse());
        for (Integer count : sortedKeys) {
            List<Musician> list = countMap.get(count);
            if (list.size() >= k) {
                break;
            }
            if (result.size() + list.size() >= k) {
                int newAddition = k - result.size();
                for (int i = 0; i < newAddition; i++) {
                    result.add(list.get(i));
                }
            } else {
                result.addAll(list);
            }
        }

        return result;
    }

    /**
     * Most talented musicians by the number of different musical instruments they play
     *
     * @Param k the number of musicians to be returned.
     */
    public List<Musician> mostTalentedMusicians(int k) {
        if (k < 1){
            throw new IllegalArgumentException();
        }
        //Let's grab all the musician instruments.
        Collection<MusicianInstrument> musicianInstruments = dao.loadAll(MusicianInstrument.class);
        if (k > musicianInstruments.size()){
            throw new IllegalArgumentException();
        }
        Map<MusicianInstrument, Integer> countMap = Maps.newHashMap();

        for (MusicianInstrument m: musicianInstruments){
            Integer instrumentCount = m.getMusicalInstruments().size();
            countMap.put(m, instrumentCount);
        }
        List<Map.Entry<MusicianInstrument, Integer>> sortList = new LinkedList<Map.Entry<MusicianInstrument, Integer>>(countMap.entrySet());

        Collections.sort(sortList, new Comparator<Map.Entry<MusicianInstrument, Integer>>() {
            @Override
            public int compare(Map.Entry<MusicianInstrument, Integer> o1, Map.Entry<MusicianInstrument, Integer> o2) {
                return (o1.getValue().compareTo(o2.getValue()));
            }
        });
        ArrayList resultList = new ArrayList();
        for (int i = sortList.size() - 1; i >= sortList.size() - k; i--){
            resultList.add(sortList.get(i).getKey().getMusician());
        }

        return resultList;
    }

    /**
     * Musicians that collaborate the most widely, by the number of other musicians they work with on albums.
     *
     * @Param k the number of musicians to be returned.
     */

    public List<Musician> mostSocialMusicians(int k) {
        //Get the list of musicians.
        //Get the list of albums.

        //Create a map where the musician is the key, and their collaborators are values. - musicianCollaborators

        //For each album: get the list of musicians
            //For each musician found for each album:
                //Add all musicians also on the album
                    // Map  Key Values
                    // ALBUM 1: A,B,C,D
                    //      A   |B, C, D
                    //      B   |A, C, D
                    //      C....

                    //ALBUM 2: A,E
                    //Say we find an album that A and E worked on
                    //      A   |B, C, D, E
                    //      E   |A

        //

        return Lists.newArrayList();
    }

    /**
     * Busiest year in terms of number of albums released.
     *
     * @Param k the number of years to be returned.
     */

    public List<Integer> busiestYears(int k) {
        //Load all albums:
        Collection<Album> albums = dao.loadAll(Album.class);
        Map<Integer, Integer> countMap = Maps.newHashMap();
        //Create a blank hashMap of Key = Integer (represents Year) , value Album (represents an album released that year)
        //Loop through all albums:
            //Get the year.
            //Add it as a value to the entry where the year is the key. myHashMap.add(albumYear, theAlbum);
        for (Album a: albums) {
            Integer year = a.getReleaseYear();
            countMap.put(year, countMap.get(year) + 1);
        }
        //Create a map of Integer, Integer where the year is the key, and the value is the count of albums From your Integer/Album hashMap

        List<Map.Entry<Integer, Integer>> sortList = new LinkedList<Map.Entry<Integer, Integer>>(countMap.entrySet());

        Collections.sort(sortList, new Comparator<Map.Entry<Integer, Integer>>() {
            @Override
            public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                return (o1.getValue().compareTo(o2.getValue()));
            }
        });
        //Create an arrayList of the top K years, and return it
        ArrayList resultList = new ArrayList();
        for (int i = sortList.size() - 1; i >= sortList.size() - k; i--) {
            resultList.add(sortList.get(i).getKey());
        }
        return resultList;
    }

    /**
     * Most similar albums to a give album. The similarity can be defined in a variety of ways.
     * For example, it can be defined over the musicians in albums, the similarity between names
     * of the albums & tracks, etc.
     *
     * @Param k the number of albums to be returned.
     * @Param album
     */

    public List<Album> mostSimilarAlbums(int k, Album album) {
        return Lists.newArrayList();
    }
}
