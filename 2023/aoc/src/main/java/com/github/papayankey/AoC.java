package com.github.papayankey;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

public class AoC {
    private static final String SESSION = "";

    private AoC() {
        throw new IllegalStateException("Utility class");
    }

    public static Path getInput(int year, int day) {
        Path path = Path.of(STR."\{year}/aoc/src/main/java/com/github/papayankey/day_0\{day}/input.txt").toAbsolutePath();

        if (Files.exists(path)) {
            return path;
        }

        CookieHandler.setDefault(new CookieManager());

        HttpCookie sessionCookie = new HttpCookie("session", SESSION);
        sessionCookie.setPath("/");
        sessionCookie.setVersion(0);

        URI uri = null;
        try {
            ((CookieManager) CookieHandler.getDefault()).getCookieStore().add(URI.create("https://adventofcode.com"),
                    sessionCookie);
            uri = URI.create(STR."https://adventofcode.com/\{year}/day/\{day}/input");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        HttpRequest req = HttpRequest.newBuilder()
                .uri(uri)
                .GET().build();

        try (HttpClient client = HttpClient.newBuilder()
                .cookieHandler(CookieHandler.getDefault())
                .connectTimeout(Duration.ofSeconds(10))
                .build()) {

            try {
                client.send(req, HttpResponse.BodyHandlers.ofFile(Files.createFile(path))).body();
            } catch (IOException | InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

        return path;
    }
}
