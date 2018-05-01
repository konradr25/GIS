package com.github.konradr25.gis;

import com.google.common.collect.Sets;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class AlgoService {
    public void run(MutableGraph<Integer> graph) {
        log.info("Algo started...");
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
}
