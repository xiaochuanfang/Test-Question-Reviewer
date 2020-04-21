import java.util.ArrayList;

public class QuestionList {

	private ArrayList<Question> arraylist;
	
	public QuestionList() {
		arraylist=new ArrayList<Question>();
	}

	public Question getQuestion(int i) {
		return arraylist.get(i);
	}
	
	public void addQuestion(Question q) {
		arraylist.add(q);
	}
	
	public int getSize() {
		return arraylist.size();
	}
}
