package examples.first;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;


/**
 * Project: Shamrock Web Portal.
 * User: Constantine Solovev
 * Date: 15.04.15
 * Time: 18:30
 */

public class DiscardServer {

    private int port;

    public DiscardServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {

        // accepts an incoming connection
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        // handles the traffic of the accepted connection
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boosGroup, workerGroup)
                // will be accept incoming connections
                .channel(NioServerSocketChannel.class)
                // will always be evaluated by a newly accepted Channel
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new EchoServerHandler());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        // Bind and start to accept incoming connections.
        ChannelFuture channelFuture = serverBootstrap.bind(port).sync();

        // Wait until the server socket is closed.
        // In this example, this does not happen, but you can do that to gracefully
        // shut down your server.
        channelFuture.channel().closeFuture().sync();

    }

    public static void main(String[] args) throws Exception {
       int port;
       if(args.length > 0) {
           port = Integer.parseInt(args[0]);
       } else {
           port = 8070;
       }
       new DiscardServer(port).run();

    }

}
