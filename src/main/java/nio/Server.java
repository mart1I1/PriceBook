package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;

import static java.nio.channels.SelectionKey.OP_ACCEPT;
import static java.nio.channels.SelectionKey.OP_READ;

public class Server extends Thread {



    @Override
    public void run() {
        try {
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            InetSocketAddress isa = new InetSocketAddress("localhost", 8090);
            serverChannel.socket().bind(isa);
            Selector selector = SelectorProvider.provider().openSelector();
            serverChannel.register(selector, OP_ACCEPT);
            ByteBuffer byteBuffer = ByteBuffer.allocate(10);

            while (!Thread.currentThread().isInterrupted() && selector.select() > -1) {
                selector.select();
                for (SelectionKey selectionKey : selector.selectedKeys()) {
                    if (selectionKey.isAcceptable()) {
                        acceptableAction(selector, selectionKey);
                    } else if (selectionKey.isReadable()) {
                        readAction(selectionKey, byteBuffer);
                    } else if (selectionKey.isWritable()) {
                        writeAction(selectionKey, byteBuffer);
                    }
                }
                selector.selectedKeys().clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Server closed!");
        }
    }

    private void writeAction(SelectionKey selectionKey, ByteBuffer byteBuffer) throws IOException {
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        byteBuffer.flip();
        channel.write(byteBuffer);
        selectionKey.interestOps(OP_READ);
    }

    private void readAction(SelectionKey selectionKey, ByteBuffer byteBuffer) throws IOException {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        String message = Protocol.convertFrom(socketChannel);
        System.out.println(message);
        selectionKey.interestOps(SelectionKey.OP_WRITE);
    }

    private void acceptableAction(Selector selector, SelectionKey selectionKey) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, OP_READ);
    }


}
