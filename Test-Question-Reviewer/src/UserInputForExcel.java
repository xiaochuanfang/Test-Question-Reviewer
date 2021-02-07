import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.JTextArea;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class UserInputForExcel extends JDialog {

	private JTextArea taStatement;
	private JTextArea taStartChoice;
	private JTextArea taEndChoice;
	private JTextArea taAnswer;
	private JTextArea taType;
	private JTextArea taStartRow;
	private JTextArea taEndRow;
	private JLabel lbErrorEmpty;
	private JLabel lbErrorColumn;
	private JLabel lbErrorRow;

	private ReadExcel reader=new ReadExcel();
	private InputChecker check=new InputChecker();

	private Font font=new Font("Monospaced", Font.BOLD, 25);
	private Font errorFont=new Font("Monospaced",Font.ITALIC,20);

	private final String DEFAULT_SUFFIX="_default.txt";
	private final String PREVIEW_SUFFIX="_preview.png";
	private static final int TOTAL_ARGUMENTS=8;
	private static final int MAX_ALPHA_INPUT_LENGTH=2;
	private static final int MAX_NUM_INPUT_LENGTH=4;

	/**
	 * Create the frame.
	 * @throws Exception 
	 */
	public UserInputForExcel(File file){

		//Create content pane
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);

		/* Check if the previous preview image can be use. If not, create
		 * preview image for the Word file
		 */
		if(!isPreviewUsable(file)) {
			try {
				reader.createExcelPreview(file,0);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
		//Create scroll bar for the preview image
		String imgFilename=StringMaster.getFileName(file)+PREVIEW_SUFFIX;
		ImageIcon icon=new ImageIcon(imgFilename);
		JScrollPane scrollPane = new JScrollPane(new JLabel(icon));
		scrollPane.setBounds(0, 0, 1590, 337);
		contentPane.add(scrollPane);

		//Create key restriction for alphabet only input
		KeyAdapter alphabetAdapter=new KeyAdapter() {
			public void keyTyped(KeyEvent arg0) {
				char c=arg0.getKeyChar();
				if(!(Character.isAlphabetic(c)) && c!=KeyEvent.VK_BACK_SPACE
						&& c!=KeyEvent.VK_DELETE) {
					getToolkit().beep();
					arg0.consume();
				}
			}
		};

		//Create key restriction for number only input
		KeyAdapter numAdapter=new KeyAdapter() {
			public void keyTyped(KeyEvent arg0) {
				char c=arg0.getKeyChar();
				if(!(Character.isDigit(c)) && c!=KeyEvent.VK_BACK_SPACE
						&& c!=KeyEvent.VK_DELETE) {
					getToolkit().beep();
					arg0.consume();
				}
			}
		};

		//Create text area for Question Type column input
		taType = new JTextArea();
		taType.setFont(font);
		taType.setBounds(885, 537, 59, 46);
		taType.addKeyListener(alphabetAdapter);
		contentPane.add(taType);

		//Create text area for question column input
		taStatement = new JTextArea();
		taStatement.setFont(font);
		taStatement.setBounds(116, 537, 59, 44);
		taStatement.addKeyListener(alphabetAdapter);
		contentPane.add(taStatement);

		//Create text area for answer column input
		taAnswer = new JTextArea();
		taAnswer.setFont(font);
		taAnswer.setBounds(625, 537, 54, 44);
		taAnswer.addKeyListener(alphabetAdapter);
		contentPane.add(taAnswer);

		//Create text area for choice start column input
		taStartChoice = new JTextArea();
		taStartChoice.setFont(font);
		taStartChoice.setBounds(335, 537, 44, 44);
		taStartChoice.addKeyListener(alphabetAdapter);
		contentPane.add(taStartChoice);

		//Create text area for choice end column input
		taEndChoice = new JTextArea();
		taEndChoice.setFont(font);
		taEndChoice.setBounds(408, 537, 44, 44);
		taEndChoice.addKeyListener(alphabetAdapter);
		contentPane.add(taEndChoice);

		//Create text area for start row input 
		taStartRow = new JTextArea();
		taStartRow.setFont(font);
		taStartRow.setBounds(1150, 537, 81, 44);
		taStartRow.addKeyListener(numAdapter);
		contentPane.add(taStartRow);

		//Create Text area for end row input
		taEndRow = new JTextArea();
		taEndRow.setFont(font);
		taEndRow.setBounds(1402, 537, 81, 44);
		taEndRow.addKeyListener(numAdapter);
		contentPane.add(taEndRow);

		//Convert all input in Text Area for type, question, answer, start choice 
		//and end choice to Upper Case and set limit input length
		DocumentFilter alphaFilter=new AlphaInputDocumentFilter();
		AbstractDocument typeDoc=(AbstractDocument) taType.getDocument();
		typeDoc.setDocumentFilter(alphaFilter);
		AbstractDocument quesDoc=(AbstractDocument) taStatement.getDocument();
		quesDoc.setDocumentFilter(alphaFilter);
		AbstractDocument ansDoc=(AbstractDocument) taAnswer.getDocument();
		ansDoc.setDocumentFilter(alphaFilter);
		AbstractDocument startChoiDoc=(AbstractDocument) taStartChoice.getDocument();
		startChoiDoc.setDocumentFilter(alphaFilter);
		AbstractDocument endChoiDoc=(AbstractDocument) taEndChoice.getDocument();
		endChoiDoc.setDocumentFilter(alphaFilter);

		//Set limit length input for start row and end row text area
		DocumentFilter numFIlter=new NumInputDocumentFilter();
		AbstractDocument startRowDoc=(AbstractDocument) taStartRow.getDocument();
		startRowDoc.setDocumentFilter(numFIlter);
		AbstractDocument endRowDoc=(AbstractDocument) taEndRow.getDocument();
		endRowDoc.setDocumentFilter(numFIlter);

		//Label for "Type" 
		JLabel lbType = new JLabel("Type");
		lbType.setHorizontalAlignment(SwingConstants.CENTER);
		lbType.setFont(font);
		lbType.setBounds(865, 460, 93, 44);
		contentPane.add(lbType);

		//Label for "Question"
		JLabel lbStatement = new JLabel("Question");
		lbStatement.setHorizontalAlignment(SwingConstants.CENTER);
		lbStatement.setFont(font);
		lbStatement.setBounds(73, 464, 146, 37);
		contentPane.add(lbStatement);

		//Label for "Answer"
		JLabel lbAnswer = new JLabel("Answer");
		lbAnswer.setHorizontalAlignment(SwingConstants.CENTER);
		lbAnswer.setFont(font);
		lbAnswer.setBounds(600, 464, 117, 37);
		contentPane.add(lbAnswer);

		//Label for "Choices"
		JLabel lbChoices = new JLabel("Choices");
		lbChoices.setHorizontalAlignment(SwingConstants.CENTER);
		lbChoices.setFont(font);
		lbChoices.setBounds(335, 464, 117, 37);
		contentPane.add(lbChoices);

		//Label for "-"
		JLabel dash = new JLabel("-");
		dash.setHorizontalAlignment(SwingConstants.CENTER);
		dash.setFont(font);
		dash.setBounds(358, 546, 69, 20);
		contentPane.add(dash);

		//Label for "Starts At Row"
		JLabel lbStartAt = new JLabel("Start Row");
		lbStartAt.setFont(font);
		lbStartAt.setBounds(1125, 464, 146, 37);
		contentPane.add(lbStartAt);

		//Label for "End At Row"
		JLabel lbEndAt = new JLabel("End Row");
		lbEndAt.setFont(font);
		lbEndAt.setBounds(1388, 459, 117, 46);
		contentPane.add(lbEndAt);

		//Label for error message when user leave black input
		lbErrorEmpty = new JLabel("");
		lbErrorEmpty.setHorizontalAlignment(SwingConstants.CENTER);
		lbErrorEmpty.setFont(errorFont);
		lbErrorEmpty.setForeground(Color.RED);
		lbErrorEmpty.setBounds(116, 391, 1327, 37);
		contentPane.add(lbErrorEmpty);

		//Label for error message when user enter wrong choices range
		lbErrorColumn = new JLabel("");
		lbErrorColumn.setFont(errorFont);
		lbErrorColumn.setForeground(Color.RED);
		lbErrorColumn.setBounds(337, 597, 320, 37);
		contentPane.add(lbErrorColumn);

		//Label for error message when user enter wrong question range
		lbErrorRow = new JLabel("");
		lbErrorRow.setFont(errorFont);
		lbErrorRow.setForeground(Color.RED);
		lbErrorRow.setBounds(1160, 597, 319, 37);
		contentPane.add(lbErrorRow);

		//Button to start the test
		JButton btnStartTest = new JButton("Start Test");
		btnStartTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				//Reset error message
				lbErrorEmpty.setText("");
				lbErrorColumn.setText("");
				lbErrorRow.setText("");

				boolean isAllValidInput=true;

				//Get the user inputs
				String inputType=taType.getText();
				String inputQues=taStatement.getText();
				String inputAns=taAnswer.getText();
				String inputChoi=taStartChoice.getText();
				String inputChoi2=taEndChoice.getText();
				String inputStartAt=taStartRow.getText();
				String inputEndAt=taEndRow.getText();

				//Check if all inputs are enter
				if(StringMaster.isEmptyString(inputType) || StringMaster.isEmptyString(inputQues) || StringMaster.isEmptyString(inputAns)
						|| StringMaster.isEmptyString(inputChoi) || StringMaster.isEmptyString(inputChoi2)
						|| StringMaster.isEmptyString(inputStartAt) ||StringMaster.isEmptyString(inputEndAt)){
					lbErrorEmpty.setText("Enter all fields to continue");
					isAllValidInput=false;
				}

				//Check if input choices start and end column is valid range
				else if(!check.isValidRange(inputChoi, inputChoi2, true)) {
					lbErrorColumn.setText("Invalid choices range");
					isAllValidInput=false;
				}

				//Check if input choices start and end row is valid range
				else if(!check.isValidRange(inputStartAt, inputEndAt, false)) {
					lbErrorRow.setText("Invalid question row range");
					isAllValidInput=false;
				}

				if(isAllValidInput) {

					//Create the Question ArrayList
					ArrayList<Question> qlist=new ArrayList<Question>();

					try {
						//Check if there any reading error message
						String message = reader.fillQuestionList(qlist,file,0,inputType,inputQues,inputAns,inputChoi,inputChoi2,inputStartAt,inputEndAt);
						if(StringMaster.isEmptyString(message)) {

							//Show dialog for user to choose test mode
							TestMode mode=new TestMode(qlist);

							//Save the input values for next time
							saveDefaultValue(file);

							//Close UserInputForExcel GUI
							dispose();
						}
						else {
							lbErrorEmpty.setText(message);
						}

					} catch (Exception e) {
						System.out.println("e toString is "+e.toString());
						System.out.println("e getmessage is "+e.getMessage());
					}

				}
			}
		});

		//Set the "Start test" button attribute
		btnStartTest.setFont(font);
		btnStartTest.setBounds(689, 626, 197, 44);
		contentPane.add(btnStartTest);

		//Load last used value
		try {
			loadLastValue(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		//Set frame operation and position
		setBounds(100, 100, 1615, 769);
		setContentPane(contentPane);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Check whether the last generated preview image for the Word document can reuse again if
	 * the Word document ever change. Check by compare the Word document last-modified date when
	 * generating the image and current last-modified date.  
	 * @param file  Word document user chose to run test. This parameter is to use to look for
	 * a text file which record user input from previous program run and a image file to preview
	 * the document, if they exist. The two files have the same name as the Word document except
	 * with different suffixes.
	 * @return true if two last-modified dates are the same. False otherwise.
	 */
	private boolean isPreviewUsable(File file) {

		String filename=StringMaster.getFileName(file);
		File defaultFile=new File(filename+DEFAULT_SUFFIX);
		File imgFile=new File(filename+PREVIEW_SUFFIX);

		/*If the preview image and the file to record the last-modified date exist,
		 *  compare the recorded last-modified date and current last-modified date. 
		 */
		if(defaultFile.exists() && imgFile.exists()) {
			String previousLastModified="";
			Scanner scanner=null;
			try {
				FileReader fileReader=new FileReader(defaultFile);
				scanner=new Scanner(fileReader);
				if(scanner.hasNext()) {
					previousLastModified=scanner.next();
				}

				//If the file record the last-modified date exist but doesn't has any record
				else {
					return false;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				scanner.close();
			}

			String lastModified=Long.toString(file.lastModified());
			if(lastModified.equals(previousLastModified)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Save user inputs as default value to be use for next program run
	 * @param file  Excel document user chose to run test. This parameter is to use to look for
	 * a text file which record user input from previous program run, if exist. The text file 
	 * has the same name as the Word document except has a extra suffix.
	 * @throws IOException  if file not found
	 */
	public void saveDefaultValue(File file) throws IOException {

		//Create a new text file with the input file name+"_default"
		File defaultFile=new File(StringMaster.getFileName(file)+DEFAULT_SUFFIX);
		defaultFile.createNewFile();


		FileWriter fileWriter=new FileWriter(defaultFile);
		long lastModify=file.lastModified();
		fileWriter.write(Long.toString(lastModify)+"\n");
		fileWriter.write(taType.getText()+"\n");
		fileWriter.write(taStatement.getText()+"\n");
		fileWriter.write(taAnswer.getText()+"\n");
		fileWriter.write(taStartChoice.getText()+"\n");
		fileWriter.write(taEndChoice.getText()+"\n");
		fileWriter.write(taStartRow.getText()+"\n");
		fileWriter.write(taEndRow.getText());

		fileWriter.close();
	}

	/**
	 * Reload previous saved default value
	 * @param file  Excel document user chose to run test. This parameter is to use to look for
	 * a text file which record user input from previous program run, if exist. The text file 
	 * has the same name as the Word document except has a extra suffix.
	 * @throws IOException  if file not found
	 */
	public void loadLastValue(File file) throws IOException {

		//Create a file that has the same name with an extra suffix
		File defaultFile=new File(StringMaster.getFileName(file)+DEFAULT_SUFFIX);

		//If the file exist before, extract previous user's setting from last program run 
		if(defaultFile.exists()) {
			FileReader fileReader=new FileReader(defaultFile);
			Scanner scanner=new Scanner(fileReader);

			ArrayList<String> arguments=new ArrayList<String>();
			while(scanner.hasNextLine()) {
				arguments.add(scanner.nextLine());
			}
			scanner.close();

			/* Check to see if arguments number match. If not match, then data had corrupt,
			 * delete the file
			 */
			if(arguments.size()==TOTAL_ARGUMENTS) {

				//Skip the first line which is the Excel file's last-modified date
				taType.setText(arguments.get(1));
				taStatement.setText(arguments.get(2));
				taAnswer.setText(arguments.get(3));
				taStartChoice.setText(arguments.get(4));
				taEndChoice.setText(arguments.get(5));
				taStartRow.setText(arguments.get(6));
				taEndRow.setText(arguments.get(7));
			}
			else {
				defaultFile.delete();
			}
		}
	}

	//Create document filter to filter all alphabet to uppercase with limit input length
	static class AlphaInputDocumentFilter extends DocumentFilter {

		/*
		 * (non-Javadoc)
		 * @see javax.swing.text.DocumentFilter#replace(javax.swing.text.DocumentFilter.FilterBypass, int, int, java.lang.String, javax.swing.text.AttributeSet)
		 * currentLength is the length before typing an input
		 * offset is the current location when typing an input
		 * text is the input string
		 */
		public void replace(DocumentFilter.FilterBypass fb,int offset,
				int length, String text, AttributeSet attrs)
						throws BadLocationException {
			int currentLength=fb.getDocument().getLength();
			int overLimit=(currentLength+text.length())-MAX_ALPHA_INPUT_LENGTH;

			if(overLimit>0) {
				text=text.substring(0,text.length()-overLimit);
			}
			if(text.length()>0) {
				fb.replace(offset, length, text.toUpperCase(), attrs);
			}
		}

	}


	//Create document filter to filter all alphabet to uppercase with limit input length
	static class NumInputDocumentFilter extends DocumentFilter {

		public void replace(DocumentFilter.FilterBypass fb,int offset,
				int length, String text, AttributeSet attrs)
						throws BadLocationException {
			int currentLength=fb.getDocument().getLength();
			int overLimit=(currentLength+text.length())-MAX_NUM_INPUT_LENGTH;

			if(overLimit>0) {
				text=text.substring(0,text.length()-overLimit);
			}
			if(text.length()>0) {
				fb.replace(offset, length, text, attrs);
			}
		}

	}
}
