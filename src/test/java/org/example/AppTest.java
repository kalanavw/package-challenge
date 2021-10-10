package org.example;

import org.example.service.impl.PackagingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Unit test for simple App.
 */
@ExtendWith(MockitoExtension.class)
public class AppTest {
    @Mock
    private PackagingServiceImpl packagingService;

    private String validFile;

    @BeforeEach
    void setUp() {
        Path resourceDirectory = Paths.get("src", "test", "resources");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        this.validFile = absolutePath + File.separator + "sampleInput.txt";
    }

    @Test
    @DisplayName("start package-challenge app")
    public void processPackaging() {
        App.processPackaging(validFile);
    }
}
