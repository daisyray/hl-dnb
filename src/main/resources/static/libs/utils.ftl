<#macro showError key>
    <#if errorMap[key]??>
        <span class="red-text" style='font-style: italic;'>${errorMap[key]}</span>
    </#if>
</#macro>

<#function static_url path>
    <#assign noStatic>${path?replace("^/static", "", 'r')?remove_beginning("/")?remove_ending("/")}</#assign>
    <#return "/static/${APP_RELEASE_VERSION}/${noStatic}">
</#function>
