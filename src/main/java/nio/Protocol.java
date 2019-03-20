package nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static java.lang.Math.*;

public class Protocol {
    public static ByteBuffer byteBuffer = ByteBuffer.allocate(10);

    private static final String MARKER = ";";

    public static String convertFrom(String message) {
        return message.length() + MARKER + message;
    }

    public static String convertFrom(SocketChannel socketChannel) throws IOException {
        StringBuilder result = new StringBuilder();

        byteBuffer.clear();

        socketChannel.read(byteBuffer);
        String firstMessage = new String(byteBuffer.array());

        int messageLength = getMessageLength(firstMessage);
        int indexOfMarker = firstMessage.indexOf(MARKER);

        result.append(
                firstMessage,
                indexOfMarker + MARKER.length(),
                min(indexOfMarker + MARKER.length() + messageLength, byteBuffer.capacity())
        );
        messageLength -= min(messageLength, byteBuffer.capacity() - indexOfMarker - MARKER.length());
        while (messageLength > 0) {
            byteBuffer.flip();
            socketChannel.read(byteBuffer);

            String message = new String(byteBuffer.array());
            int min = min(messageLength, byteBuffer.capacity());
            result.append(
                    message,
                    0,
                    min);
            messageLength -=  min;
        }

        return result.toString();
    }

    private static int getMessageLength(String s) {
        return Integer.parseInt(s.substring(0, s.indexOf(MARKER)));
    }
}
