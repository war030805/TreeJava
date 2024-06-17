package tree.trees;

import tree.Node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * This is a class that has a tree structure with {@link Node} represented as links between nodes.
 *
 * <p>The {@code pointer} is a {@link Node} that points to a given {@link Node} that can be set and moved
 * via a relative path or a an absolute path (from the {@code root})<p>
 * this tree will do everything recursively if you dont want that use {@link AdvancedTree} it will not
 * generate a {@link StackOverflowError} but it is slower and will use more memory than this tree
 * </p></p>
 *
 * @param <E> the type of elements held in this tree
 * @author Warre Wilms
 * @see Node
 * @see StackOverflowError
 * @see java.util.TreeSet
 * @see java.util.TreeMap
 */
public class Tree<E> implements Serializable {
    protected Node<E> root;
    protected int size;
    protected Node<E> pointer;

    public Tree() {
        root = new Node<>(null);
        size = 0;
        pointer = root;
    }

    /**
     * @param tree this is the tree
     */
    public Tree(Tree<E> tree) {
        Tree<E> newTree=tree.makeCopyOfTree();
        this.root = newTree.root;
        this.size = newTree.size;
        this.pointer = newTree.pointer;
    }

    /**
     * This will add the given element to the place of the {@code pointer}
     *
     * @param e           the element you want to add
     * @param movePointer if {@code true} then the {@code pointer} will be the {@link Node} of the newly created node
     *                    with the element
     */
    public void insert(E e, boolean movePointer) {
        size++;
        if (root.getElement() == null) {
            root.setElement(e);
            return;
        }
        Node<E> newNode = new Node<>(e, pointer);
        pointer.addChild(newNode);

        if (movePointer) {
            pointer = newNode;
        }
    }

    /**
     * This will add the given element to the place of the pointer
     *
     * @param e the element you want to add
     */
    public void insert(E e) {
        insert(e, false);
    }

    /**
     * This will move the {@code pointer} to the {@code parent} of the {@code pointer}
     *
     * @return {@code true} if the {@code parent} of the {@code pointer} not {@code NULL} is
     */
    public boolean movePointerUp() {
        Node<E> nextPointer = pointer.getParent();
        if (nextPointer == null) {
            return false;
        }
        pointer = nextPointer;
        return true;
    }

    /**
     * This sets the {@code pointer} to the {@code root} of the {@code Tree}
     */
    public boolean movePointerDown(E e) {
        ArrayList<Node<E>> arrayList = pointer.getChildren();
        for (Node<E> eNode : arrayList) {
            if (eNode.getElement().equals(e)) {
                pointer = eNode;
                return true;
            }
        }
        return false;
    }

    /**
     * this wil move the pointer to the index given
     *
     * @param i is the index of the element you want to move to
     * @return true when he can move down
     */
    public boolean movePointerDown(int i) {
        ArrayList<Node<E>> arrayList = pointer.getChildren();
        if (i >= arrayList.size()) {
            return false;
        }
        pointer = arrayList.get(i);
        return true;
    }

    /**
     * it will move up when the index is -1 otherwise it will move to the index given
     *
     * @param index is the index of the next node or -1 that means he moves up
     * @return {@code true} when he can move that direction
     */
    public boolean movePointer(int index) {
        if (index == -1) {
            return movePointerUp();
        }
        return movePointerDown(index);
    }

    /**
     * this will move the pointer to the given path in the array. Its a relative path so the path will be from the point
     * of view of the array it wil move in the way explaind in {@link Tree#movePointer(int)}
     *
     * @param relativePad the array of the path.
     * @return {@code true} when he can move that direction completely.
     */
    public boolean setPointerToRelativePad(int[] relativePad) {
        for (int j : relativePad) {
            if (!movePointer(j)) {
                return false;
            }
        }
        return true;
    }

    /**
     * first it wil set the pointer to the root, Then it will do the same as {@link Tree#setPointerToRelativePad(int[])}
     *
     * @param absoluthePath
     * @return {@code true} (as specified by {@link Tree#setPointerToRelativePad(int[])})
     */
    public boolean setPointerToAbsolutePad(int[] absoluthePath) {
        setPointerToRoot();
        return setPointerToRelativePad(absoluthePath);
    }

    /**
     * it will move the pointer up until it cannot move anymore given in {@link Tree#movePointerUp()}
     *
     * @return a {@link ArrayList} of {@code Integer} with the absolute path.
     */
    public List<Integer> getAbsolutePadToPointer() {
        ArrayList<Integer> pad = new ArrayList<>();
        Node<E> currentPointer = pointer;
        Node<E> lastPointer = pointer;
        while (movePointerUp()) {
            pad.add(pointer.getChildren().indexOf(lastPointer));
            lastPointer = pointer;
        }
        pointer = currentPointer;
        Collections.reverse(pad);
        return pad;
    }

    /**
     * first it wil set the pointer to the root, Then it will do the same as {@link Tree#setPointerToRelativePad(int[])}
     *
     * @param absoluthePath
     * @return {@code true} (as specified by {@link Tree#setPointerToRelativePad(int[])})
     */
    public boolean setPointerToAbsolutePad(List<Integer> absoluthePath) {
        setPointerToRoot();
        return setPointerToRelativePad(absoluthePath);
    }

    /**
     * this will move the pointer to the given path in the array. Its a relative path so the path will be from the point
     * of view of the array it wil move in the way explaind in {@link Tree#movePointer(int)}
     *
     * @param relativePad the array of the path.
     * @return {@code true} when he can move that direction completely.
     */
    public boolean setPointerToRelativePad(List<Integer> relativePad) {
        for (int j : relativePad) {
            if (!movePointer(j)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets the pointer to the root
     */
    public void setPointerToRoot() {
        pointer = root;
    }

    /**
     * @return {@code E} the element in the pointer
     */
    public E getAtPointer() {
        return pointer.getElement();
    }

    /**
     * @param relativePad this is the path relative to the element you want to return
     * @return {@code E} the element at the end of the path
     */
    public E getElementAtRelativePath(int[] relativePad) {
        Node<E> currentPointer = pointer;
        return getElementAtPath(relativePad, currentPointer);
    }

    private E getElementAtPath(int[] path, Node<E> currentPointer) {
        setPointerToRelativePad(path);
        E e = pointer.getElement();
        pointer = currentPointer;
        return e;
    }

    /**
     * @param absoluthePath this is the path from the root to the element
     * @return {@code E} the element at the end of the path
     */
    public E getElementAtAbsolutePath(int[] absoluthePath) {
        Node<E> currentPointer = pointer;
        setPointerToRoot();
        return getElementAtPath(absoluthePath, currentPointer);
    }

    /**
     * this will search the element recursive until it finds it
     *
     * @param e the element you want to search
     * @return {@link LinkedList} of {@code Integers} to the given element from the root
     * @throws StackOverflowError because its searches the element recursive.
     */
    public List<Integer> getAbsolutePadToElement(E e) {
        LinkedList<Integer> pad = new LinkedList<>();
        Node<E> currentPointer = pointer;
        setPointerToRoot();
        try {
            pathToElement(pad, pointer, e, -1);
        } finally {
            pointer = currentPointer;
        }


        return pad;
    }

    /**
     * it will search the element until it finds it
     * <p>it will search the element under it and when it doesn't find it it will move the pointer up explained in
     * {@link Tree#movePointerUp()} and then searches the elements under it except for the node he just searcht</p>
     *
     * @param e the element you want to find
     * @return {@link LinkedList} of {@code Integers} that the path is to the element
     * @throws StackOverflowError explained in {@link #pathToElement(LinkedList, Node, Object, int)}
     */
    public List<Integer> getRelativePadToElement(E e) {
        LinkedList<Integer> pad = new LinkedList<>();
        Node<E> currentPointer = pointer;
        Node<E> lastPointer = pointer;
        int skipNode = -1;
        try {
            while (!pathToElement(pad, pointer, e, skipNode)) {
                if (!movePointerUp()) {
                    break;
                }
                pad.addFirst(-1);
                skipNode = pointer.getChildren().indexOf(lastPointer);

            }
        } finally {
            pointer = currentPointer;
        }
        return pad;
    }

    /**
     * it searches the element recursively until it finds it
     *
     * @param path    a {@link LinkedList} of {@code Integer} that the path is to the element
     * @param node    the {@link Node} you want to search under
     * @param e       the element he searches
     * @param skipInt the {@code Integer} of the array he already searched.
     * @return {@code true} when it found the element
     * @throws StackOverflowError because it searches the element recursively
     */
    protected boolean pathToElement(LinkedList<Integer> path, Node<E> node, E e, int skipInt) {
        ArrayList<Node<E>> arrayList = node.getChildren();
        if (node.getElement().equals(e)) {
            return true;
        }
        for (int i = 0; i < arrayList.size(); i++) {
            if (i != skipInt) {
                path.add(i);
                if (pathToElement(path, arrayList.get(i), e, -1)) {
                    return true;
                }
                path.removeLast();
            }
        }
        return false;
    }

    /**
     * this is almost the same as {@link #getAbsolutePadToElement(Object)} but it returns an {@link ArrayList} with
     * {@link LinkedList} in and in the LinkedList contains the path to the given {@code Element}.
     *
     * @param e the element you want to have the paths to
     * @return an {@link ArrayList} with {@link LinkedList} with the absolut Paths to the elements
     * @throws StackOverflowError because it searches the element recursively
     */
    public List<List<Integer>> getAbsolutePatsToElement(E e) {
        List<List<Integer>> pats = new ArrayList<>();
        Node<E> currentPointer = pointer;
        setPointerToRoot();
        try {
            patsToElement(pats, new LinkedList<>(), pointer, e, -1, false);
        } finally {
            pointer = currentPointer;
        }


        return pats;
    }

    /**
     * see {@link #getAbsolutePadToElement(Object)}
     *
     * @param e the element you want to have the paths to
     * @return an {@link ArrayList} with {@link LinkedList} with the absolut Paths to the elements
     * @throws StackOverflowError because it searches the element recursively
     */
    public List<List<Integer>> getRelativePathsToElement(E e) {
        List<List<Integer>> pats = new ArrayList<>();
        Node<E> currentPointer = pointer;
        Node<E> lastPointer = pointer;
        int skipNode = -1;
        LinkedList<Integer> path = new LinkedList<>();
        try {
            while (true) {
                patsToElement(pats, path, pointer, e, skipNode, false);
                if (!movePointerUp()) {
                    break;
                }
                path.addFirst(-1);
                skipNode = pointer.getChildren().indexOf(lastPointer);
                lastPointer = pointer;
            }
        } finally {
            pointer = currentPointer;
        }

        return pats;
    }

    /**
     * it searches all the elements in the Tree
     *
     * @param pats        an {@link ArrayList} of {@link LinkedList}
     * @param currentPath the same as in {@link #pathToElement(LinkedList, Node, Object, int)}
     * @param node        the same as in {@link #pathToElement(LinkedList, Node, Object, int)}
     * @param e           the same as in {@link #pathToElement(LinkedList, Node, Object, int)}
     * @param skipInt     the same as in {@link #pathToElement(LinkedList, Node, Object, int)}
     * @param found       is {@code true} when it found the element (is used for the future)
     * @return {@code true} when it found the element
     * @throws StackOverflowError because it searches the element recursively
     */
    protected boolean patsToElement(List<List<Integer>> pats, LinkedList<Integer> currentPath, Node<E> node, E e,
                                    int skipInt, boolean found) {
        if (node.getElement().equals(e)) {
            found = true;
            pats.add(new LinkedList<>(currentPath));
        }
        ArrayList<Node<E>> arrayList = node.getChildren();
        for (int i = 0; i < arrayList.size(); i++) {
            if (i != skipInt) {
                currentPath.add(i);
                found = patsToElement(pats, currentPath, arrayList.get(i), e, -1, found);
                currentPath.removeLast();
            }
        }
        return found;
    }

    /**
     * @return the {@code size} of the tree
     */
    public int getSize() {
        return size;
    }

    public boolean remove(E e) {
        Node<E> currentNode = root;
        return removeNode(currentNode, e);
    }
    public boolean removeNodeAtRelativePath(List<Integer> path) {
        Node<E> currentPointer = pointer;
        boolean found;
        try {
            found=setPointerToRelativePad(path);
            if (found) {
                Node<E> parent = pointer.getParent();
                parent.getChildren().remove(pointer);
            }
        } finally {
            pointer = currentPointer;
        }
        return found;
    }
    public boolean removeNodeAtAbsolutePathPath(List<Integer> path) {
        Node<E> currentPointer = pointer;
        boolean found;
        try {
            found=setPointerToAbsolutePad(path);
            if (found) {
                Node<E> parent = pointer.getParent();
                parent.getChildren().remove(pointer);
            }
        } finally {
            pointer = currentPointer;
        }
        return found;
    }

    protected boolean removeNode(Node<E> node, E e) {
        if (node.getElement().equals(e)) {
            Node<E> parent = node.getParent();
            parent.getChildren().remove(node);
            return true;
        }
        ArrayList<Node<E>> arrayList = node.getChildren();
        for (Node<E> eNode : arrayList) {
            if (removeNode(eNode, e)) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(E e) {
        return contains(root, e);
    }

    protected boolean contains(Node<E> node, E e) {
        ArrayList<Node<E>> arrayList = node.getChildren();
        if (node.getElement().equals(e)) {
            return true;
        }
        for (Node<E> eNode : arrayList) {
            if (contains(eNode, e)) {
                return true;
            }
        }
        return false;
    }

    public List<E> toList() {
        List<E> list = new ArrayList<>(size);
        toList(list);
        return list;
    }

    public void toList(List<E> list) {
        toList(list, root);
    }

    protected void toList(List<E> list, Node<E> node) {
        for (Node<E> child : node.getChildren()) {
            list.add(child.getElement());
            toList(list, child);
        }
    }

    /**
     * it will make an Array of the elements
     *
     * @return
     * @throws StackOverflowError it will call {@link #toArray(Object[], Node, int)} recursively so it can generate
     *                            an StackOverflowError.
     */
    public E[] toArray() {
        Object[] array;
        array = new Object[size];
        toArray(array, root, 0);
        return (E[]) array;
    }

    /**
     * it will make an Array of the elements
     *
     * @return
     * @throws StackOverflowError it will call this recursively, so it can generate
     *                            an StackOverflowError.
     */
    protected int toArray(Object[] elements, Node<E> node, int index) {
        if (index == 0) {
            elements[0] = node.getElement();
            index++;
        }
        for (Node<E> next : node.getChildren()) {
            elements[index] = next.getElement();

            index = toArray(elements, next, ++index);
        }
        return index;
    }

    /**
     * this adds a copy of {@link Node} of the tree
     *
     * @param tree the tree you want to add
     * @throws StackOverflowError it will make a copy of the trees root and therefor it has to make a copy of all the children
     *                            and that has to make a copy of his children, and it does that recursively
     */
    public void addTree(Tree<E> tree) {
        pointer.addChild(tree.makeCopyOfTree().root);
        size += tree.getSize();
    }

    public Tree<E> makeCopyOfTree() {
        Tree<E> tree = new Tree<>();
        List<Integer> pathToPointer = getAbsolutePadToPointer();
        tree.root = root.makeCoppy(null);
        tree.setPointerToAbsolutePad(pathToPointer);
        tree.size = size;
        return tree;
    }

    private Tree<E> makeCopyOfTreeFromPointer() {
        Tree<E> tree = new Tree<>();
        tree.root = pointer.makeCoppy(null);
        tree.pointer = tree.root;
        tree.size = root.getSizeUnderIt();
        return tree;
    }

    /**
     * it returns the {@code String} to a tree structure it does that in a recoursive way so it can throw a
     * {@link StackOverflowError} if that happens it will return Tree: size: {@code size} en de message of the error
     *
     * @return {@code String}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        try {
            buildString(sb, root, "", true);
        } catch (Exception e) {
            return String.format("Tree: size: %d %s", size, e.getMessage());
        }
        return sb.toString();
    }
    public void clear() {
        root=new Node<>(null);
        pointer=root;
        size=0;
    }

    protected void buildString(StringBuilder sb, Node<E> node, String prefix, boolean isTail) {
        addToBuilder(sb, node, prefix, isTail);
        List<Node<E>> children = node.getChildren();
        for (int i = 0; i < children.size() - 1; i++) {
            buildString(sb, children.get(i), prefix + (isTail ? "    " : "│   "), false);
        }
        if (!children.isEmpty()) {
            buildString(sb, children.get(children.size() - 1), prefix + (isTail ? "    " : "│   "), true);
        }
    }

    protected static <E> void addToBuilder(StringBuilder sb, Node<E> node, String prefix, boolean isTail) {
        if (node.getParent() != null) {
            sb.append(prefix).append(isTail ? "└── " : "├── ").append(node.getElement()).append("\n");
        } else {
            sb.append(prefix).append(node.getElement()).append("\n");
        }
    }
}
