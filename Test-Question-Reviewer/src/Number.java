import java.math.BigDecimal;
import java.math.RoundingMode;

public class Number {
	
	//Round the decimal number into specific decimal place
	public double roundDecimal(double num, int deci){
		if(deci<=0) {
			throw new IllegalArgumentException("Argument 'deci' must be greater than zero");
		}
		BigDecimal instance=new BigDecimal(Double.toString(num));
		instance=instance.setScale(deci,RoundingMode.HALF_UP);
		return instance.doubleValue();
	}
	
	//Check if a double variable has all zero after decimal
	public boolean isIntValue(double num) {
		return ((num==Math.floor(num)) && !Double.isInfinite(num));
	}
	
	/*Convert the alphabet into number where the 
	 * first letter 'A' matches to parameter 'start'
	 */
	public int alphaToInt(String alpha, int startAWith) {
		int value=0;
		int shiftValue=-10+startAWith;
		
		for(int i=0;i<alpha.length();i++) {
			char c=alpha.charAt(i);
			value+=(Character.getNumericValue(c)+shiftValue);
		}
		return value;
	}
	
	/*Convert the number into alphabet where the 
	 * first letter 'A' match to parameter 'start'
	 */
	public String intToAlpha(int num, int startAWith) {
		if(num<startAWith) {
			System.out.println("Error: your number is smaller than the start point");
			System.exit(1);
		}
		int shiftValue=65-startAWith;
		
		String a=String.valueOf((char) (num+shiftValue));
		return a;
	}
	
}
