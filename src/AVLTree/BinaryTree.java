package AVLTree;



import AVLTree.printer.BinaryTreeInfo;

import java.util.LinkedList;
import java.util.Queue;

public class BinaryTree<E> implements BinaryTreeInfo {

    protected Integer size=0;
    protected Node<E> root; //根节点


    /**
     * @return 元素的数量
     */
    public int size() {
        return size;
    }

    /**
     * @return  是否为空
     */
    public boolean isEmpty() {
        return size==0;
    }

    /**
     * 清空所有元素
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * 前序遍历---递归
     */
    public void preorderTraversal() {
        preorderTraversal(root);
    }
    public void preorderTraversal(Visitor<E> visitor) {
        preorderTraversal(root, visitor);
    }
    private void preorderTraversal(Node<E> node) {
        if (node == null) {
            return;
        }
        System.out.println(node.element);
        preorderTraversal(node.left);
        preorderTraversal(node.right);
    }
    private void preorderTraversal(Node<E> node, Visitor<E> visitor) {
        if (node == null || visitor == null) {
            return;
        }
        visitor.visit(node.element);
        preorderTraversal(node.left, visitor);
        preorderTraversal(node.right, visitor);
    }
    /**
     * 中序遍历--递归
     */
    public void inorderTraversal() {
        inorderTraversal(root);
    }
    public void inorderTraversal(Visitor<E> visitor) {
        inorderTraversal(root, visitor);
    }
    private void inorderTraversal(Node<E> node) {
        if (node == null) {
            return;
        }
        inorderTraversal(node.left);
        System.out.println(node.element);
        inorderTraversal(node.right);
    }
    private void inorderTraversal(Node<E> node, Visitor<E> visitor) {
        if (node == null || visitor == null) {
            return;
        }
        inorderTraversal(node.left, visitor);
        visitor.visit(node.element);
        inorderTraversal(node.right, visitor);
    }

    /**
     * 后序遍历--递归
     */
    public void postorderTraversal() {
        postorderTraversal(root);
    }
    public void postorderTraversal(Visitor<E> visitor) {
        postorderTraversal(root, visitor);
    }
    private void postorderTraversal(Node<E> node) {
        if (node == null)   return;
        postorderTraversal(node.left);
        postorderTraversal(node.right);
        System.out.println(node.element);
    }
    private void postorderTraversal(Node<E> node, Visitor<E> visitor) {
        if (node == null || visitor == null)   return;
        postorderTraversal(node.left, visitor);
        postorderTraversal(node.right, visitor);
        visitor.visit(node.element);
    }

    /**
     * 层序遍历--非递归
     */
    public void levelOrderTraversal() {
        if (root == null) {
            return;
        }
        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();
            System.out.println(node.element);
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }
    }
    /**
     * @param visitor 层序遍历的打印方法 --> visitor.visit(node.element);
     */
    public void levelOrder(Visitor<E> visitor) {
        if (root == null || visitor == null) {
            return;
        }
        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();
            visitor.visit(node.element);
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }
    }

    public static interface Visitor<E> {
        void visit(E element);
    }

    /**
     * @return 返回node的前驱节点
     */
    protected Node<E> predecessor(Node<E> node) {
        if (node == null) {
            return node;
        }
        Node<E> pLeft = node.left;
        if (pLeft!= null) {
            //先找第一个左节点，然后找到最后一个右节点

            while (pLeft != null) {
                pLeft = pLeft.right;
            }
            return pLeft;
        }
        //从父节点中寻找
        while (node.parent != null && node == node.parent.left) {
            node = node.parent;
        }
        //循环结束--两种情况
        //1.node.parent == null
        //或者 node.parent.right == node
        return node.parent;
    }

    /**
     * @return 返回node的后继节点
     */
    protected Node<E> successor(Node<E> node) {
        if (node == null) {
            return node;
        }
        Node<E> pRight = node.right;
        if (pRight != null) {
            while (pRight.left != null) {
                pRight = pRight.left;
            }
            return pRight;
        }
        while (node.parent != null && node == node.parent.right) {
            node = node.parent;
        }
        return node.parent;
    }

    protected Node<E> createNode(E element, Node<E> parent) {
        return new Node<E>(element, parent);
    }
    /**
     * @return 返回二叉树高度
     */
    public int height() {
//        return height(root);
        return height2();
    }
    private int height(Node<E> node) {//--递归
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(height(node.left), height(node.right));
    }
    private int height2() {//非递归
        if (root == null) {
            return 0;
        }
        int heightIndex = 0;
        int levelSize = 1; //存储每一层的元素数量
        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();
            levelSize--;
            // System.out.println(node.element);
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
            if (levelSize == 0) {
                levelSize = queue.size();
                heightIndex++;
            }
        }
        return heightIndex;
    }

    /**
     * @return 判断是不是完全二叉树 返回true代表是， false代表不是
     */
    public boolean isComplete() {
        if (root == null) {
            return false;
        }
        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        boolean leaf = false;
        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();
            if (leaf && !node.isLeaf()) {
                return false;
            }
            if (node.left != null) {
                queue.offer(node.left);
            }else if (node.right != null) {
                return false;
            }
            if (node.right != null) {
                queue.offer(node.right);
            }else {
                leaf = true;
            }
        }
        return true;
    }


    protected static class Node<E> {
        E element;
        Node<E> left;
        Node<E> right;
        Node<E> parent;

        public Node(E element, Node<E> parent) {
            this.element = element;
            this.parent = parent;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }

        public boolean hasTwoChildren() {
            return left != null && right != null;
        }

        /**
         * @return 判断是否为左子树
         */
        public boolean isLeftChild() {
            return parent != null && this == parent.left;
        }

        public boolean isRightChild() {
            return parent != null && this == parent.right;
        }
    }

    //-----------------------------------
    //以下为实现打印器方法
    @Override
    public Object root() {
        return root;
    }

    @Override
    public Object left(Object node) {
        return ((Node<E>) node).left;
    }

    @Override
    public Object right(Object node) {
        return ((Node<E>) node).right;
    }

    @Override
    public Object string(Object node) {
        return ((Node<E>) node).element;
    }

}
