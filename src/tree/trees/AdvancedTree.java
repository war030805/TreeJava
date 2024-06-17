package tree.trees;

import tree.Node;

import java.util.*;

/**
 * this extends the {@link Tree} class. it will not do things recursive but it will use a stack what will use more
 * memory and it will be slower but as long as you don't have a
 * @see Tree
 * @param <E> see {@link Tree}
 */
public class AdvancedTree<E> extends Tree<E> {

    
    @Override
    protected void buildString(StringBuilder sb, Node<E> node, String prefix, boolean isTail) {
        class NodeWithPrefix {
            final Node<E> node;
            final String prefix;
            final boolean isTail;

            NodeWithPrefix(Node<E> node, String prefix, boolean isTail) {
                this.node = node;
                this.prefix = prefix;
                this.isTail = isTail;
            }
        }
        
        Deque<NodeWithPrefix> stack = new ArrayDeque<>();
        stack.push(new NodeWithPrefix(node, prefix, isTail));
        while (!stack.isEmpty()) {

            NodeWithPrefix current = stack.pop();
            node = current.node;
            prefix = current.prefix;
            isTail = current.isTail;
            addToBuilder(sb, node, prefix, isTail);
            List<Node<E>> children = node.getChildren();
            for (int i = children.size() - 1; i >= 0; i--) {
                stack.push(new NodeWithPrefix(children.get(i), prefix + (isTail ? "    " : "│   "), i == children.size() - 1));
            }
        }
    }

    @Override
    protected boolean pathToElement(LinkedList<Integer> path, Node<E> node, E e, int skipInt) {
        Stack<Node<E>> stack = new Stack<>();
        stack.push(node);
        while (!stack.isEmpty()) {
            node = stack.pop();
            if (node.getElement().equals(e)) {
                Node<E> currentPointer = pointer;
                pointer = node;
                path.addAll(getAbsolutePadToPointer());
                pointer = currentPointer;
                return true;
            }
            List<Node<E>> children = node.getChildren();
            for (int i = children.size() - 1; i >= 0; i--) {
                if (skipInt != i) {
                    stack.push(children.get(i));
                }
            }
        }
        return false;
    }

    @Override
    protected boolean patsToElement(List<List<Integer>> pats, LinkedList<Integer> currentPath, Node<E> node, E e, int skipInt, boolean found) {
        Stack<Node<E>> stack = new Stack<>();
        stack.push(node);
        while (!stack.isEmpty()) {
            node = stack.pop();
            if (node.getElement().equals(e)) {
                Node<E> currentPointer = pointer;
                pointer = node;
                pats.add(getAbsolutePadToPointer());
                pointer = currentPointer;
                found = true;
            }
            List<Node<E>> children = node.getChildren();
            for (int i = children.size() - 1; i >= 0; i--) {
                if (skipInt != i) {
                    stack.push(children.get(i));
                }
            }
            skipInt=-1;
        }
        return found;
    }

    @Override
    public Tree<E> makeCopyOfTree() {
        Tree<E> tree = new AdvancedTree<>();
        List<Integer> pathToPointer = getAbsolutePadToPointer();
        Stack<Node<E>> stack = new Stack<>();
        Stack<Node<E>> newNodeStack = new Stack<>();
        stack.push(root);
        Node<E> newNode=makeNewNode(root,null);
        newNodeStack.push(newNode);
        tree.root=newNode;
        while (!stack.isEmpty()) {
            Node<E> node = stack.pop();
            newNode=newNodeStack.pop();

            List<Node<E>> children = node.getChildren();
            for (Node<E> child : children) {
                node = child;
                stack.push(node);
                node = makeNewNode(node, newNode);
                newNodeStack.push(node);
                newNode.addChild(node);
            }
        }
        tree.setPointerToAbsolutePad(pathToPointer);
        tree.size = size;
        return tree;
    }
    private Node<E> makeNewNode(Node<E> node, Node<E> parrent) {
        Node<E> newNode=new Node<>(parrent);
        newNode.setElement(node.getElement());
        return newNode;
    }

    @Override
    public int getSize() {
        int beginSize=size;
        size = 0;
        try {
            Stack<Node<E>> stack = new Stack<>();
            stack.push(root);
            while (!stack.isEmpty()) {
                Node<E> node = stack.pop();
                size++;
                List<Node<E>> children = node.getChildren();
                for (int i = children.size() - 1; i >= 0; i--) {
                    stack.push(children.get(i));
                }
            }
        } catch (Exception e) {
            size=beginSize;
            throw e;
        }
        return size;
    }

    @Override
    protected boolean contains(Node<E> node, E e) {
        Stack<Node<E>> stack = new Stack<>();
        stack.push(node);
        while (!stack.isEmpty()) {
            node = stack.pop();
            if (node.getElement().equals(e)) {
                return true;
            }
            List<Node<E>> children = node.getChildren();
            for (Node<E> child : children) {
                stack.push(child);
            }
        }
        return false;
    }

    @Override
    protected void toList(List<E> list, Node<E> node) {
        Stack<Node<E>> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            node = stack.pop();
            List<Node<E>> children = node.getChildren();
            list.add(node.getElement());
            for (Node<E> child : children) {
                stack.push(child);
            }
        }
    }

    @Override
    protected int toArray(Object[] elements, Node<E> node, int index) {
        Stack<Node<E>> stack = new Stack<>();
        stack.push(node);
        while (!stack.isEmpty()) {
            node = stack.pop();
            elements[index] = node.getElement();
            List<Node<E>> children = node.getChildren();
            for (Node<E> child : children) {
                stack.push(child);
            }
            index++;
        }
        return index;
    }

    @Override
    protected boolean removeNode(Node<E> node, E e) {
        LinkedList<Integer> path = new LinkedList<>();
        boolean found = pathToElement(path, node, e, -1);
        if (found) {
            Node<E> currentPointer = pointer;
            try {
                setPointerToAbsolutePad(path);
                Node<E> nodeToRemove = pointer;
                movePointerUp();
                pointer.getChildren().remove(nodeToRemove);
            } finally {
                pointer = currentPointer;
            }

        }
        return found;
    }
}
