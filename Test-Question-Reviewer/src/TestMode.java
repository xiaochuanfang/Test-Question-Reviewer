import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class TestMode extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private final Font font=new Font("Monospaced", Font.BOLD, 23);

	/**
	 * Create the dialog.
	 */
	public TestMode(ArrayList<Question> qlist) {

		contentPanel.setLayout(null);
		
		//Create button for sequential question mode
		JButton btnSequential = new JButton("Sequential Mode");
		btnSequential.setBounds(29, 16, 251, 49);
		btnSequential.setFont(font);
		contentPanel.add(btnSequential);
		
		//Add action listener for the "Sequential Mode" button
		btnSequential.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Test test=new Test(qlist,false);
				dispose();
			}
		});
		
		//Create button for random question mode
		JButton btnRandom = new JButton("Random Mode");
		btnRandom.setBounds(309, 16, 251, 49);
		btnRandom.setFont(font);
		contentPanel.add(btnRandom);
		
		//Add action listener for the "Random Mode" button
				btnRandom.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Test test=new Test(qlist,true);
						dispose();
					}
				});

		//Set dialog operation and position
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		setBounds(100, 100, 614, 152);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
