package Servicios;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.ClientAuth;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class ServicioGrpc {
    private static final int PORT = 50051;
    private Server server;
    private int port = 50051;

    public void start() throws IOException, InterruptedException {
        File keyCertChainFile = new File("src/main/resources/public/keys/server.pem");
        File keyFile = new File("src/main/resources/public/keys/server.key");

        SslContext sslContext = GrpcSslContexts.forServer(keyCertChainFile, keyFile)
                .clientAuth(ClientAuth.OPTIONAL)
                .build();

        Server server = NettyServerBuilder.forPort(9090)
                .addService(new UrlServicesImpl())
                .sslContext(sslContext)
                .build();

        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                server.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));

        server.awaitTermination();

    }

}
