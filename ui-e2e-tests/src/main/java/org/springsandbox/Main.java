package org.springsandbox;

import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        String str = "3245 12 345 12 222 1";
        var list = List.of(str.split(" "));
        Integer max = list.stream().map(Integer::parseInt).max(Comparator.comparingInt(a -> a)).get();
        Integer min = list.stream().map(Integer::parseInt).min(Comparator.comparingInt(a -> a)).get();
        System.out.println(min + " " + max);
    }
}