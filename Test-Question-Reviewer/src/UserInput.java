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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;

public class UserInput extends JDialog {

	private JTextArea taStatement;
	private JTextArea taStartChoice;
	private JTextArea taEndChoice;
	private JTextArea taAnswer;
	private JTextArea taType;
	private JTextArea taStartRow;
	private JTextArea taEndRow;

	private InputChecker check=new InputChecker();
	private Readexcel reader;

	private final Font font=new Font("Monospaced", Font.BOLD, 25);
	private final File defaultFile=new File("default.txt");

	/**
	 * Create the frame.
	 * @throws Exception 
	 */
	public UserInput(File file){

		reader=new Readexcel();

		//Create content pane
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);

		//Create preview of the excel file
		String filename="";

		File previewImg;
		try {
			previewImg = reader.createExcelPreview(file,0);
			filename=previewImg.getName();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		//Create scroll bar for the preview image
		ImageIcon icon=new ImageIcon(filename);
		JScrollPane scrollPane = new JScrollPane(new JLabel(icon));
		scrollPane.setBounds(0, 0, 1590, 337);
		contentPane.add(scrollPane);

		//Create text area for Question Type column input
		taType = new JTextArea();
		taType.setFont(font);
		taType.setBounds(830, 472, 59, 46);
		contentPane.add(taType);

		//Create text area for question column input
		taStatement = new JTextArea();
		taStatement.setFont(font);
		taStatement.setBounds(116, 472, 59, 44);
		contentPane.add(taStatement);

		//Create text area for answer column input
		taAnswer = new JTextArea();
		taAnswer.setFont(font);
		taAnswer.setBounds(603, 472, 54, 44);
		contentPane.add(taAnswer);

		//Create text area for choice start column input
		taStartChoice = new JTextArea();
		taStartChoice.setFont(font);
		taStartChoice.setBounds(337, 472, 44, 44);
		contentPane.add(taStartChoice);

		//Create text area for choice end column input
		taEndChoice = new JTextArea();
		taEndChoice.setFont(font);
		taEndChoice.setBounds(408, 472, 44, 44);
		contentPane.add(taEndChoice);

		//Create text area for start row input 
		taStartRow = new JTextArea();
		taStartRow.setFont(font);
		taStartRow.setBounds(1104, 472, 54, 44);
		contentPane.add(taStartRow);
		
		//Create Text area for end row input
		taEndRow = new JTextArea();
		taEndRow.setFont(font);
		taEndRow.setBounds(1379, 472, 44, 46);
		contentPane.add(taEndRow);

		//Label for "Type" 
		JLabel lbType = new JLabel("Type");
		lbType.setHorizontalAlignment(SwingConstants.CENTER);
		lbType.setFont(font);
		lbType.setBounds(810, 390, 93, 44);
		contentPane.add(lbType);

		//Label for "Question"
		JLabel lbStatement = new JLabel("Question");
		lbStatement.setHorizontalAlignment(SwingConstants.CENTER);
		lbStatement.setFont(font);
		lbStatement.setBounds(73, 394, 146, 37);
		contentPane.add(lbStatement);

		//Label for "Answer"
		JLabel lbAnswer = new JLabel("Answer");
		lbAnswer.setHorizontalAlignment(SwingConstants.CENTER);
		lbAnswer.setFont(font);
		lbAnswer.setBounds(575, 394, 117, 37);
		contentPane.add(lbAnswer);

		//Label for "Choices"
		JLabel lbChoices = new JLabel("Choices");
		lbChoices.setHorizontalAlignment(SwingConstants.CENTER);
		lbChoices.setFont(font);
		lbChoices.setBounds(335, 394, 117, 37);
		contentPane.add(lbChoices);

		//Label for "-"
		JLabel dash = new JLabel("-");
		dash.setHorizontalAlignment(SwingConstants.CENTER);
		dash.setFont(font);
		dash.setBounds(358, 481, 69, 20);
		contentPane.add(dash);

		//Label for "Starts At Row"
		JLabel lbStartAt = new JLabel("Start Row");
		lbStartAt.setFont(font);
		lbStartAt.setBounds(1064, 394, 146, 37);
		contentPane.add(lbStartAt);
		
		//Label for "End At Row"
		JLabel lbEndAt = new JLabel("End Row");
		lbEndAt.setFont(font);
		lbEndAt.setBounds(1346, 389, 117, 46);
		contentPane.add(lbEndAt);

		//Button to start the test
		JButton btnStartTest = new JButton("Start Test");
		btnStartTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				//Get the user inputs
				String inputType=taType.getText();
				String inputQues=taStatement.getText();
				String inputAns=taAnswer.getText();
				String inputChoi=taStartChoice.getText();
				String inputChoi2=taEndChoice.getText();
				String inputStartAt=taStartRow.getText();
				String inputEndAt=taEndRow.getText();
				
				int startAt=Integer.parseInt(inputStartAt);
				int endAt=Integer.parseInt(inputEndAt);

				//Check if the column inputs for ID, Type, Question, Answer, and Choices are alphabets 
				if(check.isAlphabet(inputType) && check.isAlphabet(inputQues) && check.isAlphabet(inputAns) 
						&& check.isAlphabet(inputChoi) && check.isAlphabet(inputChoi2)) {

					//Check if the question start row input is number  
					if(check.isPosInt(startAt, true) 
							&& check.isPosInt(endAt, false)
							&& startAt<=endAt) {

						//Create the Question ArrayList
						ArrayList<Question> qlist;
						try {
							qlist = reader.createQuestionList(file,0,inputType,inputQues,inputAns,inputChoi,inputChoi2,startAt,endAt);

							//Show dialog for user to choose test mode
							TestMode mode=new TestMode(qlist);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						//Save the input values for next time
						try {
							saveDefaultValue();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						dispose();
					}

					//If user enter a non-numerical input for start row, show an error message
					else {
						JOptionPane.showMessageDialog(null, "Enter an valid number for 'Start Row' and 'End Row'!",
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
		btnStartTest.setBounds(626, 565, 197, 44);
		contentPane.add(btnStartTest);

		//Load last used value
		try {
			loadLastValue();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//Set frame operation and position
		setBounds(100, 100, 1615, 707);
		setContentPane(contentPane);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	//Save user inputs include ID, type, question, answer, choices and start row for next time
	public void saveDefaultValue() throws IOException {
		defaultFile.createNewFile();
		FileWriter fileWriter=new FileWriter(defaultFile);

		fileWriter.write(taType.getText()+"\n");
		fileWriter.write(taStatement.getText()+"\n");
		fileWriter.write(taAnswer.getText()+"\n");
		fileWriter.write(taStartChoice.getText()+"\n");
		fileWriter.write(taEndChoice.getText()+"\n");
		fileWriter.write(taStartRow.getText()+"\n");
		fileWriter.write(taEndRow.getText());

		fileWriter.close();
	}

	//Reload last time user inputs include ID, type, question, answer, choices and start row
	public void loadLastValue() throws IOException {
		if(defaultFile.exists()) {
			FileReader fileReader=new FileReader(defaultFile);
			Scanner scanner=new Scanner(fileReader);

			taType.setText(scanner.next());
			taStatement.setText(scanner.next());
			taAnswer.setText(scanner.next());
			taStartChoice.setText(scanner.next());
			taEndChoice.setText(scanner.next());
			taStartRow.setText(scanner.next());
			taEndRow.setText(scanner.next());

			scanner.close();
		}
	}
}
