package com.github.konradr25.gis;

import com.google.common.collect.Lists;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class FileService {
    private FileValidatorService fileValidatorService;

    public MutableGraph<Integer> loadFilesAndBuildGraph(String[] args) {
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

    private MutableGraph<Integer> parseText(List<String> loadedFile) {
        MutableGraph<Integer> graph = GraphBuilder.undirected().build();
        for (int lineIndex = 0; lineIndex < loadedFile.size(); lineIndex++) {
            log.info("Processing line number " + lineIndex);

            fileValidatorService.isGraphFromStandardInput(loadedFile.get(lineIndex));

            List<String> splitedLineStr = new LinkedList<String>(Arrays.asList(loadedFile.get(lineIndex).split("\\s+")));
            splitedLineStr.remove("");
            List<Integer> splitedLine = splitedLineStr.stream()
                    .map(Integer::parseInt).collect(Collectors.toList());

            if (splitedLine.size() == 2) {
                graph.putEdge(splitedLine.get(0), splitedLine.get(1));
            } else if (splitedLine.size() == 1) {
                graph.addNode(splitedLine.get(0));
            }
        }
        return graph;
    }

    private boolean isFile(String[] args) {
        return args != null && args.length == 1;
    }

    public void writeTextToFile(String output) throws IOException {
        FileUtils.writeStringToFile(new File("output.txt"), output);
    }
}
