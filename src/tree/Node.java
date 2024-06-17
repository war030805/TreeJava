package tree;

import tree.trees.Tree;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * this class acts like links between node every node has one {@code parent} en multiple nodes as {@code children}held
 * in an {@link ArrayList}.
 *
 * <p>every {@code node} has an {@code element}</p>
 *
 * @param <E> the type of elements held in this node
 * @author Warre Wilms
 * @see ArrayList
 * @see Tree
 */
public class Node<E> implements Serializable {
    private final Node<E> parent;
    private final ArrayList<Node<E>> children;
    private E element;

    public Node(E element, Node<E> parent) {
        this.element = element;
        children = new ArrayList<>();
        this.parent = parent;
    }



    public Node(Node<E> parent) {
        this(null, parent);

    }

    public void addChild(Node<E> child) {
        children.add(child);

    }

    public Node<E> getParent() {
        return parent;
    }

    public ArrayList<Node<E>> getChildren() {
        return children;
    }

    public E getElement() {
        return element;
    }

    public void setElement(E element) {
        this.element = element;
    }

    public int getSizeUnderIt() {
        int size = 0;
        for (Node<E> child : children) {
            size += child.getSizeUnderIt();
        }
        return size;
    }

    @Override
    public String toString() {
        return "Node{" +
                "element=" + element +
                '}';
    }

    /**
     * this will make a copy of the tree from that node recursively
     *
     * @param parrent the {@code parent} for the new nodes
     * @return {@code node} the new copy of the node
     * @throws StackOverflowError it makes copy's of the node recursively, so it can be that it generates a stack overflow error
     */
    public Node<E> makeCoppy(Node<E> parrent) {
        Node<E> newParent = new Node<>(parrent);
        newParent.element = element;
        for (Node<E> child : children) {
            newParent.addChild(child.makeCoppy(newParent));
        }
        return newParent;
    }
}
