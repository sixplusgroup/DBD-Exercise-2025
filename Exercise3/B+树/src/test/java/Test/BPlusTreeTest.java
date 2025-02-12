package Test;

import org.example.BPlusTree;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BPlusTreeTest {

    private final int ENTRY_BOUND = 100;
    @Test
    public void equivalentQuery() {
        BPlusTree<Integer, Integer> t = new BPlusTree<>(4);
        List<Integer> randomValue = new ArrayList<>();
        for (int i=0; i<200; i++){
            Random random = new Random();
            int randomNum = random.nextInt(10000);
            t.insert(i,randomNum);
            randomValue.add(randomNum);
        }
        for (int i=0; i<200; i++){
            Assertions.assertEquals(t.query(i).toString(),"["+randomValue.get(i)+"]");
        }
    }

    @Test
    public void rangeQuery() {
        BPlusTree<Integer, Integer> t = new BPlusTree<>(4);
        List<Integer> randomValue = new ArrayList<>();
        for (int i=0; i<200; i++){
            Random random = new Random();
            int randomNum = random.nextInt(10000);
            t.insert(i,randomNum);
            randomValue.add(randomNum);
        }
        for (int i=0; i<150; i++){
            Random random = new Random();
            int range = random.nextInt(40)+2;
            StringBuilder randomResult = new StringBuilder();
            randomResult.append("["+randomValue.get(i));
            for (int j=i+1;j<i+range;j++){
                randomResult.append(", "+randomValue.get(j));
            }
            randomResult.append("]");
            Assertions.assertEquals(t.rangeQuery(i,i+range).toString(),randomResult.toString());
        }
    }

    @Test
    public void insertTest() {
        BPlusTree<Integer, Integer> t = new BPlusTree<>(11);
        List<Integer> data = new ArrayList<>();
        for (int i = 9; i >= 0; --i) {
            t.insert(i, i);
            data.add(i);
        }
        t.insert(0, 10);
        data.sort(Integer::compareTo);
        Assertions.assertEquals(t.toString(), data.toString());
    }

    @Test
    public void splitTest(){
        BPlusTree<Integer, Integer> t = new BPlusTree<>(4);
        List<Integer> data = new ArrayList<>();
        for (int i = 9; i >= 0; --i) {
            t.insert(i, i);
            data.add(i);
        }
        List<Integer> root = Arrays.asList(4);
        List<Integer> leftChild = Arrays.asList(2, 4);
        List<Integer> rightChild = Arrays.asList(6, 8);
        List<Integer> leftNodeOne = Arrays.asList(0, 1);
        List<Integer> leftNodeTwo = Arrays.asList(2, 3);
        List<Integer> leftNodeThree = Arrays.asList(4, 5);
        List<Integer> leftNodeFour = Arrays.asList(6, 7);
        List<Integer> leftNodeFive = Arrays.asList(8, 9);
        Assertions.assertEquals(t.toString(), root + "  \n" + leftChild + "  " + rightChild + "  \n" + leftNodeOne + "  "
            +leftNodeTwo+ "  "+leftNodeThree+"  "+leftNodeFour+"  "+leftNodeFive+"  \n");
    }

    @Test
    public void removeTest() {
        BPlusTree<Integer, Integer> t = new BPlusTree<>(11);
        List<Integer> data = new ArrayList<>();
        for (int i = 9; i >= 0; --i) {
            t.insert(i, i);
            data.add(i);
        }
        t.remove(3);
        data.remove(6);
        data.sort(Integer::compareTo);
        Assertions.assertEquals(t.toString(), data.toString());
    }

    @Test
    public void underflowTest() {
        BPlusTree<Integer, Integer> t = new BPlusTree<>(4);
        List<Integer> data = new ArrayList<>();
        for (int i = 20; i >= 0; --i) {
            t.insert(i, i);
            data.add(i);
        }
        t.remove(13);
        t.remove(14);
        Assertions.assertEquals(t.toString(),"[7, 11, 15]  \n" +
                "[3, 5, 7]  [9, 11]  [12, 15]  [17, 19]  \n" +
                "[0, 1, 2]  [3, 4]  [5, 6]  [7, 8]  [9, 10]  [11]  [12]  [15, 16]  [17, 18]  [19, 20]  \n");
    }
}
