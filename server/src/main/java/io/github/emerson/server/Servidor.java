package io.github.emerson.server;

import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(Servidor.class.getName());

    public void start() {
        try (ServerSocket servidor = new ServerSocket(12345)) {
            LOGGER.info("Servidor ouvindo a porta " + servidor.getLocalPort());
            LOGGER.info(InetAddress.getLocalHost().getHostName());
            LOGGER.info(InetAddress.getLocalHost().getHostAddress());
            LOGGER.info(InetAddress.getLocalHost().getCanonicalHostName());
            while (true) {
                // o método accept() bloqueia a execução até que
                // o servidor receba um pedido de conexão
                Socket cliente = servidor.accept();
                LOGGER.info("Cliente conectado: " + cliente.getInetAddress().getHostAddress());
                ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
                saida.flush();
                saida.writeObject(new Date());
                saida.close();
                cliente.close();
            }
        } catch (Exception e) {
            LOGGER.info("Erro: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        LOGGER.setLevel(Level.ALL);
        while (true) {
            start();
        }
    }
}
