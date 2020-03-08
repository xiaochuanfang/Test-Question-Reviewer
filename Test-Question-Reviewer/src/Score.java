
public class Score {

	private int Id;
	private String selectAns;
	private String answer;
	private boolean correct;
	
	public Score() {
		
	}
	
	public Score(int id, String selectAns,String answer) {
		Id = id;
		this.selectAns=selectAns;
		this.answer=answer;
	}
	
	public int getId() {
		return Id;
	}
	
	public void setId(int id) {
		Id = id;
	}
	
	public String getSelectAns() {
		return selectAns;
	}

	public void setSelectAns(String selectAns) {
		this.selectAns = selectAns;
	}
	
	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public boolean isCorrect() {
		return correct;
	}
	
	public void setCorrect(boolean correct) {
		this.correct = correct;
	}
}
