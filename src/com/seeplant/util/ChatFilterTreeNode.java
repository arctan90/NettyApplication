package com.seeplant.util;
import java.util.HashMap;
/**
 * 聊天过滤器查找树的节点。
 * 在查找树中，每一个Key都必须对应一个节点
 * 最后一个Key对应的节点中isEnd==true，nextNodeMap.size==0
 * @author yuantao
 *
 */
public class ChatFilterTreeNode {
        private boolean isEnd = true;
        private HashMap<String, ChatFilterTreeNode> nextNodeMap = null;
        private boolean isOverLapEnd = false;
        /**
         * Lazy Getter and Setter
         * @return
         */
        
        public HashMap<String, ChatFilterTreeNode> getNextNodeMap() {
            if (nextNodeMap==null) {
                nextNodeMap = new HashMap<String, ChatFilterTreeNode>();
            }
            return nextNodeMap;
        }
        
        public void setNextNodeMap(HashMap<String, ChatFilterTreeNode> nextNodeMap) {
            this.nextNodeMap = nextNodeMap;
        }
        
        public boolean isEnd() {
            return isEnd;
        }
        
        public void setEnd(boolean isEnd) {
            this.isEnd = isEnd;
        }

        public boolean isOverLapEnd() {
            return isOverLapEnd;
        }

        public void setOverLapEnd(boolean isOverLapEnd) {
            this.isOverLapEnd = isOverLapEnd;
        }
}
