import java.util.Set;

public class Score {

	private String Id;
	private Set<String> selectAns;
	private Set<String> correctAns;
	private Boolean correct;
	
	public Score() {
		Id=null;
		correct=null;
	}
	
	public Score(String id, Set<String> selectAns,Set<String> correctAns) {
		Id = id;
		this.selectAns=selectAns;
		this.correctAns=correctAns;
	}
	
	public String getId() {
		return Id;
	}
	
	public void setId(String id) {
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

	public Boolean isCorrect() {
		return correct;
	}
	
	public void setCorrect(Boolean correct) {
		this.correct = correct;
	}
}
