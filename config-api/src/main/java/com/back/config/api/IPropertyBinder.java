package com.back.config.api;

/**
 * define binding of an interface to porperty name, where its concfrete supplier
 * class is defined.
 */
public interface IPropertyBinder {

   IPropertyBinder bind(Class cl,String property);
   IConfig toConfig();


}
