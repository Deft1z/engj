package com.kge.energy.crm.msg.resp;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kge.energy.crm.easyexcel.DictConvert;
import com.kge.energy.crm.easyexcel.DictFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Accessors(chain = true)
@Schema(description = "用户消息列表excel导出响应体")
public class UserMsgExcelResp {

    @Schema(description = "用户姓名")
    @ExcelProperty("用户姓名")
    private String realname;

    @Schema(description = "消息业务类型， 0 告警通知 1 工单通知 2 项目合同 3 投诉处理 4 登录提醒")
    @ExcelProperty(value = "消息类型", converter = DictConvert.class)
    @DictFormat("msg-biz-type")
    private String msgBizType;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "消息时间")
    @ExcelProperty("消息时间")
    private LocalDateTime createTime;

    @Schema(description = "消息内容Map，注意告警信息、工单通知、项目合同、投诉处理的消息内容Map字段均有差异，需按实际取值展示")
    @ExcelIgnore
    private Map<String, Object> content;

    @Schema(description = "消息内容")
    @ExcelProperty("消息内容")
    private String formatContent;

    public String getFormatContent() {
        return this.content.get("format").toString();
    }

}
