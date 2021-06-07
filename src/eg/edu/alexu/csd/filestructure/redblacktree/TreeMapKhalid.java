package eg.edu.alexu.csd.filestructure.redblacktree;

import java.security.Key;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingDeque;

import javax.management.RuntimeErrorException;

public class TreeMapKhalid < T extends Comparable <T>, V> implements ITreeMap  {
	
	RedBlackTree<T, V> tree = new RedBlackTree<>();
	@Override
	public Entry ceilingEntry(Comparable key) {
		// TODO Auto-generated method stub
		V reutrnvalue = null;
		T returnKey = null;
		if(key==null) return null;
		reutrnvalue=tree.search((T) key);
		//if the key is in the map we get the value of it
		if(reutrnvalue!=null){
			returnKey= (T) key;
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
	private INode leastGreater(INode root,Comparable key){
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
	public Comparable ceilingKey(Comparable key) {
		T returnKey = null;
		if(key==null) throw new NullPointerException();
	    boolean status= tree.contains((T) key);
		if(status==true){
	    	returnKey= (T) key;
	    }// search for the least greater key
		else{
				INode root= tree.getRoot();
				INode leastNode=leastGreater(root,key);
				if (leastNode==null) return null;
				else{
					 returnKey= (T) leastNode.getKey();
				}
			}                                                 
			return returnKey;
	}

	@Override
	public void clear() {
		tree.clear();

	}

	@Override
	public boolean containsKey(Comparable key) {
		// TODO Auto-generated method stub
		if (key==null) throw new NullPointerException();
		return tree.contains((T) key);
	}

	@Override
	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		  INode root =tree.getRoot();
		  return Preordersearch(root,value);
	}
	 private boolean Preordersearch(INode node,Object value) {
		 if (node.isNull()) return false;
		 if (node.getValue().equals(value)) return true;
		 return Preordersearch(node.getLeftChild(), value)
	            || Preordersearch(node.getRightChild(),value);
	 }
	@Override
	public Set entrySet() {
		// TODO Auto-generated method stub
		INode root =tree.getRoot();
		if(root.isNull()) return null;
		 Set<Map.Entry> set = new LinkedHashSet<>();
		 Preorder(root,set);
		return set;
	}
	private void Preorder(INode root,Set<Map.Entry> set){
		if(root.isNull()) return;
		Preorder(root.getLeftChild(),set);
		Map.Entry<T, V> node = new AbstractMap.SimpleEntry<>((T) root.getKey(), (V) root.getValue());
		set.add(node);
		Preorder(root.getRightChild(),set);
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
