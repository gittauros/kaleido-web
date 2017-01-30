<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>exhentai - list</title>
    <script src="${(contextPath)!}/js/jquery.min.js"></script>
    <style>
        html, body, p {
            font-family: sans-serif;
        }

        .bz_title {
            display: block;
            width: 290px;
            position: relative;
            line-height: 1.4em;
            height: 2.8em;
            overflow: hidden;
        }

        .bz_title::after {
            font-weight: bold;
            position: absolute;
            bottom: 0;
            right: 0;
            padding: 0 20px 1px 45px;
        }

        .advck {
            width: 15px;
            height: 15px;
        }

        .advcktd {
            width: 150px;
        }
    </style>
</head>
<body style="margin: 0; padding: 0 0;">
<div style="width: 100%; height: auto; position: fixed; background-color: white; z-index: 99;">
    <div style="float: left; display: inline; width: 100%; padding: 8px; border-bottom: solid 1px black;">
        <table border="1" style="margin: 5px auto 0 auto;">
            <tbody>
            <tr>
                <td>
                    搜索关键词:<input style="width: 300px;" type="text" value="${(search)!}" name="search" id="search_key"
                                 onkeypress="confirmPress(event)"/>
                </td>
            </tr>
            </tbody>
            <tfoot>
            <tr>
                <td>
                    <div style="width: 410px; margin: 0 auto;">
                        指定url下载：
                        <input id="singlebz" type="text" onkeypress="downloadOnePress(event)"/>
                        原图:<input style="width: 15px; height: 15px;" id="singleorigin" type="checkbox"/>
                        <input type="button" value="下载" onclick="downloadOne()"/>
                    </div>
                </td>
            </tr>
            </tfoot>
        </table>
        <div style="margin: 5px auto 0 auto; width: 700px">
        <#if page != 1 ><a style="margin-right: 10px;" href="javascript:page_change(1);">首页</a></#if>
        <#if page gt 1 ><a style="margin-right: 10px;" href="javascript:page_change(${page - 1});">上一页</a></#if>
            <input style="width: 50px; height: 30px; font-size: 15px; text-align: center;" type="number"
                   onchange="pageValChange(this)" onkeypress="confirmPress(event)" value="${(page)!}" id="page"/>
            / ${(maxPage)!}
        <#if page lt maxPage><a style="margin-left: 10px;" href="javascript:page_change(${page + 1});">下一页</a></#if>
        <#if page != maxPage><a style="margin-left: 10px;" href="javascript:page_change(${maxPage});">末页</a></#if>
            <input style="width: 300px; height: 30px; font-size: 14px; margin-left: 15px;" type="button" value="确定"
                   onclick="ex_search()"/>
        </div>
    </div>
    <div style="float: left; position: absolute; left: 0px; padding: 8px 0 0 8px; width: 500px;">
        <div style="text-align: left; margin-left: 10px;">
            <a id="toggle_advance" href="javascript:toggle_advance_table();"
               style="clear: both; width: 100%; text-align: left; margin-left: 10px;">高级</a>
        </div>
        <table id="advance_table" border="1"
               style="display: none; background-color: aliceblue; clear: both; text-align: center; margin-top: 8px; margin-left: 20px;">
            <tr>
                <td>高级:</td>
            </tr>
            <tr>
                <td>
                    f_apply:<input type="text" value="Apply+Filter" name="apply"/>
                    <select name="apply" id="f_apply">
                        <option value="Apply+Filter"
                                <#if apply==null || apply == "Apply+Filter">selected="selected"</#if>
                        >Apply+Filter
                        </option>
                        <option value="Search"
                                <#if apply == "Search">selected="selected"</#if>
                        >Search
                        </option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>
                    <table style="width: 650px; text-align: right;">
                        <tr>
                            <td class="advcktd" onclick="advtdclick(this)">
                                <span>f_doujinshi:</span>
                                <input id="fdoujinshi" class="advck" type="checkbox"
                                       <#if fdoujinshi>checked</#if>
                                />
                            </td>
                            <td class="advcktd" onclick="advtdclick(this)">
                                <span>f_manga:</span>
                                <input id="fmanga" class="advck" type="checkbox"
                                       <#if fmanga>checked</#if>
                                />
                            </td>
                            <td class="advcktd" onclick="advtdclick(this)">
                                <span>f_artistcg:</span>
                                <input id="fartistcg" class="advck" type="checkbox"
                                       <#if fartistcg>checked</#if>
                                />
                            </td>
                            <td class="advcktd" onclick="advtdclick(this)">
                                <span>f_gamecg:</span>
                                <input id="fgamecg" class="advck" type="checkbox"
                                       <#if fgamecg>checked</#if>
                                />
                            </td>
                            <td class="advcktd" onclick="advtdclick(this)">
                                <span>f_western:</span>
                                <input id="fwestern" class="advck" type="checkbox"
                                       <#if fwestern>checked</#if>
                                />
                            </td>
                        </tr>
                        <tr>
                            <td class="advcktd" onclick="advtdclick(this)">
                                <span>f_nonh:</span>
                                <input id="fnonh" class="advck" type="checkbox"
                                       <#if fnonh>checked</#if>
                                />
                            </td>
                            <td class="advcktd" onclick="advtdclick(this)">
                                <span>f_imageset:</span>
                                <input id="fimageset" class="advck" type="checkbox"
                                       <#if fimageset>checked</#if>
                                />
                            </td>
                            <td class="advcktd" onclick="advtdclick(this)">
                                <span>f_cosplay:</span>
                                <input id="fcosplay" class="advck" type="checkbox"
                                       <#if fcosplay>checked</#if>
                                />
                            </td>
                            <td class="advcktd" onclick="advtdclick(this)"
                            <span>f_asianporn:</span>
                            <input id="fasianporn" class="advck" type="checkbox"
                                   <#if fasianporn>checked</#if>
                            />
                            </td>
                            <td class="advcktd" onclick="advtdclick(this)">
                                <span>f_misc:</span>
                                <input id="fmisc" class="advck" type="checkbox"
                                       <#if fmisc>checked</#if>
                                />
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
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
<#-- 本子列表 -->
<div style="position: absolute; margin-top: 125px; margin-left: 8px; text-align: center;">
<#if listBOs??>
    <#list listBOs as bo>
        <div style="display: inline-block; height: 450px; width: 300px; margin: 5px; border: solid 1px bisque;">
            <div style="display: block; width: 100%; height: 300px; margin-top: 5px; cursor:pointer;"
                 onclick="openUrl('${(bo.galleryUrl)!}')">
                <img style="max-width: 100%; max-height: 100%; margin: auto;" src="${contextPath}${(bo.coverImg)!}"/>
            </div>
            <div style="display: block; width: 100%; text-align: center; margin-top:5px;">
                <img style="display: block; margin: 0 auto;" src="${contextPath}${(bo.tagImg)!}"/>
                <span style="display: block; margin-top: 5px;" class="bz_title"
                      title="${(bo.title)!}">${(bo.title)!}</span>
            </div>
            <div style="width: 100%; margin-top: 5px;">
                <a href="${(bo.bzUrl)!}" target="_blank" style="display: block;">
                    <span>原地址(访问需修改浏览器cookie)</span>
                </a>
            </div>
            <div style="position: relative; width: 100%; height: 35px; margin-top: 5px;">
                <div style="position: absolute; top: 50%; bottom: 50%; left: 50%; right: 50%; width: 200px; height: 30px; margin-top: -10px; margin-left: -100px;">
                    <input style="display: inline-block;" type="button" value="原图下载"
                           onclick="download('${(bo.bzUrl)!}', true)"/>
                    <input style="display: inline-block; margin-left: 20px;" type="button" value="普通下载"
                           onclick="download('${(bo.bzUrl)!}', false)"/>
                </div>
            </div>
        </div>
    </#list>
</#if>
</div>
</body>
<script>
    var cks = jQuery(".advck");
    for (var i = 0; i < cks.length; i++) {
        cks[i].addEventListener("click", function (e) {
            e.stopPropagation();
        }, false);
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
//        console.log(jQuery(obj).val());
    }

    function advtdclick(obj) {
//        jQuery(obj).find("input").click();
        var ele = jQuery(obj).find("input")[0];
        ele.checked = !ele.checked;
    }

    function baseExSearchUrl() {
        var search_key = jQuery("#search_key").val();
        var f_apply = jQuery("#f_apply").val();
        var fdoujinshi = document.getElementById("fdoujinshi").checked;
        var fmanga = document.getElementById("fmanga").checked;
        var fartistcg = document.getElementById("fartistcg").checked;
        var fgamecg = document.getElementById("fgamecg").checked;
        var fwestern = document.getElementById("fwestern").checked;
        var fnonh = document.getElementById("fnonh").checked;
        var fimageset = document.getElementById("fimageset").checked;
        var fcosplay = document.getElementById("fcosplay").checked;
        var fasianporn = document.getElementById("fasianporn").checked;
        var fmisc = document.getElementById("fmisc").checked;

        var searchUrl = "${contextPath!}/exhentai/list?" +
                "search=" + encodeURIComponent(search_key) +
                "&f_apply=" + f_apply +
                "&fdoujinshi=" + fdoujinshi +
                "&fmanga=" + fmanga +
                "&fartistcg=" + fartistcg +
                "&fgamecg=" + fgamecg +
                "&fwestern=" + fwestern +
                "&fnonh=" + fnonh +
                "&fimageset=" + fimageset +
                "&fcosplay=" + fcosplay +
                "&fasianporn=" + fasianporn +
                "&fmisc=" + fmisc;
        return searchUrl;
    }

    function ex_search() {
        var baseUrl = baseExSearchUrl();
        var page = jQuery("#page").val();
        if (page > parseInt("${(maxPage)!}")) {
            page = parseInt("${(maxPage)!}");
        }
        window.location = baseUrl + "&page=" + page;
    }

    function page_change(page) {
        var baseUrl = baseExSearchUrl();
        window.location = baseUrl + "&page=" + page;
    }

    function confirmPress(event) {
        if (event.keyCode == 13) {
            ex_search();
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

    function download(url, origin) {
        console.log(url);

        var params = {
            url: url,
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

    var downloadOne = function () {
        var url = jQuery("#singlebz").val();
        var origin = document.getElementById("singleorigin").checked;

        var params = {
            url: url,
            origin: origin
        };
        var settings = getSettings();
        jQuery.extend(params, settings);

        jQuery.post("${contextPath!}/exhentai/download", params,
                function (data, status) {
//                    alert(data);
                    console.log(data);
                });
    };

    function downloadOnePress(event) {
        if (event.keyCode == 13) {
            downloadOne();
        }
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

    var advanceTableShow = false;
    function toggle_advance_table() {
        advanceTableShow = !advanceTableShow;
        if (advanceTableShow) {
            jQuery("#advance_table").show();
        } else {
            jQuery("#advance_table").hide();
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

    var overMaxPage = ${overMaxPage?string("true", "false")};
    if (overMaxPage) {
        page_change(${(maxPage)!});
    }

    function openUrl(url) {
        window.open("${contextPath}" + url);
    }
</script>
</html>