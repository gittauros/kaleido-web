<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>exhentai - gallery</title>
    <script src="${(contextPath)!}/js/jquery.min.js"></script>
    <style>
        html, body, p {
            font-family: sans-serif;
        }
    </style>
</head>
<body style="margin: 0; padding: 0 0;">
<#-- 图片列表-->
<div style="position: absolute; margin-top: 125px; margin-left: 8px; text-align: center;">
<#if galleryBOs??>
    <#list galleryBOs as bo>
        <div style="display: inline-block; height: 450px; width: 300px; margin: 5px; border: solid 1px bisque;">
            <#if large>
                <div>
                    <img title="${(bo.title)!}" src="${contextPath}${(bo.largeImg)!}"/>
                </div>
            <#else >
                <div style="margin:1px auto 0; width:100px; height:142px; background:transparent url(${contextPath}${(bo.smallImg)!}) ${(bo.smallImgXOffset)!}px ${(bo.smallImgYOffset)!}px no-repeat">
                    <img title="${(bo.title)!}" src="${contextPath}${(bo.smallImgPlaceHolder)!}"
                         style="width:100px; height:141px; margin:-1px 0 0 -1px"/>
                </div>
            </#if>
        </div>
    </#list>
</#if>
</div>
</body>
<script>
    function openUrl(url) {
        window.open("${contextPath}" + url);
    }
</script>
</html>