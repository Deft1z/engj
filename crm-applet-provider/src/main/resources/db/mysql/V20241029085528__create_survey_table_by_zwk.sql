drop table if exists b_survey;
create table b_survey
(
    id             int auto_increment primary key comment '主键id',
    survey_code    varchar(50)  not null comment '表单编码',
    survey_name    varchar(100) not null comment '调查表单名称',
    remark         varchar(200) null comment '备注',
    create_user_id int          null comment '创建人ID',
    create_time    datetime     not null default current_timestamp comment '创建时间',
    modify_user_id int          null comment '更新人ID',
    modify_time    datetime     not null default current_timestamp on update current_timestamp comment '更新时间',
    flag           tinyint      not null default 1 comment '数据状态：-1-删除，1-正常',
    tenant_id      int          not null default 1 comment '租户id'
) engine = innodb
  default charset = utf8mb4
  collate = utf8mb4_general_ci comment ='调查表单表';
alter table b_survey add unique (survey_code, tenant_id);

insert into b_survey (id, survey_code, survey_name, remark)
values (1, 'customer-satisfaction', '客户满意度调查', '用电业务客户满意度调查表单');

drop table if exists b_survey_item;
create table b_survey_item
(
    id             int auto_increment primary key comment '主键id',
    survey_id      int          not null comment '调查表单id',
    item_name      varchar(200) not null comment '表单项名称',
    item_type      varchar(20)  not null default 'text' comment '表单项类型：title-标题，text-文本，radio-单选，checkbox-多选，select-下拉框，date-日期，time-时间，datetime-日期时间，number-数字，file-附件',
    required       tinyint      not null default 0 comment '是否必填：0-否，1-是',
    parent_id      int          null comment '父级id',
    priority       int          not null default 1 comment '优先级',
    fill_by        varchar(20)  not null default 'all' comment '可填写人：all-所有人, promoter-发起人, invitee-受邀请人',
    general_biz_key varchar(30) null comment '通用业务字段key：survey_obj_code-业务对象编码，survey_obj_name-业务对象名称',
    create_user_id int          null comment '创建人ID',
    create_time    datetime     not null default current_timestamp comment '创建时间',
    modify_user_id int          null comment '更新人ID',
    modify_time    datetime     not null default current_timestamp on update current_timestamp comment '更新时间',
    flag           tinyint      not null default 1 comment '数据状态：-1-删除，1-正常',
    tenant_id      int          not null default 1 comment '租户id'
) engine = innodb
  default charset = utf8mb4
  collate = utf8mb4_general_ci comment ='调查表单项表';
alter table b_survey_item add unique (survey_id, general_biz_key, tenant_id);

insert into b_survey_item (id, survey_id, item_name, item_type, required, parent_id, priority, fill_by, general_biz_key)
values (1, 1, '项目编号', 'text', true, null, 1, 'promoter', 'survey_obj_code')
     , (2, 1, '项目名称', 'text', true, null, 2, 'promoter', 'survey_obj_name')
     , (3, 1, '客户名称', 'text', false, null, 3, 'all', null)
     , (4, 1, '客户联系电话', 'text', false, null, 4, 'all', null)
     , (5, 1, '设备地址', 'text', false, null, 5, 'promoter', null)
     , (6, 1, '填表日期', 'date', false, null, 6, 'all', null)
     , (7, 1, '回访人员', 'text', false, null, 7, 'promoter', null)
     , (8, 1, '回访形式', 'radio', true, null, 8, 'promoter', null)
     , (9, 1, '总体评价', 'title', false, null, 9, 'invitee', null)
     , (10, 1, '对维护过程及结果是否满意', 'radio', true, 9, 1, 'invitee', null)
     , (11, 1, '对整体的服务工作是否满意', 'radio', true, 9, 2, 'invitee', null)
     , (12, 1, '业务跟进服务评价', 'title', false, null, 10, 'invitee', null)
     , (13, 1, '业务人员工作态度', 'radio', true, 12, 1, 'invitee', null)
     , (14, 1, '合同到期提醒沟通', 'radio', true, 12, 2, 'invitee', null)
     , (15, 1, '合同签订、发票开具时效', 'radio', true, 12, 3, 'invitee', null)
     , (16, 1, '客户需求处理情况', 'radio', true, 12, 4, 'invitee', null)
     , (17, 1, '设备维护服务评价', 'title', false, null, 11, 'invitee', null)
     , (18, 1, '技术人员工作态度', 'radio', true, 17, 1, 'invitee', null)
     , (19, 1, '维护工作安排', 'radio', true, 17, 2, 'invitee', null)
     , (20, 1, '巡视、检测过程', 'radio', true, 17, 3, 'invitee', null)
     , (21, 1, '设备异常情况处理', 'radio', true, 17, 4, 'invitee', null)
     , (22, 1, '工作报告接收', 'radio', true, 17, 5, 'invitee', null)
     , (23, 1, '是否存在违规收受其他费用的情况（对客户“吃拿卡要”，收受”茶水费”、“车马费”等费用）', 'radio', true, null, 12,'invitee', null)
     , (24, 1, '改进意见和建议', 'text', false, null, 13, 'invitee', null)
     , (25, 1, '客户意见处理情况', 'text', false, null, 14, 'promoter', null)
;

drop table if exists b_survey_item_option;
create table b_survey_item_option
(
    id             int auto_increment primary key comment '主键id',
    item_id        int          not null comment '调查表单项id',
    item_val       varchar(200) not null comment '表单项值',
    priority       int          not null default 1 comment '优先级',
    create_user_id int          null comment '创建人ID',
    create_time    datetime     not null default current_timestamp comment '创建时间',
    modify_user_id int          null comment '更新人ID',
    modify_time    datetime     not null default current_timestamp on update current_timestamp comment '更新时间',
    flag           tinyint      not null default 1 comment '数据状态：-1-删除，1-正常',
    tenant_id      int          not null default 1 comment '租户id'
) engine = innodb
  default charset = utf8mb4
  collate = utf8mb4_general_ci comment ='调查表单项选值表';

insert into b_survey_item_option(item_id, item_val, priority)
values (8, '上门回访', 1)
     , (8, '电话回访', 2)
     , (10, '满意', 1)
     , (10, '基本满意', 2)
     , (10, '不满意', 3)
     , (11, '满意', 1)
     , (11, '基本满意', 2)
     , (11, '不满意', 3)
     , (13, '满意', 1)
     , (13, '基本满意', 2)
     , (13, '不满意', 3)
     , (14, '满意', 1)
     , (14, '基本满意', 2)
     , (14, '不满意', 3)
     , (15, '满意', 1)
     , (15, '基本满意', 2)
     , (15, '不满意', 3)
     , (16, '满意', 1)
     , (16, '基本满意', 2)
     , (16, '不满意', 3)
     , (18, '满意', 1)
     , (18, '基本满意', 2)
     , (18, '不满意', 3)
     , (19, '满意', 1)
     , (19, '基本满意', 2)
     , (19, '不满意', 3)
     , (20, '满意', 1)
     , (20, '基本满意', 2)
     , (20, '不满意', 3)
     , (21, '满意', 1)
     , (21, '基本满意', 2)
     , (21, '不满意', 3)
     , (22, '满意', 1)
     , (22, '基本满意', 2)
     , (22, '不满意', 3)
     , (23, '否', 1)
     , (23, '是', 2)
;

drop table if exists b_survey_record;
create table b_survey_record
(
    id             int auto_increment primary key comment '主键id',
    survey_id      int          not null comment '调查表单id',
    survey_name    varchar(100) not null comment '调查表名称',
    fill_json      json         default null comment '发起人填写的表单内容',
    share_url      varchar(200) null comment '分享评价链接',
    share_expire_at datetime    null comment '分享链接过期时间',
    status         int          not null default 0 comment '0 未提交 1 待评价 2 已评价 3 已完成',
    create_user_id int          null comment '创建人ID',
    create_time    datetime     not null default current_timestamp comment '创建时间',
    modify_user_id int          null comment '更新人ID',
    modify_time    datetime     not null default current_timestamp on update current_timestamp comment '更新时间',
    flag           tinyint      not null default 1 comment '数据状态：-1-删除，1-正常',
    tenant_id      int          not null default 1 comment '租户id'
) engine = innodb
  default charset = utf8mb4
  collate = utf8mb4_general_ci comment ='调查表单记录表';


drop table if exists b_survey_record_answer;
create table b_survey_record_answer
(
    id               int auto_increment primary key comment '主键id',
    promoter_id      int          not null comment '发起人id',
    invitee_id       int          not null comment '受邀请人id',
    survey_record_id int          not null comment '调查表单记录id',
    survey_name      varchar(100) null comment '调查表单名称',
    fill_json      json           default null comment '受邀请人填写的表单内容',
    status         int            not null default 1 comment '0 未提交 1 待评价 2 已评价',
    create_user_id   int          null comment '创建人ID',
    create_time      datetime     not null default current_timestamp comment '创建时间',
    modify_user_id   int          null comment '更新人ID',
    modify_time      datetime     not null default current_timestamp on update current_timestamp comment '更新时间',
    flag             tinyint      not null default 1 comment '数据状态：-1-删除，1-正常',
    tenant_id        int          not null default 1 comment '租户id'
) engine = innodb
  default charset = utf8mb4
  collate = utf8mb4_general_ci comment ='调查表单记录填写表';


