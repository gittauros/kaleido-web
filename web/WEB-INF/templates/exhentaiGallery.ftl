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
<div style="width: 100%; height: auto; position: fixed; background-color: white; z-index: 99;">
    <div style="float: left; display: inline; width: 100%; padding: 8px; border-bottom: solid 1px black;">
        <div style="margin: 0 auto 0 auto; width: 400px;">
        <#if page != 1 ><a style="margin-right: 10px;" href="javascript:page_change(1);">首页</a></#if>
        <#if page gt 1 ><a style="margin-right: 10px;" href="javascript:page_change(${page - 1});">上一页</a></#if>
            <input style="width: 50px; height: 30px; font-size: 15px; text-align: center;" type="number"
                   onchange="pageValChange(this)" onkeypress="confirmPress(event)" value="${(page)!}" id="page"/>
            / ${(maxPage)!}
        <#if page lt maxPage><a style="margin-left: 10px;" href="javascript:page_change(${page + 1});">下一页</a></#if>
        <#if page != maxPage><a style="margin-left: 10px;" href="javascript:page_change(${maxPage});">末页</a></#if>
        <#--<input style="width: 300px; height: 30px; font-size: 14px; margin-left: 15px;" type="button" value="确定"-->
        <#--onclick="ex_search()"/>-->
        </div>
    </div>
    <div style="float: right; position: absolute; right: 0px; padding: 8px 8px 0 0; width: 500px;">
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
<div style="width: 100%; height: auto; position: absolute;">
    <div style="width: 1400px; margin: 125px auto 0 auto; text-align: center;">
    <#if galleryBOs??>
        <#list galleryBOs as bo>
            <#if large>
                <div style="display: inline-block; height: 320px; width: 240px; margin: 5px; /*border: solid 1px bisque;*/">
                    <img title="${(bo.title)!}" src="${contextPath}${(bo.largeImg)!}" style="border: solid 1px;"/>
                </div>
            <#else >
                <div style="display: inline-block; height: 160px; width: 120px; margin: 5px; /*border: solid 1px bisque;*/">
                    <div style="border: solid 1px; margin:1px auto 0; width:100px; height:142px; background:transparent url(${contextPath}${(bo.smallImg)!}) ${(bo.smallImgXOffset)!}px ${(bo.smallImgYOffset)!}px no-repeat">
                        <img title="${(bo.title)!}" src="${contextPath}${(bo.smallImgPlaceHolder)!}"
                             style="width:100px; height:141px; margin:-1px 0 0 -1px"/>
                    </div>
                </div>
            </#if>
        </#list>
    </#if>
    </div>
</div>
</body>
<script>
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
                "&large=${(large)!}";
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
            url: "${(encodeOriUrl)!}",
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

    function openUrl(url) {
        window.open("${contextPath}" + url);
    }
</script>
</html>