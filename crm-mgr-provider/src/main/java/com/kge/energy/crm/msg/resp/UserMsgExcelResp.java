package com.kge.energy.crm.msg.resp;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.enums.BooleanEnum;
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

    @Schema(description = "消息业务类型， 0 告警通知 1 工单通知 2 项目合同 3 投诉处理")
    @ExcelProperty("消息类型")
    private String msgBizType;

    @Schema(description = "消息内容")
    @ExcelProperty("消息内容")
    @ContentStyle(wrapped = BooleanEnum.TRUE, shrinkToFit = BooleanEnum.TRUE)
    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "消息时间")
    @ExcelProperty("消息时间")
    @ContentStyle(shrinkToFit = BooleanEnum.TRUE)
    private LocalDateTime createTime;

    public void setMsgBizType(Integer msgBizType) {
        if (msgBizType.equals(0)){
            this.msgBizType = "告警通知";
        } else if (msgBizType.equals(1)){
            this.msgBizType = "工单通知";
        } else if (msgBizType.equals(2)){
            this.msgBizType = "项目合同";
        } else if (msgBizType.equals(3)){
            this.msgBizType = "投诉处理";
        }
    }

    public void setContent(Map<String, Object> content) {
        this.content = content.get("format").toString();
    }

}
