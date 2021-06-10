package eg.edu.alexu.csd.filestructure.redblacktree;

import javax.management.RuntimeErrorException;

public class RedBlackTree<T extends Comparable<T>, V> implements IRedBlackTree<T,V> {
    private INode<T,V> main_root ;
    private T prev_key ;

    public RedBlackTree(){
        main_root = null;
        prev_key = null;
    }

    @Override
    public INode<T,V> getRoot() {
        return main_root;
    }

    @Override
    public boolean isEmpty() {
        if (main_root == null || main_root.isNull())
            return true;
        else
            return false;
    }

    @Override
    public void clear() {
        main_root = null;
    }

    private INode<T,V> get_by_key(T key) {
        INode<T,V> root = getRoot();
        while (root != null && !root.isNull()) {
            int comp = key.compareTo(root.getKey());
            if (comp == 0)
                return root;
            else if (comp < 0)
                root = root.getLeftChild();
            else if (comp > 0)
                root = root.getRightChild();
        }
        return null;
    }

    @Override
    public V search(T key) {
        if (key == null)
            throw new RuntimeErrorException(null);
        INode<T,V> node = get_by_key(key);
        if(node == null){
            return null;
        }
        return node.getValue();
    }

    @Override
    public boolean contains(T key) {
        if (key == null)
            throw new RuntimeErrorException(null);
        INode<T,V> node = get_by_key(key);
        if(node == null){
            return false;
        }
        return true;
    }


    @Override
    public void insert(T key, V value) {
        if(key==null || value==null)
            throw new RuntimeErrorException(null);
        INode<T,V> available = get_by_key(key);
        if (available != null && !available.isNull())
            available.setValue(value);
        else {
            BSinsert(getRoot(), key, value, new Node());
            INode<T,V> last = get_by_key(prev_key);
            Coloring(last);
        }
        return;
    }


    private INode<T,V> BSinsert(INode<T,V> root, T key, V value, INode<T,V> parent) {
        if (root == null || root.isNull() ) {
            if(root == null)
                root = new Node();

            if (getRoot() == null || getRoot().isNull()) {
                root.setColor(false);
                main_root = root;
            }
            else
                root.setColor(true);

            root.setKey(key);
            root.setValue(value);
            root.setParent(parent);
            prev_key = root.getKey();
            INode l = new Node();
            l.setKey(null);
            root.setLeftChild(l);
            INode r = new Node();
            r.setKey(null);
            root.setRightChild(r);
            return root;
        }

        if (key.compareTo(root.getKey()) < 1) {
            root.setLeftChild(BSinsert(root.getLeftChild(), key, value, root));
        } else if (key.compareTo(root.getKey()) == 1) {
            root.setRightChild(BSinsert(root.getRightChild(), key, value, root));
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
                        INode<T,V> New = UncleBlack(x, p, g, u);
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
                RightRotation(g);
                boolean temp = p.getColor();
                p.setColor(g.getColor());
                g.setColor(temp);
                return p;
            } else {
                //LeftRight
                LeftRotation(p);
                RightRotation(g);
                boolean temp = x.getColor();
                x.setColor(g.getColor());
                g.setColor(temp);
                return x;
            }
        } else {
            if (p.getLeftChild() != null && !p.getLeftChild().isNull() && x.getKey().compareTo(p.getLeftChild().getKey()) == 0) {
                //RightLeft
                RightRotation(p);
                LeftRotation(g);
                boolean temp = x.getColor();
                x.setColor(g.getColor());
                g.setColor(temp);
                return x;
            } else {
                //RightRight
                LeftRotation(g);
                boolean temp = p.getColor();
                p.setColor(g.getColor());
                g.setColor(temp);
                return p;
            }
        }
    }

    private void LeftRotation(INode<T,V> a) {
        INode<T,V> grand = a.getParent();
        INode<T,V> b = a.getRightChild();
        INode<T,V> c = b.getLeftChild();
        // Perform rotation
        b.setLeftChild(a);
        a.setRightChild(c);
        if (grand==null || grand.isNull())
            main_root = b;
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

    private void RightRotation(INode<T,V> a) {
        INode<T,V> b = a.getLeftChild();
        INode<T,V> c = b.getRightChild();
        // Perform rotation
        b.setRightChild(a);
        a.setLeftChild(c);
        INode<T,V> grand = a.getParent();
        if (grand == null || grand.isNull())
            main_root = b;
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
                    main_root.setKey(null);
                    set_node_null(root);
                }
                else {
                    if (isBlackNode(root))
                        DoubleBlack(root);
                    set_node_null(root);
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

    private void set_node_null(INode<T,V> node){
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
        else if((sibling(DB).isNull())||((isBlackNode(sibling(DB)))&&(isBlackNode(sibling(DB).getRightChild()))&&(isBlackNode(sibling(DB).getLeftChild())))){
            //System.out.println("Case3");
            if(!sibling(DB).isNull())
                sibling(DB).setColor(true);
            if(isBlackNode(DB.getParent()))
                DoubleBlack(DB.getParent());
            else
                DB.getParent().setColor(false);
        }
        //case4
        else if(!isBlackNode(sibling(DB))){
            //System.out.println("Case4");
            swap_color(sibling(DB),DB.getParent());
            if(isLeftChild(DB))
                LeftRotation(DB.getParent());
            else
                RightRotation(DB.getParent());
            DoubleBlack(DB);
        }
        //case5
        else if((isBlackNode(sibling(DB)))&&(isBlackNode(farDB(DB)))&&(!isBlackNode(nearDB(DB)))){
            //System.out.println("Case5");
            swap_color(sibling(DB),nearDB(DB));
            if(isLeftChild(DB))
                RightRotation(sibling(DB));
            else
                LeftRotation(sibling(DB));
            Case6(DB);
        }
        else
            Case6(DB);

    }

    private void Case6(INode<T,V> DB){
        //System.out.println("Case6");
        if(isBlackNode(sibling(DB))&&(!isLeftChild(DB))&&(!isBlackNode(sibling(DB).getLeftChild()))){
            swap_color(DB.getParent(), sibling(DB));
            sibling(DB).getLeftChild().setColor(false);
            if(isLeftChild(DB))
                LeftRotation(DB.getParent());
            else
                RightRotation(DB.getParent());
        }
        else if(isBlackNode(sibling(DB))&&(isLeftChild(DB))&&(!isBlackNode(sibling(DB).getRightChild()))){
            swap_color(DB.getParent(), sibling(DB));
            sibling(DB).getRightChild().setColor(false);
            if(isLeftChild(DB))
                LeftRotation(DB.getParent());
            else
                RightRotation(DB.getParent());
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
        INode<T,V> near;
        if(isLeftChild(DB))
            near= sibling(DB).getLeftChild();
        else
            near= sibling(DB).getRightChild();
        return near;
    }

    private INode<T,V> farDB(INode<T,V> DB){
        INode<T,V> far;
        if(isLeftChild(DB))
            far= sibling(DB).getRightChild();
        else
            far= sibling(DB).getLeftChild();
        return far;
    }

    private INode<T,V> sibling(INode<T,V> node){
        INode<T,V> parent = node.getParent();
        if(parent.isNull())
            return null;
        if(parent.getLeftChild() == node )
            return parent.getRightChild();
        else
            return parent.getLeftChild();
    }

    private void swap_color(INode<T,V> a, INode<T,V> b){
        boolean temp= a.getColor();
        a.setColor(b.getColor());
        b.setColor(temp);
    }

}