package AVLTree;


import AVLTree.printer.BinaryTrees;

public class TestMain {

    public static void main(String[] args) {
        AVLTree<Integer> avl = new AVLTree<Integer>();
        int[] data = new int[]{
                85, 19, 69, 3, 7, 99, 95
        };
        for (int i = 0; i < data.length; i++) {
            avl.add(data[i]);
        }
        BinaryTrees.println(avl);
        avl.remove(99);
        avl.remove(85);
        avl.remove(95);
        BinaryTrees.println(avl);
    }
}
