package com.back.domain;

import org.junit.Before;
import org.junit.Test;

import javax.script.ScriptException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ScriptProcessorTest {
    ScriptProcessor processor = new ScriptProcessor();
    @Test
    public void test()
    {
        Map<String, Serializable> m= new HashMap<String, Serializable>() ;
        String command="val=params['a']; if(val==null) val=0;params['a']=++val;";
        try {
            processor.withScriptProcessor("groovy").eval(command,m);
            assertEquals(1,m.get("a"));
            processor.withScriptProcessor("groovy").eval(command,m);
            assertEquals(2,m.get("a"));
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

}