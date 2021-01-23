package structures;

import graphs.Edge;
import graphs.IGraph;

import java.util.Set;
import java.util.*;


/**
 * @param <V> vertex
 * @author Michael Gulchuk
 * @version 1.0
 */
public class DirectedGraph<V> implements IGraph<V>
{
    public static final double DOUBLE = 1.5;
    public static final double DOUBLE1 = 0.5;
    private int[][] matrix;
    private final Stack<Integer> stack = new Stack<>();
    private final Bijection<V,Integer> map = new Bijection<>();
    private int vertices;
    private int edges;
    private static final int SIZE = 10;

    /**
     * creates a new directed graph
     */
    public DirectedGraph(){
        // create
        matrix = new int[SIZE][SIZE];

        // stack
        stack.push(0);

        // counter
        vertices = 0;
        edges = 0;

    }

    /**
     * @param initialSize initial size of matrix
     */
    // used in the test
    public DirectedGraph(int initialSize){
        // create
        matrix = new int[initialSize][initialSize];

        // stack
        stack.push(0);

        // counter
        vertices = 0;
        edges = 0;

    }

    @Override
    public boolean addVertex(V vertex)
    {
        // check if it exists
        if(containsVertex(vertex)){
            return false;
        }

        // top of stack
        int index = stack.pop();

        //add to map
        if(stack.isEmpty()){
            stack.push(index + 1);
        }

        if(vertexSize() > matrix.length * DOUBLE1){
            int[][] graph = new int[(int)(matrix.length * DOUBLE)][(int)(matrix.length * DOUBLE)];

            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix.length; j++) {
                    graph[i][j] = matrix[i][j];
                }
            }

            matrix = graph;
        }

        map.add(vertex, index);
        vertices++;

        return true;
    }

    @Override
    public boolean addEdge(V source, V destination, int weight)
    {
        // preconditions
        if(weight <= -1){
            throw new IllegalArgumentException("Weight is negative");
        }
        // vertices are missing
        if((!map.containsKey(source)) || (!map.containsKey(destination)) || (containsEdge(source, destination))){
            return false;
        }

        matrix[map.getValue(source)][map.getValue(destination)] = weight;
        edges++;

        return true;
    }

    @Override
    public int vertexSize()
    {
        return vertices;
    }

    @Override
    public int edgeSize()
    {
        return edges;
    }

    @Override
    public boolean containsVertex(V vertex)
    {
        return map.containsKey(vertex);
    }

    @Override
    public boolean containsEdge(V source, V destination)
    {
        if(!map.containsKey(source) || !map.containsKey(destination)) {
            return false;
        }

        return matrix[map.getValue(source)][map.getValue(destination)] > 0;
    }

    @Override
    public int edgeWeight(V source, V destination)
    {
        int vertices = map.getValue(source);
        int vertices2 = map.getValue(destination);

        return matrix[vertices][vertices2];
    }

    @Override
    public Set<V> vertices()
    {
        return new HashSet<>(map.keySet());
    }

    @Override
    public Set<Edge<V>> edges()
    {
        Set<Edge<V>> results = new HashSet<>();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if(matrix[i][j] != 0){
                    results.add(new Edge<>(map.getKey(i), map.getKey(j),
                            edgeWeight(map.getKey(i), map.getKey(j))));
                }
            }
        }

        return results;
    }

    @Override
    public boolean removeVertex(V vertex)
    {
        if(containsVertex(vertex)){
            matrix[map.getValue(vertex)][map.getValue(vertex)] = 0;

            stack.push(map.getValue(vertex));

            map.removeKey(vertex);

            vertices--;
            return true;
        }

        return false;
    }

    @Override
    public boolean removeEdge(V source, V destination)
    {
        if(containsEdge(source, destination)) {
            matrix[map.getValue((source))][map.getValue(destination)] = 0;
            edges--;

            return true;
        }

        return false;
    }

    @Override
    public void clear()
    {
        map.clear();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if(matrix[i][j] != 0){
                    matrix[i][j] = 0;
                }
            }
        }

        edges = 0;
        vertices = 0;
    }

    @Override
    public String toString() {
        return "DirectedGraph{" +
                "matrix=" + Arrays.toString(matrix) +
                ", stack=" + stack +
                ", map=" + map +
                ", vertices=" + vertices +
                ", edges=" + edges +
                '}';
    }
}
