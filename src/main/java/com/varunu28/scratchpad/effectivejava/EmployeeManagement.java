package com.varunu28.scratchpad.effectivejava;

enum EmployeeType {
    ENGINEERING_MANAGER,
    SOFTWARE_ENGINEER,
    PRODUCT_MANAGER
}

interface Employee {

    void performTask();
}

public class EmployeeManagement {

    private EmployeeManagement() {
    }

    public static Employee createNewEmployee(EmployeeType employeeType) {
        return switch (employeeType) {
            case ENGINEERING_MANAGER -> new EngineeringManager();
            case SOFTWARE_ENGINEER -> new SoftwareEngineer();
            case PRODUCT_MANAGER -> new ProductManager();
        };
    }

    private static class EngineeringManager implements Employee {

        @Override
        public void performTask() {
            System.out.println("Working as engineering manager");
        }
    }

    private static class SoftwareEngineer implements Employee {

        @Override
        public void performTask() {
            System.out.println("Working as software engineer");
        }
    }

    private static class ProductManager implements Employee {

        @Override
        public void performTask() {
            System.out.println("Working as product manager");
        }
    }
}