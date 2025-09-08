package com.varunu28.scratchpad.reflection;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Stream;
import com.varunu28.scratchpad.reflection.web.ServerConfiguration;

public class ReflectionRunner {

    static void main(String[] args)
        throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException,
        IllegalAccessException, IOException {
        // Introduction to Reflection
        //        Class<String> stringClass = String.class;
        //
        //        Map<String, Integer> dictionary = new HashMap<>();
        //
        //        Class<?> hashMapClass = dictionary.getClass();
        //
        //        Class<?> squareClass = Class.forName("com.varunu28.scratchpad.reflection.ReflectionRunner$Square");
        //
        //        printInfo(stringClass, hashMapClass, squareClass, Color.class);

        // Constructors and Instances
        //        printConstructors(Address.class);
        //        Address address = createInstance(Address.class, "Main Street", 123);
        //        Person person = createInstance(Person.class, address, "John", 30);
        //        System.out.println(person);

        //        initConfiguration();
        //        WebServer webServer = new WebServer();
        //        webServer.startServer();

    }

    public static void initConfiguration()
        throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<ServerConfiguration> constructor =
            ServerConfiguration.class.getDeclaredConstructor(int.class, String.class);
        constructor.setAccessible(true);
        constructor.newInstance(8000, "Hello, World!");
    }

    @SuppressWarnings("unchecked")
    public static <T> T createInstance(Class<T> clazz, Object... args) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            if (parameterTypes.length == args.length) {
                try {
                    return (T) constructor.newInstance(args);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("No constructor found with the given parameters.");
        return null;
    }

    private static void printConstructors(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        System.out.printf("Class : %s has %d declared constructors.\n", clazz.getSimpleName(), constructors.length);

        for (Constructor<?> constructor : constructors) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            List<String> parameterTypeNames = Stream.of(parameterTypes).map(Class::getSimpleName).toList();

            System.out.println(parameterTypeNames);
        }
    }

    private static void printInfo(Class<?>... classes) {
        for (Class<?> clazz : classes) {
            System.out.printf("class name: %s, class package: %s%n", clazz.getSimpleName(), clazz.getPackage());

            Class<?>[] interfaces = clazz.getInterfaces();
            for (Class<?> implementedInterface : interfaces) {
                System.out.printf(
                    "class %s implements interface: %s%n",
                    clazz.getSimpleName(),
                    implementedInterface.getSimpleName());
            }
            System.out.println();
        }
    }

    private enum Color {
        RED,
        GREEN,
        BLUE
    }

    private interface Drawable {
        int getNumberOfCorners();
    }

    private static class Square implements Drawable {

        @Override
        public int getNumberOfCorners() {
            return 4;
        }
    }

    public static class Person {
        private final Address address;
        private final String name;
        private final int age;

        public Person() {
            this.address = null;
            this.name = "anonymous";
            this.age = 0;
        }

        public Person(String name) {
            this.name = name;
            this.address = null;
            this.age = 0;
        }

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
            this.address = null;
        }

        public Person(Address address, String name, int age) {
            this.address = address;
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{" + "address=" + address + ", name='" + name + '\'' + ", age=" + age + '}';
        }
    }

    public static class Address {
        private final String street;
        private final int number;

        public Address(String street, int number) {
            this.street = street;
            this.number = number;
        }

        @Override
        public String toString() {
            return "Address{" + "street='" + street + '\'' + ", number=" + number + '}';
        }
    }
}
