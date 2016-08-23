/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.seeplant.util;

import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLiteOrBuilder;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;
import static io.netty.buffer.Unpooled.*;

/**
 * Encodes the requested <a href="http://code.google.com/p/protobuf/">Google
 * Protocol Buffers</a> {@link Message} and {@link MessageLite} into a
 * {@link ByteBuf}. A typical setup for TCP/IP would be:
 * <pre>
 * {@link ChannelPipeline} pipeline = ...;
 *
 * // Decoders
 * pipeline.addLast("frameDecoder",
 *                  new {@link LengthFieldBasedFrameDecoder}(1048576, 0, 4, 0, 4));
 * pipeline.addLast("protobufDecoder",
 *                  new {@link ProtobufDecoder}(MyMessage.getDefaultInstance()));
 *
 * // Encoder
 * pipeline.addLast("frameEncoder", new {@link LengthFieldPrepender}(4));
 * pipeline.addLast("protobufEncoder", new {@link ProtobufEncoder}());
 * </pre>
 * and then you can use a {@code MyMessage} instead of a {@link ByteBuf}
 * as a message:
 * <pre>
 * void channelRead({@link ChannelHandlerContext} ctx, MyMessage req) {
 *     MyMessage res = MyMessage.newBuilder().setText(
 *                               "Did you say '" + req.getText() + "'?").build();
 *     ch.write(res);
 * }
 * </pre>
 */

/**
 * 这个编码器用来适配带命令字的protocolBuf
 * @author YuanTao
 */
@Sharable
public class MyProtobufEncoder extends MessageToMessageEncoder<MyMessageLite> {
    @Override
    protected void encode(
            ChannelHandlerContext ctx, MyMessageLite msg, List<Object> out) throws Exception {
        MessageLiteOrBuilder frame = msg.getMessageLit();
        
        if (frame instanceof MessageLite) {
            //add 'command' into outStream
            byte[] command = msg.getCommand();
            byte[] load = ((MessageLite) frame).toByteArray();        
            ByteBufAllocator allocor = ctx.alloc();
            ByteBuf buff = allocor.buffer(command.length + load.length);
            buff.writeBytes(command);
            buff.writeBytes(load);
            
            byte[] dst = new byte[load.length + 2];
            buff.getBytes(0, dst);
            out.add(buff);
            
            return;
        }
        if (frame instanceof MessageLite.Builder) {
            out.add(wrappedBuffer(((MessageLite.Builder) frame).build().toByteArray()));
        }
    }
}
