package com.ruoyi.wordcloud;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import java.util.*;

public class TextAnalysisUtils {
    /**
     * 使用HanLP进行分词并生成词云数据，同时过滤掉标点符号和常见的无意义词汇
     * @param texts 需要分词的文本列表
     * @return 词云数据列表，每个元素是一个Map，包含词语及其出现频率
     */
    public static List<Map<String, Object>> generateWordCloudData(List<String> texts) {
        Map<String, Integer> wordCount = new HashMap<>();
        // 定义需要过滤掉的词和标点符号
        Set<String> filters = new HashSet<>(Arrays.asList("的", "了", "在", "是", "我", "需", "你们", "很", "再", "图书馆", "本书", "去", "能", "有", "和", "就",
                "不", "人", "都", "一个", "、", "。", "，", "！", "？", "（", "）", "：", "；", "\"", "“", "”", "《", "》", "…", "—"));

        for (String text : texts) {
            if (text != null && !text.isEmpty()) {
                List<Term> termList = HanLP.segment(text);
                for (Term term : termList) {
                    String word = term.word;
                    // 在这里过滤
                    if (!filters.contains(word)) {
                        wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
                    }
                }
            }
        }

        List<Map<String, Object>> wordCloudData = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
            Map<String, Object> wordData = new HashMap<>();
            wordData.put("name", entry.getKey());
            wordData.put("value", entry.getValue());
            wordCloudData.add(wordData);
        }

        return wordCloudData;
    }
}
