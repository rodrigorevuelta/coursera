import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 * Use of wrapper facade, Reactor and Acceptor Connector patterns.
 * 
 */
public final class ReactiveServer {

    static final int PORT = Integer.parseInt(System.getProperty("port", "8080"));

    public static void main(final String[] args) throws Exception {
        // The Acceptor-Connector design pattern decouples connection establishment and service initialization in a
        // distributed system from the processing performed once a service is initialized. This decoupling is
        // achieved with three components:acceptors, connectors,andservice handlers. A connector actively establishes a
        // connection with a remote acceptor component and initializes a service handler to process data exchanged
        // on the connection. Likewise, an acceptor passively waits for connection requests from remote
        // connectors, establishing a connection upon arrival of such a request, and initializing a service handler
        // to process data exchanged on the connection. The initialized service handlers then perform
        // application-specific processing and communicate via the connection established by the connector
        // and acceptor components.

        // Configure the bootstrap.
        final ServerBootstrap bootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));

        // Once the event occurs the dispatcher then calls the subscribed methods (often called “handlers”) one by one
        // and one after another. This is the reactor pattern.
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() {
                final ChannelPipeline p = Channels.pipeline();
                p.addLast("echo", new EchoServerHandler());
                return p;
            }
        });
        // Bind and start to accept incoming connections.
        bootstrap.bind(new InetSocketAddress(PORT));
    }
}

class EchoServerHandler extends SimpleChannelUpstreamHandler {

    private final AtomicLong transferredBytes = new AtomicLong();

    public long getTransferredBytes() {
        return this.transferredBytes.get();
    }

    @Override
    public void messageReceived(final ChannelHandlerContext ctx, final MessageEvent e) {
        // This method make use of the wrapper facade pattern because doesn't use the sockets api directly. 
    	// Use the netty api. Send back the received message to the remote peer.
        this.transferredBytes.addAndGet(((ChannelBuffer) e.getMessage())
                .readableBytes());
        final ChannelBuffer buf = (ChannelBuffer) e.getMessage();
        while (buf.readable()) {
            System.out.print((char) buf.readByte());
            System.out.flush();
        }
        e.getChannel().write(e.getMessage());
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final ExceptionEvent e) {
        // Close the connection when an exception is raised.
        e.getCause().printStackTrace();
        e.getChannel().close();
    }
}