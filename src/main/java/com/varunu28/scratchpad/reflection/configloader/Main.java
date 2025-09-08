package com.varunu28.scratchpad.reflection.configloader;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.Scanner;
import com.varunu28.scratchpad.reflection.configloader.data.GameConfig;
import com.varunu28.scratchpad.reflection.configloader.data.UserInterfaceConfig;

public class Main {

    private static final Path GAME_CONFIG_FILE = Path.of("src/main/resources/game-properties.cfg");
    private static final Path USER_INTERFACE_CONFIG_FILE = Path.of("src/main/resources/user-interface.cfg");

    public static void main(String[] args)
        throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException,
        IllegalAccessException {
        GameConfig gameConfig = createConfigObject(GameConfig.class, GAME_CONFIG_FILE);
        System.out.println(gameConfig);

        UserInterfaceConfig
            userInterfaceConfig = createConfigObject(UserInterfaceConfig.class, USER_INTERFACE_CONFIG_FILE);
        System.out.println(userInterfaceConfig);
    }

    public static <T> T createConfigObject(Class<T> clazz, Path filePath)
        throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException,
        IllegalAccessException {
        Scanner scanner = new Scanner(filePath);

        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);

        @SuppressWarnings("unchecked")
        T configInstance = (T) constructor.newInstance();

        while (scanner.hasNextLine()) {
            String configLine = scanner.nextLine();
            String[] nameValuePair = configLine.split("=");
            String propertyName = nameValuePair[0];
            String propertyValue = nameValuePair[1];
            Field field;
            try {
                field = clazz.getDeclaredField(propertyName);
            } catch (NoSuchFieldException e) {
                System.out.printf("Property name %s not supported%n", propertyName);
                continue;
            }
            field.setAccessible(true);
            Object parsedValue = parseValue(field.getType(), propertyValue);
            field.set(configInstance, parsedValue);
        }
        return configInstance;
    }

    private static Object parseValue(Class<?> type, String value) {
        if (type.equals(int.class)) {
            return Integer.parseInt(value);
        } else if (type.equals(long.class)) {
            return Long.parseLong(value);
        } else if (type.equals(double.class)) {
            return Double.parseDouble(value);
        } else if (type.equals(String.class)) {
            return value;
        } else if (type.equals(short.class)) {
            return Short.parseShort(value);
        } else if (type.equals(float.class)) {
            return Float.parseFloat(value);
        }
        throw new RuntimeException(String.format("Type: %s is not supported", type));
    }
}
