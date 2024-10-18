package com.kge.energy.crm.repository.entityext.result;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class BindUserResult {

    private Integer userId;
    private String realname;
    private String mobile;
    private List<RelateApp> relateApp;

    @Data
    public static class RelateApp {
        private Integer userId;
        private Integer openidId;
        private Integer appId;
        private String appName;
        private List<RelateProject> relateProject;
    }

    @Data
    public static class RelateProject {
        private Integer projectId;
        private String projectName;
    }

}
