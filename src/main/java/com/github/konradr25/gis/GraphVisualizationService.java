package com.github.konradr25.gis;

import com.google.common.graph.EndpointPair;

import java.util.List;
import java.util.Set;

public class GraphVisualizationService {

    String visualizeQuasiCut(Set<List<EndpointPair<Integer>>> quasiCuts) {

        StringBuilder stringBuilder = new StringBuilder();
        for (List<EndpointPair<Integer>> quasiCut : quasiCuts) {
            quasiCut.forEach(pair -> {
                stringBuilder.append(pair.toString()).append(" ");
            });
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }
}
