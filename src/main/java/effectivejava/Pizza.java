package effectivejava;

import java.util.HashSet;
import java.util.Set;

public class Pizza {
    private final String bread;
    private final Set<Cheese> cheeses;
    private final Set<Meat> meats;

    private Pizza(PizzaBuilder pizzaBuilder) {
        this.bread = pizzaBuilder.bread;
        this.cheeses = pizzaBuilder.cheeses;
        this.meats = pizzaBuilder.meats;
    }

    enum Cheese {
        MOZZARELLA,
        PROVOLONE,
        CHEDDAR
    }

    enum Meat {
        BACON,
        CHICKEN,
        SAUSAGE
    }

    public static class PizzaBuilder {
        private final String bread;
        private final Set<Cheese> cheeses;
        private final Set<Meat> meats;

        public PizzaBuilder(String bread) {
            this.bread = bread;
            this.cheeses = new HashSet<>();
            this.meats = new HashSet<>();
        }

        public PizzaBuilder addMeat(Meat meat) {
            this.meats.add(meat);
            return this;
        }

        public PizzaBuilder addCheese(Cheese cheese) {
            this.cheeses.add(cheese);
            return this;
        }

        public Pizza build() {
            return new Pizza(this);
        }
    }
}
