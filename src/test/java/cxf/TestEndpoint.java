package cxf;


import javax.xml.ws.Endpoint;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;



public class TestEndpoint extends TestCase {
    
    private static final String ADDRESS = "http://localhost:9000/cxfdemo"; 
    protected void setUp() throws Exception {
        super.setUp();
        
        System.out.println("Starting Server");  
        CXFDemoImpl demo = new CXFDemoImpl();  
        
        Endpoint.publish(ADDRESS, demo);
        System.out.println("Start success");
    }
    
    public void testSayHello(){
        
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(CXFDemo.class);
        factory.setAddress(ADDRESS);
        CXFDemo client = (CXFDemo)factory.create();
        Assert.assertEquals(client.sayHello("foo"), "hello foo");
    }
}