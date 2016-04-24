<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>exhentai - photo</title>
    <script src="${(contextPath)!}/js/jquery.min.js"></script>
    <style>
        html, body, p {
            font-family: sans-serif;
        }
    </style>
</head>
<body style="margin: 0; padding: 0 0;">
<div style="width: 100%; height: auto; position: fixed; background-color: white; z-index: 99;">
    <div style="float: left; display: inline; width: 100%; padding: 8px; border-bottom: solid 1px black;">
        <div style="margin: 0 auto 0 auto; width: 350px;">
            <a style="margin-right: 10px;" href="${contextPath}${(photoBO.firstPageUrl)!}">首页</a>
            <a style="margin-right: 10px;" href="${contextPath}${(photoBO.prevPageUrl)!}">上一页</a>
        ${(photoBO.curPage)!}
            / ${(photoBO.lastPage)!}
            <a style="margin-left: 10px;" href="${contextPath}${(photoBO.nextPageUrl)!}">下一页</a>
            <a style="margin-left: 10px;" href="${contextPath}${(photoBO.lastPageUrl)!}">末页</a>
        </div>
    </div>
</div>
<#-- 图片-->
<div style="width: 100%; height: auto; position: absolute;">
    <div style="width: 1400px; margin: 55px auto 20px auto; text-align: center;">
        <img src="${contextPath}${(photoBO.photoImg)!}"
             style="border: solid 1px; width: ${(photoBO.imgWidth)!}px; height: ${(photoBO.imgHeight)!}px; max-width: ${(photoBO.imgWidth)!}px; max-height: ${(photoBO.imgHeight)!}px;"/>
    </div>
</div>
</body>
<script>

    function openUrl(url) {
        window.open("${contextPath}" + url);
    }
</script>
</html>