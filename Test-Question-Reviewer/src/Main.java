import java.io.File;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
	
		String path=System.getProperty("user.dir");
		String filename="2019.xls";
		File file=new File(path+"\\"+filename);
		Readexcel reader=new Readexcel();
		try {
			reader.createQuestionList(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
