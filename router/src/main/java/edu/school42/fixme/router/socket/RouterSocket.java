package edu.school42.fixme.router.socket;

import edu.school42.fixme.common.converter.FixMessageMapper;
import edu.school42.fixme.common.creator.MessageCreator;
import edu.school42.fixme.router.Router;
import edu.school42.fixme.router.exception.RouterException;
import edu.school42.fixme.router.proccessor.MessageProcessor;
import edu.school42.fixme.router.service.FixMessagesService;
import edu.school42.fixme.router.source.ASource;
import edu.school42.fixme.router.source.BrokerSource;
import edu.school42.fixme.router.source.MarketSource;
import edu.school42.fixme.router.table.RoutingTable;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Accessors(chain = true)
@RequiredArgsConstructor
public class RouterSocket implements Runnable {
	private final int port;
	private final FixMessageMapper mapper;
	private final MessageCreator messageCreator;
	private final FixMessagesService fixMessagesService;
	private final Executor executor = Executors.newSingleThreadExecutor();

	@Override
	public void run() {
		try (ServerSocket serverSocket = new ServerSocket(port, 1000, Inet4Address.getByName("0.0.0.0"))) {
			log.info("server-socket started at port :: {}", serverSocket.getLocalPort());
			while (true) {
				Socket socket = serverSocket.accept();
				ASource source = switch (socket.getLocalPort()) {
					case 5000 -> {
						log.info("connected new broker");
						yield new BrokerSource(socket);
					}
					case 5001 -> {
						log.info("connected new market");
						yield new MarketSource(socket);
					}
					default -> throw new RouterException(String.format("unknown port :: %d", socket.getPort()));
				};
				Router.ROUTING_TABLE.add(source);
				log.info("added {} with id :: {}", source.getType(), source.getId());
				executor.execute(new MessageProcessor(source, socket, mapper, messageCreator, fixMessagesService));
			}
		} catch (Exception e) {
			throw new RouterException(e.getMessage());
		}
	}
}
