package com.wkb.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import com.wkb.coupon.constant.Constant;
import com.wkb.coupon.constant.CouponStatus;
import com.wkb.coupon.dao.CouponDao;
import com.wkb.coupon.entity.Coupon;
import com.wkb.coupon.exception.ServiceException;
import com.wkb.coupon.feign.SettlementClient;
import com.wkb.coupon.feign.TemplateClient;
import com.wkb.coupon.service.IRedisService;
import com.wkb.coupon.service.IUserService;
import com.wkb.coupon.utils.CouponClassify;
import com.wkb.coupon.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户优惠券service实现
 * 思路: 优惠券操作先保存到Redis 再通过kafka到mysql持久化
 * @author WuKB
 * @Date 2020/5/24
 */
@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    private final CouponDao couponDao;

    private final IRedisService redisService;

    private final TemplateClient templateClient;

    private final SettlementClient settlementClient;

    /** kafka客户端*/
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public UserServiceImpl(CouponDao couponDao, IRedisService redisService,
                           TemplateClient templateClient, SettlementClient settlementClient,
                           KafkaTemplate<String, String> kafkaTemplate) {
        this.couponDao = couponDao;
        this.redisService = redisService;
        this.templateClient = templateClient;
        this.settlementClient = settlementClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * 根据用户id、优惠券状态(可用、不可用、过期)查找优惠券
     * @param userId 用户 id
     * @param status 优惠券状态
     * @return
     * @throws ServiceException
     */
    @Override
    public List<Coupon> findCouponsByStatus(Long userId, Integer status) throws ServiceException {
        List<Coupon> curCached = redisService.getCachedCoupons(userId, status);
        List<Coupon> preTarget;

        //判断Redis缓存中是否存在数据 有则取缓存数据
        if (CollectionUtils.isNotEmpty(curCached)) {
            log.debug("Redis中存在缓存数据: {}, {}", userId, status);
            preTarget = curCached;
        } else {
            log.debug("Redis缓存数据为空,查找mysql: {}, {}",
                    userId, status);
            List<Coupon> dbCoupons = couponDao.findAllByUserIdAndStatus(
                    userId, CouponStatus.getByCode(status)
            );
            // 如果数据库中没有记录, 直接返回就可以, Redis 中已经加入了一张无效的优惠券
            if (CollectionUtils.isEmpty(dbCoupons)) {
                log.debug("当前用户没有相对应的优惠券: {}, {}", userId, status);
                return dbCoupons;
            }

            // 填充 dbCoupons的 templateSDK 字段
            Map<Integer, CouponTemplateVo> id2TemplateSDK =
                    templateClient.findIds2TemplateSDK(
                            dbCoupons.stream()
                                    .map(Coupon::getTemplateId)
                                    .collect(Collectors.toList())
                    ).getData();
            dbCoupons.forEach(
                    dc -> dc.setTemplateVo(
                            id2TemplateSDK.get(dc.getTemplateId())
                    )
            );
            // 数据库中存在记录
            preTarget = dbCoupons;
            // 将记录写入 Cache
            redisService.addCouponToCache(userId, preTarget, status);
        }

        // 将无效优惠券剔除
        preTarget = preTarget.stream()
                .filter(c -> c.getId() != -1)
                .collect(Collectors.toList());

        // 如果当前获取的是可用优惠券, 还需要做对已过期优惠券的延迟处理
        if (CouponStatus.getByCode(status) == CouponStatus.USABLE) {
            CouponClassify classify = CouponClassify.classify(preTarget);
            // 如果已过期状态不为空, 需要做延迟处理
            if (CollectionUtils.isNotEmpty(classify.getExpired())) {
                log.info("添加过期优惠券到Redis中, Method:[FindCouponsByStatus]: " +
                        "{}, {}", userId, status);
                redisService.addCouponToCache(
                        userId, classify.getExpired(),
                        CouponStatus.EXPIRED.getCode()
                );
                // 发送到 kafka 中做异步处理
                kafkaTemplate.send(
                        Constant.TOPIC,
                        JSON.toJSONString(new CouponKafkaMessage(
                                CouponStatus.EXPIRED.getCode(),
                                classify.getExpired().stream()
                                        .map(Coupon::getId)
                                        .collect(Collectors.toList())
                        ))
                );
            }

            return classify.getUsable();
        }

        return preTarget;
    }

    /**
     * 根据用户id 查找可领取得优惠券
     * @param userId 用户 id
     * @return
     * @throws ServiceException
     */
    @Override
    public List<CouponTemplateVo> findAvailableTemplate(Long userId) throws ServiceException {

        long curTime = new Date().getTime();
        List<CouponTemplateVo> templateSDKS =
                templateClient.findAllUsableTemplate().getData();

        log.debug("获取到所有可用的优惠券模板(TemplateClient微服务) Count: {}",
                templateSDKS.size());

        // 过滤过期的优惠券模板
        templateSDKS = templateSDKS.stream().filter(
                t -> t.getRule().getExpiration().getDeadline() > curTime
        ).collect(Collectors.toList());

        log.info("Find Usable Template Count: {}", templateSDKS.size());

        // key 是 TemplateId
        // value 中的 left 是 Template limitation, right 是优惠券模板
        Map<Integer, Pair<Integer, CouponTemplateVo>> limit2Template =
                new HashMap<>(templateSDKS.size());
        templateSDKS.forEach(
                t -> limit2Template.put(
                        t.getId(),
                        Pair.of(t.getRule().getLimitation(), t)
                )
        );

        List<CouponTemplateVo> result =
                new ArrayList<>(limit2Template.size());
        List<Coupon> userUsableCoupons = findCouponsByStatus(
                userId, CouponStatus.USABLE.getCode()
        );

        log.debug("当前用户拥有可用优惠券模板: {}, {}", userId,
                userUsableCoupons.size());

        // key 是 TemplateId
        Map<Integer, List<Coupon>> templateId2Coupons = userUsableCoupons
                .stream()
                .collect(Collectors.groupingBy(Coupon::getTemplateId));

        // 根据 Template 的 Rule 判断是否可以领取优惠券模板
        limit2Template.forEach((k, v) -> {

            int limitation = v.getLeft();
            CouponTemplateVo couponTemplateVo = v.getRight();

            if (templateId2Coupons.containsKey(k)
                    && templateId2Coupons.get(k).size() >= limitation) {
                return;
            }

            result.add(couponTemplateVo);

        });

        return result;
    }

    /**
     * 用户领取优惠券
     * @param request {@link AcquireTemplateRequest}
     * @return
     * @throws ServiceException
     */
    @Override
    public Coupon acquireTemplate(AcquireTemplateRequest request) throws ServiceException {

        Map<Integer, CouponTemplateVo> id2Template =
                templateClient.findIds2TemplateSDK(
                        Collections.singletonList(
                                request.getTemplateVo().getId()
                        )
                ).getData();

        // 如果优惠券模板不存在
        if (id2Template.size() <= 0) {
            log.error("不存在该优惠券! {}",
                    request.getTemplateVo().getId());
            throw new ServiceException("未找到优惠券!");
        }

        // 用户是否可以领取这张优惠券
        List<Coupon> userUsableCoupons = findCouponsByStatus(
                request.getUserId(), CouponStatus.USABLE.getCode()
        );
        Map<Integer, List<Coupon>> templateId2Coupons = userUsableCoupons
                .stream()
                .collect(Collectors.groupingBy(Coupon::getTemplateId));

        if (templateId2Coupons.containsKey(request.getTemplateVo().getId())
                && templateId2Coupons.get(request.getTemplateVo().getId()).size() >=
                request.getTemplateVo().getRule().getLimitation()) {
            log.error("领取优惠券次数超过限制!: {}",
                    request.getTemplateVo().getId());
            throw new ServiceException("领取优惠券次数超过限制!");
        }

        // 尝试去获取优惠券码
        String couponCode = redisService.tryToAcquireCouponCodeFromCache(
                request.getTemplateVo().getId()
        );
        if (StringUtils.isEmpty(couponCode)) {
            log.error("优惠券已经被抢光了!: {}",
                    request.getTemplateVo().getId());
            throw new ServiceException("优惠券已经被抢光了!");
        }

        Coupon newCoupon = new Coupon(
                request.getTemplateVo().getId(), request.getUserId(),
                couponCode, CouponStatus.USABLE
        );
        newCoupon = couponDao.save(newCoupon);

        // 填充 Coupon 对象的 CouponTemplateSDK, 一定要在放入缓存之前去填充
        newCoupon.setTemplateVo(request.getTemplateVo());

        // 放入缓存中
        redisService.addCouponToCache(
                request.getUserId(),
                Collections.singletonList(newCoupon),
                CouponStatus.USABLE.getCode()
        );

        return newCoupon;
    }

    /**
     * 结算（核销）优惠券
     * @param info {@link SettlementInfo}
     * @return
     * @throws ServiceException
     */
    @Override
    public SettlementInfo settlement(SettlementInfo info) throws ServiceException {

        // 当没有传递优惠券时, 直接返回商品总价
        List<SettlementInfo.CouponAndTemplateInfo> ctInfos =
                info.getCouponAndTemplateInfos();
        if (CollectionUtils.isEmpty(ctInfos)) {

            log.info("不使用优惠券");
            double goodsSum = 0.0;

            for (GoodsInfo gi : info.getGoodsInfos()) {
                goodsSum += gi.getPrice() + gi.getCount();
            }

            // 没有优惠券也就不存在优惠券的核销, SettlementInfo 其他的字段不需要修改
            info.setCost(retain2Decimals(goodsSum));
        }

        // 获取当前用户可用的优惠券
        List<Coupon> coupons = findCouponsByStatus(
                info.getUserId(), CouponStatus.USABLE.getCode()
        );
        Map<Integer, Coupon> id2Coupon = coupons.stream()
                .collect(Collectors.toMap(
                        Coupon::getId,
                        Function.identity()
                ));

        /**
         * 1、判断用户可用的优惠券是否为空
         * 2、选用的优惠券是否在可用的优惠券之中
         */
        if (MapUtils.isEmpty(id2Coupon) || !CollectionUtils.isSubCollection(
                ctInfos.stream().map(SettlementInfo.CouponAndTemplateInfo::getId)
                        .collect(Collectors.toList()), id2Coupon.keySet()
        )) {
            log.info("可用的优惠券 : {}", id2Coupon.keySet());
            log.info("选用的优惠券 : {}", ctInfos.stream()
                    .map(SettlementInfo.CouponAndTemplateInfo::getId)
                    .collect(Collectors.toList()));
            log.error("优惠券不可用!");
            throw new ServiceException("存在不可用的优惠券！请重试！");
        }

        log.debug("优惠券验证通过: {}", ctInfos.size());

        List<Coupon> settleCoupons = new ArrayList<>(ctInfos.size());
        ctInfos.forEach(ci -> settleCoupons.add(id2Coupon.get(ci.getId())));

        // 通过结算服务获取结算信息
        SettlementInfo processedInfo =
                settlementClient.computeRule(info).getData();
        if (processedInfo.getEmploy() && CollectionUtils.isNotEmpty(
                processedInfo.getCouponAndTemplateInfos()
        )) {
            log.info("Settle User Coupon: {}, {}", info.getUserId(),
                    JSON.toJSONString(settleCoupons));
            // 更新Redis数据
            redisService.addCouponToCache(
                    info.getUserId(),
                    settleCoupons,
                    CouponStatus.USED.getCode()
            );
            // kafka进行异步持久化
            kafkaTemplate.send(
                    Constant.TOPIC,
                    JSON.toJSONString(new CouponKafkaMessage(
                            CouponStatus.USED.getCode(),
                            settleCoupons.stream().map(Coupon::getId)
                                    .collect(Collectors.toList())
                    ))
            );
        }

        return processedInfo;
    }

    /**
     * 价格保留两位小数
     * @param value
     * @return
     */
    private double retain2Decimals(double value) {

        // BigDecimal.ROUND_HALF_UP 代表四舍五入
        return new BigDecimal(value)
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }
}
