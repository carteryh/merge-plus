package com.mergeplus.request;

import com.mergeplus.annonation.HttpMerge;
import com.mergeplus.constant.Constants;
import com.mergeplus.entity.FieldInfo;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;


/**
 * 项目名称：merge-plus
 * 类 名 称：AbstractRequest
 * 类 描 述：TODO
 * 创建时间：2020/11/5 1:33 下午
 * 创 建 人：chenyouhong
 */
@Component
public class PostMergeRequest extends AbstractRequest {

    /**
     *
     */
    @PostConstruct
    public void init() {
        this.requestMap.put(this.getType(), new PostMergeRequest());
    }

    /**
     *
     * @return 返回
     */
    @Override
    public HttpMethod getType() {
        return HttpMethod.POST;
    }

    /**
     *
     * @param fieldInfo 参数
     * @return 返回
     */
    @Override
    public String parseRquest(FieldInfo fieldInfo) {
        HttpMerge httpMerge = fieldInfo.getClientBeanClazz().getAnnotation(HttpMerge.class);
        Assert.notNull(httpMerge,"httpMerge don't is null!");
        Assert.notNull(httpMerge.url(),"httpMerge url don't is null!");

        String url = Constants.BLANK;
        PostMapping mapping = fieldInfo.getMethod().getAnnotation(PostMapping.class);
        String[] value = mapping.value();
        if (value != null && value.length > 0) {
            url += value[0];
        }
        return url;
    }
}
