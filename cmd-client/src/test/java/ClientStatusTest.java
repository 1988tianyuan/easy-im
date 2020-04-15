import static org.junit.Assert.*;

import org.junit.Test;

import com.tianyuan.easyui.cmdclient.chat.ClientStatus;
import com.tianyuan.easyui.cmdclient.console.ConsoleCommand;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/4/15 13:16
 */
public class ClientStatusTest {
	
	@Test
	public void command_valid() {
		ClientStatus status = ClientStatus.INIT;
		assertFalse(status.isValid(ConsoleCommand.LOGOUT));
		assertTrue(status.isValid(ConsoleCommand.LOGIN));
		assertTrue(status.isValid(ConsoleCommand.QUIT));
		assertFalse(status.validChatStatus());
		
		status = ClientStatus.LOGGED_IN;
		assertTrue(status.isValid(ConsoleCommand.LOGOUT));
		assertFalse(status.isValid(ConsoleCommand.LOGIN));
		assertTrue(status.isValid(ConsoleCommand.QUIT));
		assertTrue(status.validChatStatus());
		
		status = ClientStatus.QUITTED;
		assertFalse(status.isValid(ConsoleCommand.LOGOUT));
		assertFalse(status.isValid(ConsoleCommand.LOGIN));
		assertFalse(status.isValid(ConsoleCommand.QUIT));
		assertFalse(status.validChatStatus());
	}
}
