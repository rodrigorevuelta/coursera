package org.rodrigorr.cloud.assignment3;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class ReactiveServer {

	public class EchoServerHandler extends SimpleChannelUpstreamHandler {
		
		@Override
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
				throws Exception {
			super.messageReceived(ctx, e);
		}
	}
	
}
