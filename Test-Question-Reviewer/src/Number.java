import java.math.BigDecimal;
import java.math.RoundingMode;

public class Number {

	public double roundDecimal(double num, int deci) {
		BigDecimal instance=new BigDecimal(Double.toString(num));
		instance=instance.setScale(deci,RoundingMode.HALF_UP);
		return instance.doubleValue();
	}
	
}
