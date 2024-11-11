package com.kge.energy.crm.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * iam用户表(IamUser)实体类
 *
 * @author zhengwenke
 * @since 2024-11-11 11:30:46
 */
@TableName(value = "iam_user")
@Data
@Accessors(chain = true)
public class IamUser {

    /**
     * 用户工号
     */
    @TableId(type = IdType.AUTO)
    @TableField(value = "sim_id")
    private String SimId;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     * 用户姓名
     */
    @TableField(value = "user_cn")
    private String userCn;

    /**
     * 岗位ID
     */
    @TableField(value = "user_job_id")
    private String userJobId;

    /**
     * 员工状态：1-有效 0-无效
     */
    @TableField(value = "user_status")
    private String userStatus;

    /**
     * 手机
     */
    @TableField(value = "user_mobile")
    private String userMobile;

    /**
     * 证件类型
     */
    @TableField(value = "user_id_card_number")
    private String userIdCardNumber;

    /**
     * 登录账号
     */
    @TableField(value = "user_login_id")
    private String userLoginId;

    /**
     * 用户手机初始化标识
     */
    @TableField(value = "user_init_mobile")
    private String userInitMobile;

    /**
     * 用户密码初始化标识
     */
    @TableField(value = "user_init_passwd")
    private String userInitPasswd;

    /**
     * 用户密码
     */
    @TableField(value = "user_password")
    private String userPassword;

    /**
     * 用户密码
     */
    @TableField(value = "user_cipher_password")
    private String userCipherPassword;

    /**
     * 用户组织id
     */
    @TableField(value = "user_org_id")
    private String userOrgId;

    /**
     * 用户组织名称
     */
    @TableField(value = "user_org_name")
    private String userOrgName;

    /**
     * 用户组织编码全路径
     */
    @TableField(value = "user_org_num_full_path")
    private String userOrgNumFullPath;

    /**
     * 用户组织名称全路径
     */
    @TableField(value = "user_org_name_full_path")
    private String userOrgNameFullPath;

    /**
     * 兼职组织
     */
    @TableField(value = "user_parttime_org")
    private String userParttimeOrg;

    /**
     * 用户性别：1-男 2-女
     */
    @TableField(value = "user_gender")
    private String userGender;

    /**
     * 用户对象class
     */
    @TableField(value = "user_objectclass")
    private String userObjectclass;

    /**
     * 用户类型：E1-内，E2-外
     */
    @TableField(value = "user_type")
    private String userType;

    /**
     * 用户sso权限
     */
    @TableField(value = "user_sso_authority")
    private String userSsoAuthority;

    /**
     * 用户sso权限backup
     */
    @TableField(value = "user_sso_authority_back_up")
    private String userSsoAuthorityBackUp;

    /**
     * 用户修改时间
     */
    @TableField(value = "user_normal_modify_timestamp")
    private String userNormalModifyTimestamp;

    /**
     * 数据入库时间
     */
    @TableField(value = "sync_create_time")
    private LocalDateTime syncCreateTime;

    /**
     * 数据更新时间
     */
    @TableField(value = "sync_update_time")
    private LocalDateTime syncUpdateTime;

}



