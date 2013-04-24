/*
 * [IF2052] Teori Bahasa & Otomata (Automata and Language Theory)
 * CFG Implementation in simple program syntax
 */

package sangatcfg;

/* Library Import Default */

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.Document;


/**
 *
 * @author Muhamad Ihsan (13511049) & Iskandar Setiadi (13511073)
 * @version April 2013
 */
public class TampilanUser extends JFrame {
    // Start of Class Object Declaration
    private JPanel panel; /* main panel */
    private JButton jButton1;
    private JButton jButton2;
    private static JEditorPane jEditorPane1;
    private JFileChooser jFileChooser1;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JScrollPane jScrollPane1;
    
    private CNFRule R; //Rules list which will be used within CYK Algorithm
    private NameService nameService; //part of auto-completion
    private Document autoCompleteDocument; //part of auto-completion
    private ArrayList RawInput; //Text area input
    // End of Class Object Declaration
    
     /**
     * Creates new form TampilanUser
     */
    public TampilanUser() {
    	R = new CNFRule();
    	RawInput = new ArrayList();
        initComponents();
    }
    
    protected void disableCancelButton(Container c){
    	/* A function to hide cancel button */
        int len = c.getComponentCount();
        for (int i = 0; i < len; i++) { /* looping through all components */
          Component comp = c.getComponent(i);
          if (comp instanceof JButton) {
            JButton b = (JButton)comp;
            Icon icon = b.getIcon();
            if (i == 1) b.setVisible(false); /* disabling cancel button */
            } 
          else if (comp instanceof Container) {
            disableCancelButton((Container)comp);
          } 
        }
    }
    
    public void initComponents(){
        panel = new JPanel(); /* construct panel */
        panel.setLayout(null);
        
        jLabel1 = new JLabel("Compilation Message:"); /* creating a new label inside form */
        jLabel1.setBounds(20,600,200,40);
                
        jLabel2 = new JLabel(); /* creating a new label inside form */  
        /* Printing text to label2 - Compilation message */
        jLabel2.setText("<html>"
                        + "This parser is created by Muhamad Ihsan and Iskandar Setiadi" 
                        + "<br>" 
                        + "Error message is depended on your first word / variable at the error line" 
                        + "</html>");
        jLabel2.setBounds(20,600,500,120);
        
        jButton1 = new JButton("Check"); /* creating a new button inside form */
        jButton1.setBounds(500,100,100,40);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ArrayList<String> Input = new ArrayList(); //Input being divided per category
                String textAreaText = jEditorPane1.getText();
                String tempText = new String("");
                /* Copying all text area into arraylist */
                RawInput.clear();
                for (int i = 0; i < textAreaText.length(); i++){
                	if (textAreaText.charAt(i) != '\n'){
                		tempText = tempText + textAreaText.charAt(i);
                	} else {
                		tempText = tempText + "\n";
                		RawInput.add(new String(tempText)); /* Moving all lines to ArrayList Input */
                		tempText = "";
                	}
                }
                if (textAreaText.length() != 0){
                	tempText = tempText + "\n"; 
                	RawInput.add(new String(tempText)); /* Last line of input */
                }
                /* At this position, all text area's text is already being divided by number of lines */
                ArrayList<ArrayList> InputRule = new ArrayList(R.readInputText());
                /* Begin of Input parse */            
                
                boolean IsComment = false;
                String tempTextTwo = new String(""); /* Temporary variable for string parser */
                for (int i = 0; i < RawInput.size(); i++){
                	tempText = RawInput.get(i).toString();
                	tempTextTwo = "";
                	for (int j = 0; j < tempText.length(); j++){
                		if (tempText.charAt(j) == '\n') { /* Symbol newline processing */
                			if (tempTextTwo.equals("") == false) Input.add(new String(tempTextTwo));
                			tempTextTwo = "";
                			Input.add(new String("newline"));
                		}
                		else if (tempText.charAt(j) == '{') { /* Symbol '{' processing */
                			if (tempTextTwo.equals("") == false) Input.add(new String(tempTextTwo));
                			tempTextTwo = "";
                			Input.add(new String("{"));
                			IsComment = true;
                		}
                		else if (tempText.charAt(j) == '}') { /* Symbol '}' processing */
                			Input.add(new String(tempTextTwo));
                			tempTextTwo = "";
                			Input.add(new String("}"));
                			IsComment = false;
                		}
                		 /* All characters in a comment */
                		else if (IsComment == true) {
                			tempTextTwo = tempTextTwo + tempText.charAt(j);
                		}
                		else if (IsComment == false){
                			/* Not a comment processing, for operands, assingment, etc */
                			if ((tempText.charAt(j) == '	') || (tempText.charAt(j) == ' ')){
                    			if (tempTextTwo.equals("") == false) Input.add(new String(tempTextTwo));
                    			tempTextTwo = "";             				
                			} else if ( (tempText.charAt(j) == '<') || (tempText.charAt(j) == '(') || (tempText.charAt(j) == ')') || (tempText.charAt(j) == '+') || (tempText.charAt(j) == '-') ||(tempText.charAt(j) == '*') ){
                				if (tempTextTwo.equals("") == false) Input.add(new String(tempTextTwo));
                				tempTextTwo = "" + tempText.charAt(j);
                				Input.add(new String(tempTextTwo));
                				tempTextTwo = "";
                			} else if (tempText.charAt(j) == '>'){
                				/* if character j = 0 */
                				if (j == 0) Input.add(new String(">"));
                				else {
                					if (tempText.charAt(j-1) == '<'){
                						/* add to previous */
                						Input.set(Input.size() - 1, Input.get(Input.size() - 1) + ">");
                					} else {
                						/* add to a new object */
                        				if (tempTextTwo.equals("") == false) Input.add(new String(tempTextTwo));
                        				tempTextTwo = "";
                        				Input.add(new String(">"));
                					}
                				}
                			} else if (tempText.charAt(j) == '='){
                				/* if character j = 0 */
                				if (j == 0) Input.add(new String("="));
                				else {
                					if ((tempText.charAt(j-1) == '<') || (tempText.charAt(j-1) == '>') || (tempText.charAt(j-1) == '=')){
                						/* add to previous */
                						Input.set(Input.size() - 1, Input.get(Input.size() - 1) + "=");
                					} else {
                						/* add to a new object */
                        				if (tempTextTwo.equals("") == false) Input.add(new String(tempTextTwo));
                        				tempTextTwo = "";
                        				Input.add(new String("="));
                					}              					
                				}
                			} else tempTextTwo = tempTextTwo + tempText.charAt(j);
                			
                		}
                	}
                }
                
                for (int i = 1; i < Input.size() ; i++){
                	if ((Input.get(i).equals("newline")) && (Input.get(i-1).equals("newline"))) Input.remove(i);
                	if ((int) Input.get(i-1).toString().charAt(0) == 13) Input.remove(i-1);
                	if ((int) Input.get(i-1).toString().charAt(Input.get(i-1).toString().length() - 1) == 13) 
                		Input.set(i-1, Input.get(i-1).toString().substring(0,Input.get(i-1).toString().length()-1));
                }
                	/* End of Input parse */
                
            	/* Getting a result from CYK Algorithm */
            	Integer t[] = new Integer [Input.size() + 2];
            	t[0] = 1; /* initial state */
            
            	if (Input.size() != 0)
            	    t = new CYKAlgorithm().getResult(InputRule,Input);
            	
            	/* Printing output into screen */
            	jLabel2.setForeground(Color.green);
            	if (t[0] == -1) jLabel2.setText("Algoritma anda Valid!");
            	else {
            		jLabel2.setForeground(Color.red);
            		jLabel2.setText("<html>" + "Algoritma anda tidak Valid! <br>");
            		jLabel2.setText(jLabel2.getText() + t[0] + " Possibility Errors found! <br>");
            		/* Printing error message ; format : - [1] + message - [2] */
					List<String> e = Arrays.asList("begin", "if-then", "repeat", "else", "end", "until", "while-do", 
							"assignment", "input", "output", "comments"); /* error message list */
            		for (int i = 0; i < Input.size(); i++){
            			/* printing error here */
            			for (int j = 1; j <= t[0]; j++)
            				if (t[2*j-1] == i){ /* Exception handling */
            					if ((t[2*j] >0) && (t[2*j] <= 11)) jLabel2.setText(jLabel2.getText() + "Line " + i + " : Wrong usage of " + e.get(t[2*j] - 1));
            					else {
            						if (t[2*j] == 0) jLabel2.setText(jLabel2.getText() + "Line " + i + " : That's not a code! Undefined error");	
                					if (t[2*j] == 12) jLabel2.setText(jLabel2.getText() + "Line " + i + " : Main 'begin' expected");	
                					if (t[2*j] == 13) jLabel2.setText(jLabel2.getText() + "Line " + i + " : 'begin' without 'end' is found");	
                					if (t[2*j] == 14) jLabel2.setText(jLabel2.getText() + "Line " + i + " : 'repeat' without 'until' is found");	
                					if (t[2*j] == 15) jLabel2.setText(jLabel2.getText() + "Line " + i + " : 'if-then' is not followed by 'begin'");	
                					if (t[2*j] == 16) jLabel2.setText(jLabel2.getText() + "Line " + i + " : 'while-do' is not followed by 'begin'");	
                					if (t[2*j] == 17) jLabel2.setText(jLabel2.getText() + "Line " + i + " : 'if-then-else' is not followed by 'begin'");
                					if (t[2*j] == 18) jLabel2.setText(jLabel2.getText() + "Line " + i + " : block 'until' without 'repeat' is found");
                					if (t[2*j] == 19) jLabel2.setText(jLabel2.getText() + "Line " + i + " : 'end' without respective 'begin' is found");
                					if (t[2*j] == 20) jLabel2.setText(jLabel2.getText() + "Line " + i + " : wrong usage of 'else'");
                					if (t[2*j] == 21) jLabel2.setText(jLabel2.getText() + "Line " + i + " : undefined 'begin' block");
            					}
            					if (j != t[0]) jLabel2.setText(jLabel2.getText() + "; ");
            				}      				
            			
            	  }
            		jLabel2.setText(jLabel2.getText() + "</html>");
            	}
            }
        });
        
        jButton2 = new JButton("Exit"); /* creating a new button inside form */
        jButton2.setBounds(500,180,100,40);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                System.exit(0);
            }
        });
        
        jFileChooser1 = new JFileChooser(); /* creating a file chooser inside form */
        /* properties of file chooser */
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
        jFileChooser1.setFileFilter(filter);
        disableCancelButton(jFileChooser1);
        /* end of file chooser properties */
        jFileChooser1.setBounds(20,300,650,300);
        jFileChooser1.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	File file = (File) jFileChooser1.getSelectedFile();
            	if (evt.getActionCommand().equals("ApproveSelection")){
            		/* Copying file text into text area */
            		try{
            		   FileReader reader = new FileReader(file);
            		   BufferedReader br = new BufferedReader(reader);
            		   jEditorPane1.read(br, null);
            		   br.close();
            		   jEditorPane1.requestFocus();
            		} catch (Exception e){}
            	}
            }
        });
       
        jEditorPane1 = new JEditorPane(); /* creating a editor pane inside form */
        jEditorPane1.setCaretColor(Color.yellow);
        jEditorPane1.setBackground(Color.black);
        jEditorPane1.setForeground(Color.white);
        NameService nameService = new NameService(); /* creating a new name service */
        Document autoCompleteDocument = new AutoCompleteDocument(nameService, jEditorPane1);
        jEditorPane1.setDocument(autoCompleteDocument);
        
        jScrollPane1 = new JScrollPane(jEditorPane1); /* creating a scroll pane inside form */ 
        jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setBounds(20,20,460,270);
        
        /* Add a line number */
        TextLineNumber tln = new TextLineNumber(jEditorPane1);
        jScrollPane1.setRowHeaderView(tln);
        
        /* Add to Panel */
        panel.add(jLabel1);
        panel.add(jLabel2);
        panel.add(jButton1);
        panel.add(jFileChooser1);
        panel.add(jButton2);
        panel.add(jScrollPane1);
        
        /* Adding background to screen */
        try {
        	BufferedImage wp = ImageIO.read(this.getClass().getResource("bg.jpg"));
        	/* for creating jar environment */
			//BufferedImage wp = ImageIO.read(this.getClass().getClassLoader().getResource("bg.jpg"));
			JLabel backgroundImage = new JLabel(new ImageIcon(wp));
			backgroundImage.setBounds(0,0,700,750);
			panel.add(backgroundImage, null);
        } catch (Exception e) {System.out.println(e.getMessage());}        
        
        /* Add panel to screen */
        add(panel);
        setTitle("Program Sangat Keren");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 750);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);    
    }
    
    /* The following code is part of auto completion bonus */
    private static class NameService implements CompletionService<String> {

        /** Our name data. */
        private List<String> data;

        /**
         * Create a new <code>NameService</code> and populate it.
         */
        public NameService() {
            data = Arrays.asList("begin","end","then","if","repeat","until","while","else","input()","output()");
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            StringBuilder b = new StringBuilder();
            for (String o : data) {
                b.append(o).append("\n");
            }
            return b.toString();
        }

        /** {@inheritDoc} */
        public String autoComplete(String startsWith) {
            // Naive implementation, but good enough for the sample
            String hit = null;
            /* Getting a last word of sentence - modification */
            String lastWord = "";
            for (int i = 0; i < startsWith.length(); i++)
            {
            	if ((startsWith.charAt(i) != '	') && (startsWith.charAt(i) != ' ')) lastWord = lastWord + startsWith.charAt(i);
            	else {
            		lastWord = "";
            	}
            } 
            /* end of modification */
            for (String o : data) {
                if (o.startsWith(lastWord)) {
                    // CompletionService contract states that we only
                    // should return completion for unique hits.
                    if (hit == null) {
                        hit = o;
                    } else {
                        hit = null;
                        break;
                    }
                }
            }
            return hit;
        }

    }
    
    public static int getCaretPosition(){return jEditorPane1.getCaretPosition();}
    
    public static String getJEditorPane(){return jEditorPane1.getText();}
    /* End of auto completion bonus */
}