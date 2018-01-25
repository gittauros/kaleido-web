<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Kaleido - Home</title>
</head>
<body>
<div>
${message}
    <br>
${message_cn}
    <p>
    ${request}
        <br>
    ${response}
    </p>
    <p>
    <#--<a href="${contextPath}/exhentai/list" target="_blank">里</a>-->
    </p>
</div>
<script>
 var ddata;
 function action1() {
     var i = 0;
     var id = setInterval(function (){
         if (i >= ddata.length) {
             clearInterval(id);
             return;
         }
         var taskRequestId = ddata[i++];
         ($).ajax({
             type: 'POST',
             url: 'task/taskRetry',
             cache:false,
             async:false,
             data: {
                 taskId:taskRequestId,
                 scheduledTime:'2017-12-10 17:46:24'
             },
             success: function(data) {
                 if(data['success']) {
                     console.log(taskRequestId + '|success');
                 } else{
                     console.log(taskRequestId + '|fail|' + data.errorMsg);
                 }
             }
         });
     }, 300)
 }

 function action2() {
     var i = 0;
     var result = [];
     var endedCount = 0;
     var id = setInterval(function () {
         if (i >= ddata.length) {
             clearInterval(id);
             return;
         }
         var taskRequestId = ddata[i++];
         ($).ajax({
             url:'task/taskQuery',
             type:'POST',
             cache:false,
             data:{taskId:taskRequestId},
             success:function(data){
                 if(data['success']) {
                     data = data['model'][0];
                     if (data.status !== 1) {
                         result.push([taskRequestId, data.memo]);
                     }
                 } else{
                     result.push([taskRequestId, "failRequest" + data['errorMsg']]);
                 }
                 console.log(taskRequestId + '|end');
                 endedCount++;
                 if (endedCount === ddata.length) {
                     console.log(JSON.stringify(result));
                     console.log(result.length);
                 }
             }
         })
     }, 100);
 }

 function trans(data) {
     var dd = [];
     for (var i = 0; i < data.length; i++) {
         dd.push(data[i][0]);
     }
     console.log(JSON.stringify(dd));
 }
</script>
</body>
</html>