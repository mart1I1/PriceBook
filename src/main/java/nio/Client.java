package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static java.nio.channels.SelectionKey.*;
import static java.nio.channels.SelectionKey.OP_WRITE;

public class Client extends Thread {

    @Override
    public void run() {
        try {
            SocketChannel channel = SocketChannel.open();
            channel.configureBlocking(false);
            Selector selector = Selector.open();
            channel.register(selector, OP_CONNECT);
            channel.connect(new InetSocketAddress("localhost", 8090));
            ByteBuffer byteBuffer = ByteBuffer.allocate(10);

            BlockingQueue<String> queue = new ArrayBlockingQueue<>(1);
            new Thread(() -> {
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    String line = scanner.nextLine();
                    if ("q".equals(line)) {
                        System.exit(0);
                    }
                    try {
                        queue.put(Protocol.convertFrom(line));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    SelectionKey key = channel.keyFor(selector);
                    key.interestOps(OP_WRITE);
                    selector.wakeup();
                }
            }).start();

            while (!Thread.currentThread().isInterrupted()) {
                selector.select();
                for (SelectionKey selectionKey : selector.selectedKeys()) {
                    if (selectionKey.isConnectable()) {
                        channel.finishConnect();
                        selectionKey.interestOps(OP_WRITE);
                    } else if (selectionKey.isReadable()) {
                        byteBuffer.clear();
                        channel.read(byteBuffer);
                    } else if (selectionKey.isWritable()) {
                        String line = queue.poll();
                        if (line != null) {
                            channel.write(ByteBuffer.wrap(line.getBytes()));
                        }
                        selectionKey.interestOps(OP_READ);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
