<#import "/layout/header.ftl" as hdr>
<#import "/layout/footer.ftl" as ftr>
<#import "/libs/utils.ftl" as utils>

<#macro layout page_css="" page_js="" headerTitle="" showHeader=true showFooter=true>
<!doctype html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="">
        <meta name="author" content="">
        <link rel="icon" href="${utils.static_url("/favicon.ico")}">

        <title>HL-DNB - 2.0</title>

        <link rel="stylesheet" href="${utils.static_url("/material/materialize.min.css")}">
        <link rel="stylesheet" href="${utils.static_url("/styles.css")}"/>
        <link rel="stylesheet" href="${utils.static_url("/styles.css")}"/>
        <link rel="stylesheet" href="${utils.static_url("/material/material-icons.css")}">
        ${page_css}

        <script type="application/javascript" src="${utils.static_url("/material/materialize.min.js")}"></script>
        ${page_js}
    </head>
    <body style="height: 150vh;">
        <div style='width: 90%; margin: auto;'>
        <#if showHeader>
            <@hdr.header title=headerTitle/>
        </#if>
        <#nested>
        <#if showFooter>
            <@ftr.footer/>
        </#if>
        </div>
    </body>
</html>
</#macro>
