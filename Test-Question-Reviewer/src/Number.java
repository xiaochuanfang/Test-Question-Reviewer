import java.math.BigDecimal;
import java.math.RoundingMode;

public class Number {
	
	//Round the decimal number into specific decimal place
	public double roundDecimal(double num, int deci) {
		BigDecimal instance=new BigDecimal(Double.toString(num));
		instance=instance.setScale(deci,RoundingMode.HALF_UP);
		return instance.doubleValue();
	}
	
	//Convert the alphabet into number which match 'A' to start number
	public int alphaToInt(String alpha, int start) {
		int value=0;
		int shiftValue=-10+start;
		
		for(int i=0;i<alpha.length();i++) {
			char c=alpha.charAt(i);
			value+=(Character.getNumericValue(c)+shiftValue);
		}
		return value;
	}
}
