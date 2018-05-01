package com.github.konradr25.gis;

import com.google.common.graph.MutableValueGraph;
import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.*;

public class FileServiceTests {

    @Test
    public void shouldCreateGraph() {
        //GIVEN
        FileService fileService = new FileService(new FileValidatorService());
        URL resource = FileServiceTests.class.getClassLoader().getResource("testFile");
        String[] args = {resource.getPath()};

        //WHEN
        MutableValueGraph<Object, Integer> graph = fileService.loadFilesAndBuildGraph(args);

        //THEN
        assertNotNull(graph);
        assertFalse(graph.nodes().isEmpty());
        assertEquals(3, graph.nodes().size());
    }
}
