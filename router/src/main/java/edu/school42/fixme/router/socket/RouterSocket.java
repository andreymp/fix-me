package edu.school42.fixme.router.socket;

import edu.school42.fixme.common.converter.FixMessageMapper;
import edu.school42.fixme.common.creator.MessageCreator;
import edu.school42.fixme.common.dto.FixMessageDto;
import edu.school42.fixme.common.dto.MessageType;
import edu.school42.fixme.router.exception.RouterException;
import edu.school42.fixme.router.proccessor.MessageProcessor;
import edu.school42.fixme.router.repository.FixMessagesRepository;
import edu.school42.fixme.router.service.FixMessagesService;
import edu.school42.fixme.router.source.ASource;
import edu.school42.fixme.router.source.BrokerSource;
import edu.school42.fixme.router.source.MarketSource;
import edu.school42.fixme.router.table.RoutingTable;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
@Accessors(chain = true)
@RequiredArgsConstructor
public class RouterSocket implements Runnable {

	private static final int ID = 0;

	private final int port;
	private final RoutingTable routingTable;
	private final FixMessageMapper mapper;
	private final MessageCreator messageCreator;

	private final Executor executor = Executors.newSingleThreadExecutor();

	@Override
	public void run() {
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			log.info("server-socket started at port :: {}", serverSocket.getLocalPort());
			while (true) {
				Socket socket = serverSocket.accept();

				ASource source = switch (socket.getPort()) {
					case 5000 -> new BrokerSource(socket);
					case 5001 -> new MarketSource(socket);
					default -> throw new RouterException(String.format("unknown port :: %d", socket.getPort()));
				};

				routingTable.add(source);

				PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
				pw.println(mapper.toFixString(messageCreator.confirmationOfIdMessage(source.getId(), ID)));

				executor.execute(new MessageProcessor(source, routingTable, mapper, messageCreator, new FixMessagesService(new FixMessagesRepository())));
			}
		} catch (Exception e) {
			throw new RouterException(e.getMessage());
		}
	}
}
