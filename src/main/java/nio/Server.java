package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.channels.SelectionKey.OP_ACCEPT;
import static java.nio.channels.SelectionKey.OP_READ;

public class Server {

    public Server() throws IOException {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        InetSocketAddress isa = new InetSocketAddress("localhost", 8090);
        serverChannel.socket().bind(isa);
        Selector selector = SelectorProvider.provider().openSelector();
        serverChannel.register(selector, OP_ACCEPT);
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);

        while (selector.select() > -1) {
            for (SelectionKey selectionKey : selector.selectedKeys()) {
                if (selectionKey.isAcceptable()) {
                    System.out.println("server: accept");
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, OP_READ);
                } else if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    byteBuffer.clear();
                    socketChannel.read(byteBuffer);
                    System.out.println("server read: " + new String(byteBuffer.array()));
                    selectionKey.interestOps(SelectionKey.OP_WRITE);
                } else if (selectionKey.isWritable()) {
                    System.out.println("server write");
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    byteBuffer.flip();
                    channel.write(byteBuffer);
                    selectionKey.interestOps(OP_READ);
                }
            }
            selector.selectedKeys().clear();
        }
    }

    public static void main(String[] args) throws IOException {
        new Server();
    }
}
