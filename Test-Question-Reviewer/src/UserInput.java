import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JButton;

public class UserInput extends JFrame {
	
	/**
	 * Create the frame.
	 */
	public UserInput(File file) {
		
		//Create content pane
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		
		//Create text area for ID column input
		JTextArea taID = new JTextArea();
		taID.setFont(new Font("Monospaced", Font.PLAIN, 20));
		taID.setBounds(62, 110, 117, 44);
		contentPane.add(taID);
		
		//Create text area for question column input
		JTextArea taStatement = new JTextArea();
		taStatement.setFont(new Font("Monospaced", Font.PLAIN, 20));
		taStatement.setBounds(282, 110, 117, 44);
		contentPane.add(taStatement);
		
		//Create text area for answer column input
		JTextArea taAnswer = new JTextArea();
		taAnswer.setFont(new Font("Monospaced", Font.PLAIN, 20));
		taAnswer.setBounds(489, 110, 117, 44);
		contentPane.add(taAnswer);
		
		//Create text area for choice start column input
		JTextArea taStartChoice = new JTextArea();
		taStartChoice.setFont(new Font("Monospaced", Font.PLAIN, 20));
		taStartChoice.setBounds(698, 110, 44, 44);
		contentPane.add(taStartChoice);
		
		//Create text area for choice end column input
		JTextArea taEndchoice = new JTextArea();
		taEndchoice.setFont(new Font("Monospaced", Font.PLAIN, 20));
		taEndchoice.setBounds(771, 110, 44, 44);
		contentPane.add(taEndchoice);
		
		//Create text area for start row input 
		JTextArea taStartRow = new JTextArea();
		taStartRow.setFont(new Font("Monospaced", Font.PLAIN, 20));
		taStartRow.setBounds(62, 257, 117, 44);
		contentPane.add(taStartRow);
		
		//Label for "ID"
		JLabel lbID = new JLabel("ID");
		lbID.setHorizontalAlignment(SwingConstants.CENTER);
		lbID.setFont(new Font("Monospaced", Font.BOLD, 25));
		lbID.setBounds(62, 54, 117, 37);
		contentPane.add(lbID);
		
		//Label for "Question"
		JLabel lbStatement = new JLabel("Question");
		lbStatement.setHorizontalAlignment(SwingConstants.CENTER);
		lbStatement.setFont(new Font("Monospaced", Font.BOLD, 25));
		lbStatement.setBounds(264, 54, 146, 37);
		contentPane.add(lbStatement);
		
		//Label for "Answer"
		JLabel lbAnswer = new JLabel("Answer");
		lbAnswer.setHorizontalAlignment(SwingConstants.CENTER);
		lbAnswer.setFont(new Font("Monospaced", Font.BOLD, 25));
		lbAnswer.setBounds(489, 54, 117, 37);
		contentPane.add(lbAnswer);
		
		//Label for "Choices"
		JLabel lbChoices = new JLabel("Choices");
		lbChoices.setHorizontalAlignment(SwingConstants.CENTER);
		lbChoices.setFont(new Font("Monospaced", Font.BOLD, 25));
		lbChoices.setBounds(698, 54, 117, 37);
		contentPane.add(lbChoices);
		
		//Label for "-"
		JLabel dash = new JLabel("-");
		dash.setHorizontalAlignment(SwingConstants.CENTER);
		dash.setFont(new Font("Monospaced", Font.BOLD, 25));
		dash.setBounds(722, 114, 69, 20);
		contentPane.add(dash);
		
		//Label for "Question Starts At Row"
		JLabel lbStartAt = new JLabel("Question Starts At Row");
		lbStartAt.setFont(new Font("Monospaced", Font.BOLD, 25));
		lbStartAt.setBounds(62, 213, 342, 37);
		contentPane.add(lbStartAt);
		
		//Button to start the test
		JButton btnStartTest = new JButton("Start Test");
		btnStartTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				int ID,ques,ans,choi,choi2,startAt;
				
				//Get the user inputs into string
				String inputID=taID.getText();
				String inputQues=taStatement.getText();
				String inputAns=taAnswer.getText();
				String inputChoi=taStartChoice.getText();
				String inputChoi2=taEndchoice.getText();
				String inputStartAt=taStartRow.getText();
				
				InputChecker check=new InputChecker();
				Number number=new Number();
				
				//Check if the column inputs for ID, Question, Answer, and Choices are alphabets 
				if(check.isAlphabet(inputID) && check.isAlphabet(inputQues) && check.isAlphabet(inputAns) 
						&& check.isAlphabet(inputChoi) && check.isAlphabet(inputChoi2)) {
					
					//Check if the question start row input is number  
					if(check.isPosInt(Integer.parseInt(inputStartAt), true)) {
						
						//Save all the inputs in integers
						ID=number.alphaToInt(inputID, 0);
						ques=number.alphaToInt(inputQues, 0);
						ans=number.alphaToInt(inputAns, 0);
						choi=number.alphaToInt(inputChoi, 0);
						choi2=number.alphaToInt(inputChoi2, 0);
						startAt=Integer.parseInt(inputStartAt)-1;
						
						//Create the Question ArrayList and start a new Test
						try {
							Readexcel reader=new Readexcel(file); 
							ArrayList<Question> qlist=reader.createQuestionList(ID,ques,ans,choi,choi2,startAt);
							Test test=new Test(qlist);
							dispose();
						} catch (IOException exception) {
							exception.printStackTrace();
						}
					}
					
					//If user enter a non-numerical input for start row, show an error message
					else {
						JOptionPane.showMessageDialog(null, "Enter an number for the 'Start At Row'!","Invalid Input", JOptionPane.ERROR_MESSAGE);
					}
				}
				
				//If user enter a non-alphabet input for id, question, answer and choices, show an error message
				else {
					JOptionPane.showMessageDialog(null, "Enter an Engish alphabet for the first 5 inputs!","Invalid Input", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		
		//Set the "Start test" button attribute
		btnStartTest.setFont(new Font("Monospaced", Font.BOLD, 25));
		btnStartTest.setBounds(486, 257, 197, 44);
		contentPane.add(btnStartTest);
		
		//Button for cancel the test
		JButton btnCancel = new JButton("Cancel");
		
		//Set action listener for the "Cancel" button
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		
		//Set the "Cancel" button attribute
		btnCancel.setFont(new Font("Monospaced", Font.BOLD, 25));
		btnCancel.setBounds(732, 257, 146, 44);
		contentPane.add(btnCancel);
		
		//Set frame operation and position
		setContentPane(contentPane);		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 929, 396);
		setVisible(true);
	}
	
}
