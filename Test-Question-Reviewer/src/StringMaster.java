import java.io.File;

public class StringMaster {

	//Get filename extension
	public static String getFileExtension(File file) {
		String filename=file.getName();
		if(filename.lastIndexOf(".") != -1 && filename.lastIndexOf(".") != 0) {
			return filename.substring(filename.lastIndexOf(".")+1);
		}
		else return "";
	}
	
	
	public static String getFileName(File file) {
		String filename=file.getName();
		if(filename.lastIndexOf(".")!=-1) {
			filename=filename.substring(0,filename.lastIndexOf("."));
		}
		return filename;
	}

	//Remove space or line feed from front and back
	public static String removeFrontAndBackSpace(String s) {
		while(s.substring(s.length()-1).equals(" ")
				|| s.substring(s.length()-1).equals("\n")) {
			s=s.substring(0,s.length()-1);
		}
		while(s.substring(0,1).equals(" ")
				|| s.substring(0,1).equals("\n")) {
			s=s.substring(1);
		}
		return s;
	}
	
	//Check if the String is null or just space/line feed
	public static boolean isEmptyString(String s) {
		return (s==null || s.trim().isEmpty());
	}
}
