import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ScoreCellRenderer extends DefaultTableCellRenderer {

	public Component getTableCellRendererComponent(
			JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column)
	{
		ScoreTableModel stm=(ScoreTableModel) table.getModel();
		Scorelist scorelist=(Scorelist) stm.getValueAtRow(row);
		Score score=scorelist.getScore(column);
		String id=score.getId();
		Boolean correct=score.isCorrect();
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
