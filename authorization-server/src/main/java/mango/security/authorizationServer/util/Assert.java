package mango.security.authorizationServer.util;

import mango.security.authorizationServer.exception.AuthException;
import mango.security.authorizationServer.type.ApiResponseType;

import java.util.Collection;

public class Assert {

    public static void notBlank(String value, ApiResponseType type) {
        if (value == null || value.isBlank()) {
            throw new AuthException(type);
        }
    }

    public static void notNull(Object obj, ApiResponseType type) {
        if (obj == null) {
            throw new AuthException(type);
        }
    }

    public static void notEmpty(Collection<?> collection, ApiResponseType type) {
        if (collection == null || collection.isEmpty()) {
            throw new AuthException(type);
        }
    }

    public static void isTrue(boolean expression, ApiResponseType type) {
        if (!expression) {
            throw new AuthException(type);
        }
    }

    public static void isFalse(boolean expression, ApiResponseType type) {
        if (expression) {
            throw new AuthException(type);
        }
    }
}
