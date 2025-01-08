# 客户服务后端服务

## 模块说明

- crm-infrastructure：基础建设层，封装通用工具类、mysql、redis、es、外部服务调用
- crm-domain：通用业务处理层，沉淀通用业务实现代码
- crm-applet-provider：小程序应用层
- crm-mgr-provider：后台管理系统应用层

## 代码质量扫描 SonarQube
> 使用 Maven 扫描：
> * 使用 IDEA 打开 Terminal 终端，在 根目录 下直接执行 mvn clean verify sonar:sonar -DskipTests 命令。
> * 指定参数命令：mvn clean verify sonar:sonar -Dsonar.projectName='crm-service' -Dsonar.host.url=http://172.18.26.131:9010 -Dsonar.token=sqp_42d59c2962c88e57d5aa65ce8da43ad965802c1f -DskipTests
> * 扫描结果访问 http://172.18.26.131:9010 查看 (默认账号密码：admin/bitnami)

## API规范 
使用OpenAPI3的规范注解，添加在实体类和REST接口，示例代码如下：  
```java
@Data
@Schema(name = "文件name",description = "文件对象")
public class FileResp {
    @Schema(description = "随机名称",requiredMode = Schema.RequiredMode.REQUIRED)
    private String random;
    @Schema(description = "文件名称")
    private String name;
    @Schema(description = "文件大小")
    private Long size;
    @Schema(description = "是否上传成功")
    private Boolean success;
}
```

```java
@RestController
@RequestMapping("/api")
@Tag(name = "规范示例API")
public class BodyController {

    @Operation(summary = "普通body请求")
    @PostMapping("/body")
    public ResponseEntity<FileResp> body(@RequestBody FileResp fileResp){
        return ResponseEntity.ok(fileResp);
    }

    @Operation(summary = "普通body请求-put")
    @PutMapping("/bodyPut")
    public ResponseEntity<FileResp> bodyPut(@RequestBody FileResp fileResp){
        return ResponseEntity.ok(fileResp);
    }

    @Operation(summary = "普通body请求+Param")
    @PostMapping("/bodyParam")
    public ResponseEntity<FileResp> bodyParam(@RequestParam("name")String name,@RequestBody FileResp fileResp){
        fileResp.setName(fileResp.getName()+",receiveName:"+name);
        return ResponseEntity.ok(fileResp);
    }

    @Operation(summary = "普通body请求+Param+Header",description = "")
    @Parameters({
            @Parameter(name = "token",example = "1",description = "请求token",required = true,in = ParameterIn.HEADER),
            @Parameter(name = "name",description = "文件名称",required = true,in=ParameterIn.QUERY)
    })
    @PostMapping("/bodyParamHeader")
    public ResponseEntity<FileResp> bodyParamHeader(@RequestHeader("token") String token, @RequestParam("name")String name,@RequestBody FileResp fileResp){
        fileResp.setName(fileResp.getName()+",receiveName:"+name+",token:"+token);
        return ResponseEntity.ok(fileResp);
    }

    @Operation(summary = "普通body请求+Param+Header+Path")
    @Parameters({
            @Parameter(name = "id",description = "文件id",in = ParameterIn.PATH),
            @Parameter(name = "token",description = "请求token",required = true,in = ParameterIn.HEADER),
            @Parameter(name = "name",description = "文件名称",required = true,in=ParameterIn.QUERY)
    })
    @PostMapping("/bodyParamHeaderPath/{id}")
    public ResponseEntity<FileResp> bodyParamHeaderPath(@PathVariable("id") String id,@RequestHeader("token") String token, @RequestParam("name")String name,@RequestBody FileResp fileResp){
        fileResp.setName(fileResp.getName()+",receiveName:"+name+",token:"+token+",pathID:"+id);
        return ResponseEntity.ok(fileResp);
    }

}
```