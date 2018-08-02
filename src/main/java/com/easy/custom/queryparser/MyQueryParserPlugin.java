package com.easy.custom.queryparser;

import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.QParser;
import org.apache.solr.search.QParserPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by xiaotian on 2018/4/20.
 */
public class MyQueryParserPlugin extends QParserPlugin {
    final static Logger log= LoggerFactory.getLogger(MyQueryParserPlugin.class);
    //接受传过来的参数
    List<String> params;
    public void init(NamedList args) {
        //得到一个映射之后，转成NamedList便于操作
        params=((NamedList)args.get("words")).getAll("word");
        if(params.isEmpty()){
            throw new SolrException(SolrException.ErrorCode.SERVER_ERROR,"至少有一个参数");
        }
        // handle configuration parameters
        // passed through solrconfig.xml
        for(String pm:params){
            log.info("参数：{}",pm);
        }
        this.params=params;
    }


    @Override
    public QParser createParser(String s, SolrParams solrParams, SolrParams solrParams1, SolrQueryRequest solrQueryRequest) {
        log.info("solrParams:"+solrParams);
        log.info("solrParams1:"+solrParams1);
        log.info("solrQueryRequest:"+solrQueryRequest);
        return  new MyParser(params,s,solrParams,solrParams1,solrQueryRequest);
    }

}
