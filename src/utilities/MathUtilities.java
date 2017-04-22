package utilities;

public class MathUtilities {
	/** Calculates the greatest common divisor of the inserted numbers by using the Euklid algorithm.
	 * 
	 * @param a Number 1
	 * @param b	Number 2
	 * @return	Greatest common divisor of a and b
	 */
	public static int greatestCommonDivisor(int a, int b){
	    if (a == 0){
	    	return b;
	    }
	    while(b != 0){
	    	if (a > b){
	    		a = a - b;
	    	}
	        else{
	    		b = b - a;
	    	}
	    }
	    return a;
	}
	
	/** Returns the amount of decimal digits of the inserted number */
	public static int digits(int number){
		if(number == 0){
			return 1;
		}
		int digits= 0;
		while(number != 0){
			number= number/10;
			digits++;
		}
		return digits;
	}
	
	/** Returns a random number from 1 to "length" (possible numbers in the Sudoku).*/
	public static int randomNumber(int length){
		return (int) (Math.random()*length+1);
	}
	
	
}
