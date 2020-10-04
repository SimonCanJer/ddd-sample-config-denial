package com.back.config.api;

public interface IPropertyBinder {
   IPropertyBinder bind(Class cl,String property);
   IConfig toConfig();


}
