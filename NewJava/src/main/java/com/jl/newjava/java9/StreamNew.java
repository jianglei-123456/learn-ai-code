package com.jl.newjava.java9;

import java.util.List;
import java.util.stream.Stream;

/**
 * stream api增强
 */
public class StreamNew {

    public static void main(String[] args) {
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        //dropWhile 依次舍弃数据，直到条件为false
        list.stream().dropWhile( i -> i<5).forEach(System.out::println);
        //takeWhile 依次取数据，直到条件为false
        list.stream().takeWhile( i -> i<5).forEach(System.out::println);


        // Stream.ofNullable 处理可能为空的单元素流，只有一个元素的元素流用的比较少

        //Stream.iterate（三元重载） 类似for的写法了
        Stream.iterate(0, i -> i < 10, i -> i + 1)
                .forEach(System.out::println);

    }
}
