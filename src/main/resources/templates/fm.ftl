<html>
Hello!
<body>
<pre>
    <#--通过${xxx} 来表示变量-->
    ${value1}

    <#--colors是从后台传来的对象，和foreach语法类似-->
    <#list colors as color>
        color ${color} ${color_index}
    </#list>
    <#--map是后台对象，?key是map固定写法-->
    <#list  map?keys as key>
        key --> ${key}
        value --> ${map[key]}
    </#list>
    <#--得到属性的两种方法-->
    ${user.name}
    ${user.getDescription()}

    <#--声明变量-->
    <#assign title= 5>
    ${title}
    <#--在本页面加入其他页面-->
    <#include "header.ftl">

    <#--定义宏，colortest是宏的名字，color和index是参数-->
    <#macro colortest color index>
        render_macro: ${color} ,${index}
    </#macro>
    <#--相当于遍历时调用函数，注意参数不需要加${},我也不知道为啥-->
    <#list colors as color>
        <@colortest color =color index = color_index>
        </@colortest>
    </#list>

    <#macro greet a>
        <font size=’+2’>HelloW Joe! ${a}</font>
    </#macro>
    <@greet a =title></@greet>

    <#--可以通过已定义变量定义新的变量-->
    <#assign hello = "hello">
    <#assign helloWorld1 = "${hello}1 world">
    <#assign helloWorld2 = '${hello}2 world'>
    ${helloWorld1}
    ${helloWorld2}
</pre>
</body>
</html>