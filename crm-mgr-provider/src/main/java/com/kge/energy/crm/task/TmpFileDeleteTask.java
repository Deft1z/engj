package com.kge.energy.crm.task;

import com.kge.energy.crm.common.util.ExcelUtils;
import com.kge.platform.framework.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

/**
 * 删除临时数据文件任务
 * @author zhengwenke
 */
@Component
@Slf4j
public class TmpFileDeleteTask {

    /**
     * 每天00:30执行删除任务，删除tmp/crm-excel-pdf目录下的临时数据文件，避免磁盘空间占用
     */
    @Scheduled(cron = "${tmp.clean-cron:0 30 0 * * ?}")
    public void deleteTmpFileTask() {
        final String tmpDir = ExcelUtils.TMP_DIR;
        final int expireDay = 1;
        log.info("==> 执行{}目录的文件(最新修改时间为{}天以前)删除", tmpDir, expireDay);
        File file = new File(tmpDir);
        Date expireDate = DateUtils.addDays(new Date(), -expireDay);
        deleteExpiredFile(file, expireDate);
        log.info("<== 执行过期文件删除任务完成");
    }

    private void deleteExpiredFile(File file, Date expireDate) {
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            determineExpiredFile(file, expireDate);
        } else {
            for (File f : file.listFiles()) {
                deleteExpiredFile(f, expireDate);
            }
        }
    }

    private void determineExpiredFile(File file, Date expireDate) {
        Date lastModifiedTime = new Date(file.lastModified());
        if (lastModifiedTime.before(expireDate)) {
            String absolutePath = file.getAbsolutePath();
            try {
                Files.delete(file.toPath());
                log.info("==> [{}]-[{}] delete succeed!", absolutePath, DateFormatUtils.format(lastModifiedTime, "yyyy/MM/dd HH:mm"));
            } catch (IOException e) {
                throw new ServiceException("==> 文件删除失败: " + e.getMessage());
            }
        }
    }

}