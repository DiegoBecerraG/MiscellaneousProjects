import java.io.*;
// data structure that represents a node in the tree
class AVLNode {
    Book bookObject;
    AVLNode parent;
    //int height;
    AVLNode leftPtr;
    AVLNode rightPtr;
    int bf;


    public AVLNode(Book book) {
        this.bookObject = book;
        this.parent = null;
        //this.height = 0;
        this.leftPtr = null;
        this.rightPtr = null;
        this.bf = 0;
    }
}

public class AVLTree {
    private AVLNode root;

    public AVLTree() {
        root = null;
    }

    private void printHelper(AVLNode currPtr, String indent, boolean last) {
        // print the tree structure on the screen
        if (currPtr != null) {
            System.out.print(indent);
            if (last) {
                System.out.print("R----");
                indent += "     ";
            } else {
                System.out.print("L----");
                indent += "|    ";
            }

            System.out.println(currPtr.bookObject.toString() + "(BF = " + currPtr.bf + ")");

            printHelper(currPtr.leftPtr, indent, false);
            printHelper(currPtr.rightPtr, indent, true);
        }
    }

    private AVLNode searchTreeHelper(AVLNode node, Long key) {
        if (node == null || key == node.bookObject.id) {
            return node;
        }

        if (key < node.bookObject.id) {
            return searchTreeHelper(node.leftPtr, key);
        }
        return searchTreeHelper(node.rightPtr, key);
    }

    private AVLNode deleteNodeHelper(AVLNode node, Long key) {
        // search the key
        if (node == null) return node;
        else if (key.compareTo(node.bookObject.id) < 0) node.leftPtr = deleteNodeHelper(node.leftPtr, key);
        else if (key.compareTo(node.bookObject.id) > 0) node.rightPtr = deleteNodeHelper(node.rightPtr, key);
        else {
            // the key has been found, now delete it

            // case 1: node is a leaf node
            if (node.leftPtr == null && node.rightPtr == null) {
                node = null;
            }

            // case 2: node has only one child
            else if (node.leftPtr == null) {
                AVLNode temp = node;
                node = node.rightPtr;
            }

            else if (node.rightPtr == null) {
                AVLNode temp = node;
                node = node.leftPtr;
            }

            // case 3: has both children
            else {
                AVLNode temp = minimum(node.rightPtr);
                node.bookObject = temp.bookObject;
                node.rightPtr = deleteNodeHelper(node.rightPtr, temp.bookObject.id);
            }

        }

        return node;
    }

    // update the balance factor the node
    private void updateBalance(AVLNode node) {
        if (node.bf < -1 || node.bf > 1) {
            rebalance(node);
            return;
        }

        if (node.parent != null) {
            if (node == node.parent.leftPtr) {
                node.parent.bf -= 1;
            }

            if (node == node.parent.rightPtr) {
                node.parent.bf += 1;
            }

            if (node.parent.bf != 0) {
                updateBalance(node.parent);
            }
        }
    }

    // rebalance the tree
    void rebalance(AVLNode node) {
        if (node.bf > 0) {
            if (node.rightPtr.bf < 0) {
                rightPtrRotate(node.rightPtr);
                leftPtrRotate(node);
                // Trouble node is left pointer of inserted node after rotations
                System.out.println("Imbalance condition occurred at inserting ISBN " + node.leftPtr + ": LR case fixed at ISBN " + node.bookObject.id);
            } else {
                leftPtrRotate(node);
                // Trouble node is right pointer of inserted node after rotations
                System.out.println("Imbalance condition occurred at inserting ISBN " + node.rightPtr + ": LL case fixed at ISBN " + node.bookObject.id);
            }
        } else if (node.bf < 0) {
            if (node.leftPtr.bf > 0) {
                leftPtrRotate(node.leftPtr);
                rightPtrRotate(node);
                // Trouble node is right pointer of inserted node after rotations
                System.out.println("Imbalance condition occurred at inserting ISBN " + node.rightPtr + ": RL case fixed at ISBN " + node.bookObject.id);
            } else {
                rightPtrRotate(node);
                // Trouble node is left pointer of inserted node after rotations
                System.out.println("Imbalance condition occurred at inserting ISBN " + node.leftPtr + ": RR case fixed at ISBN " + node.bookObject.id);
            }
        }
    }


    private void preOrderHelper(AVLNode node) {
        if (node != null) {
            System.out.print(node.bookObject.id + " ");
            preOrderHelper(node.leftPtr);
            preOrderHelper(node.rightPtr);
        }
    }

    private void inOrderHelper(AVLNode node) {
        if (node != null) {
            inOrderHelper(node.leftPtr);
            System.out.print(node.bookObject.id + " ");
            inOrderHelper(node.rightPtr);
        }
    }

    private void postOrderHelper(AVLNode node) {
        if (node != null) {
            postOrderHelper(node.leftPtr);
            postOrderHelper(node.rightPtr);
            System.out.print(node.bookObject.id + " ");
        }
    }

    // Pre-Order traversal
    // Node.Left Subtree.Right Subtree
    public void preorder() {
        preOrderHelper(this.root);
    }

    // In-Order traversal
    // Left Subtree . Node . Right Subtree
    public void inorder() {
        inOrderHelper(this.root);
    }

    // Post-Order traversal
    // Left Subtree . Right Subtree . Node
    public void postorder() {
        postOrderHelper(this.root);
    }

    // search the tree for the key k
    // and return the corresponding node
    public AVLNode searchTree(Long k) {
        return searchTreeHelper(this.root, k);
    }

    // find the node with the minimum key
    public AVLNode minimum(AVLNode node) {
        while (node.leftPtr != null) {
            node = node.leftPtr;
        }
        return node;
    }

    // find the node with the maximum key
    public AVLNode maximum(AVLNode node) {
        while (node.rightPtr != null) {
            node = node.rightPtr;
        }
        return node;
    }

    // find the successor of a given node
    public AVLNode successor(AVLNode x) {
        // if the rightPtr subtree is not null,
        // the successor is the leftPtrmost node in the
        // rightPtr subtree
        if (x.rightPtr != null) {
            return minimum(x.rightPtr);
        }

        // else it is the lowest ancestor of x whose
        // leftPtr child is also an ancestor of x.
        AVLNode y = x.parent;
        while (y != null && x == y.rightPtr) {
            x = y;
            y = y.parent;
        }
        return y;
    }

    // find the predecessor of a given node
    public AVLNode predecessor(AVLNode x) {
        // if the leftPtr subtree is not null,
        // the predecessor is the rightPtrmost node in the
        // leftPtr subtree
        if (x.leftPtr != null) {
            return maximum(x.leftPtr);
        }

        AVLNode y = x.parent;
        while (y != null && x == y.leftPtr) {
            x = y;
            y = y.parent;
        }

        return y;
    }

    // rotate leftPtr at node x
    void leftPtrRotate(AVLNode x) {
        AVLNode y = x.rightPtr;
        x.rightPtr = y.leftPtr;
        if (y.leftPtr != null) {
            y.leftPtr.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.leftPtr) {
            x.parent.leftPtr = y;
        } else {
            x.parent.rightPtr = y;
        }
        y.leftPtr = x;
        x.parent = y;

        // update the balance factor
        x.bf = x.bf - 1 - Math.max(0, y.bf);
        y.bf = y.bf - 1 + Math.min(0, x.bf);
    }

    // rotate rightPtr at node x
    void rightPtrRotate(AVLNode x) {
        AVLNode y = x.leftPtr;
        x.leftPtr = y.rightPtr;
        if (y.rightPtr != null) {
            y.rightPtr.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.rightPtr) {
            x.parent.rightPtr = y;
        } else {
            x.parent.leftPtr = y;
        }
        y.rightPtr = x;
        x.parent = y;

        // update the balance factor
        x.bf = x.bf + 1 - Math.min(0, y.bf);
        y.bf = y.bf + 1 + Math.max(0, x.bf);
    }


    // insert the key to the tree in its appropriate position
    public void insert(Book book) {
        // PART 1: Ordinary BST insert
        AVLNode node = new AVLNode(book);
        AVLNode y = null;
        AVLNode x = this.root;

        while (x != null) {
            y = x;
            if (node.bookObject.id < x.bookObject.id) {
                x = x.leftPtr;
            } else {
                x = x.rightPtr;
            }
        }

        // y is parent of x
        node.parent = y;
        if (y == null) {
            root = node;
        } else if (node.bookObject.id < y.bookObject.id) {
            y.leftPtr = node;
        } else {
            y.rightPtr = node;
        }

        // PART 2: re-balance the node if necessary
        updateBalance(node);
    }

    // delete the node from the tree
    AVLNode deleteNode(Long data) {
        return deleteNodeHelper(this.root, data);
    }

    // print the tree structure on the screen
    public void prettyPrint() {
        printHelper(this.root, "", true);
    }

    public static void main(String [] args) throws FileNotFoundException {
        AVLTree bst = new AVLTree();
        BufferedReader fileReader = new BufferedReader(new FileReader("book.txt")); // Filename may need to be changed depending on IDE
        //Boolean is used to make sure we are still within the file
        boolean checkFile = true;
        while(checkFile) {
            try {
                String data = fileReader.readLine();
                if (data == null) {
                    checkFile = false;
                } else {
                    // Takes in the values from the file and adds them all to the AVLNode
                    Long titleId = Long.parseLong(data); // Value had to become Long due to range
                    String title = fileReader.readLine();
                    String author = fileReader.readLine();
                    Book newBook = new Book(titleId, title, author);
                    bst.insert(newBook);
                }
            } catch (Exception e) {
                checkFile = false;
            }
        }
        bst.prettyPrint();
    }
}
