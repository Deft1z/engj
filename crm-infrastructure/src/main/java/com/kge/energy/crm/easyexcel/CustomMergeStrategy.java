package com.kge.energy.crm.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.write.handler.RowWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义单元格合并策略
 */
public class CustomMergeStrategy implements RowWriteHandler {
    /**
     * 主键下标集合
     */
    private List<Integer> pkColumnIndex = new ArrayList<>();
 
    /**
     * 需要合并的列的下标集合
     */
    private List<Integer> needMergeColumnIndex = new ArrayList<>();
 
    /**
     * DTO数据类型
     */
    private Class<?> elementType;
 
    public CustomMergeStrategy(Class<?> elementType) {
        this.elementType = elementType;
    }
 
    @Override
    public void afterRowDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Integer relativeRowIndex, Boolean isHead) {
        // 如果是标题,则直接返回
        if (isHead) {
            return;
        }
 
        // 获取当前sheet
        Sheet sheet = writeSheetHolder.getSheet();

        if (pkColumnIndex.isEmpty()) {
            this.lazyInit();
        }
 
        // 判断是否需要和上一行进行合并
        // 不能和标题合并，只能数据行之间合并
        if (row.getRowNum() <= 1) {
            return;
        }
        // 获取上一行数据
        Row lastRow = sheet.getRow(row.getRowNum() - 1);
        // 将本行和上一行是同一类型的数据(通过主键字段进行判断)，则需要合并
        boolean margeBol = true;
        for (Integer pkIndex : pkColumnIndex) {
        	String lastKey = lastRow.getCell(pkIndex).getCellType() == CellType.STRING ? lastRow.getCell(pkIndex).getStringCellValue() : String.valueOf(lastRow.getCell(pkIndex).getNumericCellValue());
        	String currentKey = row.getCell(pkIndex).getCellType() == CellType.STRING ? row.getCell(pkIndex).getStringCellValue() : String.valueOf(row.getCell(pkIndex).getNumericCellValue());
        	if (!StringUtils.equalsIgnoreCase(lastKey, currentKey)) {
        		margeBol = false;
        		break;
        	}
        }
        if (margeBol) {
            for (Integer needMerIndex : needMergeColumnIndex) {
                CellRangeAddress cellRangeAddress = new CellRangeAddress(row.getRowNum() - 1, row.getRowNum(),
                        needMerIndex, needMerIndex);
                sheet.addMergedRegionUnsafe(cellRangeAddress);
            }
        }
    }
 
    /**
     * 初始化主键下标和需要合并字段的下标
     */
    private void lazyInit() {
 
        // 获取DTO的类型
        Class<?> eleType = this.elementType;
 
        // 获取DTO所有的属性
        Field[] fields = eleType.getDeclaredFields();
 
 		int i = 0;
        // 遍历所有的字段，因为是基于DTO的字段来构建excel，所以字段数 >= excel的列数
        for (Field theField : fields) {
            // 获取@ExcelProperty注解，用于获取该字段对应在excel中的列的下标
            ExcelProperty easyExcelAnno = theField.getAnnotation(ExcelProperty.class);
            // 为空,则表示该字段不需要导入到excel,直接处理下一个字段
            if (null == easyExcelAnno) {
                continue;
            }
            // 获取自定义的注解，用于合并单元格
            CustomMerge customMerge = theField.getAnnotation(CustomMerge.class);
 
            // 没有@CustomMerge注解的默认不合并
            if (null != customMerge) {
                // 判断是否有主键标识
                if (customMerge.isPk()) {
                    pkColumnIndex.add(i);
                }

                // 判断是否需要合并
                if (customMerge.needMerge()) {
                    needMergeColumnIndex.add(i);
                }
                i++;
            }
        }
 
        // 没有指定主键，则异常
        /*if (pkColumnIndex.isEmpty()) {
            throw new IllegalStateException("使用@CustomMerge注解必须指定主键");
        }*/
 
    }
}