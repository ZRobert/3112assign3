package spreed;
/**
 * @author Robert Payne
 * class ITCS 3112-001
 * date 3/30/2014
 */
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.JSlider;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JSeparator;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SpreedApp{
	/*
	 * Attributes
	 */
	private JFrame frmSpreedApp;			//JFrame that contains the application
	private static WordSource words;		//Gets the words and puts them into an ArrayList
											//also used to retrieve the current word
	private static JSlider positionSlider;	//GUI Position slider that seeks the word list
	private static JLabel wordLabel = new JLabel(" ");	//Label for the current word
	private JButton restartButton;			//Button that restarts the spreeder app
	private static JComboBox wpmComboBox;	//Combobox that user picks the words per minute from
	private JLabel wpmLabel;				//Simple label that displays "WPM:"
	private JToggleButton playPauseToggleButton;	//Play/Pause toggle button
	private static int currentWordIndex;	//Index of the current word being displayed
	private static AtomicBoolean isPlaying;	//Used to control the program in paused/play state
	private static double aDelta;			//represents the change in time per iteration in milliseconds
	private static double interval;			//represents the amount of time each word should be displayed
	private static double currentTime;		//Used to compute aDelta
	private static double previousTime;		//Used to compute aDelta
	private static int wordSourceSize;		//The max size of the word list; used to calculate the seek
											//and stop the loop from going out of bounds
	private static int currentWordPause;	//The duration based on the number of words contained in the
											//string
	private static String currentWord;		//The string of the current word
	
	/**
	 * Displays the spreed app window and loops over the words
	 * @param	args Requires a filename argument to run
	 */	
	public static void main(String[] args) {
		try{
			args[0].toString();
			
		}catch(ArrayIndexOutOfBoundsException e){
			JOptionPane.showMessageDialog(null, "Input filename argument missing!",
					"FILENAME ERROR",JOptionPane.ERROR_MESSAGE);
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SpreedApp window = new SpreedApp();
					window.frmSpreedApp.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
		/*=================
		 * ATTRIBUTE SETUP
		 ==================*/
		interval = 1/ 250.0; 						//1/ WPM
		interval = interval * 1000000.0 * 60;		//convert WPM into the nanosecond interval
		isPlaying = new AtomicBoolean();
		isPlaying.set(false);
		currentTime = System.nanoTime();
		aDelta = 0;
		currentWordIndex = 0;
		
		try {
			words = new WordSource(args[0]);
			wordSourceSize = words.getNumberOfWords();
			if(wordSourceSize == 0){
				JOptionPane.showMessageDialog(null, "Empty input file! Please check your filename!",
						"FILENAME ERROR",JOptionPane.ERROR_MESSAGE);
				return;
			}
			updateWord();

			while(true){
				previousTime = currentTime;
				currentTime = System.nanoTime();
				aDelta += (currentTime - previousTime)/1000;
				
				if(aDelta > interval * currentWordPause 
						&& !positionSlider.getValueIsAdjusting() 
						&& isPlaying.get()){
					if(currentWordIndex < wordSourceSize){
						updateWord();
						positionSlider.setValue((int)(((double)currentWordIndex 
								/ (double)wordSourceSize)
								*positionSlider.getMaximum()));
						currentWordIndex++;
						positionSlider.repaint();						
						aDelta = 0;
					}
				}	
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
	}


	/**
	 * Calls the initialize method which setups up the parameters for the GUI
	 */	
	public SpreedApp() {
		initialize();
	}

	/**
	 * Sets up the parameters of the GUI
	 */
	private void initialize() {
		//JFrame
		frmSpreedApp = new JFrame();
		frmSpreedApp.setBackground(Color.ORANGE);
		frmSpreedApp.setForeground(Color.ORANGE);
		frmSpreedApp.setTitle("Spreed App");
		frmSpreedApp.setResizable(false);
		frmSpreedApp.setBounds(100, 100, 450, 300);
		frmSpreedApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSpreedApp.getContentPane().setLayout(null);
		//JLabel for the current word
		wordLabel.setBackground(Color.WHITE);
		wordLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 32));
		wordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		wordLabel.setBounds(6, 6, 438, 196);
		frmSpreedApp.getContentPane().add(wordLabel);
		//JButton for reseting the spreed app to the first word
		restartButton = new JButton("Restart");
		restartButton.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		restartButton.setBounds(6, 225, 52, 47);
		frmSpreedApp.getContentPane().add(restartButton);
		//JToggleButton used to change state from play to paused and vice versa
		playPauseToggleButton = new JToggleButton("Play");
		playPauseToggleButton.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		playPauseToggleButton.setBounds(58, 225, 52, 47);
		frmSpreedApp.getContentPane().add(playPauseToggleButton);
		//JSlider which acts as a seek bar that allows the user to seek the text
		positionSlider = new JSlider();
		positionSlider.setMaximum(1000000);
		positionSlider.setBounds(109, 235, 223, 29);
		frmSpreedApp.getContentPane().add(positionSlider);
		//JComboBox which allows the user to select from a drop down menu the words per minute
		wpmComboBox = new JComboBox();
		wpmComboBox.setBounds(356, 234, 88, 27);	//250, 350, 500, 650, 800, 1000
		wpmComboBox.addItem(new Integer(250));
		wpmComboBox.addItem(new Integer(350));
		wpmComboBox.addItem(new Integer(500));
		wpmComboBox.addItem(new Integer(650));
		wpmComboBox.addItem(new Integer(800));
		wpmComboBox.addItem(new Integer(1000));
		frmSpreedApp.getContentPane().add(wpmComboBox);
		//JLabel denoting the combobox as words per minute
		wpmLabel = new JLabel("WPM:");
		wpmLabel.setBounds(324, 238, 35, 16);
		frmSpreedApp.getContentPane().add(wpmLabel);
		//JSeparator used for appearances
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.BLUE);
		separator.setBounds(6, 214, 438, 12);
		frmSpreedApp.getContentPane().add(separator);

		/*===============================================
		 * ACTION/CHANGE LISTENERS WITH ANONYMOUS CLASSES
		 ================================================*/
		restartButton.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				currentWordIndex = 0;
				updateWord();
				positionSlider.setValue(0);
				positionSlider.repaint();
			}
		});
		
		wpmComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				double wpm = Double.parseDouble(wpmComboBox.getSelectedItem().toString());
				interval = 1/ wpm; //1/ WPM
				interval = interval * 1000000.0 * 60;
			}
		});
		
		playPauseToggleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(isPlaying.get()){
					isPlaying.set(false);
					playPauseToggleButton.setText("Play");
				}else{
					isPlaying.set(true);
					playPauseToggleButton.setText("Pause");
				}
			}
		});
		
		positionSlider.addChangeListener(new ChangeListener() {		
			@Override
			public void stateChanged(ChangeEvent e) {
				if(positionSlider.getValueIsAdjusting()){
					currentWordIndex = (int)((((double)positionSlider.getValue()) 
							/ ((double)positionSlider.getMaximum())) 
							* (double)wordSourceSize) - 1;
					positionSlider.repaint();
					updateWord();
				}
			}
		});
	}
	/*
	 * Retrieves the next word and updates wordLabel
	 */
	private static void updateWord(){
		int currentWordPivot;
		currentWord = words.getWord(currentWordIndex);
		currentWordPivot = SpreedWord.getPivot(currentWord);
		currentWordPause = SpreedWord.getPauseLength(currentWord);
		currentWord = SpreedWord.getAlignedWord(currentWord, 32);
		wordLabel.setText("<html><font color=black>" + 
				currentWord.substring(0, 15) +
				"</font><font color=blue>"+ 
				currentWord.substring(15, 16) +
				"<font color=black>" + 
				currentWord.substring(16, currentWord.length()) +
				"</font></html>");
	}
}
