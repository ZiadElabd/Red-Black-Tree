package eg.edu.alexu.csd.filestructure.redblacktree;

import javax.management.RuntimeErrorException;
import java.util.*;

public class TreeMap < T extends Comparable <T>, V> implements ITreeMap<T, V>{

    RedBlackTree<T, V> tree = new RedBlackTree<>();

    @Override
    public Map.Entry ceilingEntry(T key) {
        // TODO Auto-generated method stub
        V reutrnvalue = null;
        T returnKey = null;
        if(key==null) return null;
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
        if(key==null) throw new NullPointerException();
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
        if (key==null) throw new NullPointerException();
        return tree.contains( key);
    }

    @Override
    public boolean containsValue(V value) {
        // TODO Auto-generated method stub
        INode<T, V> root =tree.getRoot();
        return Preordersearch(root,value);
    }
    private boolean Preordersearch(INode<T, V> node,V value) {
        if (node.isNull()) return false;
        if (node.getValue().equals(value)) return true;
        return Preordersearch(node.getLeftChild(), value)
                || Preordersearch(node.getRightChild(),value);
    }
    @Override
    public Set<Map.Entry<T, V>> entrySet() {
        // TODO Auto-generated method stub
        INode<T, V> root =tree.getRoot();
        if(root.isNull()) return null;
        Set<Map.Entry<T, V>> set = new LinkedHashSet<>();
        Preorder(root,set);
        return set;
    }

    private void Preorder(INode<T, V> root,Set<Map.Entry<T, V>> set){
        if(root.isNull()) return;
        Preorder(root.getLeftChild(),set);
        Map.Entry<T, V> node = new AbstractMap.SimpleEntry<>(root.getKey(), root.getValue());
        set.add(node);
        Preorder(root.getRightChild(),set);
    }

    @Override
    public Map.Entry<T,V> firstEntry() {
        if(tree.isEmpty())
            return null;
        INode<T,V> root = tree.getRoot();
        INode<T,V> prev = tree.getRoot();
        if (tree.isEmpty())
            return null;
        while (root!=null && !root.isNull()){
            prev = root;
            root = root.getLeftChild();
        }
        Map.Entry <T, V> first = new AbstractMap.SimpleEntry<T, V>(prev.getKey(),prev.getValue());
        return first;
    }

    @Override
    public T firstKey() {
        if(tree.isEmpty())
            return null;
        INode<T,V> root = tree.getRoot();
        INode<T,V> prev = tree.getRoot();
        while (root!=null && !root.isNull()){
            prev = root;
            root = root.getLeftChild();
        }
        return prev.getKey();
    }

    private INode<T,V> greatestLower(Comparable key){
        if(key == null)
            throw new RuntimeErrorException(null);
        INode<T,V> root = tree.getRoot();
        INode<T,V> prev = null;
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
    public Map.Entry<T,V> floorEntry(T key) {
        if(key == null)
            throw new RuntimeErrorException(null);
        if(tree.isEmpty())
            return null;
        V value;
        boolean exist = tree.contains(key);
        if(exist)
            value = tree.search(key);
        else{
            INode<T,V> less = greatestLower(key);
            if(less == null)
                return null;
            else {
                value = less.getValue();
                key = less.getKey();
            }
        }
        Map.Entry<T, V> result = new AbstractMap.SimpleEntry<T, V>( key,value);
        return result;
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
        INode<T,V> less = greatestLower(key);
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

    private ArrayList<Map.Entry<T,V>> inorder(T key, INode<T,V> root, ArrayList<Map.Entry<T,V>> head) {
        if (root != null && !root.isNull()) {
            inorder(key, root.getLeftChild(), head);
            if(key.compareTo(root.getKey())>0) {
                Map.Entry<T, V> node = new AbstractMap.SimpleEntry<T, V>(root.getKey(), root.getValue());
                head.add(node);
            }
            inorder(key, root.getRightChild(), head);
        }
        return head;
    }

    @Override
    public ArrayList<Map.Entry<T,V>> headMap(T toKey) {
        if(toKey == null)
            throw new RuntimeErrorException(null);
        ArrayList<Map.Entry<T,V>> head = new ArrayList<>();
        head = inorder(toKey, tree.getRoot(),head );
        return head;
    }

    @Override
    public ArrayList<Map.Entry<T,V>> headMap(T toKey, boolean inclusive) {
        if(toKey == null)
            throw new RuntimeErrorException(null);
        ArrayList<Map.Entry<T,V>> head = new ArrayList<>();
        head = inorder(toKey, tree.getRoot(),head );
        if(inclusive){
            if(tree.contains(toKey)) {
                Map.Entry<T, V> current = new AbstractMap.SimpleEntry<T, V>(toKey,tree.search(toKey));
                head.add(current);
            }
        }
        return head;
    }

    private Set<T> keyloop(INode<T,V> root, Set<T> set) {
        if (root != null && !root.isNull()) {
            keyloop(root.getLeftChild(), set);
            set.add(root.getKey());
            keyloop(root.getRightChild(), set);
        }
        return set;
    }

    @Override
    public Set<T> keySet() {
        if(tree.isEmpty())
            return null;
        Set<T> set = new LinkedHashSet<>();
        set = keyloop(tree.getRoot(),set);
        return set;
    }

    @Override
    public Map.Entry<T, V> lastEntry() {
        if(tree.isEmpty())
            return null;
        INode root = tree.getRoot();
        INode prev = tree.getRoot();
        if (tree.isEmpty())
            return null;
        while (root!=null && !root.isNull()){
            prev = root;
            root = root.getRightChild();
        }
        Map.Entry <T, V> last = new AbstractMap.SimpleEntry<T, V>((T)prev.getKey(), (V) prev.getValue());
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
        return 0;
    }

    @Override
    public Collection<V> values() {
        if(tree.isEmpty()) return null ;
        Collection <V> values = new LinkedList<>();
        values = this.loop(tree.getRoot(), values)  ;
        return values ;

    }

    private Collection<V> loop(INode<T, V> root , Collection<V> arr) {
        if (root != null && !root.isNull()) {
            loop(root.getLeftChild(),arr) ;
            arr.add(root.getValue()) ;
            loop(root.getRightChild(),arr) ;
        }
        return arr ;
    }

}
