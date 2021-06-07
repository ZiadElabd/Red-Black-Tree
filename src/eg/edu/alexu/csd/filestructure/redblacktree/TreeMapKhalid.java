package eg.edu.alexu.csd.filestructure.redblacktree;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;

import javax.management.RuntimeErrorException;

public class TreeMapKhalid < T extends Comparable <T>, V> implements ITreeMap  {
	
	RedBlackTree<T, V> tree = new RedBlackTree<>();
	@Override
	public Entry ceilingEntry(Comparable key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Comparable ceilingKey(Comparable key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean containsKey(Comparable key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set entrySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Entry firstEntry() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Comparable firstKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Entry floorEntry(Comparable key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Comparable floorKey(Comparable key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object get(Comparable key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList headMap(Comparable toKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList headMap(Comparable toKey, boolean inclusive) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Entry lastEntry() {
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
	public Comparable lastKey() {
        if(tree.isEmpty())
            return null;
        INode root = tree.getRoot();
        INode prev = tree.getRoot();
        while (root!=null && !root.isNull()){
            prev = root;
            root = root.getRightChild();
        }
        return prev.getKey();
	}

	@Override
	public Entry pollFirstEntry() {
        if(tree.isEmpty())
            return null;
        Map.Entry <T, V> first = firstEntry();
        tree.delete(first.getKey());
        return first;
	}

	@Override
	public Entry pollLastEntry() {
        if(tree.isEmpty())
            return null;
        Map.Entry <T, V> last = lastEntry();
        tree.delete(last.getKey());
        return last;
	}

	@Override
	public void put(Comparable key, Object value) {
        if(key == null || value==null)
            throw new RuntimeErrorException(null);
        tree.insert((T)key, (V)value);
	}

	@Override
	public void putAll(Map map) {
		if (map == null) throw new RuntimeErrorException(null); 
		Iterator<Map.Entry> itr = map.entrySet().iterator();
        while(itr.hasNext()) {
            Map.Entry entry = itr.next();
            put((Comparable) entry.getKey(), entry.getValue());
        }
	}

	@Override
	public boolean remove(Comparable key) {
		if (key == null) throw new RuntimeErrorException(null) ; 
		boolean isExist = tree.contains((T)key) ;
		if (isExist) tree.delete((T)key) ;
		return isExist;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Collection values() {
		if(tree.isEmpty()) return null ; 
		Collection <Object> values = new LinkedList<>();
		values = this.loop(tree.getRoot(), values)  ;
		return values ; 
		
	}
	
	private Collection loop(INode root , Collection arr) {
		if (root != null && !root.isNull()) {
			loop(root.getLeftChild(),arr) ; 
			arr.add(root.getValue()) ; 
			loop(root.getRightChild(),arr) ;
		}
		return arr ; 
	}

}
