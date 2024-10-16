package com.kge.energy.crm.common.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.converters.longconverter.LongStringConverter;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ExcelUtils {

    private ExcelUtils() {
    }

    /**
     * 将列表以 Excel 响应给前端
     *
     * @param response  响应
     * @param filename  文件名
     * @param sheetName Excel sheet 名
     * @param head      Excel head 头
     * @param data      数据列表哦
     * @param <T>       泛型，保证 head 和 data 类型的一致性
     * @throws IOException 写入失败的情况
     */
    public static <T> void write(HttpServletResponse response, String filename, String sheetName,
                                 Class<T> head, List<T> data) throws IOException {
        // 设置基础样式
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(getHeadStyle(), getContentStyle());

        // 设置 header 和 contentType。写在最后的原因是，避免报错时，响应 contentType 已经被修改了
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8.name()));
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        // 输出 Excel
        EasyExcel.write(response.getOutputStream(), head)
                .autoCloseStream(false) // 不要自动关闭，交给 Servlet 自己处理
                .registerWriteHandler(horizontalCellStyleStrategy) //自定义样式
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) // 基于 column 长度，自动适配。最大 255 宽度
                .registerConverter(new LongStringConverter()) // 避免 Long 类型丢失精度
                .sheet(sheetName).doWrite(data);

    }

    public static <T> List<T> read(MultipartFile file, Class<T> head) throws IOException {
        return EasyExcel.read(file.getInputStream(), head, null)
                .autoCloseStream(false)  // 不要自动关闭，交给 Servlet 自己处理
                .doReadAllSync();
    }

    /**
     * 头部样式
     */
    private static WriteCellStyle getHeadStyle() {
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 背景颜色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);

        // 字体
        WriteFont headWriteFont = new WriteFont();
        //设置字体名字
        headWriteFont.setFontName("微软雅黑");
        //设置字体大小
        headWriteFont.setFontHeightInPoints((short) 10);
        //字体加粗
        headWriteFont.setBold(true);
        //在样式用应用设置的字体
        headWriteCellStyle.setWriteFont(headWriteFont);

        // 边框样式
        setBorderStyle(headWriteCellStyle);

        //设置自动换行
        headWriteCellStyle.setWrapped(true);

        //设置水平对齐的样式为居中对齐
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        //设置垂直对齐的样式为居中对齐
        headWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        //设置文本收缩至合适
        headWriteCellStyle.setShrinkToFit(true);

        return headWriteCellStyle;
    }

    /**
     * 内容样式
     */
    private static WriteCellStyle getContentStyle() {
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();

        // 背景白色
        // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.头默认了 FillPatternType所以可以不指定
        contentWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);

        // 设置字体
        WriteFont contentWriteFont = new WriteFont();
        //设置字体大小
        contentWriteFont.setFontHeightInPoints((short) 10);
        //设置字体名字
        contentWriteFont.setFontName("宋体");
        //在样式用应用设置的字体
        contentWriteCellStyle.setWriteFont(contentWriteFont);

        //设置样式
        setBorderStyle(contentWriteCellStyle);

        // 水平左对齐
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
        // 垂直居中
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置自动换行
        contentWriteCellStyle.setWrapped(true);

        //设置文本收缩至合适
        //contentWriteCellStyle.setShrinkToFit(true);

        return contentWriteCellStyle;
    }

    /**
     * 边框样式
     */
    private static void setBorderStyle(WriteCellStyle cellStyle) {
        //设置底边框
        cellStyle.setBorderBottom(BorderStyle.THIN);
        //设置底边框颜色
        cellStyle.setBottomBorderColor(IndexedColors.BLACK1.getIndex());
        //设置左边框
        cellStyle.setBorderLeft(BorderStyle.THIN);
        //设置左边框颜色
        cellStyle.setLeftBorderColor(IndexedColors.BLACK1.getIndex());
        //设置右边框
        cellStyle.setBorderRight(BorderStyle.THIN);
        //设置右边框颜色
        cellStyle.setRightBorderColor(IndexedColors.BLACK1.getIndex());
        //设置顶边框
        cellStyle.setBorderTop(BorderStyle.THIN);
        //设置顶边框颜色
        cellStyle.setTopBorderColor(IndexedColors.BLACK1.getIndex());
    }

}
