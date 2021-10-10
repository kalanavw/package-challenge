package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * @author Kalana_105476, 10/8/2021 10:26 AM
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item implements Comparable<Item> {
    private Integer id;
    private Double weight;
    private Integer cost;

    @Override
    public int compareTo(Item item) {
        if (Objects.equals(this.cost, item.cost)) {
            return this.weight > item.weight ? 1 : -1;
        } else {
            return this.cost > item.cost ? -1 : 1;
        }
    }
}
