import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JTable;

public class Test extends JFrame {

	private JPanel contentPane;
	private JScrollPane scrollPane;
	private JTextArea taStatement;
	private JButton btnChoice1;
	private JButton btnChoice2;
	private JButton btnChoice3;
	private JButton btnChoice4;
	private JButton btnChoice5;
	private JButton btnSubmit;
	private JTable table;

	private ArrayList<JButton> blist;
	private ArrayList<Question> qlist;
	private Question[] qArray;
	private int position;

	private ActionListener singleAnsListener;
	private ActionListener multiAnsListener;
	private boolean resetMode;
	private Boolean randomMode;

	private QuestionTableModel questionModel;
	private Random random=new Random();
	
	private final Font font=new Font("Monospaced", Font.PLAIN, 25);
	private final Color selectColor=Color.GREEN;
	private final int MAX_CHOICES=5;

	/**
	 * Create the frame.
	 */
	public Test(ArrayList<Question> q, Boolean randomMode) {

		//Initialized fields
		blist=new ArrayList<JButton>();					//List of all multiple choices buttons in this GUI
		qlist=q;										//List of unanswer Question Object
		qArray=new Question[qlist.size()];				//List of answered Question Object
		
		position=0;										//Current Question position in Question list
		
		resetMode=true;									//Flag to switch between single choice and multiple choices question  mode  
		this.randomMode=randomMode;						//Flag to set test question sequential or random mode

		//Create action listener for the button in single answer mode
		singleAnsListener=new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {

				//Get user's choice
				JButton button=(JButton) event.getSource();
				Set<String> selectAns=new HashSet<String>();
				selectAns.add(button.getName());

				//Record the score after user push the choice button and go to next question
				recordScore(selectAns);
				gotoNextQuestion();
			}	
		};

		//Create action listener for the button in multiple answers mode
		multiAnsListener=new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {

				JButton button=(JButton) event.getSource();

				//Change the button color into green if user select
				if(button.getBackground()!=selectColor) {
					button.setBackground(selectColor);
				}

				//Change the button color into original color if user diselect
				else {
					button.setBackground(null);
				}
			}	
		};

		//Create content pane
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);

		//Create text area for statement
		taStatement = new JTextArea();
		taStatement.setFont(font);
		taStatement.setWrapStyleWord(true);
		taStatement.setLineWrap(true);

		//Create scroll pane for statement
		scrollPane=new JScrollPane(taStatement,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(15, 0, 795, 372);
		contentPane.add(scrollPane);

		//Create list of buttons for multiple choices
		btnChoice1 = new JButton("");
		btnChoice1.setName("A");
		btnChoice1.setHorizontalAlignment(SwingConstants.LEFT);
		btnChoice1.setFont(font);
		btnChoice1.setBounds(15, 401, 795, 81);
		blist.add(btnChoice1);
		contentPane.add(btnChoice1);

		btnChoice2 = new JButton("");
		btnChoice2.setName("B");
		btnChoice2.setHorizontalAlignment(SwingConstants.LEFT);
		btnChoice2.setFont(font);
		btnChoice2.setBounds(15, 498, 795, 81);
		blist.add(btnChoice2);
		contentPane.add(btnChoice2);

		btnChoice3 = new JButton("");
		btnChoice3.setName("C");
		btnChoice3.setHorizontalAlignment(SwingConstants.LEFT);
		btnChoice3.setFont(font);
		btnChoice3.setBounds(15, 595, 795, 81);
		blist.add(btnChoice3);
		contentPane.add(btnChoice3);

		btnChoice4 = new JButton("");
		btnChoice4.setName("D");
		btnChoice4.setHorizontalAlignment(SwingConstants.LEFT);
		btnChoice4.setFont(font);
		btnChoice4.setBounds(15, 692, 795, 81);
		blist.add(btnChoice4);
		contentPane.add(btnChoice4);

		btnChoice5 = new JButton("");
		btnChoice5.setName("E");
		btnChoice5.setHorizontalAlignment(SwingConstants.LEFT);
		btnChoice5.setFont(font);
		btnChoice5.setBounds(15, 787, 795, 81);
		blist.add(btnChoice5);
		contentPane.add(btnChoice5);

		//Create a submit button for multiple answers mode
		btnSubmit = new JButton("Submit");
		btnSubmit.setBounds(825, 401, 184, 81);
		btnSubmit.setFont(font);

		//Add action listener for submit button
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				//Get the list of choices that user select
				Set<String> selectAnswers=new HashSet<String>();
				for(int i=0;i<blist.size();i++) {
					if(blist.get(i).getBackground()==selectColor) {
						selectAnswers.add(blist.get(i).getName());
					}
				}

				//Record the score after user push the choice button and go to next question
				recordScore(selectAnswers);
				gotoNextQuestion();
			}
		});

		contentPane.add(btnSubmit);

		//Create a table to show progress of the test
		table = new JTable();

		//Create a Question table model to set the layout for the table
		questionModel=new QuestionTableModel();
		table.setModel(questionModel);

		//Create a renderer to show question correctness during the test
		QuestionCellRenderer renderer=new QuestionCellRenderer();
		table.setDefaultRenderer(Object.class, renderer);

		//Add Questionlist Object to the Question table model
		addQuestionlistToModel();		

		//Set table attributes
		table.setRowHeight(30);
		table.setFont(font);
		table.setTableHeader(null);
		table.setCellSelectionEnabled(false);
		
		//Create scroll pane for table
		JScrollPane tableScrollPane=new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		tableScrollPane.setBounds(825, 16, 668, 354);
		contentPane.add(tableScrollPane);

		//Set frame operation and position
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1530, 932);
		setContentPane(contentPane);
		setLocationRelativeTo(null);
		setVisible(true);

		//Load text for the text areas and buttons
		loadQuestion();
	}

	public void addQuestionlistToModel() {

		int totalQ=qArray.length;
		int maxListSize=10;
		int totalFullRow=totalQ/maxListSize;
		int remainQ=totalQ%maxListSize;
		int qNumber=0;		

		//Create Question list Object with full size and add to Question table model
		for(int i=0;i<totalFullRow;i++) {
			QuestionList sublist=new QuestionList();
			for(int j=0;j<maxListSize;j++) {
				Question question=qlist.get(qNumber);
				sublist.addQuestion(question);
				qNumber++;
			}
			questionModel.addQuestionList(sublist);
		}

		//Create Question list that adds the remaining Question
		QuestionList sublist=new QuestionList();
		for(int i=0;i<remainQ;i++) {
			Question question=qlist.get(qNumber);
			sublist.addQuestion(question);
			qNumber++;
		}

		//Add empty Question to the last Question list to make it full
		int remainSize=maxListSize-remainQ;
		for(int i=0;i<=remainSize;i++) {
			Question question=new Question();
			sublist.addQuestion(question);
		}

		//Add the last Question list to the Question table model
		questionModel.addQuestionList(sublist);
	}

	public void recordScore(Set<String> selectAns){

		//Find the question with the question ID
		Question question=qlist.get(position);

		//Set the Question object's select answer and correct answer
		Set<String> correctAns=question.getCorrectAns();
		question.setSelectAns(selectAns);
		//question.setCorrectAns(correctAns);

		//Set the Question object's correctness
		if(selectAns.equals(correctAns)) {
			question.setCorrect(true);
		}
		else {
			question.setCorrect(false);
		}

		//Update the table cell color in response to user's answer
		showCellColor();
	}

	public void gotoNextQuestion() {

		/*
		 * If remaining question is not equal to 1, 
		 * check if current and next question types are the same
		 */
		if(position!=qlist.size()-1) {
			Question currentQ=qlist.get(position);
			Question nextQ=qlist.get(position+1);
			if(currentQ.getType()!=nextQ.getType()) {
				resetMode=true;
			}
		}
		
		//Remove the last load question
		Question q=qlist.remove(position);
		qArray[Integer.parseInt(q.getId())-1]=q;

		//If didn't finish last question in question list, load another question
		if(qlist.size()!=0) {
			loadQuestion();
		}
		//Otherwise print the test result
		else {
			//Review result=new Review(qlist);
			Score score=new Score(this,qArray);				
		}
	}

	public void loadQuestion() {

		//Get current question number
		Question question;

		if(randomMode) {
			position=random.nextInt(qlist.size());
			question=qlist.get(position);
		}
		else {
			question=qlist.get(0);
		}
		
		//If current question type is different from last one
		if(resetMode) {

			//Load the corresponding action listener for current question type
			if(question.getType()=="single answer") {
				removeAllActionListener(multiAnsListener);
				setupSingleAnsMode();
			}
			else {
				removeAllActionListener(singleAnsListener);
				setupMultiAnsMode();
			}

			//Change back the flag into false once finish set up the right question mode
			resetMode=false;
		}

		//Load the text for the statement
		taStatement.setText(question.getStatement());

		//Load the buttons and texts for the multiple choices
		int NumOfChoice=question.getChoices().size();
		int count=0;
		JButton button;
		while(count!=NumOfChoice) {
			button=blist.get(count);
			button.setVisible(true);
			button.setText("<html>"+question.getChoices().get(count));
			button.setBackground(null);
			count++;
		}

		//Set the remaining buttons that has no choices to be invisible
		for(int i=count;i<MAX_CHOICES;i++) {
			blist.get(i).setVisible(false);
		}
	}

	//Change action listeners for all the buttons for single answer mode
	public void setupSingleAnsMode() {
		btnChoice1.addActionListener(singleAnsListener);
		btnChoice2.addActionListener(singleAnsListener);
		btnChoice3.addActionListener(singleAnsListener);
		btnChoice4.addActionListener(singleAnsListener);
		btnChoice5.addActionListener(singleAnsListener);
		btnSubmit.setVisible(false);
	}

	//Change action listeners for all the buttons for multiple answers mode
	public void setupMultiAnsMode() {
		btnChoice1.addActionListener(multiAnsListener);
		btnChoice2.addActionListener(multiAnsListener);
		btnChoice3.addActionListener(multiAnsListener);
		btnChoice4.addActionListener(multiAnsListener);
		btnChoice5.addActionListener(multiAnsListener);
		btnSubmit.setVisible(true);
	}

	//Remove all the action listeners for the buttons for choices
	public void removeAllActionListener(ActionListener listener) {
		btnChoice1.removeActionListener(listener);
		btnChoice2.removeActionListener(listener);
		btnChoice3.removeActionListener(listener);
		btnChoice4.removeActionListener(listener);
		btnChoice5.removeActionListener(listener);
	}

	//Fix the issue that renderer is not showing color unless deselect the row  
	public void showCellColor() {
		table.selectAll();
		table.clearSelection();
	}
}
