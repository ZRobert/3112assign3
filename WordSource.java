package spreed;
/**
 * @author Robert Payne
 * class ITCS 3112-001
 * date 3/30/2014
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class WordSource {
	private ArrayList<String> words;
	/**
	 * Constructor that loads the words from a file into an ArrayList
	 * @param	filename Requires the filename of the text which will be loaded
	 */		
	public WordSource(String filename) throws FileNotFoundException{
		words = new ArrayList<String>();		
		try{
			File openFile = new File(filename);
			Scanner inputFile = new Scanner(openFile);
			while(inputFile.hasNext()){
				words.add(inputFile.next());

			}
		}catch(FileNotFoundException e){
			System.out.println(e);
			
		} 
	}
	/**
	 * Returns a word from the ArrayList
	 * @param	index of the desired word to be returned from ArrayList
	 */		
	public String getWord(int index) throws IndexOutOfBoundsException{
		try{
			return words.get(index);
		}catch(IndexOutOfBoundsException e){
			return words.get(0);
		}
	}
	/**
	 * Returns the number of words stored in the ArrayList
	 */	
	public int getNumberOfWords(){
		return words.size();
	}
	
}
