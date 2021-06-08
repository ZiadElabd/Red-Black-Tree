package eg.edu.alexu.csd.filestructure.redblacktree;

public class Node<T extends Comparable<T> ,V> implements INode<T,V>{

    private INode<T, V> left ;
    private INode<T, V> right ;
    private INode<T, V> parent ;

    private T key ;
    private V val ;

    private boolean color ;

    public Node()
    {
        this.color = Node.BLACK ;
    }

    @Override
    public void setParent(INode<T, V> parent) {
        // TODO Auto-generated method stub
        this.parent = parent ;
    }

    @Override
    public INode<T, V> getParent() {
        // TODO Auto-generated method stub
        return this.parent;
    }

    @Override
    public void setLeftChild(INode<T, V> leftChild) {
        // TODO Auto-generated method stub
        this.left = leftChild ;

    }

    @Override
    public INode<T, V> getLeftChild() {
        // TODO Auto-generated method stub
        return this.left;
    }

    @Override
    public void setRightChild(INode<T, V> rightChild) {
        // TODO Auto-generated method stub
        this.right = rightChild ;

    }

    @Override
    public INode<T, V> getRightChild() {
        // TODO Auto-generated method stub
        return this.right;
    }

    @Override
    public T getKey() {
        // TODO Auto-generated method stub
        return this.key;
    }

    @Override
    public void setKey(T key) {
        // TODO Auto-generated method stub
        this.key = key ;

    }

    @Override
    public V getValue() {
        // TODO Auto-generated method stub
        return this.val;
    }

    @Override
    public void setValue(V value) {
        // TODO Auto-generated method stub
        this.val = value ;

    }

    @Override
    public boolean getColor() {
        // TODO Auto-generated method stub
        return this.color;
    }

    @Override
    public void setColor(boolean color) {
        // TODO Auto-generated method stub
        this.color = color ;

    }

    @Override
    public boolean isNull() {
        // TODO Auto-generated method stub
        return (this.val == null);
    }



}
