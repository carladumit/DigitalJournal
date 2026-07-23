package org.carladumit.digitaljournal.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHash {

    public static String createHash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkHash(
            String password,
            String hash) {

        return BCrypt.checkpw(password, hash);
    }
}
