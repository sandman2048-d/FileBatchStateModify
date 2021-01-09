
layui.use(['form', 'layedit', 'laydate'], function () {
    // window.onload = function () {
    //     // alert("ok");
    //     rendDatasource();
    // }
    
    var layer = layui.layer;
    var form = layui.form;
    var $ = layui.jquery;
    var datasource = document.getElementById('datasource');

    function rendDatasource() {
        var select = datasource;
        if(select==null){
            return;
        }
        var dataSourceList = [];
        dataSourceList.push("sit1");
        dataSourceList.push("sit2");
        dataSourceList.push("sit3");
        dataSourceList.push("sit4");
        dataSourceList.push("uat1");
        dataSourceList.push("uat2");
        dataSourceList.push("uat3");
        dataSourceList.push("uat4");
        dataSourceList.push("pp");
        dataSourceList.push("pi");
        // dataSourceList.push("zduat1");
        // dataSourceList.push("zduat2");
        dataSourceList.push("dev");
        dataSourceList.sort(function(a,b){
            return b-a;
        });
        for (i = select.options.length - 1; i >= 0; i--) {
            select.options[i] = null;
        }

        for (i = dataSourceList.length-1; i >= 0; i--) {
            var option = document.createElement("option");
            option.setAttribute("value", dataSourceList[i]);
            option.innerText = dataSourceList[i];
            select.appendChild(option)
        }
        form.render("select");
    }
    $(function(){
        rendDatasource();
    });
});