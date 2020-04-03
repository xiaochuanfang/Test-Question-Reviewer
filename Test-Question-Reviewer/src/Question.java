import java.util.ArrayList;
import java.util.Set;

public class Question {

	private int Id;
	private String type;
	private String statement;
	private ArrayList<String> choices;
	private Set<String> answer;
	private boolean correct;
	
	public Question() {}
	
	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}
	
	public ArrayList<String> getChoices() {
		return choices;
	}

	public void setChoices(ArrayList<String> choices) {
		this.choices = choices;
	}

	public Set<String> getAnswer() {
		return answer;
	}

	public void setAnswer(Set<String> answer) {
		this.answer = answer;
	}

	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}
}
