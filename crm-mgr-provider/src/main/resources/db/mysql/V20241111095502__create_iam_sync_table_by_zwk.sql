drop table if exists iam_sync_log;
create table iam_sync_log
(
    id           bigint auto_increment primary key comment '主键id',
    sync_time    datetime(3) comment '同步时间',
    sync_name    varchar(200)  default null comment '同步数据名称',
    sync_content longtext      default null comment '同步内容',
    sync_result  varchar(2000) default null comment '同步结果',
    success_flag boolean       default null comment '成功标志'
) engine = innodb character set = utf8mb4 collate = utf8mb4_general_ci comment ='iam数据同步日志';

drop table if exists iam_user;
create table iam_user
(
    sim_id                       varchar(50) primary key comment '用户工号',
    user_id                      varchar(100) default null comment '用户id',
    user_cn                      varchar(100) default null comment '用户姓名',
    user_job_id                  varchar(30)  default null comment '岗位ID',
    user_status                  varchar(10)  default null comment '员工状态：1-有效 0-无效',
    user_mobile                  varchar(20)  default null comment '手机',
    user_id_card_number          varchar(50)  default null comment '证件类型',
    user_login_id                varchar(50)  default null comment '登录账号',
    user_init_mobile             varchar(10)  default null comment '用户手机初始化标识',
    user_init_passwd             varchar(10)  default null comment '用户密码初始化标识',
    user_password                varchar(100) default null comment '用户密码',
    user_cipher_password         varchar(100) default null comment '用户密码',
    user_org_id                  varchar(50)  default null comment '用户组织id',
    user_org_name                varchar(100) default null comment '用户组织名称',
    user_org_num_full_path       varchar(400) default null comment '用户组织编码全路径',
    user_org_name_full_path      varchar(800) default null comment '用户组织名称全路径',
    user_parttime_org            varchar(50)  default null comment '兼职组织',
    user_gender                  varchar(10)  default null comment '用户性别：1-男 2-女',
    user_objectclass             varchar(50)  default null comment '用户对象class',
    user_type                    varchar(10)  default null comment '用户类型：E1-内，E2-外',
    user_sso_authority           json         default null comment '用户sso权限',
    user_sso_authority_back_up   json         default null comment '用户sso权限backup',
    user_normal_modify_timestamp json         default null comment '用户修改时间',
    sync_create_time datetime     null comment '数据入库时间',
    sync_update_time datetime     null comment '数据更新时间'
) engine = innodb character set = utf8mb4 collate = utf8mb4_general_ci comment ='iam用户表';