package namviet.rfid_api.serviceImpl;

import lombok.RequiredArgsConstructor;
import namviet.rfid_api.dto.DongGoiDTO;
import namviet.rfid_api.entity.DonHangSanPham;
import namviet.rfid_api.entity.DongGoi;
import namviet.rfid_api.exception.CustomException;
import namviet.rfid_api.mapper.DongGoiMapper;
import namviet.rfid_api.repository.DonHangSanPhamRepository;
import namviet.rfid_api.repository.DongGoiRepository;
import namviet.rfid_api.service.DongGoiService;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DongGoiServiceImpl implements DongGoiService {
    private final DongGoiRepository dongGoiRepository;
    private final DongGoiMapper dongGoiMapper;
    private final DonHangSanPhamRepository donHangSanPhamRepository;

    @Override
    public DongGoiDTO createDongGoi(DongGoiDTO dongGoiDTO) {
        DongGoi dongGoi = dongGoiMapper.toEntity(dongGoiDTO);
        return dongGoiMapper.toDTO(dongGoiRepository.save(dongGoi));
    }

    @Override
    public List<DongGoiDTO> getAllDongGoi() {
        return dongGoiRepository.findAll()
                .stream()
                .map(dongGoiMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DongGoiDTO getDongGoiById(Integer id) {
        DongGoi dongGoi = dongGoiRepository.findById(id)
                .orElseThrow(() -> new CustomException("DongGoi not found", HttpStatus.NOT_FOUND));
        return dongGoiMapper.toDTO(dongGoi);
    }

    @Override
    public DongGoiDTO updateDongGoi(Integer id, DongGoiDTO dongGoiDTO) {
        DongGoi existingDongGoi = dongGoiRepository.findById(id)
                .orElseThrow(() -> new CustomException("DongGoi not found", HttpStatus.NOT_FOUND));
        DongGoi updatedDongGoi = dongGoiMapper.toEntity(dongGoiDTO);
        updatedDongGoi.setDongGoiId(existingDongGoi.getDongGoiId());
        return dongGoiMapper.toDTO(dongGoiRepository.save(updatedDongGoi));
    }

    @Override
    public void deleteDongGoi(Integer id) {
        if (!dongGoiRepository.existsById(id)) {
            throw new CustomException("DongGoi not found", HttpStatus.NOT_FOUND);
        }
        dongGoiRepository.deleteById(id);
    }

    @Override
    public Page<DongGoiDTO> getDongGoiPagination(Pageable pageable) {
        return dongGoiRepository.findAll(pageable).map(dongGoiMapper::toDTO);
    }

    @Override
    public Page<DongGoiDTO> searchWithFTSService(String searchText, Pageable pageable) {
        Page<DongGoi> dongGoiPage = dongGoiRepository.searchWithFTS(searchText, pageable);
        return dongGoiPage.map(dongGoiMapper::toDTO);
    }

    @Override
    public List<DongGoiDTO> findDongGoiByMaLenh(String MaLenh) {
        return dongGoiRepository.findByDonHangSanPhamDonHangMaLenhContaining(MaLenh)
                .stream()
                .map(dongGoiMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Resource exportPackingList(int soCuonTrongThung,int soPcsTrenCuon, String maLenh) {

        List<DongGoi> dongGoiList = dongGoiRepository.findByMaLenh(maLenh);
        List<DonHangSanPham> donHangSanPhamList = donHangSanPhamRepository.findByDonHangMaLenhContaining(maLenh);

        if(dongGoiList.isEmpty() || donHangSanPhamList.isEmpty()) {
            throw new CustomException("DongGoi or DonHangSanPham not found", HttpStatus.NOT_FOUND);
        }

        try (Workbook workbook = new SXSSFWorkbook(100); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
        //Header sheet Roll Packing List Start
            var sheet = workbook.createSheet("Roll Packing List");
            var headerRow = sheet.createRow(0);
            String[] headers = {"Roll No", "Start Index", "End Index", "PO", "UPC", "Size label", "Quantity","Nhân Viên","Máy","CodeTP","Cnt 1","Cnt 2","Cnt 3","Cnt 4","Cnt 5","Cnt 6","Cnt 7","Cnt 8","Cnt 9"};
            for (int i = 0; i < headers.length; i++) {
                var cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
        //Header sheet Roll Packing List End

        //Header sheet Box Packing List Start
            var sheetBox = workbook.createSheet("Box Packing List");
            var headerRowBox = sheetBox.createRow(0);
            String[] headersBox = {
                    "Box No", "PO", "Size label", "UPC",
                    "Roll No (1)", "Start Index (1)", "End Index (1)", "Quantity (1)",
                    "Roll No (2)", "Start Index (2)", "End Index (2)", "Quantity (2)",
                    "Roll No (3)", "Start Index (3)", "End Index (3)", "Quantity (3)",
                    "Roll No (4)", "Start Index (4)", "End Index (4)", "Quantity (4)",
                    "Roll No (5)", "Start Index (5)", "End Index (5)", "Quantity (5)",
                    "Roll No (6)", "Start Index (6)", "End Index (6)", "Quantity (6)",
                    "Roll No (7)", "Start Index (7)", "End Index (7)", "Quantity (7)",
                    "Roll No (8)", "Start Index (8)", "End Index (8)", "Quantity (8)",
                    "Roll No (9)", "Start Index (9)", "End Index (9)", "Quantity (9)",
                    "Roll No (10)", "Start Index (10)", "End Index (10)", "Quantity (10)",
                    "Total Quantity","Cnt 1","Cnt 2","Cnt 3","Cnt 4","Cnt 5","Cnt 6","Cnt 7","Cnt 8","Cnt 9"
            };
            for (int i = 0; i < headersBox.length; i++) {
                var cell = headerRowBox.createCell(i);
                cell.setCellValue(headersBox[i]);
            }
        //Header sheet Box Packing List End

        //Handle data for Box Packing List start
            int rowNumBox = 1;
            int boxNo = 1;
            for (DonHangSanPham dhs : donHangSanPhamList) {
                int tongSoPcs = dhs.getSoLuong();

                for (int j = tongSoPcs; j > 0; j--) {
                    var row = sheetBox.createRow(rowNumBox++);
                    int tongPcsTrongThung = 0;

                    row.createCell(0).setCellValue(boxNo++);
                    row.createCell(1).setCellValue(dhs.getDonHang().getPo());
                    row.createCell(2).setCellValue(dhs.getSanPham().getKichThuoc());
                    row.createCell(3).setCellValue(dhs.getSanPham().getUpc().getUpc());

                    int RollNo = 1;
                    int cellNum = 4;
                    int indexStart = 1;
                    int indexEnd;

                    for (int i = 0; i < soCuonTrongThung; i++) {
                        int soPcs = Math.min(tongSoPcs, soPcsTrenCuon);
                        tongSoPcs -= soPcs;

                        row.createCell(cellNum++).setCellValue(RollNo++);
                        row.createCell(cellNum++).setCellValue(indexStart);
                        indexEnd = indexStart + soPcs - 1;
                        row.createCell(cellNum++).setCellValue(indexEnd);
                        row.createCell(cellNum++).setCellValue(soPcs);

                        tongPcsTrongThung += soPcs;
                        indexStart = indexEnd + 1;

                        if (tongSoPcs <= 0) {
                            break;
                        }
                    }
                    row.createCell(44).setCellValue(tongPcsTrongThung);

                    int cellNumContent = 45;
                    String noiDungIn = dhs.getSanPham().getContent();
                    String[] noiDungInArray = noiDungIn.split("\\|");
                    for(String noiDung: noiDungInArray) {
                        row.createCell(cellNumContent++).setCellValue(noiDung);
                    }

                    if (tongSoPcs <= 0) {
                        break;
                    }
                }
            }
        //Handle data for Box Packing List end

        //Handle data for Roll Packing List start
            int rowNum = 1;
            for(DongGoi dongGoi: dongGoiList) {
                int cellNum = 10;
                var row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(dongGoi.getIndexCuon());
                row.createCell(1).setCellValue(dongGoi.getStartIndex());
                row.createCell(2).setCellValue(dongGoi.getEndIndex());
                row.createCell(3).setCellValue(dongGoi.getDonHangSanPham().getDonHang().getPo());
                row.createCell(4).setCellValue(dongGoi.getDonHangSanPham().getSanPham().getUpc().getUpc());
                row.createCell(5).setCellValue(dongGoi.getDonHangSanPham().getSanPham().getKichThuoc());
                row.createCell(6).setCellValue(dongGoi.getSoPcsTot());
                row.createCell(7).setCellValue(dongGoi.getNhanVien().getHoTen());
                row.createCell(8).setCellValue(dongGoi.getThietBi().getTenMay());
                row.createCell(9).setCellValue(dongGoi.getCode());

                String noiDungIn = dongGoi.getDonHangSanPham().getSanPham().getContent();
                String[] noiDungInArray = noiDungIn.split("\\|");
                for(String noiDung: noiDungInArray) {
                    row.createCell(cellNum++).setCellValue(noiDung);
                }
            }
        //Handle data for Roll Packing List end



            System.out.println(dongGoiList);

            // Write the workbook to the output stream
            workbook.write(out);
            return new ByteArrayResource(out.toByteArray());
        } catch (IOException e) {
            throw new CustomException("Error generating Excel file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void createAllDongGoi(String maLenh, int soPcsTrenCuon) {
        List<DonHangSanPham> donHangSanPhamList = donHangSanPhamRepository.findByDonHangMaLenhContaining(maLenh);

        if(donHangSanPhamList.isEmpty()) {
            throw new CustomException("DonHangSanPham not found", HttpStatus.NOT_FOUND);
        }

        for(DonHangSanPham dhsp : donHangSanPhamList) {
            int soLuong = dhsp.getSoLuong();
            int soCuon = (int) Math.ceil((double) soLuong / soPcsTrenCuon);

            for (int i = 0; i < soCuon; i++) {
                DongGoi dongGoi = new DongGoi();
                int soPcs = (i == soCuon - 1) ? soLuong - (soPcsTrenCuon * i) : soPcsTrenCuon;
                dongGoi.setSoPcsTot(soPcs);
                dongGoi.setDonHangSanPham(dhsp);
                dongGoi.setNhanVien(dhsp.getDonHang().getNhanVien());
                dongGoi.setIndexCuon(i + 1);
                dongGoi.setStartIndex(i * soPcsTrenCuon + 1);
                dongGoi.setEndIndex(i * soPcsTrenCuon + soPcs);
                dongGoiRepository.save(dongGoi);
            }
        }
    }
}
