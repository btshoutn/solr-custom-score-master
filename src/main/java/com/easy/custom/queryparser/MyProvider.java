package com.easy.custom.queryparser;

import impl.GomeMarketCustomScore;
import org.apache.lucene.index.*;
import org.apache.lucene.queries.CustomScoreProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Created by xiaotian on 2018/4/20.
 */
public class MyProvider extends CustomScoreProvider {

    private List<String> params;
    final static Logger log= LoggerFactory.getLogger(MyProvider.class);

    private  String query;
    //配置参数
    public void setParams(List<String> params) {
        this.params = params;
    }

    public MyProvider(LeafReaderContext context) {
        super(context);
    }
    public MyProvider(LeafReaderContext context,String query) {
        super(context);
        this.query = query;
    }
    /**
     * 超市搜索打分策略
     * 后续逐步实现个性化搜索
     */
    @Override
    public float customScore(int doc, float subQueryScore, float valSrcScore) throws IOException {


        /**
         * 可以根据不通业务 不通需求场景实现
         * 个性化的需求搜索
         */

//        if ("超市搜索"){
//            GomeMarketCustomScore.init(params,this.context,query);
//            return GomeMarketCustomScore.customScore(doc,subQueryScore,valSrcScore);
//        }
//        if ("超市活动搜索"){
//            //实现自己的个性排序规则
//        }

         GomeMarketCustomScore.init(params,this.context,query);
        return GomeMarketCustomScore.customScore(doc,subQueryScore,valSrcScore);

        //return  subQueryScore*valSrcScore*is_self_score*spu_score.get(doc);
    }

    public static void main(String[] args) {
      /*  log.info("查询一次：docid:{} sku:{} is_self:{} 1score:{} 2score:{} is_self_score:{} spu_score:{} ",
                1,1,1
                ,1,1,1,1);
        log.info("sssss");*/
    }
}
