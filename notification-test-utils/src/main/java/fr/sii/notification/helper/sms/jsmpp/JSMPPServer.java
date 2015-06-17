package fr.sii.notification.helper.sms.jsmpp;

import java.util.List;

import org.jsmpp.bean.SubmitSm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.sii.notification.helper.sms.rule.SmppServerException;
import fr.sii.notification.helper.sms.rule.SmppServerSimulator;

/**
 * The server simulator based on <a href="http://jsmpp.org/">jsmpp</a>
 * samples.
 */
public class JSMPPServer implements SmppServerSimulator<SubmitSm> {
	private static final Logger LOG = LoggerFactory.getLogger(JSMPPServer.class);

	private Thread thread;

	private final JSMPPServerSimulator simulator;

	public JSMPPServer(int port) {
		super();
		simulator = new JSMPPServerSimulator(port);
	}

	@Override
	public void start() {
		LOG.debug("starting simulator thread...");
		simulator.reset();
		thread = new Thread(simulator);
		thread.start();
		LOG.debug("simulator thread started");
	}
	
	@Override
	public synchronized void stop() throws SmppServerException {
		try {
			LOG.debug("stopping simulator thread...");
			simulator.stop();
			thread.interrupt();
			thread.join();
			LOG.debug("simulator thread stopped");
		} catch (InterruptedException e) {
			throw new SmppServerException("Failed to stop JSMPP server", e);
		}
	}
	
	@Override
	public int getPort() {
		return simulator.getPort();
	}
	
	@Override
	public List<SubmitSm> getReceivedMessages() {
		return simulator.getReceivedMessages();
	}

}
