package io.github.emerson.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Application implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(Application.class.getName());

    public static void main(String[] args) {
        LOGGER.setLevel(Level.ALL);
        final ExecutorService executorService = Executors.newFixedThreadPool(3);
        Application app = new Application();
        Servidor servidor = new Servidor();
        EchoClient client = new EchoClient(12344);
        LOGGER.info("broadcast");
        executorService.execute(app);
        LOGGER.info("server");
        executorService.execute(servidor);
        LOGGER.info("listen");
        executorService.execute(client);
    }

    public void broadcast(String broadcastMessage, InetAddress address) {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setBroadcast(true);
            LOGGER.info(address.getHostAddress());
            byte[] buffer = broadcastMessage.getBytes();

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 12344);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startBroadcast() throws IOException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            if (networkInterface.isLoopback())
                continue;    // Do not want to use the loopback interface.
            for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                InetAddress broadcast = interfaceAddress.getBroadcast();
                if (broadcast == null)
                    continue;
                final InetAddress localHost = InetAddress.getLocalHost();
                broadcast(localHost.toString(), broadcast);
            }
        }
    }

    @Override
    public void run() {
        try {
            startBroadcast();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
