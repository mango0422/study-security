package mango.security.authorizationServer.util;

import mango.security.authorizationServer.exception.AuthException;
import mango.security.authorizationServer.type.ApiResponseType;

import java.util.Collection;

public class Assert {

    public static void notBlank(String value, ApiResponseType type, String message) {
        if (value == null || value.isBlank()) {
            throw new AuthException(type, message);
        }
    }

    public static void notNull(Object obj, ApiResponseType type, String message) {
        if (obj == null) {
            throw new AuthException(type, message);
        }
    }

    public static void notEmpty(Collection<?> collection, ApiResponseType type, String message) {
        if (collection == null || collection.isEmpty()) {
            throw new AuthException(type, message);
        }
    }

    public static void isTrue(boolean expression, ApiResponseType type, String message) {
        if (!expression) {
            throw new AuthException(type, message);
        }
    }

    public static void isFalse(boolean expression, ApiResponseType type, String message) {
        if (expression) {
            throw new AuthException(type, message);
        }
    }
}
