package com.blue.business.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.blue.business.dto.TransactionDetailsDto;
import com.blue.business.service.TransactionDetailsService;
import com.blue.business.util.DateUtils;
import com.fire.dto.entity.OrderList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.ParsedSum;
import org.elasticsearch.search.aggregations.metrics.SumAggregationBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @date: 2022-03-22 14:59
 */
@Service
@Slf4j
public class TransactionDetailsServiceImpl implements TransactionDetailsService {

    @Resource
    private ElasticsearchRestTemplate template;

    @Override
    public Map<String, Object> getTransactionDetails(TransactionDetailsDto dto) {
        HashMap<String, Object> map = Maps.newHashMap();
        List<OrderList> list = Lists.newArrayList();

        String sortField = "callbackTime";
        Sort.Direction sortBy = Sort.Direction.DESC;

        Sort sort = Sort.by(sortBy, sortField);

        int current = 0;
        int size = 10;

        if (ObjectUtil.isNotEmpty(dto.getCurrent())) {
            current = dto.getCurrent() - 1;
        }

        if (ObjectUtil.isNotEmpty(dto.getSize())) {
            size = dto.getSize();
        }

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        RangeQueryBuilder rangeQueryBuilder;

        if (ObjectUtil.isEmpty(dto.getQueryDate())) {
            map.put("sumPrice", new BigDecimal("0.00"));
            map.put("records", list);
            map.put("current", 1);
            map.put("size", 10);
            map.put("total", 0);
            map.put("pages", 0);
            return map;
        }

        rangeQueryBuilder = QueryBuilders.rangeQuery("callbackTime")
                .from(DateUtils.getDayBeginTimestamp(dto.getQueryDate()))
                .to(DateUtils.getDayEndTimestamp(dto.getQueryDate()));

        boolQuery.must(rangeQueryBuilder);

        SumAggregationBuilder sumAggregationBuilder = AggregationBuilders.sum("sumPrice").field("price");

        NativeSearchQuery queryBuilder = new NativeSearchQueryBuilder()
                .withPageable(PageRequest.of(current, size, sort))
                .withQuery(boolQuery)
                .addAggregation(sumAggregationBuilder)
                .build();

        queryBuilder.setTrackTotalHits(true);

        try {
            SearchHits<OrderList> searchHits = template.search(queryBuilder, OrderList.class);
            Aggregations aggregations = searchHits.getAggregations();
            double sumPrice = ((ParsedSum) aggregations.get("sumPrice")).getValue();
            long totalHits = searchHits.getTotalHits();

            List<SearchHit<OrderList>> hits = searchHits.getSearchHits();
            log.info("数据总数有:" + totalHits + "条");
            log.info("搜索结果有:" + hits.size() + "条");

            hits.forEach(hit -> {
                OrderList orderList = hit.getContent();
                list.add(orderList);
            });

            map.put("sumPrice", sumPrice);
            map.put("records", list);
            map.put("current", current + 1);
            map.put("size", size);
            map.put("total", totalHits);
            map.put("pages", totalHits % size == 0 ? (int) totalHits / size : (int) totalHits / size + 1);
        } catch (Exception e) {
            log.error("es查询交易明细异常!", e);
        }

        return map;
    }
}
