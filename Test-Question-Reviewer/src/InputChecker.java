

public class InputChecker {
	
	//Check if input is an alphabet
	public boolean isAlphabet(String input) {
		if(input==null) {
			return false;
		}
		for(int i=0;i<input.length();i++) {
			char c=input.charAt(i);
			if(!(c>='A' && c<='Z') && !(c>='a' && c<='z')) {
				return false;
			}
		}
		return true;
	}
	
	//Check if input is a positive integer
	public boolean isPosInt(int n, boolean acceptZero) {
		if(acceptZero) {
			if(n>=0) {
				return true;
			}
		}
		else if(n>0) {
			return true;
		}
		return false;
	}
	
	public boolean isValidRange(String a, String b, boolean isAlphabetic) {
		if(isAlphabetic) {
			int minRange=Math.min(a.length(), b.length());
			for(int i=0;i<minRange;i++) {
				if(a.charAt(i)>b.charAt(i)) {
					return false;
				};
			}
		}
		else if(!isAlphabetic){
			int c=Integer.parseInt(a);
			int d=Integer.parseInt(b);
			return c<=d;
		}
		return true;
	}
}
