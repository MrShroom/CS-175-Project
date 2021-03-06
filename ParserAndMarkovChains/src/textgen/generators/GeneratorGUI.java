package textgen.generators;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Swing GUI used for convenient testing.
 */
public class GeneratorGUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 473650642759632976L;

	public Generator generator = new GeneratorPOS();

	JButton trainButton;
	JButton generateButton;

	JTextArea reviewBox;
	// JFormattedTextField sampleSizeField;
	JSpinner starSpinner;

	JPanel panel;
	
	
	JTextArea reviewCategoryArea;

	public static void main(String[] args) {
		// GeneratorGUI gui = new GeneratorGUI();
		// gui.setVisible(true);
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new GeneratorGUI().setVisible(true);
			}
		});
	}

	public GeneratorGUI() {
		super("Review Generator");
		// this.setSize(400,400);
		panel = new JPanel(new GridBagLayout());
		// panel.setSize(400,400);
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 4;
		//c.gridheight = 4;
		// c.ipady = 300;
		reviewBox = new JTextArea(1,50);
		reviewBox.setLineWrap(true);
		reviewBox.setSize(500, 600);
		// reviewBox.set
		panel.add(reviewBox, c);

		c.ipady = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 1;
		trainButton = new JButton("Train");
		trainButton.addActionListener(e -> {
			reviewBox.setText("Training");
			generator.verbose = true;
			generator.train();
			reviewBox.setText("Training Complete");
		});
		panel.add(trainButton, c);

		c.gridx = 1;
		c.gridy = 1;
		generateButton = new JButton("Generate");
		generateButton.addActionListener(e -> {
			reviewBox.setText(generator.generateReview());
			System.out.println(reviewBox.getText());
		});
		panel.add(generateButton, c);

		c.gridx = 2;
		c.gridy = 1;
		JLabel starlabel = new JLabel("Stars:");
		panel.add(starlabel, c);

		c.gridx = 3;
		c.gridy = 1;
		SpinnerNumberModel starModel = new SpinnerNumberModel(5, 1, 5, 1);
		starSpinner = new JSpinner(starModel);
		starSpinner.addChangeListener(e -> {
			generator.setStar((int) starSpinner.getValue());
		});
		starlabel.setLabelFor(starSpinner);
		panel.add(starSpinner, c);
		
		
		c.gridx = 2;
		c.gridy = 2;
		JLabel sizeLabel = new JLabel("Training Size:");
		panel.add(sizeLabel,c);
		
		c.gridx = 3;
		SpinnerNumberModel sizeModel = new SpinnerNumberModel(1000,1000,20000,1000);
		JSpinner sizeSpinner = new JSpinner(sizeModel);
		sizeSpinner.addChangeListener(e -> {
			generator.setReviewCount((int)sizeSpinner.getValue());
		});
		sizeLabel.setLabelFor(sizeSpinner);
		panel.add(sizeSpinner,c);
		
		
		c.gridx = 2;
		c.gridy = 3;
		JLabel categoryLabel = new JLabel("Category:");
		panel.add(categoryLabel,c);
		
		c.gridx = 3;
		c.gridy = 3;
		reviewCategoryArea = new JTextArea("Restaurants");
		reviewCategoryArea.getDocument().addDocumentListener(new DocumentListener() {

	        @Override
	        public void removeUpdate(DocumentEvent e) {

	        }

	        @Override
	        public void insertUpdate(DocumentEvent e) {

	        }

	        @Override
	        public void changedUpdate(DocumentEvent arg0) {
	        	generator.category = reviewCategoryArea.getText();
	        }
	        
	    });
		panel.add(reviewCategoryArea,c);


		

		this.add(panel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setSize(500, 400);
		this.setVisible(true);
	}

}
