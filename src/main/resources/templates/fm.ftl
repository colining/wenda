<html>
Hello!
<body>
<pre>
${value1}

<#list colors as color>
    color ${color} ${color_index}
</#list>
<#list  map?keys as key>
    key --> ${key}
    value --> ${map[key]}
</#list>
${user.name}
${user.getDescription()}

<#assign title= 5>
${title}

<#include "header.ftl">

<#macro colortest color index>
    render_macro: ${color} ,${index}
</#macro>

<#list colors as color>
    <@colortest color =color index = color_index>
    </@colortest>
</#list>

<#macro greet a>
    <font size=’+2’>HelloW Joe! ${a}</font>
</#macro>
<@greet a =title></@greet>


    <#assign hello = "hello">
    <#assign helloWorld1 = "${hello}1 world">
    <#assign helloWorld2 = '${hello}2 world'>
    ${helloWorld1}
    ${helloWorld2}
</pre>
</body>
</html>