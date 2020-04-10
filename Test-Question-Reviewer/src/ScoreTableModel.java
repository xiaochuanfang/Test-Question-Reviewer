import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class ScoreTableModel extends AbstractTableModel{

	// Table column number
	protected int colNum=10;

	// Table Object for a row
	final Vector<Scorelist> data = new Vector<Scorelist>();

	// Adding a row to the table
	public void addScorelist(Scorelist scorelist) {
		data.addElement(scorelist);
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
		Scorelist scorelist=(Scorelist) data.elementAt(row);
		Score score=scorelist.getScore(column);
		return score.getId();
	}

	public Scorelist getValueAtRow(int row) {
		Scorelist scorelist = (Scorelist) data.elementAt(row);
		return scorelist;
	}

}
