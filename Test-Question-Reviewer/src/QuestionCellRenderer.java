import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class QuestionCellRenderer extends DefaultTableCellRenderer {

	public Component getTableCellRendererComponent(
			JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column)
	{
		QuestionTableModel qtm=(QuestionTableModel) table.getModel();
		QuestionList questionList=(QuestionList) qtm.getValueAtRow(row);
		Question question=questionList.getQuestion(column);
		String id=question.getId();
		Boolean correct=question.isCorrect();
		
		if(id==null) {
			setBackground(Color.white);
		}
		else if(correct==null){
			setBackground(Color.LIGHT_GRAY);
		}
		else if(correct==true) {
			setBackground(Color.green);
		}
		else if(correct==false) {
			setBackground(Color.red);
		}

		return super.getTableCellRendererComponent(table, value, isSelected,
				hasFocus, row, column);
	}
}
