package dubbo.api.service.impl;

import dubbo.api.service.TestService;

public class TestServiceImpl implements TestService {

    public String testSayDubbo() {
        System.out.println("---testSayDubbo----服务被调用----------");
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Dubbo say:Hello!!!!!");
        return stringBuffer.toString();
    }

    public String say(String name) {
        System.out.println("----say---服务被调用----------");
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(name + " say:Hello!!!!!");
        return stringBuffer.toString();
    }

}
