$(function (){
    $("#loginbtn").bind("click", function (){
        login();
    });
});

function login(){
    var validateSuccess = validate();
    if(validateSuccess){
        var userName = $("#userName").val();
        var pwd = $("#pwd").val();
        pwd = hex_md5(pwd);
        $.ajax({
            url : "/service/execuLogin",
            type : "post",
            data:{"userName": userName,"pwd":pwd},
            dataType : "json",
            success : function(data){
                var success = data.success;
                if(success && success == true){
                    // alert(data.msg);
                    window.location.href = "/wechat/total/dataStatistics";
                }else{
                    alert("登录失败");
                }
            }
        });
    }
}

function validate(){
    var userName = $("#userName").val();
    if(userName == null || $.trim(userName).length < 1){
        alert("用户名为空");
        return false;
    }

    var pwd = $("#pwd").val();
    if(pwd == null || $.trim(pwd).length < 1){
        alert("密码为空");
        return false;
    }
    return true;

}

