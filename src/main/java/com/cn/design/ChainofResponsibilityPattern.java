package com.cn.design;

import java.util.ArrayList;
import java.util.List;

/**
 * 责任链模式
 */
public class ChainofResponsibilityPattern {

    /**
     * 封装请求的类Request
     */
    public static class Request{
        String requestStr;

        public String getRequestStr() {
            return requestStr;
        }

        public void setRequestStr(String requestStr) {
            this.requestStr = requestStr;
        }
    }


    /**
     * 封装响应信息的类Response
     */
    public static class Response{
        String responseStr;

        public String getResponseStr() {
            return responseStr;
        }

        public void setResponseStr(String responseStr) {
            this.responseStr = responseStr;
        }
    }

    public interface Filter{
        void doFilter(Request request,Response response,FilterChain filterChain);
    }

    /**
     * 定义责任链FilterChain
     */
    public static class FilterChain implements Filter{
        /**
         * 用list集合存储过滤集合
         */
        List<Filter> filters = new ArrayList<>();

        /**
         * 用于标记规则引用顺序
         */
        int index = 0;

        /**
         * 往责任链条中添加规则
         * @param filter
         * @return
         */
        public FilterChain addFilter(Filter filter){
            filters.add(filter);
            return this;
        }

        public void doFilter(Request request, Response response,FilterChain filterChain) {
            if(filters.size() == index){
                return;
            }
            Filter filter = filters.get(index);
            index++;
            filter.doFilter(request,response,filterChain);
        }
    }

    public static class HTMLFilter implements Filter{

        @Override
        public void doFilter(Request request, Response response, FilterChain filterChain) {
            //将字符串中出现的"<>"符号替换成"[]"
            request.requestStr = request.requestStr
                    .replace('<', '[').replace('>', ']') +
                    //后面添加的是便于我们观察代码执行步骤的字符串
                    "----HTMLFilter()";
            filterChain.doFilter(request, response, filterChain);
            response.responseStr += "---HTMLFilter()";
        }
    }

    public static class SensitiveFilter implements Filter {

        public void doFilter(Request request, Response response, FilterChain chain) {
            //处理字符串中的敏感信息，将被就业和谐成就业
            request.requestStr = request.requestStr
                    .replace("被就业", "就业").replace("敏感", "") +
                    //后面添加的是便于我们观察代码执行步骤的字符串
                    " ---sensitiveFilter()";
            chain.doFilter(request, response, chain);
            response.responseStr += "---sensitiveFilter()";
        }

    }

    public static class FaceFilter implements Filter {

        public void doFilter(Request request, Response response, FilterChain chain) {

            //将字符串中出现的":):"转换成"^V^";
            request.requestStr = request.requestStr.replace(":):", "^V^")
                    //后面添加的是便于我们观察代码执行步骤的字符串
                    + "----FaceFilter()";
            chain.doFilter(request, response, chain);
            response.responseStr += "---FaceFilter()";
        }

    }


    public static void main(String args[]) {
                 //设定过滤规则，对msg字符串进行过滤处理
                 String msg = ":):,<script>,敏感,被就业,网络授课";
                 //过滤请求
                 Request request=new Request();
                 //set方法，将待处理字符串传递进去
                 request.setRequestStr(msg);
                 //处理过程结束，给出的响应
                 Response response=new Response();
                 //设置响应信息
                 response.setResponseStr("response:");
                 //FilterChain,过滤规则形成的拦截链条
                 FilterChain fc=new FilterChain();
                 //规则链条添加过滤规则，采用的是链式调用
                 fc.addFilter(new HTMLFilter())
                 .addFilter(new SensitiveFilter())
                 .addFilter(new FaceFilter());
                 //按照FilterChain的规则顺序，依次应用过滤规则
                 fc.doFilter(request, response,fc);
                 //打印请求信息
                 System.out.println(request.getRequestStr());
                 //打印响应信息
                 System.out.println(response.getResponseStr());
                 /*
          * 处理器对数据进行处理
          * --|----|---数据--|-----|---
          * 规则1      规则2                 规则3       规则4
          */
             }
}
