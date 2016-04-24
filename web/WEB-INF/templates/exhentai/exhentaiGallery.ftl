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
<body style="margin: 0; padding: 0 0;text-align: center;">
<div style="width: 100%; height: auto; position: fixed; background-color: white; z-index: 99;">
    <div style="float: left; display: inline; width: 100%; padding: 8px; border-bottom: solid 1px black;">
        <div style="margin: 0 auto 0 auto; width: 630px;">
        <#if page != 1 ><a style="margin-right: 10px;" href="javascript:page_change(1);">首页</a></#if>
        <#if page gt 1 ><a style="margin-right: 10px;" href="javascript:page_change(${page - 1});">上一页</a></#if>
            <input style="width: 50px; height: 30px; font-size: 15px; text-align: center;" type="number"
                   onchange="pageValChange(this)" onkeypress="confirmPress(event)" value="${(page)!}" id="page"/>
            / ${(maxPage)!}
        <#if page lt maxPage><a style="margin-left: 10px;" href="javascript:page_change(${page + 1});">下一页</a></#if>
        <#if page != maxPage><a style="margin-left: 10px;" href="javascript:page_change(${maxPage});">末页</a></#if>
            <div style="display: inline-block; margin: auto 0 auto 10px; width: 230px; height: 30px;">
                <input style="display: inline-block;width: 100px; height: 100%;" type="button" value="原图下载"
                       onclick="download(true)"/>
                <input style="display: inline-block;width: 100px; height: 100%; margin-left: 20px;" type="button"
                       value="普通下载"
                       onclick="download(false)"/>
            </div>
        </div>
    </div>
    <div style="float: right; position: absolute; right: 0px; padding: 8px 8px 0 0; width: 500px;">
        <a id="toggle_large" href="javascript:setLarge(!${large?string('true','false')});">
        <#if large>
            小图浏览
        <#else>
            大图浏览
        </#if>
        </a>
        <div style="float: right; text-align: right; margin-right: 10px;">
            <a id="toggle_settings" href="javascript:toggle_settings_table();"
               style="clear: both; width: 100%; text-align: right; margin-right: 10px;">展开设置</a>
            <table id="settings_table" border="1"
                   style="display: none; background-color: aliceblue; clear: both; text-align: center; margin-top: 8px; margin-right: 10px;">
                <tr>
                    <td>设置:</td>
                </tr>
                <tr>
                    <td>
                        保存路径:<input id="path" type="text" value="${savePath!}" onchange="modify_save_path()"
                                    onkeydown="modify_save_path()"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        间隔时间(毫秒):<input id="sleepTime" type="text" value="500"/>
                    </td>
                </tr>
                <tr>
                    <td>代理：</td>
                </tr>
                <tr>
                    <td>
                        ip:<input id="proxyIp" type="text"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        port:<input id="proxyPort" type="text"/>
                    </td>
                </tr>
            </table>
        </div>
    </div>
</div>
<#-- 图片列表-->
<div style="position: absolute; width: 100%; height: auto;">
    <div style="width: 1200px; margin: 65px auto 0 auto; display: block;">
    <#if galleryBOs??>
        <#list galleryBOs as bo>
            <#if large>
                <div onclick="openUrl('${(bo.photoUrl)!}')"
                     style="cursor: pointer; display: block; float: left; height: 320px; width: 240px; /*border: solid 1px bisque;*/">
                    <img title="${(bo.title)!}" src="${contextPath}${(bo.largeImg)!}" style="border: solid 1px;"/>
                </div>
            <#else>
                <div onclick="openUrl('${(bo.photoUrl)!}')"
                     style="cursor: pointer; display: block; float: left; height: 170px; width: 120px; text-align: center; /*border: solid 1px bisque;*/">
                    <div style="margin:1px auto 0; width:${(bo.smallImgWidth)!}px; height:${(bo.smallImgHeight + 1)!}px; background:transparent url(${contextPath}${(bo.smallImg)!}) ${(bo.smallImgXOffset)!}px ${(bo.smallImgYOffset)!}px no-repeat">
                        <img title="${(bo.title)!}" src="${contextPath}${(bo.smallImgPlaceHolder)!}"
                             style="border: solid 1px; width:${(bo.smallImgWidth)!}px; height:${(bo.smallImgHeight)!}px; margin:-1px 0 0 -1px"/>
                    </div>
                </div>
            </#if>
        </#list>
    </#if>
    </div>
</div>
</body>
<script>
    var large = ${large?string('true','false')};
    function setLarge(isLarge) {
        large = isLarge;
        page_change(${(page)!});
    }

    function pageValChange(obj) {
        var page = jQuery(obj).val();
        if (page <= 0) {
            page = 1;
        }
        if (page > parseInt("${(maxPage)!}")) {
            page = parseInt("${(maxPage)!}");
        }
        jQuery(obj).val(page);
    }

    function baseGalleryUrl() {
        return "${contextPath!}/exhentai/gallery?" +
                "oriUrl=${(encodeOriUrl)!}" +
                "&large=" + large;
    }

    function page_change(page) {
        var baseUrl = baseGalleryUrl();
        window.location = baseUrl + "&page=" + page;
    }

    function gallery() {
        var baseUrl = baseGalleryUrl();
        var page = jQuery("#page").val();
        if (page > parseInt("${(maxPage)!}")) {
            page = parseInt("${(maxPage)!}");
        }
        window.location = baseUrl + "&page=" + page;
    }

    function download(origin) {
        var params = {
            url: "${(oriUrl)!}",
            origin: origin
        };
        var settings = getSettings();
        jQuery.extend(params, settings);

        jQuery.post("${contextPath!}/exhentai/download", params,
                function (data, status) {
//                    alert(data);
                    console.log(data);
                });
    }

    function confirmPress(event) {
        if (event.keyCode == 13) {
            gallery();
        }
    }

    function getSettings() {
        var sleep = jQuery("#sleepTime").val();
        var proxyIp = jQuery("#proxyIp").val();
        var proxyPort = jQuery("#proxyPort").val();
        var maxThreadNum = jQuery("#maxThreadNum").val();
        var timeOut = jQuery("#timeOut").val();
        var path = jQuery("#path").val();
//        var origin = document.getElementById("origin").checked;

        var settings = {
            sleep: sleep,
            proxyIp: proxyIp,
            proxyPort: proxyPort,
            maxThreadNum: maxThreadNum,
            timeOut: timeOut,
            path: path/*,
             origin: origin*/
        };
        return settings;
    }

    var settingsTableShow = false;
    function toggle_settings_table() {
        settingsTableShow = !settingsTableShow;
        if (settingsTableShow) {
            jQuery("#settings_table").show();
            jQuery("#toggle_settings").html("收起设置");
        } else {
            jQuery("#settings_table").hide();
            jQuery("#toggle_settings").html("展开设置");
        }
    }

    function modify_save_path() {
        var path = jQuery("#path").val();
        jQuery.ajax({
            url: "${contextPath}/exhentai/modifySavePath",
            async: true,
            data: {
                savePath: path
            },
            type: "GET",
            success: function (data) {
                console.log(data);
            },
            error: function (data) {
                console.log(data);
            }
        })
    }

    function openUrl(url) {
        window.open("${contextPath}" + url);
    }
</script>
</html>