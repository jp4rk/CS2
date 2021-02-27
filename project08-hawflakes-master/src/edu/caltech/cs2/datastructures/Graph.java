package edu.caltech.cs2.datastructures;

import com.sun.net.httpserver.Filter;
import edu.caltech.cs2.interfaces.*;

public class Graph<V, E> extends IGraph<V, E> {
    private ChainingHashDictionary<V, ChainingHashDictionary<V, E>> graph;
    private ISet<V> vertices;

    public Graph(){
        graph = new ChainingHashDictionary<>(MoveToFrontDictionary::new);
        vertices = new ISet<>(new ChainingHashDictionary<V, Object>(MoveToFrontDictionary::new));
    }

    @Override
    public boolean addVertex(V vertex) {
        if(graph.containsKey(vertex)){
            return false;
        }
        graph.put(vertex, new ChainingHashDictionary<>(MoveToFrontDictionary::new));
        vertices.add(vertex);
        return true;
    }

    @Override
    public boolean addEdge(V src, V dest, E e) {
        if(!graph.containsKey(src) || !graph.containsKey(dest)){
            throw new IllegalArgumentException("e is not a valid edge");
        }
        E add = graph.get(src).put(dest, e);
        return add == null;
    }

    @Override
    public boolean addUndirectedEdge(V n1, V n2, E e) {
        if(!graph.containsKey(n1) || !graph.containsKey(n2)){
            throw new IllegalArgumentException("e is not a valid edge");
        }
        return addEdge(n1, n2, e) && addEdge(n2, n1, e);
    }

    @Override
    public boolean removeEdge(V src, V dest) {
        if(!graph.containsKey(src) || !graph.containsKey(dest)){
            throw new IllegalArgumentException("e is not a valid edge");
        }
        if(graph.get(src).containsKey(dest)){
            graph.get(src).remove(dest);
            return true;
        }
        return false;

    }

    @Override
    public ISet<V> vertices() {
        return vertices;
    }

    @Override
    public E adjacent(V i, V j) {
        if(!graph.containsKey(i) || !graph.containsKey(j)){
            throw new IllegalArgumentException("e is not a valid edge");
        }
        return graph.get(i).get(j);
    }

    @Override
    public ISet<V> neighbors(V vertex) {
        if(!graph.containsKey(vertex)){
            throw new IllegalArgumentException("e is not a valid edge");
        }
        return graph.get(vertex).keySet();
    }
}