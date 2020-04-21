import java.util.ArrayList;
import java.util.Set;

public class Question {

	private String Id;
	private String type;
	private String statement;
	private ArrayList<String> choices;
	private Set<String> correctAns;
	private Set<String> selectAns;
	private Boolean correct;
	
	public Question() {}
	
	public String getId() {
		return Id;
	}

	public void setId(String id) {
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

	public Set<String> getCorrectAns() {
		return correctAns;
	}

	public void setCorrectAns(Set<String> answer) {
		this.correctAns = answer;
	}
	
	public Set<String> getSelectAns() {
		return selectAns;
	}

	public void setSelectAns(Set<String> selectAns) {
		this.selectAns = selectAns;
	}

	public Boolean isCorrect() {
		return correct;
	}

	public void setCorrect(Boolean correct) {
		this.correct = correct;
	}
}
