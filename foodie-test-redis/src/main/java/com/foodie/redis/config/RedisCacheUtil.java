package com.foodie.redis.config;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisCacheUtil {

    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 指定缓存失效时间 - common
     *
     * @param key      主KEY
     * @param childKey 子KEY
     * @param timeout  缓存时间：秒
     * @return true 成功 false 失败
     */
    public Boolean expire(String key, String childKey, long timeout) {
        return redisTemplate.expire(getFinallyKey(key, childKey), timeout, TimeUnit.SECONDS);
    }

    /**
     * 根据key 获取过期时间 - common
     *
     * @param key      主KEY
     * @param childKey 子KEY
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key, String childKey) {
        return redisTemplate.getExpire(getFinallyKey(key, childKey), TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在 - common
     *
     * @param key      主KEY
     * @param childKey 子KEY
     * @return true 存在 false不存在
     */
    public Boolean hasKey(String key, String childKey) {
        return redisTemplate.hasKey(getFinallyKey(key, childKey));
    }

    /**
     * 添加数据对象到缓存 - String
     *
     * @param key      主KEY
     * @param childKey 子KEY
     * @param value    数据对象
     */
    public void put(String key, String childKey, Object value) {
        redisTemplate.boundValueOps(getFinallyKey(key, childKey)).set(value);
    }

    /**
     * 添加数据对象到缓存 - String
     *
     * @param key      主KEY
     * @param childKey 子KEY
     * @param value    数据对象
     * @param timeout  缓存时间：秒
     */
    public void put(String key, String childKey, Object value, long timeout) {
        putForExpire(key, childKey, value, timeout);
    }

    /**
     * 添加数据对象到缓存 - String
     *
     * @param key      主KEY
     * @param childKey 子KEY
     * @param value    数据对象
     * @param timeout  缓存时间：秒
     */
    public void putForExpire(String key, String childKey, Object value, long timeout) {
        redisTemplate.boundValueOps(getFinallyKey(key, childKey)).set(value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 从缓存中获取数据对象 - String
     *
     * @param key      主KEY
     * @param childKey 子KEY
     * @return 数据对象
     */
    public Object get(String key, String childKey) {
        return getForExpire(key, childKey);
    }

    /**
     * 从缓存中移除数据对象 - String
     *
     * @param key      主KEY
     * @param childKey 子KEY
     */
    public void remove(String key, String childKey) {
        removeForExpire(key, childKey);
    }

    /**
     * 删除通过 putForExpire方法添加的缓存值 - String
     *
     * @param key      主KEY
     * @param childKey 子KEY
     */
    public void removeForExpire(String key, String childKey) {
        redisTemplate.delete(getFinallyKey(key, childKey));
    }

    /**
     * 取通过 putForExpire方法添加的缓存值 - String
     *
     * @param key      主KEY
     * @param childKey 子KEY
     */
    public Object getForExpire(String key, String childKey) {
        return redisTemplate.boundValueOps(getFinallyKey(key, childKey)).get();
    }

    /**
     * 递增 - String
     *
     * @param key      主KEY
     * @param childKey 子KEY
     * @param delta    要增加几(大于0)
     * @return         结果
     */
    public long incr(String key, String childKey, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.boundValueOps(getFinallyKey(key, childKey)).increment(delta);
    }

    /**
     * 递减 - String
     *
     * @param key      主KEY
     * @param childKey 子KEY
     * @param delta    要增加几(大于0)
     * @return         结果
     */
    public long decr(String key, String childKey, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.boundValueOps(getFinallyKey(key, childKey)).increment(-delta);
    }

    /**
     * HashGet - String
     *
     * @param key      主KEY
     * @param childKey 子KEY
     * @param item     子项
     * @return         结果
     */
    public Object hget(String key, String childKey, String item) {
        return redisTemplate.boundHashOps(getFinallyKey(key, childKey)).get(item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key      主KEY
     * @param childKey 子KEY
     * @return         对应的多个键值
     */
    public Map<Object, Object> hget(String key, String childKey) {
        return redisTemplate.boundHashOps(getFinallyKey(key, childKey)).entries();
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key      主KEY
     * @param childKey 子KEY
     * @param map      对应多个键值
     */
    public void hmset(String key, String childKey, Map<String, Object> map) {
        redisTemplate.boundHashOps(getFinallyKey(key, childKey)).putAll(map);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key      主KEY
     * @param childKey 子KEY
     * @param map      对应多个键值
     */
    public void hmset(String key, String childKey, Map<String, Object> map,long timeout) {
        redisTemplate.boundHashOps(getFinallyKey(key, childKey)).putAll(map);
        expire(key, childKey,timeout);
    }

    /**
     * HashGet - String
     *
     * @param key      主KEY
     * @param childKey 子KEY
     * @param item     子项
     * @param value    值
     */
    public void hget(String key, String childKey, String item, Object value) {
        redisTemplate.boundHashOps(getFinallyKey(key, childKey)).put(item,value);
    }

    /**
     * HashGet - String
     *
     * @param key      主KEY
     * @param childKey 子KEY
     * @param item     子项
     * @param timeout  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @param value    值
     */
    public void hget(String key, String childKey, String item, Object value, long timeout) {
        redisTemplate.boundHashOps(getFinallyKey(key, childKey)).put(item,value);
        expire(key,childKey,timeout);
    }

    /**
     * 删除hash表中的值
     *
     * @param key      主KEY
     * @param childKey 子KEY
     * @param items    子项
     */
    public void hdel(String key, String childKey, String... items) {
        redisTemplate.boundHashOps(getFinallyKey(key, childKey)).delete(items);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key      主KEY
     * @param childKey 子KEY
     * @param item     子项
     * @return         true 存在 false不存在
     */
    public void hHasKey(String key, String childKey, String item) {
        redisTemplate.boundHashOps(getFinallyKey(key, childKey)).hasKey(item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key      主KEY
     * @param childKey 子KEY
     * @param item     子项
     * @param by       要增加几(大于0)
     * @return         新值
     */
    public Double hincr(String key, String childKey, String item, double by) {
        return redisTemplate.boundHashOps(getFinallyKey(key, childKey)).increment(item,by);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key      主KEY
     * @param childKey 子KEY
     * @param item     子项
     * @param by       要增加几(大于0)
     * @return         新值
     */
    public Long hincr(String key, String childKey, String item, long by) {
        return redisTemplate.boundHashOps(getFinallyKey(key, childKey)).increment(item,by);
    }

    /**
     * hash递减 如果不存在,就会创建一个 并把减少后的值返回
     *
     * @param key      主KEY
     * @param childKey 子KEY
     * @param item     子项
     * @param by       减少加几(大于0)
     * @return         新值
     */
    public Double hdecr(String key, String childKey, String item, double by) {
        return redisTemplate.boundHashOps(getFinallyKey(key, childKey)).increment(item,-by);
    }

    /**
     * hash递减 如果不存在,就会创建一个 并把减少后的值返回
     *
     * @param key      主KEY
     * @param childKey 子KEY
     * @param item     子项
     * @param by       减少加几(大于0)
     * @return         新值
     */
    public Long hdecr(String key, String childKey, String item, long by) {
        return redisTemplate.boundHashOps(getFinallyKey(key, childKey)).increment(item,-by);
    }

    /**
     * 根据key获取Set中的所有值
     *
     * @param key      主KEY
     * @param childKey 子KEY
     * @return         结果
     */
    public Set<Object> sGet(String key,String childKey) {
        return redisTemplate.boundSetOps(getFinallyKey(key, childKey)).members();
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key      主KEY
     * @param childKey 子KEY
     * @param value    值
     * @return         true 存在 false不存在
     */
    public Boolean sHasKey(String key, String childKey, Object value) {
        return redisTemplate.boundSetOps(getFinallyKey(key, childKey)).isMember(value);
    }

    /**
     * 将数据放入set缓存
     *
     * @param key      主KEY
     * @param childKey 子KEY
     * @param values   值可以是多个
     * @return         成功个数
     */
    public Long sSet(String key, String childKey, Object... values) {
        return redisTemplate.boundSetOps(getFinallyKey(key, childKey)).add(values);
    }

    /**
     * 将数据放入set缓存
     *
     * @param key      主KEY
     * @param childKey 子KEY
     * @param timeout  缓存时间
     * @param values   值可以是多个
     * @return         成功个数
     */
    public Long sSet(String key, String childKey, long timeout, Object... values) {
        Long count = redisTemplate.boundSetOps(getFinallyKey(key, childKey)).add(values);
        expire(key, childKey,timeout);
        return count;
    }

    /**
     * 获取set缓存的长度
     *
     * @param key      主KEY
     * @param childKey 子KEY
     * @return         数量
     */
    public Long sGetSetSize(String key, String childKey) {
        return redisTemplate.boundSetOps(getFinallyKey(key, childKey)).size();
    }

    /**
     * 移除值为value的
     *
     * @param key      主KEY
     * @param childKey 子KEY
     * @param values   值 可以是多个
     * @return         移除的个数
     */
    public long setRemove(String key, String childKey, Object... values) {
        return redisTemplate.boundSetOps(getFinallyKey(key, childKey)).remove(values);
    }

    /**
     * 获取list缓存的内容
     *
     * @param key      主KEY
     * @param childKey 子KEY
     * @param start    开始
     * @param end      结束 0 到 -1代表所有值
     * @return         数据
     */
    public List<Object> lGet(String key, String childKey, long start, long end) {
        return redisTemplate.boundListOps(getFinallyKey(key, childKey)).range(start,end);
    }

    /**
     * 获取list缓存的内容
     *
     * @param key      主KEY
     * @param childKey 子KEY
     * @return         数据个数
     */
    public Long lGetListSize(String key, String childKey) {
        return redisTemplate.boundListOps(getFinallyKey(key, childKey)).size();
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key      主KEY
     * @param childKey 子KEY
     * @param index    索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return         数据
     */
    public Object lGetIndex(String key, String childKey, long index) {
        return redisTemplate.boundListOps(getFinallyKey(key, childKey)).index(index);
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key      主KEY
     * @param childKey 子KEY
     * @param value    数据
     */
    public void lSet(String key, String childKey, Object value) {
        redisTemplate.boundListOps(getFinallyKey(key, childKey)).rightPush(value);
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key      主KEY
     * @param childKey 子KEY
     * @param value    数据
     */
    public void lSet(String key, String childKey, Object value,long timeout) {
        redisTemplate.boundListOps(getFinallyKey(key, childKey)).rightPush(value);
        expire(key, childKey,timeout);
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key      主KEY
     * @param childKey 子KEY
     * @param values   数据
     */
    public void lSet(String key, String childKey, List<Object> values) {
        redisTemplate.boundListOps(getFinallyKey(key, childKey)).rightPushAll(values);
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key      主KEY
     * @param childKey 子KEY
     * @param values   数据
     */
    public void lSet(String key, String childKey, List<Object> values,long timeout) {
        redisTemplate.boundListOps(getFinallyKey(key, childKey)).rightPushAll(values);
        expire(key, childKey,timeout);
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key      主KEY
     * @param childKey 子KEY
     * @param index    索引
     * @param value    数据
     */
    public void lUpdateIndex(String key, String childKey, long index, Object value) {
        redisTemplate.boundListOps(getFinallyKey(key, childKey)).set(index,value);
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key      主KEY
     * @param childKey 子KEY
     * @param count    移除多少个
     * @param value    数据
     * @return         移除的个数
     */
    public Long lRemove(String key, String childKey, long count, Object value) {
        return redisTemplate.boundListOps(getFinallyKey(key, childKey)).remove(count,value);
    }

    private String getFinallyKey(String key, String childKey) {
        return key + childKey;
    }
}
