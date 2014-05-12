package spreed;
/**
 * @author Robert Payne
 * class ITCS 3112-001
 * date 3/30/2014
 */
public class SpreedWord {
	/**
	 * Returns the pivot of a particular word
	 * @param	word The word that the pivot value will be based on
	 */	
	public static int getPivot(String word){
		if(word.endsWith(",") ||word.endsWith(";") || word.endsWith(".")){
			word = word.substring(0, word.length()-1);
		}	
		if(word.length() == 1){
			return 0;
		}else if(word.length() < 6){
			return 1;
		}else if(word.length() < 10){
			return 2;
		}else if(word.length() < 14){
			return 3;
		}else{
			return 4;
		}
	}
	/**
	 * Pads the word within spaces in order to make the word display evenly
	 * @param	word The word to be padded
	 * @param	length The desired number of characters to make the string
	 */	
	public static String getAlignedWord(String word, int length) {
		int padding = length;
		if(word.length() > length){
			word = word.substring(0, length);
			return word;
		}
		while(length > word.length()){
			if(padding%2 == 0){
				word = " " + word;
			}else{
				word = word + " ";
			}
			padding--;
		}
		return word;
	}
	/**
	 * Determines the number of words a string contains using 5 characters
	 * equals 1 word and rounds up if the word.length() % 5 > 0. Also adds
	 * more if there is a comma, period, or semi-colon
	 * @param	word The word for the desired pause length.
	 */	
	public static int getPauseLength(String word) {
		int pause;
		pause = word.length()/5;
		if(word.length()%5 != 0){
			pause++;
		}
		if(word.endsWith(",")){
			pause++;
		}else if(word.endsWith(";") || word.endsWith(".")){
			pause += 2;
		}
		return pause;
	}
}
