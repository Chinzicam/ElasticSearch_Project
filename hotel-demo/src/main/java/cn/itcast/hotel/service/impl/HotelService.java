package cn.itcast.hotel.service.impl;

import cn.itcast.hotel.mapper.HotelMapper;
import cn.itcast.hotel.pojo.Hotel;
import cn.itcast.hotel.pojo.HotelDoc;
import cn.itcast.hotel.pojo.PageResult;
import cn.itcast.hotel.pojo.RequestParams;
import cn.itcast.hotel.service.IHotelService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;

@Service
public class HotelService extends ServiceImpl<HotelMapper, Hotel> implements IHotelService {
    @Autowired
    private RestHighLevelClient client;

    @Override
    public PageResult search(RequestParams params) {
        try {
            // 1. 准备request对象
            SearchRequest request = new SearchRequest("hotel");
            // 2. 准备DSL
            // 2.1 获取搜索关键字
            String key = params.getKey();
            // 2.2 健壮性判断
            if (StringUtils.isEmpty(key)) {
                // 未输入搜索条件，则查询全部
                request.source().query(QueryBuilders.matchAllQuery());
            } else {
                request.source().query(QueryBuilders.matchQuery("all", key));
            }
            // 2.3 分页
            int page = params.getPage();
            int size = params.getSize();
            request.source()
                    .from((page - 1) * size)
                    .size(size);
            // 3. 发送请求
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            // 4. 结果解析
            return handleResponse(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 结果解析依旧是封装为了一个函数
    private PageResult handleResponse(SearchResponse response) {
        // 获取总条数
        SearchHits searchHits = response.getHits();
        long total = searchHits.getTotalHits().value;
        // 获取文档数组
        SearchHit[] hits = searchHits.getHits();
        // 遍历
        ArrayList<HotelDoc> hotels = new ArrayList<>();
        for (SearchHit hit : hits) {
            // 获取每条文档
            String json = hit.getSourceAsString();
            // 反序列化为对象
            HotelDoc hotelDoc = JSON.parseObject(json, HotelDoc.class);
            // 放入集合
            hotels.add(hotelDoc);
        }
        // 封装返回
        return new PageResult(total, hotels);
    }
}
