package textgen;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

public class GeneratorGUI extends JFrame {
	public Generator generator = new Generator();
	
	JButton trainButton;
	JButton generateButton;
	
	JTextField reviewBox;
	//JFormattedTextField sampleSizeField;
	JSpinner starSpinner;
	
	JPanel panel;
	
	public static void main(String [] args){
		GeneratorGUI gui = new GeneratorGUI();
		gui.setVisible(true);
	}
	
	public GeneratorGUI(){
		super("Review Generator");
		panel = new JPanel();
		//panel.setSize(400,400);
		GroupLayout gl = new GroupLayout(panel);
		panel.setLayout(gl);
		gl.setAutoCreateGaps(true);
		gl.setAutoCreateContainerGaps(true);
		
		
		this.add(panel);
		
		trainButton = new JButton("Train");
		//panel.add(trainButton);
		
		generateButton = new JButton("Generate");
		//panel.add(generateButton);
		
		JLabel starlabel = new JLabel("Stars:");
		//panel.add(starlabel);
		
		SpinnerNumberModel starModel = new SpinnerNumberModel(5,1,5,1);
		starSpinner = new JSpinner(starModel);
		starlabel.setLabelFor(starSpinner);
		//panel.add(starSpinner);
		
		//gl.setHorizontalGroup(gl.createSequentialGroup().addComponent(trainButton).addComponent(generateButton).addGroup(group));
		
		
		
		
		//SpringUtilities.makeCompactGrid(panel,4,4);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}
	
	
	
}
