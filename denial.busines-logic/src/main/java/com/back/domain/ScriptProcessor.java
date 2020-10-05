package com.back.domain;

import com.google.gson.Gson;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.Serializable;
import java.util.Map;

public class ScriptProcessor {
   IScriptProcessor withScriptProcessor(String str)
   {
       return new  IScriptProcessor(){
           ScriptEngine se= new ScriptEngineManager().getEngineByName(str);

           @Override
           public Serializable eval(String proc, Map<String, Serializable> map) throws ScriptException {
               se.put("params",map);

               return (Serializable) se.eval(proc);

           }
       };
   }
}
