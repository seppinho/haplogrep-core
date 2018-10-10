package contamination.objects;

public class HSDEntry {

	String id;
	String range;
	StringBuffer profile;

	public HSDEntry() {
		range = "1-16569";
		profile = new StringBuffer();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public StringBuffer getProfile() {
		return profile;
	}

	public void appendToProfile(String pos) {
		profile.append("\t" + pos);
	}
	
	@Override
	public String toString() {
		return (id + "\t" + range + "\t" +"?" + profile);
	}
}