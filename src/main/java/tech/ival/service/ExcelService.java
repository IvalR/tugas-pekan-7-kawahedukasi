package tech.ival.service;

import io.vertx.core.json.JsonObject;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import tech.ival.models.KebunModel;

import javax.enterprise.context.ApplicationScoped;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ExcelService {

    public JsonObject generateExcel() throws IOException {
        List<KebunModel> kebunList = KebunModel.listAll();

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Laporan Bulanan");

        int rownum = 0 ;
        Row row = sheet.createRow(rownum++);
        row.createCell(0).setCellValue("id");
        row.createCell(1).setCellValue("komoditas");
        row.createCell(2).setCellValue("total");
        row.createCell(3).setCellValue("create_at");
        row.createCell(4).setCellValue("update_at");

        for (KebunModel kebunModel : kebunList){
            row = sheet.createRow(rownum++);
            row.createCell(0).setCellValue(String.valueOf(kebunModel.id));
            row.createCell(1).setCellValue(kebunModel.komoditas);
            row.createCell(2).setCellValue(kebunModel.total);
            row.createCell(3).setCellValue(kebunModel.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss")));
            row.createCell(4).setCellValue(kebunModel.getUpdatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss")));
        }

        OutputStream outputStream = null;
        String filename = "Laporan-Bulanan-" + UUID.randomUUID()+".xlsx";
        outputStream = new FileOutputStream(filename);
        workbook.write(outputStream);

//        return Response.ok()
//                .type("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
//                .header("Conten-Disposition", "attachment; filename=\"laporan-bulanan.xlsx\"")
//                .entity(outputStream)
//                .build();
        JsonObject result = new JsonObject();
        result.put("fileName",filename);
        return result;
    }
}
