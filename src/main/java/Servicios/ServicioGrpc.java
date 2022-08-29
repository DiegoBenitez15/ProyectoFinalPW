package Servicios;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class ServicioGrpc {
    private static final int PORT = 50051;
    private Server server;
    private int port = 50051;

    public void start() throws IOException, InterruptedException {
        server = ServerBuilder.forPort(port)
                .addService(new UrlServicesImpl())// indicando el servicio registrado.
                .build()
                .start();
        System.out.println("Servidor gRPC iniciando y escuchando en " + port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("Cerrando servidor por la JVM ");
            if (server != null) {
                server.shutdown();
            }
            System.err.println("Servidor abajo!...");
        }));
        server.awaitTermination();
    }

}
