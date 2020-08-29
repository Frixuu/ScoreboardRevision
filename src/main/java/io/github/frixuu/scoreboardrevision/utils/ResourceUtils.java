package io.github.frixuu.scoreboardrevision.utils;

import java.io.InputStream;
import java.io.InputStreamReader;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class ResourceUtils {
    public static InputStream getResourceAsStream(String path) {
        return ResourceUtils.class.getResourceAsStream("/" + path);
    }

    public static InputStreamReader getResourceAsReader(String path) {
        return new InputStreamReader(getResourceAsStream(path), UTF_8);
    }
}
