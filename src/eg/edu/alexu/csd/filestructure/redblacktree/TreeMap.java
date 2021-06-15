package eg.edu.alexu.csd.filestructure.redblacktree;

import javax.management.RuntimeErrorException;
import java.util.*;

public class TreeMap < T extends Comparable <T>, V> implements ITreeMap<T, V>{

    RedBlackTree<T, V> tree = new RedBlackTree<>();

    @Override
    public Map.Entry<T,V> ceilingEntry(T key) {
        // TODO Auto-generated method stub
        V reutrnvalue = null;
        T returnKey = null;
        if(key==null) throw new  RuntimeErrorException(null);
        reutrnvalue=tree.search(key);
        //if the key is in the map we get the value of it
        if(reutrnvalue!=null){
            returnKey= key;
        }
        // search for the least greater key
        else {
            INode root= tree.getRoot();
            INode leastNode=leastGreater(root,key);
            if (leastNode==null) return null;
            else{
                returnKey= (T) leastNode.getKey();
                reutrnvalue = (V) leastNode.getValue();
            }
        }

        Map.Entry<T, V> result = new AbstractMap.SimpleEntry<>( returnKey,  reutrnvalue);
        return result;
    }

    private INode<T, V> leastGreater(INode<T, V> root,T key){
        // If leaf node reached and is smaller than Key
        if (root.getLeftChild() == null
                &&root.getRightChild() == null
                && root.getKey().compareTo(key)<0)
            return null;
        // If node's value is greater than N and left value
        // is null or smaller then return the node value
        if ((root.getKey().compareTo(key) >= 0 && root.getLeftChild() == null) ||
                (root.getKey().compareTo(key) >= 0 && root.getLeftChild().getKey().compareTo(key) < 0))
            return root;
        // if node value is smaller than N search in the right subtree
        if (root.getKey().compareTo(key) <= 0)
            return leastGreater(root.getRightChild(), key);
            // if node value is greater than N search in the left subtree
        else
            return leastGreater(root.getLeftChild(), key);
    }


    @Override
    public T ceilingKey(T key) {
        T returnKey = null;
        if(key==null) throw new  RuntimeErrorException(null); ;
        boolean status= tree.contains(key);
        if(status==true){
            returnKey= key;
        }// search for the least greater key
        else{
            INode<T, V> root= tree.getRoot();
            INode<T, V> leastNode=leastGreater(root,key);
            if (leastNode==null) return null;
            else{
                returnKey= leastNode.getKey();
            }
        }
        return returnKey;
    }

    @Override
    public void clear() {
        tree.clear();

    }

    @Override
    public boolean containsKey(T key) {
        // TODO Auto-generated method stub
        if (key==null) throw new RuntimeErrorException(null);
        return tree.contains( key);
    }

    @Override
    public boolean containsValue(V value) {
        // TODO Auto-generated method stub
        if (value==null) throw new RuntimeErrorException(null);
        INode<T, V> root =tree.getRoot();
        return Preordersearch(root,value);
    }
    private boolean Preordersearch(INode<T, V> node,V value) {
        if (node.isNull()) return false;
        if (node.getValue().equals(value)) return true;
        return Preordersearch(node.getLeftChild(), value)
                || Preordersearch(node.getRightChild(),value);
    }

    private void preorder(INode<T, V> root, Set<Map.Entry<T, V>> set){
        if(root.isNull()) return;
        preorder(root.getLeftChild(),set);
        Map.Entry<T, V> node = new AbstractMap.SimpleEntry<>( root.getKey() , root.getValue() );
        set.add(node);
        preorder(root.getRightChild(),set);
    }

    @Override
    public Set<Map.Entry<T, V>> entrySet() {
        INode<T, V> root =tree.getRoot();
        if(root.isNull()) return null;
        Set<Map.Entry<T, V>> set = new LinkedHashSet<>();
        preorder(root,set);
        return set;
    }
    // i start from here   # ziad elabd
    @Override
    public Map.Entry<T,V> firstEntry() {
        if(tree.isEmpty())
            return null;
        INode<T,V> current = tree.getRoot();
        INode<T,V> prev = current;
        while (current !=null && !current.isNull()){
            prev = current;
            current = current.getLeftChild();
        }
        return new AbstractMap.SimpleEntry<>( prev.getKey() , prev.getValue() );
    }

    @Override
    public T firstKey() {
        if(tree.isEmpty())
            return null;
        INode<T,V> current = tree.getRoot();
        INode<T,V> prev = current;
        while (current !=null && !current.isNull()){
            prev = current;
            current = current.getLeftChild();
        }
        return prev.getKey();
    }

    @Override
    public Map.Entry<T,V> floorEntry(T key) {
        if(key == null)
            throw new RuntimeErrorException(null);
        if(tree.isEmpty())
            return null;
        V value = tree.search(key);
        if(value == null){  // key doesn't exist
            INode<T,V> great_low = greatest_lower(key);
            if(great_low == null)
                return null;
            return new AbstractMap.SimpleEntry<>( great_low.getKey() , great_low.getValue() );
        }
        return new AbstractMap.SimpleEntry<>( key , value );
    }

    private INode<T,V> greatest_lower(T key){
        if(key == null)
            throw new RuntimeErrorException(null);
        INode<T,V> current = tree.getRoot();
        INode<T,V> prev = null;
        while (current !=null && !current.isNull()){
            if(key.compareTo(current.getKey())>0) {  // key > current
                prev = current;
                current = current.getRightChild();
            }else if( current.getLeftChild()!=null
                    && !current.getLeftChild().isNull()
                    && key.compareTo(current.getLeftChild().getKey())>0){  // key > left child
                prev = current.getLeftChild();
                current = prev.getRightChild();
            }else  // return the last node that achieved the conditions
                return prev;
        }
        return prev;
    }

    @Override
    public T floorKey(T key) {
        if(key == null)
            throw new RuntimeErrorException(null);
        if(tree.isEmpty())
            return null;
        if(tree.contains(key))
            return key;
        INode<T,V> great_low = greatest_lower(key);
        if(great_low == null)
            return null;
        return great_low.getKey();
    }

    @Override
    public V get(T key) {
        if(key == null)
            throw new RuntimeErrorException(null);
        return tree.search(key);
    }

    private void inorder(T key, INode<T,V> root , ArrayList<Map.Entry<T,V>> head ) {
        if (root != null && !root.isNull()) {
            inorder(key, root.getLeftChild(), head);
            if(key.compareTo(root.getKey())>0) {
                Map.Entry<T, V> node = new AbstractMap.SimpleEntry<>(root.getKey(), root.getValue());
                head.add(node);
            }
            inorder(key, root.getRightChild(), head);
        }
    }

    @Override
    public ArrayList<Map.Entry<T,V>> headMap(T toKey) {
        if(toKey == null)
            throw new RuntimeErrorException(null);
        ArrayList<Map.Entry<T,V>> head = new ArrayList<>();
        inorder( toKey , tree.getRoot(), head );
        return head;
    }

    @Override
    public ArrayList<Map.Entry<T,V>> headMap(T toKey, boolean inclusive) {
        if(toKey == null)
            throw new RuntimeErrorException(null);
        ArrayList<Map.Entry<T,V>> head = new ArrayList<>();
        inorder(toKey, tree.getRoot(),head );
        if(inclusive){
            V value = tree.search(toKey);
            if(value != null) {
                head.add(new AbstractMap.SimpleEntry<>(toKey, value));
            }
        }
        return head;
    }


    private void inorder(INode<T,V> root, Set<T> set) {
        if (root == null || root.isNull())
            return;
        inorder(root.getLeftChild(), set);
        set.add(root.getKey());
        inorder(root.getRightChild(), set);
    }

    @Override
    public Set<T> keySet() {
        if(tree.isEmpty())
            return null;
        Set<T> set = new LinkedHashSet<>();
        inorder(tree.getRoot(),set);
        return set;
    }

    // i finished here   # ziad elabd

    @Override
    public Map.Entry<T, V> lastEntry() {
        if(tree.isEmpty())
            return null;
        INode<T, V> root = tree.getRoot();
        INode<T, V> prev = tree.getRoot();
        if (tree.isEmpty())
            return null;
        while (root!=null && !root.isNull()){
            prev = root;
            root = root.getRightChild();
        }
        Map.Entry <T, V> last = new AbstractMap.SimpleEntry<>(prev.getKey(),prev.getValue());
        return last;
    }

    @Override
    public T lastKey() {
        if(tree.isEmpty())
            return null;
        INode<T, V> root = tree.getRoot();
        INode<T, V> prev = tree.getRoot();
        while (root!=null && !root.isNull()){
            prev = root;
            root = root.getRightChild();
        }
        return prev.getKey();
    }

    @Override
    public Map.Entry<T, V> pollFirstEntry() {
        if(tree.isEmpty())
            return null;
        Map.Entry <T, V> first = firstEntry();
        tree.delete(first.getKey());
        return first;
    }

    @Override
    public Map.Entry<T, V> pollLastEntry() {
        if(tree.isEmpty())
            return null;
        Map.Entry <T, V> last = lastEntry();
        tree.delete(last.getKey());
        return last;
    }

    @Override
    public void put(T key, V value) {
        if(key == null || value==null)
            throw new RuntimeErrorException(null);
        tree.insert(key,value);
    }

    @Override
    public void putAll(Map<T, V> map) {
        if (map == null) throw new RuntimeErrorException(null);
        Iterator<Map.Entry<T, V>> itr = map.entrySet().iterator();
        while(itr.hasNext()) {
            Map.Entry<T, V> entry = itr.next();
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public boolean remove(T key) {
        if (key == null) throw new RuntimeErrorException(null) ;
        boolean isExist = tree.contains(key) ;
        if (isExist) tree.delete(key) ;
        return isExist;
    }

    @Override
    public int size() {
        int[] s = new int[1];
        s[0] = 0;
        if(!tree.isEmpty())
            inorder(tree.getRoot(),s);
        return s[0];
    }


    private int[] inorder(INode root, int[] s) {
        if (root != null && !root.isNull()) {
            inorder(root.getLeftChild(), s);
            s[0]++;
            inorder(root.getRightChild(), s);
        }
        return s;
    }


    @Override
    public Collection<V> values() {
        if(tree.isEmpty()) return null ;
        Collection <V> values = new LinkedList<>();
        loop(tree.getRoot(), values)  ;
        return values ;

    }

    private void loop(INode<T, V> root , Collection<V> arr) {
        if (root != null && !root.isNull()) {
            loop(root.getLeftChild(),arr) ;
            arr.add(root.getValue()) ;
            loop(root.getRightChild(),arr) ;
        }
    }
}
