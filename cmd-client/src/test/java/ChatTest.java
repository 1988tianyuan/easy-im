import static org.junit.Assert.*;

import org.junit.Test;

import com.tianyuan.easyui.cmdclient.chat.ChatContext;
import com.tianyuan.easyui.cmdclient.chat.ChatType;
import com.tianyuan.easyui.cmdclient.chat.handler.ChatHandler;
import com.tianyuan.easyui.cmdclient.chat.handler.GroupChatHandler;
import com.tianyuan.easyui.cmdclient.chat.handler.P2PChatHandler;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/4/15 10:48
 */
public class ChatTest {
	
	private String validInput1 = ":liugeng hello";
	private String validInput2 = ":liugeng   hello world!!";
	private String validInput3 = ">group hello";
	private String validInput4 = ">group   hello world!!";
	
	@Test
	public void chatMsg_valid() {
		assertEquals(ChatType.getChatType(validInput1), ChatType.P2P);
		assertEquals(ChatType.getChatType(validInput2), ChatType.P2P);
		assertEquals(ChatType.getChatType(validInput3), ChatType.GROUP);
		assertEquals(ChatType.getChatType(validInput4), ChatType.GROUP);
		
		String invalidInput1 = "liugeng hello";
		String invalidInput2 = ":liugeng";
		String invalidInput3 = ":group";
		assertNull(ChatType.getChatType(invalidInput1));
		assertNull(ChatType.getChatType(invalidInput2));
		assertNull(ChatType.getChatType(invalidInput3));
	}
	
	@Test
	public void chatMsg_parse() {
		ChatHandler p2PChatHandler = new P2PChatHandler(new ChatContext());
		assertEquals("liugeng", p2PChatHandler.parseTarget(validInput1));
		assertEquals("hello", p2PChatHandler.parseMessage(validInput1));
		assertEquals("liugeng", p2PChatHandler.parseTarget(validInput2));
		assertEquals("hello world!!", p2PChatHandler.parseMessage(validInput2));
		
		ChatHandler groupChatHandler = new GroupChatHandler(new ChatContext());
		assertEquals("group", groupChatHandler.parseTarget(validInput3));
		assertEquals("hello", groupChatHandler.parseMessage(validInput3));
		assertEquals("group", groupChatHandler.parseTarget(validInput4));
		assertEquals("hello world!!", groupChatHandler.parseMessage(validInput4));
	}
}
