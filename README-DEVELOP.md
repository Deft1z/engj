## 注解类

- @ConvertToGoFormats：兼容旧的 Go 接口响应字段格式，新接口无需添加此注解

## 工具类

- [UserInfoContextUtils.java](crm-infrastructure%2Fsrc%2Fmain%2Fjava%2Fcom%2Fkge%2Fenergy%2Fcrm%2Fcommon%2Futil%2FUserInfoContextUtils.java)
  ：获取用户信息工具类
- [AuthVerifyUtils.java](crm-infrastructure%2Fsrc%2Fmain%2Fjava%2Fcom%2Fkge%2Fenergy%2Fcrm%2Fcommon%2Futil%2FAuthVerifyUtils.java)
  ：用户权限校验工具类

## 异常处理

- ServiceException：新实现代码抛出此异常，BadExecption 后面将剔除
