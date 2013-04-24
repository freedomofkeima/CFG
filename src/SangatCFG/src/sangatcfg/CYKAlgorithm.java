/*
 * [IF2052] Teori Bahasa & Otomata (Automata and Language Theory)
 * CFG Implementation in simple program syntax
 */

package sangatcfg;

/* Library Import Default */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;


/**
 *
 * @author Muhamad Ihsan (13511049) & Iskandar Setiadi (13511073)
 * @version April 2013
 */
public class CYKAlgorithm {
    
	public void CYKAlgorithm(){
		/* Constructor of class CYKAlgorithm */
	}
	
	public Integer[] getResult(ArrayList<ArrayList> R, ArrayList<String> S){
		//CNFRule = Rules which will be used
		//S = Input from user
		//Output format : array of Integer, first element = number of following error lines
		//If no error exists, first line = '-1'
		//If error exists, then each of next two lines = number of error lines + error message
		Integer t[] = new Integer[S.size() + 2];
		String AddressVal[] = new String[R.size()];
		Integer LHS[] = new Integer[R.size()];
		Integer RHS1[] = new Integer[R.size()];
		Integer RHS2[] = new Integer[R.size()];
		String tempA = new String();
		String tempB = new String(); /* string for parse */
		List<String> ec = Arrays.asList("BA", "EA", "HA", "GA", "DA", "IA", "JA", "VA", "MA", "NA", "KA"); /* for throwing error message */
		List<String> ed = Arrays.asList("XA", "XD", "HB", "GA", "DA", "HK", "XE", "XF", "XB", "XC", "KE"); /* For searching error line */
		
		/* Count all unique (LHS) variable in R */
		int Unique = 0;
		
		boolean IsUnique[] = new boolean[R.size()];
		for (int i = 0; i < R.size(); i++) IsUnique[i] = true;
		for (int i = 0; i < R.size(); i++){
			if (IsUnique[i]){
			 IsUnique[i] = false;
			 AddressVal[Unique] = (String) R.get(i).get(0);
			 Unique++;
				for (int j = i+1; j < R.size(); j++)
				  if (R.get(i).get(0).equals(R.get(j).get(0))){IsUnique[j] = false;
			   }
			}
		}
		
		/* Add all RHS */
		for (int i = 0; i < R.size(); i++){
			tempA = (String) R.get(i).get(0);
			for (int j = 0; j < Unique; j++)
				if (AddressVal[j].equals(tempA)) {LHS[i] = j; break;}	
			if (R.get(i).size() == 3){			
				tempA = (String) R.get(i).get(1);
				for (int j = 0; j < Unique; j++)
					if (AddressVal[j].equals(tempA)) {RHS1[i] = j; break;}
				tempA = (String) R.get(i).get(2);
				for (int j = 0; j < Unique; j++)
					if (AddressVal[j].equals(tempA)) {RHS2[i] = j; break;}
			}		
		}
		
		/* At this point, Unique equals to number of different terminal variables */
		boolean p[][][] = new boolean[S.size()+1][S.size()+1][Unique+1]; /* construct 3D boolean array */
		/*for creating parse tree */
		DefaultMutableTreeNode Tree[][][] = new DefaultMutableTreeNode[S.size()+1][S.size()+1][Unique+1];
		
		/* Initialize all elements by false */
		for (int i = 1; i < S.size()+1; i++)
			for (int j = 1; j < S.size()+1; j++)
				for (int k = 0; k < Unique+1; k++)
					p[i][j][k] = false;
		
		/* Initialize all elements by false */
		for (int i = 1; i < S.size()+1; i++)
			for (int j = 1; j < S.size()+1; j++)
				for (int k = 0; k < Unique+1; k++)
					Tree[i][j][k] = null;
		
		/* Translate each character in S into boolean */
		for (int i = 0; i < S.size(); i++)
			for (int j = 1; j < Unique + 1; j++)
			{
				/* translate each S into specific terminal symbol */
				if (R.get(j-1).size() == 2){
					if (R.get(j-1).get(1).equals("integer")){
						/* check whether its an integer (contains digit 0 - 9 only) */
						if (S.get(i).matches("[0-9]+")) {
							p[i+1][1][j-1] = true;
							Tree[i+1][1][j-1] = (new DefaultMutableTreeNode(S.get(i)) );
						}
					}
					if (R.get(j-1).get(1).equals("variable")){
						/* check whether its an variable (contains a..z, A..Z, 0..9, not part of syntax) */
						if (S.get(i).matches("^[a-zA-Z0-9]*$") && CheckSyntax(S.get(i))) {
							tempB = "" + S.get(i).charAt(0);
							if (!tempB.matches("[0-9]+")) {
								p[i+1][1][j-1] = true;
								Tree[i+1][1][j-1] = (new DefaultMutableTreeNode(S.get(i)) );						
							}
						}
					} 
					if (R.get(j-1).get(1).equals("comment")){
					/* check whether its an comment (contains all symbols except syntax)*/
						if (CheckSyntax(S.get(i))) {
							p[i+1][1][j-1] = true;
							Tree[i+1][1][j-1] = (new DefaultMutableTreeNode(S.get(i)) );
						}
					} 
					/* check whether it's a match */
					if (R.get(j-1).get(1).equals(S.get(i))){
						p[i+1][1][j-1] = true;
						Tree[i+1][1][j-1] = (new DefaultMutableTreeNode(S.get(i)) );						
					}
				}
				/* check if double operator */
				if (S.get(i).matches("<=") && R.get(j-1).get(0).equals("CD")) {
					p[i+1][1][j-1] = true;
					Tree[i+1][1][j-1] = (new DefaultMutableTreeNode(S.get(i)) );		
				}
				if (S.get(i).matches(">=") && R.get(j-1).get(0).equals("CE")) {
					p[i+1][1][j-1] = true;
					Tree[i+1][1][j-1] = (new DefaultMutableTreeNode(S.get(i)) );
				}
				if (S.get(i).matches("<>") && R.get(j-1).get(0).equals("CF")) {
					p[i+1][1][j-1] = true;
					Tree[i+1][1][j-1] = (new DefaultMutableTreeNode(S.get(i)) );
				}
				if (S.get(i).matches("==") && R.get(j-1).get(0).equals("CX")) {
					p[i+1][1][j-1] = true;
					Tree[i+1][1][j-1] = (new DefaultMutableTreeNode(S.get(i)) );
				}
			}
		
		/* Check each partition using CYK-Algorithm */
		for (int i = 2; i < S.size() + 1; i++ ) /* length of span */
			for (int j = 1; j <= S.size() - i + 1; j++) /* start of span */
				for (int k = 1; k < i; k++) /* partition of span */
					/* for each production RA -> RB RC */
					/* if p[j,k,B] and p[j+k, i-k, C] then P[j,i,A] = true */
					for (int l = 0; l < R.size(); l++)
						if (R.get(l).size() == 3) 
							if ((p[j][k][RHS1[l]]) && (p[j+k][i-k][RHS2[l]])) {
								p[j][i][LHS[l]] = true;
								Tree[j][i][LHS[l]] = (new DefaultMutableTreeNode(AddressVal[LHS[l]]) );
								Tree[j][i][LHS[l]].add(Tree[j][k][RHS1[l]]);
								Tree[j][i][LHS[l]].add(Tree[j+k][i-k][RHS2[l]]);
							}
					
		/* Check if the language is valid */
		/* if any of p[1,n,x] is true (x is iterated over the set S, where S are all the indices for Rs then S <- member of language */
		t[0] = 0; /* initial state */
		
		for (int i = 0; i < Unique; i++){
	    	if ((p[1][S.size()][i]) && (AddressVal[i].equals("XX"))) t[0] = -1;
			/* Showing parse tree, iff t[0] = -1 */
			if (t[0] == -1) BuildTree.createAndShowGUI(Tree[1][S.size()][i]);
		}

		if (t[0] == 0){
			/* Error is found */
			Integer CurrentPosition = new Integer(1); /* Using two pointers indexing method */
			Integer PrevPosition = new Integer(1);
			int NL = 1;
			boolean IsLineValid;
			ArrayList<String> SpError = new ArrayList();
			ArrayList<Integer> SpErrorLine = new ArrayList();
			
			while (CurrentPosition <= S.size()){
				/* Iterate through all positions */
				if (S.get(CurrentPosition - 1).equals("newline")){
					IsLineValid = false;
					/* Validity check */
					for (int i = 0; i < Unique; i++){
					/* searching error lines position */
					for (int j = 0; j < ed.size(); j++)	
					  if ((p[PrevPosition][CurrentPosition - PrevPosition + 1][i]) && (AddressVal[i].equals(ed.get(j)))) IsLineValid = true;
					if ((p[PrevPosition][1][i]) && ((AddressVal[i].equals("GA")) || (AddressVal[i].equals("DA")))) 
						for (int j = 0; j < Unique; j++)
					     	if ((p[PrevPosition+1][1][j]) && (AddressVal[j].equals("QA"))) 
							  IsLineValid = true;
					}
					
					if (IsLineValid){
					for (int i = 0; i < Unique; i++){
					/* Special Validity Check for main begin, begin, end,[if], [while], repeat, until, else */
					/* Note that, end, until, and else may error while previous stack != this point ('Programmer Note)*/
					/* Note that, a sequence end > else > begin must occur respectively while end replaced else and else must be followed by begin */
						if (NL == 1){ /* case for main begin */
							if ((!p[1][1][i]) && (AddressVal[i].equals("BA"))) IsLineValid = false;
							if ((p[1][1][i]) && (AddressVal[i].equals("BA"))) {
								SpError.add("begin"); SpErrorLine.add(1);
							}
						} else { 
							/* case begin may only occur when stack - 1 = if / while / else */
							if ((p[PrevPosition][1][i]) && (AddressVal[i].equals("BA"))) { 
								if (SpErrorLine.get(SpError.size() - 1) == NL -1){ /* check the previous line of begin */
								  if ((SpError.get(SpError.size() - 1).equals("if")) || (SpError.get(SpError.size() - 1).equals("while")) || (SpError.get(SpError.size() - 1).equals("else"))){
									SpError.set(SpError.size() - 1, "begin"); SpErrorLine.set(SpError.size() - 1, NL);	
								} else { 
									/* remove the previous stack, add error message */
									t[0]++;  t[2 * t[0] - 1] = SpErrorLine.get(SpError.size() - 1); /* push number of lines */
									if (SpError.get(SpError.size() - 1).equals("if")) t[2 * t[0]] = 15; 
									if (SpError.get(SpError.size() - 1).equals("while")) t[2 * t[0]] = 16; 
									if (SpError.get(SpError.size() - 1).equals("else")) t[2 * t[0]] = 17; 
									/* pop previous stack */
									SpErrorLine.remove(SpError.size() - 1);
									SpError.remove(SpError.size() - 1);
									break;
								}
							  } else {/* add error message */ t[0]++;  t[2 * t[0] - 1] = NL; t[2 * t[0]] = 21; break; /* push number of lines and error message */}
							}
							/* case for end */
							if ((p[PrevPosition][1][i]) && (AddressVal[i].equals("DA")))  {
							  if (SpError.size() - 1 >= 0){
								while (SpError.size() -1 >= 0){
									if (!SpError.get(SpError.size() - 1).equals("begin")){
										/* pop previous repeat stack */
										SpErrorLine.remove(SpError.size() - 1);
										SpError.remove(SpError.size() - 1);
									} else break;
								}
								if (SpError.size() -1 < 0) {/* add error message */ t[0]++;  t[2 * t[0] - 1] = NL; t[2 * t[0]] = 19; break; /* push number of lines and error message */	}
								if (SpError.get(SpError.size() - 1).equals("begin")) {
									/* pop previous repeat stack */
									SpErrorLine.remove(SpError.size() - 1);
									SpError.remove(SpError.size() - 1);
									break;
								} else {/* add error message */ t[0]++;  t[2 * t[0] - 1] = NL; t[2 * t[0]] = 19; /* push number of lines and error message */	}
							  } else {/* add error message */ t[0]++;  t[2 * t[0] - 1] = NL; t[2 * t[0]] = 19; /* push number of lines and error message */	}
							}
							/* case for if */
							if ((p[PrevPosition][1][i]) && (AddressVal[i].equals("EA"))) { SpError.add("if"); SpErrorLine.add(NL);/* add to stack */ }
							/* case for end > else */
							if ((p[PrevPosition][1][i]) && (AddressVal[i].equals("GA"))) { SpError.add("else"); SpErrorLine.add(NL);/* add to stack */ }
							/* case for while */
							if ((p[PrevPosition][1][i]) && (AddressVal[i].equals("JA"))) { SpError.add("while"); SpErrorLine.add(NL);/* add to stack */ }						
							/* case for repeat */
							if ((p[PrevPosition][1][i]) && (AddressVal[i].equals("HA"))) { SpError.add("repeat"); SpErrorLine.add(NL);/* add to stack */ }
							/* case for until */
							if ((p[PrevPosition][1][i]) && (AddressVal[i].equals("IA")) ) {
								if (SpError.get(SpError.size() - 1).equals("repeat")){
									/* pop previous repeat stack */
									SpErrorLine.remove(SpError.size() - 1);
									SpError.remove(SpError.size() - 1);
									break;
								} else {
									/* add error message */
									t[0]++;  t[2 * t[0] - 1] = NL; t[2 * t[0]] = 18; /* push number of lines and error message */	
							}
						}
					 }}}
					
					/* End of Validity check */
					if (!IsLineValid){
						t[0]++;
						/* Push number of lines */
						t[2 * t[0] - 1] = NL;
						t[2 * t[0]] = 0;
						/* Push error message */
						for (int i = 0; i < Unique; i++) { /* Normal cases */
							for (int j = 0; j < ec.size(); j++)
							if ((p[PrevPosition][1][i]) && (AddressVal[i].equals(ec.get(j)))) t[2 * t[0]] = j+1; /* Error messages */ 
						/* Special cases */
							if (NL == 1) { t[2 * t[0] - 1] = 0; t[2 * t[0]] = 12; }
						}
					}
					NL++;
					PrevPosition = CurrentPosition + 1; /* Prev pointers to Current Position + 1 */
				}
				CurrentPosition++; /* Increase to next position */
			}
			
			/* pop all SpError stack if not empty */
			while (!SpError.isEmpty()){
				t[0]++;
				t[2 * t[0] - 1] = SpErrorLine.get(SpError.size() - 1); /* push number of lines */
				if (SpError.get(SpError.size() - 1).equals("begin")) t[2 * t[0]] = 13;
				if (SpError.get(SpError.size() - 1).equals("repeat")) t[2 * t[0]] = 14;		
				if (SpError.get(SpError.size() - 1).equals("else")) t[2 * t[0]] = 20;	
				SpError.remove(SpError.size() - 1);
				SpErrorLine.remove(SpErrorLine.size() - 1);
			}
			
		}

		return t;
	}
	
	protected boolean CheckSyntax(String s){
		boolean temp = true; /* temporary boolean variable */
		List<String> ee = Arrays.asList("begin", "end", "if", "then", "else", "repeat", "until", "while", "do", "input", "output", "newline"); /* For variable syntax parser */
		
		for (int i = 0; i < ee.size(); i++)
			if (s.equals(ee.get(i))) temp = false;
		return temp;
	}
}
