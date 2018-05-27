package com.github.konradr25.gis;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AlgoServiceTests {

    @Test
    public void createTreeTest() {
        //GIVEN
        MutableGraph<Integer> graph = GraphBuilder.undirected().build();
        graph.putEdge(1, 3);
        graph.putEdge(1, 2);
        graph.putEdge(2, 5);
        graph.putEdge(2,4);
        graph.putEdge(4,6);
        graph.putEdge(4, 5);
        graph.putEdge(5, 6);
        graph.putEdge(3, 5);

        AlgoService algoService = new AlgoService();

        //WHEN
        MutableGraph<Integer> treeUsingBfs = algoService.createTreeUsingBfs(graph, 1);

        //THEN
        assertNotNull(treeUsingBfs);
        assertEquals(5, treeUsingBfs.edges().size());
        assertEquals(6, treeUsingBfs.nodes().size());
    }

    @Test
    public void shouldFindQuasiCut() {
        //GIVEN
        MutableGraph<Integer> graph = GraphBuilder.undirected().build();
        graph.putEdge(0, 1);
        graph.putEdge(0, 2);
        graph.putEdge(1, 2);
        graph.putEdge(2, 3);
        graph.putEdge(2, 4);
        graph.putEdge(3, 5);
        graph.putEdge(3, 6);
        graph.putEdge(5, 6);


        AlgoService algoService = new AlgoService();

        //WHEN
        Set<List<EndpointPair<Integer>>> quasiCuts = algoService.run(graph);

        //THEN
        assertNotNull(quasiCuts);
    }
}
