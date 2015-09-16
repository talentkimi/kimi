package cxf;

import javax.jws.WebService;

@WebService()
public class CXFDemoImpl implements CXFDemo {

    public String sayHello(String foo) {
        return "hello "+foo;
    }

}