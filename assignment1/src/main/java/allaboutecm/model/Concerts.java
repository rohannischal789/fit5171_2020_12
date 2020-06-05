package allaboutecm.model;

import allaboutecm.dataaccess.neo4j.DateConverter;
import allaboutecm.dataaccess.neo4j.URLConverter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;
@NodeEntity
public class Concerts extends Entity {

	@Convert(DateConverter.class)
    @Property(name="date")
    private Date date;

    @Property(name="name")
    private String name;

    @Property(name="location")
    private String location;

	@Property(name="country")
    private String country;

    @Relationship(type="featuredMusicians")
    private List<Musician> featuredMusicians;
    
    
    public Concerts() {
		super();
	}

	public Concerts(Date date, String name) {
		super();
		notNull(date);
		notNull(name);
		notBlank(name);	
		
		this.date = date;
		this.name = name;
		this.featuredMusicians = Lists.newArrayList();
	}

	@Override
	public String toString() {
		return "Concerts [date=" + date + ", name=" + name + ", location=" + location + ", country=" + country
				+ ", featuredMusicians=" + featuredMusicians + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((featuredMusicians == null) ? 0 : featuredMusicians.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Concerts other = (Concerts) obj;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (featuredMusicians == null) {
			if (other.featuredMusicians != null)
				return false;
		} else if (!featuredMusicians.equals(other.featuredMusicians))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		notNull(date);
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		notNull(name);
		notBlank(name);
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		notNull(location);
		notBlank(location);
		this.location = location;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		notNull(country);
		notBlank(country);
		this.country = country;
	}

	public List<Musician> getFeaturedMusicians() {
		return featuredMusicians;
	}

	public void setFeaturedMusicians(List<Musician> featuredMusicians) {
    	notNull(featuredMusicians);
        if (featuredMusicians.size()==0) throw new IllegalArgumentException("You should enter at least one featured musician");
        for (Musician m : featuredMusicians){
        	if (m == null) {
        		throw new NullPointerException("featured musicians cannot be null");
        	}
        }
		this.featuredMusicians = featuredMusicians;
	}
}
