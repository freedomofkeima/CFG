/*
 * [IF2052] Teori Bahasa & Otomata (Automata and Language Theory)
 * CFG Implementation in simple program syntax
 */

package sangatcfg;

/**
 * @see AutoCompleteDocument
 * 
 * @author Samuel Sjoberg, http://samuelsjoberg.com
 * @version 1.0.0
 * 
 * @param <T>
 *            the type to be returned by the service
 */

/**
*
* @modified by Muhamad Ihsan (13511049) & Iskandar Setiadi (13511073)
* @version April 2013
*/

public interface CompletionService<T> {

    /**
     * Autocomplete the passed string. The method will return the matching
     * object when one single object matches the search criteria. As long as
     * multiple objects stored in the service matches, the method will return
     * <code>null</code>.
     * 
     * @param startsWith
     *            prefix string
     * @return the matching object or <code>null</code> if multiple matches are
     *         found.
     */
    T autoComplete(String startsWith);
}
