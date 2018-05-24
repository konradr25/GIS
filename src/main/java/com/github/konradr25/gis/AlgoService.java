package com.github.konradr25.gis;

import com.google.common.collect.Sets;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.Graphs;
import com.google.common.graph.MutableGraph;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class AlgoService {
    public void run(MutableGraph<Integer> graph) {
        log.info("Algo started...");
        MutableGraph<Integer> tree = createTreeUsingBfs(graph, 0);
        Set<Set<EndpointPair<Integer>>> base = createBase(graph, tree);
    }

    public MutableGraph<Integer> createTreeUsingBfs(MutableGraph<Integer> graph, int startNode) {
        MutableGraph<Integer> tree = GraphBuilder.undirected().build();
        createTree(graph, tree, Sets.newHashSet(startNode), startNode);
        return tree;
    }

    private void createTree(MutableGraph<Integer> graph, MutableGraph<Integer> tree, Set<Integer> visited, int startNode) {
        Set<Integer> successors = graph.successors(startNode);
        Set<Integer> notVisitedYet = Sets.difference(successors, visited).immutableCopy();
        notVisitedYet.forEach(node -> {
            tree.putEdge(startNode, node);
            visited.add(node);
        });

        notVisitedYet.forEach(node -> createTree(graph, tree, visited, node));
    }

    /**
     *
     * @param graph
     * @param tree
     * @return Lista list krawędzi
     */
    private Set<Set<EndpointPair<Integer>>> createBase(MutableGraph<Integer> graph, MutableGraph<Integer> tree) {
        Set<EndpointPair<Integer>> allEdges = tree.edges();
        Set<Set<EndpointPair<Integer>>> allBaseEdges = new HashSet<>();

        allEdges.forEach(edge -> {
            Integer firstNode = edge.nodeU();
            Integer secondNode = edge.nodeV();
            MutableGraph<Integer> splittedTree = Graphs.copyOf(tree);

            splittedTree.removeEdge(firstNode, secondNode);
            Set<Integer> v1Nodes = getAllSuccessors(splittedTree, new HashSet<>(), firstNode);
            Set<Integer> v2Nodes = getAllSuccessors(splittedTree, new HashSet<>(), secondNode);

            Set<EndpointPair<Integer>> baseEdges = new HashSet<>();

            v1Nodes.forEach(v1 -> {
                v2Nodes.forEach(v2 -> {
                    if (graph.hasEdgeConnecting(v1, v2))
                        baseEdges.add(EndpointPair.unordered(v1, v2));
                });
            });

            allBaseEdges.add(baseEdges);
        });

        return allBaseEdges;
    }

    private Set<Integer> getAllSuccessors(MutableGraph<Integer> splittedTree, Set<Integer> result, Integer node) {

        result.add(node);
        Set<Integer> successors = splittedTree.successors(node);

        //zadnych nowych potencjalnych wierzcholkow
        if (successors.isEmpty() || result.containsAll(successors))
            return result;

        for (Integer sNode : successors) {
            if (!result.contains(sNode))
                getAllSuccessors(splittedTree, result, sNode);
        }

        return result;
    }
}
