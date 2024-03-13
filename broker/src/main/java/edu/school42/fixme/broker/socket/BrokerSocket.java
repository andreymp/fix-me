package edu.school42.fixme.broker.socket;

import edu.school42.fixme.broker.exception.BrokerException;
import edu.school42.fixme.broker.validator.BrokerFixValidator;
import edu.school42.fixme.common.converter.FixMessageMapper;
import edu.school42.fixme.common.exception.FixMessageValidationException;
import edu.school42.fixme.common.validation.FixMessageValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

@Slf4j
@RequiredArgsConstructor
public class BrokerSocket {

	private static final String HOST = "localhost";

	private final int port;

	private final FixMessageMapper mapper = new FixMessageMapper();

	public void start() throws BrokerException {
		log.info("""
				Broker ready to read fix-messages

				FIX: tag=value|
				TAGS:
				1. 54 -> side: BUY or SELL
				2. 55 -> instrument
				3. 53 -> quantity
				4. 262 -> market destination ID
				5. 44 -> price
				""");
		try (
				Socket socket = new Socket(HOST, port);
				PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))
		) {
			log.info("received :: {}", br.readLine());
			Scanner sc = new Scanner(System.in);
			FixMessageValidator validator = new BrokerFixValidator(mapper);
			while (sc.hasNext()) {
				String message = sc.nextLine();
				log.info("line :: \"{}\"", message);
				try {
					String incomingMessage = mapper.toFixString(validator.validate(message));

					pw.println(incomingMessage);
					log.info("sent :: {}", incomingMessage);
					handleResponse(br);
				} catch (FixMessageValidationException e) {
					log.error(e.getMessage());
				}
			}
		} catch (IOException e) {
			throw new BrokerException(e.getMessage());
		}
	}



	private void handleResponse(BufferedReader br) throws IOException {
		String message = br.readLine();
		if (Objects.nonNull(message)) {
			log.info("received :: {}", message);
		}
	}
}
