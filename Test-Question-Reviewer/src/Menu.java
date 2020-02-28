import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class Menu extends JFrame {

	//Launch the application
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu frame = new Menu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	//Create the frame
	public Menu() {
		
		//Set frame operation and bound
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 967, 568);
		
		//Create the contentPane
		JPanel contentPane= new JPanel();
		
		//Set the contentPane bound and layout
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		
		//Create "New Test" button to start a test
		JButton btnNewTest = new JButton("New Test");
		
		//Set action listener for the "New Test" button
		btnNewTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//Open file with JFileChooser
				JFileChooser chooser=new JFileChooser();
				
				//Set JFileChooser GUI size
				chooser.setPreferredSize(new Dimension(800,600));
				
				//Set the path of the JFileChooser
				String path=System.getProperty("user.dir")+System.getProperty("file.separator");
				File dir=new File(path);
				chooser.setCurrentDirectory(dir);
				
				//Set the filename font inside the GUI
				DialogModifyer modifyer=new DialogModifyer();
				modifyer.setDialogFont(chooser.getComponents());
				
				//Read the file if user choose to open the file
				int returnVal = chooser.showOpenDialog(btnNewTest);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					File file=chooser.getSelectedFile();
					
					//Create the Question ArrayList and start a new Test
					Readexcel reader=new Readexcel(); 
					try {
						ArrayList<Question> qlist=reader.createQuestionList(file);
						Test test=new Test(qlist);
						dispose();
					} catch (IOException exception) {
						exception.printStackTrace();
					}
				}
			}
		});
		
		//Set font and bound for the "New Test" button
		btnNewTest.setFont(new Font("Tahoma", Font.PLAIN, 28));
		btnNewTest.setBounds(145, 349, 251, 86);
		
		//Create "Exit" button to exit the program
		JButton btnExit = new JButton("Exit");
		
		//Set action listener for the "Exit" button
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		
		//Set font and bound for the "New Test" button
		btnExit.setFont(new Font("Tahoma", Font.PLAIN, 28));
		btnExit.setBounds(527, 349, 256, 86);
		
		//Add contentPane, "New Test" button and "Exit" button to the frame
		setContentPane(contentPane);
		contentPane.add(btnNewTest);
		contentPane.add(btnExit);
	}
}
