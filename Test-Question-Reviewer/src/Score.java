import java.util.Set;

public class Score {

	private int Id;
	private Set<String> selectAns;
	private Set<String> correctAns;
	private boolean correct;
	
	public Score() {
		
	}
	
	public Score(int id, Set<String> selectAns,Set<String> correctAns) {
		Id = id;
		this.selectAns=selectAns;
		this.correctAns=correctAns;
	}
	
	public int getId() {
		return Id;
	}
	
	public void setId(int id) {
		Id = id;
	}
	
	public Set<String> getSelectAns() {
		return selectAns;
	}

	public void setSelectAns(Set<String> selectAns) {
		this.selectAns = selectAns;
	}
	
	public Set<String> getCorrectAns() {
		return correctAns;
	}

	public void setCorrectAns(Set<String> correctAns) {
		this.correctAns = correctAns;
	}

	public boolean isCorrect() {
		return correct;
	}
	
	public void setCorrect(boolean correct) {
		this.correct = correct;
	}
}
