package eg.edu.alexu.csd.filestructure.redblacktree;

import java.util.ArrayList;

import org.junit.Assert;
public class RedBlackTree<T extends Comparable<T>, V>  implements IRedBlackTree<T, V> {

    private INode<T, V> nil = new Node<T,V>();
    private INode<T, V> root = nil ;
    private ArrayList<T> inOrderTraverse = new ArrayList<T>();


    public RedBlackTree() {
        root.setLeftChild(nil);
        root.setRightChild(nil);
        root.setParent(nil);
    }


    private boolean isNil(INode<T, V> node){
        return (node == nil);
    }

    @Override
    public INode<T, V> getRoot() {
        return this.root;
    }

    @Override
    public boolean isEmpty() {
        return (this.root==nil);
    }

    @Override
    public void clear() {
        this.root.setLeftChild(nil);
        this.root.setRightChild(nil);
        this.root.setParent(nil);
        this.root = nil ;
    }

    @Override
    public V search(T key) {
        INode<T, V> current = root;
        while (!isNil(current)){
            if (current.getKey().equals(key))
                return current.getValue();
            else if (current.getKey().compareTo(key) < 0)
                current = current.getRightChild();
            else
                current = current.getLeftChild();
        }
        return null;
    }

    public   INode<T, V>  searchToGetNode(T key) {
        INode<T, V> current = root;
        while (!isNil(current)){
            if (current.getKey().equals(key))
                return current;
            else if (current.getKey().compareTo(key) < 0)
                current = current.getRightChild();
            else
                current = current.getLeftChild();
        }
        return nil;
    }

    @Override
    public boolean contains(T key) {
        V v = this.search(key);
        return (v != null);
    }


    private void leftRotate(INode<T, V> x){

        INode<T, V> y;
        y = x.getRightChild();
        x.setRightChild(y.getLeftChild());

        // Check for existence of y.left and make pointer changes
        if (!isNil(y.getLeftChild()))
            y.getLeftChild().setParent(x);
        y.setParent(x.getParent());

        // x's parent is nul
        if (isNil(x.getParent()))
            root = y;

            // x is the left child of it's parent
        else if (x.getParent().getLeftChild() == x)
            x.getParent().setLeftChild(y);

            // x is the right child of it's parent.
        else
            x.getParent().setRightChild(y);

        // Finish of the leftRotate
        y.setLeftChild(x);
        x.setParent(y);
    }

    private void rightRotate(INode<T, V> y){

        INode<T, V>  x = y.getLeftChild();
        y.setLeftChild(x.getRightChild());

        // Check for existence of x.right
        if (!isNil(x.getRightChild()))
            x.getRightChild().setParent(y);
        x.setParent(y.getParent());

        // y.parent is nil
        if (isNil(y.getParent()))
            root = x;

            // y is a right child of it's parent.
        else if (y.getParent().getRightChild() == y)
            y.getParent().setRightChild(x);

            // y is a left child of it's parent.
        else
            y.getParent().setLeftChild(x);

        x.setRightChild(y);
        y.setParent(x);

    }

    @Override
    public void insert(T key, V value) {

        INode<T, V> z = new Node<T,V>();
        z.setKey(key);
        z.setValue(value);
        z.setLeftChild(nil);
        z.setRightChild(nil);
        z.setColor(Node.RED);
        INode<T, V> y = nil;
        INode<T, V>  x = root;


        while (!isNil(x)){
            y = x;
            if (z.getKey().compareTo(x.getKey()) < 0){
                x = x.getLeftChild();
            }
            else if (z.getKey().compareTo(x.getKey()) > 0){
                x = x.getRightChild();
            }
            else
                return ;
        }
        z.setParent(y);

        if (isNil(y))
            root = z;
        else if (key.compareTo(y.getKey()) < 0)
            y.setLeftChild(z);
        else
            y.setRightChild(z);

        insertFixup(z);

    }
    private void insertFixup(INode<T, V> z){

        INode<T, V> y = nil;

        while (z.getParent().getColor()){

            if (z.getParent() == z.getParent().getParent().getLeftChild()){
                y = z.getParent().getParent().getRightChild();
                // Case 1: if y is red...recolor
                if (y.getColor()){
                    z.getParent().setColor(Node.BLACK);
                    y.setColor(Node.BLACK);
                    z.getParent().getParent().setColor(Node.RED);
                    z = z.getParent().getParent();
                }
                // Case 2: if y is black & z is a right child
                else if (z == z.getParent().getRightChild()){
                    // leftRotaet around z's parent
                    z = z.getParent();
                    leftRotate(z);
                }
                // Case 3: else y is black & z is a left child
                else{
                    z.getParent().setColor(Node.BLACK);
                    z.getParent().getParent().setColor(Node.RED);
                    rightRotate(z.getParent().getParent());
                }
            }

            // If z's parent is the right child of it's parent.
            else{
                y = z.getParent().getParent().getLeftChild();

                // Case 1: if y is red...recolor
                if (y.getColor()){
                    z.getParent().setColor(Node.BLACK);
                    y.setColor(Node.BLACK);
                    z.getParent().getParent().setColor(Node.RED);
                    z = z.getParent().getParent();
                }
                // Case 2: if y is black & z is a right child
                else if (z == z.getParent().getRightChild()){
                    // leftRotaet around z's parent
                    z = z.getParent();
                    leftRotate(z);
                }
                // Case 3: else y is black & z is a left child
                else{
                    z.getParent().setColor(Node.BLACK);
                    z.getParent().getParent().setColor(Node.RED);
                    rightRotate(z.getParent().getParent());
                }
            }
        }
        root.setColor(Node.BLACK);

    }

    public  INode<T, V> treeMinimum( INode<T, V> node){

        // while there is a smaller key, keep going left
        while (!isNil(node.getLeftChild()))
            node = node.getLeftChild();
        return node;
    }
    public  INode<T, V> treeSuccessor( INode<T, V> x){

        // if x.left is not nil, call treeMinimum(x.right) and
        // return it's value
        if (!isNil(x.getLeftChild()) )
            return treeMinimum(x.getRightChild());

        INode<T, V> y = x.getParent();

        // while x is it's parent's right child...
        while (!isNil(y) && x == y.getRightChild()){
            // Keep moving up in the tree
            x = y;
            y = y.getParent();
        }
        return y;
    }

    @Override
    public boolean delete(T key) {


        INode<T, V> z = searchToGetNode(key);
        if(z == nil)
            return false ;

        INode<T, V> x = nil;
        INode<T, V> y = nil;

        // if either one of z's children is nil, then remove z
        if (isNil(z.getLeftChild()) || isNil(z.getRightChild()))
            y = z;

            // else we must remove the successor of z
        else y = treeSuccessor(z);

        // Let x be the left or right child of y (y can only have one child)
        if (!isNil(y.getLeftChild()))
            x = y.getLeftChild();
        else
            x = y.getRightChild();

        // link x's parent to y's parent
        x.setParent(y.getParent());

        // If y's parent is nil, then x is the root
        if (isNil(y.getParent()))
            root = x;

            // else if y is a left child, set x to be y's left sibling
        else if (!isNil(y.getParent().getLeftChild()) && y.getParent().getLeftChild() == y)
            y.getParent().setLeftChild(x);

            // else if y is a right child, set x to be y's right sibling
        else if (!isNil(y.getParent().getRightChild()) && y.getParent().getRightChild() == y)
            y.getParent().setRightChild(x);

        // if y != z, trasfer y's satellite data into z.
        if (y != z){
            z.setKey(y.getKey());
        }

        // If y's color is black, it is a violation of the
        // RedBlackTree properties so call removeFixup()
        if (!y.getColor())
            removeFixup(x);

        return true ;


    }

    private void removeFixup(INode<T, V> x){

        INode<T, V> w;

        // While we haven't fixed the tree completely...
        while (x != root && !x.getColor()){

            // if x is it's parent's left child
            if (x == x.getParent().getLeftChild()){

                // set w = x's sibling
                w = x.getParent().getRightChild();

                // Case 1, w's color is red.
                if (w.getColor()){
                    w.setColor(Node.BLACK);
                    x.getParent().setColor(Node.RED);
                    leftRotate(x.getParent());
                    w = x.getParent().getRightChild();
                }

                // Case 2, both of w's children are black
                if (!w.getLeftChild().getColor() && !w.getRightChild().getColor()){
                    w.setColor(Node.RED);
                    x = x.getParent();
                }
                // Case 3 / Case 4
                else{
                    // Case 3, w's right child is black
                    if (!w.getRightChild().getColor()){
                        w.getLeftChild().setColor(Node.BLACK);
                        w.setColor(Node.RED);
                        rightRotate(w);
                        w = x.getParent().getRightChild();
                    }
                    // Case 4, w = black, w.right = red
                    w.setColor(x.getParent().getColor());
                    x.getParent().setColor(Node.BLACK);
                    w.getRightChild().setColor(Node.BLACK);
                    leftRotate(x.getParent());
                    x = root;
                }
            }
            // if x is it's parent's right child
            else{

                // set w to x's sibling
                w = x.getParent().getLeftChild();

                // Case 1, w's color is red.
                if (w.getColor()){
                    w.setColor(Node.BLACK);
                    x.getParent().setColor(Node.RED);
                    leftRotate(x.getParent());
                    w = x.getParent().getRightChild();
                }

                // Case 2, both of w's children are black
                if (!w.getLeftChild().getColor() && !w.getRightChild().getColor()){
                    w.setColor(Node.RED);
                    x = x.getParent();
                }
                // Case 3 / Case 4
                else{
                    // Case 3, w's right child is black
                    if (!w.getRightChild().getColor()){
                        w.getLeftChild().setColor(Node.BLACK);
                        w.setColor(Node.RED);
                        rightRotate(w);
                        w = x.getParent().getRightChild();
                    }
                    // Case 4, w = black, w.right = red
                    w.setColor(x.getParent().getColor());
                    x.getParent().setColor(Node.BLACK);
                    w.getRightChild().setColor(Node.BLACK);
                    leftRotate(x.getParent());
                    x = root;
                }

            }
        }
    }// end while

    private void inOrder(INode node) {
        if (node == nil) {
            return;
        }
        inOrder(node.getLeftChild());
        this.inOrderTraverse.add((T) node.getKey());
        inOrder(node.getRightChild());
    }

    public ArrayList<T> getTraverseList(INode root)
    {
        this.inOrder(root);
        return this.inOrderTraverse ;
    }




}
