package jdk25;

public class StableValueDemo {

    StableValue<CommandService> commandServiceStableValue = StableValue.of();

    CommandService getService() {
        return commandServiceStableValue.orElseSet(CommandService::new);
    }

    static void main() {
        StableValueDemo demo = new StableValueDemo(); // No constructor invocation yet

        demo.getService().processCommand("TestCommand1"); // CommandService gets created

        demo.getService().processCommand("TestCommand2"); // The same CommandService instance gets re-used
    }

    static class CommandService {

        public CommandService() {
            System.out.println("CommandService created");
        }

        void processCommand(String command) {
            System.out.println("Processing command: " + command);
            // some processing logic
            System.out.println("Command processed: " + command);
        }
    }
}
