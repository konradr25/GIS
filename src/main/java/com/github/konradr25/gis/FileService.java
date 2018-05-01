package com.github.konradr25.gis;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class FileService {
    private FileValidatorService fileValidatorService;

    public Graph loadFilesAndBuildGraph(String[] args) {
        log.info("Loading files started...");
        List<String> loadedFile = null;
        try {
            loadedFile = loadFiles(args);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("Building graph started...");
        return parseText(loadedFile);
    }

    private List<String> loadFiles(String[] args) throws IOException {
        fileValidatorService.validateArgs(args);
        if (isFile(args)) {
            return Files.lines(Paths.get(args[0])).collect(Collectors.toList());
        } else {
            return Lists.newArrayList();
        }
    }

    private Graph parseText(List<String> loadedFile) {
        MutableGraph<Object> graph = GraphBuilder.directed().build();
        for (int lineIndex = 0; lineIndex < loadedFile.size(); lineIndex++) {
            List<String> splitedLine = Splitter.on(" ").splitToList(loadedFile.get(lineIndex));
            for (int columnIndex = 0; columnIndex < splitedLine.size(); columnIndex ++) {
                if (Integer.parseInt(splitedLine.get(columnIndex)) != 0) {
                    graph.putEdge(lineIndex, columnIndex);
                }
            }

        }
        return graph;
    }

    private boolean isFile(String[] args) {
        return args != null && args.length == 1;
    }
}
