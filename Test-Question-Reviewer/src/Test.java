import javax.swing.JFrame;
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
import java.util.Set;

import javax.swing.JButton;
import javax.swing.SwingConstants;

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

	private ArrayList<JButton> blist;
	private ArrayList<Question> qlist;
	private ArrayList<Score> slist;
	private int qNumber;

	private ActionListener singleAnsListener;
	private ActionListener multiAnsListener;
	private boolean resetMode;

	private final Font font=new Font("Monospaced", Font.PLAIN, 25);
	private final Color selectColor=Color.GREEN;
	private final int MAX_CHOICES=5;

	/**
	 * Create the frame.
	 */
	public Test(ArrayList<Question> qlist) {

		//Initialized question number,button list, score list, question list and resetMode
		qNumber=0;
		blist=new ArrayList<JButton>();
		slist=new ArrayList<Score>();
		this.qlist=qlist;
		resetMode=true;

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

		//Set frame operation and position
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1530, 932);
		setContentPane(contentPane);
		setVisible(true);

		//Load text for the text areas and buttons
		loadQuestion();
	}

	public ArrayList<Score> recordScore(Set<String> selectAns){

		//Create the score
		Set<String> correctAns=qlist.get(qNumber).getAnswer();
		Score score=new Score(qNumber,selectAns,correctAns);
		
		//Set the score to be true or false
		if(checkAnswer(selectAns)) {
			score.setCorrect(true);
		}
		else {
			score.setCorrect(false);
		}
		slist.add(score);	//Add to score list
		return slist;
	}

	public boolean checkAnswer(Set<String> selectAnswer) {

		//Check if user had chose the correct answer
		Set<String> correctAnswer=qlist.get(qNumber).getAnswer();
		return selectAnswer.equals(correctAnswer);
	}

	public void gotoNextQuestion() {
		
		qNumber++;			//Increment the question number

		//If didn't finish last question in question list, load another question
		if(qNumber<qlist.size()) {
			loadQuestion();
		}
		//Otherwise print the test result
		else {
			Result result=new Result(slist);
			dispose();				
		}
	}
	
	public void loadQuestion() {

		//Get current question number
		Question question=qlist.get(qNumber);

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
			button.setText(question.getChoices().get(count));
			button.setBackground(null);
			count++;
		}

		//Set the remaining buttons that has no choices to be invisible
		for(int i=count;i<MAX_CHOICES;i++) {
			blist.get(i).setVisible(false);
		}

		//Check if current and next question types are the same
		if(qNumber!=qlist.size()-1) {
			Question nextQ=qlist.get(qNumber+1);
			if(question.getType()!=nextQ.getType()) {
				resetMode=true;
			}
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

	//Remove all the actionlisteners for the buttons for choices
	public void removeAllActionListener(ActionListener listener) {
		btnChoice1.removeActionListener(listener);
		btnChoice2.removeActionListener(listener);
		btnChoice3.removeActionListener(listener);
		btnChoice4.removeActionListener(listener);
		btnChoice5.removeActionListener(listener);
	}
}
