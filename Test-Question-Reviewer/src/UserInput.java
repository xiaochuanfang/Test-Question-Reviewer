import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JButton;

public class UserInput extends JFrame {
	
	private JTextArea taID;
	private JTextArea taType;
	private JTextArea taStatement;
	private JTextArea taAnswer;
	private JTextArea taStartChoice;
	private JTextArea taEndChoice;
	private JTextArea taStartRow;

	private InputChecker check=new InputChecker();
	private Number number=new Number();
	
	private final Font font=new Font("Monospaced", Font.BOLD, 25);
	private final File defaultFile=new File("default.txt");
	
	/**
	 * Create the frame.
	 */
	public UserInput(File file) {
		
		//Create content pane
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		
		//Create text area for ID column input
		taID = new JTextArea();
		taID.setFont(font);
		taID.setBounds(62, 110, 54, 44);
		contentPane.add(taID);
		
		//Create text area for Question Type column input
		taType = new JTextArea();
		taType.setFont(font);
		taType.setBounds(232, 110, 59, 46);
		contentPane.add(taType);
				
		//Create text area for question column input
		taStatement = new JTextArea();
		taStatement.setFont(font);
		taStatement.setBounds(402, 110, 59, 44);
		contentPane.add(taStatement);
		
		//Create text area for answer column input
		taAnswer = new JTextArea();
		taAnswer.setFont(font);
		taAnswer.setBounds(558, 110, 54, 44);
		contentPane.add(taAnswer);
		
		//Create text area for choice start column input
		taStartChoice = new JTextArea();
		taStartChoice.setFont(font);
		taStartChoice.setBounds(698, 110, 44, 44);
		contentPane.add(taStartChoice);
		
		//Create text area for choice end column input
		taEndChoice = new JTextArea();
		taEndChoice.setFont(font);
		taEndChoice.setBounds(771, 110, 44, 44);
		contentPane.add(taEndChoice);
		
		//Create text area for start row input 
		taStartRow = new JTextArea();
		taStartRow.setFont(font);
		taStartRow.setBounds(62, 257, 54, 44);
		contentPane.add(taStartRow);
		
		//Label for "ID"
		JLabel lbID = new JLabel("ID");
		lbID.setHorizontalAlignment(SwingConstants.CENTER);
		lbID.setFont(font);
		lbID.setBounds(62, 54, 54, 37);
		contentPane.add(lbID);
		
		//Label for "Type" 
		JLabel lbType = new JLabel("Type");
		lbType.setHorizontalAlignment(SwingConstants.CENTER);
		lbType.setFont(font);
		lbType.setBounds(210, 50, 93, 44);
		contentPane.add(lbType);

		//Label for "Question"
		JLabel lbStatement = new JLabel("Question");
		lbStatement.setHorizontalAlignment(SwingConstants.CENTER);
		lbStatement.setFont(font);
		lbStatement.setBounds(352, 57, 146, 37);
		contentPane.add(lbStatement);
		
		//Label for "Answer"
		JLabel lbAnswer = new JLabel("Answer");
		lbAnswer.setHorizontalAlignment(SwingConstants.CENTER);
		lbAnswer.setFont(font);
		lbAnswer.setBounds(533, 57, 117, 37);
		contentPane.add(lbAnswer);
		
		//Label for "Choices"
		JLabel lbChoices = new JLabel("Choices");
		lbChoices.setHorizontalAlignment(SwingConstants.CENTER);
		lbChoices.setFont(font);
		lbChoices.setBounds(698, 54, 117, 37);
		contentPane.add(lbChoices);
		
		//Label for "-"
		JLabel dash = new JLabel("-");
		dash.setHorizontalAlignment(SwingConstants.CENTER);
		dash.setFont(font);
		dash.setBounds(722, 114, 69, 20);
		contentPane.add(dash);
		
		//Label for "Question Starts At Row"
		JLabel lbStartAt = new JLabel("Question Starts At Row");
		lbStartAt.setFont(font);
		lbStartAt.setBounds(62, 213, 342, 37);
		contentPane.add(lbStartAt);
		
		//Button to start the test
		JButton btnStartTest = new JButton("Start Test");
		btnStartTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				int ID,type,ques,ans,choi,choi2,startAt;
				
				//Get the user inputs into string
				String inputID=taID.getText();
				String inputType=taType.getText();
				String inputQues=taStatement.getText();
				String inputAns=taAnswer.getText();
				String inputChoi=taStartChoice.getText();
				String inputChoi2=taEndChoice.getText();
				String inputStartAt=taStartRow.getText();
				
				//Check if the column inputs for ID, Type, Question, Answer, and Choices are alphabets 
				if(check.isAlphabet(inputID) && check.isAlphabet(inputType) && check.isAlphabet(inputQues) 
						 && check.isAlphabet(inputAns) && check.isAlphabet(inputChoi) && check.isAlphabet(inputChoi2)) {
					
					//Check if the question start row input is number  
					if(check.isPosInt(Integer.parseInt(inputStartAt), true)) {
						
						//Save all the inputs in integers
						ID=number.alphaToInt(inputID, 0);
						type=number.alphaToInt(inputType, 0);
						ques=number.alphaToInt(inputQues, 0);
						ans=number.alphaToInt(inputAns, 0);
						choi=number.alphaToInt(inputChoi, 0);
						choi2=number.alphaToInt(inputChoi2, 0);
						startAt=Integer.parseInt(inputStartAt);
						
						//Create the Question ArrayList and start a new Test
						try {
							Readexcel reader=new Readexcel(file); 
							ArrayList<Question> qlist=reader.createQuestionList(ID,type,ques,ans,choi,choi2,startAt);
							Test test=new Test(qlist);
							
							//Save the input values for next time
							saveDefaultValue();
							
							dispose();
						} catch (IOException exception) {
							exception.printStackTrace();
						}
					}
					
					//If user enter a non-numerical input for start row, show an error message
					else {
						JOptionPane.showMessageDialog(null, "Enter an number for the 'Start At Row'!",
								"Invalid Input", JOptionPane.ERROR_MESSAGE);
					}
				}
				
				//If user enter a non-alphabet input for id, question, answer and choices, show an error message
				else {
					JOptionPane.showMessageDialog(null, "Enter an Engish alphabet for the first 5 inputs!",
							"Invalid Input", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		
		//Set the "Start test" button attribute
		btnStartTest.setFont(font);
		btnStartTest.setBounds(489, 257, 197, 44);
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
		btnCancel.setFont(font);
		btnCancel.setBounds(735, 257, 146, 44);
		contentPane.add(btnCancel);

		//Load last used value
		try {
			loadLastValue();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Set frame operation and position
		setContentPane(contentPane);		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 929, 450);
		setVisible(true);
	}
	
	//Save user inputs include ID, type, question, answer, choices and start row for next time
	public void saveDefaultValue() throws IOException {
		defaultFile.createNewFile();
		FileWriter fileWriter=new FileWriter(defaultFile);
		
		fileWriter.write(taID.getText()+"\n");
		fileWriter.write(taType.getText()+"\n");
		fileWriter.write(taStatement.getText()+"\n");
		fileWriter.write(taAnswer.getText()+"\n");
		fileWriter.write(taStartChoice.getText()+"\n");
		fileWriter.write(taEndChoice.getText()+"\n");
		fileWriter.write(taStartRow.getText());
		
		fileWriter.close();
	}
	
	//Reload last time user inputs include ID, type, question, answer, choices and start row
	public void loadLastValue() throws IOException {
		if(defaultFile.exists()) {
			FileReader fileReader=new FileReader(defaultFile);
			Scanner scanner=new Scanner(fileReader);
			
			taID.setText(scanner.next());
			taType.setText(scanner.next());
			taStatement.setText(scanner.next());
			taAnswer.setText(scanner.next());
			taStartChoice.setText(scanner.next());
			taEndChoice.setText(scanner.next());
			taStartRow.setText(scanner.next());
			
			scanner.close();
		}
	}
}
