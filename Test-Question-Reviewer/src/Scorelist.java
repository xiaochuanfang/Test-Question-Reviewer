import java.util.ArrayList;

public class Scorelist {

	private ArrayList<Score> arraylist;
	
	public Scorelist() {
		arraylist=new ArrayList<Score>();
	}

	public Score getScore(int i) {
		return arraylist.get(i);
	}
	
	public void addScore(Score s) {
		arraylist.add(s);
	}
	
	public int getSize() {
		return arraylist.size();
	}
}
