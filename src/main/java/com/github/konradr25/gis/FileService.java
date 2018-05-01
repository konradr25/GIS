package com.github.konradr25.gis;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.graph.*;
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

    public MutableValueGraph<Object, Integer> loadFilesAndBuildGraph(String[] args) {
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

    private MutableValueGraph<Object, Integer>  parseText(List<String> loadedFile) {
        MutableValueGraph<Object, Integer> graph = ValueGraphBuilder.undirected().build();
        for (int lineIndex = 0; lineIndex < loadedFile.size(); lineIndex++) {
            List<Integer> splitedLine = Splitter.on(" ").splitToList(loadedFile.get(lineIndex)).stream()
                    .map(Integer::parseInt).collect(Collectors.toList());

            if (splitedLine.size() == 3) {
                graph.putEdgeValue(splitedLine.get(0), splitedLine.get(1), splitedLine.get(2));
            } else if (splitedLine.size() == 1) {
                graph.addNode(splitedLine.get(0));
            }
        }
        return graph;
    }

    private boolean isFile(String[] args) {
        return args != null && args.length == 1;
    }
}
