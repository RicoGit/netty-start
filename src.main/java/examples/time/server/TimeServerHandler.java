package examples.time.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;

/**
 * Project: Shamrock Web Portal.
 * User: Constantine Solovev
 * Date: 16.04.15
 * Time: 10:44
 */

public class TimeServerHandler extends ChannelHandlerAdapter {

    @Override // when a connection is established and ready to generate traffic
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        System.out.println(new Date());
        final ByteBuf time = ctx.alloc().buffer(4);
        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));

        final ChannelFuture channelFuture = ctx.writeAndFlush(time);

        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                ctx.close();
            }
        });

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
