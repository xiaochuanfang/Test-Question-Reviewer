import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class Menu extends JFrame {

	private final Font font=new Font("Monospaced", Font.PLAIN, 30);

	//Launch the application
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu frame = new Menu();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	//Create the frame
	public Menu() {

		//Create the contentPane
		JPanel contentPane= new JPanel();
		contentPane.setLayout(null);

		//Create button to start a test
		JButton btnNewTest = new JButton("New Test");
		btnNewTest.setFont(font);
		btnNewTest.setBounds(145, 349, 251, 86);
		contentPane.add(btnNewTest);

		//Add action listener for the "New Test" button
		btnNewTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				FileChooser chooser=new FileChooser();
				chooser.setFileType(new FileNameExtensionFilter("Microsoft Word/Excel or PDF", "doc","docx","xlsx","xls","pdf"));
				
				//Read the file if user choose to open the file
				int returnVal = chooser.showOpenDialog(btnNewTest);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					File file=chooser.getSelectedFile();

					//Create UserInput dialog for user to enter additional information
					String type=StringMaster.getFileExtension(file);
					if(type.equalsIgnoreCase("docx") || type.equalsIgnoreCase("doc")) {
						UserInputForWord userInput=new UserInputForWord(file);
					}
					else if(type.equalsIgnoreCase("xlsx") || type.equalsIgnoreCase("xls")) {
						UserInputForExcel userInput=new UserInputForExcel(file);
					}
					dispose();
				}
			}
		});

		//Create button to exit the program
		JButton btnExit = new JButton("Exit");
		btnExit.setFont(font);
		btnExit.setBounds(527, 349, 256, 86);
		contentPane.add(btnExit);

		//Add action listener for the "Exit" button
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});

		//Set frame operation and position
		setBounds(100, 100, 967, 568);
		setContentPane(contentPane);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
