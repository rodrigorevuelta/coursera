import java.net.InetSocketAddress;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
<<<<<<< HEAD
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

        // bootstrap.setOption("child.tcpNoDelay", true);
        // bootstrap.setOption("child.receiveBufferSize", 1048576);
        // bootstrap.setOption("child.sendBufferSize", 1048576);

        // Bind and start to accept incoming connections.
        bootstrap.bind(new InetSocketAddress(PORT));
    }
}
=======
import org.jboss.netty.channel.socket.ServerSocketChannelFactory;
import org.jboss.netty.channel.socket.oio.OioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

public class ReactiveServer {
>>>>>>> 5fa0e27f4027d30c3413098f71abcaa3c6412383

    private final class EchoServerPipelineFactory implements ChannelPipelineFactory {
        public ChannelPipeline getPipeline() throws Exception {
            ChannelPipeline p = Channels.pipeline();
            p.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
            p.addLast("decoder", new StringDecoder());
            p.addLast("encoder", new StringEncoder());
            p.addLast("echo", new EchoServerHandler());
            return p;
        }
    };

    private final class EchoServerHandler extends SimpleChannelUpstreamHandler {

        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {

            e.getChannel().write(e.getMessage() + "\n");
            System.out.println(String.format("Recieved %s", e.getMessage()));
        }

        @Override
        public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            System.out.println("Channel opened");
            super.channelOpen(ctx, e);
        }

        @Override
        public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            System.out.println("Channel closed");
            super.channelClosed(ctx, e);
        }
    }

    int port = 8081;

    public ReactiveServer(int port) {
        this.port = port;
    }

    public void run() {
        // The Netty NIO library and the java library used to access sockets are
        // the Facade pattern.

        // Create the channel factory, acting as part of the acceptor from
        // acceptor/connector.
        ServerSocketChannelFactory acceptorFactory = new OioServerSocketChannelFactory();
        ServerBootstrap server = new ServerBootstrap(acceptorFactory);

        // The pipelines string together handlers, we set a factory to create
        // pipelines( handlers ) to handle events.
        // For Netty is using the reactor pattern to react to data coming in
        // from the open connections.
        server.setPipelineFactory(new EchoServerPipelineFactory());

        server.bind(new InetSocketAddress(port));
    }

    public static void main(String[] args) {

        int port = (args.length > 1) ? Integer.valueOf(args[0]) : 8081;

        System.out.println(" Assignment 3: Echo Server w/Netty ");
        System.out.println(" Using port " + port);
        System.out.println(" Ctrl-C to QUIT");
        System.out.println("------------------------------------------");

        new ReactiveServer(port).run();
    }
}
