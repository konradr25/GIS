package com.github.konradr25.gis;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.Graphs;
import com.google.common.graph.MutableGraph;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class AlgoService {
    public Set<List<EndpointPair<Integer>>> run(MutableGraph<Integer> graph) {
        log.info("Algo started...");
        MutableGraph<Integer> tree = createTreeUsingBfs(graph, 0);
        List<List<EndpointPair<Integer>>> base = createBase(graph, tree);
        Set<List<EndpointPair<Integer>>> quasiCuts = findQuasiCut(base);
        return quasiCuts;

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
    private List<List<EndpointPair<Integer>>> createBase(MutableGraph<Integer> graph, MutableGraph<Integer> tree) {
        List<EndpointPair<Integer>> allEdges = new ArrayList<>(tree.edges());
        List<List<EndpointPair<Integer>>> allBaseEdges = Lists.newArrayList();

        allEdges.forEach(edge -> {
            Integer firstNode = edge.nodeU();
            Integer secondNode = edge.nodeV();
            MutableGraph<Integer> splittedTree = Graphs.copyOf(tree);

            splittedTree.removeEdge(firstNode, secondNode);
            Set<Integer> v1Nodes = getAllSuccessors(splittedTree, new HashSet<>(), firstNode);
            Set<Integer> v2Nodes = getAllSuccessors(splittedTree, new HashSet<>(), secondNode);

            List<EndpointPair<Integer>> baseEdges = new ArrayList<>();

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

    private Set<List<EndpointPair<Integer>>> findQuasiCut(List<List<EndpointPair<Integer>>> base) {
        Set<List<EndpointPair<Integer>>> quasiCuts = Sets.newHashSet();
        findQuasiCut(quasiCuts, base, Lists.newArrayList(1));
        findQuasiCut(quasiCuts, base, Lists.newArrayList(2));
        return quasiCuts;
    }

    private void findQuasiCut(Set<List<EndpointPair<Integer>>> quasiCuts, List<List<EndpointPair<Integer>>> base,
                              List<Integer> attributes) {
        if (attributes.size() < base.size()) {
            List<Integer> attributesCopy = Lists.newArrayList(attributes);
            List<Integer> attributesWithOne = Lists.newArrayList(1);
            List<Integer> attributesWithZero= Lists.newArrayList(0);
            attributesWithOne.addAll(attributesCopy);
            attributesWithZero.addAll(attributesCopy);
            findQuasiCut(quasiCuts, base, attributesWithOne);
            findQuasiCut(quasiCuts, base, attributesWithZero);
        } else {
            List<EndpointPair<Integer>> quasiCut = Lists.newArrayList();
            for (int i = 0; i < attributes.size(); ++i) {
                if (attributes.get(i) == 1) {
                    quasiCut.addAll(Helper.symetricAdd(quasiCut, base.get(i)));
                }
            }

            quasiCuts.add(quasiCut);
        }

    }
}
