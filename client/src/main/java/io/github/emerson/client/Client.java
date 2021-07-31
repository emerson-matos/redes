package io.github.emerson.client;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Logger;

public class Client {
    private static final Logger LOGGER = Logger.getLogger(Client.class.getName());

    public static void main(String[] args) {
        try (Socket cliente = new Socket("server", 12345)) {
            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
            Date data_atual = (Date) entrada.readObject();
            entrada.close();
            LOGGER.info("Conexão encerrada");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("Erro: " + e.getMessage());
        }
    }
}
