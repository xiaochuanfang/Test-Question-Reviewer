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

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
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

	/**
	 * Create the frame.
	 */
	public Menu() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 967, 568);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewTest = new JButton("New Test");
		btnNewTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser=new JFileChooser();
				chooser.setPreferredSize(new Dimension(800,600));
				String path=System.getProperty("user.dir")+System.getProperty("file.separator");
				System.out.print("Paht is"+path);
				File dir=new File(path);
				chooser.setCurrentDirectory(dir);
				DialogModifyer modifyer=new DialogModifyer();
				modifyer.setDialogFont(chooser.getComponents());
				int returnVal = chooser.showOpenDialog(btnNewTest);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					File file=chooser.getSelectedFile();
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
		btnNewTest.setFont(new Font("Tahoma", Font.PLAIN, 28));
		btnNewTest.setBounds(145, 349, 251, 86);
		contentPane.add(btnNewTest);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		btnExit.setFont(new Font("Tahoma", Font.PLAIN, 28));
		btnExit.setBounds(527, 349, 256, 86);
		contentPane.add(btnExit);
	}
}
