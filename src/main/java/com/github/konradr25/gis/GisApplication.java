package com.github.konradr25.gis;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.MutableGraph;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@Slf4j
@SpringBootApplication
public class GisApplication {

    private static String[] args;

    private final FileService fileService;
    private final AlgoService algoService;
    private final GraphVisualizationService graphVisualizationService;

    @Autowired
    public GisApplication(FileService fileService, AlgoService algoService, GraphVisualizationService graphVisualizationService) {
        this.fileService = fileService;
        this.algoService = algoService;
        this.graphVisualizationService = graphVisualizationService;
    }

    public static void main(String[] args) {
        GisApplication.args = args;
        SpringApplication.run(GisApplication.class, args);
    }

    @PostConstruct
    private void init() {
        if (args == null || args.length == 0) {
            log.info("No args passed...");
        } else {
            MutableGraph<Integer> graph = fileService.loadFilesAndBuildGraph(args);
            Set<List<EndpointPair<Integer>>> quasiCuts = algoService.run(graph);
            String output = graphVisualizationService.visualizeQuasiCut(quasiCuts);
            try {
                fileService.writeTextToFile(output);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
