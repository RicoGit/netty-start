package examples.time.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Project: Shamrock Web Portal.
 * User: Constantine Solovev
 * Date: 16.04.15
 * Time: 11:05
 */

public class TimeServer {

    int port;

    public TimeServer(int port) {
        this.port = port;
    }

    public void run() throws InterruptedException {

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(new NioEventLoopGroup(), new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new TimeServerHandler());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
        channelFuture.channel().closeFuture().sync();

    }

    public static void main(String[] args) throws InterruptedException {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8070;
        }
        new TimeServer(port).run();

    }

}
