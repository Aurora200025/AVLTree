package AVLTree;

import java.util.Comparator;

public class AVLTree<E> extends BinarySearchTree<E>  {



    public AVLTree() {
        this(null);
    }
    public AVLTree(Comparator<E> comparator) {
        super(comparator);
    }

    /**
     * 恢复平衡的逻辑 -- node是新添加的节点
     * @param node 添加node之后的调整--AVLTree
     */
    @Override
    protected void afterAdd(Node<E> node) {
        while ((node = node.parent) != null) {
            if (isBalanced(node)) {//node是否平衡
                //如果平衡就更新高度，否则恢复平衡
                updateHeight(node);
            } else {//恢复平衡
                rebalance2(node);
                break;
            }
        }
    }

    @Override
    public void afterRemove(Node<E> node) {
        while ((node = node.parent) != null) {
            if (isBalanced(node)) {
                updateHeight(node);
            }else {
                rebalance2(node);
            }
        }
    }

    /**
     * @param node 判断该节点是否平衡
     * @return true--平衡；false -- 不平衡
     */
    private boolean isBalanced(Node<E> node) {
        return Math.abs(((AVLNode<E>) node).balanceFactor()) <= 1;
    }

    /**
     * @param node 更新节点高度
     */
    private void updateHeight(Node<E> node) {
        ((AVLNode<E>) node).updateHeight();
    }
    @Override
    protected Node<E> createNode(E element, Node<E> parent) {
        return new AVLNode<E>(element, parent);
    }

    /**
     * @param grand 恢复平衡--node=高度最低的那个不平衡的点--分开处理
     */
    private void rebalance(Node<E> grand) {
        Node<E> parent = (((AVLNode<E>) grand)).tallerChild();
        Node<E> node = (((AVLNode<E>) parent)).tallerChild();
        if (parent.isLeftChild()) { //L
            if (node.isLeftChild()) { //LL
                //直接左旋转
                rotateRight(grand);
            }else { //LR
                //1.先左旋转
                rotateLeft(parent);
                //2.在右旋转
                rotateRight(grand);
            }
        }else { //R
            if (node.isRightChild()) { //RR
                rotateLeft(grand);
            }else { //RL
                //1.先对parent进行右旋转
                rotateRight(parent);
                //2.在对grant进行左旋转
                rotateLeft(grand);
            }
        }
    }

    /**
     * @param grand 恢复平衡--node=高度最低的那个不平衡的点--统一处理
     */
    private void rebalance2(Node<E> grand) {
        Node<E> parent = (((AVLNode<E>) grand)).tallerChild();
        Node<E> node = (((AVLNode<E>) parent)).tallerChild();
        if (parent.isLeftChild()) { //L
            if (node.isLeftChild()) { //LL
                rotate(grand, node, node.right, parent, parent.right, grand);
            }else { //LR
                rotate(grand, parent, node.left, node, node.right, grand);
            }
        }else { //R
            if (node.isRightChild()) { //RR
                rotate(grand, grand, parent.left, parent, node.left, node);
            }else { //RL
                rotate(grand, grand, node.left, node, node.right, parent);
            }
        }
    }

    private void rotate(
            Node<E> r, // 子树的根节点
            Node<E> b, Node<E> c,
            Node<E> d,
            Node<E> e, Node<E> f) {
        // 让d成为这棵子树的根节点
        d.parent = r.parent;
        if (r.isLeftChild()) {
            r.parent.left = d;
        } else if (r.isRightChild()) {
            r.parent.right = d;
        } else {
            root = d;
        }

        //b-c
        b.right = c;
        if (c != null) {
            c.parent = b;
        }
        updateHeight(b);

        // e-f
        f.left = e;
        if (e != null) {
            e.parent = f;
        }
        updateHeight(f);

        // b-d-f
        d.left = b;
        d.right = f;
        b.parent = d;
        f.parent = d;
        updateHeight(d);
    }

    /**
     * @param grand 左旋转
     */
    private void rotateLeft(Node<E> grand) {
        Node<E> parent = grand.right;
        Node<E> child = parent.left;
        grand.right = child;
        parent.left = grand;
        afterRotate(grand, parent, child);
    }

    /**
     * @param grand 右旋转
     */
    private void rotateRight(Node<E> grand) {
        Node<E> parent = grand.left;
        Node<E> child = parent.right;
        grand.left = child;
        parent.right = grand;
        afterRotate(grand, parent, child);
    }

    /**
     * 左右旋转中需要做的事情
     */
    private void afterRotate(Node<E> grand, Node<E> parent, Node<E> child) {
        //让parent成为子树的根节点
        parent.parent = grand.parent;
        if (grand.isLeftChild()) {
            grand.parent.left = parent;
        } else if (grand.isRightChild()) {
            grand.parent.right = parent;
        }else {
            //父节点为null 即grand是root节点
            root = parent;
        }
        //更新child的parent
        if (child != null) {
            child.parent = grand;
        }
        //更新grand的parent
        grand.parent = parent;
        //更新高度
        updateHeight(grand);
        updateHeight(parent);
    }

    private static class AVLNode<E> extends Node<E> {
        int height = 1;

        public AVLNode(E element, Node<E> parent) {
            super(element, parent);
        }

        /**
         * @return 获取平衡因子
         */
        public int balanceFactor() {
            int leftHeight = left == null ? 0 : ((AVLNode<E>) left).height;
            int rightHeight = right == null ? 0 : ((AVLNode<E>) right).height;
            return leftHeight - rightHeight;
        }

        /**
         * 更新高度
         */
        public void updateHeight() {
            int leftHeight = left == null ? 0 : ((AVLNode<E>) left).height;
            int rightHeight = right == null ? 0 : ((AVLNode<E>) right).height;
            height = 1 + Math.max(leftHeight, rightHeight);
        }

        public Node<E> tallerChild() {
            int leftHeight = left == null ? 0 : ((AVLNode<E>) left).height;
            int rightHeight = right == null ? 0 : ((AVLNode<E>) right).height;
            if (leftHeight < rightHeight) {
                return right;
            }
            if (leftHeight > rightHeight) {
                return left;
            }
            return isLeftChild() ? left : right;
        }
    }
}
