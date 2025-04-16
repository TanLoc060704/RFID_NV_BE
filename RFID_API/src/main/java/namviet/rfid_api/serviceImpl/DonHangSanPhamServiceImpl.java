package namviet.rfid_api.serviceImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import namviet.rfid_api.dto.DonHangSanPhamDTO;
import namviet.rfid_api.dto.SanPhamDTO;
import namviet.rfid_api.entity.*;
import namviet.rfid_api.exception.CustomException;
import namviet.rfid_api.mapper.*;
import namviet.rfid_api.repository.*;
import namviet.rfid_api.service.DonHangSanPhamService;
import namviet.rfid_api.utils.ConvertToHex;
import namviet.rfid_api.utils.Encoder;
import org.antlr.v4.runtime.misc.Pair;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
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

    final JdbcTemplate jdbcTemplate;
    private final Encoder encoder;

    /**
    public void batchInsertDulieu(List<Dulieu> dulieus) {
        String sql = "INSERT INTO data (epc, sku, don_hang_id, tid, data_goc, noi_dung_bien_doi, don_hang_san_pham_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Dulieu d = dulieus.get(i);
                ps.setString(1, d.getEpc());
                ps.setString(2, d.getSku());
                ps.setInt(3, d.getDonHang().getDonHangId());
                ps.setString(4, d.getTid());
                ps.setString(5, d.getDataGoc());
                ps.setString(6, d.getNoiDungBienDoi());
                ps.setInt(7, d.getDonHangSanPham().getDonHangSanPhamId());
            }

            public int getBatchSize() {
                return dulieus.size();
            }
        });
    }*/

        @Transactional
        public void batchInsertDulieu(List<Dulieu> dulieus) {
            String sql = "INSERT INTO data (epc, sku, don_hang_id, tid, data_goc, noi_dung_bien_doi, don_hang_san_pham_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            int batchSize = 1000;
            int total = dulieus.size();

            for (int i = 0; i < total; i += batchSize) {
                List<Dulieu> batchList = dulieus.subList(i, Math.min(i + batchSize, total));

                jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int j) throws SQLException {
                        Dulieu d = batchList.get(j);
                        ps.setString(1, d.getEpc());
                        ps.setString(2, d.getSku());
                        ps.setInt(3, d.getDonHang().getDonHangId());
                        ps.setString(4, d.getTid());
                        ps.setString(5, d.getDataGoc());
                        ps.setString(6, d.getNoiDungBienDoi());
                        ps.setInt(7, d.getDonHangSanPham().getDonHangSanPhamId());
                    }

                    @Override
                    public int getBatchSize() {
                        return batchList.size();
                    }
                });
            }
        }

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
        return taoTenFile(sanPham, 0);
    }

    private String taoTenFile(SanPhamDTO sanPham, int index) {
        if (sanPham == null || sanPham.getSku() == null) {
            throw new IllegalArgumentException("SanPham or SKU cannot be null");
        }

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");

        String sku = sanPham.getSku();
        String ngayThang = dateFormat.format(date);
        String thoiGian = timeFormat.format(date);

        String baseFileName = sku + "-" + ngayThang + "-" + thoiGian;
        if (index > 0) {
            baseFileName += " (" + index + ")";
        }
        return baseFileName + ".xlsx";
    }


    private List<Dulieu> listDuLieu ;
    @Override
    @Transactional
    public List<DonHangSanPhamDTO> importFile(List<MultipartFile> dsFileImport, String maLenh, String sku, int viTriEPC, boolean isHex) {
        DonHang donHang = donHangRepository.findByMaLenh(maLenh);
        SanPham sanPham = sanPhamRepository.findBySku(sku);

        if (donHang == null) {
            throw new CustomException("Don Hang not found", HttpStatus.BAD_REQUEST);
        }
        if (sanPham == null) {
            throw new CustomException("SKU not found", HttpStatus.BAD_REQUEST);
        }

        Set<String> existingFiles = new HashSet<>(donHangSanPhamRepository.findAllTenFile()); // Lấy trước danh sách file

        List<DonHangSanPham> dsDonHangSanPham = new ArrayList<>();

        int i = 1;
        for (MultipartFile multipartFile : dsFileImport) {
            List<Dulieu> allDuLieu = new ArrayList<>();


            long start = System.currentTimeMillis();
            try {
                long startR = System.currentTimeMillis();
                DonHangSanPham donHangSanPham = readFileExportDonHangSanPham(multipartFile, donHang, sanPham, viTriEPC, isHex);
                System.out.println("Thời gian đọc file : " + i +" "+ (System.currentTimeMillis() - startR) + "ms");

                if (existingFiles.contains(donHangSanPham.getTenFile())) {
                    throw new CustomException("File already exists: " + donHangSanPham.getTenFile(), HttpStatus.BAD_REQUEST);
                }

                donHangSanPham.setSoLanTao(1);
                dsDonHangSanPham.add(donHangSanPham);
                existingFiles.add(donHangSanPham.getTenFile());

                // Cập nhật DonHangSanPham cho Dulieu
                listDuLieu.forEach(d -> d.setDonHangSanPham(donHangSanPham));
                allDuLieu.addAll(listDuLieu);
                listDuLieu.clear();

            } catch (Exception e) {
                e.printStackTrace();
                throw new CustomException("Error processing file: " + multipartFile.getOriginalFilename(), HttpStatus.BAD_REQUEST);
            }
            System.out.println("Thời gian đọc file: " + (System.currentTimeMillis() - start) + "ms");


            long startluu = System.currentTimeMillis();
            // Lưu hàng loạt để tối ưu tốc độ
            donHangSanPhamRepository.saveAll(dsDonHangSanPham);
            batchInsertDulieu(allDuLieu);
            System.out.println("Thời gian luu du lieu: " +i+" " + (System.currentTimeMillis() - startluu) + "ms");
            i++;
        }


        return dsDonHangSanPham.stream()
                .map(donHangSanPhamMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public List<DonHangSanPhamDTO> themSPVaoDonHangFile(MultipartFile multipartFile, String maLenh) {
        if (maLenh == null || maLenh.isEmpty()) {
            throw new CustomException("Mã lệnh không được để trống", HttpStatus.BAD_REQUEST);
        }

        String fileName = multipartFile.getOriginalFilename();
        boolean isXlsx = fileName != null && fileName.endsWith(".xlsx");
        boolean isXls = fileName != null && fileName.endsWith(".xls");

        if (!isXls && !isXlsx) {
            throw new CustomException("File không đúng định dạng (.xls,.xlsx)", HttpStatus.BAD_REQUEST);
        }

        DonHang donHang = donHangRepository.findByMaLenh(maLenh);
        if (donHang == null) {
            throw new CustomException("Không tìm thấy đơn hàng với mã lệnh: " + maLenh, HttpStatus.BAD_REQUEST);
        }

        List<DonHangSanPham> donHangSanPhams = new ArrayList<>();
        try (InputStream inputStream = multipartFile.getInputStream();
             Workbook workbook = isXlsx ? new XSSFWorkbook(inputStream) : new HSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Cell skuCell = row.getCell(1);
                Cell quantityCell = row.getCell(2);

                if (skuCell == null || quantityCell == null) {
                    throw new CustomException("Dữ liệu không hợp lệ tại dòng: " + (i + 1), HttpStatus.BAD_REQUEST);
                }

                String sku = skuCell.getStringCellValue();
                SanPham sanPham = sanPhamRepository.findBySku(sku);
                if (sanPham == null) {
                    throw new CustomException("Không tìm thấy sản phẩm với SKU: " + sku, HttpStatus.BAD_REQUEST);
                }

                int soLuong = (int) quantityCell.getNumericCellValue();
                if (soLuong <= 0) {
                    throw new CustomException("Số lượng không hợp lệ tại dòng: " + (i + 1), HttpStatus.BAD_REQUEST);
                }

                DonHangSanPham donHangSanPham = new DonHangSanPham();
                donHangSanPham.setDonHang(donHang);
                donHangSanPham.setSoLanTao(1);
                donHangSanPham.setSanPham(sanPham);
                donHangSanPham.setSoLuong(soLuong);
                String tenFile = taoTenFile(sanPhamMapper.toDto(sanPham));

                int index = 1;
                while (donHangSanPhamRepository.existsByTenFile(tenFile)) {
                    tenFile = taoTenFile(sanPhamMapper.toDto(sanPham), index);
                    index++;
                }
                donHangSanPham.setTenFile(tenFile);
                donHangSanPhams.add(donHangSanPham);
                donHangSanPhamRepository.save(donHangSanPham);
            }



        } catch (IOException e) {
            throw new CustomException("Lỗi khi đọc file: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new CustomException("Lỗi không xác định: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return donHangSanPhams.stream()
                .map(donHangSanPhamMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DonHangSanPhamDTO themMotSanPhamVaoDonHang(String sku, String maLenh, int soLuong) {
        if (maLenh == null || maLenh.isEmpty()) {
            throw new CustomException("Mã lệnh không được để trống", HttpStatus.BAD_REQUEST);
        }
        SanPham sanPham = sanPhamRepository.findBySku(sku);
        if (sku == null || sanPham == null) {
            throw new CustomException("Sản phẩm không hợp lệ", HttpStatus.BAD_REQUEST);
        }

        DonHang donHang = donHangRepository.findByMaLenh(maLenh);
        if (donHang == null) {
            throw new CustomException("Không tìm thấy đơn hàng với mã lệnh: " + maLenh, HttpStatus.BAD_REQUEST);
        }



        DonHangSanPham donHangSanPham = new DonHangSanPham();
        donHangSanPham.setSoLanTao(1);
        donHangSanPham.setDonHang(donHang);
        donHangSanPham.setSanPham(sanPham);
        donHangSanPham.setSoLuong(soLuong);
        String tenFile = taoTenFile(sanPhamMapper.toDto(sanPham));
        donHangSanPham.setTenFile(tenFile);
        return donHangSanPhamMapper.toDTO(donHangSanPhamRepository.save(donHangSanPham));
    }

    @Override
    public void deleteDonHangSanPham(int donHangSanPhamId) {
        if (donHangSanPhamId <= 0) {
            throw new CustomException("ID không hợp lệ", HttpStatus.BAD_REQUEST);
        }

        Optional<DonHangSanPham> donHangSanPhamOptional = donHangSanPhamRepository.findByDonHangSanPhamId(donHangSanPhamId);
        if (donHangSanPhamOptional.isEmpty()) {
            throw new EntityNotFoundException("Không tìm thấy đơn hàng sản phẩm với ID: " + donHangSanPhamId);
        }

        DonHangSanPham donHangSanPham = donHangSanPhamOptional.get();
        try {
            // Xóa tất cả dữ liệu liên quan đến donHangSanPham
            List<Dulieu> dulieus = duLieuRepository.findByDonHangSanPhamDonHangSanPhamId(donHangSanPhamId);
            for (Dulieu dulieu : dulieus) {
                duLieuRepository.delete(dulieu);
            }
            // Xóa donHangSanPham
            donHangSanPhamRepository.delete(donHangSanPham);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException("Không thể xóa đơn hàng sản phẩm này vì nó đang được sử dụng", HttpStatus.BAD_REQUEST);
        }
    }


    private DonHangSanPham readFileExportDonHangSanPham(MultipartFile multipartFile, DonHang donHang, SanPham sanPham, int viTriEPC, boolean isHex) {
        listDuLieu = new ArrayList<>();
        String fileName = multipartFile.getOriginalFilename();
        boolean isXlsx = fileName != null && fileName.endsWith(".xlsx");
        boolean isXls = fileName != null && fileName.endsWith(".xls");
        boolean isCsv = fileName != null && fileName.endsWith(".csv");

        if (!isXls && !isXlsx && !isCsv) {
            throw new CustomException("File không đúng định dạng (.xls, .xlsx, .csv)", HttpStatus.BAD_REQUEST);
        }

        DonHangSanPham donHangSanPham = new DonHangSanPham();
        donHangSanPham.setTenFile(fileName);
        donHangSanPham.setDonHang(donHang);
        donHangSanPham.setSanPham(sanPham);

        try {
            if (isCsv) {
                // Đọc CSV
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream(), StandardCharsets.UTF_8))) {
                    String line;
                    int lineNumber = 0;
                    int soLuong = 0;
                    while ((line = reader.readLine()) != null) {
                        String[] cells = line.split(";", -1); // giữ cả ô rỗng
                        if(cells[0].startsWith("Start") || cells[0].startsWith("End")){
                            continue;
                        }

                        Dulieu dulieu = new Dulieu();
                        StringBuilder sb = new StringBuilder();

                        int filter= 1;
                        int partition = cells[7].length() == 6 ? 6 : cells[7].length() == 7 ? 5 : cells[7].length() == 8 ? 4 : cells[7].length() == 9 ? 3 : cells[7].length() == 10 ? 2 : cells[7].length() == 11 ? 1 : 0;
                        long serial = Long.parseLong(cells[9]);

                        SanPham sp = new SanPham();
                        Upc upc1 = new Upc(cells[5], serial);
                        sp.setUpc(upc1);
                        sp.setFilter(filter);
                        sp.setPartition(partition);


                        String epc = Encoder.taoEPCtheoChuanGS1(sp);

                        for (int j = 0; j < cells.length; j++) {
                            if (j != viTriEPC) {
                                sb.append(cells[j]).append("|");
                            } else {
                                dulieu.setDataGoc(epc);
                                dulieu.setEpc(epc);
                            }
                        }
                        dulieu.setSku(sanPham.getSku());
                        dulieu.setDonHang(donHang);
                        dulieu.setSanPham(sanPham);
                        dulieu.setNoiDungBienDoi(sb.toString());

                        listDuLieu.add(dulieu);
                        soLuong++;
                    }
                    donHangSanPham.setSoLuong(soLuong);
                }

            } else {
                // Đọc Excel
                try (InputStream inputStream = multipartFile.getInputStream();
                     Workbook workbook = isXlsx ? new XSSFWorkbook(inputStream) : new HSSFWorkbook(inputStream)) {

                    Sheet sheet = workbook.getSheetAt(0);
                    int soLuong = sheet.getLastRowNum();

                    donHangSanPham.setSoLuong(soLuong);

                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);
                        if (row == null) continue;

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
                                    if (isHex) {
                                        dulieu.setEpc(ConvertToHex.convertToHexadecimal(getCellStringValue(cellEPC)));
                                    } else {
                                        dulieu.setEpc(getCellStringValue(cellEPC));
                                    }
                                }
                            }
                        }

                        dulieu.setSku(sanPham.getSku());
                        dulieu.setDonHang(donHang);
                        dulieu.setSanPham(sanPham);
                        dulieu.setNoiDungBienDoi(sb.toString());

                        listDuLieu.add(dulieu);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
