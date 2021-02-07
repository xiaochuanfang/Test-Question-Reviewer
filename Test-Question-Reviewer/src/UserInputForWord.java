import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class UserInputForWord extends JDialog {

	private JRadioButton rbQuesPrefix;
	private JRadioButton rbChoiPrefix;
	private JRadioButton rbSingleAnsType;
	private JRadioButton rbMultiAnsType;
	private JTextArea taAnswer;
	private JLabel lbError;

	private ReadWord reader=new ReadWord();

	private final Font labelFont=new Font("Courier", Font.BOLD, 25);
	private final Font inputFont=new Font("Courier", Font.PLAIN, 20);
	private final Font radioFont=new Font("Courier", Font.ITALIC, 20);
	private final Font errorFont=new Font("Courier", Font.ITALIC, 20);

	private static final String DEFAULT_SUFFIX="_default.txt";
	private static final String PREVIEW_SUFFIX="_preview.png";
	private static final int TOTAL_ARGUMENTS=5;

	/**
	 * Create the frame.
	 * @throws Exception   when file is not found
	 */
	public UserInputForWord(File file){

		//Content pane
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);

		/* Check if the previous preview image can be use. If not, create
		 * preview image for the Word file
		 */
		if(!isPreviewUsable(file)) {
			try {
				reader.createWordPreview(file);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		//Scroll bar for the preview image
		String imgFilename=StringMaster.getFileName(file)+PREVIEW_SUFFIX;
		ImageIcon icon=new ImageIcon(imgFilename);
		JScrollPane scrollPane = new JScrollPane(new JLabel(icon));
		scrollPane.setBounds(0, 0, 1590, 337);
		contentPane.add(scrollPane);

		//Radio button for question prefix
		rbQuesPrefix = new JRadioButton("1. 2. 3.",true);
		rbQuesPrefix.setBounds(144, 472, 133, 44);
		rbQuesPrefix.setFont(radioFont);
		contentPane.add(rbQuesPrefix);

		//Radio button for choice prefix
		rbChoiPrefix = new JRadioButton("a. b. c.",true);
		rbChoiPrefix.setFont(radioFont);
		rbChoiPrefix.setBounds(499, 472, 133, 44);
		contentPane.add(rbChoiPrefix);

		//Radio button for single answer type question
		rbSingleAnsType = new JRadioButton("  All single answer",true);
		rbSingleAnsType.setFont(radioFont);
		rbSingleAnsType.setBounds(834, 471, 295, 46);
		contentPane.add(rbSingleAnsType);

		//Radio button for multiple answers type question
		rbMultiAnsType = new JRadioButton("<html>&nbsp Some/all contain &nbsp   mutliple answers</html>");
		rbMultiAnsType.setFont(radioFont);
		rbMultiAnsType.setBounds(834, 509, 266, 78);
		contentPane.add(rbMultiAnsType);

		//Button group for question type
		ButtonGroup typeGroup=new ButtonGroup();
		typeGroup.add(rbSingleAnsType);
		typeGroup.add(rbMultiAnsType);

		//Text area for answer prefix
		taAnswer = new JTextArea();
		taAnswer.setFont(inputFont);
		taAnswer.setBounds(1227, 474, 197, 44);
		contentPane.add(taAnswer);

		//Label for "Question Prefix"
		JLabel lbStatement = new JLabel("Question Prefix");
		lbStatement.setHorizontalAlignment(SwingConstants.CENTER);
		lbStatement.setFont(labelFont);
		lbStatement.setBounds(104, 413, 237, 37);
		contentPane.add(lbStatement);

		//Label for "Choices Prefix"
		JLabel lbChoices = new JLabel("Choice Prefix");
		lbChoices.setHorizontalAlignment(SwingConstants.CENTER);
		lbChoices.setFont(labelFont);
		lbChoices.setBounds(478, 413, 197, 37);
		contentPane.add(lbChoices);

		//Label for "Choices Type" 
		JLabel lbType = new JLabel("Choices Type");
		lbType.setHorizontalAlignment(SwingConstants.CENTER);
		lbType.setFont(labelFont);
		lbType.setBounds(834, 409, 197, 44);
		contentPane.add(lbType);

		//Label for "Answer Prefix"
		JLabel lbAnswer = new JLabel("Answer Prefix");
		lbAnswer.setHorizontalAlignment(SwingConstants.CENTER);
		lbAnswer.setFont(labelFont);
		lbAnswer.setBounds(1227, 413, 197, 37);
		contentPane.add(lbAnswer);

		//Label for error message
		lbError = new JLabel("");
		lbError.setHorizontalAlignment(SwingConstants.CENTER);
		lbError.setFont(errorFont);
		lbError.setForeground(Color.RED);
		lbError.setBounds(110, 353, 1327, 37);
		contentPane.add(lbError);

		//Button to start the test
		JButton btnStartTest = new JButton("Start Test");
		btnStartTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				//Reset error message
				lbError.setText("");

				boolean isAllValidInput=true;

				//Get the user inputs
				String inputAns=taAnswer.getText();

				//Check if all inputs are enter
				if(StringMaster.isEmptyString(inputAns)){
					lbError.setText("Enter the answer prefix to continue");
					isAllValidInput=false;
				}

				if(isAllValidInput) {
					ArrayList<Question> qlist=new ArrayList<Question>();

					try {
						//Check if there any extracting error message
						String errorMessage = reader.fillQuestionList(qlist,file,taAnswer.getText());
						if(StringMaster.isEmptyString(errorMessage)) {

							//Set question type
							if(rbSingleAnsType.isSelected()) {
								for(int i=0;i<qlist.size();i++) {
									qlist.get(i).setType("single answer");
								}
							}
							else {
								for(int i=0;i<qlist.size();i++) {
									qlist.get(i).setType("multi answer");
								}
							}

							//Show dialog for user to choose test mode
							TestMode mode=new TestMode(qlist);

							//Save the input values for next time
							saveDefaultValue(file);

							//Close UserInputForExcel GUI
							dispose();
						}
						else {
							lbError.setText(errorMessage);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		btnStartTest.setFont(labelFont);
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
	 * @param file  Word document user chose to run test. This parameter is to use to look for
	 * a text file which record user input from previous program run, if exist. The text file 
	 * has the same name as the Word document except has a extra suffix.
	 * @throws IOException  if file not found
	 */
	private void saveDefaultValue(File file) throws IOException {

		//Create a new text file with the input file name+"_default"
		File defaultFile=new File(StringMaster.getFileName(file)+DEFAULT_SUFFIX);
		defaultFile.createNewFile();

		FileWriter fileWriter=new FileWriter(defaultFile);
		long lastModify=file.lastModified();
		fileWriter.write(Long.toString(lastModify)+"\n");
		fileWriter.write(rbQuesPrefix.isSelected()+"\n");
		fileWriter.write(rbChoiPrefix.isSelected()+"\n");
		fileWriter.write(rbSingleAnsType.isSelected()+"\n");
		fileWriter.write(taAnswer.getText()+"\n");

		fileWriter.close();
	}

	/**
	 * Reload previous saved default value
	 * @param file  Word document user chose to run test. This parameter is to use to look for
	 * a text file which record user input from previous program run, if exist. The text file 
	 * has the same name as the Word document except has a extra suffix.
	 * @throws IOException  if file not found
	 */
	private void loadLastValue(File file) throws IOException {

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
				rbQuesPrefix.setSelected(Boolean.parseBoolean(arguments.get(1)));
				rbChoiPrefix.setSelected(Boolean.parseBoolean(arguments.get(2)));
				rbSingleAnsType.setSelected(Boolean.parseBoolean(arguments.get(3)));
				taAnswer.setText(arguments.get(4));
			}
			else {
				defaultFile.delete();
			}
		}
	}

}
