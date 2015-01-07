package nl.tue.win.ga.model;

import java.awt.Point;


/**
 *
 * @author Coen
 */
public class BinarySearchTree {

    /**
     * Construct the tree.
     */
    public BinarySearchTree() {
        root = null;
    }

    public void insert(Point p) {
        root = insert(p, root, null);
    }

    public void remove(Point p) {
        root = remove(p, root);
    }

    public void removeMin() {
        root = removeMin(root);
    }

    public Point findMin() {
        return elementAt(findMin(root));
    }

    public Point findMax() {
        return elementAt(findMax(root));
    }

    public Point find(Point x) {
        return elementAt(find(x, root));
    }

    /**
     * Make the tree logically empty.
     */
    public void makeEmpty() {
        root = null;
    }

    public Point findLeft(Point p) {
        BinaryNode t = find(p, root);
        Point parElement = t.Parent;
        
        while(parElement != null) {
           if(t == find(parElement, root).right) {
            return parElement;
        }
           else {
               t = find(parElement, root);
               parElement = t.Parent;
           }
        }
        return null;
    }
    
        public Point findRight(Point p) {
        BinaryNode t = find(p, root);
        Point parElement = t.Parent;
        
        while(parElement != null) {
           if(t == find(parElement, root).left) {
            return parElement;
        }
           else {
               t = find(parElement, root);
               parElement = t.Parent;
           }
        }
        return null;
    }
    /**
     * Test if the tree is logically empty.
     *
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty() {
        return root == null;
    }

    private Point elementAt(BinaryNode t) {
        return t == null ? null : t.element;
    }

    protected BinaryNode insert(Point p, BinaryNode t, Point parElement) {
        if (t == null) {
            t = new BinaryNode(p, parElement);
        } else if (p.y < t.element.y) {
            if (parElement == null) {
                t.left = insert(p, t.left, root.element);
            } else {
                t.left = insert(p, t.left, t.element);
            }
        } else if (p.y > t.element.y) {
            if (parElement == null) {
                t.right = insert(p, t.right, root.element);
            } else {
                t.right = insert(p, t.right, t.element);
            }
        }
        return t;
    }

    /**
     * Internal method to remove from a subtree.
     */
    protected BinaryNode remove(Point p, BinaryNode t) {
        if (t == null) {

        }
        if (p.y < t.element.y) {
            t.left = remove(p, t.left);
        } else if (p.y > t.element.y) {
            t.right = remove(p, t.right);
        } else if (t.left != null && t.right != null) // Two children
        {
            t.element = findMin(t.right).element;
            t.right = removeMin(t.right);
        } else {
            t = (t.left != null) ? t.left : t.right;
        }
        return t;
    }

    /**
     * Internal method to remove minimum item from a subtree.
     *
     * @param t the node that roots the tree.
     * @return the new root.
     */
    protected BinaryNode removeMin(BinaryNode t) {
        if (t == null) {
            System.out.println("hoi");
        } else if (t.left != null) {
            t.left = removeMin(t.left);
            return t;
        } else {
            return t.right;
        }
        return t.right;
    }

    /**
     * Internal method to find the smallest item in a subtree.
     *
     * @param t the node that roots the tree.
     * @return node containing the smallest item.
     */
    protected BinaryNode findMin(BinaryNode t) {
        if (t != null) {
            while (t.left != null) {
                t = t.left;
            }
        }

        return t;
    }

    /**
     * Internal method to find the largest item in a subtree.
     *
     * @param t the node that roots the tree.
     * @return node containing the largest item.
     */
    private BinaryNode findMax(BinaryNode t) {
        if (t != null) {
            while (t.right != null) {
                t = t.right;
            }
        }

        return t;
    }

    /**
     * Internal method to find an item in a subtree.
     *
     * @param x is item to search for.
     * @param t the node that roots the tree.
     * @return node containing the matched item.
     */
    private BinaryNode find(Point p, BinaryNode t) {
        while (t != null) {
            if (p.y < t.element.y) {
                t = t.left;
            } else if (p.y > t.element.y) {
                t = t.right;
            } else {
                return t;    // Match
            }
        }

        return null;         // Not found
    }

    /**
     * The tree root.
     */
    protected BinaryNode root;

}

// Basic node stored in unbalanced binary search trees
// Note that this class is not accessible outside
// of this package.
class BinaryNode {

    // Constructors
    BinaryNode(Point theElement, Point par) {
        element = theElement;
        left = right = null;
        Parent = par;
    }

    // Friendly data; accessible by other package routines
    Point element;      // The data in the node
    BinaryNode left;         // Left child
    BinaryNode right;        // Right child
    Point Parent;
}
