/*
 * [IF2052] Teori Bahasa & Otomata (Automata and Language Theory)
 * CFG Implementation in simple program syntax
 */

package sangatcfg;

/* Library Import Default */
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import java.awt.Dimension;
import java.awt.GridLayout;

/**
*
* @author Muhamad Ihsan (13511049) & Iskandar Setiadi (13511073)
* @version April 2013
*/

public class BuildTree extends JPanel implements TreeSelectionListener {
    private JTree tree;
    private static boolean DEBUG = false;

    public BuildTree(DefaultMutableTreeNode BT) {
        super(new GridLayout(1,0));

        //Create the nodes.
        //createNodes(BT);

        //Create a tree that allows one selection at a time.
        tree = new JTree(BT);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        //Listen for when the selection changes.
        tree.addTreeSelectionListener(this);

        //Create the scroll pane and add the tree to it. 
        JScrollPane treeView = new JScrollPane(tree);
        Dimension minimumSize = new Dimension(200, 200);
        treeView.setMinimumSize(minimumSize);

        //Add the tree to this panel.
        add(treeView);
    }

    /** Required by TreeSelectionListener interface. */
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node == null) return;
    } 
        
    /**
     * Create the GUI and show it.
     */
    public static void createAndShowGUI(DefaultMutableTreeNode BT) {

        //Create and set up the window.
        JFrame frame = new JFrame("Parse Tree");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Add content to the window.
        frame.add(new BuildTree(BT));

        //Display the window.
        frame.pack();
        frame.setVisible(true);
        frame.setSize(500,500);
        frame.setLocationRelativeTo(null);
    }

}
