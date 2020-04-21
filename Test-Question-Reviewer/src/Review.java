import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.JTextPane;

import java.awt.Color;
import java.awt.Font;

public class Review extends JFrame {

	private JTextPane textpane;
	private Number number=new Number();
	private Color corColor=new Color(44, 148, 72);
	private Color selColor=new Color(217, 29, 4);

	/**
	 * Create the frame.
	 */
	public Review(ArrayList<Question> qlist, Boolean reviewMistakeOnly) {

		//Create content pane
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		
		//Create text pane to show the questions for review
		textpane=new JTextPane();
		textpane.setFont(new Font("Monospaced", Font.PLAIN, 25));
		
		//Set the text for the text pane
		reviewQuestion(qlist,reviewMistakeOnly);

		//Create scroll pane for the text pane
		JScrollPane scrollPane=new JScrollPane(textpane,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(15, 0, 1273, 742);
		contentPane.add(scrollPane);

		//Set frame operation and position
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1354, 862);
		setContentPane(contentPane);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void reviewQuestion(ArrayList<Question> qlist, Boolean reviewMistakeOnly) {

		int totalQ=qlist.size();

		//Review mistaken questions if user choose "Review Mistakes"
		if(reviewMistakeOnly) {
			for(int i=0;i<totalQ;i++) {
				Question question=qlist.get(i);
				if(!(question.isCorrect())) {
					loadQuestionWithStyle(question);
				}
			}
		}
		
		//Review all questions if user choose "Review All"
		else {
			for(int i=0;i<totalQ;i++) {
				Question question=qlist.get(i);
				loadQuestionWithStyle(question);
			}
		}
		
		//Set the scroll pane to the top 
		textpane.setCaretPosition(0);
	}

	public void loadQuestionWithStyle(Question question) {
		
		//Set different styles for question, choices, correct answer and select answer 
		SimpleAttributeSet quesAttribute=new SimpleAttributeSet();
		StyleConstants.setBold(quesAttribute, true);

		SimpleAttributeSet choiAttribute=new SimpleAttributeSet();

		SimpleAttributeSet corAttribute=new SimpleAttributeSet();
		StyleConstants.setBold(corAttribute, true);
		StyleConstants.setForeground(corAttribute, corColor);
		

		SimpleAttributeSet selAttribute=new SimpleAttributeSet();
		StyleConstants.setBold(selAttribute, true);
		StyleConstants.setForeground(selAttribute, selColor);

		//Add the question to the text pane
		String q=question.getId()+": "+question.getStatement()+"\n";
		append(q,quesAttribute);

		//Add the choices to the text pane
		String c="";
		int totalChoice=question.getChoices().size();
		ArrayList<String> choices=question.getChoices();
		for(int k=0;k<totalChoice;k++) {
			c=c+number.intToAlpha(k, 0)+". "+choices.get(k)+"\n";
		}
		append(c, choiAttribute);

		//Add the correct answer to the text pane
		String a="Answer is "+question.getCorrectAns()+"\n";
		append(a,corAttribute);

		//Add the select answer to the text pane
		String w="You selected "+question.getSelectAns()+"\n"+"\n";
		if(question.isCorrect()) {
			append(w,corAttribute);
		}
		else {
			append(w,selAttribute);
		}
	}
	
	//Add the text with text style to the text pane
	public void append(String s,AttributeSet attribute) {
		Document d=textpane.getDocument();
		try {
			d.insertString(d.getLength(), s, attribute);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
}
