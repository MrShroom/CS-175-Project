package textgen;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

public class GeneratorGUI extends JFrame {
	public Generator generator = new Generator();
	
	JButton trainButton;
	JButton generateButton;
	
	JTextArea reviewBox;
	//JFormattedTextField sampleSizeField;
	JSpinner starSpinner;
	
	JPanel panel;
	
	public static void main(String [] args){
		GeneratorGUI gui = new GeneratorGUI();
		gui.setVisible(true);
	}
	
	public GeneratorGUI(){
		super("Review Generator");
		//this.setSize(400,400);
		panel = new JPanel(new GridBagLayout());
		//panel.setSize(400,400);
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.HORIZONTAL;
	
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth=4;
		//c.ipady = 300;
		reviewBox = new JTextArea("Wow dude that's crazy");
		reviewBox.setLineWrap(true);
		reviewBox.setSize(300,300);
		//reviewBox.set
		panel.add(reviewBox,c);
		
		c.ipady = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		trainButton = new JButton("Train");
		trainButton.addActionListener(e->{
			reviewBox.setText("Training");
			generator.verbose = true;
			generator.train();
			reviewBox.setText("Training Complete");
		});
		panel.add(trainButton,c);
		
		c.gridx = 1;
		c.gridy = 1;
		generateButton = new JButton("Generate");
		generateButton.addActionListener(e->{
			reviewBox.setText(generator.generateReview());
		});
		panel.add(generateButton,c);
		
		c.gridx = 2;
		c.gridy = 1;
		JLabel starlabel = new JLabel("Stars:");
		panel.add(starlabel,c);
		
		
		c.gridx = 3;
		c.gridy = 1;
		SpinnerNumberModel starModel = new SpinnerNumberModel(5,1,5,1);
		starSpinner = new JSpinner(starModel);
		starSpinner.addChangeListener(e->{
			generator.setStar((int)starSpinner.getValue());
		});
		starlabel.setLabelFor(starSpinner);
		panel.add(starSpinner,c);
		
		
		this.add(panel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}
	
	
	
}
