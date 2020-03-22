
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
	
	//Checi if input is a positive integer
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
}
