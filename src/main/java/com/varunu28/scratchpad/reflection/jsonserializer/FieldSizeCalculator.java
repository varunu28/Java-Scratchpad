package com.varunu28.scratchpad.reflection.jsonserializer;

import java.lang.reflect.Field;

public class FieldSizeCalculator {

    private static final long HEADER_SIZE = 12;
    private static final long REFERENCE_SIZE = 4;

    public static void main(String[] args) {
        DemoClass demoClass = new DemoClass(1, "Hello", 1.0f, 2.0, 3L, (short) 4, (byte) 5);
        FieldSizeCalculator fieldSizeCalculator = new FieldSizeCalculator();
        try {
            System.out.println(fieldSizeCalculator.sizeOfObject(demoClass));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public long sizeOfObject(Object input) throws IllegalAccessException {
        /**
         * Complete your code here
         */
        Field[] fields = input.getClass().getDeclaredFields();
        long size = HEADER_SIZE + REFERENCE_SIZE;
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isSynthetic()) {
                continue;
            }
            if (field.getType().equals(String.class)) {
                size += sizeOfString(field.get(input).toString());
            } else if (field.getType().isPrimitive()) {
                size += sizeOfPrimitiveType(field.getType());
            } else {
                size += sizeOfObject(field.get(input));
            }
        }
        return size;
    }

    private long sizeOfPrimitiveType(Class<?> primitiveType) {
        if (primitiveType.equals(int.class)) {
            return 4;
        } else if (primitiveType.equals(long.class)) {
            return 8;
        } else if (primitiveType.equals(float.class)) {
            return 4;
        } else if (primitiveType.equals(double.class)) {
            return 8;
        } else if (primitiveType.equals(byte.class)) {
            return 1;
        } else if (primitiveType.equals(short.class)) {
            return 2;
        }
        throw new IllegalArgumentException(String.format("Type: %s is not supported", primitiveType));
    }

    private long sizeOfString(String inputString) {
        int stringBytesSize = inputString.getBytes().length;
        return HEADER_SIZE + REFERENCE_SIZE + stringBytesSize;
    }

    static class DemoClass {
        private int a;
        private String b;
        private float d;
        private double e;
        private long f;
        private short g;
        private byte h;

        public DemoClass(int a, String b, float d, double e, long f, short g, byte h) {
            this.a = a;
            this.b = b;
            this.d = d;
            this.e = e;
            this.f = f;
            this.g = g;
            this.h = h;
        }
    }
}
