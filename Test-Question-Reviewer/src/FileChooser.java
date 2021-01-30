import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.JTextComponent;

public class FileChooser extends JFileChooser{

	//Constructor
	public FileChooser() {
		setPreferredSize(new Dimension(800,600));
		increaseFileChooserFont(getComponents(),1.75f);
		setDirAsCurrent();
		setAcceptAllFileFilterUsed(false);
	}

	//Set FileChooser size
	public void setSize(Dimension d) {
		setPreferredSize(d);
	}

	//Set FileChooser's font base on user's input font
	public void setFileChooserFont(Component[] comp,Font font) {
		for (int x = 0; x < comp.length; x++) {

			//Looping its child elements 
			if (comp[x] instanceof Container) {
				setFileChooserFont(((Container) comp[x]).getComponents(),font);
			}

			//Change the font if the element is a JList, JTable, JLabel, JComboBox or JButton
			if (comp[x] instanceof JList || comp[x] instanceof JTable || comp[x] instanceof JLabel || 
					comp[x] instanceof JComboBox || comp[x] instanceof JButton) {
				comp[x].setFont(font);
			}
		}
	}

	//Set FileChooser's font size base on component's original font size
	public void increaseFileChooserFont(Component[] comp, float fontSizeMultiplier) {
		for (int x = 0; x < comp.length; x++) {

			//Looping its child elements 
			if (comp[x] instanceof Container) {
				increaseFileChooserFont(((Container) comp[x]).getComponents(),fontSizeMultiplier);
			}

			//Increase font size if the element is a JList, JTable, JLabel, JComboBox, JButton or JTextComponent
			if (comp[x] instanceof JList || comp[x] instanceof JTable || comp[x] instanceof JLabel || 
					comp[x] instanceof JComboBox || comp[x] instanceof JButton || comp[x] instanceof JTextComponent) {
				comp[x].setFont(comp[x].getFont().deriveFont(comp[x].getFont().getSize() * fontSizeMultiplier));
			}
		}
	}

	//Set FileChooser's directory
	public void setDirAsCurrent() {
		String currentDir=System.getProperty("user.dir")+System.getProperty("file.separator");
		File path=new File(currentDir);
		setCurrentDirectory(path);
	}

	
	//Set File filter for desire file type
	public void setFileType(FileNameExtensionFilter f) {
		addChoosableFileFilter(f);
	}
}
