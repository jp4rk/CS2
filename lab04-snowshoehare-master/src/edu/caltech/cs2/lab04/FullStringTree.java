package edu.caltech.cs2.lab04;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FullStringTree {

    protected StringNode root;

    protected static class StringNode {
        public final String data;
        public StringNode left;
        public StringNode right;

        public StringNode(String data) {
            this(data, null, null);
        }

        public StringNode(String data, StringNode left, StringNode right) {
            this.data = data;
            this.left = left;
            this.right = right;
            // Ensures that the StringNode is either a leaf or has two child nodes.
            if ((this.left == null || this.right == null) && !this.isLeaf()) {
                throw new IllegalArgumentException("StringNodes must represent nodes in a full binary tree");
            }
        }

        // Returns true if the StringNode has no child nodes.
        public boolean isLeaf() {
            return left == null && right == null;
        }
    }

    protected FullStringTree() {

    }

    public FullStringTree(Scanner in) {
        root = deserialize(in);
    }

    private StringNode deserialize(Scanner in) {
        Scanner temp = in;
        String command = temp.nextLine();
        if (command.charAt(0) == 'I'){
            return new StringNode(command.substring(3), deserialize(temp), deserialize(temp));
        }
        return new StringNode(command.substring(3), null,null);
    }

    public List<String> explore() {
        return explore(root, new ArrayList<String>());
    }

    private List<String> explore(StringNode node, List<String> s){
        if(node.left != null && node.right != null){
            s.add("I: " + node.data);
            explore(node.left, s);
            explore(node.right, s);
        }
        else{
            s.add("L: " + node.data);
        }
        return s;
    }

    public void serialize(PrintStream output) {
        for(String s: explore()){
            System.out.println(s);
        }
    }
}