package eg.edu.alexu.csd.filestructure.redblacktree;

import javax.management.RuntimeErrorException;
import java.util.*;

public class TreeMapziad < T extends Comparable <T>, V> implements ITreeMap<T, V>{

    RedBlackTree<T, V> tree = new RedBlackTree<>();

    @Override
    public Map.Entry ceilingEntry(Comparable key) {
        return null;
    }

    @Override
    public Comparable ceilingKey(Comparable key) {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean containsKey(Comparable key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public Set<Map.Entry<T,V>> entrySet() {
        return null;
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
    public Map.Entry lastEntry() {
        return null;
    }

    @Override
    public T lastKey() {
        return null;
    }

    @Override
    public Map.Entry pollFirstEntry() {
        return null;
    }

    @Override
    public Map.Entry pollLastEntry() {
        return null;
    }

    @Override
    public void put(Comparable key, Object value) {

    }

    @Override
    public void putAll(Map map) {

    }

    @Override
    public boolean remove(Comparable key) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Collection values() {
        return null;
    }
}
