/*
 * [IF2052] Teori Bahasa & Otomata (Automata and Language Theory)
 * CFG Implementation in simple program syntax
 */

package sangatcfg;

/* Library Import Default */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


/**
 *
 * @author Muhamad Ihsan (13511049) & Iskandar Setiadi (13511073)
 * @version April 2013
 */
public class CNFRule {
    
	private ArrayList<String> temp = new ArrayList(); /* first element = LHS, rest = RHS */
	
	public void CNFRule() {
		/* Constructor of class CNFRule */
	}
	
    public ArrayList readInputText(){
		ArrayList<ArrayList> tempInput = new ArrayList();
    	try{
    		/* Reading input from file */
    		File inputRule = new File("bin/sangatcfg/rules.txt");
    		BufferedReader reader = new BufferedReader(new FileReader(inputRule));
    		String s;
    		int count;
    		
    		while ((s = reader.readLine()) != null){
    			temp.add(s.substring(0,2));
    			count = 0;
    			for (int i = 0; i < s.length(); i++) if(s.charAt(i) == ' ') count++;
    			if (count == 2) {
    				temp.add(s.substring(6));
    			} else {
    				temp.add(s.substring(6,8));
    				temp.add(s.substring(9,11));
    			}
    			tempInput.add((ArrayList) temp.clone());
    			temp.clear();
    		}

    	} catch (FileNotFoundException e){
    		System.out.println("File not Found!" + e);
    	} catch (IOException e){
    		System.out.println("IO error!" + e);
    	}
    	return tempInput;
    }
    
}
