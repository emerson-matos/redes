package io.github.emerson.server;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Logger;

public class EchoClient implements Closeable, Runnable {
    private static final Logger LOGGER = Logger.getLogger(EchoClient.class.getName());
    private byte[] buf;
    private DatagramSocket socket;

    public EchoClient(int port) {
        try {
            this.socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public String listen() throws IOException {
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        LOGGER.info("ESPERANDO MENSAGEM");
        socket.receive(packet);
        LOGGER.info("PACOTE RECEBIDO");
        return new String(packet.getData(), 0, packet.getLength());
    }

    public void close() {
        socket.close();
    }

    @Override
    public void run() {
        LOGGER.info("INICIANDO LISTENER");
        while (true) {
            try {
                this.buf = new byte[1024];
                LOGGER.info(listen());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
