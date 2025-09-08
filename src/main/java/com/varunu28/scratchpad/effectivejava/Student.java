package com.varunu28.scratchpad.effectivejava;

public class Student implements Comparable<Student> {
    private final int id;
    private final int marks;

    public Student(int id, int marks) {
        this.id = id;
        this.marks = marks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Student student = (Student) o;
        if (id != student.id) {
            return false;
        }
        return marks == student.marks;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + marks;
        return result;
    }

    @Override
    public int compareTo(Student o) {
        return Integer.compare(this.marks, o.marks);
    }
}