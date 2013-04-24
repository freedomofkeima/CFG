/*
 * [IF2052] Teori Bahasa & Otomata (Automata and Language Theory)
 * CFG Implementation in simple program syntax
 */

package sangatcfg;

/* Library Import Default */

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

/**
 * A {@link Document} performing auto completion on the inserted text. This
 * document can be used on any {@link JTextComponent}.
 * 
 * @see CompletionService
 * 
 * @author Samuel Sjoberg, http://samuelsjoberg.com
 * @version 1.0.0
 */

/**
*
* @modified by Muhamad Ihsan (13511049) & Iskandar Setiadi (13511073)
* @version April 2013
*/

public class AutoCompleteDocument extends PlainDocument {

    /** Default serial version UID. */
    private static final long serialVersionUID = 1L;

    /** Completion service. */
    private CompletionService<?> completionService;

    /** The document owner. */
    private JTextComponent documentOwner;

    /**
     * Create a new <code>AutoCompletionDocument</code>.
     * 
     * @param service
     *            the service to use when searching for completions
     * @param documentOwner
     *            the document owner
     */
    public AutoCompleteDocument(CompletionService<?> service,
            JTextComponent documentOwner) {
        this.completionService = service;
        this.documentOwner = documentOwner;
    }

    /**
     * Look up the completion string.
     * 
     * @param str
     *            the prefix string to complete
     * @return the completion or <code>null</code> if completion was found.
     */
    protected String complete(String str) {
        Object o = completionService.autoComplete(str);
        return o == null ? null : o.toString();
    }

    /** {@inheritDoc} */
    @Override
    public void insertString(int offs, String str, AttributeSet a)
            throws BadLocationException {
        if (str == null || str.length() == 0) {
            return;
        }
        /* Getting a last word of sentence - modification */
        String lastWord = "";
        int length = TampilanUser.getJEditorPane().length(); /* length of JEditorPane */
        int numberofline = 0;
        for (int i = length - 1; i >= 0; i--)
        	if (TampilanUser.getJEditorPane().charAt(i) == '\n') numberofline++;
        for (int i = length - 1; i >= 0; i--)
        {
        	if (TampilanUser.getJEditorPane().charAt(i) == ' ') break;
        	if (TampilanUser.getJEditorPane().charAt(i) == '	') break;
        	if (TampilanUser.getJEditorPane().charAt(i) == '\n') break;
        	lastWord =  TampilanUser.getJEditorPane().charAt(i) + lastWord;
        } 
        /* end of modification */
        String text = getText(0, offs); // Current text.
        String completion = complete(lastWord + str);
        if (completion != null && text.length() > 0) {
            str = completion.substring(lastWord.length());
            super.insertString(offs, str, a);
            documentOwner.select(length + 1 - numberofline, getLength());
        } else {
            super.insertString(offs, str, a);
        }
    }
}