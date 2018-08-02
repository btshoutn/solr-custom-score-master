package inter;

import org.apache.lucene.index.LeafReaderContext;

import java.io.IOException;
import java.util.List;

/**
 * Created by xiaotian on 2018/4/21.
 */
public abstract class ScoreAlgorithmInter {

    //接受传过来的参数
    public static List<String> params=null;

    public static LeafReaderContext context=null;

    public static String query=null;


    /**
     * 打分计算
     * @param doc
     * @param subQueryScore
     * @param valSrcScore
     * @return
     * @throws IOException
     */
    public static  float customScore(int doc, float subQueryScore, float valSrcScore) throws IOException{
        return 1f;
    }
}
