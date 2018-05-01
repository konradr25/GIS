package com.github.konradr25.gis;

import com.google.common.graph.Graph;
import org.junit.Test;

import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class FileServiceTests {

    @Test
    public void shouldCreateGraph() throws URISyntaxException {
        //GIVEN
        FileService fileService = new FileService(new FileValidatorService());
        URL resource = FileServiceTests.class.getClassLoader().getResource("testMatrix");
        String[] args = {resource.getPath()};

        //WHEN
        Graph graph = fileService.loadFilesAndBuildGraph(args);

        //THEN
        assertNotNull(graph);
        assertFalse(graph.nodes().isEmpty());
    }
}
