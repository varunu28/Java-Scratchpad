package reflection.fields;

import java.lang.reflect.Field;

public class FieldsReflectionRunner {

    public static void main(String[] args) {
        Movie movie = new Movie("Inception", 2010, 19.99, true, Category.ADVENTURE);
        try {
            printDeclaredFieldsInfo(movie.getClass(), movie);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static <T> void printDeclaredFieldsInfo(Class<? extends T> clazz, T instance) throws IllegalAccessException {
        for (Field field : clazz.getDeclaredFields()) {
            System.out.printf(
                "Field name: %s type: %s value: %s\n",
                field.getName(),
                field.getType().getName(),
                field.get(instance));
            System.out.println("Is synthetic: " + field.isSynthetic());
        }
    }

    public enum Category {
        ADVENTURE,
        ACTION,
        COMEDY
    }

    public static class Movie extends Product {

        private static final double MINIMUM_PRICE = 10.99;

        private final boolean isReleased;
        private final Category category;
        private final double actualPrice;

        public Movie(String name, int year, double price, boolean isReleased, Category category) {
            super(name, year);
            this.actualPrice = price;
            this.isReleased = isReleased;
            this.category = category;
        }

        public class MovieStats {
            private final double timesWatched;

            public MovieStats(double timesWatched) {
                this.timesWatched = timesWatched;
            }

            public double getRevenue() {
                return timesWatched * actualPrice;
            }
        }
    }

    public static class Product {
        protected String name;
        protected int year;
        protected double actualPrice;

        public Product(String name, int year) {
            this.name = name;
            this.year = year;
        }
    }
}
