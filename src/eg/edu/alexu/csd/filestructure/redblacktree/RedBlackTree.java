package eg.edu.alexu.csd.filestructure.redblacktree;

import javax.management.RuntimeErrorException;

public class RedBlackTree<T extends Comparable<T>, V> implements IRedBlackTree<T,V> {
    private INode<T,V> main_root;
    private T prev_key;

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
        if(key==null || value==null)  throw new RuntimeErrorException(null);
        INode<T,V> exist = get_by_key(key);
        if (exist != null && !exist.isNull())
            exist.setValue(value);
        else {
            binary_insert(getRoot(), key, value, new Node());
            INode last = get_by_key(prev_key);
            fix_color(last);
        }
    }



   private INode<T,V> binary_insert(INode<T,V> root, T key, V value, INode<T,V> parent) {
        if (root == null || root.isNull() ) {
            if(root == null)
                root = new Node();
            if (getRoot() == null || getRoot().isNull()) {
                root.setColor(false);
                main_root = root;
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
                        INode<T,V> New = UncleBlack(x, parent, grand);
                        fix_color(New);
                    }
                }
            } else
                x.setColor(false);
        }
        return;
    }

    private INode<T,V> UncleBlack(INode<T,V> x, INode<T,V> parent, INode<T,V> grand) {
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


    private void rotate_left(INode a) {
        INode grand = a.getParent();
        INode b = a.getRightChild();
        INode c = b.getLeftChild();
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

    private void rotate_right(INode a) {
        INode b = a.getLeftChild();
        INode c = b.getRightChild();
        // Perform rotation
        b.setRightChild(a);
        a.setLeftChild(c);
        INode grand = a.getParent();
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
        return delete_node(getRoot(),key);
    }

    private boolean delete_node(INode<T,V> root, T key){
        if( root.isNull())
            return false;
        else if(key.compareTo(root.getKey())<0)
            delete_node(root.getLeftChild(),key);
        else if(key.compareTo(root.getKey())>0)
            delete_node(root.getRightChild(),key);
        else  { //case1 no children
            if((root.getLeftChild().isNull())&&(root.getRightChild().isNull())){
                if(root==getRoot()) {
                    main_root.setKey(null);
                    set_node_null(root);
                } else {
                    if (is_black(root))
                        double_black(root);
                    set_node_null(root);
                }
            } else if(root.getLeftChild().isNull()){ //case2 one child
                INode<T,V> temp=root.getRightChild();
                root.setKey(temp.getKey());
                root.setValue(temp.getValue());
                delete_node(temp,temp.getKey());
            } else if(root.getRightChild().isNull()){
                INode<T,V> temp=root.getLeftChild();
                root.setKey(temp.getKey());
                root.setValue(temp.getValue());
                delete_node(temp,temp.getKey());
            } else{
                INode<T,V> temp= find_min(root.getRightChild());
                root.setKey(temp.getKey());
                root.setValue(temp.getValue());
                delete_node(root.getRightChild(),temp.getKey());
            }
        }
        return true;
    }

    private INode<T,V> find_min(INode<T,V> node){
        INode<T,V> minimum = node;
        node = node.getLeftChild();
        while(!node.isNull()){
            if(node.getKey().compareTo(minimum.getKey())<0)
                minimum = node;
            node = node.getLeftChild();
        }
        return minimum;
    }

    private void double_black(INode<T,V> node){
        //case2
        if(node ==getRoot())
            return;
        //case3
        else if((sibling(node).isNull())||((is_black(sibling(node)))&&(is_black(sibling(node).getRightChild()))&&(is_black(sibling(node).getLeftChild())))){
            if(!sibling(node).isNull())
                sibling(node).setColor(true);
            if(is_black(node.getParent()))
                double_black(node.getParent());
            else
                node.getParent().setColor(false);
        } else if(!is_black(sibling(node))){//case4
            swap_color(sibling(node), node.getParent());
            if(isLeftChild(node))
                rotate_left(node.getParent());
            else
                rotate_right(node.getParent());
            double_black(node);
        } else if((is_black(sibling(node)))&&(is_black(get_far(node)))&&(!is_black(get_near(node)))){ //case5
            swap_color(sibling(node), get_near(node));
            if(isLeftChild(node))
                rotate_right(sibling(node));
            else
                rotate_left(sibling(node));
            last_case(node);
        } else
            last_case(node);

    }

    private void last_case(INode<T,V> node){
        if(is_black(sibling(node))&&(!isLeftChild(node))&&(!is_black(sibling(node).getLeftChild()))){
            swap_color(node.getParent(), sibling(node));
            sibling(node).getLeftChild().setColor(false);
            if(isLeftChild(node))
                rotate_left(node.getParent());
            else
                rotate_right(node.getParent());
        } else if(is_black(sibling(node))&&(isLeftChild(node))&&(!is_black(sibling(node).getRightChild()))){
            swap_color(node.getParent(), sibling(node));
            sibling(node).getRightChild().setColor(false);
            if(isLeftChild(node))
                rotate_left(node.getParent());
            else
                rotate_right(node.getParent());
        } else
            return;
    }

    private boolean is_black(INode<T,V> node){
        if(node.isNull())
            return true;
        return !node.getColor();
    }

    private INode<T,V> get_near(INode<T,V> node){
        if(isLeftChild(node))
            return sibling(node).getLeftChild();
        else
            return sibling(node).getRightChild();
    }

    private INode<T,V> get_far(INode<T,V> node){
        if(isLeftChild(node))
            return sibling(node).getRightChild();
        else
            return sibling(node).getLeftChild();
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

    private boolean isLeftChild(INode<T,V> node){
        if(node.getParent().getLeftChild()== node)
            return true;
        else
            return false;
    }

    private void set_node_null(INode<T,V> node){
        node.setKey(null);
        node.setValue(null);
        node.setColor(false);
        node.setRightChild(null);
        node.setLeftChild(null);
    }

}
