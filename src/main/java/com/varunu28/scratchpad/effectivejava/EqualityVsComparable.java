package com.varunu28.scratchpad.effectivejava;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class EqualityVsComparable {

    public static void main(String[] args) {
        Student s1 = new Student(1, 10);
        Student s2 = new Student(2, 10);

        Set<Student> hashset = new HashSet<>();
        hashset.add(s1);
        hashset.add(s2);
        assert hashset.size() == 2;

        TreeSet<Student> treeSet = new TreeSet<>();
        treeSet.add(s1);
        treeSet.add(s2);
        assert treeSet.size() == 1;
    }
}


