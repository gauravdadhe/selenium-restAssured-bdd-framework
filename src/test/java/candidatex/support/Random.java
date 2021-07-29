package candidatex.support;

import org.apache.commons.lang3.RandomStringUtils;

public class Random {
    public static String getRandomString(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

}
