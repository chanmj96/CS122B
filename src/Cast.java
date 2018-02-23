public class Cast {
	private String ftitle;	// Film Title
	private String fid;		// Film ID
	private String dname;	// Director Name
	private String did;		// Director ID
	private String aname;	// Actor Name
	
	// NOTE: Should probably make actor names into an array
	
	public Cast() {}
	public Cast(String ftitle, String fid, String dname, String did, String aname) {
		this.ftitle = ftitle;
		this.fid = fid;
		this.dname = dname;
		this.did = did;
		this.aname = aname;
	}
	public String getFilm() {
		return ftitle;
	}
	public void setFilm(String ftitle) {
		this.ftitle = ftitle;
	}
	public String getFilmId() {
		return fid;
	}
	public void setFilmId(String fid) {
		this.fid = fid;
	}
	public String getDirector() {
		return dname;
	}
	public void setDirector(String dname) {
		this.dname = dname;
	}
	public String getDirectorId() {
		return did;
	}
	public void setDirectorId(String did) {
		this.did = did;
	}
	public String getActor() {
		return aname;
	}
	public void setActor(String aname) {
		this.aname = aname;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Movie Details - ");
		sb.append("Title:" + getFilm() + " (" + getFilmId() + ")");
		sb.append(", ");
		sb.append("Director:" + getDirector() + " (" + getDirectorId() + ")");
		sb.append(", ");
		sb.append("Actor:" + getActor());
		sb.append(".");
		
		return sb.toString();
	}
}
