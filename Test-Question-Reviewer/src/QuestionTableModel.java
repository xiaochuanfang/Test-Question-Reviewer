import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class QuestionTableModel extends AbstractTableModel{

	// Table column number
	protected int colNum=10;

	// Table Object for a row
	final Vector<QuestionList> data = new Vector<QuestionList>();

	// Adding a row to the table
	public void addQuestionList(QuestionList questionList) {
		data.addElement(questionList);
		fireTableRowsInserted(data.size()-1, data.size()-1);
	}

	@Override
	public int getColumnCount() {
		return colNum;
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		QuestionList questionList=(QuestionList) data.elementAt(row);
		Question question=questionList.getQuestion(column);
		return question.getId();
	}

	public QuestionList getValueAtRow(int row) {
		QuestionList questionList = (QuestionList) data.elementAt(row);
		return questionList;
	}

}
