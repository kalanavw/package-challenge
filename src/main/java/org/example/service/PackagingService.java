package org.example.service;

import com.google.common.collect.Multimap;
import org.example.dto.Item;

import java.util.List;
import java.util.Map;

/**
 * @author Kalana_105476, 10/8/2021 9:27 AM
 */
public interface PackagingService {
    Multimap<String, Item> readFile(String filePath);

    Map<String, List<Item>> processPackageData(Multimap<String, Item> things);

    String getValidItems(Map<String, List<Item>> packs);
}
