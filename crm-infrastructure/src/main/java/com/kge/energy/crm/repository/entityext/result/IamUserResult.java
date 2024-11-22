package com.kge.energy.crm.repository.entityext.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * iam用户表(IamUser)响应对象
 *
 * @author zhengwenke
 * @since 2024-11-11 11:30:46
 */
@Data
@Accessors(chain = true)
@Schema(description = "iam用户表响应对象")
public class IamUserResult {

    @Schema(description = "用户工号")
    private String simId;

    @Schema(description = "用户id")
    private String userId;

    @Schema(description = "用户姓名")
    private String userCn;

    @Schema(description = "岗位ID")
    private String userJobId;

    @Schema(description = "员工状态：1-有效 0-无效")
    private String userStatus;

    @Schema(description = "手机")
    private String userMobile;

    @Schema(description = "证件类型")
    private String userIdCardNumber;

    @Schema(description = "登录账号")
    private String userLoginId;

    @Schema(description = "用户手机初始化标识")
    private String userInitMobile;

    @Schema(description = "用户密码初始化标识")
    private String userInitPasswd;

    @Schema(description = "用户密码")
    private String userPassword;

    @Schema(description = "用户密码")
    private String userCipherPassword;

    @Schema(description = "用户组织id")
    private String userOrgId;

    @Schema(description = "用户组织名称")
    private String userOrgName;

    @Schema(description = "用户组织编码全路径")
    private String userOrgNumFullPath;

    @Schema(description = "用户组织名称全路径")
    private String userOrgNameFullPath;

    @Schema(description = "兼职组织")
    private String userParttimeOrg;

    @Schema(description = "用户性别：1-男 2-女")
    private String userGender;

    @Schema(description = "用户对象class")
    private String userObjectclass;

    @Schema(description = "用户类型：E1-内，E2-外")
    private String userType;

    @Schema(description = "用户sso权限")
    private String userSsoAuthority;

    @Schema(description = "用户sso权限backup")
    private String userSsoAuthorityBackUp;

    @Schema(description = "用户修改时间")
    private String userNormalModifyTimestamp;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "数据入库时间")
    private LocalDateTime syncCreateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "数据更新时间")
    private LocalDateTime syncUpdateTime;

}



