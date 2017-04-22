package utilities;

import java.util.Objects;

/**
 * These Objects contain only two Integer values
 */
public class Coordinate {
	private int iCoord;
	private int jCoord;
	
	public Coordinate(int iCoord, int jCoord){
		this.iCoord = iCoord;
		this.jCoord = jCoord;
	}
	
	public int getICoord() {
		return iCoord;
	}
	public void setICoord(int iCoord) {
		this.iCoord = iCoord;
	}
	public int getJCoord() {
		return jCoord;
	}
	public void setJCoord(int jCoord) {
		this.jCoord = jCoord;
	}
	
	
	@Override
	public boolean equals(Object o) {
		
		if (o.getClass() != this.getClass()) {
			return false;
		}
		
		Coordinate other = (Coordinate) o;
		return this.iCoord == other.iCoord && this.jCoord == other.jCoord;
	}
	
	@Override
	public int hashCode() {
		
		return Objects.hash(iCoord, jCoord);
	}
}
