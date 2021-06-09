package eg.edu.alexu.csd.filestructure.redblacktree;

import javax.management.RuntimeErrorException;

public class RedBlackTree<T extends Comparable<T>, V> implements IRedBlackTree<T,V> {
    private INode<T,V> basic_root;
    private T prev_key;

    public RedBlackTree(){
        basic_root = null;
        prev_key = null;
    }

    @Override
    public INode<T,V> getRoot() {
        return basic_root;
    }

    @Override
    public boolean isEmpty() {
        if (basic_root == null || basic_root.isNull())
            return true;
        else
            return false;
    }

    @Override
    public void clear() {
        basic_root = null;
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
        INode<T,V> exist = get_by_key(key);
        if (exist != null && !exist.isNull())
            exist.setValue(value);
        else {
            binary_insert(getRoot(), key, value, new Node());
            INode last = get_by_key(prev_key);
            fix_color(last);
        }
        return;
    }



   private INode<T,V> binary_insert(INode<T,V> root, T key, V value, INode<T,V> parent) {
        if (root == null || root.isNull() ) {
            if(root == null)
                root = new Node();
            if (getRoot() == null || getRoot().isNull()) {
                root.setColor(false);
                basic_root = root;
            }else
                root.setColor(true);

            root.setKey(key);
            root.setValue(value);
            root.setParent(parent);
            prev_key = root.getKey();
            INode<T,V> l = new Node<>();
            l.setKey(null);
            root.setLeftChild(l);
            INode<T,V> r = new Node<>();
            r.setKey(null);
            root.setRightChild(r);
            return root;
        }
        if (key.compareTo(root.getKey()) < 1) {
            root.setLeftChild(binary_insert(root.getLeftChild(), key, value, root));
        } else if (key.compareTo(root.getKey()) == 1) {
            root.setRightChild(binary_insert(root.getRightChild(), key, value, root));
        }
        return root;
    }

    private void fix_color (INode<T,V> node) {
        INode<T,V> x = node, parent, grand, uncle;
        if (x!=null && !x.isNull() && x.getColor() == true){
            parent = x.getParent();
            if (!parent.isNull() && !parent.isNull()) {
                grand = parent.getParent();
                if (!grand.isNull() && !grand.isNull()) {
                    if (grand.getLeftChild() != null && !grand.getLeftChild().isNull() && parent.getKey().compareTo(grand.getLeftChild().getKey()) == 0)
                        uncle = grand.getRightChild();
                    else
                        uncle = grand.getLeftChild();
                    if (uncle != null && !uncle.isNull() && uncle.getColor() == true && parent.getColor() == true)  {
                        parent.setColor(false);
                        uncle.setColor(false);
                        grand.setColor(true);
                        fix_color(grand);
                    }
                    else if(parent.getColor() == true) {
                        INode<T,V> New = UncleBlack(x, parent, grand, uncle);
                        fix_color(New);
                    }
                }
            } else
                x.setColor(false);
        }
        return;
    }

    private INode<T,V> UncleBlack(INode<T,V> x, INode<T,V> parent, INode<T,V> grand, INode<T,V> u) {
        if (grand.getLeftChild() != null && !grand.getLeftChild().isNull() && parent.getKey().compareTo(grand.getLeftChild().getKey()) == 0) {
            if (parent.getLeftChild() != null && !parent.getLeftChild().isNull() && x.getKey().compareTo(parent.getLeftChild().getKey()) == 0) {
                //LeftLeft
                rotate_right(grand);
                boolean temp = parent.getColor();
                parent.setColor(grand.getColor());
                grand.setColor(temp);
                return parent;
            } else {
                //LeftRight
                rotate_left(parent);
                rotate_right(grand);
                boolean temp = x.getColor();
                x.setColor(grand.getColor());
                grand.setColor(temp);
                return x;
            }
        } else {
            if (parent.getLeftChild() != null && !parent.getLeftChild().isNull() && x.getKey().compareTo(parent.getLeftChild().getKey()) == 0) {
                //RightLeft
                rotate_right(parent);
                rotate_left(grand);
                boolean temp = x.getColor();
                x.setColor(grand.getColor());
                grand.setColor(temp);
                return x;
            } else {
                //RightRight
                rotate_left(grand);
                boolean temp = parent.getColor();
                parent.setColor(grand.getColor());
                grand.setColor(temp);
                return parent;
            }
        }
    }

    private void rotate_left(INode<T,V> node) {
        INode<T,V> grand = node.getParent();
        INode<T,V> right = node.getRightChild();
        INode<T,V> left = right.getLeftChild();
        // Perform rotation
        right.setLeftChild(node);
        node.setRightChild(left);
        if (grand==null || grand.isNull())
            basic_root = right;
        else {
            if (grand.getLeftChild() != null && !grand.getLeftChild().isNull() && node.getKey().compareTo(grand.getLeftChild().getKey()) == 0)
                grand.setLeftChild(right);
            else
                grand.setRightChild(right);
        }
        node.setParent(right);
        if (left != null && !left.isNull())
            left.setParent(node);
        right.setParent(grand);
        return;
    }

    private void rotate_right(INode<T,V> node) {
        INode<T,V> b = node.getLeftChild();
        INode<T,V> c = b.getRightChild();
        // Perform rotation
        b.setRightChild(node);
        node.setLeftChild(c);
        INode<T,V> grand = node.getParent();
        if (grand == null || grand.isNull())
            basic_root = b;
        else {
            if ( grand.getLeftChild() != null && !grand.getLeftChild().isNull() && node.getKey().compareTo(grand.getLeftChild().getKey()) == 0)
                grand.setLeftChild(b);
            else
                grand.setRightChild(b);
        }
        node.setParent(b);
        if (c != null && !c.isNull())
            c.setParent(node);
        b.setParent(grand);
        return;
    }

    @Override
    public boolean delete(T key) {
        if(key==null)
            throw new RuntimeErrorException(null);
        boolean result= delete_node(getRoot(),key);
        return result;
    }

    private boolean delete_node(INode<T,V> root, T key){
        if( root.isNull())
            return false;
        else if(key.compareTo(root.getKey())<0)
            delete_node(root.getLeftChild(),key);
        else if(key.compareTo(root.getKey())>0)
            delete_node(root.getRightChild(),key);
        else  {
            //case1 no children
            if((root.getLeftChild().isNull())&&(root.getRightChild().isNull())){
                if(root==getRoot()) {
                    basic_root.setKey(null);
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
                delete_node(temp,temp.getKey());

            }
            else if(root.getRightChild().isNull()){
                INode<T,V> temp=root.getLeftChild();
                root.setKey(temp.getKey());
                root.setValue(temp.getValue());
                delete_node(temp,temp.getKey());
            }
            else{
                INode<T,V> temp=FindMin(root.getRightChild());
                root.setKey(temp.getKey());
                root.setValue(temp.getValue());
                delete_node(root.getRightChild(),temp.getKey());
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
        else if((Slibbing(DB).isNull())||((isBlackNode(Slibbing(DB)))&&(isBlackNode(Slibbing(DB).getRightChild()))&&(isBlackNode(Slibbing(DB).getLeftChild())))){
            //System.out.println("Case3");
            if(!Slibbing(DB).isNull())
                Slibbing(DB).setColor(true);
            if(isBlackNode(DB.getParent()))
                DoubleBlack(DB.getParent());
            else
                DB.getParent().setColor(false);
        }
        //case4
        else if(!isBlackNode(Slibbing(DB))){
            //System.out.println("Case4");
            SwapColors(Slibbing(DB),DB.getParent());
            if(isLeftChild(DB))
                rotate_left(DB.getParent());
            else
                rotate_right(DB.getParent());
            DoubleBlack(DB);
        }
        //case5
        else if((isBlackNode(Slibbing(DB)))&&(isBlackNode(farDB(DB)))&&(!isBlackNode(nearDB(DB)))){
            //System.out.println("Case5");
            SwapColors(Slibbing(DB),nearDB(DB));
            if(isLeftChild(DB))
                rotate_right(Slibbing(DB));
            else
                rotate_left(Slibbing(DB));
            Case6(DB);
        }
        else
            Case6(DB);

    }

    private void Case6(INode<T,V> DB){
        //System.out.println("Case6");
        if(isBlackNode(Slibbing(DB))&&(!isLeftChild(DB))&&(!isBlackNode(Slibbing(DB).getLeftChild()))){
            SwapColors(DB.getParent(),Slibbing(DB));
            Slibbing(DB).getLeftChild().setColor(false);
            if(isLeftChild(DB))
                rotate_left(DB.getParent());
            else
                rotate_right(DB.getParent());
        }
        else if(isBlackNode(Slibbing(DB))&&(isLeftChild(DB))&&(!isBlackNode(Slibbing(DB).getRightChild()))){
            SwapColors(DB.getParent(),Slibbing(DB));
            Slibbing(DB).getRightChild().setColor(false);
            if(isLeftChild(DB))
                rotate_left(DB.getParent());
            else
                rotate_right(DB.getParent());
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
            near=Slibbing(DB).getLeftChild();
        else
            near=Slibbing(DB).getRightChild();
        return near;
    }

    private INode<T,V> farDB(INode<T,V> DB){
        INode far;
        if(isLeftChild(DB))
            far=Slibbing(DB).getRightChild();
        else
            far=Slibbing(DB).getLeftChild();
        return far;
    }

    private INode<T,V> Slibbing(INode<T,V> DB){
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
