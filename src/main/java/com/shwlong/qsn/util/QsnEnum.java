package com.shwlong.qsn.util;


/**
 * 全局枚举类
 */
public enum QsnEnum {
    LOGIN_SUCCESS("登录成功"),
    LOGIN_PASSWORD_ERROR("密码错误"),
    LOGIN_USER_UNEXIST_OR_UNAUTHORIZED("用户名不存在或用户未认证"),
    TOKEN_IS_EMPTY("您还未登录，请登录"),
    TOKEN_IS_Expired("您的登录信息已失效，请重新登录"),
    PAPER_CREATE_FINISH("问卷已创建完成"),
    PAPER_EDIT_FINISH("问卷已编辑完成"),
    PAPER_PUBLIC_SUCCESS("问卷已发布成功"),
    PAPER_STOP_RECLAIM("问卷已停止回收"),
    PAPER_DELETE_SUCCESS("问卷删除成功"),
    PAPER_ANSWER_IS_SUBMIT("答卷已提交成功"),
    PAPER_HAVE_NO_ANSWER("当前没有答卷，无法查看"),
    PAPER_NO_PUBLISH("问卷未发布，无法填写"),
    PAPER_IS_STOP("问卷已结束，无法填写"),
    MODEL_CREATE_SUCCESS("问卷模板创建成功"),
    REGISTER_SUCCESS("注册成功"),
    REGISTER_USER_EXIST("此用户已存在"),
    EXCEL_DOWNLOAD_SUCCESS("文件下载成功"),
    FILE_FORMAT_ERROR("文件格式不支持"),
    FILE_SIZE_ERROR("文件超过100kb"),
    FILE_UPLOAD_SUCCESS("文件上传成功"),
    FILE_UPLOAD_FORMAT_ERROR("您上传的文件内容未按照要求的格式"),
    FILE_OPTION_ERROR("文件操作异常");
    private String msg;

    QsnEnum(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

}
