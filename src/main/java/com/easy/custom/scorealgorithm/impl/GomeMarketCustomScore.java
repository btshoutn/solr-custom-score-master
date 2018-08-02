package impl;

import inter.ScoreAlgorithmInter;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.BinaryDocValues;
import org.apache.lucene.index.DocValues;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.NumericDocValues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * http://10.144.43.141:8066/solr/supermarket/select?indent=on&q={!boost%20b=myfunc(shop_flag,spu_score)}%20product_ch:LG&wt=json
 * 国美超市搜索打分实现
 * Created by xiaotian on 2018/4/21.
 */
public   class GomeMarketCustomScore  extends ScoreAlgorithmInter{

    final static Logger log= LoggerFactory.getLogger(GomeMarketCustomScore.class);

    public static  void init(List<String> params1,LeafReaderContext context1,String query1){
        params = params1;
        context = context1;
        query = query1;
        /*log.info("params:"+params);
        log.info("context:"+context);
        log.info("query:"+query);*/
    }

    /**
     *
     * @param doc
     * @param subQueryScore
     * @param valSrcScore
     * @return
     * @throws IOException
     */
    public  static float customScore(int doc, float subQueryScore, float valSrcScore) throws IOException {
        //http://10.144.43.141:8066/solr/supermarket/select?indent=on&q=*:*&defType=myQueryParser&wt=json
        //sum(recip(ms(NOW,create_time),3.16e-8,1,1),product(is_self,10),product(spu_score,5))

        Document document = context.reader().document(doc);
        String timestamp  = document.get("create_time");
        //String create_time1 = document.get("create_time");
        //log.info("测试document"+document);
        log.info("timestamp"+timestamp);

        //直接从valuesource中读取数据
        float is_self_score=1;
        float is_in_store_score=1;
        NumericDocValues create_time =    DocValues.getNumeric(context.reader(), "create_time");
        BinaryDocValues sku = DocValues.getBinary(context.reader(), "sku");
        // SortedDocValues is_self = DocValues.getSorted(this.context.reader(), "is_self");
        BinaryDocValues is_self = DocValues.getBinary(context.reader(), "is_self");
        BinaryDocValues is_in_store = DocValues.getBinary(context.reader(), "is_in_store");
        final NumericDocValues spu_score = DocValues.getNumeric(context.reader(), "spu_score");

        if (is_self.get(doc).utf8ToString().equals("T")){
            is_self_score=10;
        }
        if (is_in_store.get(doc).utf8ToString().equals("T")){
            is_in_store_score=5;
        }

        log.info("is_self.get(doc).toString()"+is_self.get(doc).utf8ToString());

        /*
         * 通过得分相乘放大分数
         * 此处可以控制与原有得分结合的方式，加减乘除都可以
         * **/

        //打分算法,可以通过程序实现复杂的打分
        //recip(x,m,a,b)  a/(m*x+b)
        long time = System.currentTimeMillis()-create_time.get(doc);
        log.info("time="+time);
        double timeScore;
        timeScore = 1/(3.16e-8*time+1);
        Double totalScore = (timeScore+is_self_score+spu_score.get(doc) * 5)*subQueryScore*valSrcScore * is_in_store_score;

        log.info("前台参数：大小:{}  内容:{} ",params.size(),params);
        log.info("查询一次：docid:{} sku:{} is_self:{} subQueryScore:{} valSrcScore:{} is_self_score:{} spu_score:{} totalScore:{} ",
                doc,sku.get(doc).utf8ToString(),is_self.get(doc)
                ,subQueryScore,valSrcScore,is_self_score,spu_score.get(doc),totalScore.floatValue());

        return totalScore.floatValue();

    }
}
