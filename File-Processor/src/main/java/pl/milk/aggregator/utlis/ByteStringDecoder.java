package pl.milk.aggregator.utlis;

import akka.util.ByteString;

public class ByteStringDecoder {
    private static final String ENCODING = "UTF-8";

    public static String decodeToStringWithoutWhitespace(final ByteString byteString) {
        return byteString.decodeString(ENCODING).strip();

    }
}
