package com.blue.business.service.impl;

import com.blue.business.query.StatisticsQuery;
import com.blue.business.service.IStatisticsService;
import com.blue.business.util.DateUtils;
import com.blue.business.vo.ChartStatisticVo;
import com.blue.business.vo.CountStatisticsVo;
import com.blue.business.vo.PayTypeStatisticsVo;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.*;
import org.elasticsearch.search.aggregations.bucket.range.ParsedRange;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedSum;
import org.elasticsearch.search.aggregations.metrics.SumAggregationBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.fire.dto.constants.EsIndex.ORDER_INDEX;
import static com.fire.dto.constants.EsIndex.SCAN_CODE_INDEX;

/**
 * @author fcq
 * @version v2.0.8.business
 * @description 统计数据
 * @date 2022/3/22 15:28
 */
@Slf4j
@Service
public class StatisticsServiceImpl implements IStatisticsService {

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public CountStatisticsVo count(StatisticsQuery query) {
        Long merchantId = 1000000000000002L;
        IndexCoordinates orderIndexCoordinates = IndexCoordinates.of(ORDER_INDEX);
        IndexCoordinates scanIndexCoordinates = IndexCoordinates.of(SCAN_CODE_INDEX);
        String type = query.getType();
        Date date = new Date();
        long startTime = DateUtils.getDayBeginTimestamp(query.getDate());
        long endTime = DateUtils.getDayEndTimestamp(query.getDate());
        switch (type) {
            case "day":
                break;
            case "week":
                startTime = DateUtils.getWeekBeginTimestamp(date);
                endTime = DateUtils.getCurrentTimestamp();
                break;
            case "month":
                startTime = DateUtils.getMonthBeginTimestamp(date);
                endTime = DateUtils.getCurrentTimestamp();
                break;
            case "year":
                startTime = DateUtils.getYearBeginTimestamp(date);
                endTime = DateUtils.getCurrentTimestamp();
                break;
        }
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("createTime").from(startTime).to(endTime);
        TermQueryBuilder merchantIdQuery = QueryBuilders.termQuery("merchantId", merchantId);
        TermQueryBuilder statusBuilder = QueryBuilders.termQuery("status", 2);
        SumAggregationBuilder sumBuilder = AggregationBuilders.sum("turnover").field("price");
        //查询交易金额
        NativeSearchQuery turnoverQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.boolQuery()
                        .must(merchantIdQuery)
                        .must(statusBuilder)
                        .must(rangeQueryBuilder)
                )
                .addAggregation(sumBuilder)
                //不需要数据，最少查询一条
                .withPageable(PageRequest.of(0, 1))
                .build();
        SearchHits<Object> search = elasticsearchRestTemplate.search(turnoverQuery, Object.class, orderIndexCoordinates);
        Aggregations aggregations = search.getAggregations();
        double turnover = ((ParsedSum) aggregations.get("turnover")).getValue();
        //查询交易笔数
        NativeSearchQuery orderCountQuery = new NativeSearchQueryBuilder()
                //查询条件
                .withQuery(QueryBuilders.boolQuery()
                        .must(merchantIdQuery)
                        .must(statusBuilder)
                        .must(rangeQueryBuilder))
                .build();
        long orderCount = elasticsearchRestTemplate.count(orderCountQuery, orderIndexCoordinates);
        //查询扫码次数
        NativeSearchQuery scanCountQuery = new NativeSearchQueryBuilder()
                //查询条件
                .withQuery(QueryBuilders.boolQuery()
                        .must(merchantIdQuery)
                        .must(rangeQueryBuilder))
                .build();
        long scanCount = elasticsearchRestTemplate.count(scanCountQuery, scanIndexCoordinates);
        CountStatisticsVo statisticsVo = CountStatisticsVo.builder()
                .turnover(turnover)
                .turnoverChange(100d)
                .transaction(orderCount)
                .transactionChange(100L)
                .scan(scanCount)
                .scanChange(100L)
                .build();
        return statisticsVo;
    }

    @Override
    public List<PayTypeStatisticsVo> type(StatisticsQuery query) {
        Long merchantId = 1000000000000002L;
        IndexCoordinates orderIndexCoordinates = IndexCoordinates.of(ORDER_INDEX);
        String type = query.getType();
        Date date = new Date();
        long startTime = DateUtils.getDayBeginTimestamp(query.getDate());
        long endTime = DateUtils.getDayEndTimestamp(query.getDate());
        switch (type) {
            case "day":
                break;
            case "week":
                startTime = DateUtils.getWeekBeginTimestamp(date);
                endTime = DateUtils.getCurrentTimestamp();
                break;
            case "month":
                startTime = DateUtils.getMonthBeginTimestamp(date);
                endTime = DateUtils.getCurrentTimestamp();
                break;
            case "year":
                startTime = DateUtils.getYearBeginTimestamp(date);
                endTime = DateUtils.getCurrentTimestamp();
                break;
        }
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("createTime").from(startTime).to(endTime);
        TermQueryBuilder merchantIdQuery = QueryBuilders.termQuery("merchantId", merchantId);
        TermQueryBuilder statusBuilder = QueryBuilders.termQuery("status", 2);
        RangeAggregationBuilder payTypeBuilder = AggregationBuilders.range("payType")
                .addRange(0, 1)
                .addRange(1, 2)
                .addUnboundedFrom(3)
                .field("type");
        //查询交易金额
        NativeSearchQuery turnoverQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.boolQuery()
                        .must(merchantIdQuery)
                        .must(statusBuilder)
                        .must(rangeQueryBuilder)
                )
                .addAggregation(payTypeBuilder)
                //不需要数据，最少查询一条
                .withPageable(PageRequest.of(0, 1))
                .build();
        log.info("ES查询支付类型统计数据：{}", turnoverQuery.getQuery());
        log.info("ES查询支付类型统计数据：{}", turnoverQuery.getAggregations());
        SearchHits<Object> search = elasticsearchRestTemplate.search(turnoverQuery, Object.class, orderIndexCoordinates);
        Aggregations aggregations = search.getAggregations();
        Iterator<? extends Range.Bucket> iterator = ((ParsedRange) aggregations.get("payType")).getBuckets().iterator();
        List<PayTypeStatisticsVo> payTypeStatisticsVos = new LinkedList<>();
        String[] titles = {"微信", "支付宝", "其他支付"};
        int i = 0;
        while (iterator.hasNext()) {
            Range.Bucket next = iterator.next();
            long docCount = next.getDocCount();
            PayTypeStatisticsVo payTypeStatisticsVo = new PayTypeStatisticsVo(titles[i++], docCount);
            payTypeStatisticsVos.add(payTypeStatisticsVo);
        }
        return payTypeStatisticsVos;
    }

    @Override
    public ChartStatisticVo money(StatisticsQuery query) {
        Long merchantId = 1000000000000002L;
        IndexCoordinates orderIndexCoordinates = IndexCoordinates.of(ORDER_INDEX);
        String type = query.getType();
        Date date = new Date();
        long startTime = DateUtils.getDayBeginTimestamp(query.getDate());
        long endTime = DateUtils.getDayEndTimestamp(query.getDate());
        DateHistogramInterval interval = DateHistogramInterval.HOUR;
        switch (type) {
            case "day":
                break;
            case "week":
                startTime = DateUtils.getWeekBeginTimestamp(date);
                endTime = DateUtils.getCurrentTimestamp();
                interval = DateHistogramInterval.DAY;
                break;
            case "month":
                startTime = DateUtils.getMonthBeginTimestamp(date);
                endTime = DateUtils.getCurrentTimestamp();
                interval = DateHistogramInterval.DAY;
                break;
            case "year":
                startTime = DateUtils.getYearBeginTimestamp(date);
                endTime = DateUtils.getCurrentTimestamp();
                interval = DateHistogramInterval.MONTH;
                break;
        }
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("createTime").from(startTime).to(endTime);
        TermQueryBuilder merchantIdQuery = QueryBuilders.termQuery("merchantId", merchantId);
        TermQueryBuilder statusBuilder = QueryBuilders.termQuery("status", 2);
        DateHistogramAggregationBuilder dateRa = AggregationBuilders.dateHistogram("dateRa")
                .calendarInterval(interval)
                //北京时间GMT+8
                .extendedBounds(new ExtendedBounds(startTime + 28800000L, endTime + 28800000L))
                .minDocCount(0L)
//                .field("createTime");
                .field("price");
        //查询交易金额
        NativeSearchQuery turnoverQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.boolQuery()
                        .must(merchantIdQuery)
                        .must(statusBuilder)
                        .must(rangeQueryBuilder)
                )
                .addAggregation(dateRa)
                //不需要数据，最少查询一条
                .withPageable(PageRequest.of(0, 1))
                .build();
        log.info("ES查询支付类型统计数据：{}", turnoverQuery.getQuery());
        log.info("ES查询支付类型统计数据：{}", turnoverQuery.getAggregations());
        SearchHits<Object> search = elasticsearchRestTemplate.search(turnoverQuery, Object.class, orderIndexCoordinates);
        Aggregations aggregations = search.getAggregations();
        Iterator<? extends Histogram.Bucket> iterator = ((ParsedDateHistogram) aggregations.get("dateRa")).getBuckets().iterator();
        List<String> xAxis = new LinkedList<>();
        List<Object> yAxis = new LinkedList<>();
        while (iterator.hasNext()) {
            Histogram.Bucket next = iterator.next();
            ZonedDateTime key = (ZonedDateTime) next.getKey();
            switch (type) {
                case "day": xAxis.add(key.getHour()+"时");break;
                case "week": xAxis.add("周"+key.getDayOfWeek().getValue());break;
                case "month": xAxis.add(key.getDayOfMonth() + "日");break;
                case "year": xAxis.add(key.getMonthValue() + "月");break;
            }
//            double turnover = ((ParsedSum) next.getAggregations().get("turnover")).getValue();
            long docCount = next.getDocCount();
            yAxis.add(docCount);
        }
        return new ChartStatisticVo(xAxis, yAxis);
    }

    @Override
    public ChartStatisticVo frequency(StatisticsQuery query) {
        Long merchantId = 1000000000000002L;
        IndexCoordinates orderIndexCoordinates = IndexCoordinates.of(ORDER_INDEX);
        String type = query.getType();
        Date date = new Date();
        long startTime = DateUtils.getDayBeginTimestamp(query.getDate());
        long endTime = DateUtils.getDayEndTimestamp(query.getDate());
        DateHistogramInterval interval = DateHistogramInterval.HOUR;
        switch (type) {
            case "day":
                break;
            case "week":
                startTime = DateUtils.getWeekBeginTimestamp(date);
                endTime = DateUtils.getCurrentTimestamp();
                interval = DateHistogramInterval.DAY;
                break;
            case "month":
                startTime = DateUtils.getMonthBeginTimestamp(date);
                endTime = DateUtils.getCurrentTimestamp();
                interval = DateHistogramInterval.DAY;
                break;
            case "year":
                startTime = DateUtils.getYearBeginTimestamp(date);
                endTime = DateUtils.getCurrentTimestamp();
                interval = DateHistogramInterval.MONTH;
                break;
        }
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("createTime").from(startTime).to(endTime);
        TermQueryBuilder merchantIdQuery = QueryBuilders.termQuery("merchantId", merchantId);
        TermQueryBuilder statusBuilder = QueryBuilders.termQuery("status", 2);
        DateHistogramAggregationBuilder dateRa = AggregationBuilders.dateHistogram("dateRa")
                .calendarInterval(interval)
                //北京时间GMT+8
                .extendedBounds(new ExtendedBounds(startTime + 28800000L, endTime + 28800000L))
                .minDocCount(0L)
                .field("createTime");
        //查询交易金额
        NativeSearchQuery turnoverQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.boolQuery()
                        .must(merchantIdQuery)
                        .must(statusBuilder)
                        .must(rangeQueryBuilder)
                )
                .addAggregation(dateRa)
                //不需要数据，最少查询一条
                .withPageable(PageRequest.of(0, 1))
                .build();
        log.info("ES查询支付类型统计数据：{}", turnoverQuery.getQuery());
        log.info("ES查询支付类型统计数据：{}", turnoverQuery.getAggregations());
        SearchHits<Object> search = elasticsearchRestTemplate.search(turnoverQuery, Object.class, orderIndexCoordinates);
        Aggregations aggregations = search.getAggregations();
        Iterator<? extends Histogram.Bucket> iterator = ((ParsedDateHistogram) aggregations.get("dateRa")).getBuckets().iterator();
        List<String> xAxis = new LinkedList<>();
        List<Object> yAxis = new LinkedList<>();
        while (iterator.hasNext()) {
            Histogram.Bucket next = iterator.next();
            ZonedDateTime key = (ZonedDateTime) next.getKey();
            switch (type) {
                case "day": xAxis.add(key.getHour()+"时");break;
                case "week": xAxis.add("周"+key.getDayOfWeek().getValue());break;
                case "month": xAxis.add(key.getDayOfMonth() + "日");break;
                case "year": xAxis.add(key.getMonthValue() + "月");break;
            }
            long docCount = next.getDocCount();
            yAxis.add(docCount);
        }
        return new ChartStatisticVo(xAxis, yAxis);
    }
}
