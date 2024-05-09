package com.library.jupiter.extension;

import com.library.jupiter.annotation.User;
import com.library.model.apiModels.UserJson;
import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class UserExtension implements BeforeEachCallback, BeforeAllCallback {
    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(UserExtension.class);

    private static final Map<User.UserType, UserJson> USERS = new ConcurrentHashMap<>();
    static {
        USERS.put(User.UserType.LIBRARIAN, UserJson.simpleUser("librarian10@library", "libraryUser"));
        USERS.put(User.UserType.STUDENT, UserJson.simpleUser("someEmail", "somePassword"));

    }

    public static void discordTokenInjection() {
        System.out.println(System.getProperty("discordToken"));
        String filePath = "notifications/config.json"; // Укажите путь к вашему JSON файлу

        try {
            // Чтение JSON из файла
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(filePath));

            // Вывод исходного JSON в консоль
            System.out.println("Исходный JSON:");
            System.out.println(jsonObject.toJSONString());

            // Получение значения поля "plid"
            String discordBotToken = System.getProperty("discordToken");


            // Изменение значения поля "plid" на новое значение
            JSONObject discordObject = (JSONObject) jsonObject.get("discord");
            discordObject.put("botToken", discordBotToken); // Укажите ваше новое значение для "token"

            jsonObject.put("discord", discordObject);


            // Вывод измененного JSON в консоль
            System.out.println("новый JSON:");
            System.out.println(jsonObject.toJSONString());

            // Запись измененного JSON обратно в файл
            try (FileWriter fileWriter = new FileWriter(filePath)) {
                fileWriter.write(jsonObject.toJSONString());
                System.out.println("\nJSON успешно обновлен.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) {

        AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                User.class
        ).ifPresent(
                api -> {
                    Allure.step("Log in as " + api.userType());
                    UserJson currentUser = USERS.get(User.UserType.valueOf(api.userType().name()));
                    context.getStore(NAMESPACE).put(context.getUniqueId(), Objects.requireNonNull(currentUser));
                }
        );
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        discordTokenInjection();
    }
}
