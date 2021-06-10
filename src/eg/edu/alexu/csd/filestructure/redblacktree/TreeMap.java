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

    // i start from here   # ziad elabd


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
    public Map.Entry<T, V> floorEntry(T key) {
        if(key == null)
            throw new RuntimeErrorException(null);
        if(tree.isEmpty())
            return null;
        V value;
        boolean exist = tree.contains(key);
        if(exist)
            value = tree.search(key);
        else{
            INode<T, V> less = greatestLower(key);
            if(less == null)
                return null;
            else {
                value = less.getValue();
                key = less.getKey();
            }
        }
        Map.Entry<T, V> result = new AbstractMap.SimpleEntry<T, V>((T) key, (V) value);
        return result;
    }

    private INode<T, V> greatestLower(T key){
        if(key == null)
            throw new RuntimeErrorException(null);
        INode<T, V> root = tree.getRoot();
        INode<T, V> prev = null;
        while (root!=null && !root.isNull()){
            if(key.compareTo(root.getKey())>0) {
                prev = root;
                root = root.getRightChild();
            }
            else if(root.getLeftChild()!=null && !root.getLeftChild().isNull()&& key.compareTo(root.getLeftChild().getKey())>0){
                prev = root.getLeftChild();
                root = prev.getRightChild();
            }
            else
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
        boolean exist = tree.contains(key);
        if(exist)
            return key;
        INode<T, V> less = greatestLower(key);
        if(less == null)
            return null;
        return less.getKey();
    }

    @Override
    public V get(T key) {
        if(key == null)
            throw new RuntimeErrorException(null);
        V value = tree.search(key);
        return value;
    }

    @Override
    public ArrayList<Map.Entry<T, V>> headMap(T toKey) {
        if(toKey == null)
            throw new RuntimeErrorException(null);
        ArrayList<Map.Entry<T, V>> head = new ArrayList<>();
        head = loophead(toKey, tree.getRoot(),head );
        return head;
    }

    @Override
    public ArrayList<Map.Entry<T, V>> headMap(T toKey, boolean inclusive) {
        if(toKey == null)
            throw new RuntimeErrorException(null);
        ArrayList<Map.Entry<T, V>> head = new ArrayList<>();
        head = loophead(toKey, tree.getRoot(),head );
        if(inclusive){
            if(tree.contains(toKey)) {
                Map.Entry<T, V> current = new AbstractMap.SimpleEntry<T, V>((T) toKey, (V) tree.search(toKey));
                head.add(current);
            }
        }
        return head;
    }

    private ArrayList<Map.Entry<T, V>> loophead(T key, INode<T, V> root, ArrayList<Map.Entry<T, V>> head) {
        if (root != null && !root.isNull()) {
            loophead(key, root.getLeftChild(), head);
            if(key.compareTo(root.getKey())>0) {
                Map.Entry<T, V> node = new AbstractMap.SimpleEntry<T, V>((T) root.getKey(), (V) root.getValue());
                head.add(node);
            }
            loophead(key, root.getRightChild(), head);
        }
        return head;
    }

    @Override
    public Set<T> keySet() {
        if(tree.isEmpty())
            return null;
        Set<T> set = new LinkedHashSet<>();
        set = keyloop(tree.getRoot(),set);
        return set;
    }

    private Set<T> keyloop(INode<T, V> root, Set<T> set) {
        if (root != null && !root.isNull()) {
            keyloop(root.getLeftChild(), set);
            set.add(root.getKey());
            keyloop(root.getRightChild(), set);
        }
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
            sizeloop(tree.getRoot(),s);
        return s[0];
    }


    private int[] sizeloop(INode root, int[] s) {
        if (root != null && !root.isNull()) {
            sizeloop(root.getLeftChild(), s);
            s[0]++;
            sizeloop(root.getRightChild(), s);
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
