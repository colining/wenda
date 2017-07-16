package cn.colining.service;

import cn.colining.controller.MyIndexController;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Created by colin on 2017/7/12.
 */
//通过实现InitializingBean 可以添加一个初始化bean后就执行的方法
@Service
public class SensitiveService implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);

    /**
     * Service初始化后，即进行读取文件操作
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader read = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                addWord(lineTxt.trim());
            }
        } catch (Exception e) {
            logger.error("读取文件失败" + e.getMessage());
        }
    }

    /**
     * 增加关键字的结点
     * 每次从lineText中读一个字符。然后查询当前结点下有没有这个字符
     * 如果没有就创建一下，然后当前指针向下移动
     * 如果lineText走到最后，说明为结尾结点
     * @param lineText
     */
    private void addWord(String lineText) {
        TrieNode temp = rootNode;
        for (int i = 0; i < lineText.length(); i++) {
            Character c = lineText.charAt(i);
            TrieNode node = temp.getSubNode(c);
            if (node == null) {
                node = new TrieNode();
                temp.addSubNode(c, node);
            }
            temp = node;
            if (i == lineText.length() - 1) {
                temp.setKeyWord(true);
            }
        }
    }

    private class TrieNode {
        //是否为关键词结尾的结点
        private boolean end = false;

        //前缀树中的一个结点持有一个map，这个map中就是当前结点的子结点
        private Map<Character, TrieNode> subNodes = new HashMap<Character, TrieNode>();

        //增加子节点
        public void addSubNode(Character key, TrieNode node) {
            subNodes.put(key, node);
        }

        //得到子节点
        TrieNode getSubNode(Character key) {
            return subNodes.get(key);
        }
        //判断是否为结尾节点
        boolean isKeyWordEnd() {
            return end;
        }
        //设置为结尾结点
        void setKeyWord(boolean end) {
            this.end = end;
        }
    }

    private TrieNode rootNode = new TrieNode();

    //判断是否为故意扰乱敏感词的字符
    private boolean isSymbol(char c) {
        int ic = (int) c;
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }

    /**
     * 用来过滤的函数
     * @param text
     * @return
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return text;
        }
        StringBuilder result = new StringBuilder();
        String repalcement = "***";
        TrieNode tempNode = rootNode;
        //begin一直向后移动，代表当前搜索的敏感词的头结点
        int begin = 0;
        //position是当前敏感词的某一个结点，来回移动的那个
        int position = 0;

        while (position < text.length()) {
            char c = text.charAt(position);
            if (isSymbol(c)) {
                if (tempNode == rootNode) {
                    result.append(c);
                    begin++;
                }
                position++;
                continue;
            }
            tempNode = tempNode.getSubNode(c);
            //当前结点为null，说明不是敏感词
            if (tempNode == null) {
                result.append(text.charAt(begin));
                position = begin + 1;
                begin = position;
                tempNode = rootNode;
            } else if (tempNode.isKeyWordEnd()) {
                result.append(repalcement);
                position = position + 1;
                begin = position;
                tempNode = rootNode;
            } else {
                ++position;
            }
        }
        //position走到了最后，别忘了把begin剩下的也加进来，
        //不过也有可能begin也没有啥嘞
        result.append(text.substring(begin));
        return result.toString();
    }


    public static void main(String[] args) {
        SensitiveService s = new SensitiveService();
        s.addWord("色情");
        s.addWord("赌博");
        System.out.println(s.filter("hi  你好色 情"));

    }
}
