package namviet.rfid_api.serviceImpl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import namviet.rfid_api.entity.DonHangSanPham;
import namviet.rfid_api.entity.Dulieu;
import namviet.rfid_api.entity.SanPham;
import namviet.rfid_api.entity.Upc;
import namviet.rfid_api.exception.CustomException;
import namviet.rfid_api.repository.DonHangSanPhamRepository;
import namviet.rfid_api.repository.DuLieuRepository;
import namviet.rfid_api.repository.UpcRepository;
import namviet.rfid_api.service.DuLieuService;
import namviet.rfid_api.utils.Decoder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static namviet.rfid_api.utils.Encoder.taoEPCtheoChuanGS1;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DuLieuServiceImpl implements DuLieuService {

    final DonHangSanPhamRepository donHangSanPhamRepository;
    final UpcRepository upcRepository;
    final DuLieuRepository duLieuRepository;

    @Override
    @Transactional
    public boolean taoFileData(int donHangSanPhamID) {

        if(donHangSanPhamID < 0){
            throw  new CustomException("don Hang San Pham Id Null",HttpStatus.BAD_REQUEST);
        }
        List<DonHangSanPham> dsDonHangSanPham = donHangSanPhamRepository.findByDonHangDonHangId(donHangSanPhamID);

        if(dsDonHangSanPham.size() <= 0 ){
            throw new CustomException("Id Don Hang Khong Ton Tai",HttpStatus.BAD_REQUEST);
        }

        for (DonHangSanPham donHangSanPham : dsDonHangSanPham) {
            SanPham sanPham = donHangSanPham.getSanPham();
            int soLuong = donHangSanPham.getSoLuong() + soLuongThem(donHangSanPham.getSoLuong());

            List<Dulieu> dulieus = new ArrayList<>();
            List<Upc> upcList = new ArrayList<>();

            String contents = sanPham.getContent();
            String[] content = contents.split("\\|");

            for (int i = 0; i < soLuong; i++) {
                long serial = sanPham.getUpc().getSerial();
                String epc = taoEPCtheoChuanGS1(sanPham);

                SanPham sanPhamDecoder = Decoder.epcToDulieu(epc);

                Dulieu dulieu = new Dulieu(epc,sanPham.getSku(),sanPham,donHangSanPham.getDonHang(),donHangSanPham);
                Upc upcExcel = new Upc(sanPhamDecoder.getUpc().getUpc(),sanPhamDecoder.getUpc().getSerial());

                try{
                    duLieuRepository.save(dulieu);
                    dulieus.add(dulieu);
                    upcList.add(upcExcel);
                    serial += 1;
                    sanPham.getUpc().setSerial(serial);
                }catch (Exception e){
                    throw new CustomException(String.valueOf(e), HttpStatus.BAD_REQUEST);
                }

                if (i == soLuong - 1) {
                    Upc upcSave = new Upc(sanPham.getUpc().getUpcId(),sanPham.getUpc().getUpc(), serial);
                    updateSerial(upcSave);
                }
            }
            exportToExcel(donHangSanPham.getTenFile(),dulieus,upcList,content);
        }
        return true;
    }

    @Override
    public SanPham decoderEpc(String epc) {
        if(epc == null){
            throw new CustomException("epc null",HttpStatus.BAD_REQUEST);
        }
        return Decoder.epcToDulieu(epc);
    }

    /**
     * @param fileName data upcs
     * xuất file excel tại dịa chi cố định
     * **/
    private void exportToExcel(String fileName, List<Dulieu> data, List<Upc> upcs, String[] content) {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Data");

            String[] columns = {"STT", "EPC", "UPC", "Serial"};

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.length + content.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(i < columns.length ? columns[i] : "Content " + (i - columns.length + 1));
            }

            int rowNum = 1;
            for (int i = 0; i < data.size(); i++) {
                Row row = sheet.createRow(rowNum++);

                Dulieu dulieu = data.get(i);
                Upc upc = i < upcs.size() ? upcs.get(i) : null;

                row.createCell(0).setCellValue(rowNum - 1);
                row.createCell(1).setCellValue(dulieu.getEpc());
                row.createCell(2).setCellValue(upc != null ? upc.getUpc() : "N/A");
                row.createCell(3).setCellValue(upc != null ? String.valueOf(upc.getSerial()) : "N/A");

                for (int j = 0; j < content.length; j++) {
                    row.createCell(columns.length + j).setCellValue(content[j]);
                }
            }

            String relativePath = "src/main/java/namviet/rfid_api/fileExport";
            String absolutePath = System.getProperty("user.dir") + File.separator + relativePath;
            File directory = new File(absolutePath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            try (FileOutputStream fileOut = new FileOutputStream(new File(directory, fileName))) {
                workbook.write(fileOut);
                System.out.println("Xuất file Excel thành công tại: " + directory.getAbsolutePath());
            }
        } catch (IOException e) {
            throw new CustomException("Lỗi khi ghi file Excel: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * hàm cập nhật upc sau khi tạo epc cuối cùng
     *
     * @param upc
     *
     * hàm này nhận upc và cập nhật lại serial cho upc **/
    private void updateSerial(Upc upc){
        if(upc == null){
            throw new CustomException("upc null",HttpStatus.BAD_REQUEST);
        }

        try{
            upcRepository.save(upc);
        }catch (Exception e){
            throw new CustomException(String.valueOf(e), HttpStatus.BAD_REQUEST);
        }
    }

    private int soLuongThem(int soLuong) {
        if (soLuong < 0) {
            throw new CustomException("Số lượng phải lớn hơn 0", HttpStatus.BAD_REQUEST);
        }
        int soLuongPhanTram = (int) Math.round(soLuong * 10.0 / 100);
        return Math.max(soLuongPhanTram, 100);
    }

}
