
public class Question {

	private int Id;
	private String statement;
	private String[] choices;
	private String answer;
	
	public Question() {}
	
	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getQuestion() {
		return statement;
	}

	public void setQuestion(String question) {
		this.statement = question;
	}

	public String[] getChoices() {
		return choices;
	}

	public void setChoices(String[] choices) {
		this.choices = choices;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	
}
