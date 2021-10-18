package org.example.service.impl;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.example.dto.Item;
import org.example.exception.PackageException;
import org.example.service.PackagingService;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.example.util.PackageConstants.*;


/**
 * @author Kalana_105476, 10/8/2021 9:26 AM
 */
public class PackagingServiceImpl implements PackagingService {

    private static final Logger LOGGER = LogManager.getLogger(PackagingServiceImpl.class);

    /**
     * method for read file and collect the file data with exception handling
     *
     * @param filePath path of the file that contains package data
     * @return Multimap key->package weight, item->item details
     */
    @Override
    public Multimap<String, Item> readFile(String filePath) {
        if (filePath == null || filePath.isEmpty() || !Files.exists(Paths.get(filePath))) {
            LOGGER.error("File not found::" + filePath);
            throw new PackageException("File not found::" + filePath);
        }
        Multimap<String, Item> packageData = TreeMultimap.create(); //multi-map for duplicating the key
        try (Stream<String> fileStream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            fileStream
                    .filter(Objects::nonNull)
                    .filter(line -> !line.isEmpty())
                    .collect(Collectors.toList())
                    .forEach(line -> {
                        String[] splittedLine = line.split(":");
                        String weight = splittedLine[0].trim();
                        String rawItems = splittedLine[1].trim();
                        if (Integer.parseInt(splittedLine[0].trim()) > MAX_PACKAGE_WEIGHT) {
                            LOGGER.error("Package Weight limit is exceeds");
                            throw new PackageException("Package Weight limit is exceeds");
                        }

                        List<String> rawItemList = Arrays.stream(rawItems.split(" "))
                                .collect(Collectors.toList());
                        if (rawItemList.size() > MAX_ITEM_COUNT) {
                            LOGGER.error("Max Item count limit is exceeds");
                            throw new PackageException("Max Item count limit is exceeds");
                        }
                        packageData.putAll(weight,
                                rawItemList.stream()
                                        .map(rawItem -> rawItem.replaceAll("\\(", ""))
                                        .map(rawItem -> rawItem.replaceAll("\\)", ""))
                                        .map(rawItem -> {
                                            String[] data = rawItem.split(",");
                                            Item item = Item.builder()
                                                    .id(Integer.parseInt(data[0]))
                                                    .weight(Double.parseDouble(data[1]))
                                                    .cost(Integer.parseInt(data[2].substring(1)))
                                                    .build();
                                            if (item.getCost() > MAX_ITEM_COST) {
                                                LOGGER.error("Max Item cost limit is exceeds");
                                                throw new PackageException("Max Item cost limit is exceeds");
                                            }
                                            if (item.getWeight() > MAX_ITEM_WEIGHT) {
                                                LOGGER.error("Max Item weight limit is exceeds");
                                                throw new PackageException("Max Item weight limit is exceeds");
                                            }
                                            return item;
                                        })
                                        .collect(Collectors.toList())
                        );
                    });

        } catch (Exception e) {
            LOGGER.error("Error in read file: {}", e);
            throw new PackageException("Error in read file");
        }
        return packageData;
    }

    /**
     * method for processing items against the requirement
     * find items that match the package by item cost and item weight
     *
     * @param packageData
     * @return Map with selected items
     */
    @Override
    public Map<String, List<Item>> processPackageData(Multimap<String, Item> packageData) {
        Map<String, List<Item>> validPackageMap = new LinkedHashMap<>();
        packageData.keySet().forEach(k -> validPackageMap.put(k, new ArrayList<>()));
        packageData.entries().forEach(
                entry -> validPackageMap.computeIfPresent(entry.getKey(), (stringKey, listValue) -> {
                    Item item = entry.getValue();
                    double sumOfItemWeights = listValue.stream().mapToDouble(Item::getWeight).sum();
                    double totalSum = sumOfItemWeights + item.getWeight();
                    if (totalSum < Integer.parseInt(entry.getKey())) {
                        listValue.add(item);
                    }
                    listValue.sort(Comparator.comparingInt(Item::getId));
                    return listValue;
                })
        );
        return validPackageMap;
    }

    /**
     * method that find final output
     *
     * @param packages processed items
     * @return String
     */
    @Override
    public String getValidItems(Map<String, List<Item>> packages) {
        StringBuilder builder = new StringBuilder();
        packages.keySet().forEach(key -> {
            if (packages.get(key).isEmpty()) {
                builder.append("-").append(System.lineSeparator());
            } else {
                List<Item> items = packages.get(key);
                items.sort((o1, o2) -> o2.getId() - o1.getId());
                builder.append(items.stream()
                        .map(item -> item.getId().toString())
                        .collect(Collectors.joining(",")));
                builder.append(System.lineSeparator());
            }
        });
        return builder.reverse().toString();
    }
}
