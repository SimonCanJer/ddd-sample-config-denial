package com.back.domain;

import javax.script.ScriptException;
import java.io.Serializable;
import java.util.Map;

/**
 * defines functionality of script processing
 */
public interface  IScriptProcessor {
    Serializable eval(String proc, Map<String, Serializable> object) throws ScriptException;

}
