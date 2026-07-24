package com.jl.newjava.java9;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 集合工厂
 */
public class SetFactory {
    public static void main(String[] args) {
        List<String> strings = List.of("1", "a", "dd");
        System.out.println(strings);
        Set<String> strings1 = Set.of("1", "2", "3");
        System.out.println(strings1);
        Map<Integer, String> integerStringMap = Map.of(1, "2", 3, "4");
        System.out.println(integerStringMap);
        // map.of 最大只能接受10个entry 再大需要用
        Map<Integer, String> integerStringMap1 = Map.ofEntries(
                Map.entry(1, "2"),
                Map.entry(2, "4")
        );
        System.out.println(integerStringMap1);
        // 集合工厂构建出来的集合是不可变集合
    }

}
