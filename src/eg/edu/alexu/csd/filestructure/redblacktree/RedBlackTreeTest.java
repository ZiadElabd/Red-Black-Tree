package eg.edu.alexu.csd.filestructure.redblacktree;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class RedBlackTreeTest {

    @Test
    void insertTest() {
        RedBlackTree<Integer, Integer> r = new RedBlackTree<Integer, Integer>();
        r.insert(41, 1);
        r.insert(38, 2);
        r.insert(31, 3);
        r.insert(12, 4);
        r.insert(19, 5);
        r.insert(8, 6);
        ArrayList<Integer> inOrderTraverse = r.getTraverseList(r.getRoot());
        //expected arraylist
        ArrayList<Integer> expectedArray = new ArrayList<Integer>();
        expectedArray.add(8);
        expectedArray.add(12);
        expectedArray.add(19);
        expectedArray.add(31);
        expectedArray.add(38);
        expectedArray.add(41);
        for(int i=0 ;i<inOrderTraverse.size();i++)
        {
            Assert.assertEquals(expectedArray.get(i), inOrderTraverse.get(i));
        }

    }
    @Test
    void DeleteTest() {
        RedBlackTree<Integer, Integer> r = new RedBlackTree<Integer, Integer>();
        r.insert(41, 1);
        r.insert(38, 2);
        r.insert(31, 3);
        r.insert(12, 4);
        r.insert(19, 5);
        r.insert(8, 6);
        r.delete(8);
        r.delete(12);
        ArrayList<Integer> inOrderTraverse = r.getTraverseList(r.getRoot());
        //expected arraylist
        ArrayList<Integer> expectedArray = new ArrayList<Integer>();
        expectedArray.add(19);
        expectedArray.add(31);
        expectedArray.add(38);
        expectedArray.add(41);
        for(int i=0 ;i<inOrderTraverse.size();i++)
        {
            Assert.assertEquals(expectedArray.get(i), inOrderTraverse.get(i));
        }

    }
    @Test
    void searchTest() {
        RedBlackTree<Integer, Integer> r = new RedBlackTree<Integer, Integer>();
        r.insert(41, 1);
        r.insert(38, 2);
        r.insert(31, 3);
        r.insert(12, 4);
        r.insert(19, 5);
        r.insert(8, 6);

        Assert.assertEquals((long )2,(long) r.search(38));
        Assert.assertEquals(null, r.search(3));


    }
    @Test
    void containTest() {
        RedBlackTree<Integer, Integer> r = new RedBlackTree<Integer, Integer>();
        r.insert(41, 1);
        r.insert(38, 2);
        r.insert(31, 3);
        r.insert(12, 4);
        r.insert(19, 5);
        r.insert(8, 6);

        Assert.assertEquals(true, r.contains(12));
        Assert.assertEquals(false, r.contains(5));

    }
    @Test
    void isEmptyTest() {
        RedBlackTree<Integer, Integer> r = new RedBlackTree<Integer, Integer>();
        r.insert(41, 1);
        r.insert(38, 2);
        r.insert(31, 3);
        r.insert(12, 4);
        r.insert(19, 5);
        r.insert(8, 6);
        Assert.assertEquals(false, r.isEmpty());

        r.delete(41);
        r.delete(38);
        r.delete(31);
        r.delete(12);
        r.delete(19);
        r.delete(8);
        Assert.assertEquals(true,r.isEmpty());

    }

}
