package namviet.rfid_api.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import namviet.rfid_api.dto.DonHangSanPhamDTO;
import namviet.rfid_api.dto.SanPhamDTO;
import namviet.rfid_api.entity.DonHang;
import namviet.rfid_api.entity.DonHangSanPham;
import namviet.rfid_api.entity.Dulieu;
import namviet.rfid_api.entity.SanPham;
import namviet.rfid_api.exception.CustomException;
import namviet.rfid_api.mapper.*;
import namviet.rfid_api.repository.*;
import namviet.rfid_api.service.DonHangSanPhamService;
import namviet.rfid_api.utils.ConvertToHex;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class DonHangSanPhamServiceImpl implements DonHangSanPhamService {

    final DonHangSanPhamRepository donHangSanPhamRepository;
    final DonHangSanPhamMapper donHangSanPhamMapper;
    final DonHangRepository donHangRepository;
    final SanPhamMapper sanPhamMapper;
    final DonHangMapper donHangMapper;
    final ThietBiMapper thietBiMapper;
    final NhanVienMapper nhanVienMapper;
    final UpcRepository upcRepository;
    final SanPhamRepository sanPhamRepository;
    final DuLieuRepository duLieuRepository;

    @Override
    public List<DonHangSanPhamDTO> dSSanPhamTheoDonHang(String maLenh) {
        List<DonHangSanPham> dSDonHangSP = donHangSanPhamRepository.findByDonHangMaLenhContaining(maLenh);
        return dSDonHangSP.stream()
                .map(donHangSanPhamMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DonHangSanPhamDTO> dSTatCaSanPham() {
        List<DonHangSanPham> dSTatCaSP = donHangSanPhamRepository.findAll();
        return dSTatCaSP.stream()
                .map(donHangSanPhamMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DonHangSanPhamDTO capNhatDonHangSanPham(DonHangSanPhamDTO donHangSanPhamDTO) {
        if (donHangSanPhamDTO == null || donHangSanPhamDTO.getDonHangSanPhamId() == null) {
            throw new IllegalArgumentException("Dữ liệu đơn hàng hoặc ID không hợp lệ");
        }
        Optional<DonHangSanPham> optionalDonHang = donHangSanPhamRepository.findByDonHangSanPhamId(donHangSanPhamDTO.getDonHangSanPhamId());
        if (optionalDonHang.isEmpty()) {
            throw new EntityNotFoundException("Không tìm thấy đơn hàng với ID: " + donHangSanPhamDTO.getDonHangSanPhamId());
        }
        DonHangSanPham donHangSanPham = optionalDonHang.get();
        DonHang donHang = donHangSanPham.getDonHang();

        donHangSanPham.setSanPham(sanPhamMapper.toEntity(donHangSanPhamDTO.getSanPham()));
        donHangSanPham.setSoLuong(donHangSanPhamDTO.getSoLuong());
        donHangSanPham.setDonHang(donHangMapper.toEntity(donHangSanPhamDTO.getDonHang()));

        SanPhamDTO sanPhamDTO = sanPhamMapper.toDto(donHangSanPham.getSanPham());
        donHangSanPham.setTenFile(taoTenFile(sanPhamDTO));

        donHangSanPham = donHangSanPhamRepository.save(donHangSanPham);

        return donHangSanPhamMapper.toDTO(donHangSanPham);
    }

    @Override
    @Transactional
    public List<DonHangSanPhamDTO> themSPVaoDonHangNew(List<DonHangSanPhamDTO> dsDonHangSanPhamDTO) {
        List<DonHangSanPhamDTO> result = new ArrayList<>();
        DonHang donHang = donHangRepository.findByDonHangId(dsDonHangSanPhamDTO.get(0).getDonHang().getDonHangId());
        if (donHang == null) {
            return null;
        }

        for (DonHangSanPhamDTO donHangSanPhamDTO : dsDonHangSanPhamDTO) {
            SanPham sanPham = sanPhamMapper.toEntity(donHangSanPhamDTO.getSanPham());

            DonHangSanPham donHangSanPham = new DonHangSanPham();
            donHangSanPham.setDonHang(donHang);
            donHangSanPham.setSanPham(sanPham);
            donHangSanPham.setTenFile(taoTenFile(sanPhamMapper.toDto(sanPham)));
            donHangSanPham.setSoLuong(donHangSanPhamDTO.getSoLuong());

            donHangSanPhamRepository.save(donHangSanPham);

            DonHangSanPhamDTO savedDonHangSanPhamDTO = donHangSanPhamMapper.toDTO(donHangSanPham);
            result.add(savedDonHangSanPhamDTO);
        }

        return result;
    }

    @Override
    public Optional<DonHangSanPhamDTO> timDonHangSanPham(int donHangSanPhamId) {
        if(donHangSanPhamId <= 0){
            return Optional.empty();
        }
        return donHangSanPhamRepository.findByDonHangSanPhamId(donHangSanPhamId)
                .map(donHangSanPhamMapper::toDTO);
    }

    @Override
    public Optional<List<DonHangSanPhamDTO>> timDonHangTheoDonHangId(int donHangId) {
        if(donHangId <= 0){
            return Optional.empty();
        }
        List<DonHangSanPham> dsDonHangDonHangId = donHangSanPhamRepository.findByDonHangDonHangId(donHangId);
        if(dsDonHangDonHangId.isEmpty()){
            return Optional.empty();
        }
        List<DonHangSanPhamDTO> dsDonHangSanPhamDTO = dsDonHangDonHangId.stream()
                .map(donHangSanPhamMapper::toDTO)
                .collect(Collectors.toList());

        return Optional.of(dsDonHangSanPhamDTO);
    }


    private String taoTenFile(SanPhamDTO sanPham) {
        if (sanPham == null || sanPham.getSku() == null) {
            throw new IllegalArgumentException("SanPham or SKU cannot be null");
        }

        Date date = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");

        String sku = sanPham.getSku();
        String ngayThang = dateFormat.format(date);
        String thoiGian = timeFormat.format(date);

        String tenFile = sku +"-"+ ngayThang + "-" + thoiGian+".xlsx";


        return tenFile;
    }
    private List<Dulieu> listDuLieu ;

    @Override
    @Transactional
    public List<DonHangSanPhamDTO> importFile(List<MultipartFile> dsFileImport, String maLenh, String sku, int viTriEPC) {
        DonHang donHang = donHangRepository.findByMaLenh(maLenh);
        SanPham sanPham = sanPhamRepository.findBySku(sku);
        if(donHang == null){
            throw new CustomException("Don Hang not found",HttpStatus.BAD_REQUEST);
        }
        if(sanPham == null){
            throw new CustomException("SKU not found", HttpStatus.BAD_REQUEST);
        }
        List<DonHangSanPham> dsDonHangSanPham = new ArrayList<>();

        for (MultipartFile multipartFile : dsFileImport) {
            try {
                DonHangSanPham donHangSanPham = readFileExcelExportDonHangSanPham(multipartFile, donHang, sanPham,viTriEPC);

                boolean exists = donHangSanPhamRepository.existsByTenFile(donHangSanPham.getTenFile());
                if (exists) {
                    throw new CustomException("File with the same name already exists: " + donHangSanPham.getTenFile(),HttpStatus.BAD_REQUEST);
                }

                DonHangSanPham savedDhSp =  donHangSanPhamRepository.save(donHangSanPham);
                dsDonHangSanPham.add(savedDhSp);

                for(Dulieu dulieu: listDuLieu) {
                    dulieu.setDonHangSanPham(savedDhSp);
                }
                duLieuRepository.saveAll(listDuLieu);


            } catch (DataIntegrityViolationException e) {
                throw new CustomException("Error saving DonHangSanPham: Duplicate key violation for file: " + multipartFile.getOriginalFilename(), HttpStatus.BAD_REQUEST);
            } catch (Exception e) {
                e.printStackTrace();
                throw new CustomException("Error processing file: " + multipartFile.getOriginalFilename() + e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }

        return dsDonHangSanPham.stream()
                .map(donHangSanPhamMapper::toDTO)
                .collect(Collectors.toList());
    }

    private DonHangSanPham readFileExcelExportDonHangSanPham(MultipartFile multipartFile, DonHang donHang, SanPham sanPham, int viTriEPC) {
        listDuLieu = new ArrayList<>();
        String fileName = multipartFile.getOriginalFilename();
        boolean isXlsx = fileName != null && fileName.endsWith(".xlsx");
        boolean isXls = fileName != null && fileName.endsWith(".xls");

        if (!isXls && !isXlsx) {
            throw new CustomException("File không đúng định dạng (.xls,.xlsx)", HttpStatus.BAD_REQUEST);
        }
        DonHangSanPham donHangSanPham;
        try (InputStream inputStream = multipartFile.getInputStream();
             Workbook workbook = isXlsx ? new XSSFWorkbook(inputStream) : new HSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            int soLuong = sheet.getLastRowNum();

            donHangSanPham = new DonHangSanPham();
            donHangSanPham.setTenFile(fileName);
            donHangSanPham.setSoLuong(soLuong);
            donHangSanPham.setDonHang(donHang);
            donHangSanPham.setSanPham(sanPham);

            for(int i = 1; i <= sheet.getLastRowNum(); i++){
                Row row = sheet.getRow(i);
                if(row == null) continue;

                Dulieu dulieu = new Dulieu();
                StringBuilder sb = new StringBuilder();

                for (int j = 0; j <= row.getLastCellNum(); j++) {
                    Cell cell = row.getCell(j);
                    if (cell != null && j != viTriEPC) {
                        sb.append(getCellStringValue(cell)).append("|");
                    }
                    if (j == viTriEPC) {
                        Cell cellEPC = row.getCell(viTriEPC);
                        if (cellEPC != null) {
                            dulieu.setDataGoc(getCellStringValue(cellEPC));
                            dulieu.setEpc(ConvertToHex.convertToHexadecimal(getCellStringValue(cellEPC)));
                        }
                    }
                }

                dulieu.setSku(sanPham.getSku());
                dulieu.setDonHang(donHang);
                dulieu.setSanPham(sanPham);
                dulieu.setNoiDungBienDoi(sb.toString());

                listDuLieu.add(dulieu);
            }

        } catch (Exception e) {
            throw new CustomException("Lỗi khi đọc file: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return donHangSanPham;
    }


    private String getCellStringValue(Cell cell) {
        if( cell != null){
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getDateCellValue().toString();
                    } else {
                        return String.valueOf(cell.getNumericCellValue());
                    }
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case FORMULA:
                    return cell.getCellFormula();
                default:
                    return "UNKNOWN";
            }
        }
        return "";
    }

}
