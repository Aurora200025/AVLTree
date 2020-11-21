package AVLTree;


import java.util.Comparator;

public class BinarySearchTree<E> extends BinaryTree<E> {

    private Comparator<E> comparator;

    public BinarySearchTree() {
        this(null);
    }
    public BinarySearchTree(Comparator<E> comparator) {
            this.comparator = comparator;
    }


    /**
     * @param element 添加元素
     */
    public void add(E element) {
        elementNotNullCheck(element);
        if (root == null) {
            root = createNode(element, null);
            afterAdd(root);
            size++;
            return;
        }
        //添加的不是第一个节点
            Node<E> parent = root;
            Node<E> node = root;
            int cmp = 0;
            do {
                cmp = compare(element, node.element);
                parent = node;
                if (cmp > 0) {
                    node = node.right;
                }else if (cmp < 0) {
                    node = node.left;
                }else { //cmp == 0
                    node.element = element;
                    return;
                }
            } while (node != null);
            Node<E> newNode = createNode(element, parent);
            if (cmp > 0) {
                parent.right = newNode;
            }else {
                parent.left = newNode;
            }
            size++;

            afterAdd(newNode);
    }

    /**
     * @param node 添加node之后的调整--AVLTree
     */
    protected void afterAdd(Node<E> node) {}

    /**
     * @param element 删除元素
     */
    public void remove(E element) {
        remove(node(element));
    }

    private void remove(Node<E> node) {
        if (node == null) {
            return;
        }
        //节点不为null
        if (node.hasTwoChildren()) {//度为2的节点
            Node<E> s = successor(node);
//            System.out.println(s);
            node.element = s.element;
            node = s;
        }
        //node的度为1或者为0
        Node<E> replacement = node.left != null ? node.left : node.right;
        if (replacement != null) {//度为1的节点
            replacement.parent = node.parent;
            if (node.parent == null) {//node是度为1的节点并且是根节点
                root = replacement;
            }else if (node == node.parent.left) {
                node.parent.left = replacement;
            }else {
                node.parent.right = replacement;
            }
            afterRemove(node);
        }else if (node.parent == null) {//叶子节点同时也是根节点
            root = null;
        }else {//叶子节点但不是根节点
            if (node == node.parent.left) {
                node.parent.left = null;
            }else {
                node.parent.right = null;
            }
            afterRemove(node);
        }
        size--;
    }

    /**
     * @param node 删除某个节点后要做出调整
     */
    public void afterRemove(Node<E> node) {}

    /**
     * @return 找到节点node
     */
    private Node<E> node(E element) {
        Node<E> node = root;
        while (node != null) {
            int cmp = compare(element, node.element);
            if (cmp == 0) {//则相等
                return node;
            }
            if (cmp > 0) {//element > node.element
                node = node.right;
            }else { //cmp < 0 element < node.element
                node = node.left;
            }
        }
        return null;
    }

    /**
     * @param element // 是否包含某元素
     * @return true代表包含 false则不包含
     */
    public boolean contains(E element) {
        return node(element) != null;
    }



    //=================================================
    /**
     * 比较元素大小
     * @return 返回值等于0，则e1=e2； 返回值大于0，则e1>e2； 返回值小于0，则e1<e2
     */
    private int compare(E e1, E e2) {
        if (comparator != null) {
            return comparator.compare(e1, e2);
        }
        return ((Comparable<E>) e1).compareTo(e2);
    }
    /**
     * @param element 检测所传的参数是否为null
     */
    private void elementNotNullCheck(E element) {
        if (element == null) {
            throw new IllegalArgumentException("参数不能为null");
        }
    }



}
