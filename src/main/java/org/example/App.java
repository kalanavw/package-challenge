package org.example;

import com.google.common.collect.Multimap;
import org.example.dto.Item;
import org.example.service.PackagingService;
import org.example.service.impl.PackagingServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) {
        processPackaging(args[0]);
    }

    public static void processPackaging(String filePath) {
        PackagingService packagingService = new PackagingServiceImpl();
        Multimap<String, Item> integerItemMultimap = packagingService.readFile(filePath);
        Map<String, List<Item>> stringArrayListMap = packagingService.processPackageData(integerItemMultimap);
        String output = packagingService.getValidItems(stringArrayListMap);
        System.out.println(output);
    }
}
