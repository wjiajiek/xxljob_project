package com.xxl.job.executor.service.jobhandler;

import com.alibaba.fastjson.JSON;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.IJobHandler;
//import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.executor.annotation.DistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.xxl.job.core.biz.model.ReturnT.SUCCESS;

/**
 * @author fueen
 * 编写自己的定时任务Handler
 * 继承IJobHandler
 */

//@XxlJob(value = "MyJobHandler")
@Component
@Slf4j
public class MyJobHandler {

    private final String HANLP = "HanLP";
    private final String NLP = "Nlp";
//    @Value("${fengci.stop.dictionary}")
//    private String stopDictionaryPath;

    @XxlJob("mydemo")
    public void demoJobHandler() throws Exception {
        System.out.println("哇哇哇哇哇");
    }

    @XxlJob("testHanleDictionary")
    public void testHanleDictionary() {

        String text = "15盐焗鸡 null  铁板鱿鱼，小米椒 下午茶套餐 手撕鸡11231231231231231 猪脚+米饭 周六 1320-9 土豆炒肉、白灼青菜 15咖喱鸡 111 周一 小吃+奶茶 1258-周日 1318-周四 无糖可乐 1258-周六 手撕鸡（微辣） 生日餐菜品AA 1320-21 可乐鸡翅 1320-20 铁板鱿鱼+辣椒 酸菜鱼 周三 A套餐18 10盐焗鸡 1258-周四 爆汁鸡排 123 规格的餐品 虎皮凤爪 369 川湘菜1231231231123123 酸奶+面包 蜜汁鸡翅-1258 早餐套餐 1320-10 午餐套餐 铁板鱿鱼A 测试规格的餐品 手撕鸡 1318-周三 1258小炒肉 川湘菜 粤港菜 铁板鱿鱼-1258 酸奶+面包2 晚餐套餐 酸菜鱼+米饭 小炒肉 测试边界值测试边界值测试边界值测试边界值测试边界值测试边界值测试边界值测试边界值测试边界值测试边界值 1318-周六 1258-周五 1319-10 12盐焗鸡 麦当劳B套餐-1318 咖喱鸡 小炒肉（重辣） 1258-周一 1318-周一 黑胡椒厚切牛排沙拉配香溢蛋料 姜葱白切鸡双拼饭 周四 1258-周三 麻辣鲜鱿 1258-周二 金牌手撕鸡";
        System.out.println(text);
        Set<String> terms = segment(HANLP,text);

    }
    /**
     * 分词器
     * @param type 分词器类型
     * @param content 分词文本
     * @return 返回分词结果
     */
    public Set<String> segment(String type, String content){
        Set<String> wordSet = new HashSet<>();

        if(StringUtils.isNotBlank(content)){
            // 根据不同分词器进行分词
            switch (type){
                case "HanLP":{
                    long startTime = System.currentTimeMillis();
                    CoreStopWordDictionary.load("xxl-job-executor-samples/xxl-job-executor-sample-springboot/src/main/resources/hanlpDictionary/stopwords.txt",false);
//                  CoreStopWordDictionary.load("E:/fencizidian/data/dictionary/mystopwords.txt",false);
                    List<Term> terms = HanLP.segment(content);
                    CoreStopWordDictionary.apply(terms);
                    long endTime = System.currentTimeMillis();
                    for (Term term : terms) {
                        String name = term.word;
                        String nature = term.nature.toString();
                        wordSet.add(name);
                    }
                    log.info("HanLP分词耗时={},content={},结果={}",endTime - startTime,content, JSON.toJSONString(wordSet));
                    break;
                }
                case "Nlp":{
                    NLPTokenizer.ANALYZER.enableCustomDictionary(true); // 使用用词典分词。
                    long startTime = System.currentTimeMillis();
                    List<Term> terms = NLPTokenizer.segment(content);
                    CoreStopWordDictionary.apply(terms);
                    long endTime = System.currentTimeMillis();
                    for (Term term : terms) {
                        String name = term.word;
                        String nature = term.nature.toString();
                        wordSet.add(name);
                    }
                    log.info("NLP分词耗时={},content={},结果={}",endTime - startTime,content, JSON.toJSONString(wordSet));
                    break;
                }
                default:{break;}
            }
        }
        return wordSet;
    }



    @XxlJob("testAopJob")
    @DistributedLock("kkwjob_TestJob_test")
    public void testAopJob(){
        log.info("kkwjob_TestJob_test");
        try {
            String jobParam = XxlJobHelper.getJobParam();
            Thread.sleep(Integer.parseInt(jobParam));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

