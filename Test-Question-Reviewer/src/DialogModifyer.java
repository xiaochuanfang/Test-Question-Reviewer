import java.awt.Component;
import java.awt.Container;

import javax.swing.JList;
import javax.swing.JTable;

public class DialogModifyer {

	public void setDialogFont(Component[] comp) {
		for (int x = 0; x < comp.length; x++) {
            // System.out.println( comp[x].toString() ); // Trying to know the type of each element in the JFileChooser.
            if (comp[x] instanceof Container)
                setDialogFont(((Container) comp[x]).getComponents());

            try {
                if (comp[x] instanceof JList || comp[x] instanceof JTable)
                    comp[x].setFont(comp[x].getFont().deriveFont(comp[x].getFont().getSize() * 2f));
            } catch (Exception e) {
            } // do nothing
        }
	}
	
}
