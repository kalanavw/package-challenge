package org.example.service.impl;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import org.example.dto.Item;
import org.example.exception.PackageException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author Kalana_105476, 10/10/2021 8:42 AM
 */
@ExtendWith(MockitoExtension.class)
class PackagingServiceImplTest {

    @InjectMocks
    private PackagingServiceImpl packagingService;

    private String invalidPackageWeightFile;
    private String invalidItemCount;
    private String invalidItemCost;
    private String invalidItemWeight;
    private String validFile;
    private String exception;

    @BeforeEach
    void setUp() {
        //setup the paths
        Path resourceDirectory = Paths.get("src", "test", "resources");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        this.invalidPackageWeightFile = absolutePath + File.separator + "invalidPackageWeightFile.txt";
        this.invalidItemCount = absolutePath + File.separator + "invalidItemCount.txt";
        this.invalidItemCost = absolutePath + File.separator + "invalidItemCost.txt";
        this.invalidItemWeight = absolutePath + File.separator + "invalidItemWeight.txt";
        this.validFile = absolutePath + File.separator + "sampleInput.txt";
        this.exception = absolutePath + File.separator + "exception.txt";
    }

    @Test
    @DisplayName("readFile_invalid_file_path")
    void readFile_invalid_file_path() {
        Assertions.assertThrows(PackageException.class, () -> this.packagingService.readFile(""));
    }

    @Test
    @DisplayName("readFile_invalid_package_weight")
    void readFile_invalid_package_weight() {
        Assertions.assertThrows(PackageException.class, () -> this.packagingService.readFile(invalidPackageWeightFile));
    }

    @Test
    @DisplayName("readFile_invalid_item_count")
    void readFile_invalid_item_count() {
        Assertions.assertThrows(PackageException.class, () -> this.packagingService.readFile(invalidItemCount));
    }

    @Test
    @DisplayName("readFile_invalid_item_cost")
    void readFile_invalid_item_cost() {
        Assertions.assertThrows(PackageException.class, () -> this.packagingService.readFile(invalidItemCost));
    }

    @Test
    @DisplayName("readFile_invalid_item_weight")
    void readFile_invalid_item_weight() {
        Assertions.assertThrows(PackageException.class, () -> this.packagingService.readFile(invalidItemWeight));
    }

    @Test
    @DisplayName("readFile")
    void readFile() {
        Multimap<String, Item> data = this.packagingService.readFile(validFile);
        Assertions.assertNotNull(data);
        Assertions.assertNotEquals(data.size(), 0);
        Assertions.assertEquals(data.size(), 25);
        Assertions.assertEquals(data.keySet().toString(), "[56, 75, 8, 81]");
        Assertions.assertEquals(data.get("8").toString(), "[Item(id=1, weight=15.3, cost=34)]");
        Assertions.assertTrue(data.get("56").stream().anyMatch(item -> item.getId().equals(8)));
        Assertions.assertTrue(data.get("56").stream().anyMatch(item -> item.getCost().equals(79)));
    }

    @Test
    @DisplayName("readFile_invalid_file_processing")
    void readFile_invalid_file_processing() {
        Assertions.assertThrows(PackageException.class, () -> this.packagingService.readFile(exception));
    }

    @Test
    @DisplayName("processPackageData")
    void processPackageData() {
        Multimap<String, Item> packageData = TreeMultimap.create();
        packageData.put("25", new Item(1, 3.0, 20));
        packageData.put("12", new Item(2, 6.0, 20));
        packageData.put("12", new Item(3, 10.0, 20));
        packageData.put("12", new Item(4, 3.0, 20));

        Map<String, List<Item>> data = this.packagingService.processPackageData(packageData);
        Assertions.assertNotNull(data);
        Assertions.assertEquals(data.size(), 2);
        Assertions.assertEquals(data.keySet().toString(), "[12, 25]");
        Assertions.assertTrue(data.get("12").stream().anyMatch(item -> item.getId().equals(2)));
        Assertions.assertTrue(data.get("12").stream().anyMatch(item -> item.getId().equals(4)));
    }

    @Test
    @DisplayName("getValidItems")
    void getValidItems() {
        Map<String, List<Item>> data = new LinkedHashMap<>();
        data.put("12", Arrays.asList(new Item(2, 6.0, 20), new Item(4, 3.0, 20)));
        data.put("25", Arrays.asList(new Item(1, 3.0, 20)));
        data.put("10", new ArrayList<>());

        String output = this.packagingService.getValidItems(data);
        Assertions.assertNotNull(output);
    }
}
