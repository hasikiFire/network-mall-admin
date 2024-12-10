package com.hasikiFire.networkmall.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.hasikiFire.networkmall.dao.Proxy.ClashHttpProxy;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class YamlBuilder {

  public static String buildYaml(List<ClashHttpProxy> proxies) {

    Map<String, Object> yamlData = new LinkedHashMap<>();
    buildYamlHeader(yamlData);
    // 主要看这个
    buildYamlProxy(yamlData, proxies);

    buildYamlRules(yamlData);

    try {
      String yaml = toYamlWithJackson(yamlData);
      return yaml;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;

  }

  public static void buildYamlProxy(Map<String, Object> yamlData, List<ClashHttpProxy> proxies) {

    // 配置代理列表;
    yamlData.put("proxies", proxies);
    // 配置 Proxy Groups
    Map<String, Object> proxyGroup = new HashMap<>();
    proxyGroup.put("name", "auto");
    proxyGroup.put("type", "select");
    proxyGroup.put("proxies", proxies.stream().map(ClashHttpProxy::getName).toList());
    proxyGroup.put("interval", 300);
    yamlData.put("proxy-groups", List.of(proxyGroup));

  }

  public static void buildYamlHeader(Map<String, Object> yamlData) {
    yamlData.put("port", 7890);
    yamlData.put("socks-port", 7891);
    yamlData.put("redir-port", 7892);
    yamlData.put("mixed-port", 7893);
    yamlData.put("ipv6", false);
    yamlData.put("allow-lan", true);
    yamlData.put("mode", "Rule");
    yamlData.put("log-level", "info");
    yamlData.put("external-controller", "0.0.0.0:9090");
    yamlData.put("external-ui", "ui");
    yamlData.put("secret", "");

    Map<String, Object> dns = new HashMap<>();
    dns.put("enable", true);
    dns.put("ipv6", false);
    dns.put("listen", "0.0.0.0:53");
    dns.put("default-nameserver", List.of("114.114.114.114"));
    dns.put("enhanced-mode", "fake-ip");
    dns.put("fake-ip-range", "198.18.0.1/16");
    dns.put("nameserver", List.of("114.114.114.114", "223.5.5.5"));

    yamlData.put("dns", dns);

  }

  static public void buildYamlRules(Map<String, Object> yamlData) {
    // 配置 Rules
    List<String> rules = List.of(
        "DOMAIN-SUFFIX,local,DIRECT",
        "IP-CIDR,127.0.0.0/8,DIRECT",
        "DOMAIN-SUFFIX,cn,DIRECT",
        "MATCH,auto",
        "DOMAIN-SUFFIX,local,DIRECT",
        "IP-CIDR,127.0.0.0/8,DIRECT",
        "IP-CIDR,172.16.0.0/12,DIRECT",
        "IP-CIDR,192.168.0.0/16,DIRECT",
        "IP-CIDR,10.0.0.0/8,DIRECT",

        // # 国内网站
        "DOMAIN-SUFFIX,cn,DIRECT",
        "DOMAIN-KEYWORD,-cn,DIRECT",
        "DOMAIN-SUFFIX,126.com,DIRECT",
        "DOMAIN-SUFFIX,126.net,DIRECT",
        "DOMAIN-SUFFIX,127.net,DIRECT",
        "DOMAIN-SUFFIX,163.com,DIRECT",
        "DOMAIN-SUFFIX,360buyimg.com,DIRECT",
        "DOMAIN-SUFFIX,36kr.com,DIRECT",
        "DOMAIN-SUFFIX,acfun.tv,DIRECT",
        "DOMAIN-SUFFIX,air-matters.com,DIRECT",
        "DOMAIN-SUFFIX,aixifan.com,DIRECT",
        "DOMAIN-KEYWORD,alicdn,DIRECT",
        "DOMAIN-KEYWORD,alipay,DIRECT",
        "DOMAIN-KEYWORD,taobao,DIRECT",
        "DOMAIN-SUFFIX,amap.com,DIRECT",
        "DOMAIN-SUFFIX,autonavi.com,DIRECT",
        "DOMAIN-KEYWORD,baidu,DIRECT",
        "DOMAIN-SUFFIX,bdimg.com,DIRECT",
        "DOMAIN-SUFFIX,bdstatic.com,DIRECT",
        "DOMAIN-SUFFIX,bilibili.com,DIRECT",
        "DOMAIN-SUFFIX,bilivideo.com,DIRECT",
        "DOMAIN-SUFFIX,caiyunapp.com,DIRECT",
        "DOMAIN-SUFFIX,clouddn.com,DIRECT",
        "DOMAIN-SUFFIX,cnbeta.com,DIRECT",
        "DOMAIN-SUFFIX,cnbetacdn.com,DIRECT",
        "DOMAIN-SUFFIX,cootekservice.com,DIRECT",
        "DOMAIN-SUFFIX,csdn.net,DIRECT",
        "DOMAIN-SUFFIX,ctrip.com,DIRECT",
        "DOMAIN-SUFFIX,dgtle.com,DIRECT",
        "DOMAIN-SUFFIX,dianping.com,DIRECT",
        "DOMAIN-SUFFIX,douban.com,DIRECT",
        "DOMAIN-SUFFIX,doubanio.com,DIRECT",
        "DOMAIN-SUFFIX,duokan.com,DIRECT",
        "DOMAIN-SUFFIX,easou.com,DIRECT",
        "DOMAIN-SUFFIX,ele.me,DIRECT",
        "DOMAIN-SUFFIX,feng.com,DIRECT",
        "DOMAIN-SUFFIX,fir.im,DIRECT",
        "DOMAIN-SUFFIX,frdic.com,DIRECT",
        "DOMAIN-SUFFIX,g-cores.com,DIRECT",
        "DOMAIN-SUFFIX,godic.net,DIRECT",
        "DOMAIN-SUFFIX,gtimg.com,DIRECT",
        "DOMAIN,cdn.hockeyapp.net,DIRECT",
        "DOMAIN-SUFFIX,hongxiu.com,DIRECT",
        "DOMAIN-SUFFIX,hxcdn.net,DIRECT",
        "DOMAIN-SUFFIX,iciba.com,DIRECT",
        "DOMAIN-SUFFIX,ifeng.com,DIRECT",
        "DOMAIN-SUFFIX,ifengimg.com,DIRECT",
        "DOMAIN-SUFFIX,ipip.net,DIRECT",
        "DOMAIN-SUFFIX,iqiyi.com,DIRECT",
        "DOMAIN-SUFFIX,jd.com,DIRECT",
        "DOMAIN-SUFFIX,jianshu.com,DIRECT",
        "DOMAIN-SUFFIX,knewone.com,DIRECT",
        "DOMAIN-SUFFIX,le.com,DIRECT",
        "DOMAIN-SUFFIX,lecloud.com,DIRECT",
        "DOMAIN-SUFFIX,lemicp.com,DIRECT",
        "DOMAIN-SUFFIX,licdn.com,DIRECT",
        "DOMAIN-SUFFIX,luoo.net,DIRECT",
        "DOMAIN-SUFFIX,meituan.com,DIRECT",
        "DOMAIN-SUFFIX,meituan.net,DIRECT",
        "DOMAIN-SUFFIX,mi.com,DIRECT",
        "DOMAIN-SUFFIX,miaopai.com,DIRECT",
        "DOMAIN-SUFFIX,microsoft.com,DIRECT",
        "DOMAIN-SUFFIX,microsoftonline.com,DIRECT",
        "DOMAIN-SUFFIX,miui.com,DIRECT",
        "DOMAIN-SUFFIX,miwifi.com,DIRECT",
        "DOMAIN-SUFFIX,mob.com,DIRECT",
        "DOMAIN-SUFFIX,netease.com,DIRECT",
        "DOMAIN-SUFFIX,office.com,DIRECT",
        "DOMAIN-SUFFIX,office365.com,DIRECT",
        "DOMAIN-KEYWORD,officecdn,DIRECT",
        "DOMAIN-SUFFIX,oschina.net,DIRECT",
        "DOMAIN-SUFFIX,ppsimg.com,DIRECT",
        "DOMAIN-SUFFIX,pstatp.com,DIRECT",
        "DOMAIN-SUFFIX,qcloud.com,DIRECT",
        "DOMAIN-SUFFIX,qdaily.com,DIRECT",
        "DOMAIN-SUFFIX,qdmm.com,DIRECT",
        "DOMAIN-SUFFIX,qhimg.com,DIRECT",
        "DOMAIN-SUFFIX,qhres.com,DIRECT",
        "DOMAIN-SUFFIX,qidian.com,DIRECT",
        "DOMAIN-SUFFIX,qihucdn.com,DIRECT",
        "DOMAIN-SUFFIX,qiniu.com,DIRECT",
        "DOMAIN-SUFFIX,qiniucdn.com,DIRECT",
        "DOMAIN-SUFFIX,qiyipic.com,DIRECT",
        "DOMAIN-SUFFIX,qq.com,DIRECT",
        "DOMAIN-SUFFIX,qqurl.com,DIRECT",
        "DOMAIN-SUFFIX,rarbg.to,DIRECT",
        "DOMAIN-SUFFIX,ruguoapp.com,DIRECT",
        "DOMAIN-SUFFIX,segmentfault.com,DIRECT",
        "DOMAIN-SUFFIX,sinaapp.com,DIRECT",
        "DOMAIN-SUFFIX,smzdm.com,DIRECT",
        "DOMAIN-SUFFIX,snapdrop.net,DIRECT",
        "DOMAIN-SUFFIX,sogou.com,DIRECT",
        "DOMAIN-SUFFIX,sogoucdn.com,DIRECT",
        "DOMAIN-SUFFIX,sohu.com,DIRECT",
        "DOMAIN-SUFFIX,soku.com,DIRECT",
        "DOMAIN-SUFFIX,speedtest.net,DIRECT",
        "DOMAIN-SUFFIX,sspai.com,DIRECT",
        "DOMAIN-SUFFIX,suning.com,DIRECT",
        "DOMAIN-SUFFIX,taobao.com,DIRECT",
        "DOMAIN-SUFFIX,tencent.com,DIRECT",
        "DOMAIN-SUFFIX,tenpay.com,DIRECT",
        "DOMAIN-SUFFIX,tianyancha.com,DIRECT",
        "DOMAIN-SUFFIX,tmall.com,DIRECT",
        "DOMAIN-SUFFIX,tudou.com,DIRECT",
        "DOMAIN-SUFFIX,umetrip.com,DIRECT",
        "DOMAIN-SUFFIX,upaiyun.com,DIRECT",
        "DOMAIN-SUFFIX,upyun.com,DIRECT",
        "DOMAIN-SUFFIX,veryzhun.com,DIRECT",
        "DOMAIN-SUFFIX,weather.com,DIRECT",
        "DOMAIN-SUFFIX,weibo.com,DIRECT",
        "DOMAIN-SUFFIX,xiami.com,DIRECT",
        "DOMAIN-SUFFIX,xiami.net,DIRECT",
        "DOMAIN-SUFFIX,xiaomicp.com,DIRECT",
        "DOMAIN-SUFFIX,ximalaya.com,DIRECT",
        "DOMAIN-SUFFIX,xmcdn.com,DIRECT",
        "DOMAIN-SUFFIX,xunlei.com,DIRECT",
        "DOMAIN-SUFFIX,yhd.com,DIRECT",
        "DOMAIN-SUFFIX,yihaodianimg.com,DIRECT",
        "DOMAIN-SUFFIX,yinxiang.com,DIRECT",
        "DOMAIN-SUFFIX,ykimg.com,DIRECT",
        "DOMAIN-SUFFIX,youdao.com,DIRECT",
        "DOMAIN-SUFFIX,youku.com,DIRECT",
        "DOMAIN-SUFFIX,zealer.com,DIRECT",
        "DOMAIN-SUFFIX,zhihu.com,DIRECT",
        "DOMAIN-SUFFIX,zhimg.com,DIRECT",
        "DOMAIN-SUFFIX,zimuzu.tv,DIRECT",
        "DOMAIN-SUFFIX,zoho.com,DIRECT",
        // # 常见广告域名屏蔽
        "DOMAIN-KEYWORD,admarvel,REJECT",
        "DOMAIN-KEYWORD,admaster,REJECT",
        "DOMAIN-KEYWORD,adsage,REJECT",
        "DOMAIN-KEYWORD,adsmogo,REJECT",
        "DOMAIN-KEYWORD,adsrvmedia,REJECT",
        "DOMAIN-KEYWORD,adwords,REJECT",
        "DOMAIN-KEYWORD,adservice,REJECT",
        "DOMAIN-SUFFIX,appsflyer.com,REJECT",
        "DOMAIN-KEYWORD,domob,REJECT",
        "DOMAIN-SUFFIX,doubleclick.net,REJECT",
        "DOMAIN-KEYWORD,duomeng,REJECT",
        "DOMAIN-KEYWORD,dwtrack,REJECT",
        "DOMAIN-KEYWORD,guanggao,REJECT",
        "DOMAIN-KEYWORD,lianmeng,REJECT",
        "DOMAIN-SUFFIX,mmstat.com,REJECT",
        "DOMAIN-KEYWORD,mopub,REJECT",
        "DOMAIN-KEYWORD,omgmta,REJECT",
        "DOMAIN-KEYWORD,openx,REJECT",
        "DOMAIN-KEYWORD,partnerad,REJECT",
        "DOMAIN-KEYWORD,pingfore,REJECT",
        "DOMAIN-KEYWORD,supersonicads,REJECT",
        "DOMAIN-KEYWORD,uedas,REJECT",
        "DOMAIN-KEYWORD,umeng,REJECT",
        "DOMAIN-KEYWORD,usage,REJECT",
        "DOMAIN-SUFFIX,vungle.com,REJECT",
        "DOMAIN-KEYWORD,wlmonitor,REJECT",
        "DOMAIN-KEYWORD,zjtoolbar,REJECT",

        "DOMAIN-SUFFIX,telegra.ph,auto",
        "DOMAIN-SUFFIX,telegram.org,auto",
        "IP-CIDR,91.108.4.0/22,auto",
        "IP-CIDR,91.108.8.0/21,auto",
        "IP-CIDR,91.108.16.0/22,auto",
        "IP-CIDR,91.108.56.0/22,auto",
        "IP-CIDR,149.154.160.0/20,auto",
        "IP-CIDR6,2001:67c:4e8::/48,auto",
        "IP-CIDR6,2001:b28:f23d::/48,auto",
        "IP-CIDR6,2001:b28:f23f::/48,auto",

        "GEOIP,CN,DIRECT",
        "MATCH,auto");

    yamlData.put("rules", rules);
  }

  public static String toYamlWithJackson(Map<String, Object> yamlData) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
    return objectMapper.writeValueAsString(yamlData);
  }

}
