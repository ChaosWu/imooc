import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Date;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 * ********************************************************
 * @文件名称：MinaService.java
 * @文件作者：renzhiqiang
 * @创建时间：2016年5月31日 下午8:51:03
 * @文件描述：这样一个我们最简单的服务器就抢建好了
 * @修改历史：2016年5月31日创建初始版本
 *********************************************************
 */
public class MinaService
{
	public static void main(String[] args)
	{
		IoAcceptor acceptor = new NioSocketAcceptor();
		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		acceptor.setHandler(new TimeServerHandler());
		acceptor.getSessionConfig().setReadBufferSize(2048);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		try
		{
			acceptor.bind(new InetSocketAddress(9124));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static class TimeServerHandler extends IoHandlerAdapter
	{
		@Override
		public void sessionOpened(IoSession session) throws Exception
		{
			// 模拟向客户端发送一条消息(json对象)
			String data = "{\n"
					+ "    \"ecode\": \"0\",\n"
					+ "    \"emsg\": \"\",\n"
					+ "    \"data\": {\n"
					+ "        \"systemValues\": [\n"
					+ "            {\n"
					+ "                \"type\": 3,\n"
					+ "                \"site\": \"慕课\",\n"
					+ "                \"title\": \"慕课网\",\n"
					+ "                \"info\": \"慕课网攻略\",\n"
					+ "                \"dayTime\": \"3天前\",\n"
					+ "                \"contentUrl\": \"http://www.imooc.com\",\n"
					+ "                \"imageUrl\": \"http://img3.imgtn.bdimg.com/it/u=2417006062,3587518707&fm=21&gp=0.jpg\"\n"
					+ "            },\n"
					+ "            {\n"
					+ "                \"type\": 3,\n"
					+ "                \"site\": \"慕课\",\n"
					+ "                \"title\": \"慕女神\",\n"
					+ "                \"info\": \"又有新课程啦，快来围观\",\n"
					+ "                \"dayTime\": \"3天前\",\n"
					+ "                \"contentUrl\": \"http://www.imooc.com/learn/304\",\n"
					+ "                \"imageUrl\": \"http://img0.imgtn.bdimg.com/it/u=1683180650,1714186648&fm=21&gp=0.jpg\"\n"
					+ "            }\n"
					+ "        ],\n"
					+ "        \"zanValues\": [\n"
					+ "            {\n"
					+ "                \"type\": 2,\n"
					+ "                \"site\": \"tb13145\",\n"
					+ "                \"title\": \"非学好\",\n"
					+ "                \"info\": \"课程非常好,我已经买了\",\n"
					+ "                \"dayTime\": \"3天前\",\n"
					+ "                \"photoUrl\": \"http://d.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=f0466418f41fbe091c0bcb105e502005/eac4b74543a98226447170498c82b9014a90eb28.jpg\",\n"
					+ "                \"qq\": \"277451977\"\n"
					+ "            },\n"
					+ "            {\n"
					+ "                \"type\": 2,\n"
					+ "                \"site\": \"小灰灰\",\n"
					+ "                \"title\": \"好,很好,非学好\",\n"
					+ "                \"info\": \"课程很好,非常好,楼主好人\",\n"
					+ "                \"dayTime\": \"5天前\",\n"
					+ "                \"photoUrl\": \"http://v1.qzone.cc/avatar/201309/13/20/59/52330c3e104d9472.jpg%21200x200.jpg\",\n"
					+ "                \"qq\": \"942591746\"\n"
					+ "            }\n"
					+ "        ],\n"
					+ "        \"msgValues\": [\n"
					+ "            {\n"
					+ "                \"type\": 1,\n"
					+ "                \"site\": \"王的女人\",\n"
					+ "                \"title\": \"课程还会出吗?\",\n"
					+ "                \"info\": \"您的课程非好，期待出续集。\",\n"
					+ "                \"dayTime\": \"3天前\",\n"
					+ "                \"qq\": \"277451977\",\n"
					+ "                \"photoUrl\": \"http://img5.imgtn.bdimg.com/it/u=3845370438,3189689799&fm=11&gp=0.jpg\"\n"
					+ "            },\n" + "            {\n" + "                \"type\": 1,\n"
					+ "                \"site\": \"举起手来\",\n" + "                \"title\": \"感兴趣\",\n"
					+ "                \"info\": \"我对你的东西有兴趣\",\n" + "                \"dayTime\": \"2天前\",\n"
					+ "                \"photoUrl\": \"http://pic.qqtn.com/up/2016-7/2016072614451378952.jpg\",\n"
					+ "                \"qq\": \"942591746\"\n" + "            }\n" + "        ]\n" + "    }\n" + "}";

			session.write(data);
		}

		@Override
		public void exceptionCaught(IoSession session, Throwable cause) throws Exception
		{
			cause.printStackTrace();
		}

		@Override
		public void messageReceived(IoSession session, Object message) throws Exception
		{
			String str = message.toString();
			if (str.trim().equalsIgnoreCase("quit"))
			{
				session.close();
				return;
			}

			Date date = new Date();
			session.write(date.toString()); // 往客户端写的数据
			System.out.println("接收到的消为:" + str);
		}

		@Override
		public void messageSent(final IoSession session, Object message) throws Exception
		{

		}

		@Override
		public void sessionIdle(IoSession session, IdleStatus status) throws Exception
		{
			System.out.println("IDLE " + session.getIdleCount(status));
		}

		@Override
		public void sessionClosed(IoSession session) throws Exception
		{
		}
	}
}
