package com.keuin.fastjoin.mixin;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.network.*;
import net.minecraft.util.Lazy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.InetAddress;

import static net.minecraft.network.ClientConnection.CLIENT_IO_GROUP;
import static net.minecraft.network.ClientConnection.CLIENT_IO_GROUP_EPOLL;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

//	@Inject(at = @At("HEAD"), method = "init()V")
//	private void init(CallbackInfo info) {
//		System.out.println("This line is printed by an example mod mixin!");
//	}

	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * @author Keuin
	 * @reason Replace address to address.getHostAddress()
	 */
	@Overwrite
	public static ClientConnection connect(InetAddress address, int port, boolean shouldUseNativeTransport) {
		String addr = address.getHostAddress();
		LOGGER.info("You are using the manipulated connecting method (by FastJoin).");
		LOGGER.info(String.format("Connecting to server %s ...", addr));
		final ClientConnection clientConnection = new ClientConnection(NetworkSide.CLIENTBOUND);
		Class class2;
		Lazy lazy2;
		if (Epoll.isAvailable() && shouldUseNativeTransport) {
			class2 = EpollSocketChannel.class;
			lazy2 = CLIENT_IO_GROUP_EPOLL;
		} else {
			class2 = NioSocketChannel.class;
			lazy2 = CLIENT_IO_GROUP;
		}

		LOGGER.info("Connecting ...");
		long t = System.currentTimeMillis();
		(new Bootstrap()).group((EventLoopGroup)lazy2.get()).handler(new ChannelInitializer<Channel>() {
			protected void initChannel(Channel channel) throws Exception {
				try {
					channel.config().setOption(ChannelOption.TCP_NODELAY, true);
				} catch (ChannelException ignored) {
				}

				channel.pipeline().addLast("timeout", new ReadTimeoutHandler(30)).addLast("splitter", new SplitterHandler()).addLast("decoder", new DecoderHandler(NetworkSide.CLIENTBOUND)).addLast("prepender", new SizePrepender()).addLast("encoder", new PacketEncoder(NetworkSide.SERVERBOUND)).addLast("packet_handler", clientConnection);
			}
		}).channel(class2).connect(addr, port).syncUninterruptibly(); // use address.getHostAddress() instead of address, we avoid doing slow host name reverse lookup.
		t = System.currentTimeMillis() - t;
		LOGGER.info(String.format("Connection established. Time elapsed: %d ms.", t));
		return clientConnection;
	}
}
