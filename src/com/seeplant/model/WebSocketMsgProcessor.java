package com.seeplant.model;

import org.seeplant.myjson.JSONObject;

import com.seeplant.bean.ChattingMap;
import com.seeplant.bean.MsgType;
import com.seeplant.util.MyLogger;
import io.netty.channel.ChannelHandlerContext;

public class WebSocketMsgProcessor implements Runnable {
	private String msg;
	private ChannelHandlerContext ctx;
	public WebSocketMsgProcessor(ChannelHandlerContext ctx, String msg) {
		// TODO Auto-generated constructor stub
		this.ctx = ctx;
		this.msg = msg;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (msg != null && !msg.isEmpty()) {
			JSONObject jObject = new JSONObject(msg);
			String uid = "0";
			String roomId = "0";
			String userType = "";
			String msgType = "";
			if (jObject.has("uid")) {
				uid = jObject.getString("uid");
			}
			if (jObject.has("roomId")) {
				roomId = jObject.getString("roomId");
			}
			if (jObject.has("userType")) {
				userType = jObject.getString("userType");
			}
			if (jObject.has("type")) {
				msgType = jObject.getString("type");
			}
			try {
				// String msgType = jObject.getString("type");
				if (!msgType.equals("heartbeat")) {
					MyLogger.log(msg);
				}

				switch (msgType) {
				case MsgType.WEBSOCKET_SPELL:
					processChatting(jObject);
					break;
				case MsgType.WEBSOCKET_BYE:
					break;
				default:
					break;
				}
			} catch (Exception e) {
				StackTraceElement[] traces = e.getStackTrace();
				StringBuffer sb = new StringBuffer();
				sb.append(e.getMessage()).append(":");
				for (StackTraceElement trace : traces) {
					sb.append(trace.toString());
				}
				JSONObject exception = new JSONObject().put("type", "exception").put("uid", uid).put("roomId", roomId)
						.put("userType", userType).put("message", sb.toString()).put("level", "warning")
						.put("msgType", msgType);
				MyLogger.log(exception.toString());
			}
		}
	}

	private void processChatting(JSONObject jObject) {
		ChattingMap.wsChatting(jObject, ctx);
	}

	public int processLogin(JSONObject jObject) {
		int responseCode = 0;
		Long uid = jObject.getLong("uid");
		Long roomId = jObject.getLong("roomId");
		String nickName = jObject.getString("nickName");

		ChattingMap.wsChatJoinIn(uid, roomId, ctx, nickName);
		return responseCode;
	}
}
