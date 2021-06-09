package eg.edu.alexu.csd.filestructure.redblacktree;

import javax.management.RuntimeErrorException;

public class RedBlackTree<T extends Comparable<T>, V> implements IRedBlackTree<T,V> {
	
    private INode<T,V> root = new Node();
    private T lastkey = null;
    int count = 1;


    @Override
    public INode<T,V> getRoot() {
        return root;
    }

    @Override
    public boolean isEmpty() {
        if (root==null || root.isNull())
        	return true;
        else 
        	return false ;
    }

    @Override
    public void clear() {
        this.root=null;
    }

    @Override
    public V search(T key) {
        INode<T,V> root = getRoot();
        if (key == null)
            throw new RuntimeErrorException(null);
        while (root != null && !root.isNull()) {
            if (key.compareTo(root.getKey()) == 0)
                return root.getValue();
            else if (key.compareTo(root.getKey()) < 0)
                root = root.getLeftChild();
            else if (key.compareTo(root.getKey()) > 0)
                root = root.getRightChild();
        }
        return null;
    }

    @Override
    public boolean contains(T key) {
       V v = this.search(key) ;
       return v!=null ;
    }

    @Override
    public void insert(T key, V value) {
        if(key==null || value==null)
            throw new RuntimeErrorException(null);
        INode available = getByKey(key);
        if (available != null && !available.isNull())
            available.setValue(value);
        else {
            secInsert(getRoot(), key, value, new Node());
            INode last = getByKey(lastkey);
            count++;
            Coloring(last);
        }
        return;
    }

    private INode<T,V> getByKey(Comparable key) {
        INode root = getRoot();
        while (root != null && !root.isNull()) {
            if (key.compareTo(root.getKey()) == 0)
                return root;
            else if (key.compareTo(root.getKey()) < 0)
                root = root.getLeftChild();
            else if (key.compareTo(root.getKey()) > 0)
                root = root.getRightChild();
        }
        return null;
    }

   private INode<T,V> secInsert(INode<T,V> root, T key, V value, INode<T,V> parent) {
        if (root == null || root.isNull() ) {
            if(root == null)
                root = new Node();

            if (getRoot() == null || getRoot().isNull()) {
                root.setColor(false);
                root = root;
            }
            else
                root.setColor(true);

            root.setKey(key);
            root.setValue(value);
            root.setParent(parent);
            lastkey = root.getKey();
            INode l = new Node();
            l.setKey(null);
            root.setLeftChild(l);
            INode r = new Node();
            r.setKey(null);
            root.setRightChild(r);
            return root;
        }

        if (key.compareTo(root.getKey()) < 1) {
            root.setLeftChild(secInsert(root.getLeftChild(), key, value, root));
        } else if (key.compareTo(root.getKey()) == 1) {
            root.setRightChild(secInsert(root.getRightChild(), key, value, root));
        }
        return root;
    }

    private void Coloring(INode<T,V> node) {
        INode<T,V> x = node;
        INode<T,V> p = null;
        INode<T,V> g = null;
        INode<T,V> u = null;
        INode<T,V> t = null;
        if (x!=null && !x.isNull() && x.getColor() == true) {
            p = x.getParent();
            if (!p.isNull() && !p.isNull()) {
                g = p.getParent();
                if (!g.isNull() && !g.isNull()) {
                    if (g.getLeftChild() != null && !g.getLeftChild().isNull() && p.getKey().compareTo(g.getLeftChild().getKey()) == 0)
                        u = g.getRightChild();
                    else
                        u = g.getLeftChild();
                    if (u != null && !u.isNull() && u.getColor() == true && p.getColor() == true)  {
                        p.setColor(false);
                        u.setColor(false);
                        g.setColor(true);
                        Coloring(g);
                    }
                    else if(p.getColor() == true) {
                        INode New = UncleBlack(x, p, g, u);
                        Coloring(New);
                    }
                }
            } else
                x.setColor(false);
        }
        return;
    }

    private INode<T,V> UncleBlack(INode<T,V> x, INode<T,V> p, INode<T,V> g, INode<T,V> u) {
        if (g.getLeftChild() != null && !g.getLeftChild().isNull() && p.getKey().compareTo(g.getLeftChild().getKey()) == 0) {
            if (p.getLeftChild() != null && !p.getLeftChild().isNull() && x.getKey().compareTo(p.getLeftChild().getKey()) == 0) {
                //LeftLeft
                RotateRight(g);
                boolean temp = p.getColor();
                p.setColor(g.getColor());
                g.setColor(temp);
                return p;
            } else {
                //LeftRight
                RotateLeft(p);
                RotateRight(g);
                boolean temp = x.getColor();
                x.setColor(g.getColor());
                g.setColor(temp);
                return x;
            }
        } else {
            if (p.getLeftChild() != null && !p.getLeftChild().isNull() && x.getKey().compareTo(p.getLeftChild().getKey()) == 0) {
                //RightLeft
                RotateRight(p);
                RotateLeft(g);
                boolean temp = x.getColor();
                x.setColor(g.getColor());
                g.setColor(temp);
                return x;
            } else {
                //RightRight
                RotateLeft(g);
                boolean temp = p.getColor();
                p.setColor(g.getColor());
                g.setColor(temp);
                return p;
            }
        }
    }

    private void RotateLeft(INode<T,V> a) {
        INode<T,V> grand = a.getParent();
        INode<T,V> b = a.getRightChild();
        INode<T,V> c = b.getLeftChild();
        // Perform rotation
        b.setLeftChild(a);
        a.setRightChild(c);
        if (grand==null || grand.isNull())
            root = b;
        else {
            if (grand.getLeftChild() != null && !grand.getLeftChild().isNull() && a.getKey().compareTo(grand.getLeftChild().getKey()) == 0)
                grand.setLeftChild(b);
            else
                grand.setRightChild(b);
        }
        a.setParent(b);
        if (c != null && !c.isNull())
            c.setParent(a);
        b.setParent(grand);
        return;
    }

    private void RotateRight(INode<T,V> a) {
        INode<T,V> b = a.getLeftChild();
        INode<T,V> c = b.getRightChild();
        // Perform rotation
        b.setRightChild(a);
        a.setLeftChild(c);
        INode<T,V> grand = a.getParent();
        if (grand == null || grand.isNull())
            root = b;
        else {
            if ( grand.getLeftChild() != null && !grand.getLeftChild().isNull() && a.getKey().compareTo(grand.getLeftChild().getKey()) == 0)
                grand.setLeftChild(b);
            else
                grand.setRightChild(b);
        }
        a.setParent(b);
        if (c != null && !c.isNull())
            c.setParent(a);
        b.setParent(grand);
        return;
    }

    @Override
    public boolean delete(T key) {
        if(key==null)
            throw new RuntimeErrorException(null);
        boolean result=Delete(getRoot(),key);
        return result;
    }

    private boolean Delete(INode<T,V> root,T key){
        if( root.isNull())
            return false;
        else if(key.compareTo(root.getKey())<0)
            Delete(root.getLeftChild(),key);
        else if(key.compareTo(root.getKey())>0)
            Delete(root.getRightChild(),key);
        else  {
            //case1 no children
            if((root.getLeftChild().isNull())&&(root.getRightChild().isNull())){
                if(root==getRoot()) {
                    root.setKey(null);
                    removeNode(root);
                }
                else {
                    if (isBlackNode(root))
                        DoubleBlack(root);
                    removeNode(root);
                }
            }
            //case2 one child
            else if(root.getLeftChild().isNull()){
                INode<T,V> temp=root.getRightChild();
                root.setKey(temp.getKey());
                root.setValue(temp.getValue());
                Delete(temp,temp.getKey());

            }
            else if(root.getRightChild().isNull()){
                INode<T,V> temp=root.getLeftChild();
                root.setKey(temp.getKey());
                root.setValue(temp.getValue());
                Delete(temp,temp.getKey());
            }
            else{
                INode<T,V> temp=FindMin(root.getRightChild());
                root.setKey(temp.getKey());
                root.setValue(temp.getValue());
                Delete(root.getRightChild(),temp.getKey());
            }
        }
        return true;
    }

    private void removeNode(INode<T,V> node){
        node.setKey(null);
        node.setValue(null);
        node.setColor(false);
        node.setRightChild(null);
        node.setLeftChild(null);
    }

    private INode<T,V> FindMin(INode<T,V> r){
        INode<T,V> min=r;
        r=r.getLeftChild();
        while(!r.isNull()){
            if(r.getKey().compareTo(min.getKey())<0)
                min=r;
            r=r.getLeftChild();
        }
        return min;
    }

    private boolean isLeftChild(INode<T,V> r){
        if(r.getParent().getLeftChild()==r)
            return true;
        else
            return false;
    }

    private void DoubleBlack(INode<T,V> DB){
        //case2
        if(DB==getRoot())
            return;

            //case3
        else if((sibbling(DB).isNull())||((isBlackNode(sibbling(DB)))&&(isBlackNode(sibbling(DB).getRightChild()))&&(isBlackNode(sibbling(DB).getLeftChild())))){
            //System.out.println("Case3");
            if(!sibbling(DB).isNull())
                sibbling(DB).setColor(true);
            if(isBlackNode(DB.getParent()))
                DoubleBlack(DB.getParent());
            else
                DB.getParent().setColor(false);
        }
        //case4
        else if(!isBlackNode(sibbling(DB))){
            //System.out.println("Case4");
            SwapColors(sibbling(DB),DB.getParent());
            if(isLeftChild(DB))
                RotateLeft(DB.getParent());
            else
                RotateRight(DB.getParent());
            DoubleBlack(DB);
        }
        //case5
        else if((isBlackNode(sibbling(DB)))&&(isBlackNode(farDB(DB)))&&(!isBlackNode(nearDB(DB)))){
            //System.out.println("Case5");
            SwapColors(sibbling(DB),nearDB(DB));
            if(isLeftChild(DB))
                RotateRight(sibbling(DB));
            else
                RotateLeft(sibbling(DB));
            Case6(DB);
        }
        else
            Case6(DB);

    }

    private void Case6(INode<T,V> DB){
        //System.out.println("Case6");
        if(isBlackNode(sibbling(DB))&&(!isLeftChild(DB))&&(!isBlackNode(sibbling(DB).getLeftChild()))){
            SwapColors(DB.getParent(),sibbling(DB));
            sibbling(DB).getLeftChild().setColor(false);
            if(isLeftChild(DB))
                RotateLeft(DB.getParent());
            else
                RotateRight(DB.getParent());
        }
        else if(isBlackNode(sibbling(DB))&&(isLeftChild(DB))&&(!isBlackNode(sibbling(DB).getRightChild()))){
            SwapColors(DB.getParent(),sibbling(DB));
            sibbling(DB).getRightChild().setColor(false);
            if(isLeftChild(DB))
                RotateLeft(DB.getParent());
            else
                RotateRight(DB.getParent());
        }
        else
            return;
    }

    private boolean isBlackNode(INode<T,V> node){
        if(node.isNull())
            return true;
        else if(!node.getColor())
            return true;
        else
            return false;
    }

    private INode<T,V> nearDB(INode<T,V> DB){
        INode near;
        if(isLeftChild(DB))
            near=sibbling(DB).getLeftChild();
        else
            near=sibbling(DB).getRightChild();
        return near;
    }

    private INode<T,V> farDB(INode<T,V> DB){
        INode far;
        if(isLeftChild(DB))
            far=sibbling(DB).getRightChild();
        else
            far=sibbling(DB).getLeftChild();
        return far;
    }

    private INode<T,V> sibbling(INode<T,V> DB){
        INode slib;
        if(DB.getParent().isNull())
            return null;
        if(isLeftChild(DB))
            slib=DB.getParent().getRightChild();
        else
            slib=DB.getParent().getLeftChild();
        return slib;
    }

    private void SwapColors(INode<T,V> node1,INode<T,V> node2){
        boolean temp=node1.getColor();
        node1.setColor(node2.getColor());
        node2.setColor(temp);
    }

}
