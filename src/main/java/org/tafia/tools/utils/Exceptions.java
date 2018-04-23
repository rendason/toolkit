package org.tafia.tools.utils;

/**
 * Created by Dason on 2018/4/21.
 */
public class Exceptions {

    public static RuntimeException checked(Exception e) throws RuntimeException {
        return e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e);
    }
}
