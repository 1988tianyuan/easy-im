import static org.junit.Assert.*;

import org.junit.Test;

import com.tianyuan.easyui.cmdclient.chat.ChatUtil;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/4/15 10:48
 */
public class ChatTest {
	
	private String validInput1 = ":liugeng hello";
	private String validInput2 = ":liugeng   hello world!!";
	
	@Test
	public void chatMsg_valid() {
		assertTrue(ChatUtil.isValidChat(validInput1));
		assertTrue(ChatUtil.isValidChat(validInput2));
		
		String invalidInput1 = "liugeng hello";
		String invalidInput2 = ":liugeng";
		assertFalse(ChatUtil.isValidChat(invalidInput1));
		assertFalse(ChatUtil.isValidChat(invalidInput2));
	}
	
	@Test
	public void chatMsg_parse() {
		assertEquals("liugeng", ChatUtil.parseTargetUer(validInput1));
		assertEquals("hello", ChatUtil.parseChatMsg(validInput1));
		assertEquals("liugeng", ChatUtil.parseTargetUer(validInput2));
		assertEquals("hello world!!", ChatUtil.parseChatMsg(validInput2));
	}
}
