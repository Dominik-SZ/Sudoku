package util;

import java.util.Objects;

/**
 * These Objects contain only two Integer values
 */
public class Coordinate {
	public int i;
	public int j;
	
	public Coordinate(int i, int j){
		this.i = i;
		this.j = j;
	}

	@Override
	public boolean equals(Object o) {
		
		if (o.getClass() != this.getClass()) {
			return false;
		}
		
		Coordinate other = (Coordinate) o;
		return this.i == other.i && this.j == other.j;
	}
	
	@Override
	public int hashCode() {
		
		return Objects.hash(i, j);
	}
}
